/*
 * VortexGeom.java
 *
 * Created on 2 avril 2003, 22:08
 */
package data;

import java.awt.Point;

/**
 *
 * @author Mahler
 */
public class VortexPhys extends Boucle implements constants.physique {

    private Point monCentre = null;

    /**
     * Creates a new instance of VortexGeom
     */
    public VortexPhys() {
        super();
    }

    /**
     * Creates a new instance of VortexGeom
     */
    public VortexPhys(int tag) {
        super(tag);
    }

    public VortexPhys(int[] xpts, int[] ypts, int npts, int tag) {
        super(xpts, ypts, npts, tag);
    }

    /**
     * Creates a new instance of VortexGeom from CENTER
     */
    public VortexPhys(Point center, int tag) {
        super(tag);
        monCentre = new Point(center.x, center.y);
    }

    public double[] getCentre() {
        double[] centre = new double[2];
        centre[0] = (double) monCentre.x;
        centre[1] = (double) monCentre.y;
        return centre;
    }

    public Point getPointCentre() {
        return monCentre;
    }

    public double[] getCentreParCalcul() {
        return super.getCentre();
    }

    public void setCentre(Point p) {
        monCentre = new Point(p.x, p.y);
    }

    public boolean centreIsMin() {
        return ((getTag() < TAG_MAX) & (getTag() >= TAG_MIN));
    }

    /**
     * indique si le centre du vortex est un maximum local
     */
    public boolean centreIsMax() {
        return (getTag() >= TAG_MAX);
    }

    /*
     public void addPointCentre(int x, int y) {
     if (centreDiscret==null) {centreDiscret=new Boucle();}
     centreDiscret.addPoint(x,y);
     }
     */
    /*
     public double getDispersionCGeom() {
     return centreDiscret.getDispersion();
     }
     */
}
