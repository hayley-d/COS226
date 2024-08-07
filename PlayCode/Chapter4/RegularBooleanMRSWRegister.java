public class RegularBooleanMRSWRegister implements Register<Boolean>{

    ThreadLocal<Boolean> last;
    boolean s_value; //safe MRSW register

    RegularBooleanMRSWRegister(int capacity){
        last = new ThreadLocal<Boolean>() {
            protected Boolean initialValue() {return false;}
        };
    }

    @Override
    public Boolean read() {
        return s_value;
    }

    /*
    * if the value is the same as the old value dont write
    *
    * */
    @Override
    public void write(Boolean aBoolean) {
        if(aBoolean != last.get()){
            last.set(aBoolean);
            s_value = aBoolean;
        }
    }
}
