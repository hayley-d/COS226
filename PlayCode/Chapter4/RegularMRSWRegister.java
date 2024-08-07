public class RegularMRSWRegister implements Register<Byte>{
    // Implementation of a 8-bit MRSW register
    // values true = 0 and false = 1
    //M-valued register is an array of M Boolean registers

    private static int RANGE = Byte.MIN_VALUE - Byte.MIN_VALUE + 1;
    boolean[] r_bit = new boolean[RANGE];

    //initially the 0th bit is set to true while the rest are set to false
    RegularMRSWRegister(int capacity){
       for(int i = 1; i < r_bit.length; i++){
           r_bit[i] = false;
       }
       r_bit[0] = true;
    }

    //Reads until it reads a value == true at some index i then returns i
    // read always returns true
    @Override
    public Byte read() {
        for(int i = 0; i < RANGE; i++){
            if(r_bit[i]){
                return (byte) i;
            }
        }
        return -1;
    }

    //sets location aByte to true and then sets all values below to false
    @Override
    public void write(Byte aByte) {
        r_bit[aByte] = true;
        for(int i = aByte-1; i >= 0; i--){
            r_bit[i] = false;
        }
    }
}
