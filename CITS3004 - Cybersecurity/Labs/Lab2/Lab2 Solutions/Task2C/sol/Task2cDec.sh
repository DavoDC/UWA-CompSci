openssl enc -aes-128-cbc -d -in plainEnc-cbc.bin -out plainDecr-cbc.txt -K 00112233445566778899aabbccddeeff -iv 0102030405060708
openssl enc -aes-128-ecb -d -in plainEnc-ecb.bin -out plainDecr-ecb.txt -K 00112233445566778899aabbccddeeff -iv 0102030405060708
openssl enc -aes-128-cfb -d -in plainEnc-cfb.bin -out plainDecr-cfb.txt -K 00112233445566778899aabbccddeeff -iv 0102030405060708
openssl enc -aes-128-ofb -d -in plainEnc-ofb.bin -out plainDecr-ofb.txt -K 00112233445566778899aabbccddeeff -iv 0102030405060708

openssl enc -aes-128-cbc -d -in plainEnc-corrupt-cbc.bin -out plainDecr-afterCorrupt-cbc.txt -K 00112233445566778899aabbccddeeff -iv 0102030405060708
openssl enc -aes-128-ecb -d -in plainEnc-corrupt-ecb.bin -out plainDecr-afterCorrupt-ecb.txt -K 00112233445566778899aabbccddeeff -iv 0102030405060708
openssl enc -aes-128-cfb -d -in plainEnc-corrupt-cfb.bin -out plainDecr-afterCorrupt-cfb.txt -K 00112233445566778899aabbccddeeff -iv 0102030405060708
openssl enc -aes-128-ofb -d -in plainEnc-corrupt-ofb.bin -out plainDecr-afterCorrupt-ofb.txt -K 00112233445566778899aabbccddeeff -iv 0102030405060708
