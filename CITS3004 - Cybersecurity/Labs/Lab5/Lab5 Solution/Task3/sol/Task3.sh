#Clean Code
rm call_shellcode

echo "1. DISABLE Address Space Randomization:"
sudo sysctl -w kernel.randomize_va_space=2   # 2 or 0 is making no difference in execution

echo ""
echo "2. Compile Shellcode:"
gcc -z execstack -o call_shellcode call_shellcode.c
#echo "2. Compile Shellcode (Execution will fail):"
#gcc -o call_shellcode call_shellcode.c


echo ""
echo "3. Run Shellcode:"
./call_shellcode
