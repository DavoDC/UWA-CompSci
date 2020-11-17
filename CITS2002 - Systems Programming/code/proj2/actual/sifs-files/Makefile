# Parent makefile

# Variables
PROJECT		= sifs
HEADER		= $(PROJECT).h
LIBRARY		= lib$(PROJECT).a
COMP    = cc -std=c99 -Wall -Werror -pedantic
LIBS	= -L. -lsifs -lm
    
# Make library + testing 
$(LIBRARY):
	make -C library --silent && make -C testing --silent
    
%:	%.c $(LIBRARY)
	$(COMP) -o $@ $< $(LIBS)
	
    
	
	

	




    
    



