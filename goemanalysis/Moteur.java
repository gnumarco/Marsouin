/*
 * Moteur.java
 *
 * Created on 24 septembre 2002, 19:00
 */

/*
 * @author Fonlupt,Robillard,Mahler,Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.1
 *
 */
package goemanalysis;

import data.DataMap;
import data.TabloDouble2D;
import data.VortexGeom;

import java.util.ArrayList;

public class Moteur implements constants.centre, constants.courant {

    // moteur ne fait que des methodes !!
    /**
     * indicateur ( en pourcentage !)
     */
    public static int avancement = 0;
    private int step;

    /**
     * tableau de TabloDouble2D des r�sultats : pour chercher les Maxima Locaux
     * de chaque methode meme taille que CHECKBOX_CENTRE dans memoire
     */
    private TabloDouble2D[] tabFitness = null;

    /**
     * constructeur
     */
    public Moteur() {
    }

    /**
     * destructeur
     */
    protected void finalize() {
        free(tabFitness);
    }

    /**
     * libere la memoire
     */
    public void dispose() {
        free(tabFitness);
    }

    private void free(TabloDouble2D[] t) {
        if ((t != null) && (t.length > 0)) {
            int i;
            for (i = 0; i < t.length; i++) {
                if (t[i] != null) {
                    t[i].dispose();
                    t[i] = null;
                }
            }
        }
        t = null;
    }

    public void init(boolean[] methodesCochees) {
        int i;
        step = 0;
        for (i = 0; i < methodesCochees.length; i++) {
            if (methodesCochees[i]) {
                step++;
            }
        }
        if (step != 0) {
            step = (100 / step) + 1;
        }
        avancement = 0;
    }

    /**
     * fonction qui renseigne sur l'avancement
     */
    public int getAvancement() {
        return avancement;
    }

    /**
     * fonction de calcul de norme euclydienne
     */
    public double calculNorme(double vx, double vy) {
        if ((java.lang.Math.abs(vx) > SEUIL_NORME) | (java.lang.Math.abs(vy) > SEUIL_NORME)) {
            return java.lang.Math.sqrt(vx * vx + vy * vy);
        } else {
            return 0.0;
        }
    }

    /**
     * cette fonction ecrit les coordonn�es des centres trouv�s avec les
     * methodes du moteur
     */
    public void sauveResultats(DataMap c, String nFRes) {
        ResultatFile res = new ResultatFile(nFRes);
        res.sauverLesCentres(c);
        res = null;
    }

    /**
     * cette fonction va tager chaque courant suivant le succes des methodes
     * demand�es = old fonction de Memoire
     */
    public void majBooleens(DataMap c, boolean[] methode, double[] limite) {
        this.dispose();

        int i, j;

        for (i = 0; i < c.getXSize(); i++) {
            for (j = 0; j < c.getYSize(); j++) {
                c.getC(i, j).resetCfgCentre();
            }
        }

        if (methode[VECTEURS * NB_TYPES + TYPE_PLUS]) {
            System.out.println(COMMENT[VECTEURS * NB_TYPES + TYPE_PLUS]);
            j = this.majCVP(c, limite[VECTEURS * NB_TYPES + TYPE_PLUS] * c.getMaxNorm());
            System.out.println(j + "trouves");
        }
        if (methode[VECTEURS * NB_TYPES + TYPE_X]) {
            System.out.println(COMMENT[VECTEURS * NB_TYPES + TYPE_X]);
            j = this.majCVX(c, limite[VECTEURS * NB_TYPES + TYPE_X] * c.getMaxNorm());
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS * NB_TYPES + TYPE_O]) {
            System.out.println(COMMENT[VECTEURS * NB_TYPES + TYPE_O]);
            j = this.majCVO(c, limite[VECTEURS * NB_TYPES + TYPE_O] * c.getMaxNorm());
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS * NB_TYPES + TYPE_CARRE]) {
            System.out.println(COMMENT[VECTEURS * NB_TYPES + TYPE_CARRE]);
            j = this.majCVC(c, limite[VECTEURS * NB_TYPES + TYPE_CARRE] * c.getMaxNorm());
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CU * NB_TYPES + TYPE_PLUS]) {
            System.out.println(COMMENT[VECTEURS_CU * NB_TYPES + TYPE_PLUS]);
            j = this.majCVUP(c, limite[VECTEURS_CU * NB_TYPES + TYPE_PLUS]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CU * NB_TYPES + TYPE_X]) {
            System.out.println(COMMENT[VECTEURS_CU * NB_TYPES + TYPE_X]);
            j = this.majCVUX(c, limite[VECTEURS_CU * NB_TYPES + TYPE_X]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CU * NB_TYPES + TYPE_O]) {
            System.out.println(COMMENT[VECTEURS_CU * NB_TYPES + TYPE_O]);
            j = this.majCVUO(c, limite[VECTEURS_CU * NB_TYPES + TYPE_O]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CU * NB_TYPES + TYPE_CARRE]) {
            System.out.println(COMMENT[VECTEURS_CU * NB_TYPES + TYPE_CARRE]);
            j = this.majCVUC(c, limite[VECTEURS_CU * NB_TYPES + TYPE_CARRE]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_TANGENTS * NB_TYPES + TYPE_PLUS]) {
            System.out.println(COMMENT[VECTEURS_TANGENTS * NB_TYPES + TYPE_PLUS]);
            j = this.majCVTP(c, 1.0 / (limite[VECTEURS_TANGENTS * NB_TYPES + TYPE_PLUS]));
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_TANGENTS * NB_TYPES + TYPE_X]) {
            System.out.println(COMMENT[VECTEURS_TANGENTS * NB_TYPES + TYPE_X]);
            j = this.majCVTX(c, 1.0 / (limite[VECTEURS_TANGENTS * NB_TYPES + TYPE_X]));
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_TANGENTS * NB_TYPES + TYPE_O]) {
            System.out.println(COMMENT[VECTEURS_TANGENTS * NB_TYPES + TYPE_O]);
            j = this.majCVTO(c, 1.0 / (limite[VECTEURS_TANGENTS * NB_TYPES + TYPE_O]));
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_TANGENTS * NB_TYPES + TYPE_CARRE]) {
            System.out.println(COMMENT[VECTEURS_TANGENTS * NB_TYPES + TYPE_CARRE]);
            j = this.majCVTC(c, 1.0 / (limite[VECTEURS_TANGENTS * NB_TYPES + TYPE_CARRE]));
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_PLUS]) {
            System.out.println(COMMENT[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_PLUS]);
            j = this.majCVTUP(c, 1.0 / limite[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_PLUS]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_X]) {
            System.out.println(COMMENT[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_X]);
            j = this.majCVTUX(c, 1.0 / limite[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_X]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_O]) {
            System.out.println(COMMENT[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_O]);
            j = this.majCVTUO(c, 1.0 / limite[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_O]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_CARRE]) {
            System.out.println(COMMENT[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_CARRE]);
            j = this.majCVTUC(c, 1.0 / limite[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_CARRE]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_PLUS]) {
            System.out.println(COMMENT[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_PLUS]);
            j = this.majCVFP(c, c.getMaxNorm() / (limite[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_PLUS]));
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_X]) {
            System.out.println(COMMENT[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_X]);
            j = this.majCVFX(c, c.getMaxNorm() / (limite[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_X]));
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_O]) {
            System.out.println(COMMENT[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_O]);
            j = this.majCVFO(c, c.getMaxNorm() / (limite[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_O]));
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_CARRE]) {
            System.out.println(COMMENT[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_CARRE]);
            j = this.majCVFC(c, c.getMaxNorm() / (limite[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_CARRE]));
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_PLUS]) {
            System.out.println(COMMENT[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_PLUS]);
            j = this.majCVFUP(c, 1.0 / limite[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_PLUS]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_X]) {
            System.out.println(COMMENT[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_X]);
            j = this.majCVFUX(c, 1.0 / limite[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_X]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_O]) {
            System.out.println(COMMENT[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_O]);
            j = this.majCVFUO(c, 1.0 / limite[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_O]);
            System.out.println(j + " trouves ");
        }
        if (methode[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_CARRE]) {
            System.out.println(COMMENT[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_CARRE]);
            j = this.majCVFUC(c, 1.0 / limite[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_CARRE]);
            System.out.println(j + " trouves ");
        }
    }

    /**
     * cette fonction va calculer le "fitness" de chaque methode d�sir�e, en
     * chaque point le stock de ces grandeurs est effectu� dans le tabFitness
     */
    public void calculerFitness(DataMap c, boolean[] methode) {
        this.dispose();
        tabFitness = new TabloDouble2D[methode.length];
        // balayage des methodes coch�es

    }

	//AJOUT LISTE ICI//

    /*
     public  final  int majCVFUC(DataMap c, double limite)
     public  final  int majCVFC(DataMap c, double limite)
     public  final  int majCVFUO(DataMap c, double limite)
     public  final  int majCVFO(DataMap c, double limite)
     public  final  int majCVFUX(DataMap c, double limite)
     public  final  int majCVFX(DataMap c, double limite)
     public  final  int majCVFUP(DataMap c, double limite)
     public  final  int majCVFP(DataMap c, double limite)
     public  final  int majCVTUC(DataMap c, double limite)
     public  final  int majCVTC(DataMap c, double limite)
     public  final  int majCVTUO(DataMap c, double limite)
     public  final  int majCVTO(DataMap c, double limite)
     public  final  int majCVTUX(DataMap c, double limite)
     public  final  int majCVTX(DataMap c, double limite)
     public  final  int majCVTUP(DataMap c, double limite)
     public  final  int majCVTP(DataMap c, double limite)
     public  final  int majCVUC(DataMap c, double limite)
     public  final  int majCVC(DataMap c, double limite)
     public  final  int majCVUO(DataMap c, double limite)
     public  final  int majCVO(DataMap c, double limite)
     public  final  int majCVUX(DataMap c, double limite)
     public  final  int majCVX(DataMap c, double limite)
     public  final  int majCVUP(DataMap c, double limite)
     public  final  int majCVP(DataMap c, double limite)

     */
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SOMME VECTORIELLE SIMPLE @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //*************************************************************************************
    //****************************** centre : somme des vecteurs, sur case PLUS
    public int majCVP(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVP(c, posX, posY);
                ret = booleenCVP(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS * NB_TYPES + TYPE_PLUS, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS*NB_TYPES + TYPE_PLUS] = lstPoint;
        // m.limitC[VECTEURS*NB_TYPES + TYPE_PLUS] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVP(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS * NB_TYPES + TYPE_PLUS] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS * NB_TYPES + TYPE_PLUS];

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVP(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        // TODO t.complementerTablo();
        avancement += step;
    }

    private double fitnessCVP(DataMap c, int posX, int posY) {
        double fit = 1000d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;
            if (c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = calculNorme(c.getC(posX - 1, posY).getXBase() + c.getC(posX + 1, posY).getXBase() + c.getC(posX, posY - 1).getXBase() + c.getC(posX, posY + 1).getXBase()
                    + c.getC(posX, posY).getXBase() * FACTEUR_CENTRE[VECTEURS * NB_TYPES + TYPE_PLUS], c.getC(posX - 1, posY).getYBase() + c.getC(posX + 1, posY).getYBase() + c.getC(posX, posY - 1).getYBase() + c.getC(posX, posY + 1).getYBase()
                    + c.getC(posX, posY).getYBase() * FACTEUR_CENTRE[VECTEURS * NB_TYPES + TYPE_PLUS]);
            fit = fit / POIDS_TOTAL[VECTEURS * NB_TYPES + TYPE_PLUS];
        } catch (Exception e) {
            fit = 1000d;
        }
        return fit;
    }

    private boolean booleenCVP(double fitness, double limite) {
        // limite inferieure
        return (limite > fitness);
    }

    // ------------------------------------------------------------------------------------------
    //--------------------------- centre : somme des vecteurs UNITAIRES, sur case PLUS ----------
    // ------------------------------------------------------------------------------------------
    public int majCVUP(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVUP(c, posX, posY);
                ret = booleenCVUP(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CU * NB_TYPES + TYPE_PLUS, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CU*NB_TYPES + TYPE_PLUS] = lstPoint;
        // m.limitC[VECTEURS_CU*NB_TYPES + TYPE_PLUS] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVUP(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_CU * NB_TYPES + TYPE_PLUS] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CU * NB_TYPES + TYPE_PLUS];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVUP(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        // TODO t.complementerTablo();
        avancement += step;
    }

    private double fitnessCVUP(DataMap c, int posX, int posY) {
        double fit = 1000d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = calculNorme(c.getC(posX - 1, posY).uX + c.getC(posX + 1, posY).uX + c.getC(posX, posY - 1).uX + c.getC(posX, posY + 1).uX + c.getC(posX, posY).uX * FACTEUR_CENTRE[VECTEURS_CU * NB_TYPES + TYPE_PLUS], c.getC(posX - 1, posY).uY + c.getC(posX + 1, posY).uY + c.getC(posX, posY - 1).uY + c.getC(posX, posY + 1).uY + c.getC(posX, posY).uY * FACTEUR_CENTRE[VECTEURS_CU * NB_TYPES + TYPE_PLUS]);
            fit = fit / POIDS_TOTAL[VECTEURS_CU * NB_TYPES + TYPE_PLUS];
        } catch (Exception e) {
            fit = 1000d;
        }
        return fit;
    }

    private boolean booleenCVUP(double fitness, double limite) {
        // limite inferieure
        return (limite > fitness);
    }

    //*******************************************************************************************
    //***************************************  centre : somme des vecteurs, sur case X **********
    //*******************************************************************************************
    public int majCVX(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVX(c, posX, posY);
                ret = booleenCVX(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS * NB_TYPES + TYPE_X, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS*NB_TYPES + TYPE_X] = lstPoint;
        // m.limitC[VECTEURS*NB_TYPES + TYPE_X] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVX(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS * NB_TYPES + TYPE_X] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS * NB_TYPES + TYPE_X];

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVX(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        // TODO t.complementerTablo();
        avancement += step;
    }

    private double fitnessCVX(DataMap c, int posX, int posY) {
        double fit = 1000d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = calculNorme(c.getC(posX - 1, posY - 1).getXBase() + c.getC(posX + 1, posY + 1).getXBase() + c.getC(posX + 1, posY - 1).getXBase() + c.getC(posX - 1, posY + 1).getXBase()
                    + c.getC(posX, posY).getXBase() * FACTEUR_CENTRE[VECTEURS * NB_TYPES + TYPE_X], c.getC(posX - 1, posY - 1).getYBase() + c.getC(posX + 1, posY + 1).getYBase() + c.getC(posX + 1, posY - 1).getYBase() + c.getC(posX - 1, posY + 1).getYBase()
                    + c.getC(posX, posY).getYBase() * FACTEUR_CENTRE[VECTEURS * NB_TYPES + TYPE_X]);
            fit = fit / POIDS_TOTAL[VECTEURS * NB_TYPES + TYPE_X];
        } catch (Exception e) {
            fit = 1000d;
        }
        return fit;
    }

    private boolean booleenCVX(double fitness, double limite) {
        // limite inferieure
        return (limite > fitness);
    }

    // ------------------------------------------------------------------------------------------------
    // --------------------------------centre : somme des vecteurs unitaires, sur case X --------------
    // -----------------------------------------------------------------------------------------------
    public int majCVUX(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVUX(c, posX, posY);
                ret = booleenCVUX(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CU * NB_TYPES + TYPE_X, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CU*NB_TYPES + TYPE_X] = lstPoint;
        // m.limitC[VECTEURS_CU*NB_TYPES + TYPE_X] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVUX(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_CU * NB_TYPES + TYPE_X] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CU * NB_TYPES + TYPE_X];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVUX(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        // TODO t.complementerTablo();
        avancement += step;
    }

    private double fitnessCVUX(DataMap c, int posX, int posY) {
        double fit = 1000d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = calculNorme(c.getC(posX - 1, posY - 1).uX + c.getC(posX + 1, posY + 1).uX + c.getC(posX + 1, posY - 1).uX + c.getC(posX - 1, posY + 1).uX + c.getC(posX, posY).uX * FACTEUR_CENTRE[VECTEURS_CU * NB_TYPES + TYPE_X], c.getC(posX - 1, posY - 1).uY + c.getC(posX + 1, posY + 1).uY + c.getC(posX + 1, posY - 1).uY + c.getC(posX - 1, posY + 1).uY + c.getC(posX, posY).uY * FACTEUR_CENTRE[VECTEURS_CU * NB_TYPES + TYPE_X]);
            fit = fit / POIDS_TOTAL[VECTEURS_CU * NB_TYPES + TYPE_X];
        } catch (Exception e) {
            fit = 1000d;
        }
        return fit;
    }

    private boolean booleenCVUX(double fitness, double limite) {
        // limite  inferieure
        return (limite > fitness);
    }

    //**********************************************************************************************************************************
    //************************  centre : somme des vecteurs, sur case O centre de 8 mesures ********************************************
    //**********************************************************************************************************************************
    public int majCVO(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVO(c, posX, posY);
                ret = booleenCVO(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS * NB_TYPES + TYPE_O, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS*NB_TYPES + TYPE_O] = lstPoint;
        // m.limitC[VECTEURS*NB_TYPES + TYPE_O] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVO(DataMap c) {
        int posX, posY;
        double n;
        tabFitness[VECTEURS * NB_TYPES + TYPE_O] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS * NB_TYPES + TYPE_O];

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVO(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        // TODO t.complementerTablo();
        avancement += step;
    }

    private double fitnessCVO(DataMap c, int posX, int posY) {
        double fit = 1000d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()
                    || c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = calculNorme(c.getC(posX - 1, posY).getXBase() + c.getC(posX + 1, posY).getXBase() + c.getC(posX, posY - 1).getXBase() + c.getC(posX, posY + 1).getXBase()
                    + (c.getC(posX - 1, posY - 1).getXBase() + c.getC(posX + 1, posY + 1).getXBase() + c.getC(posX + 1, posY - 1).getXBase() + c.getC(posX - 1, posY + 1).getXBase()) * FACTEUR_CORRECTIF
                    + c.getC(posX, posY).getXBase() * FACTEUR_CENTRE[VECTEURS * NB_TYPES + TYPE_O], c.getC(posX - 1, posY).getYBase() + c.getC(posX + 1, posY).getYBase() + c.getC(posX, posY - 1).getYBase() + c.getC(posX, posY + 1).getYBase()
                    + c.getC(posX - 1, posY - 1).getYBase() + c.getC(posX + 1, posY + 1).getYBase() + c.getC(posX + 1, posY - 1).getYBase() + c.getC(posX - 1, posY + 1).getYBase()
                    + c.getC(posX, posY).getYBase() * FACTEUR_CENTRE[VECTEURS * NB_TYPES + TYPE_O]);
            fit = fit / POIDS_TOTAL[VECTEURS * NB_TYPES + TYPE_O];
        } catch (Exception e) {
            fit = 1000d;
        }
        return fit;
    }

    private boolean booleenCVO(double fitness, double limite) {
        // limite inferieure
        return (limite > fitness);
    }

    // ----------------------------------------------------------------------------------------------------
    // ------------------------------centre : somme des vecteurs unitaires, sur case O --------------------
    // ----------------------------------------------------------------------------------------------------
    public int majCVUO(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVUO(c, posX, posY);
                ret = booleenCVUO(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CU * NB_TYPES + TYPE_O, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CU*NB_TYPES + TYPE_O] = lstPoint;
        // m.limitC[VECTEURS_CU*NB_TYPES + TYPE_O] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVUO(DataMap c, double limite) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_CU * NB_TYPES + TYPE_O] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CU * NB_TYPES + TYPE_O];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVUO(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        // TODO t.complementerTablo();
        avancement += step;
    }

    private double fitnessCVUO(DataMap c, int posX, int posY) {
        double fit = 1000d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()
                    || c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = calculNorme(c.getC(posX - 1, posY).uX + c.getC(posX + 1, posY).uX + c.getC(posX, posY - 1).uX + c.getC(posX, posY + 1).uX
                    + (c.getC(posX - 1, posY - 1).uX + c.getC(posX + 1, posY + 1).uX + c.getC(posX + 1, posY - 1).uX + c.getC(posX - 1, posY + 1).uX) * FACTEUR_CORRECTIF
                    + c.getC(posX, posY).uX * FACTEUR_CENTRE[VECTEURS_CU * NB_TYPES + TYPE_O], c.getC(posX - 1, posY).uY + c.getC(posX + 1, posY).uY + c.getC(posX, posY - 1).uY + c.getC(posX, posY + 1).uY
                    + c.getC(posX - 1, posY - 1).uY + c.getC(posX + 1, posY + 1).uY + c.getC(posX + 1, posY - 1).uY + c.getC(posX - 1, posY + 1).uY
                    + c.getC(posX, posY).uY * FACTEUR_CENTRE[VECTEURS_CU * NB_TYPES + TYPE_O]);
            fit = fit / POIDS_TOTAL[VECTEURS_CU * NB_TYPES + TYPE_O];
        } catch (Exception e) {
            fit = 1000d;
        }
        return fit;
    }

    private boolean booleenCVUO(double fitness, double limite) {
        // limite  inferieure
        return (limite > fitness);
    }

    //*****************************************  centre : somme des vecteurs de 4 mesures *********
    //*****************************en CARRE, ref = point (xmin,Ymin)**************************************************************************************************************************************
    public int majCVC(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVC(c, posX, posY);
                ret = booleenCVC(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS * NB_TYPES + TYPE_CARRE, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS*NB_TYPES + TYPE_CARRE] = lstPoint;
        // m.limitC[VECTEURS*NB_TYPES + TYPE_CARRE] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVC(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS * NB_TYPES + TYPE_CARRE] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS * NB_TYPES + TYPE_CARRE];

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVC(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }

        // TODO t.complementerTablo();
        avancement += step;
    }

    private double fitnessCVC(DataMap c, int posX, int posY) {
        double fit = 1000d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = calculNorme(c.getC(posX, posY).getXBase() + c.getC(posX + 1, posY).getXBase() + c.getC(posX, posY + 1).getXBase() + c.getC(posX + 1, posY + 1).getXBase(), c.getC(posX, posY).getYBase() + c.getC(posX + 1, posY).getYBase() + c.getC(posX, posY + 1).getYBase() + c.getC(posX + 1, posY + 1).getYBase());
            fit = fit / POIDS_TOTAL[VECTEURS * NB_TYPES + TYPE_CARRE];

        } catch (Exception e) {
            fit = 1000d;
        }
        return fit;
    }

    private boolean booleenCVC(double fitness, double limite) {
        // limite  inferieure
        return (limite > fitness);
    }

    // -------------------------------------------------------------------------------------------------
    // -------------------------centre : somme des vecteurs unitaires sur 4 mesures en CARRE -----------
    // -------------------------------------------------------------------------------------------------
    public int majCVUC(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVUC(c, posX, posY);
                ret = booleenCVUC(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CU * NB_TYPES + TYPE_CARRE, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CU*NB_TYPES + TYPE_CARRE] = lstPoint;
        // m.limitC[VECTEURS_CU*NB_TYPES + TYPE_CARRE] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVUC(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_CU * NB_TYPES + TYPE_CARRE] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CU * NB_TYPES + TYPE_CARRE];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVUC(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }

        // TODO t.complementerTablo();
        avancement += step;

    }

    private double fitnessCVUC(DataMap c, int posX, int posY) {
        double fit = 1000d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = calculNorme(c.getC(posX, posY).uX + c.getC(posX + 1, posY).uX + c.getC(posX, posY + 1).uX + c.getC(posX + 1, posY + 1).uX, c.getC(posX, posY).uY + c.getC(posX + 1, posY).uY + c.getC(posX, posY + 1).uY + c.getC(posX + 1, posY + 1).uY);
            fit = fit / POIDS_TOTAL[VECTEURS_CU * NB_TYPES + TYPE_CARRE];
        } catch (Exception e) {
            fit = 1000d;
        }
        return fit;
    }

    private boolean booleenCVUC(double fitness, double limite) {
        // limite  inferieure
        return (limite > fitness);
    }

	//�������������������������������������������������������������������������������������������������������
    //*******************************************************************************************************
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SOMME DES VECTEURS TOURNANT (ROTATION) @@@@@@@@@@@@@@@@@@@@@@@@@@
    //*******************************************************************************************************
    //****************************** centre : somme des vecteurs tournant, sur case PLUS ********************
    //*******************************************************************************************************
    public int majCVTP(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTP(c, posX, posY);
                ret = booleenCVTP(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_TANGENTS * NB_TYPES + TYPE_PLUS, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_TANGENTS*NB_TYPES + TYPE_PLUS] = lstPoint;
        // m.limitC[VECTEURS_TANGENTS*NB_TYPES + TYPE_PLUS] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVTP(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_TANGENTS * NB_TYPES + TYPE_PLUS] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_TANGENTS * NB_TYPES + TYPE_PLUS];

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTP(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;
    }

    private double fitnessCVTP(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX - 1, posY).getYBase() + c.getC(posX + 1, posY).getYBase()
                    + c.getC(posX, posY - 1).getXBase() - c.getC(posX, posY + 1).getXBase();

            fit = java.lang.Math.abs(fit) - c.getC(posX, posY).norme * FACTEUR_CENTRE[VECTEURS_TANGENTS * NB_TYPES + TYPE_PLUS];

            fit = fit / POIDS_TOTAL[VECTEURS_TANGENTS * NB_TYPES + TYPE_PLUS];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVTP(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    // --------------------------------------------------------------------------------------------------
    // -------------------------- centre : somme des vecteurs TANGENTS UNITAIRES, sur case PLUS ---------
    // --------------------------------------------------------------------------------------------------
    public int majCVTUP(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTUP(c, posX, posY);
                ret = booleenCVTUP(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_PLUS, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_TANGENTS_CU*NB_TYPES + TYPE_PLUS] = lstPoint;
        // m.limitC[VECTEURS_TANGENTS_CU*NB_TYPES + TYPE_PLUS] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVTUP(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_PLUS] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_PLUS];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTUP(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;
    }

    private double fitnessCVTUP(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX - 1, posY).uY + c.getC(posX + 1, posY).uY
                    + c.getC(posX, posY - 1).uX - c.getC(posX, posY + 1).uX;

            // centrifuge force : nulle au centre >> ajouter 1/norme
            // ATTENTION : fort adjuvant >>  soustraire le /10 de la norme
            fit = java.lang.Math.abs(fit) - this.calculNorme(c.getC(posX, posY).uX, c.getC(posX, posY).uY) * FACTEUR_CENTRE[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_PLUS];
            //limite superieure
            fit = fit / POIDS_TOTAL[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_PLUS];
        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVTUP(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    //****************************************************************************************************
    //***************************************  centre : somme des vecteurs TANGENTS , sur case X *********
    //****************************************************************************************************
    public int majCVTX(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();
        // changement de repere orient� � 45�
        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTX(c, posX, posY);
                ret = booleenCVTX(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_TANGENTS * NB_TYPES + TYPE_X, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }

        // mise � jour OK
        // m.lstC[VECTEURS_TANGENTS*NB_TYPES + TYPE_X] = lstPoint;
        // m.limitC[VECTEURS_TANGENTS*NB_TYPES + TYPE_X] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVTX(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_TANGENTS * NB_TYPES + TYPE_X] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_TANGENTS * NB_TYPES + TYPE_X];

        // changement de repere orient� � 45�
        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTX(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;
    }

    private double fitnessCVTX(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX - 1, posY - 1).Y + c.getC(posX + 1, posY + 1).Y
                    + c.getC(posX + 1, posY - 1).X - c.getC(posX - 1, posY + 1).X;

            fit = java.lang.Math.abs(fit) - c.getC(posX, posY).norme * FACTEUR_CENTRE[VECTEURS_TANGENTS * NB_TYPES + TYPE_X];
            //limite superieure
            fit = fit / POIDS_TOTAL[VECTEURS_TANGENTS * NB_TYPES + TYPE_X];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVTX(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    // --------------------------------------------------------------------------------------------------
    // --------------------- centre : somme des vecteurs TANGENTS unitaires, sur case X -----------------
    // --------------------------------------------------------------------------------------------------
    public int majCVTUX(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTUX(c, posX, posY);
                ret = booleenCVTUX(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_X, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_TANGENTS_CU*NB_TYPES + TYPE_X] = lstPoint;
        // m.limitC[VECTEURS_TANGENTS_CU*NB_TYPES + TYPE_X] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVTUX(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_X] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_X];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTUX(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;
    }

    private double fitnessCVTUX(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX - 1, posY - 1).uY + c.getC(posX + 1, posY + 1).uY
                    + c.getC(posX + 1, posY - 1).uX - c.getC(posX - 1, posY + 1).uX;

	        // centrifuge force : nulle au centre >> ajouter 1/norme
            // ATTENTION : fort adjuvant >>  soustraire le /10 de la norme
            fit = java.lang.Math.abs(fit) - this.calculNorme(c.getC(posX, posY).uX, c.getC(posX, posY).uY) * FACTEUR_CENTRE[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_X];
            //limite superieure
            fit = fit / POIDS_TOTAL[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_X];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVTUX(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    //**********************************************************************************************************************
    //**************************************  centre : somme des VECTEURS_TANGENTS, sur case O centre de 8 mesures *********
    //**********************************************************************************************************************
    public int majCVTO(DataMap c, double limite) {
        int posX, posY, decX, decY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        // orientation intelligente :
        for (decX = 0; decX < 2; decX++) {
            for (decY = 0; decY < 2; decY++) {
                // 4 cas d'initialisation qui permettent de traiter 1/4 de l'oc�an
                // 3 types de chgt de reperes
                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 1);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize() - 1; posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize() - 1; posY += 2) {
                        n = fitnessCVTO(c, posX, posY);
                        ret = booleenCVTO(n, limite);
                        if (ret) {
                            cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                        }
                    }
                }
            }
        }
		// mise � jour OK

        // m.lstC[VECTEURS_TANGENTS*NB_TYPES + TYPE_O] = lstPoint;
        // m.limitC[VECTEURS_TANGENTS*NB_TYPES + TYPE_O] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVTO(DataMap c) {
        int posX, posY, decX, decY;
        double n;

        tabFitness[VECTEURS_TANGENTS * NB_TYPES + TYPE_O] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_TANGENTS * NB_TYPES + TYPE_O];

        // orientation intelligente :
        for (decX = 0; decX < 2; decX++) {
            for (decY = 0; decY < 2; decY++) {
                // 4 cas d'initialisation qui permettent de traiter 1/4 de l'oc�an
                // 3 types de chgt de reperes
                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 1);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize() - 1; posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize() - 1; posY += 2) {
                        n = fitnessCVTO(c, posX, posY);
                        t.setTablo(posX, posY, n);
                    }
                }
            }
        }
        avancement += step;
    }

    private double fitnessCVTO(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()
                    || c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re
            /*
             n = - c.getC(posX-1,posY).getYBase() + c.getC(posX+1,posY).getYBase()
             + c.getC(posX,posY-1).getXBase() - c.getC(posX,posY+1).getXBase()
             - c.getC(posX-1,posY-1).getYBase() + c.getC(posX+1,posY+1).getYBase()
             + c.getC(posX+1,posY-1).getXBase() - c.getC(posX-1,posY+1).getXBase() ;
             */

            fit = -c.getC(posX - 1, posY).Y + c.getC(posX + 1, posY).Y
                    + c.getC(posX, posY - 1).X - c.getC(posX, posY + 1).X
                    + (-c.getC(posX - 1, posY - 1).Y + c.getC(posX + 1, posY + 1).Y
                    + c.getC(posX + 1, posY - 1).X - c.getC(posX - 1, posY + 1).X) * FACTEUR_CORRECTIF;

            fit = java.lang.Math.abs(fit)
                    - c.getC(posX, posY).norme * FACTEUR_CENTRE[VECTEURS_TANGENTS * NB_TYPES + TYPE_O];
            //limite superieure
            fit = fit / POIDS_TOTAL[VECTEURS_TANGENTS * NB_TYPES + TYPE_O];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVTO(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    // ------------------------------------------------------------------------------------------------
    // ------------------------------centre : somme des vecteurs TANGENTS unitaires, sur case O -------
    // ------------------------------------------------------------------------------------------------
    public int majCVTUO(DataMap c, double limite) {
        int posX, posY, decX, decY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        // orientation intelligente :
        for (decX = 0; decX < 2; decX++) {
            for (decY = 0; decY < 2; decY++) {
                // 4 cas d'initialisation qui permettent de traiter 1/4 de l'oc�an
                // 3 types de chgt de reperes
                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 1);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize() - 1; posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize() - 1; posY += 2) {
                        n = fitnessCVTUO(c, posX, posY);
                        ret = booleenCVTUO(n, limite);
                        c.getC(posX, posY).setCfgCentre(VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_O, ret);
                        if (ret) {
                            cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                        }
                    }
                }
            }
        }
		// mise � jour OK

        // m.lstC[VECTEURS_TANGENTS_CU*NB_TYPES + TYPE_O] = lstPoint;
        // m.limitC[VECTEURS_TANGENTS_CU*NB_TYPES + TYPE_O] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVTUO(DataMap c) {
        int posX, posY, decX, decY;
        double n;

        tabFitness[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_O] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_O];

        // orientation intelligente :
        for (decX = 0; decX < 2; decX++) {
            for (decY = 0; decY < 2; decY++) {
                // 4 cas d'initialisation qui permettent de traiter 1/4 de l'oc�an
                // 3 types de chgt de reperes
                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 1);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize() - 1; posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize() - 1; posY += 2) {
                        n = fitnessCVTUO(c, posX, posY);
                        t.setTablo(posX, posY, n);
                    }
                }
            }
        }
        avancement += step;
    }

    private double fitnessCVTUO(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()
                    || c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX - 1, posY).uY + c.getC(posX + 1, posY).uY
                    + c.getC(posX, posY - 1).uX - c.getC(posX, posY + 1).uX
                    + (-c.getC(posX - 1, posY - 1).uY + c.getC(posX + 1, posY + 1).uY
                    + c.getC(posX + 1, posY - 1).uX - c.getC(posX - 1, posY + 1).uX) * FACTEUR_CORRECTIF;

	    // centrifuge force : nulle au centre >> ajouter 1/norme
            // ATTENTION : fort adjuvant >>  soustraire le /10 de la norme
            fit = java.lang.Math.abs(fit) - this.calculNorme(c.getC(posX, posY).uX, c.getC(posX, posY).uY) * FACTEUR_CENTRE[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_O];
            //limite superieure
            fit = fit / POIDS_TOTAL[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_O];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVTUO(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    //************************************  centre : somme des vecteurs TANGENTS de 4 mesures **************
    //***************************** en CARRE, ref = point (xmin,Ymin)***************************************
    //******************************************************************************************************
    public int majCVTC(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();
        // orientation
        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTC(c, posX, posY);
                ret = booleenCVTC(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_TANGENTS * NB_TYPES + TYPE_CARRE, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_TANGENTS*NB_TYPES + TYPE_CARRE] = lstPoint;
        // m.limitC[VECTEURS_TANGENTS*NB_TYPES + TYPE_CARRE] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVTC(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_TANGENTS * NB_TYPES + TYPE_CARRE] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_TANGENTS * NB_TYPES + TYPE_CARRE];

        // orientation
        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTC(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;
    }

    private double fitnessCVTC(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX, posY).Y + c.getC(posX + 1, posY).X
                    - c.getC(posX, posY + 1).X + c.getC(posX + 1, posY + 1).Y;

            //limite superieure
            fit = java.lang.Math.abs(fit) / POIDS_TOTAL[VECTEURS_TANGENTS * NB_TYPES + TYPE_CARRE];
        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVTC(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    // ---------------------------------------------------------------------------------------------
    // --------------centre : somme des vecteurs TANGENTS unitaires sur 4 mesures en CARRE ---------
    // ---------------------------------------------------------------------------------------------
    public int majCVTUC(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTUC(c, posX, posY);
                ret = booleenCVTUC(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_CARRE, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_TANGENTS_CU*NB_TYPES + TYPE_CARRE] = lstPoint;
        // m.limitC[VECTEURS_TANGENTS_CU*NB_TYPES + TYPE_CARRE] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVTUC(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_CARRE] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_CARRE];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVTUC(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;
    }

    private double fitnessCVTUC(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX, posY).uY + c.getC(posX + 1, posY).uX
                    - c.getC(posX, posY + 1).uX + c.getC(posX + 1, posY + 1).uY;

            //limite superieure
            fit = java.lang.Math.abs(fit) / POIDS_TOTAL[VECTEURS_TANGENTS_CU * NB_TYPES + TYPE_CARRE];
        } catch (Exception e) {
            fit = 0d;
        }
        return fit;

    }

    private boolean booleenCVTUC(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

	//������������������������������������������������������������������������������������������������������
    //******************************************************************************************************
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ SOMME DES VECTEURS CENTRIFUGES (centrifuges) @@@@@@@@@@@@@@@@@@@
    //******************************************************************************************************
    //****************************** centre : somme des vecteurs CENTRIFUGES, sur case PLUS ****************
    //******************************************************************************************************
    public int majCVFP(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFP(c, posX, posY);
                ret = booleenCVFP(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_PLUS, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CENTRIFUGES*NB_TYPES + TYPE_PLUS] = lstPoint;
        // m.limitC[VECTEURS_CENTRIFUGES*NB_TYPES + TYPE_PLUS] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVFP(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_PLUS] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_PLUS];

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFP(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;
    }

    private double fitnessCVFP(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX - 1, posY).getXBase() + c.getC(posX + 1, posY).getXBase()
                    - c.getC(posX, posY - 1).getYBase() + c.getC(posX, posY + 1).getYBase();

            // centrifuge force : nulle au centre >> ajouter 1/norme
            // ATTENTION : fort adjuvant >>  soustraire le /10 de la norme
            fit = java.lang.Math.abs(fit) - c.getC(posX, posY).norme * FACTEUR_CENTRE[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_PLUS];
            fit = fit / POIDS_TOTAL[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_PLUS];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVFP(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    //--------------------------------------------------------------------------------------------
    //---------------- centre : somme des vecteurs CENTRIFUGES UNITAIRES, sur case PLUS-----------
    //--------------------------------------------------------------------------------------------
    public int majCVFUP(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFUP(c, posX, posY);
                ret = booleenCVFUP(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_PLUS, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CENTRIFUGES_CU*NB_TYPES + TYPE_PLUS] = lstPoint;
        // m.limitC[VECTEURS_CENTRIFUGES_CU*NB_TYPES + TYPE_PLUS] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVFUP(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_PLUS] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_PLUS];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 0);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFUP(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;
    }

    private double fitnessCVFUP(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX - 1, posY).uX + c.getC(posX + 1, posY).uX
                    - c.getC(posX, posY - 1).uY + c.getC(posX, posY + 1).uY;

            // centrifuge force : nulle au centre >> ajouter 1/norme
            // ATTENTION : fort adjuvant >>  soustraire le /10 de la norme
            fit = java.lang.Math.abs(fit) - this.calculNorme(c.getC(posX, posY).uX, c.getC(posX, posY).uY) * FACTEUR_CENTRE[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_PLUS];
            fit = fit / POIDS_TOTAL[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_PLUS];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVFUP(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    //*******************************************************************************************************
    //***************************************  centre : somme des vecteurs CENTRIFUGES , sur case X *********
    //*******************************************************************************************************
    public int majCVFX(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();
        // changement de repere orient� � 45�
        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFX(c, posX, posY);
                ret = booleenCVFX(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_X, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CENTRIFUGES*NB_TYPES + TYPE_X] = lstPoint;
        // m.limitC[VECTEURS_CENTRIFUGES*NB_TYPES + TYPE_X] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVFX(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_X] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_X];

        // changement de repere orient� � 45�
        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFX(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;
    }

    private double fitnessCVFX(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re
            /*
             n = - c.getC(posX-1,posY-1).getXBase() + c.getC(posX+1,posY+1).getXBase()
             - c.getC(posX+1,posY-1).getYBase() + c.getC(posX-1,posY+1).getYBase() ;
             */
            fit = -c.getC(posX - 1, posY - 1).X + c.getC(posX + 1, posY + 1).X
                    - c.getC(posX + 1, posY - 1).Y + c.getC(posX - 1, posY + 1).Y;

            // centrifuge force : nulle au centre >> ajouter 1/norme
            // ATTENTION : fort adjuvant >>  soustraire le /10 de la norme
            fit = java.lang.Math.abs(fit) - c.getC(posX, posY).norme * FACTEUR_CENTRE[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_X];
            fit = fit / POIDS_TOTAL[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_X];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVFX(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    //----------------------------------------------------------------------------------------------
    //----------------------- centre : somme des vecteurs CENTRIFUGES unitaires, sur case X --------
    //----------------------------------------------------------------------------------------------
    public final int majCVFUX(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFUX(c, posX, posY);
                ret = booleenCVFUX(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_X, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CENTRIFUGES_CU*NB_TYPES + TYPE_X] = lstPoint;
        // m.limitC[VECTEURS_CENTRIFUGES_CU*NB_TYPES + TYPE_X] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVFUX(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_X] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_X];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 1; posX < c.getXSize() - 1; posX++) {
            for (posY = 1; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFUX(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }
        avancement += step;

    }

    private double fitnessCVFUX(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX - 1, posY - 1).uX + c.getC(posX + 1, posY + 1).uX
                    - c.getC(posX + 1, posY - 1).uY + c.getC(posX - 1, posY + 1).uY;

            // centrifuge force : nulle au centre >> ajouter 1/norme
            // ATTENTION : fort adjuvant >>  soustraire le /10 de la norme
            fit = java.lang.Math.abs(fit) - this.calculNorme(c.getC(posX, posY).uX, c.getC(posX, posY).uY) * FACTEUR_CENTRE[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_X];
            //limite superieure
            fit = fit / POIDS_TOTAL[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_X];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVFUX(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    //**************************************  centre : somme des VECTEURS_CENTRIFUGES, sur case O centre de 8 mesures *********
    //**********************************************************************************************************************************
    public int majCVFO(DataMap c, double limite) {
        int posX, posY, decX, decY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        // orientation intelligente :
        for (decX = 0; decX < 2; decX++) {
            for (decY = 0; decY < 2; decY++) {
                // 4 cas d'initialisation qui permettent de traiter 1/4 de l'oc�an
                // 3 types de chgt de reperes
                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 1);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize() - 1; posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize() - 1; posY += 2) {
                        n = fitnessCVFO(c, posX, posY);
                        ret = booleenCVFO(n, limite);
                        c.getC(posX, posY).setCfgCentre(VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_O, ret);
                        if (ret) {
                            cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                        }
                    }
                }
            }
        }
// mise � jour OK

        // m.lstC[VECTEURS_CENTRIFUGES*NB_TYPES + TYPE_O] = lstPoint;
        // m.limitC[VECTEURS_CENTRIFUGES*NB_TYPES + TYPE_O] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVFO(DataMap c) {
        int posX, posY, decX, decY;
        double n;

        tabFitness[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_O] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_O];

        // orientation intelligente :
        for (decX = 0; decX < 2; decX++) {
            for (decY = 0; decY < 2; decY++) {
                // 4 cas d'initialisation qui permettent de traiter 1/4 de l'oc�an
                // 3 types de chgt de reperes
                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 1);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize() - 1; posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize() - 1; posY += 2) {
                        n = fitnessCVFO(c, posX, posY);
                        t.setTablo(posX, posY, n);
                    }
                }
            }
        }
        avancement += step;
    }

    private double fitnessCVFO(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()
                    || c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re
            /*
             n = - c.getC(posX-1,posY).getXBase() + c.getC(posX+1,posY).getXBase()
             - c.getC(posX,posY-1).getYBase() + c.getC(posX,posY+1).getYBase()
             - c.getC(posX-1,posY-1).getXBase() + c.getC(posX+1,posY+1).getXBase()
             - c.getC(posX+1,posY-1).getYBase() + c.getC(posX-1,posY+1).getYBase() ;
             */
            fit = -c.getC(posX - 1, posY).X + c.getC(posX + 1, posY).X
                    - c.getC(posX, posY - 1).Y + c.getC(posX, posY + 1).Y
                    - c.getC(posX - 1, posY - 1).X + c.getC(posX + 1, posY + 1).X
                    - c.getC(posX + 1, posY - 1).Y + c.getC(posX - 1, posY + 1).Y;

        // centrifuge force : nulle au centre >> ajouter 1/norme
            // ATTENTION : fort adjuvant >>  soustraire le /10 de la norme
            fit = java.lang.Math.abs(fit) - c.getC(posX, posY).norme * FACTEUR_CENTRE[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_O];
            fit = fit / POIDS_TOTAL[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_O];
        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVFO(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    //*****************************************************************************************************
    //***************************** centre : somme des vecteurs CENTRIFUGES unitaires, sur case O *********
    //*****************************************************************************************************
    public int majCVFUO(DataMap c, double limite) {
        int posX, posY, decX, decY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        // orientation intelligente :
        for (decX = 0; decX < 2; decX++) {
            for (decY = 0; decY < 2; decY++) {
                // 4 cas d'initialisation qui permettent de traiter 1/4 de l'oc�an
                // 3 types de chgt de reperes
                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 1);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize() - 1; posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize() - 1; posY += 2) {
                        n = fitnessCVFUO(c, posX, posY);
                        ret = booleenCVFUO(n, limite);
                        c.getC(posX, posY).setCfgCentre(VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_O, ret);
                        if (ret) {
                            cpt++;
                        }
                    }
                }
            }
        }
		// mise � jour OK

        // m.lstC[VECTEURS_CENTRIFUGES_CU*NB_TYPES + TYPE_O] = lstPoint;
        // m.limitC[VECTEURS_CENTRIFUGES_CU*NB_TYPES + TYPE_O] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVFUO(DataMap c) {
        int posX, posY, decX, decY;
        boolean ret;
        double n;
        int cpt = 0;

        tabFitness[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_O] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_O];

        // orientation intelligente :
        for (decX = 0; decX < 2; decX++) {
            for (decY = 0; decY < 2; decY++) {
                // 4 cas d'initialisation qui permettent de traiter 1/4 de l'oc�an
                // 3 types de chgt de reperes
                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 1);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize(); posX += 2) {
                    for (posY = decY; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = decX; posX < c.getXSize(); posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize(); posY += 2) {
                        c.getC(posX, posY).calculCU(1, 0);
                    }
                }

                for (posX = (decX + 1) % 2; posX < c.getXSize() - 1; posX += 2) {
                    for (posY = (decY + 1) % 2; posY < c.getYSize() - 1; posY += 2) {
                        n = fitnessCVFUO(c, posX, posY);
                        t.setTablo(posX, posY, n);
                    }
                }
            }
        }
        avancement += step;
    }

    private double fitnessCVFUO(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre()) {
                throw new Exception("Terre");
            }
            // on a ecarte la terre;

            if (c.getC(posX - 1, posY - 1).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY - 1).getSurTerre() || c.getC(posX - 1, posY + 1).getSurTerre()
                    || c.getC(posX - 1, posY).getSurTerre() || c.getC(posX + 1, posY).getSurTerre()
                    || c.getC(posX, posY - 1).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX - 1, posY).uX + c.getC(posX + 1, posY).uX
                    - c.getC(posX, posY - 1).uY + c.getC(posX, posY + 1).uY
                    - c.getC(posX - 1, posY - 1).uX + c.getC(posX + 1, posY + 1).uX
                    - c.getC(posX + 1, posY - 1).uY + c.getC(posX - 1, posY + 1).uY;

				// centrifuge force : nulle au centre >> ajouter 1/norme
            // ATTENTION : fort adjuvant >>  soustraire le /10 de la norme
            fit = java.lang.Math.abs(fit) - this.calculNorme(c.getC(posX, posY).uX, c.getC(posX, posY).uY) * FACTEUR_CENTRE[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_O];
            fit = fit / POIDS_TOTAL[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_O];

        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVFUO(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    //*****************************************  centre : somme des vecteurs CENTRIFUGES de 4 mesures *********
    //*****************************en CARRE, ref = point (xmin,Ymin)*******************************************
    //*********************************************************************************************************
    //*********************************************************************************************************
    public final int majCVFC(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();
        // orientation
        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFC(c, posX, posY);
                ret = booleenCVFC(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_CARRE, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CENTRIFUGES*NB_TYPES + TYPE_CARRE] = lstPoint;
        // m.limitC[VECTEURS_CENTRIFUGES*NB_TYPES + TYPE_CARRE] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVFC(DataMap c) {
        int posX, posY;
        boolean ret;
        double n;
        tabFitness[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_CARRE] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_CARRE];
        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFC(c, posX, posY);
                t.setTablo(posX, posY, n);

            }
        }
        avancement += step;
    }

    private double fitnessCVFC(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX, posY).X - c.getC(posX + 1, posY).Y
                    + c.getC(posX, posY + 1).Y + c.getC(posX + 1, posY + 1).X;
            fit = (java.lang.Math.abs(fit) / POIDS_TOTAL[VECTEURS_CENTRIFUGES * NB_TYPES + TYPE_CARRE]);
        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVFC(double fitness, double limite) {
        // limite superieure
        return (limite < fitness);
    }

    // *****************************************************************************************************
    // ******************* centre : somme des vecteurs CENTRIFUGES unitaires sur 4 mesures en CARRE ********
    // *****************************************************************************************************
    public int majCVFUC(DataMap c, double limite) {
        int posX, posY;
        boolean ret;
        double n;
        int cpt = 0;
        System.out.println("NormeMax " + c.getMaxNorm() + " // Limite : " + limite);   // java.util.ArrayList lstPoint = new java.util.ArrayList();

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFUC(c, posX, posY);
                ret = booleenCVFUC(n, limite);
                c.getC(posX, posY).setCfgCentre(VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_CARRE, ret);
                if (ret) {
                    cpt++;  //  lstPoint.add(new java.awt.Point(posX,posY));
                }
            }
        }
        // mise � jour OK

        // m.lstC[VECTEURS_CENTRIFUGES_CU*NB_TYPES + TYPE_CARRE] = lstPoint;
        // m.limitC[VECTEURS_CENTRIFUGES_CU*NB_TYPES + TYPE_CARRE] = limite;
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return cpt;
    }

    public void calculerFitnessCVFUC(DataMap c) {
        int posX, posY;
        double n;

        tabFitness[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_CARRE] = new TabloDouble2D(c.getXSize(), c.getYSize());
        TabloDouble2D t = tabFitness[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_CARRE];

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                c.getC(posX, posY).calculCU(1, 1);
            }
        }

        for (posX = 0; posX < c.getXSize() - 1; posX++) {
            for (posY = 0; posY < c.getYSize() - 1; posY++) {
                n = fitnessCVFUC(c, posX, posY);
                t.setTablo(posX, posY, n);
            }
        }

        avancement += step;
    }

    private double fitnessCVFUC(DataMap c, int posX, int posY) {
        double fit = 0d;
        try {
            // le courant maximal de l'ocean  ou la limite de d'calage de centre //  est d�j� trouv� : limite
            if (c.getC(posX, posY).getSurTerre() || c.getC(posX + 1, posY + 1).getSurTerre()
                    || c.getC(posX + 1, posY).getSurTerre() || c.getC(posX, posY + 1).getSurTerre()) {
                throw new Exception("Bande Cotiere");
            }
            // on a ecarte la bande Coti�re

            fit = -c.getC(posX, posY).uX - c.getC(posX + 1, posY).uY
                    + c.getC(posX, posY + 1).uY + c.getC(posX + 1, posY + 1).uX;
            fit = java.lang.Math.abs(fit) / POIDS_TOTAL[VECTEURS_CENTRIFUGES_CU * NB_TYPES + TYPE_CARRE];
        } catch (Exception e) {
            fit = 0d;
        }
        return fit;
    }

    private boolean booleenCVFUC(double fitness, double limite) {
        //limite superieure
        return (limite < fitness);
    }

	//***********************************************************************************************************
    //****************************** Calcule des points de pond�rations pour combiner les differentes methodes **
    //***********************************************************************************************************
    public int majCombinerGC(DataMap c, int[] coef, int seuil) {
        int posX, posY;
        int ret = 0;
        int i, val, cpt = 0;

        System.out.println("seuil " + seuil);

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                cpt = 0;
                for (i = 0; i < NB_METHODES * NB_TYPES; i++) {
                    val = (c.getC(posX, posY).getCfgCentre(i)) ? 1 : 0;
                    cpt += val * coef[i];
                }
                c.getC(posX, posY).setCfgCentre(COMBINER_GC, (cpt >= seuil));
                if (cpt >= seuil) {
                    ret++;
                }
            }
        }
        avancement += step;
        try {
            Thread.yield();
        } catch (Exception e) {
        }
        return ret;
    }

    public static int NO_INFO = -1;

    /**
     * apres la mise � jour des centres combin�s, on consid�re que seuls les
 centres int�r�ssants sont gard�s ! DiscretiserCentres va ajouter � la
 DataMap des VortexGeom, en ne rep�rant que les groupes de centres
     *
     */
    public int discretiserCentres(DataMap c) {
        int posX, posY;
        int ret = 0;
        int i, cpt = 0, mynum = 0;

        //System.out.println("connex "+connexite);
        int[][] tab = new int[c.getXSize()][c.getYSize()];
        ArrayList coll;
        coll = new ArrayList();
        VortexGeom myV;

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                tab[posX][posY] = NO_INFO;
            }
        }

        for (posX = 0; posX < c.getXSize(); posX++) {
            for (posY = 0; posY < c.getYSize(); posY++) {
                // si on est sur un centre
                if (c.getC(posX, posY).getCfgCentre(COMBINER_GC)) {
                    // si lengthcentre n'est pas tagu�
                    if (tab[posX][posY] == NO_INFO) {
                        // on regarde les 3 points sup�rieurs pour d�terminer le num�ro du centre
                        mynum = NO_INFO;
                        i = -2;
                        while ((i < 1) & (mynum == NO_INFO)) {
                            i = i + 1;
                            try {
                                mynum = tab[posX + i][posY - 1];
                            } catch (Exception e) {
                                mynum = NO_INFO;
                            }
                        }
                        // on regarde le point pr�c�dent
                        if (mynum == NO_INFO) {
                            try {
                                mynum = tab[posX - 1][posY];
                            } catch (Exception e) {
                                mynum = NO_INFO;
                            }
                        }
                        // si aucun tag n'est trouv� : new centre >>
                        if (mynum == NO_INFO) {
                            myV = new VortexGeom();
                            tab[posX][posY] = cpt;
                            cpt++;
                            myV.addPointCentre(posX, posY);
                            coll.add(myV);
                            myV = null;
                        } else {
                            myV = (VortexGeom) coll.get(mynum);
                            tab[posX][posY] = mynum;
                            myV.addPointCentre(posX, posY);
                            coll.add(mynum, myV);
                            myV = null;
                        }
                    }
                }
            }
        }
        // TODO c.setVortexGeom(coll);
        System.out.println(" *** " + cpt + "vortex trouves apres discretisation des centres !");
        return cpt;
    }

}
