openssl enc -aes-128-cbc -e -in plain.txt -out plainEnc-cbc.bin -K 00112233445566778899aabbccddeeff -iv 0102030405060708
openssl enc -aes-128-ecb -e -in plain.txt -out plainEnc-ecb.bin -K 00112233445566778899aabbccddeeff
openssl enc -aes-128-cfb -e -in plain.txt -out plainEnc-cfb.bin -K 00112233445566778899aabbccddeeff -iv 0102030405060708
openssl enc -aes-128-ofb -e -in plain.txt -out plainEnc-ofb.bin -K 00112233445566778899aabbccddeeff -iv 0102030405060708
