package randomx;


public class randomMCG extends randomX {
    long state;

    //  Constructors

    /** Creates a new pseudorandom number generator, seeded from
        the current time. */

    public randomMCG() {
        this.setSeed(System.currentTimeMillis());
    }

    /** Creates a new pseudorandom number generator with a
        specified nonzero seed.

@param seed initial seed for the generator
    */

    public randomMCG(long seed) throws IllegalArgumentException {
        this.setSeed(seed);
    }

    //  Seed access

    /** Set seed for generator.  Subsequent values will be based
        on the given nonzero seed.

@param seed seed for the generator
    */

    public final void setSeed(long seed) throws IllegalArgumentException {
        int i;

        if (seed == 0) {
            throw new IllegalArgumentException("seed must be nonzero");
        }
        super.setSeed();              // Notify parent seed has changed
        state = seed & 0xFFFFFFFFL;
        for (i = 0; i < 11; i++) {
            nextByte();
        }
    }

    /** Get next byte from generator.

@return the next byte from the generator.
    */

    public byte nextByte() {
        state = (state * 16807) & 0x7FFFFFFFL;
        return (byte) ((state >> 11) & 0xFF);
    }
};
