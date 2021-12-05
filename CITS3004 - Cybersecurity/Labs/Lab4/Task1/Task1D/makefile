INC=/usr/local/include/openssl
LIB=/usr/local/lib/

all :
	gcc -I$(INC) -L$(LIB) -o owb OneWayBreaker.c -lcrypto -ldl
	gcc -I$(INC) -L$(LIB) -o cfb CollisionFreeBreaker.c -lcrypto -ldl
