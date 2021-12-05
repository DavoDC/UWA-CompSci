# CITS 3004
# Name: RSA
# Authors: Alvaro Monsalve, Other UWA Lecturers
# Last Update: 02 AUG 2021
# Requirements: Python 3.8

# Encrypt Message m, Given Public Key (e,n) of 128 bits
def q3():
    m = "Meeting at 4"
    e = 0x010001
    n = 0xe726db87d69e55ef7e96ddcd5f837d29

    m = m.encode("utf-8").hex()
    m = int(m, 16)

    c = pow(m, e, n)
    print(" Encrypted Message: ", format(c, '02x'))
    # c_sol = 0xc7e20e909cc6133b79365adbcff27f65
    return


# Decrypt Message c, Given Private Key (d,n) of 128 bits
def q7():
    c = 0xa3fe6baa133e012833a38f592f265634
    d = 0xd34514c9a743b8ce1c06d2ae5a03aab1
    n = 0xe726db87d69e55ef7e96ddcd5f837d29

    # original message is "At 2PM"
    orig = pow(c, d, n)
    print(" Decrypted Message: ", bytes.fromhex(hex(orig)[2:]).decode("utf-8"))

    return


# Verifying Signed Message s, Given Public Key (e,n) of 128 bits
def q8():
    s = 0x345b2ad16ed459ec90e92c4402384cf126cee0693db3ceaa5e1165cc02fa4f0f
    e = 0x010001
    n = 0xdcbffe3e51f62e09ce7032e2677a78946a849dc4cdde3a4d0cb81629242fb1a5

    orig = pow(s, e, n)
    print(" Verifying Message: ", bytes.fromhex(hex(orig)[2:]).decode("utf-8")) # "I am Eve!"

    return


print("Quiz Question 3")
q3()

print("Quiz Question 7")
q7()

print("Quiz Question 8")
q8()

#print("Task 1e")
#t1e()
