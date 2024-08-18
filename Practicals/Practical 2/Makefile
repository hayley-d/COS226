# Define variables
JAVAC = javac
JAVA = java
SRC = Main.java ExecutionOrderChecker.java MethodCall.java
CLASSES = Main.class ExecutionOrderChecker.class MethodCall.class

# Default target
all: run

# Compile Java files
Main.class: Main.java
	$(JAVAC) Main.java

ExecutionOrder.class: ExecutionOrderChecker.java
	$(JAVAC) ExecutionOrderChecker.java

MethodCall.class: MethodCall.java
	$(JAVAC) MethodCall.java

# Run the Java application
run: $(CLASSES)
	$(JAVA) Main

# Clean up class files
clean:
	rm -f $(CLASSES)

