openssl enc -des -e -aes-128-ecb -in sample.bmp -out sampleEnc-aes-128-ecb.bmp -K 00112233445566778899aabbccddeeff
openssl enc -des -e -aes-128-cbc -in sample.bmp -out sampleEnc-aes-128-cbc.bmp -K 00112233445566778899aabbccddeeff -iv 0102030405060708

openssl enc -des -e -bf-ecb -in sample.bmp -out sampleEnc-bf-ecb.bmp -K 00112233445566778899aabbccddeeff
openssl enc -des -e -bf-cbc -in sample.bmp -out sampleEnc-bf-cbc.bmp -K 00112233445566778899aabbccddeeff -iv 0102030405060708

cp sampleEnc-aes-128-ecb.bmp sampleEnc-withHeader-aes-128-ecb.bmp
cp sampleEnc-aes-128-cbc.bmp sampleEnc-withHeader-aes-128-cbc.bmp
cp sampleEnc-bf-ecb.bmp sampleEnc-withHeader-bf-ecb.bmp
cp sampleEnc-bf-cbc.bmp sampleEnc-withHeader-bf-cbc.bmp
