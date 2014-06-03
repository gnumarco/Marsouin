/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.gpc.gp.ec.app.control;

/**
 *
 * @author Marc Segond
 */


import java.io.IOException;
import java.io.*;
import java.util.EnumSet;


/**
 * Created by IntelliJ IDEA.
 * User: bbejeck
 * Date: 1/22/12
 * Time: 12:46 PM
 */

public class DirUtils {

    public void recursiveCopy(File fSource, File fDest) {
     try {
          if (fSource.isDirectory()) {
          // A simple validation, if the destination is not exist then create it
               if (!fDest.exists()) {
                    fDest.mkdirs();
               }
 
               // Create list of files and directories on the current source
               // Note: with the recursion 'fSource' changed accordingly
               String[] fList = fSource.list();
 
               for (int index = 0; index < fList.length; index++) {
                    File dest = new File(fDest, fList[index]);
                    File source = new File(fSource, fList[index]);
 
                    // Recursion call take place here
                    recursiveCopy(source, dest);
               }
          }
          else {
               // Found a file. Copy it into the destination, which is already created in 'if' condition above
 
               // Open a file for read and write (copy)
               FileInputStream fInStream = new FileInputStream(fSource);
               FileOutputStream fOutStream = new FileOutputStream(fDest);
 
               // Read 2K at a time from the file
               byte[] buffer = new byte[2048];
               int iBytesReads;
 
               // In each successful read, write back to the source
               while ((iBytesReads = fInStream.read(buffer)) >= 0) {
                    fOutStream.write(buffer, 0, iBytesReads);
               }
 
               // Safe exit
               if (fInStream != null) {
                    fInStream.close();
               }
 
               if (fOutStream != null) {
                    fOutStream.close();
               }
               fDest.setExecutable(true);
          }
     }
     catch (Exception ex) {
          // Please handle all the relevant exceptions here
     }
}
}