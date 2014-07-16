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
package com.marsouin.data;

import java.io.IOException;

public class WriteTextFile {

    private java.io.FileWriter fw;
    private java.io.BufferedWriter bw;

    public WriteTextFile(String s) throws Exception{
        try{
             fw= new java.io.FileWriter(s);
             bw = new java.io.BufferedWriter(fw);
        }catch(java.io.FileNotFoundException e){ throw e;}
    }

    public void uneLigne(String ln) {
        try{
          bw.write(ln);
          bw.newLine();
          bw.flush();
        }catch(IOException e){System.out.println("Le fichier specifie n'existe pas");}
    }
    public void uneLigne() {
        try{
          bw.newLine();
          bw.flush();
        }catch(IOException e){System.out.println("Le fichier specifie n'existe pas");}
    }

    public void unPointEnLigne(java.awt.Point p) {
        uneLigne( unPoint(p));
    }
    public String unPoint (java.awt.Point p){
        return ("( "+ p.x+" ; "+p.y+" )");
    }
    public void unVecteurDePointsEnLigne(java.util.ArrayList v) {
         String tmp="";
         int i;
         try{
             if (v.get(0) instanceof java.awt.Point){
                 tmp = unPoint((java.awt.Point) v.get(0));
                 for(i=1;i<v.size(); i++)
                      if (v.get(i) instanceof java.awt.Point)
                        tmp = tmp + "_" + unPoint((java.awt.Point)v.get(i));
             }

         }catch(Exception e){System.out.println("writeTexteFile : erreur d'ecriture");}
         if (tmp == null) tmp="null";
         uneLigne(tmp);
    }


    public void fermer(){
        try{
            bw.close();
        }catch(IOException e){ System.out.println("bufferedWriter close "+ e);  }
        try{
            fw.close();
        }catch(IOException e){ System.out.println("fileWriter close "+ e);}
    }
}
