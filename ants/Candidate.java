/*
 * candidate.java
 *
 * Created on 10 septembre 2002, 21:52
 */

/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
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
