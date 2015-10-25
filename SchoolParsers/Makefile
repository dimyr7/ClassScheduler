EXE=test
OBJS =  course.o instructor.o location.o section.o sectioncombo.o semester.o time.o week.o
OBJPATH = ./obj/	

COMPILER = g++
COMPILER_OPTS = -c -g -O0 -Wall -Werror
LINKER = g++

all:$(EXE)

$(EXE):$(OBJS) cleanup
	$(LINKER) $(OBJS) $(LINKER_OPTS) -o $(EXE)
	
main.o:Course.hpp 
	$(COMPILER) $(COMPILER_OPTS) main.cpp

course.o:Course.hpp Section.hpp
	$(COMPILER) $(COMPILER_OPTS) Course.cpp

section.o: Section.hpp Week.hpp Instructor.hpp Location.hpp
	$(COMPILER) $(COMPILER_OPTS) Section.cpp

sectioncombo.o: SectionCombo.hpp Section.hpp
	$(COMPILER) $(COMPILER_OPTS) SectionCombo.cpp

instructor.o: Instructor.hpp 
	$(COMPILER) $(COMPILER_OPTS) Instructor.cpp

location.o: Location.hpp
	$(COMPILER) $(COMPILER_OPTS) Location.cpp
	
week.o: Week.hpp Time.hpp
	$(COMPILER) $(COMPILER_OPTS) Weeh.cpp

semester.o: Semester.hpp
	$(COMPILER) $(COMPILER_OPTS) Semester.cpp
	
time.o: Time.hpp
	$(COMPILER) $(COMPILER_OPTS) Time.cpp

cleanup:
	-mv *.o *.gch $(OBJPATH)

clean :
	-rm -f *.o *.gch $(EXE)
