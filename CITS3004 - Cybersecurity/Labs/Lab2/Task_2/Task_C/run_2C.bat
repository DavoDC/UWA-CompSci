
:: Less output
@echo off

:: Clear terminal
cls

:: Remove previous
@REM rm *.bin

:: Space
echo.

echo "********************* Plaintext:"
sed -n 1,1p 2C_plain.txt

echo.
echo.




@REM echo "### 1 - TRYING ECB    (AES-128)"

@REM echo.
@REM echo "Encrypt:"
@REM @echo on
@REM openssl enc -e -aes-128-ecb -in 2C_plain.txt -out ECB-cipher.bin^
@REM     -K bdb264c1400b07679dc4a1553eeb6193 
@REM @echo off
@REM echo.
@REM echo "ECB-cipher.bin - part:"
@REM sed -n 1,1p ECB-cipher.bin

@REM echo.
@REM echo.
@REM echo "Change a single bit (any bit) of the 0x30th/48th byte in the encrypted file."
@REM @pause 

@REM echo.
@REM echo "Decrypt:"
@REM @echo on
@REM openssl enc -d -aes-128-ecb -in ECB-cipher.bin -out ECB-output.txt^
@REM     -K bdb264c1400b07679dc4a1553eeb6193 
@REM @echo off
@REM echo.
@REM echo "ECB-output.txt - part:"
@REM sed -n 1,4p ECB-output.txt






@REM echo "### 2 - TRYING CBC    (AES-128)"

@REM echo.
@REM echo "Encrypt:"
@REM @echo on
@REM openssl enc -e -aes-128-cbc -in 2C_plain.txt -out CBC-cipher.bin^
@REM     -K bdb264c1400b07679dc4a1553eeb6193 -iv 99b74007b6254e4bd1c6e03631cad15b
@REM @echo off
@REM echo.
@REM echo "CBC-cipher.bin - part:"
@REM sed -n 1,1p CBC-cipher.bin

@REM echo.
@REM echo.
@REM echo "Change a single bit (any bit) of the 0x30th/48th byte in the encrypted file."
@REM @pause 

@REM echo.
@REM echo "Decrypt:"
@REM @echo on
@REM openssl enc -d -aes-128-cbc -in CBC-cipher.bin -out CBC-output.txt^
@REM     -K bdb264c1400b07679dc4a1553eeb6193 -iv 99b74007b6254e4bd1c6e03631cad15b
@REM @echo off
@REM echo.
@REM echo "CBC-output.txt - part:"
@REM sed -n 1,4p CBC-output.txt





@REM echo "### 3 - TRYING CFB    (AES-128)"

@REM echo.
@REM echo "Encrypt:"
@REM @echo on
@REM openssl enc -e -aes-128-cfb -in 2C_plain.txt -out CFB-cipher.bin^
@REM     -K bdb264c1400b07679dc4a1553eeb6193 -iv 99b74007b6254e4bd1c6e03631cad15b
@REM @echo off
@REM echo.
@REM echo "CFB-cipher.bin - part:"
@REM sed -n 1,1p CFB-cipher.bin

@REM echo.
@REM echo.
@REM echo "Change a single bit (any bit) of the 0x30th/48th byte in the encrypted file."
@REM @pause 

@REM echo.
@REM echo "Decrypt:"
@REM @echo on
@REM openssl enc -d -aes-128-cfb -in CFB-cipher.bin -out CFB-output.txt^
@REM     -K bdb264c1400b07679dc4a1553eeb6193 -iv 99b74007b6254e4bd1c6e03631cad15b
@REM @echo off
@REM echo.
@REM echo "CFB-output.txt - part:"
@REM sed -n 1,4p CFB-output.txt




@REM echo "### 4 - TRYING OFB    (AES-128)"

@REM echo.
@REM echo "Encrypt:"
@REM @echo on
@REM openssl enc -e -aes-128-ofb -in 2C_plain.txt -out OFB-cipher.bin^
@REM     -K bdb264c1400b07679dc4a1553eeb6193 -iv 99b74007b6254e4bd1c6e03631cad15b
@REM @echo off
@REM echo.
@REM echo "OFB-cipher.bin - part:"
@REM sed -n 1,1p OFB-cipher.bin

@REM echo.
@REM echo "Decrypt:"
@REM @echo on
@REM openssl enc -d -aes-128-ofb -in OFB-cipher.bin -out OFB-output.txt^
@REM     -K bdb264c1400b07679dc4a1553eeb6193 -iv 99b74007b6254e4bd1c6e03631cad15b
@REM @echo off
@REM echo.
@REM echo "OFB-output.txt - part:"
@REM sed -n 1,4p OFB-output.txt





@REM INFO

@REM C:\Users\David\GitHub Repos\CITS3004_Code\Labs\Lab2\Task_2\Task_A>openssl enc -help
@REM Usage: enc [options]
@REM Valid options are:
@REM  -help               Display this summary
@REM  -ciphers            List ciphers
@REM  -in infile          Input file
@REM  -out outfile        Output file
@REM  -pass val           Passphrase source
@REM  -e                  Encrypt
@REM  -d                  Decrypt
@REM  -p                  Print the iv/key
@REM  -P                  Print the iv/key and exit
@REM  -v                  Verbose output
@REM  -nopad              Disable standard block padding
@REM  -salt               Use salt in the KDF (default)
@REM  -nosalt             Do not use salt in the KDF
@REM  -debug              Print debug info
@REM  -a                  Base64 encode/decode, depending on encryption flag
@REM  -base64             Same as option -a
@REM  -A                  Used with -[base64|a] to specify base64 buffer as a single line
@REM  -bufsize val        Buffer size
@REM  -k val              Passphrase
@REM  -kfile infile       Read passphrase from file
@REM  -K val              Raw key, in hex
@REM  -S val              Salt, in hex
@REM  -iv val             IV in hex
@REM  -md val             Use specified digest to create a key from the passphrase
@REM  -iter +int          Specify the iteration count and force use of PBKDF2
@REM  -pbkdf2             Use password-based key derivation function 2
@REM  -none               Don't encrypt
@REM  -*                  Any supported cipher
@REM  -rand val           Load the file(s) into the random number generator
@REM  -writerand outfile  Write random data to the specified file
@REM  -z                  Use zlib as the 'encryption'
@REM  -engine val         Use engine, possibly a hardware device


@REM C:\Users\David\GitHub Repos\CITS3004_Code\Labs\Lab2\Task_2\Task_A>openssl enc -ciphers
@REM Supported ciphers:
@REM -aes-128-cbc               -aes-128-cfb               -aes-128-cfb1
@REM -aes-128-cfb8              -aes-128-ctr               -aes-128-ecb
@REM -aes-128-ofb               -aes-192-cbc               -aes-192-cfb
@REM -aes-192-cfb1              -aes-192-cfb8              -aes-192-ctr
@REM -aes-192-ecb               -aes-192-ofb               -aes-256-cbc
@REM -aes-256-cfb               -aes-256-cfb1              -aes-256-cfb8
@REM -aes-256-ctr               -aes-256-ecb               -aes-256-ofb
@REM -aes128                    -aes128-wrap               -aes192
@REM -aes192-wrap               -aes256                    -aes256-wrap
@REM -aria-128-cbc              -aria-128-cfb              -aria-128-cfb1
@REM -aria-128-cfb8             -aria-128-ctr              -aria-128-ecb
@REM -aria-128-ofb              -aria-192-cbc              -aria-192-cfb
@REM -aria-192-cfb1             -aria-192-cfb8             -aria-192-ctr
@REM -aria-192-ecb              -aria-192-ofb              -aria-256-cbc
@REM -aria-256-cfb              -aria-256-cfb1             -aria-256-cfb8
@REM -aria-256-ctr              -aria-256-ecb              -aria-256-ofb
@REM -aria128                   -aria192                   -aria256
@REM -bf                        -bf-cbc                    -bf-cfb
@REM -bf-ecb                    -bf-ofb                    -blowfish
@REM -camellia-128-cbc          -camellia-128-cfb          -camellia-128-cfb1
@REM -camellia-128-cfb8         -camellia-128-ctr          -camellia-128-ecb
@REM -camellia-128-ofb          -camellia-192-cbc          -camellia-192-cfb
@REM -camellia-192-cfb1         -camellia-192-cfb8         -camellia-192-ctr
@REM -camellia-192-ecb          -camellia-192-ofb          -camellia-256-cbc
@REM -camellia-256-cfb          -camellia-256-cfb1         -camellia-256-cfb8
@REM -camellia-256-ctr          -camellia-256-ecb          -camellia-256-ofb
@REM -camellia128               -camellia192               -camellia256
@REM -cast                      -cast-cbc                  -cast5-cbc
@REM -cast5-cfb                 -cast5-ecb                 -cast5-ofb
@REM -chacha20                  -des                       -des-cbc
@REM -des-cfb                   -des-cfb1                  -des-cfb8
@REM -des-ecb                   -des-ede                   -des-ede-cbc
@REM -des-ede-cfb               -des-ede-ecb               -des-ede-ofb
@REM -des-ede3                  -des-ede3-cbc              -des-ede3-cfb
@REM -des-ede3-cfb1             -des-ede3-cfb8             -des-ede3-ecb
@REM -des-ede3-ofb              -des-ofb                   -des3
@REM -des3-wrap                 -desx                      -desx-cbc
@REM -id-aes128-wrap            -id-aes128-wrap-pad        -id-aes192-wrap
@REM -id-aes192-wrap-pad        -id-aes256-wrap            -id-aes256-wrap-pad
@REM -id-smime-alg-CMS3DESwrap  -idea                      -idea-cbc
@REM -idea-cfb                  -idea-ecb                  -idea-ofb
@REM -rc2                       -rc2-128                   -rc2-40
@REM -rc2-40-cbc                -rc2-64                    -rc2-64-cbc
@REM -rc2-cbc                   -rc2-cfb                   -rc2-ecb
@REM -rc2-ofb                   -rc4                       -rc4-40
@REM -rc5-cbc                   -rc5-cfb                   -rc5-ecb
@REM -rc5-ofb                   -seed                      -seed-cbc
@REM -seed-cfb                  -seed-ecb                  -seed-ofb




@REM C:\Users\David\GitHub Repos\CITS3004_Code\Labs\Lab2\Task_2\Task_A>openssl help
@REM Standard commands
@REM asn1parse         ca                ciphers           cms
@REM crl               crl2pkcs7         dgst              dhparam
@REM dsa               dsaparam          ec                ecparam
@REM ***enc***               engine            errstr            gendsa
@REM genpkey           genrsa            help              list
@REM nseq              ocsp              passwd            pkcs12
@REM pkcs7             pkcs8             pkey              pkeyparam
@REM pkeyutl           prime             rand              rehash
@REM req               rsa               rsautl            s_client
@REM s_server          s_time            sess_id           smime
@REM speed             spkac             srp               storeutl
@REM ts                verify            version           x509

@REM Message Digest commands (see the `dgst' command for more details)
@REM blake2b512        blake2s256        gost              md2
@REM md4               md5               rmd160            sha1
@REM sha224            sha256            sha3-224          sha3-256
@REM sha3-384          sha3-512          sha384            sha512
@REM sha512-224        sha512-256        shake128          shake256
@REM sm3

@REM Cipher commands (see the `enc' command for more details)
@REM aes-128-cbc       aes-128-ecb       aes-192-cbc       aes-192-ecb
@REM aes-256-cbc       aes-256-ecb       aria-128-cbc      aria-128-cfb
@REM aria-128-cfb1     aria-128-cfb8     aria-128-ctr      aria-128-ecb
@REM aria-128-ofb      aria-192-cbc      aria-192-cfb      aria-192-cfb1
@REM aria-192-cfb8     aria-192-ctr      aria-192-ecb      aria-192-ofb
@REM aria-256-cbc      aria-256-cfb      aria-256-cfb1     aria-256-cfb8
@REM aria-256-ctr      aria-256-ecb      aria-256-ofb      base64
@REM bf                bf-cbc            bf-cfb            bf-ecb
@REM bf-ofb            camellia-128-cbc  camellia-128-ecb  camellia-192-cbc
@REM camellia-192-ecb  camellia-256-cbc  camellia-256-ecb  cast
@REM cast-cbc          cast5-cbc         cast5-cfb         cast5-ecb
@REM cast5-ofb         des               des-cbc           des-cfb
@REM des-ecb           des-ede           des-ede-cbc       des-ede-cfb
@REM des-ede-ofb       des-ede3          des-ede3-cbc      des-ede3-cfb
@REM des-ede3-ofb      des-ofb           des3              desx
@REM idea              idea-cbc          idea-cfb          idea-ecb
@REM idea-ofb          rc2               rc2-40-cbc        rc2-64-cbc
@REM rc2-cbc           rc2-cfb           rc2-ecb           rc2-ofb
@REM rc4               rc4-40            rc5               rc5-cbc
@REM rc5-cfb           rc5-ecb           rc5-ofb           seed
@REM seed-cbc          seed-cfb          seed-ecb          seed-ofb
@REM zlib
