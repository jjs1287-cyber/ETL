
all:	build run
build:
	export LIBRARY_PATH=/usr/local/etlforge/lib; \
	export LD_LIBRARY_PATH=/usr/local/etlforge/lib; \
	g++ -Wall -I../../.. -L../../../../../lib/konga -L/usr/local/etlforge/lib \
	-lkonga-characterencodingconversion-d -lkonga-utils-d -lxerces-c -lxalan-c \
	TestCharacterEncodingConversion.cpp

run:
	export LD_LIBRARY_PATH=../../../../../lib/konga:/usr/local/etlforge/lib; \
	./a.out

ldd:
	export LD_LIBRARY_PATH=../../../../../lib/konga:/usr/local/etlforge/lib; \
	ldd ./a.out

val:
	export LD_LIBRARY_PATH=../../../../../lib/konga:/usr/local/etlforge/lib; \
	valgrind ./a.out

clean:
	rm -f ./a.out
