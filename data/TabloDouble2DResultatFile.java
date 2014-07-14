/*
 * ResultatFile.java
 *
 * Created on 18 avril 2003, 12:58
 */

/*
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
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