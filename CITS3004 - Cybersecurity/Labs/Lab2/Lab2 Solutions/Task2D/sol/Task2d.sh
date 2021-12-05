echo "plain20 CBC: ";openssl enc -aes-128-cbc -e -in plain20.txt -out plain20Enc-cbc.bin -K 00112233445566778899aabbccddeeff -iv 0102030405060708
echo "plain20 ECB: ";openssl enc -aes-128-ecb -e -in plain20.txt -out plain20Enc-ecb.bin -K 00112233445566778899aabbccddeeff
echo "plain20 CFB: ";openssl enc -aes-128-cfb -e -in plain20.txt -out plain20Enc-cfb.bin -K 00112233445566778899aabbccddeeff -iv 0102030405060708
echo "plain20 OFB: ";openssl enc -aes-128-ofb -e -in plain20.txt -out plain20Enc-ofb.bin -K 00112233445566778899aabbccddeeff -iv 0102030405060708

echo "plain32 CBC: ";openssl enc -aes-128-cbc -e -in plain32.txt -out plain32Enc-cbc.bin -K 00112233445566778899aabbccddeeff -iv 0102030405060708
echo "plain32 ECB: ";openssl enc -aes-128-ecb -e -in plain32.txt -out plain32Enc-ecb.bin -K 00112233445566778899aabbccddeeff
echo "plain32 CFB: ";openssl enc -aes-128-cfb -e -in plain32.txt -out plain32Enc-cfb.bin -K 00112233445566778899aabbccddeeff -iv 0102030405060708
echo "plain32 OFB: ";openssl enc -aes-128-ofb -e -in plain32.txt -out plain32Enc-ofb.bin -K 00112233445566778899aabbccddeeff -iv 0102030405060708
