/*
 * MetaVortex.java
 *
 * Created on 19 decembre 2002, 11:53
 */

/*
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package data;

import java.awt.*;
import java.util.ArrayList;

public class VortexAnt extends Boucle{
    
    
    private ArrayList mesVortex = null;
    
    
    /** Creates a new instance of MetaVortex */
    public VortexAnt() {
        super();
        mesVortex = new ArrayList();
    }
    
    public VortexAnt(Boucle b){
        super(b);
        mesVortex = new ArrayList();
        mesVortex.add(b);
    }
    
    public VortexAnt(int[] xp, int[] yp, int np, int tag){
        super(xp,yp,np,tag);
        mesVortex = new ArrayList();
        mesVortex.add(new Boucle(xp,yp,np,tag));
    }
    
    public VortexAnt(ArrayList vect){
        super();
        
        int ind=0;
        int ptMax=0;
        
        for(int i=0;i<vect.size();i++){
            if(((Boucle)vect.get(i)).npoints>ptMax){
                ind=i;
                ptMax = ((Boucle)vect.get(i)).npoints;
            }
        }
        mesVortex = vect;
        
        npoints = ((Boucle)vect.get(ind)).npoints;
        xpoints = ((Boucle)vect.get(ind)).xpoints;
        ypoints = ((Boucle)vect.get(ind)).ypoints;
        tag = ((Boucle)vect.get(ind)).tag;
        centre = ((Boucle)vect.get(ind)).centre;
        aire = ((Boucle)vect.get(ind)).aire;
        aiguilleMontre= ((Boucle)vect.get(ind)).aiguilleMontre;
        
    }
    
    public void addBoucle(Boucle b){
        mesVortex.add(b);
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
        double[] centre = new double[2];
        
        if(mesVortex.isEmpty())
            return null;
        else{
            somme[0]=0d;
            somme[1]=0d;
            centre[0]=0d;
            centre[1]=0d;
            for (Object mesVortex1 : mesVortex) {
                somme[0] += ((Boucle) mesVortex1).getCentre()[0];
                somme[1] += ((Boucle) mesVortex1).getCentre()[1];
            }
            centre[0]=somme[0]/(double)mesVortex.size();
            centre[1]=somme[1]/(double)mesVortex.size();
            return centre;
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
            if(((Boucle)mesVortex.get(i)).getAire()>maxAire){
                maxAire=((Boucle)mesVortex.get(i)).getAire();
                indiceMax = i;
            }
        return ((Boucle)mesVortex.get(indiceMax)).getContour();
    }
    
}
