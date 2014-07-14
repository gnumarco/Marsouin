/*
 * OceanFile.java
 *
 * Created on 18 septembre 2002, 12:58
 */

/*
 * @author Fonlupt,Robillard,Mahler,Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */
package goemanalysis;

import data.*;

public class ResultatFile implements constants.centre {

    private String nomCompletFichier;

    public ResultatFile(String s) {
        nomCompletFichier = s;
    }

    public void entete(final DataMap c, writeTextFile ecrit) {
        ecrit.uneLigne(" # carte analysee : ");
        ecrit.uneLigne(" datacarte " + c.getFileName());
        ecrit.uneLigne("");
    }

    // ecrit un fichier contenant les resultats D'UNE METHODE
    public void sauverUneMethodeCentre(final DataMap c, final int FLAGMETHOD) {
        try {
            writeTextFile ecrit = new writeTextFile(nomCompletFichier);
            int i, j;
            ecrit.uneLigne(" # Fichier de resultats pour 1 methode ");
            entete(c, ecrit);
            ecrit.uneLigne(COMMENT[FLAGMETHOD]);
            for (i = 0; i < c.getXSize(); i++) {
                for (j = 0; j < c.getYSize(); j++) {
                    if (c.getC(i, j).getCfgCentre(FLAGMETHOD)) {
                        ecrit.unPointEnLigne(new java.awt.Point(i, j));
                    }
                }
            }
            ecrit.fermer();
            ecrit = null;
        } catch (Exception e) {
            System.out.println(" erreur d ecriture de ResultFile ");
        }
    }
// ecrit un fichier contenant les resultats DE TOUTES LES METHODES

    public void sauverLesCentres(final DataMap c) {
        try {
            int i, j, FLAGMETHOD;
            writeTextFile ecrit = null;
            java.util.ArrayList[] lst = new java.util.ArrayList[LENGTH_CFGCENTRE];
            for (i = 0; i < lst.length; i++) {
                lst[i] = new java.util.ArrayList();
            }

            for (i = 0; i < c.getXSize(); i++) {
                for (j = 0; j < c.getYSize(); j++) {
                    for (FLAGMETHOD = 0; FLAGMETHOD < (LENGTH_CFGCENTRE); FLAGMETHOD++) {
                        if (c.getC(i, j).getCfgCentre(FLAGMETHOD)) {
                            lst[FLAGMETHOD].add(new java.awt.Point(i, j));
                        }
                    }
                }
            }
            // ecriture
            ecrit = new writeTextFile(nomCompletFichier);
            ecrit.uneLigne(" # Fichier de resultats ");
            entete(c, ecrit);
            for (FLAGMETHOD = 0; FLAGMETHOD < (LENGTH_CFGCENTRE); FLAGMETHOD++) {
                if (!lst[FLAGMETHOD].isEmpty()) {
                    ecrit.uneLigne(COMMENT[FLAGMETHOD]);
                    ecrit.unVecteurDePointsEnLigne(lst[FLAGMETHOD]);
                    ecrit.uneLigne();
                }
            }
            ecrit.fermer();
            ecrit = null;
        } catch (Exception e) {
            System.out.println(" erreur d ecriture de ResultFile ");
        }
    }

}

//*****************************************************************************
// **********************************************A REFAIRE
/*    public void LireResultat(final DataMap c){
 try{
 int FLAGMETHOD;
 readTextFile resFile = new readTextFile(nomCompletFichier);
 int i,j;
 boolean EcrirePoint;
 for ( i=0; i<c.getXSize(); i++)
 for ( j=0; j<c.getYSize(); j++){
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
