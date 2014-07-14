/*
 * Vortex3D.java
 *
 * Created on 12 fevrier 2002, 11:00
 */

/*
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package visu;

import javax.media.j3d.*;
import javax.vecmath.*;
import data.Boucle;

public class Vortex3D {
    
    private Boucle monVortex;
    private BranchGroup VortexBG;
    
    /** Creates a new instance of Vortex3D */
    public Vortex3D(Boucle v, int maxX, int maxY, ColoringAttributes c) {
        monVortex = v;
        
        VortexBG = new BranchGroup();
        
        Appearance vortexAppear = new Appearance();
        ColoringAttributes caL1 = c;
        vortexAppear.setColoringAttributes(caL1);
        Transform3D translate;
        for(int i=0;i<monVortex.npoints-1;i++){
            double lon = Math.sqrt(((monVortex.xpoints[i+1]-monVortex.xpoints[i])*(monVortex.xpoints[i+1]-monVortex.xpoints[i]))/8d+((monVortex.ypoints[i+1]-monVortex.ypoints[i])*(monVortex.ypoints[i+1]-monVortex.ypoints[i]))/8d);
            double rotat = getAngle((monVortex.xpoints[i+1]-monVortex.xpoints[i]),(monVortex.ypoints[i+1]-monVortex.ypoints[i]));
            translate = new Transform3D();
            translate.set(new Vector3f((monVortex.xpoints[i]-(maxX/2f))/2f,(monVortex.ypoints[i]-(maxY/2f)+1)/2f, 0.0f));
            TransformGroup CylindreTGT2 = new TransformGroup(translate);
            CylindreTGT2.addChild((new Fleche3D((float)rotat,(float)lon, vortexAppear)).getBG());
            VortexBG.addChild(CylindreTGT2);
        }
        
        VortexBG.compile();
    }
    
    public double getAngle(int X, int Y){
        double res = 0d;
        if(X==0)
            if(Y>=0)
                res = Math.PI/2d;
            else
                res = (3d*Math.PI)/2d;
        else{
            double YsurX = (double)Math.abs(Y)/(double)Math.abs(X);
            
            if((X >= 0) && (Y >= 0))
                res= Math.atan(YsurX);
            if((X < 0) && (Y >= 0))
                res = Math.PI-Math.atan(YsurX);
            if((X < 0) && (Y < 0))
                res = Math.PI+Math.atan(YsurX);
            if((X >= 0) && (Y < 0))
                res = (2*Math.PI)-Math.atan(YsurX);
        }
        return res;
    }
    
    public BranchGroup getBG(){
        return VortexBG;
    }
    
}
