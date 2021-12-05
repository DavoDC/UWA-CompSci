echo "1. DISABLE Address Space Randomization:"
sudo sysctl -w kernel.randomize_va_space=0

echo ""
echo "2. Compile Shellcode:"
gcc -z execstack -o call_myshell call_myshell.c

echo ""
echo "3. Run My Shellcode:"
./call_myshell
