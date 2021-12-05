# CITS 3004
# Name: RSA
# Authors: Alvaro Monsalve, Other UWA Lecturers
# Last Update: 19 JUL 2021
# Requirements: Python 3.8


# extended euclidean algo
def egcd(a, b):
    if a == 0:
        return (b, 0, 1)
    g, y, x = egcd(b % a, a)
    return (g, x - (b // a) * y, y)


# modular inverse function
def modinv(a, m):
    g, x, y = egcd(a, m)
    if g != 1:
        raise Exception('No modular inverse')
    return x % m


# RSA Generator Function
def rsa_gen():

    # WRITE YOUR CODE BELOW


    # 1. Select two large (> 1024 bits) primes p, q (Basic Mode)
    # Generates Key Size (n and d) each 2048 bits
    p = 0x0
    q = 0x0

    # 2. Compute modulus n = p*q


    # 3. Compute ϕ(n)


    # 4. Pick an integer e relatively prime to ϕ(n)
    e = 0x0
    print("Public Key - Exponent (e): ", format(e, '02x'))

    # 5. Compute d


    print("Public/Private Key - Modulus (n): ", format(n, '02x'))
    print("Private Key - Exponent (d): ", format(d, '02x'))

    # Encrypt a Message
    m = "Send the ships at 3."
    print("Plain Input Message: ", m)
    m = m.encode("utf-8").hex()
    m = int(m, 16)

    c = pow(m, e, n)
    print("Encrypted Message: ", format(c, '02x'))

    # Decrypt a Message
    dc = pow(c, d, n)
    print("Decrypted Message: ", bytes.fromhex(hex(dc)[2:]).decode("utf-8") )

    return


rsa_gen()
