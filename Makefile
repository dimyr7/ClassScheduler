OBJS = parser.o course.o section.o sectiongroup.o sectioncombo.o sectionbuilder.o week.o time.o instructor.o semester.o location.o
TESTOBJS = phase1test.o
MAINOBJS = main.o
OBJPATH = ./build

CC = g++
CFLAGS = -c -g -O0 -Wall -Werror -std=c++11 -I include
LINKER = g++
LFLAGS = -o

SRCDIR = src/
BUILDDIR = build/

TARGET = bin/autoschedule
TESTER = bin/phase1test

SRCEXT = cpp
INC = -I include 
all: $(TARGET) 

autoschedule: $(TARGET)

# Linking the autoschedule executible
$(TARGET): $(addprefix $(BUILDDIR), $(MAINOBJS) $(OBJS))
	@echo " Building Auto-Scheduler"
	$(LINKER) $(addprefix $(BUILDDIR), $(MAINOBJS) $(OBJS))  $(INC) $(LIBS) $(LFLAGS) $(TARGET)

$(BUILDDIR)phase1test.o: $(TESTDIR)Phase1Test.cpp
	$(CC) $(CFLAGS) $(TESTDIR)Phase1Test.cpp
	@mv phase1test.o $(BUILDDIR)

# Compiling main.o
$(BUILDDIR)main.o: $(SRCDIR)Main.cpp $(addprefix $(BUILDDIR), course.o section.o parser.o)
	$(CC) $(CFLAGS) $(SRCDIR)Main.cpp
	@mv main.o $(BUILDDIR)

#compiling parser.o
$(BUILDDIR)parser.o: $(SRCDIR)Course/Parser.cpp $(addprefix $(BUILDDIR), section.o sectionbuilder.o)
	$(CC) $(CFLAGS) $(SRCDIR)Course/Parser.cpp
	@mv parser.o $(BUILDDIR)


# Compiling course.o
$(BUILDDIR)course.o: $(SRCDIR)Course/Course.cpp $(addprefix $(BUILDDIR), section.o sectioncombo.o sectiongroup.o)
	$(CC) $(CFLAGS) $(SRCDIR)Course/Course.cpp
	@mv course.o $(BUILDDIR)

# Compiling sectionbuilder.o
$(BUILDDIR)sectionbuilder.o: $(SRCDIR)Course/SectionBuilder.cpp $(addprefix $(BUILDDIR), section.o week.o time.o semester.o instructor.o location.o)
	$(CC) $(CFLAGS) $(SRCDIR)Course/SectionBuilder.cpp
	@mv sectionbuilder.o $(BUILDDIR)

# Compiling section.o
$(BUILDDIR)section.o: $(SRCDIR)Course/Section.cpp $(addprefix $(BUILDDIR), time.o week.o semester.o instructor.o)
	$(CC) $(CFLAGS) $(SRCDIR)Course/Section.cpp
	@mv section.o $(BUILDDIR)

# Compiling semester.o
$(BUILDDIR)semester.o: $(SRCDIR)Course/Semester.cpp
	$(CC) $(CFLAGS) $(SRCDIR)Course/Semester.cpp
	@mv semester.o $(BUILDDIR)

#compiling instructor.o
$(BUILDDIR)instructor.o: $(SRCDIR)Course/Instructor.cpp
	$(CC) $(CFLAGS) $(SRCDIR)Course/Instructor.cpp
	@mv instructor.o $(BUILDDIR)

# compiling location.o
$(BUILDDIR)location.o: $(SRCDIR)Course/Location.cpp
	$(CC) $(CFLAGS) $(SRCDIR)Course/Location.cpp
	@mv location.o $(BUILDDIR)

# compiling week.o
$(BUILDDIR)week.o: $(SRCDIR)Course/Week.cpp $(addprefix $(BUILDDIR), time.o)
	$(CC) $(CFLAGS) $(SRCDIR)Course/Week.cpp
	@mv week.o $(BUILDDIR)

# compiling time.o
$(BUILDDIR)time.o: $(SRCDIR)Course/Time.cpp 
	$(CC) $(CFLAGS) $(SRCDIR)Course/Time.cpp
	@mv time.o $(BUILDDIR)

# compiling sectioncombo.o
$(BUILDDIR)sectioncombo.o: $(SRCDIR)Course/SectionCombo.cpp $(addprefix $(BUILDDIR), section.o)
	$(CC) $(CFLAGS) $(SRCDIR)Course/SectionCombo.cpp
	@mv sectioncombo.o $(BUILDDIR)

# compiling sectiongroup.o
$(BUILDDIR)sectiongroup.o: $(SRCDIR)Course/SectionGroup.cpp $(addprefix $(BUILDDIR), section.o)
	$(CC) $(CFLAGS) $(SRCDIR)Course/SectionGroup.cpp
	@mv sectiongroup.o $(BUILDDIR)
	

#client
client.o: 
	gcc src/client.cpp -o bin/client
clean:
	-rm -f build/*.o bin/autoschedule
