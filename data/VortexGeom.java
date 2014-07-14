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

/**
 *
 * @author  Mahler
 */
public class VortexGeom extends Loop {
    
    private Loop centreDiscret = null;
    
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
        if (centreDiscret==null) {centreDiscret=new Loop();}
        centreDiscret.addPoint(x,y);
    }
    
    public double getDispersionCGeom() {
        return centreDiscret.getDispersion(); 
    }
    

    
    
}
