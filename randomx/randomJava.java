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


import java.util.Random;

public class randomJava extends randomX {

    private Random r;
    private int ibytes = 0;

    //  Constructors

    /** Creates a new pseudorandom number generator, seeded from
        the current time. */

    public randomJava() {
        /* Since Java's generator seeds itself from the current time
           when called with no arguments, we don't have to pass in a
           seed here. */
        r = new Random();
    }

    /** Creates a new pseudorandom number generator with a
        specified seed.

@param seed initial seed for the generator
    */

    public randomJava(long seed) {
        r = new Random(seed);
    }

    /** Set seed for generator.  Subsequent values will be based
        on the given seed.

@param seed seed for the generator
    */

    public void setSeed(long seed) {
        super.setSeed();              // Notify parent seed has changed
        r.setSeed(seed);
        ibytes = 0;                   // Clear bytes in nextByte buffer
    }

    private int idat;

    /** Get next byte from generator.  To minimise calls on the
        underlying Java generator, integers are generated and
        individual bytes returned on subsequent calls.  A call
        on <tt>setSeed()</tt> discards any bytes in the buffer.

@return the next byte from the generator.
    */

    public byte nextByte() {
        byte d;

        if (ibytes <= 0) {
            idat = r.nextInt();
            ibytes = 4;
        }
        d = (byte) idat;
        idat >>= 8;
        ibytes--;
        return d;
    }
};
