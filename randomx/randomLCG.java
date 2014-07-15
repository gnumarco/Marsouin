package randomx;


public class randomLCG extends randomX {
    long state;

    //  Constructors

    /** Creates a new pseudorandom number generator, seeded from
        the current time. */

    public randomLCG() {
        this.setSeed(System.currentTimeMillis());
    }

    /** Creates a new pseudorandom number generator with a
        specified seed.

@param seed initial seed for the generator
    */

    public randomLCG(long seed) {
        this.setSeed(seed);
    }

    //  Seed access

    /** Set seed for generator.  Subsequent values will be based
        on the given seed.

@param seed seed for the generator
    */

    public final void setSeed(long seed) {
        super.setSeed();              // Notify parent seed has changed
        state = seed & 0xFFFFFFFFL;
    }

    /** Get next byte from generator.  Given how poor this generator
        is, it's wise to make a separate call for each byte rather than
        extract fields from a single call, which may be correlated.
        Also, the higher-order bits of this generator are more
        random than the low, so we extract the byte after discarding
        the low-order 11 bits.

@return the next byte from the generator.
    */

    public byte nextByte() {
        state = (state * 1103515245L + 12345L) & 0x7FFFFFFFL;
        return (byte) ((state >> 11) & 0xFF);
    }
};
