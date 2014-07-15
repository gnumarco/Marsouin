/* 
 * Copyright (C) 2014 Marc Segond <dr.marc.segond@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package randomx;

import static java.lang.System.currentTimeMillis;


public class randomLCG extends randomX {
    long state;

    //  Constructors

    /** Creates a new pseudorandom number generator, seeded from
        the current time. */

    public randomLCG() {
        this.setSeed(currentTimeMillis());
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
