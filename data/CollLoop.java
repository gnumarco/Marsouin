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
import java.awt.Point;

/**
 * Contient un tableau de boucles.
 */
public class CollLoop {

    private Loop lot[] = null;

    /**
     * Cr�e une nouvelle instance de CollBoucle
     *
     * @param n Taille du tableau de Boucles pour l'initialisation
     */
    public CollLoop(int n) {
        if (n > 0) {
            lot = new Loop[n];
            for (int i = 0; i < n; i++) {
                lot[n] = new Loop();
            }
        }
    }

    /**
     * Cr�e une nouvelle instance vide de CollBoucle.
     */
    public CollLoop() {

    }

    /**
     * Ajoute dans le tableau de boucles la boucle pass�e en param�tre.
     *
     * @param b La boucle � ajouter.
     */
    public void ajouter(Loop b) {
        if (lot == null) {
            lot = new Loop[1];
            lot[0] = b;
        } else {
            int s = lot.length;
            Loop[] tmp = lot;
            lot = null;
            lot = new Loop[s + 1];
            for (s = 0; s < tmp.length; s++) {
                lot[s] = tmp[s];
            }
            lot[tmp.length] = b;
            tmp = null;
        }
    }

    public void ajouter(int[] xpts, int[] ypts, int npts, int tag) {
        Loop b = new Loop(xpts, ypts, npts, tag);
        if (lot == null) {
            lot = new Loop[1];
            lot[0] = b;
        } else {
            int s = lot.length;
            Loop[] tmp = lot;
            lot = null;
            lot = new Loop[s + 1];
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
    public Loop getBoucle(int num) {
        Loop ret = null;
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
    public Loop[] getBoucle() {
        Loop[] ret = lot;
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
                lot = new Loop[tmp.size()];
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
                lot = new Loop[tmp.size()];
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
