"""
CITS 3004
Name: Python BlockChain
Authors: Other UWA Lecturers, Alvaro Monsalve
Last Update: 14 AUG 2021

Notes:  solution from https://hackernoon.com/learn-blockchains-by-building-one-117428612f46
"""



# Import libraries
import hashlib
from hashlib import sha256
import json
from time import time
from urllib.parse import urlparse
from uuid import uuid4



# Import flask libraries
import requests
from flask import Flask, jsonify, request





# Blockchain class
class Blockchain:



    # Blockchain constructor
    def __init__(self):

        # a list of transactions
        self.current_transactions = []

        # chain
        self.chain = []

        # Set of nodes (URL/net locations)
        self.nodes = set()

        # Create the genesis block
        self.new_block(previous_hash='1', proof=100)





    def register_node(self, address):
        """
        Add a new node to the list (set) of nodes

        :param address: Address of node. Eg. 'http://192.168.0.5:5000'
        """
        parsed_url = urlparse(address)
        if parsed_url.netloc:
            self.nodes.add(parsed_url.netloc)
        elif parsed_url.path:
            # Accepts an URL without scheme like '192.168.0.5:5000'.
            self.nodes.add(parsed_url.path)
        else:
            raise ValueError('Invalid URL')





    def valid_chain(self, chain):
        """
        Determine if a given blockchain is valid

        :param chain: A blockchain
        :return: True if valid, False if not
        """

        # Last block holder
        last_block = chain[0]

        # Cur index holder
        current_index = 1

        # For every block in chain
        while current_index < len(chain):

            # Get current block
            block = chain[current_index]

            # Print info
            print(f'{last_block}')
            print(f'{block}')
            print("\n-----------\n")

            # Check that the hash of the block is correct
            last_block_hash = self.hash(last_block)
            # Compare hashes
            if block['previous_hash'] != last_block_hash:

                # Return invalid if hash mismatches
                return False

            # Check that the Proof of Work is correct
            if not self.valid_proof(last_block['proof'], block['proof'], last_block_hash):

                # Return invalid if POW is incorrect
                return False

            # Shift last block and index forward
            last_block = block
            current_index += 1


        # Otherwise return true as blockchain is valid
        return True







    def resolve_conflicts(self):
        """
        This is our consensus algorithm, it resolves conflicts
        by replacing our chain with the longest one in the network.

        :return: True if our chain was replaced, False if not
        """

        neighbours = self.nodes
        new_chain = None

        # We're only looking for chains longer than ours
        max_length = len(self.chain)

        # Grab and verify the chains from all the nodes in our network
        for node in neighbours:

            # Retrieve chain info
            response = requests.get(f'http://{node}/chain')

            # If good response
            if response.status_code == 200:

                # Get length and chain
                length = response.json()['length']
                chain = response.json()['chain']

                # Check if the length is longer and if the chain is valid
                if length > max_length and self.valid_chain(chain):
                    max_length = length
                    new_chain = chain

        # Replace our chain if we discovered a new, valid chain longer than ours
        if new_chain:
            self.chain = new_chain
            return True

        # Otherwise return false if chain is the same
        return False




    def new_block(self, proof, previous_hash):
        """
        Create a new Block in the Blockchain

        :param proof: The proof given by the Proof of Work algorithm
        :param previous_hash: Hash of previous Block
        :return: New Block
        """

        # Create block data
        block = {

            # Length of the current chain + 1 
            'index': len(self.chain) + 1,

            # Unix time
            'timestamp': time(),

            # Current transactions that the block is storing
            'transactions': self.current_transactions,

            # Proof of work
            'proof': proof,

            # Either the previous hash or hash value of the last block on the chain 
            'previous_hash': previous_hash or self.hash(self.chain[-1]),
        }

        # Reset the current list of transactions
        self.current_transactions = []

        # Add block to chain
        self.chain.append(block)

        # Return block
        return block




    def new_transaction(self, sender, recipient, amount):
        """
        Creates a new transaction to go into the next mined Block
        Appends the new transaction to the current_transactions list
        The new transaction is a dictionary. 

        :param sender: Address of the Sender
        :param recipient: Address of the Recipient
        :param amount: Amount
        :return: The index of the Block that will hold this transaction
        """
        #------------------------------------------------
        # MY CODE
        #------------------------------------------------

        new_transaction = {
            "sender": sender, 
            "recepient": recipient, 
            "amount": amount
        }

        self.current_transactions.append(new_transaction)

        return self.last_block['index'] + 1




    @property
    def last_block(self):
        return self.chain[-1]




    @staticmethod
    def hash(block):
        """
        Creates a SHA-256 hash of a Block

        :param block: Block
        """

        # We must make sure that the Dictionary is Ordered, or we'll have inconsistent hashes
        block_string = json.dumps(block, sort_keys=True).encode()
        return hashlib.sha256(block_string).hexdigest()




    def proof_of_work(self, last_block):
        """
        This is a simple Proof of Work Algorithm:

        1) Find a number p' (y) such that hash(pp') / hash(xy) contains leading 4 zeroes
        2) Where p (x) is the previous proof, and p' (y) is the new proof
                
        :param last_block: <dict> last Block
        :return: <int> proof
        
        Note: Add the hash of the previous block so PoW cannot be reused.
        """

        last_proof = last_block['proof']
        last_hash = self.hash(last_block)

        #------------------------------------------------
        # MY CODE
        #------------------------------------------------

        # p
        x = last_proof 

        # p', start from 0
        y = 0 

        # While hash value does not have four leading zeroes
        while sha256(f'{x}{y}{last_hash}'.encode()).hexdigest()[:4] != "0000":

            # Keep searching
            y += 1


        # When 4 zero number found, print info
        print('solution value is {}'.format(y))

        # Return proof number
        return y


    @staticmethod
    def valid_proof(last_proof, proof, last_hash):
        """
        Validates the Proof
        
        1) encode the guess into byte in the order of last_proof, proof, and last_hash
        2) calculate the hash using SHA256 in hex form
        3) check the leading four digits are all 0's (i.e., "0000")
        
        :param last_proof: <int> Previous Proof
        :param proof: <int> Current Proof
        :param last_hash: <str> The hash of the Previous Block
        :return: <bool> True if correct, False if not.

        """
        #------------------------------------------------
        # MY CODE
        #------------------------------------------------

        # 1) Encode the guess into byte in the order of last_proof, proof, and last_hash
        block_data = f'{last_proof}{proof}{last_hash}'.encode()

        # 2) Calculate the hash using SHA256 in hex form
        block_hash = sha256(block_data).hexdigest()

        # 3) Check the leading four digits are all 0's (i.e., "0000")
        return (block_hash[:4] == "0000")





# Instantiate the Node
app = Flask(__name__)

# Generate a globally unique address for this node
node_identifier = str(uuid4()).replace('-', '')

# Instantiate the Blockchain
blockchain = Blockchain()


@app.route('/mine', methods=['GET'])
def mine():
    # We run the proof of work algorithm to get the next proof...
    last_block = blockchain.last_block
    proof = blockchain.proof_of_work(last_block)

    # We must receive a reward for finding the proof.
    # The sender is "0" to signify that this node has mined a new coin.
    blockchain.new_transaction(
        sender="0",
        recipient=node_identifier,
        amount=1,
    )

    # Forge the new Block by adding it to the chain
    previous_hash = blockchain.hash(last_block)
    block = blockchain.new_block(proof, previous_hash)

    response = {
        'message': "New Block Forged",
        'index': block['index'],
        'transactions': block['transactions'],
        'proof': block['proof'],
        'previous_hash': block['previous_hash'],
    }
    return jsonify(response), 200




@app.route('/transactions/new', methods=['POST'])
def new_transaction():
    values = request.get_json()

    # Check that the required fields are in the POST'ed data
    required = ['sender', 'recipient', 'amount']
    if not all(k in values for k in required):
        return 'Missing values', 400

    # Create a new Transaction
    index = blockchain.new_transaction(values['sender'], values['recipient'], values['amount'])

    response = {'message': f'Transaction will be added to Block {index}'}
    return jsonify(response), 201


@app.route('/chain', methods=['GET'])
def full_chain():
    response = {
        'chain': blockchain.chain,
        'length': len(blockchain.chain),
    }
    return jsonify(response), 200


@app.route('/nodes/register', methods=['POST'])
def register_nodes():
    values = request.get_json()

    nodes = values.get('nodes')

    if nodes is None:
        return "Error: Please supply a valid list of nodes", 400

    for node in nodes:
        blockchain.register_node(node)

    response = {
        'message': 'New nodes have been added',
        'total_nodes': list(blockchain.nodes),
    }
    return jsonify(response), 201


@app.route('/nodes/resolve', methods=['GET'])
def consensus():
    replaced = blockchain.resolve_conflicts()

    if replaced:
        response = {
            'message': 'Our chain was replaced',
            'new_chain': blockchain.chain
        }
    else:
        response = {
            'message': 'Our chain is authoritative',
            'chain': blockchain.chain
        }

    return jsonify(response), 200


if __name__ == '__main__':
    from argparse import ArgumentParser

    parser = ArgumentParser()
    parser.add_argument('-p', '--port', default=5000, type=int, help='port to listen on')
    args = parser.parse_args()
    port = args.port

    app.run(host='127.0.0.1', port=port)
