# Define the Java compiler
JC = javac

# Define the Java runtime
JAVA = java

# List all Java files in the current directory
SOURCES = $(wildcard *.java)

# Define the main class to run
MAIN_CLASS = Main

# Define the target for building the project
.PHONY: all clean run

all: compile run

# Compile all Java files
compile:
	$(JC) $(SOURCES)

# Run the main class
run: compile
	$(JAVA) $(MAIN_CLASS)

# Clean up class files
clean:
	rm -f *.class

