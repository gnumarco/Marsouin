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
package com.marsouin.ants;

import com.marsouin.visu.FrmMap;
import java.util.Random;
import java.util.ArrayList;
import com.marsouin.data.DataMap;
import com.marsouin.data.Loop;
import com.marsouin.data.VortexAnt;
import java.util.logging.Logger;

public class SearchEngine extends Thread implements com.marsouin.constants.Colors {

    private final int nbAnts;
    private final int nbSpecies;
    private final int nbGenerations;
    private final double pheromoneAmount;
    private final double evapCoef;
    private ArrayList<Ant[]> team;
    private ArrayList<Loop> tmp;

    private com.marsouin.data.DataMap myMap;

    public static int status = 0;

    private ArrayList<Loop> vortX;
    private ArrayList<VortexAnt> metaVortX;
    private final com.marsouin.data.BatchDataMap myDataMap;
    private final FrmMap visu;
    private final javax.swing.ProgressMonitor prog;

    public SearchEngine(com.marsouin.data.BatchDataMap bdc, FrmMap v, int f, int esp, int g, double p, double c, int nbRuns, javax.swing.ProgressMonitor pm) {
        myDataMap = bdc;
        visu = v;
        nbAnts = f;
        nbSpecies = esp;
        nbGenerations = g;
        pheromoneAmount = p;
        evapCoef = c;
        prog = pm;
    }

    @Override
    public void run() {
        tmp = new ArrayList<>();
        Random rdGen = new Random();
        int cptAv = 0;
        for (int k = 0; k < myDataMap.getNbDataCartesTps(); k++) {
            for (int l = 0; l < myDataMap.getNbDataCartesProf(); l++) {
                DataMap maData = myDataMap.getDataCarte(k, l);
                prog.setNote("Processing ant based detection on map " + maData.getDate());
                for (int i = 0; i < maData.getXSize(); i++) {
                    for (int j = 0; j < maData.getYSize(); j++) {
                        maData.getC(i, j).calculNorme();
                        maData.getC(i, j).calculCU(1, 0);
                        maData.getC(i, j).resetPheromone(nbAnts, nbSpecies);
                    }
                }

                reInit();
                setMaCarte(maData);

                vortX = maData.getCollLoop();
                metaVortX = maData.getVortexAnt();

                team = new ArrayList<>();
                for (int i = 0; i < nbSpecies; i++) {
                    team.add(new Ant[nbAnts]);
                }

                for (int i = 0; i < nbSpecies; i++) {
                    for (int j = 0; j < nbAnts; j++) {
                        double b = rdGen.nextDouble();

                        double signe = rdGen.nextDouble();
                        if (signe < 0.5) {
                            b = -b;
                        }
                        ((Ant[]) (team.get(i)))[j] = (new Ant(this, b, rdGen.nextInt(getMaCarte().getXSize()), rdGen.nextInt(getMaCarte().getYSize()), i, j, pheromoneAmount, rdGen));
                    }
                }

                SearchEngine.status = 0;

                //System.out.println("Moteur fourmi "+nbGenerations+" generations");
                for (int i = 0; i < nbGenerations; i++) {
                    cptAv++;
                    for (Object team1 : team) {
                        for (Ant item : ((Ant[]) (team1))) {
                            item.move();
                        }
                    }
                    evaporation();
                    prog.setProgress(cptAv);
                }
                this.getResultat();
                for (Object team1 : team) {
                    for (int j = 0; j < ((Ant[]) (team1)).length; j++) {
                        ((Ant[]) (team1))[j] = null;
                    }
                }

                creationMetaVortex();

                team = null;
                status = 100;
                System.gc();
            }
        }
        tmp = null;
        prog.close();
        visu.getMonCanvas().processImage();
        visu.getMonCanvas().repaint();
    }

    public double getDepot() {
        return pheromoneAmount;
    }

    private void getResultat() {
        ArrayList<Loop> tmpRes;

        for (Object team1 : team) {
            for (Ant item : ((Ant[]) (team1))) {
                tmpRes = item.getResult();
                if ((tmpRes != null) && (tmpRes.size() > 0)) {
                    for (Loop tmpRe : tmpRes) {
                        vortX.add(tmpRe);
                    }
                }
            }
        }
    }

    public void evaporation() {
        for (int i = 0; i < getMaCarte().getXSize(); i++) {
            for (int j = 0; j < getMaCarte().getYSize(); j++) {
                getMaCarte().getC(i, j).evaporePheromone(evapCoef);
            }
        }
    }

    public void reInit() {
        SearchEngine.status = 0;
    }

    private void creationMetaVortex() {
        tmp.clear();
        double maxArea = 0;
        int iMax = 0;
        if (!vortX.isEmpty()) {
            VortexAnt meta;
            for (int i = 0; i < vortX.size(); i++) {
                if (vortX.get(i).getAire() > maxArea) {
                    maxArea = vortX.get(i).getAire();
                    iMax = i;
                }
                tmp.add(vortX.get(i));
            }
            int compt = 0;

            while (!tmp.isEmpty()) {
                meta = new VortexAnt((Loop) (tmp.get(iMax)));
                meta.setNum(compt);

                metaVortX.add(meta);
                compt++;
                tmp.remove(iMax);
                maxArea = 0d;
                iMax = 0;
                for (int n = 0; n < tmp.size(); n++) {
                    if ((metaVortX.get(metaVortX.size() - 1)).contains(((Loop) (tmp.get(n))).getCentre()[0], ((Loop) (tmp.get(n))).getCentre()[1])) {
                        tmp.remove(n);
                        n--;
                    } else {
                        if (((Loop) (tmp.get(n))).getAire() > maxArea) {
                            maxArea = ((Loop) (tmp.get(n))).getAire();
                            iMax = n;
                        }
                    }
                }
            }
        }
    }

    public DataMap getMaCarte() {
        return this.myMap;
    }

    public void setMaCarte(final DataMap MaCarte) {
        this.myMap = MaCarte;
    }
}
