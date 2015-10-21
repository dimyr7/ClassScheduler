EXE=test
OBJS = main.o course.o 

COMPILER = g++
COMPILER_OPTS = -c -g -O0 -Wall -Werror
LINKER = g++

all:$(EXE)

$(EXE):clean $(OBJS) 
	$(LINKER) $(OBJS) $(LINKER_OPTS) -o $(EXE)
	
main.o:Course.hpp 
	$(COMPILER) $(COMPILER_OPTS) main.cpp

course.o:Course.hpp Section.hpp
	$(COMPILER) $(COMPILER_OPTS) Course.cpp

clean :
	-rm -f *.o $(EXE)
