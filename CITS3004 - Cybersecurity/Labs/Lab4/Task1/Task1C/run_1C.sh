
# Please, complete the following steps for BOTH MD5 and SHA256. :
# 	• 1. Hash the text “this is a hash message” using the function. 
#Save the result in a file, i.e. output1_md5.hash
# 	• 2. Hash the text “this ir a hash message” using the function. 
#Save the result in a file, i.e. output2_md5.hash
# 	• 3. Compare the two files using the provided Comparer program
# ./Comparer 32 output1_md5.hash output2_md5.hash

echo "Lab 4 - Part C"

echo ""
infile1="static/message.txt"
infile2="static/corrupted_message.txt"



echo "Part 1 = MD5"

outfile1="output/normal-md5-hash.txt" 
# rm outfile1 2> /dev/null                  # Remove previous 
# openssl dgst -md5 $infile1 > $outfile1    # Generate another
cat $outfile1

outfile2="output/corrupted-md5-hash.txt" 
# rm outfile2 2> /dev/null
# openssl dgst -md5 $infile2 > $outfile2
cat $outfile2

echo ""
./static/Comparer 32 $outfile1 $outfile2 # 32 as MD5
echo ""



echo "Part 2 = SHA256"

SHAoutfile1="output/normal-sha256-hash.txt" 
# rm SHAoutfile1 2> /dev/null                  # Remove previous 
# openssl dgst -sha256 $infile1 > $SHAoutfile1    # Generate another
cat $SHAoutfile1

SHAoutfile2="output/corrupted-sha256-hash.txt" 
# rm SHAoutfile2 2> /dev/null                  # Remove previous 
# openssl dgst -sha256 $infile2 > $SHAoutfile2    # Generate another
cat $SHAoutfile2


echo ""
./static/Comparer 64 $SHAoutfile1 $SHAoutfile2 # 64 as SHA256
echo ""




