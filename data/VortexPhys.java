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
package data;

import java.awt.Point;

/**
 *
 * @author Mahler
 */
public class VortexPhys extends Loop implements constants.physique {

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
     if (centreDiscret==null) {centreDiscret=new Loop();}
     centreDiscret.addPoint(x,y);
     }
     */
    /*
     public double getDispersionCGeom() {
     return centreDiscret.getDispersion();
     }
     */
}
