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
package data;

import java.util.ArrayList;

public class TabloDouble2DResultatFile {

    private String nomCompletFichier;


    public TabloDouble2DResultatFile(String s) {
    nomCompletFichier= s;
    }
    
    public double COEFF_MULTIPLICATIF = 1.0;
    
    
    public void sauver( double[][] t){
        try{
            int i,j;
            writeTextFile ecrit = null;
            ArrayList meslignes = new ArrayList();
            String ligne = null;
            
            ligne = new String(t.length + "" +t[0].length);
            meslignes.add(ligne);
            
            for ( j=0; j<t.length; j++)
            {
                ligne = new String("");
                for ( i=0; i<t[j].length; i++){
	                  ligne = ligne + "\t "+ (long)(t[j][i]*COEFF_MULTIPLICATIF);
                      }
                meslignes.add(ligne);
                ligne=null;
            }
            // ecriture
            ecrit= new writeTextFile(nomCompletFichier);
            for (i=0;i<meslignes.size();i++)
                ecrit.uneLigne((String)meslignes.get(i));
            
            ecrit.fermer();
            ecrit = null;
    }catch(Exception e){System.out.println(" erreur d ecriture de ResultFile ");e.printStackTrace();}
    }
}