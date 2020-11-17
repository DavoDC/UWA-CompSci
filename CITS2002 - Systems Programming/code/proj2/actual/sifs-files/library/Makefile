# Library makefile

# Variables
HEADERS	= ../sifs.h sifs-internal.h md5.h helper.h
LIBRARY	= libsifs.a
OBJECTS	= mkvolume.o mkdir.o rmdir.o dirinfo.o writefile.o\
        readfile.o rmfile.o fileinfo.o perror.o md5.o helper.o defrag.o
CC      = cc
CFLAGS  = -std=c99 -Wall -Werror -pedantic

# Dependencies and actions
$(LIBRARY):	$(OBJECTS)
	ar rc $(LIBRARY) $(OBJECTS)
	ranlib $(LIBRARY)
	cp $(LIBRARY) ../testing

%.o:	%.c $(HEADERS)
	$(CC) $(CFLAGS) -c $<
