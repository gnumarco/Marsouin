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
package com.marsouin.streamlines;

import com.marsouin.data.PointFloat;
import com.marsouin.data.VortexStreamlines;
import static java.lang.Math.abs;
import static java.lang.Math.ceil;
import static java.lang.Math.floor;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;

/**
 *
 * @author marco
 */
public class StreamlinesEngine extends Thread implements com.marsouin.constants.Streamlines {

    private final com.marsouin.data.BatchDataMap myBatchMap;
    private com.marsouin.data.DataMap myMap = null;
    //un tableau de vecteurs contenant chacun tous les points d'une streamline
    private ArrayList<VortexStreamlines> result;
    //Distance minimale entre deux points pour que l'int�gration de la streamline s'arrete
    double threshold = 0.4;
    //Pas d'int�gration de la streamline
    double step = 20d;

    double precision = 1d;
    //Longueur maximale d'une streamline
    int maxL = 500;

    double detectionThreshold = 0.4;

    int id;

    private final javax.swing.ProgressMonitor prog;

    com.marsouin.visu.Memory mem;
    
    public StreamlinesEngine(com.marsouin.data.BatchDataMap d, com.marsouin.visu.Memory m, int i, javax.swing.ProgressMonitor p) {
        mem = m;
        myBatchMap = d;
        id = i;
        result = new ArrayList<>();
        prog = p;
    }

    @Override
    public void run() {
        int cpt = 0;
        for (int ii = 0; ii < myBatchMap.getNbDataCartesTps(); ii++) {
            for (int jj = 0; jj < myBatchMap.getNbDataCartesProf(); jj++) {
                myMap = myBatchMap.getDataCarte(ii, jj);
                prog.setNote("Processing streamlines detection on map " + myMap.getDate());
                result = new ArrayList<>();
                double[] params = mem.getStreamParams(id);
                threshold = params[STREAM_SEUIL_POINT_CRITIQUE];
                precision = params[STREAM_PRECISION];
                detectionThreshold = params[STREAM_SEUIL_BOUCLAGE];
                //System.out.println("demarrage Streamlines avec pt crit = "+threshold+"; precision = "+precision+"; threshold boucle = "+detectionThreshold);
                double x, y;
                int i;
                int z;
                PointFloat res;
                ArrayList<ArrayList<PointFloat>> tab;
                tab = new ArrayList<>();
                z = 0;
                int perimMax = 0;
                double valeurPerimMax = 0d;
                for (double k = 0d; k < myMap.getOcean()[0].length; k += precision) {
                    for (double j = 0d; j < myMap.getOcean().length; j += precision) {
                        if (dansDomaine(new PointFloat(j, k))) {
                            VortexStreamlines resTmp = null;
                            tab.add(new ArrayList<PointFloat>());
                            x = j;
                            y = k;
                            res = integre(x, y, 0);
                            i = 0;
                            while ((res != null) && (i != maxL) && ((resTmp = backwardTest(tab.get(z), res)) == null)) {
                                tab.get(z).add(res);
                                x = res.x;
                                y = res.y;
                                res = integre(x, y, 0);
                                i++;
                            }
                            z++;
                            if (resTmp != null) {
                                result.add(resTmp);
                                if (resTmp.getPerimetre() > valeurPerimMax) {
                                    valeurPerimMax = resTmp.getPerimetre();
                                    perimMax = result.size() - 1;
                                }
                            }
                        }
                    }
                    cpt++;
                    prog.setProgress(cpt);
                }
                ArrayList<VortexStreamlines> resFinal = new ArrayList<>();
                while (!result.isEmpty()) {
                    resFinal.add(result.get(perimMax));
                    result.remove(perimMax);
                    valeurPerimMax = 0d;
                    perimMax = 0;
                    for (int n = 0; n < result.size(); n++) {
                        if (resFinal.get(resFinal.size() - 1).contains(result.get(n).getCentre())) {
                            result.remove(n);
                            n--;
                        } else {
                            if (result.get(n).getPerimetre() > valeurPerimMax) {
                                valeurPerimMax = result.get(n).getPerimetre();
                                perimMax = n;
                            }
                        }
                    }
                }
                myMap.setCollVortexStream(resFinal);
            }
        }
        prog.close();
        mem.getBatchDataCarte(id).setCarteActive(0, 0);
        mem.getFrmVisu(id).getMonCanvas().processImage();
        mem.getFrmVisu(id).getMonCanvas().repaint();
    }

    private VortexStreamlines backwardTest(java.util.ArrayList<PointFloat> v, PointFloat r) {
        VortexStreamlines res = new VortexStreamlines();
        int i = -1;

        if (v.size() > 4) {
            i = v.size() - 3;
            res.addPoint((v.get(v.size() - 1)));
            res.addPoint((v.get(v.size() - 2)));
            while ((i >= 0) && (sqrt(pow(abs((v.get(i)).x - r.x), 2) + pow(abs((v.get(i)).y - r.y), 2)) > detectionThreshold)) {
                res.addPoint((v.get(i)));
                i--;
            }
        } else {
            res = null;
        }

        if (i < 0) {
            res = null;
        } else {
            res.addPoint((v.get(i)));
        }

        return res;
    }

    private PointFloat integre(double a, double b, int sens) {
        double hh, dx, dy, norme;
        PointFloat pp1, p1, p2;
        PointFloat res;

        p1 = new PointFloat(a, b);
        //System.out.println("point � int�grer : "+p1.x+","+p1.y);
        if (sens == 0) {
            hh = step;
        } else {
            hh = -step;
        }
        double gvx = getVX(p1);
        double gvy = getVY(p1);
        //System.out.println("gvx : "+gvx);
        //System.out.println("gvy : "+gvy);
        pp1 = new PointFloat(p1.x + 0.5 * hh * gvx, p1.y + 0.5 * hh * gvy);
        //System.out.println("pp1 : "+pp1.x+","+pp1.y);
        if (!dansDomaine(pp1)) {
            res = null;
        } else {
            //System.out.println("dans domaine !!");
            p2 = new PointFloat(p1.x + (hh * getVX(pp1)), p1.y + (hh * getVY(pp1)));
            dx = (p2.x - p1.x);
            dy = (p2.y - p1.y);
            norme = sqrt(dx * dx + dy * dy);

            if (norme > sqrt(threshold) && dansDomaine(p2)) {
                res = p2;
            } else {
                //System.out.println("threshold atteint !!");
                res = null;
            }
        }

        return res;
    }

    private double getVX(PointFloat p) {
        Double x1, y1, x2, y2;
        double v1, v2, v3, v4, d1, d2, d3, d4, res;
        x1 = floor(p.x);
        x2 = ceil(p.x);
        y1 = floor(p.y);
        y2 = ceil(p.y);
        //System.out.println("p.x, p.y : "+p.x+","+p.y);
        //System.out.println("x1,x2,y1,y2 : "+x1.intValue()+","+x2.intValue()+","+y1.intValue()+","+y2.intValue());

        v1 = myMap.getOcean()[x1.intValue()][y1.intValue()].getXNorm();
        v2 = myMap.getOcean()[x2.intValue()][y1.intValue()].getXNorm();
        v3 = myMap.getOcean()[x2.intValue()][y2.intValue()].getXNorm();
        v4 = myMap.getOcean()[x1.intValue()][y2.intValue()].getXNorm();

        //System.out.println("v1,v2,v3,v4 : "+v1+","+v2+","+v3+","+v4);
        d1 = sqrt(pow(abs(p.x - x1), 2) + pow(abs(p.y - y1), 2));
        d2 = sqrt(pow(abs(p.x - x2), 2) + pow(abs(p.y - y1), 2));
        d3 = sqrt(pow(abs(p.x - x2), 2) + pow(abs(p.y - y2), 2));
        d4 = sqrt(pow(abs(p.x - x1), 2) + pow(abs(p.y - y2), 2));
        double somD = d1 + d2 + d3 + d4;
        if (somD == 0d) {
            res = v1;
        } else {
            res = (v1 * (1d / d1) + v2 * (1d / d2) + v3 * (1d / d3) + v4 * (1d / d4)) / ((1d / d1) + (1d / d2) + (1d / d3) + (1d / d4));
        }

        return res;

    }

    private double getVY(PointFloat p) {
        Double x1, y1, x2, y2;
        double v1, v2, v3, v4, d1, d2, d3, d4, res;
        x1 = floor(p.x);
        x2 = ceil(p.x);
        y1 = floor(p.y);
        y2 = ceil(p.y);
        //System.out.println("p.x, p.y : "+p.x+","+p.y);
        //System.out.println("x1,x2,y1,y2 : "+x1.intValue()+","+x2.intValue()+","+y1.intValue()+","+y2.intValue());

        v1 = myMap.getOcean()[x1.intValue()][y1.intValue()].getYNorm();
        v2 = myMap.getOcean()[x2.intValue()][y1.intValue()].getYNorm();
        v3 = myMap.getOcean()[x2.intValue()][y2.intValue()].getYNorm();
        v4 = myMap.getOcean()[x1.intValue()][y2.intValue()].getYNorm();

        //System.out.println("v1,v2,v3,v4 : "+v1+","+v2+","+v3+","+v4);
        d1 = sqrt(pow(abs(p.x - x1), 2) + pow(abs(p.y - y1), 2));
        d2 = sqrt(pow(abs(p.x - x2), 2) + pow(abs(p.y - y1), 2));
        d3 = sqrt(pow(abs(p.x - x2), 2) + pow(abs(p.y - y2), 2));
        d4 = sqrt(pow(abs(p.x - x1), 2) + pow(abs(p.y - y2), 2));
        double somD = d1 + d2 + d3 + d4;
        if (somD == 0d) {
            res = v1;
        } else {
            res = (v1 * d1 + v2 * d2 + v3 * d3 + v4 * d4) / somD;
        }

        return res;

    }

    private boolean dansDomaine(PointFloat p) {
        boolean res;
        int x1, x2, y1, y2;

        x1 = (new Double(floor(p.x))).intValue();
        x2 = (new Double(ceil(p.x))).intValue();
        y1 = (new Double(floor(p.y))).intValue();
        y2 = (new Double(ceil(p.y))).intValue();

        if (myMap.isCorrect(x1, y1) && myMap.isCorrect(x1, y2) && myMap.isCorrect(x2, y1) && myMap.isCorrect(x2, y2)) {
            res = true;
        } else {
            res = false;
        }
        return res;

    }

    public ArrayList<VortexStreamlines> getRes() {
        return result;
    }
}
