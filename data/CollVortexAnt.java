/*
 * CollVortexAnt.java
 *
 * Created on 4 avril 2003, 14:09
 */
package data;

/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0
 */
import java.util.ArrayList;
import java.awt.Point;

public class CollVortexAnt {

    private VortexAnt lot[] = null;

    /**
     * Cr�e une nouvelle instance vide de CollVortexAnt
     */
    public CollVortexAnt() {
    }

    /**
     * inutile Creates a new instance of candidates
     *
     * @param n Taille du tableau de MetaVortex pour l'initialisation.
     */
    public CollVortexAnt(int n) {
        if (n > 0) {
            lot = new VortexAnt[n];
            for (int i = 0; i < n; i++) {
                lot[n] = new VortexAnt();
            }
        }
    }

    /**
     * Ajoute un MetaVortex au tableau.
     *
     * @param b Le MetaVortex � ajouter.
     */
    public void ajouter(VortexAnt b) {
        if (lot == null) {
            lot = new VortexAnt[1];
            lot[0] = b;
        } else {
            int s = lot.length;
            VortexAnt[] tmp = lot;
            lot = null;
            lot = new VortexAnt[s + 1];
            for (s = 0; s < tmp.length; s++) {
                lot[s] = tmp[s];
            }
            lot[tmp.length] = b;
            tmp = null;
        }
    }

    public void ajouter(int[] xpts, int[] ypts, int npts, int tag) {
        VortexAnt b = new VortexAnt(xpts, ypts, npts, tag);
        if (lot == null) {
            lot = new VortexAnt[1];
            lot[0] = b;
        } else {
            int s = lot.length;
            VortexAnt[] tmp = lot;
            lot = null;
            lot = new VortexAnt[s + 1];
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
     * Retourne le MetaVortex � l'indice pass� en param�tre.
     *
     * @param num Indice du MetaVortex � retourner.
     * @return Le MetaVortex � l'indice pass� en param�tre dans le tableau.
     */
    public VortexAnt getMetaVortex(int num) {
        VortexAnt ret = null;
        if ((lot != null) && ((num > -1) & (num < lot.length))) {
            ret = lot[num];
        }
        return ret;
    }

    /**
     * Retourne la liste des MetaVotex.
     *
     * @return Le tableau des MetaVortex.
     */
    public VortexAnt[] getMetaVortex() {
        VortexAnt[] ret = lot;
        return ret;
    }

    /**
     * Efface le MetaVortex a l'indice passe en parametre.
     *
     * @param num Indice du MetaVortex a effacer.
     */
    public void effacerMetaVortex(int num) {
        if ((lot != null) && ((num > -1) & (num < lot.length))) {
            int s = lot.length;
            ArrayList tmp = new ArrayList(s);
            for (s = 0; s < lot.length; s++) {
                tmp.add(lot[s]);
            }

            tmp.remove(num);
            lot = null;
            if (tmp.size() > 0) {
                lot = new VortexAnt[tmp.size()];
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

    public int size() {
        int ret = 0;
        if (lot != null) {
            ret = lot.length;
        }
        return ret;
    }

}
