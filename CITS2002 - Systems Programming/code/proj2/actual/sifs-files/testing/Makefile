# Makefile for testing executable

# Variables
COMP  =  cc -std=c99 -Wall -pedantic -Werror -fsanitize=address
LIB = -L. -lsifs -lm
NAME = sifs_test

# Executable depends on C file
# Compile C file 
$(NAME) : $(NAME).c libsifs.a
	$(COMP) -o $(NAME) $(NAME).c $(LIB)
	
    
    


