EXE=test
OBJS =  main.o course.o sectionbuilder.o instructor.o location.o section.o sectioncombo.o semester.o time.o week.o
OBJPATH = ./	

COMPILER = g++
COMPILER_OPTS = -c -g -O0 -Wall -Werror -std=c++14
LINKER = g++

all:$(EXE)

$(EXE): $(OBJS)
	$(LINKER) $(OBJS) $(LINKER_OPTS) -o $(EXE)
	
main.o:SectionBuilder.hpp Course.hpp
	$(COMPILER) $(COMPILER_OPTS) Main.cpp

course.o:Course.hpp Section.hpp SectionCombo.hpp SectionBuilder.hpp
	$(COMPILER) $(COMPILER_OPTS) Course.cpp

section.o: Section.hpp Week.hpp Instructor.hpp Location.hpp Semester.hpp
	$(COMPILER) $(COMPILER_OPTS) Section.cpp

sectioncombo.o: SectionCombo.hpp Section.hpp
	$(COMPILER) $(COMPILER_OPTS) SectionCombo.cpp

instructor.o: Instructor.hpp 
	$(COMPILER) $(COMPILER_OPTS) Instructor.cpp

location.o: Location.hpp
	$(COMPILER) $(COMPILER_OPTS) Location.cpp
	
week.o: Week.hpp Time.hpp
	$(COMPILER) $(COMPILER_OPTS) Week.cpp

semester.o: Semester.hpp
	$(COMPILER) $(COMPILER_OPTS) Semester.cpp
	
time.o: Time.hpp
	$(COMPILER) $(COMPILER_OPTS) Time.cpp

sectionbuilder.o: SectionBuilder.hpp
	$(COMPILER) $(COMPILER_OPTS) SectionBuilder.cpp

cleanup:
	-mv *.o *.gch $(OBJPATH)

clean :
	-rm -f *.o *.gch *.out $(EXE)
