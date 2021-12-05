#clean executables
rm auth_overflow auth_overflow2

#1. DISABLE Address Space Randomization (ASLR) - It requires SuperUser 
#su 
#echo 0 > /proc/sys/kernel/randomize_va_space 
#exit

echo "1. DISABLE Address Space Randomization (ASLR):"
sudo sysctl -w kernel.randomize_va_space=2   # 2 or 0 is making no difference in execution

echo "2. Compile auth_overflow.c"
#gcc -fno-stack-protector -z execstack -o auth_overflow auth_overflow.c
gcc -g -fno-stack-protector -o auth_overflow auth_overflow.c


echo ""
echo "3.1 Run Authentication Overflow with password test"
./auth_overflow test

echo ""
echo "3.2 Run Authentication Overflow with password briling"
./auth_overflow brillig

echo ""
echo "3.3 Run Authentication Overflow with password outgrabe"
./auth_overflow outgrabe

echo ""
echo "3.4 Run Authentication Overflow with password AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
./auth_overflow AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

echo ""
echo "4. Fix 1: Compile auth_overflow with stack-protector"
gcc -o auth_overflow auth_overflow.c

echo ""
echo "5. Fix 1: Run Authentication Overflow with password AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
./auth_overflow AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

echo ""
echo "6. Fix2: Compile auth_overflow2.c"
#gcc -fno-stack-protector -z execstack -o auth_overflow auth_overflow.c
gcc -fno-stack-protector -g -o auth_overflow2 auth_overflow2.c

echo ""
echo "7.1 Run Authentication Overflow 2 with password test"
./auth_overflow2 test

echo ""
echo "7.2 Run Authentication Overflow 2 with password briling"
./auth_overflow2 brillig

echo ""
echo "7.3 Run Authentication Overflow 2 with password outgrabe"
./auth_overflow2 outgrabe

echo ""
echo "7.4 Run Authentication Overflow 2 with password AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
./auth_overflow2 AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA


