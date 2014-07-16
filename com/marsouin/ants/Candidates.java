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
package com.marsouin.ants;

public class Candidates {
    
    private Candidate[] cand;
    
    /** Creates a new instance of candidates */
    public Candidates() {
        setCand(new Candidate[8]);
        for(int i=0;i<8;i++){
            cand[i] = new Candidate((java.lang.Math.PI/4)*i);
        }
    }
    

    public Candidate[] getCand() {
        return this.cand;
    }

    public final void setCand(final Candidate[] cand) {
        this.cand = cand;
    }
}
