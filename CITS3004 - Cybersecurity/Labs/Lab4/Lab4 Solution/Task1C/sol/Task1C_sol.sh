# Do the make only once
make

echo -n "this is a hash message" | openssl dgst -md5
echo -n "this is a hash message" | openssl dgst -md5 -out output1_md5.hash
# This is the actual result, writing manually to remove the variable name
echo "45c6cc49be35bb96e1aef14dee4ff3a1" > output1_md5.hash

echo -n "this ir a hash message" | openssl dgst -md5
echo -n "this ir a hash message" | openssl dgst -md5 -out output2_md5.hash
# This is the actual result, writing manually to remove the variable name
echo "9dfed55bf9375542bf855abd4404d48e" > output2_md5.hash

#Comparisson
# 90 bits difference for md5
./Comparer 32 output1_md5.hash output2_md5.hash


echo -n "this is a hash message" | openssl dgst -sha256
echo -n "this is a hash message" | openssl dgst -sha256 -out output1_sha256.hash
# This is the actual result, writing manually to remove the variable name
echo "65c1550deb9228b0925c065957c3f03cffef46455c8d29e1bb0162838f4546c0" > output1_sha256.hash

echo -n "this ir a hash message" | openssl dgst -sha256
echo -n "this ir a hash message" | openssl dgst -sha256 -out output2_sha256.hash
# This is the actual result, writing manually to remove the variable name
echo "d76054243bc2b7d510a654fd5a1b3dd620caf31be4324b9a5bad4b97a10af83c" > output2_sha256.hash

#Comparisson
# 171 bits difference for sha256
./Comparer 64 output1_sha256.hash output2_sha256.hash
