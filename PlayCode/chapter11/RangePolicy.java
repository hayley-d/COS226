
public class RangePolicy {
    public int successCount = 0;
    public int failCount = 0;

    public int getRange() {
        return 10;
    }
    
    public void recordEliminationSuccess() {
        successCount++;
    }
    
    public void recordEliminationTimeout() {
        failCount++;
    }
}

