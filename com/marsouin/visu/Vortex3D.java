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
package com.marsouin.visu;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.marsouin.data.Loop;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.sqrt;

public class Vortex3D {
    
    private Loop monVortex;
    private BranchGroup VortexBG;
    
    /** Creates a new instance of Vortex3D */
    public Vortex3D(Loop v, int maxX, int maxY, ColoringAttributes c) {
        monVortex = v;
        
        VortexBG = new BranchGroup();
        
        Appearance vortexAppear = new Appearance();
        ColoringAttributes caL1 = c;
        vortexAppear.setColoringAttributes(caL1);
        Transform3D translate;
        for(int i=0;i<monVortex.npoints-1;i++){
            double lon = sqrt(((monVortex.xpoints[i+1]-monVortex.xpoints[i])*(monVortex.xpoints[i+1]-monVortex.xpoints[i]))/8d+((monVortex.ypoints[i+1]-monVortex.ypoints[i])*(monVortex.ypoints[i+1]-monVortex.ypoints[i]))/8d);
            double rotat = getAngle((monVortex.xpoints[i+1]-monVortex.xpoints[i]),(monVortex.ypoints[i+1]-monVortex.ypoints[i]));
            translate = new Transform3D();
            translate.set(new Vector3f((monVortex.xpoints[i]-(maxX/2f))/2f,(monVortex.ypoints[i]-(maxY/2f)+1)/2f, 0.0f));
            TransformGroup CylindreTGT2 = new TransformGroup(translate);
            CylindreTGT2.addChild((new Arrow3D((float)rotat,(float)lon, vortexAppear)).getBG());
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
            double YsurX = (double)abs(Y)/(double)abs(X);
            
            if((X >= 0) && (Y >= 0))
                res= atan(YsurX);
            if((X < 0) && (Y >= 0))
                res = Math.PI-atan(YsurX);
            if((X < 0) && (Y < 0))
                res = Math.PI+atan(YsurX);
            if((X >= 0) && (Y < 0))
                res = (2*Math.PI)-atan(YsurX);
        }
        return res;
    }
    
    public BranchGroup getBG(){
        return VortexBG;
    }
    
}
