/*
 * Canvas3D.java
 *
 * Created on 31 mars 2005, 21:26
 */

package visu;

import com.sun.j3d.utils.behaviors.mouse.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import data.BatchDataMap;
import data.Loop;

/**
 *
 * @author Marc Segond
 */
public class Canv3D {
    
    private SimpleUniverse u = null;
    private BatchDataMap mother;
    private Canvas3D c;
    
    /** Creates a new instance of Canvas3D */
    public Canv3D(BatchDataMap m) {
        System.out.println("Constructeur canvas 3D");
        mother = m;
        make3D();
    }
    
    public Canvas3D getCanvas3D(){
        return c;
    }
    
    private void make3D(){
        
        try{
            GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
            
            c = new Canvas3D(config);
            c.setDoubleBufferEnable(true);
            
            // Create a simple scene and attach it to the virtual universe
            BranchGroup scene = createSceneGraph();
            u = new SimpleUniverse(c);
            
            // This will move the ViewPlatform back a bit so the
            // objects in the scene can be viewed.
            u.getViewingPlatform().setNominalViewingTransform();
            
            u.addBranchGraph(scene);
        }catch(java.lang.Exception e){javax.swing.JOptionPane.showMessageDialog(null,"Probl�me de configuration 3D, veuillez v�rifier que Java3D est install�\nErreur : "+e.toString());}
    }
    
    public BranchGroup createSceneGraph() throws java.lang.Exception{
        // Create the root of the branch graph
        try{
            BranchGroup objRoot = new BranchGroup();
            
            BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
            
            
            
            // Create a Transformgroup to scale all objects so they
            // appear in the scene.
            TransformGroup objScale = new TransformGroup();
            Transform3D t3d = new Transform3D();
            t3d.setScale(0.03);
            objScale.setTransform(t3d);
            objRoot.addChild(objScale);
            
            TransformGroup objTransMaster = new TransformGroup();
            objTransMaster.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            objTransMaster.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            objScale.addChild(objTransMaster);
            Background bg = new Background(new Color3f(1.0f,0.5f,1.0f));
            objTransMaster.addChild(bg);
            
            // Create the TransformGroup node and initialize it to the
            // identity. Enable the TRANSFORM_WRITE capability so that
            // our behavior code can modify it at run time. Add it to
            // the root of the subgraph.
            for(int w=0;w<mother.getNbDataCartesProf();w++){
                Transform3D transl = new Transform3D();
                transl.set(new Vector3f(0.0f, 0.0f, (float)w*-8f));
                TransformGroup objTransMast = new TransformGroup(transl);
                BranchGroup objTrans = new BranchGroup();
                objTransMast.addChild(objTrans);
                Appearance flecheAppear;
                ColoringAttributes caL1;
                for(int k=-(mother.getDataCarte(0,w).getYSize()/2);k<((mother.getDataCarte(0,w).getYSize()/2)+(mother.getYSize()%2));k++){
                    Transform3D translate = new Transform3D();
                    translate.set(new Vector3f(0.0f,(float)-k*0.5f, 0.0f));
                    TransformGroup objTransInter = new TransformGroup(translate);
                    for(int i=-(mother.getDataCarte(0,w).getXSize()/2);i<((mother.getDataCarte(0,w).getXSize()/2)+(mother.getDataCarte(0,w).getXSize()%2));i++){
                        int l = i+(mother.getDataCarte(0,w).getXSize()/2);
                        int m = (mother.getDataCarte(0,w).getYSize()-1)-(k+(mother.getDataCarte(0,w).getYSize()/2));
                        if(mother.getDataCarte(0,w).getOcean()[l][m].norme != 0d){
                            flecheAppear = new Appearance();
                            caL1 = new ColoringAttributes();
                            caL1.setColor(new Color3f(0.0f,0.5f,1.0f));
                            flecheAppear.setColoringAttributes(caL1);
                            BranchGroup objBis = new BranchGroup();
                            translate = new Transform3D();
                            translate.set(new Vector3f((float)i*0.5f, 0.0f, 0.0f));
                            TransformGroup objTransBis = new TransformGroup(translate);
                            objBis.addChild(objTransBis);
                            Fleche3D fl = new Fleche3D(mother.getDataCarte(0,w).getOcean()[l][m].getAngle(),mother.getDataCarte(0,w).getOcean()[l][m].norme,flecheAppear);
                            objTransBis.addChild(fl.getBG());
                            objTransInter.addChild(objBis);
                        }
                    }
                    objTrans.addChild(objTransInter);
                }
                BranchGroup objTransInter = new BranchGroup();
                if(mother.getVortexAnt().size()!=0){
                    for(int i=0;i<mother.getDataCarte(0,w).getVortexAnt().size();i++){
                        caL1 = new ColoringAttributes();
                        float[] cols = mother.getDataCarte(0,w).getVortexAnt().getMetaVortex(i).getCouleur().getRGBColorComponents(null);
                        caL1.setColor(new Color3f(cols[0],cols[1],cols[2]));
                        Vortex3D v = new Vortex3D((Loop)mother.getDataCarte(0,w).getVortexAnt().getMetaVortex(i),mother.getDataCarte(0,w).getXSize(),mother.getDataCarte(0,w).getYSize(),caL1);
                        objTransInter.addChild(v.getBG());
                    }
                    objTransMast.addChild(objTransInter);
                }
                objTransMaster.addChild(objTransMast);
            }
            
            // Create the rotate behavior node
            MouseRotate behavior = new MouseRotate(objTransMaster);
            objTransMaster.addChild(behavior);
            behavior.setSchedulingBounds(bounds);
            
            // Create the zoom behavior node
            MouseZoom behavior2 = new MouseZoom(objTransMaster);
            objTransMaster.addChild(behavior2);
            behavior2.setSchedulingBounds(bounds);
            
            // Create the translate behavior node
            MouseTranslate behavior3 = new MouseTranslate(objTransMaster);
            objTransMaster.addChild(behavior3);
            behavior3.setSchedulingBounds(bounds);
            
            Light lght = new AmbientLight(new Color3f(0.2f,0.2f,0.8f));
            lght.setInfluencingBounds(bounds);
            objTransMaster.addChild(lght);
            // Have Java 3D perform optimizations on this scene graph.
            objRoot.compile();
            
            return objRoot;
        }catch(java.lang.Exception e){throw(e);}
    }
    
}
