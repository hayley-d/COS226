# Variables
JAVAC = javac
JAVA = java
SRC = ExecutionTree.java Label.java Main.java
CLASS_FILES = $(SRC:.java=.class)
MAIN_CLASS = Main

# Default target: compile the project
all: $(CLASS_FILES)

# Compile .java files to .class files
%.class: %.java
	$(JAVAC) $<

# Run the Main class
run: all
	$(JAVA) $(MAIN_CLASS)

# Clean up compiled .class files
clean:
	rm -f $(CLASS_FILES)

# Phony targets to prevent conflicts with files of the same name
.PHONY: all run clean

