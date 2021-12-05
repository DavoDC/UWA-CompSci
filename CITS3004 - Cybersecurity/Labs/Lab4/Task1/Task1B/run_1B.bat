
:: Less output
@echo off

:: Clear terminal
cls

:: Space
echo.
@echo off

@REM Task 1B Desc
@REM Keyed-Hashing for Message Authentication (HMAC)
@REM In this task, you will evaluate HMAC mechanism [3] that is implemented in openssl. 
@REM Your task is to hash the plain.txt file with MD5, SHA1 and SHA256
@REM using different key values and sizes. 


@REM ####### Keys
@REM Source: https://www.browserling.com/tools/random-hex
@REM %key1%
set key1=1
@REM %key2% (64 bit hex)
set key2=5f43d8635951e15e48db996849d2063c2138fcb5f7d8014c9e6b2ced18466e07
@REM %key2%  (96 bit hex)
set key3=b83358aea622f478003ac3c3a8de9f5131414644454368033413482b988664e3b2d362c1fd61e96fe4a419ad6fad1c93



echo "### Part 1: MD5"
echo.

echo "# Key 1 (1 bit):"
@echo on
openssl dgst -md5 -hmac %key1% plain.txt  > "plain-md5-key1-hash.txt" 
@REM cat "plain-md5-key1-hash.txt" 
@echo off
echo.

echo "# Key 2 (64 bit): "
@echo on
openssl dgst -md5 -hmac %key2% plain.txt  > "plain-md5-key2-hash.txt" 
@REM cat "plain-md5-key2-hash.txt" 
@echo off
echo.

echo "# Key 3 (96 bit): " 
@echo on
openssl dgst -md5 -hmac %key3% plain.txt  > "plain-md5-key3-hash.txt" 
@REM cat "plain-md5-key3-hash.txt" 
@echo off
echo.         





echo. 
echo. 
echo. 
echo "### Part 2: SHA1"              
echo.

echo "# Key 1 (1 bit):"
@echo on
openssl dgst -sha1 -hmac %key1% plain.txt  > "plain-sha1-key1-hash.txt" 
@REM cat "plain-sha1-key1-hash.txt" 
@echo off
echo.

echo "# Key 2 (64 bit): "
@echo on
openssl dgst -sha1 -hmac %key2% plain.txt  > "plain-sha1-key2-hash.txt" 
@REM cat "plain-sha1-key2-hash.txt" 
@echo off
echo.

echo "# Key 3 (96 bit): " 
@echo on
openssl dgst -sha1 -hmac %key3% plain.txt  > "plain-sha1-key3-hash.txt" 
@REM cat "plain-sha1-key3-hash.txt" 
@echo off
echo.     






echo. 
echo. 
echo. 
echo "### Part 3: SHA256"              
echo.

echo "# Key 1 (1 bit):"
@echo on
openssl dgst -sha256 -hmac %key1% plain.txt  > "plain-sha256-key1-hash.txt" 
@REM cat "plain-sha256-key1-hash.txt" 
@echo off
echo.

echo "# Key 2 (64 bit): "
@echo on
openssl dgst -sha256 -hmac %key2% plain.txt  > "plain-sha256-key2-hash.txt" 
@REM cat "plain-sha256-key2-hash.txt" 
@echo off
echo.

echo "# Key 3 (96 bit): " 
@echo on
openssl dgst -sha256 -hmac %key3% plain.txt  > "plain-sha256-key3-hash.txt" 
@REM cat "plain-sha256-key3-hash.txt" 
@echo off
echo.















@REM INFO

@REM Message Digest commands (see the `dgst' command for more details)
@REM blake2b512        blake2s256        gost              md2
@REM md4               md5               rmd160            sha1
@REM sha224            sha256            sha3-224          sha3-256
@REM sha3-384          sha3-512          sha384            sha512
@REM sha512-224        sha512-256        shake128          shake256
@REM sm3


@REM Usage: dgst [options] [file...]
@REM   file... files to digest (default is stdin)
@REM  -help               Display this summary
@REM  -c                  Print the digest with separating colons
@REM  -r                  Print the digest in coreutils format
@REM  -out outfile        Output to filename rather than stdout
@REM  -passin val         Input file pass phrase source
@REM  -sign val           Sign digest using private key
@REM  -verify val         Verify a signature using public key
@REM  -prverify val       Verify a signature using private key
@REM  -signature infile   File with signature to verify
@REM  -keyform format     Key file format (PEM or ENGINE)
@REM  -hex                Print as hex dump
@REM  -binary             Print in binary form
@REM  -d                  Print debug info
@REM  -debug              Print debug info
@REM  -fips-fingerprint   Compute HMAC with the key used in OpenSSL-FIPS fingerprint
@REM  -hmac val           Create hashed MAC with key
@REM  -mac val            Create MAC (not necessarily HMAC)
@REM  -sigopt val         Signature parameter in n:v form
@REM  -macopt val         MAC algorithm parameters in n:v form or key
@REM  -*                  Any supported digest
@REM  -rand val           Load the file(s) into the random number generator
@REM  -writerand outfile  Write random data to the specified file
@REM  -engine val         Use engine e, possibly a hardware device
@REM  -engine_impl        Also use engine given by -engine for digest operations


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






