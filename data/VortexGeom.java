/*
 * VortexGeom.java
 *
 * Created on 2 avril 2003, 22:08
 */

package data;

/**
 *
 * @author  Mahler
 */
public class VortexGeom extends Boucle {
    
    private Boucle centreDiscret = null;
    
    /** Creates a new instance of VortexGeom */
    public VortexGeom() {
        super();
    }
    public VortexGeom (int[] xpts, int[] ypts, int npts,int tag) {
        super(xpts,ypts,npts,tag);
    }
    
    public double[] getCentreCGeom() {
        return centreDiscret.getCentre(); 
    }
    
    public void addPointCentre(int x, int y) {
        if (centreDiscret==null) {centreDiscret=new Boucle();}
        centreDiscret.addPoint(x,y);
    }
    
    public double getDispersionCGeom() {
        return centreDiscret.getDispersion(); 
    }
    

    
    
}
