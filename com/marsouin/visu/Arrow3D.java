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

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;


public class Arrow3D {

  private final BranchGroup flecheBG;

  public Arrow3D(double rotat, double longueur, Appearance a){
      
    float lon = (float)longueur*1.5f;
      
    flecheBG = new BranchGroup();
    BranchGroup flecheBG2 = new BranchGroup();
    Transform3D rotate = new Transform3D();
    Transform3D translate = new Transform3D();
    

    rotate.rotZ(-Math.PI/2.0d);
    TransformGroup flecheTGR1 = new TransformGroup(rotate);

    translate.set(new Vector3f(lon, 0.0f, 0.0f));
    TransformGroup flecheTGT1 = new TransformGroup(translate);

    Cone cone1 = new Cone(lon/10f,lon/5f);
    cone1.setAppearance(a);

    flecheBG2.addChild(flecheTGT1);
    flecheTGT1.addChild(flecheTGR1);
    flecheTGR1.addChild(cone1);

    rotate.rotZ(Math.PI/2.0d);
    TransformGroup flecheTGR2 = new TransformGroup(rotate);

    translate.set(new Vector3f(lon/2f, 0.0f, 0.0f));
    TransformGroup flecheTGT2 = new TransformGroup(translate);

    Cylinder cone2 = new Cylinder(lon/50f,lon);
    cone2.setAppearance(a);
    
    flecheTGT2.addChild(flecheTGR2);
    flecheTGR2.addChild(cone2);
    flecheBG2.addChild(flecheTGT2);
    
    rotate.rotZ((double)rotat);
    TransformGroup GlobalRot = new TransformGroup(rotate);
    
    GlobalRot.addChild(flecheBG2); 
    
    flecheBG.addChild(GlobalRot);
    
    flecheBG.compile();

  }

  public BranchGroup getBG(){
    return flecheBG;
  }

}
