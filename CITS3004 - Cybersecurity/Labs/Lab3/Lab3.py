# CITS 3004
# Name: RSA Key Generation, Encryption, Decryption
# Authors: Alvaro Monsalve, Other UWA Lecturers
# Last Update: 19 JUL 2021
# Requirements: Python 3.8
# Run using:   cls && python3 Lab3.py


# Libraries
import math
from sympy.ntheory import factorint


################### Provided helper functions

# Extended Euclidean Algorithm (Recursive)
# GCD value is in first index of tuple
def egcd(a, b):
    if a == 0:
        return (b, 0, 1)
    g, y, x = egcd(b % a, a)
    return (g, x - (b // a) * y, y)


# Modular Inverse Function
def modinv(a, m):
    g, x, y = egcd(a, m)
    if g != 1:
        raise Exception('No modular inverse')
    return x % m





############### My helper functions #########

# Arg 1: Step value
# Arg 2: Description
def print_step(step_val, desc):
    print("### Step " + str(step_val) + ": " + desc)


# Print a value as hex
# Arg 1: Variable Name
# Arg 2: Value of Variable
def print_as_hex(name, value):
    print(name + ": 0x" + format(value, '02x'))

# Print comparison
# Arg 1: Value 1
# Arg 2: Value 2
def print_comp(val1, val2):
    print("Matches expected: " + str(val1 == val2))

# Helper function for Step 3
# Eulers Totient Function
def phi(p, q):
    return ((p - 1) * (q - 1))

# Helper function for Step 4
# Get integer e relatively/mutually/co prime to phi(n)
def get_an_e(phi_of_n):
    
    # Holder
    e = 2

    # Go from 2 to phi(n)  (as 1 < e < phi(n))
    for cur_e in range(2, phi_of_n):
        
        # If conditions satified
        if(egcd(cur_e, phi_of_n)[0] == 1):
            # Save current
            e = cur_e
            # Stop processing
            break
        else:
            # Else if conditions not satisfied,
            # increment and keep going
            e += 1

    # Return final value
    return e



# Encrypt a Message using RSA
# Arg1 = Plaintext message
# Arg2 = E value (The Public Exponent)
# Arg3 = N value (The Modulus)
# Arg4 = If true, print result
def encrypt(pm, e, n, doPrint):

    # Convert message to hex
    pm = pm.encode("utf-8").hex()
    pm = int(pm, 16)
    
    # Do: C = P^E mod N (Ciphertext = Plaintext raised to E modulo N)
    c = pow(pm, e, n)

    # Print ciphertext 
    if doPrint:
        print("Encrypted: 0x" + format(c, '02x'))

    # Return ciphertext 
    return c


# Decrypt a Message using RSA
# Arg1 = Ciphertext message
# Arg2 = D value (The Private Exponent)
# Arg3 = N value (The Modulus)
# Arg4 = If true, print result
def decrypt(cm, d, n, doPrint):

    # Do: P = C^D mod N (Plaintext = Ciphertext raised to D modulo N)
    pm_raw = pow(cm, d, n)

    # Convert from hex to string
    pm = bytes.fromhex(hex(pm_raw)[2:]).decode("utf-8")

    # Print plaintext 
    if doPrint:
        print("Decrypted: ", pm)

    # Return plaintext
    return pm







###################### TASKS #####################


# Part A
def task_1_part_a():

    # Title
    print("\n########## Part A ########## ")


    # 1. Select two large (> 1024 bits) primes p, q (Basic Mode)
    # Generates Key Size (n and d) each 2048 bits
    # Acquired from: https://www.mobilefish.com/services/rsa_key_generation/rsa_key_generation.php
    # Inputted as hex but python will interpret/save as decimal
    # 512 bits each
    # ALTERNATE WAY:
    # If you also have pycryptodome installed you can just do the following in your python code
    # from Crypto.Util.number import getPrime
    # p = getPrime()
    # q = getPrime()

    p = 0xd7c2657cb20221fdb77f884b78c19b493b1774f4c7d825069de014605dd1870ca5f98167fd30e13c08533993483232bc21c2403c175b96e2679463278e282a09
    q = 0xb395adab1589b32054f5b3f35e607a81715d7355166720d0a6ae5312b0bfa6af5c8b3c9404f222b5ddc1e52c4e258627c87dd80743129f15fd0c144fc332b875
    print_step(1, "Got primes p and q")
    #print_as_hex("p", p)
    #print_as_hex("q", q)
    print("")


    # 2. Compute modulus value (n) = p*q
    n = p*q
    print_step(2, "Got n (p*q)   (Public/Private Key - Modulus)")
    #print_as_hex("n", n)
    expected_n = 0x975b1379d1a127ec3f9250ffae4b4a2b258c1efa419f0417002724229cfaf56859ece3f96e182e5e6df56d21da75ca8968857a8b128014af1ef68546a54b565bc4d4c67dd37b06716eccf987cf8cfbaee33d3e9a4b58cd71e5eeb72a77da77a711ae224a61f6e13259182766cb803307de359496a55807e85c31c7e4e753ae1d
    print_comp(n, expected_n)
    print("")


    # 3. Compute ϕ(n) and check
    phi_of_n = phi(p, q)
    print_step(3, "Got phi of n")
    #print_as_hex("phi_of_n", phi_of_n)
    expected_phi_of_n = 0x975b1379d1a127ec3f9250ffae4b4a2b258c1efa419f0417002724229cfaf56859ece3f96e182e5e6df56d21da75ca8968857a8b128014af1ef68546a54b565a397cb3560bef31536257bd48f86ae5e436c856506d19879aa1604fb7694949eb0f29644e5fd3dd40730308a735287a23f3f57c534ae9d1eff791506d95f8cba0
    print_comp(phi_of_n, expected_phi_of_n)
    print("")


    # 4. Find/set integer e relatively prime to ϕ(n)
    print_step(4, "Get integer e (The Public Exponent) (e is relatively prime to ϕ(n))")
    e = get_an_e(phi_of_n)
    #print_as_hex("e", e)
    print("")


    # 5. Compute d  =(e^-1) mod phi
    print_step(5, "Get d (The Private Exponent)")
    # Example:  6^8   mod 5    = pow(6, 8, 5)
    # So:     (e^-1)  mod phi  = pow(e, -1, phi)
    d = pow(e, -1, phi_of_n)
    #print_as_hex("d", d)
    expected_d = 0x64e76251366b6ff2d50c35ffc98786c76e5d69fc2bbf580f556f6d6c68a74e45914897fb9ebac994494e48c13c4e870645ae51b20c55631f69f9ae2f18dce43c265322395d4a20e2418fd385faf1ee9824858ee048bbafbc6b958a7a4630dbf20a1b98343fe293804cacb06f78c5a6c2a2a3a83787468bf54fb6359e63fb326b
    print_comp(d, expected_d)
    print("")


    # # Encryption/Decryption
    print("### Encryption/Decryption")

    # Message
    m = "Send the ships at 3."
    print("Message: ", m)

    # Encrypt
    cm = encrypt(m, e, n, True)

    # Decrypt
    decrypt(cm, d, n, True)

    return




# Part B
def task_1_part_b():

    # Title
    print("\n########## Part B ########## ")

    # Given values
    n = 0xe726db87d69e55ef7e96ddcd5f837d29
    e = 0x010001 
    m = "A top secret!" # Labsheet
    #m = "Meeting at 4" # Quiz

    # Get values from factorization to separate n into p and q
    # Acquired using special library:
    # print(str(factorint(n)))
    p = 16750243642551145271
    q = 18343221356872753823
    print("Factorized given 'n' to get p={} and q={}".format(p, q))

    # Check p and q
    check_val = n - p*q
    print("Checking 'p' and 'q': n - p*q = ", check_val, "  Correct: ", check_val == 0)

    # Compute ϕ(n) and check
    phi_of_n = phi(p, q)

    # Compute d  =(e^-1) mod phi   (The Private Exponent)")
    d = pow(e, -1, phi_of_n)

    # # Encryption/Decryption
    print("### Encryption/Decryption")

    # Show message
    print("Message: ", m)

    # Encrypt
    cm = encrypt(m, e, n, True)

    # Decrypt
    decrypt(cm, d, n, True)

    return






# Part C
def task_1_part_c():

    # Title
    print("\n########## Part C ########## ")

    # Given values
    #cm=0xcb42f3711ecfef4ac0b25aa2f4c0f48f # Original
    cm=0xa3fe6baa133e012833a38f592f265634 # Quiz
    d=0xd34514c9a743b8ce1c06d2ae5a03aab1 # Original
    # d=0xd34514c9a743b8ce1c06d2ae5a03aab0 # Testing what happens if you corrupt last bt
    n=0xe726db87d69e55ef7e96ddcd5f837d29

    # Print values
    print_as_hex("cm", cm)
    print_as_hex("d", d)
    print_as_hex("n", n)

    # Decrypt
    print("Decryption:")
    decrypt(cm, d, n, True)


    



# Part D
def task_1_part_d():

    # Title
    print("\n########## Part D ########## ")
    
    # Message
    m = "I owe you $4000"

    # Given values
    d=0xd34514c9a743b8ce1c06d2ae5a03aab1 # Private exponent
    n=0xe726db87d69e55ef7e96ddcd5f837d29

    # We use the private (d) like the public (e)
    # - Make signature
    # -- Encryption formula: pow(pm, e, n)
    # -- Use d value as e value in signing!!! (use private instead of public)
    signed_m = encrypt(m, d, n, True)

    # Check value
    expected_signed_m=0x2e7de46cce98bb21c400ac8eb535e004
    check = str(signed_m==expected_signed_m)
    print("Matches expected: " + check + "\n")




# Part A
def task_2_part_a(g, a, b, p):

    # Title
    print("\n########## Part A ########## ")
    # Derive the shared key between Bob and Alice.
    print("Variables: g: ", g, " a:", a, " b:", b, " p:", p)
    # 1. Alice calculates g^a mod p for the parameters (g, a, p) = (10, 5, 541)
    alice_val = pow(g, a) % p
    print("1. Alice calculated: g^a mod p = ", g, "^", a, "mod", p, " = ", alice_val)

    # 2. Alice sends g^a mod p to Bob
    print("2. Alice sent g^a mod p = ", alice_val, " to Bob")

    # 3. Bob calculates g^b mod p and k using (b, p) = (7, 541)
    bob_val = pow(g, b) % p
    print("3. Bob calculated: g^b mod p = ", g, "^", b, "mod", p, " = ", bob_val)

    # 4. Bob sends g^b mod p to Alice
    print("4. Bob sent g^b mod p = ", bob_val, " to Alice")

    # 5. Alice computes her key by raising Bob's value to her a and doing mod p
    step5str = "5. Alice computes her key = k = (g^b mod p)^a mod p = "
    step5str += "(bob_val)^a mod p = " + str(bob_val) + "^" + str(a) + " mod " + str(p) 
    alice_key = pow(bob_val,a) % p
    step5str += " = " + str(alice_key)
    print(step5str)

    # 6. Bob computes his key by raising Alice's value to his b and doing mod p:
    step6str = "6. Bob computes his key = k = (g^a mod p)^b mod p = "
    step6str += "(alice_val)^b mod p = " + str(alice_val) + "^" + str(b) + " mod " + str(p) 
    bob_key = pow(alice_val,b) % p
    step6str += " = " + str(bob_key)
    print(step6str)

    # 7. Check that Bob and Alice derive the same k.
    check = (alice_key == bob_key)
    print("7. Alice and Bob derived the same key = " + str(check))

    # Extra
    k_check = str(pow(g, a * b) % p)
    print("Key Check: g^ab mod p = ", g, "^(", a, "x", b, ") mod ", p, " = " + k_check)

    return alice_val, bob_val

# Part B
def task_2_part_b(p, alice_val, bob_val, c):

    # Title
    print("\n########## Part B ########## ")
    # Man In the Middle Attack
    # Now calculate the shared keys between k1 = (Alice, Eve) and k2 = (Eve, Bob) 
    # given that secret value of Eve c=5. 
    print("Variables:", "p:" , p,  "alice_val: ", alice_val, " bob_val:", bob_val, " c:", c)

    # Eve calculates K1 = k_1  =(g^a mod p)^c mod p   =g^ac mod p 
    print("Eve calculates k1 = (g^a mod p)^c mod p = (alice_val)^c mod p:")
    k1 = pow(alice_val, c) % p
    print(alice_val, "^", c, "mod", p, "= ", k1)


    # Eva calculates K2 = k_2  = (g^b mod p )^c mod p   =g^bc mod p 
    print("Eve calculates k2 = (g^b mod p)^c mod p = (bob_val)^c mod p:")
    k1 = pow(bob_val, c) % p
    print(bob_val, "^", c, "mod", p, "= ", k1)






# Part C
def task_2_part_c(g, a, b, A, B):

    # Title
    print("\n########## Part C ########## ")
    # Mitigate the Man In the Middle Attacks
    # One approach for mitigating an attack is to ensure that Alice and Bob verify their identities. 
    # To do so, they must sign their message using the public and private keys. 

    # Modulus value
    n = 0xe2a200a6d2862045e95f9281d400f670ecf1fa51a27792b6671fa7e1d4c9b03f3224844e9854fea710fac7256b2c55da5759dfe810f16804d863f2bdc6a598d988b7e2eab4ce3c47b374a5b1e67f914804056c2a6a6c2db84281f3264a8ef3299b0065515f20c460304e48f019b9b2e0e020cf69e3d37daf0158ba1568555a87f69c20e4b1d58f6c7fb83f0843d230d478ec63497062671d5cdee4d91f3fdaf7fc751448add7cd207b38b9ac296e18a2dca1b64d9235de5c82f32db338af44e54b65e65532c39ee8fcb59431d2f193ab5fbcc1252719d529e54d6299231ce431aaf516f3ac3fce4929095103c8c012168095b73132a904650a44d2cbc7d4fad3
    
    # Bob's Private Key
    # Private Key (d,n) of 128 bits
    d = 0x935add2f8f0fb01ea0bae7ecc9593e7f4c5fa0a0d8998b23cb5e87c17be621173411b82e79d47722a6f6bfa023dda8ce863d1c463e2cdf182914289550b93e0d366abd428a2ba79ee3dc400ce35a21aa62c466490f94f9ccf785b5e1cfbe99b398456fb7efcb16e44f19375b6209dd736b74820eaa06515c4010aeea7b3bd216f0e040dc2a3ab7b6be5995d76433ed6d61b1ae01f892e16b2511d80803978f3471dd5845684fe1cb25fd1b6721b8ac38a44da41be248cb7cb43f425621be3c9e888b9a2303547b21195959d4c061d5365643868b68e0bbf2fd8865d6ef80f8af141d832201e2b46f522b45a0b4519a654f627d5321239058f67fad4502a1d5eb

    # Bob's Public Key
    # Public Key (e,n) of 128 bits
    e = 0x0D88C3

    # 1. Alice sends g^a (same as previous tasks) and A to Bob.
    alice_v = pow(g, a)
    print("1. Alice sends g^a ({}^{} = {}) and A={} to Bob".format(g, a, alice_v, A))

    # 2. Bob sends back B and g^b 
    bob_v = pow(g, b)
    print("2. Bob sends back:")
    print(" - B = ", B)
    print(" - g^b = ", g, "^", b, " = ", bob_v)

    
    # 3. Bob signs (encrypts) the message SIGB(m) using his PRIVATE key.
    print("3. Bob creates and sends signature")
    # - Make message
    raw_m = "({}, {}, {})".format(alice_v, bob_v, A)
    print("- Bob generates message = m=(g^a, g^b, A) = " + raw_m)
    # - Make signature
    # -- Encryption formula: pow(pm, e, n)
    # -- Use d value as e value in signing!!! (use private instead of public)
    sig_b_m = encrypt(raw_m, d, n, False)
    print("- Bob sends SIGB(m) = 0x" + format(sig_b_m, '02x'))

    # 4. Alice uses the PUBLIC key of Bob to verify (decrypt) the signature SIGB(m)
    print("4. Alice does verification")
    # - Do verification
    # -- Decryption formula: pw(cm, d, n):
    # -- Use e value as d value in verifying!!! (use public instead of private)
    # -- Ciphertext is signature!
    result = decrypt(sig_b_m, e, n, False)
    print("- Alice's verification produced: ", result)
    print("- It matches to original: " + str(result == raw_m))

    return




############### CALL FUNCTIONS ###########
# Title
print("###################### CITS3004 Lab 3 ######################")

print("###################### Task 1 - RSA ######################")
task_1_part_a()
task_1_part_b()
task_1_part_c()
task_1_part_d()

print("\n###################### Task 2 - DH ######################")
g = 10
a = 5
b = 7
p = 541
alice_val, bob_val = task_2_part_a(g, a, b, p)
task_2_part_b(p, alice_val, bob_val, 5)
A = 2791
B = 8507
task_2_part_c(g, a, b, A, B)



# Quiz question
# s = 0x345b2ad16ed459ec90e92c4402384cf126cee0693db3ceaa5e1165cc02fa4f0f
# e = 0x010001
# n = 0xdcbffe3e51f62e09ce7032e2677a78946a849dc4cdde3a4d0cb81629242fb1a5
# result = decrypt(s, e, n, False)
# print("\n- Verification produced: ", result)