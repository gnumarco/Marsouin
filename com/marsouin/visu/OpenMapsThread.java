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
package com.marsouin.visu;

import com.marsouin.data.BatchDataMap;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OpenMapsThread extends Thread {

    private static final Logger log = Logger.getLogger(OpenMapsThread.class.getName());
    int[] profondeurs, dates;
    int indexProfondeurs, indexDates, uZ, indexU, vZ, indexV, id;
    String missingValue, fichier;
    boolean noMissingValue, Ok3D;
    javax.swing.ProgressMonitor prog;
    ArrayList<FrmMap> listeFrmVisu;
    ArrayList<BatchDataMap> listeDataCartes;
    Memory mem;

    public OpenMapsThread(String f, int[] prof, int profInd, int[] time, int timeInd, int u, int uInd, int v, int vInd, String MVAtt, boolean noMV, javax.swing.ProgressMonitor p, ArrayList<BatchDataMap> liste, boolean troisD, ArrayList<FrmMap> liste2, int i, Memory m) {
        fichier = f;
        profondeurs = prof;
        dates = time;
        indexProfondeurs = profInd;
        indexDates = timeInd;
        uZ = u;
        vZ = v;
        indexU = uInd;
        indexV = vInd;
        missingValue = MVAtt;
        noMissingValue = noMV;
        prog = p;
        listeDataCartes = liste;
        listeFrmVisu = liste2;
        mem = m;
        log.setLevel(log.getParent().getLevel());
    }

    @Override
    public void run() {
        listeDataCartes.add(new BatchDataMap(fichier, profondeurs, indexProfondeurs, dates, indexDates, uZ, indexU, vZ, indexV, missingValue, noMissingValue, prog));
        try {
            if (Ok3D) {
                listeFrmVisu.add(new FrmCarte3D(mem, id, true));
            } else {
                listeFrmVisu.add(new FrmMap(mem, id, true));
            }
            listeFrmVisu.get(id).setVisible(true);
            listeFrmVisu.get(id).toFront();
            listeFrmVisu.get(id).MajTaille();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Error trying to add a FrmVisu", e);
            mem.retraitCarte(id);
            id = -1;
            mem.modifierParametres(id);
        }

    }

}
