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

    # 1. Select two large (> 1024 bits) primes p, q (Basic Mode)
    # Generates Key Size (n and d) each 2048 bits
    p = 0xf5e78c310e4ed610bc47dc37c173a9d2ab58813dc60c51814c1becef059ee0f75f205d0317e1de634085c2895560fbcf44626f963b0ff39708b74f9e4437dbbe2a8a23234014ad1eaf6e836e73ca707be8aff238a8a73ffecd6be7e7cba8fa269c993a0974e3f904091d4938120d51aeb67fcd28b7911c68d1a975dc98236dd3
    q = 0xebefe8f9eca8c8301d0f23defac1eb0c5c52d12f514ecef4a7a53a8429c38e2d55f89635a548cc9bf088c1e29f4fcc96f2b6ad66cc24f3bc213e50234abbe505f87d0a99e5bc0a11c9734b638e6a2af5c3ee1e506064276d4e50db92cb7d5a6b8f9a5f79646f1db2093b484f454c898f62951526edcd3d14f0a56a2e84e71f01

    # 2. Compute modulus n = p*q
    n = p * q

    # 3. Compute ϕ(n) = (p - 1) * (q - 1)
    #         ϕ is Euler’s Totient Function
    phi = (p - 1) * (q - 1)

    # 4. Pick an integer e relatively prime to ϕ(n)
    #               gcd(e, ϕ(n)) = 1; 1 < e < ϕ(n)
    e = 0x0D88C3
    print("Public Key - Exponent (e): ", format(e, '02x'))

    # 5. Compute d such that e * d = 1 mod ϕ(n)
    d = modinv(e, phi)

    # n_sol = 0xe2a200a6d2862045e95f9281d400f670ecf1fa51a27792b6671fa7e1d4c9b03f3224844e9854fea710fac7256b2c55da5759dfe810f16804d863f2bdc6a598d988b7e2eab4ce3c47b374a5b1e67f914804056c2a6a6c2db84281f3264a8ef3299b0065515f20c460304e48f019b9b2e0e020cf69e3d37daf0158ba1568555a87f69c20e4b1d58f6c7fb83f0843d230d478ec63497062671d5cdee4d91f3fdaf7fc751448add7cd207b38b9ac296e18a2dca1b64d9235de5c82f32db338af44e54b65e65532c39ee8fcb59431d2f193ab5fbcc1252719d529e54d6299231ce431aaf516f3ac3fce4929095103c8c012168095b73132a904650a44d2cbc7d4fad3
    # d_sol = 0x935add2f8f0fb01ea0bae7ecc9593e7f4c5fa0a0d8998b23cb5e87c17be621173411b82e79d47722a6f6bfa023dda8ce863d1c463e2cdf182914289550b93e0d366abd428a2ba79ee3dc400ce35a21aa62c466490f94f9ccf785b5e1cfbe99b398456fb7efcb16e44f19375b6209dd736b74820eaa06515c4010aeea7b3bd216f0e040dc2a3ab7b6be5995d76433ed6d61b1ae01f892e16b2511d80803978f3471dd5845684fe1cb25fd1b6721b8ac38a44da41be248cb7cb43f425621be3c9e888b9a2303547b21195959d4c061d5365643868b68e0bbf2fd8865d6ef80f8af141d832201e2b46f522b45a0b4519a654f627d5321239058f67fad4502a1d5eb
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
    print("Decrypted Message: ", bytes.fromhex(hex(dc)[2:]).decode("utf-8"))

    return


rsa_gen()