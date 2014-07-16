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
package com.marsouin.randomx;

import static java.lang.System.currentTimeMillis;


public class randomMCG extends randomX {
    long state;

    //  Constructors

    /** Creates a new pseudorandom number generator, seeded from
        the current time. */

    public randomMCG() {
        this.setSeed(currentTimeMillis());
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
