EXE=schedule
OBJS =  main.o course.o sectionbuilder.o instructor.o location.o section.o sectioncombo.o semester.o time.o week.o parser.o #jsonparser.o
OBJPATH = ./	

COMPILER = g++
COMPILER_OPTS = -c -g -O0 -Wall -Werror -std=c++11
LINKER = g++

all:$(EXE)

$(EXE):$(OBJS)
	$(LINKER) $(OBJS) $(LINKER_OPTS) -o $(EXE)
			
main.o: Main.cpp SectionBuilder.o Course.o Section.o
	$(COMPILER) $(COMPILER_OPTS) Main.cpp

course.o: Course.cpp Section.o SectionCombo.o SectionBuilder.o
	$(COMPILER) $(COMPILER_OPTS) Course.cpp

section.o: Section.cpp Week.o Instructor.o Location.o Semester.o
	$(COMPILER) $(COMPILER_OPTS) Section.cpp

sectioncombo.o: SectionCombo.cpp Section.o
	$(COMPILER) $(COMPILER_OPTS) SectionCombo.cpp

instructor.o: Instructor.cpp 
	$(COMPILER) $(COMPILER_OPTS) Instructor.cpp

location.o: Location.cpp
	$(COMPILER) $(COMPILER_OPTS) Location.cpp
			
week.o: Week.cpp Time.o
	$(COMPILER) $(COMPILER_OPTS) Week.cpp

semester.o: Semester.cpp
	$(COMPILER) $(COMPILER_OPTS) Semester.cpp
			
time.o: Time.cpp
	$(COMPILER) $(COMPILER_OPTS) Time.cpp

sectionbuilder.o: SectionBuilder.cpp
	$(COMPILER) $(COMPILER_OPTS) SectionBuilder.cpp

parser.o: Parser.hpp
	$(COMPILER) $(COMPILER_OPTS) Parser.cpp

#jsonparser.o: JSONParser.cpp
#	$(COMPILER) $(COMPILER_OPTS) JSONParser.cpp

cleanup:
	-mv *.o *.gch $(OBJPATH)

clean :
	-rm -f *.o *.gch *.out $(EXE)
