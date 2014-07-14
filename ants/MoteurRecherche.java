/*
 * MoteurRecherche.java
 *
 * Created on 10 septembre 2002, 15:09
 */
/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */
package ants;

import java.util.Random;
import java.util.ArrayList;
import data.DataCarte;
import data.CollBoucle;
import data.Boucle;
import data.CollVortexAnt;
import data.VortexAnt;
import visu.*;

public class MoteurRecherche extends Thread implements constants.couleur {

    private int NbFourmis;
    private int NbEspeces;
    private int nbGenerations;
    private double qtePheromone;
    private int runs;
    private double coeffEvap;
    private ArrayList team, tmp;
    private Fourmi[] subTeam;

    private data.DataCarte MaCarte;

    //indicateur ( en pourcentage !)
    /**
     * Etat d'avancement de la detection.
     */
    public static int avancement = 0;

    private final boolean dBug = true;

    private CollBoucle vortX;
    private CollVortexAnt MetaVortX;
    private data.BatchDataCarte maDataCarte;
    private FrmCarte visu;
    private javax.swing.ProgressMonitor prog;

    /**
     * Creates a new instance of MoteurRecherche
     */
    public MoteurRecherche(data.BatchDataCarte bdc, FrmCarte v, int f, int esp, int g, double p, double c, int nbRuns, javax.swing.ProgressMonitor pm) {
        maDataCarte = bdc;
        visu = v;
        NbFourmis = f;
        NbEspeces = esp;
        nbGenerations = g;
        qtePheromone = p;
        coeffEvap = c;
        runs = nbRuns;
        prog = pm;
    }

    public void run() {
        tmp = new ArrayList();
        Random rdGen = new Random();
        int cptAv = 0;
        for (int k = 0; k < maDataCarte.getNbDataCartesTps(); k++) {
            for (int l = 0; l < maDataCarte.getNbDataCartesProf(); l++) {
                DataCarte maData = maDataCarte.getDataCarte(k, l);
                prog.setNote("Processing ant based detection on map " + maData.getDate());
                for (int i = 0; i < maData.getTailleX(); i++) {
                    for (int j = 0; j < maData.getTailleY(); j++) {
                        maData.getC(i, j).calculNorme();
                        maData.getC(i, j).calculCU(1, 0);
                        maData.getC(i, j).resetPheromone(NbFourmis, NbEspeces);
                    }
                }

                reInit();
                setMaCarte(maData);

                vortX = maData.getCollBoucle();
                MetaVortX = maData.getVortexAnt();

                team = new ArrayList();
                for (int i = 0; i < NbEspeces; i++) {
                    team.add(new Fourmi[NbFourmis]);
                }

                for (int i = 0; i < NbEspeces; i++) {
                    for (int j = 0; j < NbFourmis; j++) {
                        double b = rdGen.nextDouble();

                        double signe = rdGen.nextDouble();
                        if (signe < 0.5) {
                            b = -b;
                        }
                        ((Fourmi[]) (team.get(i)))[j] = (new Fourmi(this, b, rdGen.nextInt(getMaCarte().getTailleX()), rdGen.nextInt(getMaCarte().getTailleY()), i, j, qtePheromone, rdGen));
                    }
                }

                this.avancement = 0;

                //System.out.println("Moteur fourmi "+nbGenerations+" generations");
                for (int i = 0; i < nbGenerations; i++) {
                    cptAv++;
                    for (int z = 0; z < team.size(); z++) {
                        for (int j = 0; j < ((Fourmi[]) (team.get(z))).length; j++) {
                            ((Fourmi[]) (team.get(z)))[j].Deplacer();
                        }
                    }
                    evaporation();
                    prog.setProgress(cptAv);
                }
                this.getResultat();
                for (int i = 0; i < team.size(); i++) {
                    for (int j = 0; j < ((Fourmi[]) (team.get(i))).length; j++) {
                        ((Fourmi[]) (team.get(i)))[j].dispose();
                        ((Fourmi[]) (team.get(i)))[j] = null;
                    }
                }

                creationMetaVortex();

                team = null;
                avancement = 100;
                System.gc();
            }
        }
        tmp = null;
        prog.close();
        visu.getMonCanvas().processImage();
        visu.getMonCanvas().repaint();
    }

    public double getDepot() {
        return qtePheromone;
    }

    private void getResultat() {
        ArrayList tmpRes;

        for (int id = 0; id < team.size(); id++) {
            for (int j = 0; j < ((Fourmi[]) (team.get(id))).length; j++) {
                tmpRes = ((Fourmi[]) (team.get(id)))[j].getResultat();
                if ((tmpRes != null) && (tmpRes.size() > 0)) {
                    for (int k = 0; k < tmpRes.size(); k++) {
                        vortX.ajouter((Boucle) (tmpRes.get(k)));
                    }
                }
            }
        }
    }

    /**
     * Effectue une evaporation sur toute la carte.
     */
    public void evaporation() {
        for (int i = 0; i < getMaCarte().getTailleX(); i++) {
            for (int j = 0; j < getMaCarte().getTailleY(); j++) {
                getMaCarte().getC(i, j).evaporePheromone(coeffEvap);
            }
        }
    }

    /**
     * Reinitialise le moteur.
     */
    public void reInit() {
        this.avancement = 0;
    }

    private void creationMetaVortex() {
        tmp.clear();
        double aireMax = 0;
        double centreXMax, centreYMax;
        int iMax = 0;
        if (vortX.getBoucle() != null) {
            VortexAnt meta;
            for (int i = 0; i < vortX.getBoucle().length; i++) {
                if (vortX.getBoucle()[i].getAire() > aireMax) {
                    aireMax = vortX.getBoucle()[i].getAire();
                    iMax = i;
                }
                tmp.add(vortX.getBoucle()[i]);
            }
            int compt = 0;

            while (tmp.size() != 0) {
                meta = new VortexAnt((Boucle) (tmp.get(iMax)));
                meta.setNum(compt);

                MetaVortX.ajouter(meta);
                compt++;
                tmp.remove(iMax);
                aireMax = 0d;
                iMax = 0;
                for (int n = 0; n < tmp.size(); n++) {
                    if ((MetaVortX.getMetaVortex(MetaVortX.size() - 1)).contains(((Boucle) (tmp.get(n))).getCentre()[0], ((Boucle) (tmp.get(n))).getCentre()[1])) {
                        tmp.remove(n);
                        n--;
                    } else {
                        if (((Boucle) (tmp.get(n))).getAire() > aireMax) {
                            aireMax = ((Boucle) (tmp.get(n))).getAire();
                            iMax = n;
                        }
                    }
                }
            }
        }
    }

    private boolean appartient(double[] c, Boucle b) {
        boolean ok = true;

        if (!(b.contains(c[0], c[1]))) {
            ok = false;
        }

        return ok;
    }

    public DataCarte getMaCarte() {
        return this.MaCarte;
    }

    public void setMaCarte(final DataCarte MaCarte) {
        this.MaCarte = MaCarte;
    }
}
