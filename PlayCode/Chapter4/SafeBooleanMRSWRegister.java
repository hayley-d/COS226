public class SafeBooleanMRSWRegister implements Register<Boolean>{
    boolean [] s_table; // array of safe SRSW regesters

    public SafeBooleanMRSWRegister(int capacity){
        s_table = new boolean[capacity];
    }

    @Override
    public Boolean read() {
        int id = (int)Thread.currentThread().getId();
        return s_table[id];
    }

    @Override
    public void write(Boolean aBoolean) {
        for(int i = 0; i < s_table.length; i++){
            s_table[i] = aBoolean;
        }
    }
}
