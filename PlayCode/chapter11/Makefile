# Variables
JAVAC = javac
JAVA = java
SRC = $(wildcard *.java)
MAIN = Main

# Default target to compile all .java files
all: compile

# Compile all .java files
compile: $(SRC)
	$(JAVAC) $(SRC)

# Run the Main class
run: compile
	$(JAVA) $(MAIN)

# Clean up class files
clean:
	rm -f *.class

# Phony targets to avoid conflicts with files of the same name
.PHONY: all compile run clean

