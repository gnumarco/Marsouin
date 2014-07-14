/*
 * CollBoucle.java
 *
 * Created on 20 november 2002, 17:26
 */

/*
 * @author Mahler
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */
package data;

import java.util.ArrayList;
import java.awt.Point;

/**
 * Contient un tableau de boucles.
 */
public class CollBoucle {

    private Boucle lot[] = null;

    /**
     * Cr�e une nouvelle instance de CollBoucle
     *
     * @param n Taille du tableau de Boucles pour l'initialisation
     */
    public CollBoucle(int n) {
        if (n > 0) {
            lot = new Boucle[n];
            for (int i = 0; i < n; i++) {
                lot[n] = new Boucle();
            }
        }
    }

    /**
     * Cr�e une nouvelle instance vide de CollBoucle.
     */
    public CollBoucle() {

    }

    /**
     * Ajoute dans le tableau de boucles la boucle pass�e en param�tre.
     *
     * @param b La boucle � ajouter.
     */
    public void ajouter(Boucle b) {
        if (lot == null) {
            lot = new Boucle[1];
            lot[0] = b;
        } else {
            int s = lot.length;
            Boucle[] tmp = lot;
            lot = null;
            lot = new Boucle[s + 1];
            for (s = 0; s < tmp.length; s++) {
                lot[s] = tmp[s];
            }
            lot[tmp.length] = b;
            tmp = null;
        }
    }

    public void ajouter(int[] xpts, int[] ypts, int npts, int tag) {
        Boucle b = new Boucle(xpts, ypts, npts, tag);
        if (lot == null) {
            lot = new Boucle[1];
            lot[0] = b;
        } else {
            int s = lot.length;
            Boucle[] tmp = lot;
            lot = null;
            lot = new Boucle[s + 1];
            for (s = 0; s < tmp.length; s++) {
                lot[s] = tmp[s];
            }
            lot[tmp.length] = b;
            tmp = null;
        }
    }

    public void ajouter(ArrayList v, int tag) {
        int[] x = null;
        int[] y = null;
        int n = 0;
        if ((v != null) && (!v.isEmpty())) {
            //extraireX(ArrayList v)
            x = new int[v.size()];
            for (int i = 0; i < v.size(); i++) {
                x[i] = ((Point) v.get(i)).x;
            }

            // extraireY(ArrayList v)
            y = new int[v.size()];
            for (int i = 0; i < v.size(); i++) {
                y[i] = ((Point) v.get(i)).y;
            }
            n = v.size();
        }
        this.ajouter(x, y, n, tag);
    }

    /**
     * Retourne la boucle a l'indice passe en parametre.
     *
     * @param num Indice de la boucle a retourner.
     * @return La boucle a l'indice passe en parametre.
     */
    public Boucle getBoucle(int num) {
        Boucle ret = null;
        if ((lot != null) && ((num > -1) & (num < lot.length))) {
            ret = lot[num];
        }
        return ret;
    }

    /**
     * Retourne le tableau de boucles.
     *
     * @return Le tableau de boucles.
     */
    public Boucle[] getBoucle() {
        Boucle[] ret = lot;
        return ret;
    }

    /**
     * Efface la boucle � l'indice i.
     *
     * @param num Indice de la boucle � effacer.
     */
    public void effacerBoucle(int num) {
        if ((lot != null) && ((num > -1) & (num < lot.length))) {
            int s = lot.length;
            ArrayList tmp = new ArrayList(s);//new ArrayList((java.util.Collection)lot);
            for (s = 0; s < lot.length; s++) {
                tmp.add(lot[s]);
            }

            tmp.remove(num);
            lot = null;
            if (tmp.size() > 0) {
                lot = new Boucle[tmp.size()];
                tmp.toArray(lot);
            }
        }
    }

    public void effacerBoucle(int from, int to) {
        if ((lot != null) && ((from > -1) & (to < lot.length))) {
            int s = lot.length;
            ArrayList tmp = new ArrayList(s);//new ArrayList((java.util.Collection)lot);
            for (s = 0; s < lot.length; s++) {
                tmp.add(lot[s]);
            }

            for (s = from; s <= to; s++) {
                tmp.remove(from);
            }
            lot = null;
            if (tmp.size() > 0) {
                lot = new Boucle[tmp.size()];
                tmp.toArray(lot);
            }
        }
    }

    public void effacerTout() {
        if (lot != null) {
            for (int s = 0; s < lot.length; s++) {
                lot[s] = null;
            }
        }
        lot = null;
    }

    /**
     * Rend la m�moire.
     */
    public void dispose() {
        if (lot != null) {
            for (int s = 0; s < lot.length; s++) {
                lot[s].dispose();
                lot[s] = null;
            }
        }
        lot = null;
    }

    /**
     * Retourne la taille du tableau de boucles.
     *
     * @return La taille du tableau de boucles.
     */
    public int size() {
        int ret = 0;
        if (lot != null) {
            ret = lot.length;
        }
        return ret;
    }

}
