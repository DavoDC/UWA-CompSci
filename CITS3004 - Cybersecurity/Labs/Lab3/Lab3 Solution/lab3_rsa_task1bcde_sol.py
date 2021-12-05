# CITS 3004
# Name: RSA
# Authors: Alvaro Monsalve, Other UWA Lecturers
# Last Update: 19 JUL 2021
# Requirements: Python 3.8

# Encrypt Message m, Given Public Key (e,n) of 128 bits
def t1b():
    m = "A top secret!"
    e = 0x010001
    n = 0xe726db87d69e55ef7e96ddcd5f837d29

    m = m.encode("utf-8").hex()
    m = int(m, 16)

    c = pow(m, e, n)
    print(" Encrypted Message: ", format(c, '02x'))
    # c_sol = 0x59882ea5273565c1c460a2b3f4958e7a
    return


# Decrypt Message c, Given Private Key (d,n) of 128 bits
def t1c():
    c = 0xcb42f3711ecfef4ac0b25aa2f4c0f48f
    d = 0xd34514c9a743b8ce1c06d2ae5a03aab1
    n = 0xe726db87d69e55ef7e96ddcd5f837d29

    # original message is "Maximum Message!"
    orig = pow(c, d, n)
    print(" Decrypted Message: ", bytes.fromhex(hex(orig)[2:]).decode("utf-8"))

    return


# Signing Message m, Given Private Key (d,n) of 128 bits
def t1d():
    m = "I owe you $4000"
    d = 0xd34514c9a743b8ce1c06d2ae5a03aab1
    n = 0xe726db87d69e55ef7e96ddcd5f837d29


    m = int(m.encode("utf-8").hex(), 16)
    
    # I owe you $4000 -> 2e7de46cce98bb21c400ac8eb535e004
    s = pow(m, d, n)
    print(" Signed Message: ", format(s, '02x'))

    return


# Verifying Signed Message s, Given Public Key (e,n) of 128 bits
def t1e():
    s = 0x9ec0be5129d418c8dc81d6fa9a9a6ec3
    e = 0x010001
    n = 0xe726db87d69e55ef7e96ddcd5f837d29

    orig = pow(s, e, n)
    print(" Verifying Message: ", bytes.fromhex(hex(orig)[2:]).decode("utf-8")) # "I am Eve!"

    return


print("Task 1b")
t1b()

print("Task 1c")
t1c()

print("Task 1d")
t1d()

print("Task 1e")
t1e()
