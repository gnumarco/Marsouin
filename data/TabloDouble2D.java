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
/**
 * @author Mahler
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */
package data;

import java.util.ArrayList;
import java.awt.Point;

/**
 * ce tableau permet de stocker en 2D des valeurs doubles, puis de produire un
 * !plus grand! tableau de doubles � partir d'une interpolation
 *
 * @author mahler
 */
public class TabloDouble2D implements constants.courant {

    private boolean dBug = true;
    private boolean dBugPlus = false;

    /**
     * le tableau de donn�es initiales
     */
    private double[][] tab = null;

    /**
     * les donn�es finales
     */
    private double[][] tabloRes = null;

    /**
     * Creates a new instance of TabloDouble Attention : la taille du tableau
     * initial est FIXE pour chaque objet !
     */
    public TabloDouble2D(int taille_ini_i, int taille_ini_j) {
        tab = new double[taille_ini_i][taille_ini_j];
        int i, j;

        for (i = 0; i < taille_ini_i; i++) {
            for (j = 0; j < taille_ini_j; j++) {
                tab[i][j] = -1.2;
            }
        }
        tab[0][0] = 1.0; // pour fond bleu
    }

    /**
     * returns a new instance of TabloDouble Attention : Clonage !
     */
    public TabloDouble2D cloneMe() {
        TabloDouble2D ret = new TabloDouble2D(tab.length, tab[0].length);
        ret.setTablo((double[][]) tab.clone());
        if ((tabloRes != null) && (tabloRes[0].length > 0)) {
            ret.setGrandTablo((double[][]) tabloRes.clone());
        }
        return ret;
    }

    /**
     * remlacer les valeurs du grand tableau
     */
    private void setGrandTablo(double[][] mytab) {
        if (mytab != null) {
            free(tabloRes);
            tabloRes = mytab;
        }
    }

    /**
     * renseigne les valeurs du tableau initial
     */
    public void setTablo(double[][] mytab) {
        if ((mytab.length != tab.length) | (mytab[0].length != tab[0].length)) {
            System.out.println(" ### TabloDouble : ERREUR de dimension dans les tableaux ");
        } else {
            free(tab);
            tab = mytab;
        }
    }

    /**
     * renseigne chaque valeur du tableau initial
     */
    public void setTablo(int i, int j, double val_i_j) {
        tab[i][j] = val_i_j;
    }

    /**
     * rcnvoie la valeur de la table initiale Pour INFO
     */
    public double getTablo(int i, int j) {
        return tab[i][j];
    }

    /**
     * renvoie le tableau initial * pour info, attention danger
     */
    public double[][] getTablo() {
        return tab;
    }

    public int getTailleX() {
        return tab.length;
    }

    public int getTailleY() {
        return tab[0].length;
    }

    /**
     * renseigne sur la taille du grand tableau
     */
    public int getGrandTabTailleX() {
        return tabloRes.length;
    }

    public int getGrandTabTailleY() {
        return tabloRes[0].length;
    }

    public double getMaxAbsolu() {
        double max = 0d;
        int posX, posY;
        for (posX = 0; posX < tab.length; posX++) {
            for (posY = 0; posY < tab[0].length; posY++) {
                max = Math.max(max, Math.abs(tab[posX][posY]));
            }
        }
        return max;
    }

    /**
     * recherche d'extremas en mer ! construit une liste de java.awt.Point qui
     * ne sont pas sur terre
     */
    public ArrayList getListeMaximaEnMer(DataMap mer) {
        ArrayList list = new ArrayList();
        int posX, posY, i, j;
        boolean pointIsMax = false;
        Point pmax;
        double max;

        for (posX = 0; posX < tab.length; posX++) {
            for (posY = 0; posY < tab[0].length; posY++) {
                try {
                //check<sur terre
                    // *********************** eliminons le rivage *******************************
                    if (mer.getC(posX, posY).getSurTerre()) {
                        throw new Exception("ce point est sur terre");
                    }
                    // *********************** eliminons la zone cotiere *******************************
                    if (mer.isNearCoast(posX, posY)) {
                        throw new Exception("ce point est sur la zone cotiere");
                    }
                    if (mer.isBorderMap(posX, posY)) {
                        throw new Exception("ce point est sur le bord de carte");
                    }

                    max = tab[posX][posY];

                    pointIsMax = true;
                    for (i = BORNE_INF; i <= BORNE_SUP; i++) {
                        for (j = BORNE_INF; j <= BORNE_SUP; j++) {
                            try {
                                // *********************** eliminons le rivage *******************************
                                if (mer.getC(posX + i, posY + j).getSurTerre()) {
                                    throw new Exception("ce point est sur terre");
                                }

                                // recherche si CE point posx,posY est un max
                                if (max < tab[posX + i][posY + j]) // ce n'est pas le point �tudi�
                                {
                                    pointIsMax = false;
                                }

                            } catch (Exception e) {
                            }
                        }
                    }

                    if (pointIsMax) {
                        // on sauve le point
                        pmax = new Point(posX, posY);
                        if (!list.contains(pmax)) {
                            list.add(pmax);
                        }

                    }

                } catch (Exception e) {
                }
            }
        }
        if (dBug) {
            System.out.println(" ** " + list.size() + " maxima, trouv�s par Tb2d ");
        }
        return list;
    }

    /**
     * recherche d'extremas en mer ! construit une liste de java.awt.Point qui
     * ne sont pas sur terre
     */
    public ArrayList getListeMinimaEnMer(DataMap mer) {
        ArrayList list = new ArrayList();
        int posX, posY, i, j;
        boolean pointIsMin = false;
        Point pmin;
        double min;

        for (posX = 0; posX < tab.length; posX++) {
            for (posY = 0; posY < tab[0].length; posY++) {
                try {
                //check<sur terre
                    // *********************** eliminons le rivage *******************************
                    if (mer.getC(posX, posY).getSurTerre()) {
                        throw new Exception("ce point est sur terre");
                    }
                    // *********************** eliminons la zone cotiere *******************************
                    if (mer.isNearCoast(posX, posY) | mer.isBorderMap(posX, posY)) {
                        throw new Exception("ce point est sur la zone cotiere ou le bord de carte");
                    }

                    min = tab[posX][posY];

                    pointIsMin = true;
                    for (i = BORNE_INF; i <= BORNE_SUP; i++) {
                        for (j = BORNE_INF; j <= BORNE_SUP; j++) {
                            try {
                                // *********************** eliminons le rivage *******************************
                                if (mer.getC(posX + i, posY + j).getSurTerre()) {
                                    throw new Exception("ce point est sur terre");
                                }

                                // recherche si CE point posx,posY est un min
                                if (min > tab[posX + i][posY + j]) // ce n'est pas le point �tudi�
                                {
                                    pointIsMin = false;
                                }

                            } catch (Exception e) {
                            }
                        }
                    }

                    if (pointIsMin) {
                        // on sauve le point
                        pmin = new Point(posX, posY);
                        if (!list.contains(pmin)) {
                            list.add(pmin);
                        }
                    }

                } catch (Exception e) {
                }
            }
        }
        if (dBug) {
            System.out.println(" ** " + list.size() + " minima, trouv�s par Tb2d ");
        }
        return list;
    }

//    /** recuperer les extrema (min et max) du tab */
/*    public void getListeExtremaDans(ArrayList min, ArrayList max) {
     ArrayList m = this.listeMaxima();
     max = new ArrayList(m);
     m = this.listeMinima();
     min = new ArrayList(m);
     }
     */
    /**
     * passer d'un tableau initial � un plus grand tableau, (changement
     * d'echelle) en interpolant des valeurs
     */
    public void changeTaille(int nouv_i, int nouv_j) {
        if ((nouv_i < tab.length) | (nouv_j < tab[0].length)) {
            System.out.println(" ### TabloDouble : ERREUR de dimension dans les tableaux changeTaille");
        } else {
            // coef d'echelle
            double scaleX = ((double) tab.length) / (double) nouv_i;
            double scaleY = ((double) tab[0].length) / (double) nouv_j;
            if (dBug) {
                System.out.println(" changeTaille : sX = " + scaleX + " sY = " + scaleY);
            }
            free(tabloRes);
            tabloRes = new double[nouv_i][nouv_j];

            int i, j;

            for (i = 0; i < tabloRes.length; i++) {
                for (j = 0; j < tabloRes[i].length; j++) {
                    tabloRes[i][j] = interpolerTab(scaleX * (double) i, scaleY * (double) j);
                }
            }
        }
    }

    /**
     * normaliser le tableau initial : reduire @ l'intervalle 0.0 1.0 une fois
     * pour toutes
     */
    public void normaliserTab() {
        normaliserUnTableau(tab);
    }

    /**
     * normaliser : reduire @ l'intervalle 0.0 1.0 une fois pour toutes
     */
    private void normaliserUnTableau(double[][] monTab) {

        if (dBug) {
            System.out.println("normalisation");
        }
        int i, j;
        double mini;
        double dist;
        double maxi;
        mini = monTab[0][0];
        maxi = monTab[0][0];
        for (i = 0; i < monTab.length; i++) {
            for (j = 0; j < monTab[i].length; j++) {
                mini = Math.min(monTab[i][j], mini);
                maxi = Math.max(monTab[i][j], maxi);
            }
        }

        if (dBug) {
            System.out.println("normalisation : bornes ok");
        }
        if ((Math.abs(mini) > 0.00001) && (mini < 0d)) {
            mini = -mini;
            dist = maxi + mini;
            /**
             * redressement dans les positifs
             */
            for (i = 0; i < monTab.length; i++) {
                for (j = 0; j < monTab[i].length; j++) {
                    monTab[i][j] = monTab[i][j] + mini;
                }
            }
        } else {
            dist = maxi;
        }

        if (dBug) {
            System.out.println("normalisation min ok");
        }
        for (i = 0; i < monTab.length; i++) {
            for (j = 0; j < monTab[i].length; j++) {
                monTab[i][j] = monTab[i][j] / dist;
            }
        }

        if (dBug) {
            System.out.println("normalisation ok");
        }

    }

    /**
     * normaliser : reduire @ l'intervalle 0.0 1.0 une fois pour toutes
     */
    public void normaliserGrandTab() {
        normaliserUnTableau(tabloRes);
    }

    /**
     * normaliser : reduire @ l'intervalle [ -1 +1 ] avec +1 correspondant a la
     * valeur abs(ValeurMax)
     */
    public void normaliserEnConservantLeSigne() {
        normaliserEnConservantLeSigne(tab);
    }

    private void normaliserEnConservantLeSigne(double[][] monTab) {

        if (dBug) {
            System.out.println("normalisation");
        }
        int i, j;
        double maxi;
        maxi = Math.abs(monTab[0][0]);
        for (i = 0; i < monTab.length; i++) {
            for (j = 0; j < monTab[i].length; j++) {
                maxi = Math.max(Math.abs(monTab[i][j]), maxi);
            }
        }
        if (dBug) {
            System.out.println("normalisationECLSigne : max ok");
        }

        if (maxi > 0.00001) {
            for (i = 0; i < monTab.length; i++) {
                for (j = 0; j < monTab[i].length; j++) {
                    monTab[i][j] = monTab[i][j] / maxi;
                }
            }
        }

        if (dBug) {
            System.out.println("normalisationECLSigne ok");
        }

    }

    /**
     * renvoie une donnee du tableau resultat !
     */
    public double getGrandTab(int i, int j) {
        return tabloRes[i][j];
    }

    /**
     * renvoie le maximum du grand tableau
     */
    public double getGrandTabMax() {
        int i, j;
        double maxi;
        maxi = tabloRes[0][0];
        for (i = 0; i < tabloRes.length; i++) {
            for (j = 0; j < tabloRes[i].length; j++) {
                maxi = Math.max(tabloRes[i][j], maxi);
            }
        }
        return maxi;
    }

    /**
     * renvoie le tableau resultat en entier
     */
    public double[][] getGrandTab() {
        double[][] ret = new double[tabloRes.length][tabloRes[0].length];
        int i, j;
        if (dBugPlus) {
            System.out.println("get grand tab");
        }

        for (i = 0; i < tabloRes.length; i++) {
            for (j = 0; j < tabloRes[0].length; j++) {
                ret[i][j] = tabloRes[i][j];
            }
        }
        if (dBugPlus) {
            System.out.println("get grand tab ok");
        }
        return ret;
    }

    /**
     * renvoie une interpolation de la valeur en i,j du GRAND tableau
     */
    public double getGrandTab(double i, double j) {
        double scaleX = (double) tab.length / (double) tabloRes.length;
        double scaleY = (double) tab[0].length / (double) tabloRes[0].length;
        return interpolerTab(scaleX * i, scaleY * j);
    }

    /**
     * libere la memoire
     */
    public void free(double[][] t) {
        if (dBugPlus) {
            System.out.println("+ TabloDouble2D : free tab");
        }
        if ((t != null) && (t.length > 0)) {
            int i;
            for (i = 0; i < t.length; i++) {
                if ((t[i] != null) && (t[i].length > 0)) {
                    t[i] = null;
                }
            }
        }
        t = null;
        //if (dBugPlus) System.out.println("free tab ok");
    }

    /**
     * destructeur
     */
    protected void finalize() {
        free(tabloRes);
        free(tab);
    }

    /**
     * pour terminer l'utilisation de cet objet
     */
    public void dispose() {
        free(tabloRes);
        free(tab);
    }

    /**
     * interpolation de valeur en i,j Indices du TABLEAU INITIAL tab
     */
    public double interpolerTab(double x, double y) {
        double ret = -1d;
        int x0, y0, xx, yy;
        double dist, sigma;
        double dx, dy;
        double[][] delta = new double[2][2];
        double toAdd, lastAdd;
        int i, j;

        boolean ok = false;

        // point depart x0 y0
        x0 = (int) (java.lang.Math.floor(x));
        y0 = (int) (java.lang.Math.floor(y));

        // ini : regarder ou l'on peut interpoler (x,y) : s'il est sur un bord d'indice max
        if (x0 >= tab.length - 2) {
            x0 = tab.length - 2;
        }
        if (y0 >= tab[0].length - 2) {
            y0 = tab[0].length - 2;
        }

    	// 1ere etape : trouver si (x,y) est tout proche d'un point(xx,yy) de tab (memes coordonnees)
        //   1.1 : reperer si (x,y) se troue sur une ligne ou une colonne (x entier XOR y entier)
        // si de telles conditions ont satifaites, on peut interpoler autrement :-> ok = true;
        for (i = 0; i <= 1; i++) {
            for (j = 0; j <= 1; j++) {
                xx = x0 + i;
                yy = y0 + j;
                dx = ((double) xx - x);
                dy = ((double) yy - y);
                delta[i][j] = (dx * dx + dy * dy);
                if ((!ok) && (Math.abs(delta[i][j]) < PROXIMITE_POUR_INTERPOLATION * PROXIMITE_POUR_INTERPOLATION)) {
                    ret = tab[xx][yy];
                    ok = true;
                }
                if ((!ok) && (Math.abs(dx) < PROXIMITE_POUR_INTERPOLATION)) {
                    // on est sur de la 1ere coordonnee xx >> sur une colonne
                    if (y0 == yy) {
                        yy = yy + 1;
                    } // pour trouver le second point
                    ret = interpolerTab2ndCoord(x, y, xx, y0, yy);
                    ok = true;
                }
                if ((!ok) && (Math.abs(dy) < PROXIMITE_POUR_INTERPOLATION)) {
                    // on est sur de la 2ere coordonnee yy >> sur une ligne
                    if (x0 == xx) {
                        xx = xx + 1;
                    } // pour trouver le second point
                    ret = interpolerTab1stCoord(x, y, x0, xx, yy);
                    ok = true;
                }
            }
        }
        // sinon :
        if (!ok) {
            ret = 0d;
            sigma = 0d;
            toAdd = tab[x0][y0];
            // interpolation bilineaire
            for (i = 0; i <= 1; i++) {
                for (j = 0; j <= 1; j++) {
                    xx = x0 + i;
                    yy = y0 + j;
                    dist = Math.sqrt(delta[i][j]);
                    sigma = sigma + dist;
                    lastAdd = toAdd;
                    try {
                        toAdd = tab[xx][yy];
                    } catch (Exception e) {
                        toAdd = lastAdd;
                    }
                    ret = ret + toAdd * dist;
                }
            }
            ret = ret / sigma;
        }
        //free(d)
        delta[0] = null;
        delta[1] = null;
        delta = null;

        //if ((dBugPlus)&((int)x%20==0)) System.out.println(" @ "+ret+" = interpolation en "+x+" "+y);
        return ret;
    }

    /**
     * interpole seulement autour de x0, x1, avec y~yCommun
     */
    public double interpolerTab1stCoord(double x, double y, int x0, int x1, int yCommun) {
        double toAdd = 0d;
        double ret;
        double dist, sigma;
        sigma = 0d;
        ret = 0d;
        // interpolation lineaire

        dist = Math.abs((double) x0 - x);
        sigma = sigma + dist;
        try {
            toAdd = tab[x0][yCommun];
        } catch (Exception e) {
            if ((dBugPlus)) {
                System.out.println(" EXCEPTION @ 1stCoord x0" + x0 + " " + yCommun);
            }
        }
        ret = ret + toAdd * dist;

        dist = Math.abs((double) x1 - x);
        sigma = sigma + dist;
        try {
            toAdd = tab[x1][yCommun];
        } catch (Exception e) {
            if ((dBugPlus)) {
                System.out.println(" EXCEPTION @ 1stCoord x1" + x1 + " " + yCommun);
            }
        }
        ret = ret + toAdd * dist;

        ret = ret / sigma;
        //if ((dBugPlus)) System.out.println(" @ "+ret+" = interpolation 1st lineaire en "+x+" "+y);
        return ret;
    }

    /**
     * interpole seulement autour de y0, y1, avec x~xCommun
     */
    public double interpolerTab2ndCoord(double x, double y, int xCommun, int y0, int y1) {
        double toAdd = -1d;
        double ret;
        double dist, sigma;
        sigma = 0d;
        ret = 0d;
        // interpolation lineaire

        dist = Math.abs((double) y0 - y);
        sigma = sigma + dist;
        try {
            toAdd = tab[xCommun][y0];
        } catch (Exception e) {
            if ((dBugPlus)) {
                System.out.println(" EXCEPTION @ 2ndCoord y0" + xCommun + " " + y0);
            }
        }
        ret = ret + toAdd * dist;

        dist = Math.abs((double) y1 - y);
        sigma = sigma + dist;
        try {
            toAdd = tab[xCommun][y1];
        } catch (Exception e) {
            if ((dBugPlus)) {
                System.out.println(" EXCEPTION @ 2ndCoord y1" + xCommun + " " + y1);
            }
        }
        ret = ret + toAdd * dist;

        ret = ret / sigma;
        //if ((dBugPlus)) System.out.println(" @ "+ret+" = interpolation 2nd lineaire en "+x+" "+y);
        return ret;
    }

    /**
     * inverser les signes !
     */
    public void doSymetry() {
        int i, j;
        if (tab != null) {
            for (i = 0; i < tab.length; i++) {
                for (j = 0; j < tab[i].length; j++) {
                    tab[i][j] = -tab[i][j];
                }
            }
        }
    }

    /**
     * ne garder que les valeurs negatives
     */
    public void selectNegatif() {
        int i, j;
        if (tab != null) {
            for (i = 0; i < tab.length; i++) {
                for (j = 0; j < tab[i].length; j++) {
                    if (tab[i][j] > 0d) {
                        tab[i][j] = 0d;
                    }
                }
            }
        }
    }

    /**
     * ne garder que les valeurs positives
     */
    public void selectPositif() {
        int i, j;
        if (tab != null) {
            for (i = 0; i < tab.length; i++) {
                for (j = 0; j < tab[i].length; j++) {
                    if (tab[i][j] < 0d) {
                        tab[i][j] = 0d;
                    }
                }
            }
        }
    }

    // tests :
    public void savetab(String s) {
        TabloDouble2DResultatFile r = new TabloDouble2DResultatFile(s);
        r.sauver(tab);
        r = null;
    }

    public void saveGrandTab(String s) {
        TabloDouble2DResultatFile r = new TabloDouble2DResultatFile(s);
        r.sauver(tabloRes);
        r = null;
    }
}
