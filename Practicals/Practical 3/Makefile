# Variables
JAVAC = javac
JAVA = java
SRC = Register.java Main.java RegisterOperation.java
TARGET = Main

# Default rule to compile and run
all: $(TARGET)

$(TARGET): $(SRC)
	$(JAVAC) $(SRC)

run: $(TARGET)
	$(JAVA) $(TARGET)

clean:
	rm -f *.class

