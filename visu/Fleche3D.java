/*
 * fleche3D.java
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

import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;


public class Fleche3D {

  private BranchGroup flecheBG;

  public Fleche3D(double rotat, double longueur, Appearance a){
      
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
