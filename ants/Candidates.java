/*
 * candidates.java
 *
 * Created on 11 septembre 2002, 11:58
 */

/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */

package ants;

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
