
# Lab 5 - Task 1

# Part 1
# Updating docker image...


# Part 2
# sysctl -w kernel.randomize_va_space=0


# Part 3
gcc -fno-stack-protector -o test_no_stack_guard test.c


# Part 4
gcc -z execstack -o test_w_exec_stack test.c
gcc -z noexecstack -o test_wo_exec_stack test.c


# Tests
echo "./test_no_stack_guard"
./test_no_stack_guard
echo ""

echo "./test_w_exec_stack"
./test_w_exec_stack
echo ""

echo "./test_wo_exec_stack"
./test_wo_exec_stack
echo ""


