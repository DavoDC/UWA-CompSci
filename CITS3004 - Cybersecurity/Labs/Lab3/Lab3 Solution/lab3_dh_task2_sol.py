def t2a():
    g = 10
    p = 541
    a = 5
    b = 7

    gA = pow(g, a) % p
    gB = pow(g, b) % p

    # Bob Calculates k
    k_b = pow(gA, b) % p

    # Alice Calculates k
    k_a = pow(gB, a) % p

    if k_b == k_a:
        print("Alice and Bob share the same k =", k_a)
    else:
        print("Alice k =", k_a, "Bob k =", k_b)


def t2b():
    g = 10
    p = 541
    a = 5
    b = 7
    c = 5

    gA = pow(g, a) % p
    gB = pow(g, b) % p
    gC = pow(g, c) % p

    # Bob Calculates k
    k2_b = pow(gC, b) % p

    # Alice Calculates k
    k1_a = pow(gC, a) % p

    # Eve Calculates k
    k1_c = pow(gA, c) % p
    k2_c = pow(gB, c) % p


    if k1_a == k1_c and k2_b == k2_c:
        print("Alice and Eve share the same k1 =", k1_a, "Bob and Eve share the same k2 =", k2_b)
    else:
        print("Alice k1_a =", k1_a,
              "| Eve k1_c =", k1_c,
              "| Eve k2_c =", k2_c,
              "| Bob k2_c =", k2_b)


# Signing Message m, Given Private Key (d,n) of 128 bits
def RSAsign_BobPrivateKey(m):
    d = 0x935add2f8f0fb01ea0bae7ecc9593e7f4c5fa0a0d8998b23cb5e87c17be621173411b82e79d47722a6f6bfa023dda8ce863d1c463e2cdf182914289550b93e0d366abd428a2ba79ee3dc400ce35a21aa62c466490f94f9ccf785b5e1cfbe99b398456fb7efcb16e44f19375b6209dd736b74820eaa06515c4010aeea7b3bd216f0e040dc2a3ab7b6be5995d76433ed6d61b1ae01f892e16b2511d80803978f3471dd5845684fe1cb25fd1b6721b8ac38a44da41be248cb7cb43f425621be3c9e888b9a2303547b21195959d4c061d5365643868b68e0bbf2fd8865d6ef80f8af141d832201e2b46f522b45a0b4519a654f627d5321239058f67fad4502a1d5eb
    n = 0xe2a200a6d2862045e95f9281d400f670ecf1fa51a27792b6671fa7e1d4c9b03f3224844e9854fea710fac7256b2c55da5759dfe810f16804d863f2bdc6a598d988b7e2eab4ce3c47b374a5b1e67f914804056c2a6a6c2db84281f3264a8ef3299b0065515f20c460304e48f019b9b2e0e020cf69e3d37daf0158ba1568555a87f69c20e4b1d58f6c7fb83f0843d230d478ec63497062671d5cdee4d91f3fdaf7fc751448add7cd207b38b9ac296e18a2dca1b64d9235de5c82f32db338af44e54b65e65532c39ee8fcb59431d2f193ab5fbcc1252719d529e54d6299231ce431aaf516f3ac3fce4929095103c8c012168095b73132a904650a44d2cbc7d4fad3
    m = int(m.encode("utf-8").hex(), 16)
    s = pow(m, d, n)
    #print("Signed Message: ", format(s, '02x'))
    return s


# Verifying Signed Message s, Given Public Key (e,n) of 128 bits
def RSAverify_BobPublicKey(s):
    e = 0x0D88C3
    n = 0xe2a200a6d2862045e95f9281d400f670ecf1fa51a27792b6671fa7e1d4c9b03f3224844e9854fea710fac7256b2c55da5759dfe810f16804d863f2bdc6a598d988b7e2eab4ce3c47b374a5b1e67f914804056c2a6a6c2db84281f3264a8ef3299b0065515f20c460304e48f019b9b2e0e020cf69e3d37daf0158ba1568555a87f69c20e4b1d58f6c7fb83f0843d230d478ec63497062671d5cdee4d91f3fdaf7fc751448add7cd207b38b9ac296e18a2dca1b64d9235de5c82f32db338af44e54b65e65532c39ee8fcb59431d2f193ab5fbcc1252719d529e54d6299231ce431aaf516f3ac3fce4929095103c8c012168095b73132a904650a44d2cbc7d4fad3
    orig = pow(s, e, n)
    #print(" Verifying Message: ", bytes.fromhex(hex(orig)[2:]).decode("utf-8"))
    return bytes.fromhex(hex(orig)[2:]).decode("utf-8")


def t2c():
    import hashlib
    g = 10
    p = 541
    a = 5
    b = 7
    A = 2791
    B = 8507
    # E = 4617

    gA = pow(g, a) % p
    gB = pow(g, b) % p

    # Bob sends back gB, B and SIGB(gA, gB, A)
    m = str(gA) + str(gB) + str(A)
    hash1B = hashlib.sha1(m.encode()).hexdigest()  #.upper()
    print("Bob Hash =", hash1B)
    SIGB = RSAsign_BobPrivateKey(hash1B)
    print("Bob Signed Message SIGB =", format(SIGB, '02x'))

    # Alice receives m and SIGB. verifies SIGB(m)
    hasb1B_a = hashlib.sha1(m.encode()).hexdigest()
    hasb1B_a_v = RSAverify_BobPublicKey(SIGB)
    if hasb1B_a == hasb1B_a_v:
        print("Verification Successful!, Hash Message =", hasb1B_a)
    else:
        print("Verification Unsuccessful!, hash1B_a =", hasb1B_a, "| hash1B_a_v =", hasb1B_a_v)

print("Task 2a")
t2a()

print("Task 2b")
t2b()

print("Task 2c")
t2c()
