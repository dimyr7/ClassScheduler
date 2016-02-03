OBJS = parser.o course.o section.o sectiongroup.o sectioncombo.o sectionbuilder.o week.o time.o instructor.o semester.o location.o schedule.o
MAINOBJS = main.o
OBJPATH = ./build

CC = g++
CFLAGS = -c -g -O0 -Wall -Werror -std=c++11 -I include 
LINKER = g++
LFLAGS = -o

ifdef DEBUG
	CFLAGS += -DNDEBUG
else
	CFLAGS += -DDEBUG
endif



SRCDIR = src/
BUILDDIR = build/

TARGET = bin/autoschedule

SRCEXT = cpp
INC = -I include 
all: $(TARGET) 

autoschedule: $(TARGET)

# Linking the autoschedule executible
$(TARGET): $(addprefix $(BUILDDIR), $(MAINOBJS) $(OBJS))
	@echo " Building Auto-Scheduler"
	$(LINKER) $(addprefix $(BUILDDIR), $(MAINOBJS) $(OBJS))  $(INC) $(LIBS) $(LFLAGS) $(TARGET)

# Compiling main.o
$(BUILDDIR)main.o: $(SRCDIR)Main.cpp $(addprefix $(BUILDDIR), course.o section.o parser.o schedule.o)
	$(CC) $(CFLAGS) $(SRCDIR)Main.cpp
	@mv main.o $(BUILDDIR)

# Compiling schedule.o
$(BUILDDIR)schedule.o: $(SRCDIR)Schedule.cpp $(addprefix $(BUILDDIR), sectioncombo.o)
	$(CC) $(CFLAGS) $(SRCDIR)Schedule.cpp
	@mv schedule.o $(BUILDDIR)

# compiling parser.o
$(BUILDDIR)parser.o: $(SRCDIR)Parser.cpp $(addprefix $(BUILDDIR), section.o sectionbuilder.o)
	$(CC) $(CFLAGS) $(SRCDIR)Parser.cpp
	@mv parser.o $(BUILDDIR)


# Compiling course.o
$(BUILDDIR)course.o: $(SRCDIR)Course.cpp $(addprefix $(BUILDDIR), section.o sectioncombo.o sectiongroup.o)
	$(CC) $(CFLAGS) $(SRCDIR)Course.cpp
	@mv course.o $(BUILDDIR)

# Compiling sectionbuilder.o
$(BUILDDIR)sectionbuilder.o: $(SRCDIR)SectionBuilder.cpp $(addprefix $(BUILDDIR), section.o week.o time.o semester.o instructor.o location.o)
	$(CC) $(CFLAGS) $(SRCDIR)SectionBuilder.cpp
	@mv sectionbuilder.o $(BUILDDIR)

# Compiling section.o
$(BUILDDIR)section.o: $(SRCDIR)Section.cpp $(addprefix $(BUILDDIR), time.o week.o semester.o instructor.o)
	$(CC) $(CFLAGS) $(SRCDIR)Section.cpp
	@mv section.o $(BUILDDIR)

# Compiling semester.o
$(BUILDDIR)semester.o: $(SRCDIR)Semester.cpp
	$(CC) $(CFLAGS) $(SRCDIR)Semester.cpp
	@mv semester.o $(BUILDDIR)

#compiling instructor.o
$(BUILDDIR)instructor.o: $(SRCDIR)Instructor.cpp
	$(CC) $(CFLAGS) $(SRCDIR)Instructor.cpp
	@mv instructor.o $(BUILDDIR)

# compiling location.o
$(BUILDDIR)location.o: $(SRCDIR)Location.cpp
	$(CC) $(CFLAGS) $(SRCDIR)Location.cpp
	@mv location.o $(BUILDDIR)

# compiling week.o
$(BUILDDIR)week.o: $(SRCDIR)Week.cpp $(addprefix $(BUILDDIR), time.o)
	$(CC) $(CFLAGS) $(SRCDIR)Week.cpp
	@mv week.o $(BUILDDIR)

# compiling time.o
$(BUILDDIR)time.o: $(SRCDIR)Time.cpp 
	$(CC) $(CFLAGS) $(SRCDIR)Time.cpp
	@mv time.o $(BUILDDIR)




# compiling sectioncombo.o
$(BUILDDIR)sectioncombo.o: $(SRCDIR)SectionCombo.cpp $(addprefix $(BUILDDIR), section.o)
	$(CC) $(CFLAGS) $(SRCDIR)SectionCombo.cpp
	@mv sectioncombo.o $(BUILDDIR)

# compiling sectiongroup.o
$(BUILDDIR)sectiongroup.o: $(SRCDIR)SectionGroup.cpp $(addprefix $(BUILDDIR), section.o)
	$(CC) $(CFLAGS) $(SRCDIR)SectionGroup.cpp
	@mv sectiongroup.o $(BUILDDIR)
	
clean:
	-rm -f build/*.o bin/autoschedule
