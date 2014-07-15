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
import static java.lang.ClassLoader.getSystemClassLoader;
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
        Class c = getSystemClassLoader().loadClass(className);
        }catch(ClassNotFoundException e){found = false;System.out.println("No Java3D");}
        
    return found;
    }
}
