# Compiler
JAVAC = javac

# Files to compile
SRC = BackeryLock.java BoundedTimestamp.java Main.java Marker.java Paper.java

# Output directory
OUT_DIR = bin

# Main class to run
MAIN_CLASS = Main

# Default target: compile all and run
run: $(SRC)
	$(JAVAC) -d $(OUT_DIR) $(SRC)
	java -cp $(OUT_DIR) $(MAIN_CLASS)

# Clean up the compiled files
clean:
	rm -rf $(OUT_DIR)/*.class

