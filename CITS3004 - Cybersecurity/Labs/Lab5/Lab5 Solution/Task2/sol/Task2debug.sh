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


#Steps for Debuggin in dbg
#gdb -q ./auth_overflow
#list 1
#break 9
#break 16
#run AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
#x/s password_buffer
#x/x &auth_flag
#print 0x7fffffffe3dc -  0x7fffffffe3c0
#x/16xw password_buffer
#continue
#x/s password_buffer
#x/x &auth_flag
#x/16xw password_buffer
#x/4cb &auth_flag
#x/dw &auth_flag
#continue
