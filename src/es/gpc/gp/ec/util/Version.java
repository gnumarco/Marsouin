/* 
 * Copyright (C) 2014 Marc Segond <dr.marc.segond@gmail.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package es.gpc.gp.ec.util;
import java.util.*;

/* 
 * Version.java
 * 
 * Created: Wed Aug 11 19:44:46 1999
 * By: Sean Luke
 */

/**
 * Version is a static class which stores version information for this
 * evolutionary computation system.
 *
 * @author Sean Luke
 * @version 1.0 
 */

public class Version
    {
    public static final String name = "GP-Control Genetic Programming core based on ECJ 21";
    public static final String version = "0.1";
    public static final String copyright = "2014";
    public static final String author = "Marc Segond";
//    public static final String contributors = "L. Panait, G. Balan, S. Paus, Z. Skolicki, R. Kicinger,";
//    public static final String contributors2 = "E. Popovici, K. Sullivan, J. Harrison, J. Bassett, R. Hubley,";
//    public static final String contributors3 = "A. Desai, A. Chircop, J. Compton, W. Haddon, S. Donnelly,";
//    public static final String contributors4 = "B. Jamil, J. Zelibor, E. Kangas, F. Abidi, H. Mooers,";
//    public static final String contributors5 = "J. O'Beirne, L. Manzoni, K. Talukder, and J. McDermott";
//    public static final String authorEmail0 = "ecj-help";
//    public static final String authorEmail1 = "cs.gmu.edu";
//    public static final String authorEmail2 = "(better: join ECJ-INTEREST at URL above)";
//    public static final String authorURL = "http://marcsegond.com";
    public static final String date = "May 1, 2014";
    public static final String minimumJavaVersion = "1.7";

    public static final String message()
        {
        Properties p = System.getProperties();
        String javaVersion = p.getProperty("java.version");
        String javaVM = p.getProperty("java.vm.name");
        String javaVMVersion = p.getProperty("java.vm.version");
        if (javaVM!=null) javaVersion = javaVersion + " / " + javaVM;
        if (javaVM!=null && javaVMVersion!=null) javaVersion = javaVersion + "-" + javaVMVersion;
        
    
        return 
            "\n| " + name + 
            "\n| An evolutionary computation system (version " + version + ")" +
            //"\n| Copyright " + copyright + */ " By " + author +
            "\n| By " + author + 
//            "\n| Contributors: " + contributors +
//            "\n|               " + contributors2 +
//            "\n|               " + contributors3 +
//            "\n|               " + contributors4 +
//            "\n|               " + contributors5 +
//            "\n| URL: " + authorURL +
//            "\n| Mail: " + authorEmail0 + "@" + authorEmail1 +
//            "\n|       " + authorEmail2 + 
            "\n| Date: " + date +
            "\n| Current Java: " + javaVersion +
            "\n| Required Minimum Java: " + minimumJavaVersion +
            "\n\n";
        }
    }
