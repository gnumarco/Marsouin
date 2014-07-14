/*
 * moteurStreamlines.java
 *
 * Created on 16 juillet 2004, 10:09
 */
/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */
package streamlines;

import data.PointFloat;
import data.VortexStreamlines;
import data.CollVortexStreamlines;

/**
 *
 * @author marco
 */
public class moteurStreamlines extends Thread implements constants.streamlines {

    private data.BatchDataMap MaBatchCarte;
    private data.DataMap MaCarte = null;
    //un tableau de vecteurs contenant chacun tous les points d'une streamline
    private CollVortexStreamlines resultat;
    //Distance minimale entre deux points pour que l'int�gration de la streamline s'arrete
    double seuil = 0.4;
    //Pas d'int�gration de la streamline
    double pas = 20d;

    double precision = 1d;
    //Longueur maximale d'une streamline
    int maxL = 500;

    double detectionThreshold = 0.4;

    int id;

    private javax.swing.ProgressMonitor prog;

    visu.Memoire mem;

    /**
     * Creates a new instance of moteurStreamlines
     */
    public moteurStreamlines(data.BatchDataMap d, visu.Memoire m, int i, javax.swing.ProgressMonitor p) {
        mem = m;
        MaBatchCarte = d;
        id = i;
        resultat = new CollVortexStreamlines();
        double[] params = mem.getStreamParams(id);
        prog = p;
    }

    @Override
    public void run() {
        int cpt = 0;
        for (int ii = 0; ii < MaBatchCarte.getNbDataCartesTps(); ii++) {
            for (int jj = 0; jj < MaBatchCarte.getNbDataCartesProf(); jj++) {
                MaCarte = MaBatchCarte.getDataCarte(ii, jj);
                prog.setNote("Processing streamlines detection on map " + MaCarte.getDate());
                resultat = new CollVortexStreamlines();
                double[] params = mem.getStreamParams(id);
                seuil = params[STREAM_SEUIL_POINT_CRITIQUE];
                precision = params[STREAM_PRECISION];
                detectionThreshold = params[STREAM_SEUIL_BOUCLAGE];
                //System.out.println("demarrage streamlines avec pt crit = "+seuil+"; precision = "+precision+"; seuil boucle = "+detectionThreshold);
                double x, y;
                int i = 0;
                int z;
                PointFloat res;
                java.util.ArrayList tab;
                tab = new java.util.ArrayList();
                z = 0;
                int perimMax = 0;
                double valeurPerimMax = 0d;
                for (double k = 0d; k < MaCarte.getOcean()[0].length; k += precision) {
                    for (double j = 0d; j < MaCarte.getOcean().length; j += precision) {
                        if (dansDomaine(new PointFloat(j, k))) {
                            VortexStreamlines resTmp = null;
                            tab.add(new java.util.ArrayList());
                            x = j;
                            y = k;
                            res = integre(x, y, 0);
                            i = 0;
                            while ((res != null) && (i != maxL) && ((resTmp = backwardTest(((java.util.ArrayList) tab.get(z)), res)) == null)) {
                                ((java.util.ArrayList) tab.get(z)).add(res);
                                x = res.x;
                                y = res.y;
                                res = integre(x, y, 0);
                                i++;
                            }
                            z++;
                            if (resTmp != null) {
                                resultat.ajouter(resTmp);
                                if (resTmp.getPerimetre() > valeurPerimMax) {
                                    valeurPerimMax = resTmp.getPerimetre();
                                    perimMax = resultat.size() - 1;
                                }
                            }
                        }
                    }
                    cpt++;
                    this.yield();
                    prog.setProgress(cpt);
                }
                CollVortexStreamlines resFinal = new CollVortexStreamlines();
                while (resultat.size() != 0) {
                    resFinal.ajouter(resultat.getVortexStreamlines(perimMax));
                    resultat.effacerVortexStreamlines(perimMax);
                    valeurPerimMax = 0d;
                    perimMax = 0;
                    for (int n = 0; n < resultat.size(); n++) {
                        if (resFinal.getVortexStreamlines(resFinal.size() - 1).contains(resultat.getVortexStreamlines(n).getCentre())) {
                            resultat.effacerVortexStreamlines(n);
                            n--;
                        } else {
                            if (resultat.getVortexStreamlines(n).getPerimetre() > valeurPerimMax) {
                                valeurPerimMax = resultat.getVortexStreamlines(n).getPerimetre();
                                perimMax = n;
                            }
                        }
                    }
                }
                MaCarte.setCollVortexStream(resFinal);
            }
        }
        prog.close();
        mem.getBatchDataCarte(id).setCarteActive(0, 0);
        mem.getFrmVisu(id).getMonCanvas().processImage();
        mem.getFrmVisu(id).getMonCanvas().repaint();
    }

    private VortexStreamlines backwardTest(java.util.ArrayList v, PointFloat r) {
        VortexStreamlines res = new VortexStreamlines();
        int i = -1;

        if (v.size() > 4) {
            i = v.size() - 3;
            res.addPoint(((PointFloat) (v.get(v.size() - 1))));
            res.addPoint(((PointFloat) (v.get(v.size() - 2))));
            while ((i >= 0) && (Math.sqrt(Math.pow(Math.abs(((PointFloat) (v.get(i))).x - r.x), 2) + Math.pow(Math.abs(((PointFloat) (v.get(i))).y - r.y), 2)) > detectionThreshold)) {
                res.addPoint(((PointFloat) (v.get(i))));
                i--;
            }
        } else {
            res = null;
        }

        if (i < 0) {
            res = null;
        } else {
            res.addPoint(((PointFloat) (v.get(i))));
        }

        return res;
    }

    private PointFloat integre(double a, double b, int sens) {
        double hh, dx, dy, norme;
        PointFloat pp1, p1, p2;
        PointFloat res = null;

        p1 = new PointFloat(a, b);
        //System.out.println("point � int�grer : "+p1.x+","+p1.y);
        if (sens == 0) {
            hh = pas;
        } else {
            hh = -pas;
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
            norme = Math.sqrt(dx * dx + dy * dy);

            if (norme > Math.sqrt(seuil) && dansDomaine(p2)) {
                res = p2;
            } else {
                //System.out.println("seuil atteint !!");
                res = null;
            }
        }

        return res;
    }

    private double getVX(PointFloat p) {
        Double x1, y1, x2, y2;
        double v1, v2, v3, v4, d1, d2, d3, d4, res;
        x1 = java.lang.Math.floor(p.x);
        x2 = java.lang.Math.ceil(p.x);
        y1 = java.lang.Math.floor(p.y);
        y2 = java.lang.Math.ceil(p.y);
        //System.out.println("p.x, p.y : "+p.x+","+p.y);
        //System.out.println("x1,x2,y1,y2 : "+x1.intValue()+","+x2.intValue()+","+y1.intValue()+","+y2.intValue());

        v1 = MaCarte.getOcean()[x1.intValue()][y1.intValue()].getXNorm();
        v2 = MaCarte.getOcean()[x2.intValue()][y1.intValue()].getXNorm();
        v3 = MaCarte.getOcean()[x2.intValue()][y2.intValue()].getXNorm();
        v4 = MaCarte.getOcean()[x1.intValue()][y2.intValue()].getXNorm();

        //System.out.println("v1,v2,v3,v4 : "+v1+","+v2+","+v3+","+v4);
        d1 = Math.sqrt(Math.pow(Math.abs(p.x - x1), 2) + Math.pow(Math.abs(p.y - y1), 2));
        d2 = Math.sqrt(Math.pow(Math.abs(p.x - x2), 2) + Math.pow(Math.abs(p.y - y1), 2));
        d3 = Math.sqrt(Math.pow(Math.abs(p.x - x2), 2) + Math.pow(Math.abs(p.y - y2), 2));
        d4 = Math.sqrt(Math.pow(Math.abs(p.x - x1), 2) + Math.pow(Math.abs(p.y - y2), 2));
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
        x1 = java.lang.Math.floor(p.x);
        x2 = java.lang.Math.ceil(p.x);
        y1 = java.lang.Math.floor(p.y);
        y2 = java.lang.Math.ceil(p.y);
        //System.out.println("p.x, p.y : "+p.x+","+p.y);
        //System.out.println("x1,x2,y1,y2 : "+x1.intValue()+","+x2.intValue()+","+y1.intValue()+","+y2.intValue());

        v1 = MaCarte.getOcean()[x1.intValue()][y1.intValue()].getYNorm();
        v2 = MaCarte.getOcean()[x2.intValue()][y1.intValue()].getYNorm();
        v3 = MaCarte.getOcean()[x2.intValue()][y2.intValue()].getYNorm();
        v4 = MaCarte.getOcean()[x1.intValue()][y2.intValue()].getYNorm();

        //System.out.println("v1,v2,v3,v4 : "+v1+","+v2+","+v3+","+v4);
        d1 = Math.sqrt(Math.pow(Math.abs(p.x - x1), 2) + Math.pow(Math.abs(p.y - y1), 2));
        d2 = Math.sqrt(Math.pow(Math.abs(p.x - x2), 2) + Math.pow(Math.abs(p.y - y1), 2));
        d3 = Math.sqrt(Math.pow(Math.abs(p.x - x2), 2) + Math.pow(Math.abs(p.y - y2), 2));
        d4 = Math.sqrt(Math.pow(Math.abs(p.x - x1), 2) + Math.pow(Math.abs(p.y - y2), 2));
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

        x1 = (new Double(java.lang.Math.floor(p.x))).intValue();
        x2 = (new Double(java.lang.Math.ceil(p.x))).intValue();
        y1 = (new Double(java.lang.Math.floor(p.y))).intValue();
        y2 = (new Double(java.lang.Math.ceil(p.y))).intValue();

        if (MaCarte.isCorrect(x1, y1) && MaCarte.isCorrect(x1, y2) && MaCarte.isCorrect(x2, y1) && MaCarte.isCorrect(x2, y2)) {
            res = true;
        } else {
            res = false;
        }
        return res;

    }

    public CollVortexStreamlines getRes() {
        return resultat;
    }
}
