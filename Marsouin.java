

import visu.*;


public class Marsouin {
    
    /** nouvelle instance (vide) de Marsouin */
    public Marsouin() {
        
    }
    
    public static void main(String[] args) {
        // objet memoire
        boolean Ok3D = loadClass("javax.media.j3d.Canvas3D");
        Memory mem =new Memory();
        // frame de configuration;
        FrmConf fcfg = new FrmConf(mem, Ok3D);
        fcfg.setVisible(true);
        mem.setFrmConfig(fcfg, Ok3D);
    }
    
    public static boolean loadClass(String className)
{
        // First check if the class is already loaded
        boolean found = true;
        try{
        Class c = java.lang.ClassLoader.getSystemClassLoader().loadClass(className);
        }catch(ClassNotFoundException e){found = false;System.out.println("No Java3D");}
        
    return found;
    }
}
