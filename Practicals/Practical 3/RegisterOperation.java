public class RegisterOperation {
    enum Type {
        READ, WRITE
    }

    Type type;
    Integer value; // Relevant for write operations
    int startTime;
    int endTime;

    RegisterOperation(Type type, Integer value, int startTime, int endTime) {
        this.type = type;
        this.value = value;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String toString() {
        return "Type: " + type + ", Value: " + value + ", Start Time: " + startTime + ", End Time: " + endTime;
    }
}