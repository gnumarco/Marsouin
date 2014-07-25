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

public class Candidate {
    

    private boolean choosen;
    
    private double fitness;
  
    private double angle;
     
    private double diff;

    public Candidate(double a) {
        setChoosen(false);
        setAngle(a);
        setFitness(0.0);
        setDiff(0.0);
    } 

    public double getDiff() {
        return this.diff;
    }

    public final void setDiff(final double diff) {
        this.diff = diff;
    }

    public boolean isChoosen() {
        return this.choosen;
    }

    public final void setChoosen(final boolean choosen) {
        this.choosen = choosen;
    }

    public double getFitness() {
        return this.fitness;
    }

    public final void setFitness(final double fitness) {
        this.fitness = fitness;
    }

    public double getAngle() {
        return this.angle;
    }

    public final void setAngle(final double angle) {
        this.angle = angle;
    }
}
