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

package ants;

/** Une cellule candidate � la s�lection par une fourmi. */
public class Candidate {
    

    private boolean choosen;
    
    private double fitness;
  
    private double angle;
     
    private double diff;
    
    /** Creates a new instance of candidate
     * @param a Angle entre la cellule et le 0 du cercle trigonometrique.
     */
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
