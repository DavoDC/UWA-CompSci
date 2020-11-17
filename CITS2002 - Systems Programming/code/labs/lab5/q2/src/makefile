# Makefile for 'stringtest'

# Variables
PROJECT =  stringtest
HEADERS =  $(PROJECT).h
OBJ     =  $(PROJECT).o pointers.o
COMP    =  cc -std=c99 -Wall -pedantic -Werror

# Dependencies and actions
$(PROJECT) : $(OBJ)
	$(COMP) -o $(PROJECT) $(OBJ)

$(PROJECT).o : $(PROJECT).c $(HEADERS)
	$(COMP) -c $(PROJECT).c

pointers.o : pointers.c $(HEADER)
	$(COMP) -c pointers.c

# Commands (silenced)

#  Remove generated files
clean:
	@rm -f *.o $(PROJECT)