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
package com.marsouin.data;

import java.awt.*;
import java.util.ArrayList;

public class VortexAnt extends Loop{
    
    
    private ArrayList mesVortex = null;
    
    
    /** Creates a new instance of MetaVortex */
    public VortexAnt() {
        super();
        mesVortex = new ArrayList();
    }
    
    public VortexAnt(Loop b){
        super(b);
        mesVortex = new ArrayList();
        mesVortex.add(b);
    }
    
    public VortexAnt(int[] xp, int[] yp, int np, int tag){
        super(xp,yp,np,tag);
        mesVortex = new ArrayList();
        mesVortex.add(new Loop(xp,yp,np,tag));
    }
    
    public VortexAnt(ArrayList vect){
        super();
        
        int ind=0;
        int ptMax=0;
        
        for(int i=0;i<vect.size();i++){
            if(((Loop)vect.get(i)).npoints>ptMax){
                ind=i;
                ptMax = ((Loop)vect.get(i)).npoints;
            }
        }
        mesVortex = vect;
        
        npoints = ((Loop)vect.get(ind)).npoints;
        xpoints = ((Loop)vect.get(ind)).xpoints;
        ypoints = ((Loop)vect.get(ind)).ypoints;
        tag = ((Loop)vect.get(ind)).tag;
        centre = ((Loop)vect.get(ind)).centre;
        aire = ((Loop)vect.get(ind)).aire;
        aiguilleMontre= ((Loop)vect.get(ind)).aiguilleMontre;
        
    }
    
    
    public ArrayList getBoucles(){
        return mesVortex;
    }
    
    @Override
     public double[] getCentre() {
        return getBaricentre();
    }
     
    public double[] getBaricentre(){
        double[] somme = new double[2];
        double[] localCentre = new double[2];
        
        if(mesVortex.isEmpty())
            return null;
        else{
            somme[0]=0d;
            somme[1]=0d;
            localCentre[0]=0d;
            localCentre[1]=0d;
            for (Object mesVortex1 : mesVortex) {
                somme[0] += ((Loop) mesVortex1).getCentre()[0];
                somme[1] += ((Loop) mesVortex1).getCentre()[1];
            }
            localCentre[0]=somme[0]/(double)mesVortex.size();
            localCentre[1]=somme[1]/(double)mesVortex.size();
            return localCentre;
        }
    }
    
    @Override
    public Point[] getContour() {
        return  getPlusGrandeEnveloppe();
    }
    
    public Point[] getPlusGrandeEnveloppe(){
        double maxAire = 0d;
        int indiceMax = 0;
        
        for(int i=0;i<mesVortex.size();i++)
            if(((Loop)mesVortex.get(i)).getAire()>maxAire){
                maxAire=((Loop)mesVortex.get(i)).getAire();
                indiceMax = i;
            }
        return ((Loop)mesVortex.get(indiceMax)).getContour();
    }
    
}
