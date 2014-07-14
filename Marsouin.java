/** Marsouin.java */


import visu.*;

/** Created on 2 octobre 2002, 15:12
 * @author Marc Segond
 * 
 */

public class Marsouin {
    
    /** nouvelle instance (vide) de Marsouin */
    public Marsouin() {
        
    }
    
    /** c'est la methode de lancement du logiciel
     * une interface graphique de configuration apparait !
     * @param args pas de parametres en ligne de commande
     */
    public static void main(String[] args) {
        // objet memoire
        boolean Ok3D = loadClass("javax.media.j3d.Canvas3D");
        Memoire mem =new Memoire();
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
