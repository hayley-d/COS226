# Variables
JAVAC = javac
JAVA = java
MAIN = Main
SRC = $(wildcard *.java)
CLASS_FILES = $(SRC:.java=.class)

# Default target: compile
all: $(CLASS_FILES)

# Rule to compile .java files into .class files
%.class: %.java
	$(JAVAC) $<

# Run the main class
run: all
	$(JAVA) $(MAIN)

# Clean up all .class files
clean:
	rm -f *.class

# Phony targets
.PHONY: all run clean

