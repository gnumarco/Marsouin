/*
 * ResultatFile.java
 *
 * Created on 18 avril 2003, 12:58
 */

/*
 * @author Mahler
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package physic;

import data.*;
import java.util.Vector;

public class ResultatFile {

    private String nomCompletFichier;


    public ResultatFile(String s) {
    nomCompletFichier= s;
    }
    public double COEFF_MULTIPLICATIF = 1000000.0;
    
    public void sauverLeCurl(final DataCarte c){
        try{
            int i,j;
            writeTextFile ecrit = null;
            Vector meslignes = new Vector();
            String ligne = null;
            
            ligne = new String(" ROTATIONNEL ");
            for ( i=0; i<c.getTailleX(); i++)
	           ligne = ligne + "\t "+ i +".";
            meslignes.addElement(ligne);
            
            for ( j=0; j<c.getTailleY(); j++)
            {
                ligne = new String(j+". ");
                for ( i=0; i<c.getTailleX(); i++){
	              if (Moteur.courantDerivable(c,i,j)) {
                          ligne = ligne + "\t "+ (long)(c.getC(i,j).getCurl()*COEFF_MULTIPLICATIF);
                      }
                      else ligne = ligne + "\t "+"0";
                }
                meslignes.addElement(ligne);
                ligne=null;
            }
            // ecriture
            ecrit= new writeTextFile(nomCompletFichier);
            for (i=0;i<meslignes.size();i++)
                ecrit.uneLigne((String)meslignes.elementAt(i));
            
            ecrit.fermer();
            ecrit = null;
    }catch(Exception e){System.out.println(" erreur d ecriture de ResultFile ");e.printStackTrace();}
    }

        public void sauverLaDiv(final DataCarte c){
        try{
            int i,j;
            writeTextFile ecrit = null;
            Vector meslignes = new Vector();
            String ligne = null;
            
            ligne = new String(" DIVERGENCE ");
            for ( i=0; i<c.getTailleX(); i++)
	           ligne = ligne + "\t "+ i +".";
            meslignes.addElement(ligne);
            
            for ( j=0; j<c.getTailleY(); j++)
            {
                ligne = new String(j+". ");
                for ( i=0; i<c.getTailleX(); i++){
	              if (Moteur.courantDerivable(c,i,j))
                        ligne = ligne + "\t "+ (long)(c.getC(i,j).getDiv()*COEFF_MULTIPLICATIF);
                      else ligne = ligne + "\t "+"0";
                }
                meslignes.addElement(ligne);
                ligne=null;
            }
            // ecriture
            ecrit= new writeTextFile(nomCompletFichier);
            for (i=0;i<meslignes.size();i++)
                ecrit.uneLigne((String)meslignes.elementAt(i));
            
            ecrit.fermer();
            ecrit = null;
    }catch(Exception e){System.out.println(" erreur d ecriture de ResultFile ");}
    }

    public void sauverLaValeurAbsolueDuCurl(final DataCarte c){
        try{
            int i,j;
            writeTextFile ecrit = null;
            Vector meslignes = new Vector();
            String ligne = null;
            long val;
            
            ligne = new String(" ROTATIONNEL ");
            for ( i=0; i<c.getTailleX(); i++)
	           ligne = ligne + "\t "+ i +".";
            meslignes.addElement(ligne);
            
            for ( j=0; j<c.getTailleY(); j++)
            {
                ligne = new String(j+". ");
                for ( i=0; i<c.getTailleX(); i++){
	              if (Moteur.courantDerivable(c,i,j)) {
                          val = (long)(c.getC(i,j).getCurl()*COEFF_MULTIPLICATIF);
                          if (val<0) val = - val;
                          ligne = ligne + "\t "+ val;
                      }
                      else ligne = ligne + "\t "+"0";
                }
                meslignes.addElement(ligne);
                ligne=null;
            }
            // ecriture
            ecrit= new writeTextFile(nomCompletFichier);
            for (i=0;i<meslignes.size();i++)
                ecrit.uneLigne((String)meslignes.elementAt(i));
            
            ecrit.fermer();
            ecrit = null;
    }catch(Exception e){System.out.println(" erreur d ecriture de ResultFile ");e.printStackTrace();}
    }

    public void sauverLaValeurAbsolueDeLaDivergence(final DataCarte c){
        try{
            int i,j;
            writeTextFile ecrit = null;
            Vector meslignes = new Vector();
            String ligne = null;
            long val;
            
            ligne = new String(" DIVERGENCE ");
            for ( i=0; i<c.getTailleX(); i++)
	           ligne = ligne + "\t "+ i +".";
            meslignes.addElement(ligne);
            
            for ( j=0; j<c.getTailleY(); j++)
            {
                ligne = new String(j+". ");
                for ( i=0; i<c.getTailleX(); i++){
	              if (Moteur.courantDerivable(c,i,j)) {
                        val = (long)(c.getC(i,j).getDiv()*COEFF_MULTIPLICATIF);
                        if (val<0) val = -val;
                        ligne = ligne + "\t "+ val;
                      }
                      else ligne = ligne + "\t "+"0";
                }
                meslignes.addElement(ligne);
                ligne=null;
            }
            // ecriture
            ecrit= new writeTextFile(nomCompletFichier);
            for (i=0;i<meslignes.size();i++)
                ecrit.uneLigne((String)meslignes.elementAt(i));
            
            ecrit.fermer();
            ecrit = null;
    }catch(Exception e){System.out.println(" erreur d ecriture de ResultFile ");}
    }
    /*
     public void entete(final DataCarte c,writeTextFile ecrit) {
            ecrit.uneLigne(" # carte analysee : ");
            ecrit.uneLigne(" datacarte " + c.getNomFichier());
            ecrit.uneLigne("");
    }

    // ecrit un fichier contenant les resultats D'UNE METHODE
    public void sauverUneMethodeCentre(final DataCarte c,final int FLAGMETHOD){
        try{
            writeTextFile ecrit = new writeTextFile(nomCompletFichier);
            int i,j;
            ecrit.uneLigne(" # Fichier de resultats pour 1 methode ");
            entete(c,ecrit);
            ecrit.uneLigne(COMMENT[FLAGMETHOD]);
            for ( i=0; i<c.getTailleX(); i++)
            for ( j=0; j<c.getTailleY(); j++){
                if (c.getC(i,j).getCfgCentre(FLAGMETHOD))
                    ecrit.unPointEnLigne(new java.awt.Point(i,j));
            }
            ecrit.fermer();
            ecrit = null;
    }catch(Exception e){System.out.println(" erreur d ecriture de ResultFile ");}
    }
// ecrit un fichier contenant les resultats DE TOUTES LES METHODES
    public void sauverLesCentres(final DataCarte c){
        try{
            int i,j,FLAGMETHOD;
            writeTextFile ecrit = null;
            java.util.Vector[] lst = new java.util.Vector [LENGTH_CFGCENTRE];
            for( i =0; i< lst.length; i++)
                lst[i] = new java.util.Vector();

            for ( i=0; i<c.getTailleX(); i++)
            for ( j=0; j<c.getTailleY(); j++)
	          for (FLAGMETHOD = 0; FLAGMETHOD<(LENGTH_CFGCENTRE);FLAGMETHOD++)
                      if (c.getC(i,j).getCfgCentre(FLAGMETHOD))
                          lst[FLAGMETHOD].add(new java.awt.Point(i,j));
            // ecriture
            ecrit= new writeTextFile(nomCompletFichier);
            ecrit.uneLigne(" # Fichier de resultats ");
            entete(c,ecrit);
            for (FLAGMETHOD = 0; FLAGMETHOD<(LENGTH_CFGCENTRE);FLAGMETHOD++)
                if(!lst[FLAGMETHOD].isEmpty()){
                    ecrit.uneLigne(COMMENT[FLAGMETHOD]);
                    ecrit.unVecteurDePointsEnLigne(lst[FLAGMETHOD]);
                    ecrit.uneLigne();
                }
            ecrit.fermer();
            ecrit = null;
    }catch(Exception e){System.out.println(" erreur d ecriture de ResultFile ");}
    }
    */

}

//*****************************************************************************
// **********************************************A REFAIRE
/*    public void LireResultat(final DataCarte c){
	        try{
	            int FLAGMETHOD;
	            readTextFile resFile = new readTextFile(nomCompletFichier);
	            int i,j;
	            boolean EcrirePoint;
	            for ( i=0; i<c.getTailleX(); i++)
	            for ( j=0; j<c.getTailleY(); j++){
	                EcrirePoint = true;
	                for (FLAGMETHOD = 0; FLAGMETHOD<(NB_METHODES*NB_TYPES);FLAGMETHOD++){
	                     if (c.getC(i,j).getCfgCentre(FLAGMETHOD)) {
	                         if (EcrirePoint) {
	                             //ecrit.ecrireLigne("En "+i+" ; "+j);
	                             EcrirePoint = false;
	                         }
	                         //ecrit.ecrireLigne(STRINGCENTRE[FLAGMETHOD]);
	                     }
                        }
                    }
                }catch(Exception e){System.out.println(" erreur d ecriture de ResultFile ");}
    }
*/


