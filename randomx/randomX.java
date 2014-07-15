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

import static java.lang.Math.log;
import static java.lang.Math.sqrt;

public abstract class randomX {

    private int nbits = 0;
    private boolean iset = false;

    /** Reset when seed changes.  A generator which supports seed
        must call this method by <tt>super.setSeed()</tt> when its own
        <tt>setSeed(</tt><i>long</i><tt>)</tt> method is called.  This
        allows randomX to discard any buffered data in the
        <tt>nextBit()</tt> and <tt>nextGaussian()</tt> methods so that
        subsequent calls will immediately reflect the new seed.

        <p>
        If a derived class does not permit specification of a seed
        (hardware-based generators, for example), it should declare:

        <p>
        <blockquote>
            <tt>private void setSeed(long seed) { }</tt>
        </blockquote>
        <p>
        which will hide the setSeed method from its users and cause
        a compile-time error if a program attempts to specify a seed.  */

    public void setSeed() {
        nbits = 0;
        iset = false;
    }

    /** Return next [pseudo]random byte from low level generator.  All
        generators derived from this class must implement
        <tt>nextByte()</tt>.  */

    public abstract byte nextByte();

    /*  Emulation of standard java.util.Random methods.  Given
        an implementation of nextByte() in the derived class, these
        methods provide all the other forms of results.  A derived
        class is, of course, free to reimplement any of these.
        For example, a generator which produces 31 high-quality
        pseudorandom bits on each call might reimplement nextBit(), 
        nextShort(), and nextFloat() to avoid the inefficiency of
        passing individual bytes to the parent class, then reassembling
        to return to the caller.  */

    /** @return the next random, uniformly distributed, <tt>int</tt>
                value. */

    public int nextInt() {
        return (int) ((((int) nextShort()) << 16) | (((int) nextShort()) & 0xFFFF));
    }

    /** @return the next random, uniformly distributed, <tt>long</tt>
                value. */

    public long nextLong() {
        return (long) ((((long) nextInt()) << 32) | (((long) nextInt()) & 0xFFFFFFFFl));
    }

    /** @return the next random, uniformly distributed, <tt>float</tt>
                value, greater than or equal to 0 and less than 1. */

    public float nextFloat() {
        return (float) ((nextInt() & 0x7FFFFFFF) / (0x7FFFFFFF * 1.0));
    }

    /** @return the next random, uniformly distributed, <tt>double</tt>
                value, greater than or equal to 0 and less than 1. */

    public double nextDouble() {
        return (double) ((nextLong() & 0x7FFFFFFFFFFFFFFFl) /
                         (0x7FFFFFFFFFFFFFFFl * 1.0));
    }

    private double gset;

    /** @return the next Gaussian (normal, or bell-curve) distributed
                random value, with mean of 0.0 and standard deviation
                1.0. */

    public double nextGaussian() {
        double fac, rsq, v1, v2;

        if (!iset) {
            do {
                v1 = 2 * nextDouble() - 1;
                v2 = 2 * nextDouble() - 1;
                rsq = v1 * v1 + v2 * v2;
            } while (rsq > 1.0 || rsq == 0.0);
            fac = sqrt(-2.0 * log(rsq) / rsq);
            gset = v1 * fac;
            iset = true;
            return v2 * fac;
        } else {
            iset = false;
            return gset;
        }
    }

    //  Extended generator access methods with default implementations

    private byte b;

    /** @return the next random bit, as a <tt>boolean</tt>. */

    public boolean nextBit() {
        boolean bit;

        if (nbits <= 0) {
            b = nextByte();
            nbits = 8;
        }
        bit = (b & 0x80) != 0;
        b <<= 1;
        nbits--;
        return bit;
    }

    /** Fill a portion of an array of bytes with random data.

@param buf array of <tt>byte</tt> to fill.

@param buflen number of bytes to store.
    */

    public void nextByte(byte buf[], int buflen) {
        int i = 0;

        while (buflen-- > 0) {
            buf[i++] = nextByte();
        }
    }

    /** Fill an array of bytes with random data.

@param buf array of <tt>byte</tt>s to fill.
    */

    public void nextByte(byte buf[]) {
        nextByte(buf, buf.length);
    }

    /** @return the next random, uniformly distributed, <tt>short</tt>
                value. */

    public short nextShort() {
        return (short) ((((short) nextByte()) << 8) | ((short) (nextByte() & 0xFF)));
    }
}
