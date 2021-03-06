/*
 * MoteurSuivi.java
 *
 * Created on 5 juin 2003, 10:37
 */
package tracking;

import data.*;
import data.VortexAnt;
import constants.suivi;
import java.io.File;

/**
 *
 * @author segond
 */
public class MoteurSuivi extends java.lang.Thread implements suivi {

    BatchDataCarte maCarte;
    int deplacementMax = 6;
    String fichierLog = null;

    /**
     * Creates a new instance of MoteurSuivi
     */
    public MoteurSuivi(BatchDataCarte d) {
        maCarte = d;
    }

    public void LancerSuivi() {
        this.start();
    }

    public void run() {

        VortexAnt vCourant = null;
        VortexAnt vCandidat = null;

        javax.swing.JFileChooser F = new javax.swing.JFileChooser(System.getProperty("user.home"));

        F.setDialogTitle("Fichier de sauvegarde");
        F.setMultiSelectionEnabled(false);

        int returnVal = F.showOpenDialog(null);
        File fichs = null;
        try {
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                fichs = F.getSelectedFile();
                fichierLog = fichs.getAbsolutePath();
                System.out.println("fichierLog: " + fichierLog);
                data.writeTextFile wtf = new writeTextFile(fichierLog);
                wtf.uneLigne("Carte;Prof;Num;precedent1,precedent2,...;suivant1,suivant2,...;long;lat;aire;sens");
                System.out.println("Suivi lanc�");
                for (int i = 0; i < maCarte.getNbDataCartesProf(); i++) {
                    System.out.println("Suivi sur la prof num:" + i);
                    int numUtilises = 0;
                    for (int j = 0; j < maCarte.getNbDataCartesTps() - 1; j++) {
                        if (j == 0) {
                            numUtilises = maCarte.getDataCarte(j, i).getVortexAnt().size() - 1;
                        }
                        System.out.println("Suivi sur la date num:" + j);
                        for (int k = 0; k < maCarte.getDataCarte(j, i).getVortexAnt().size(); k++) {
                            double distanceMin = 99999d;
                            int indDistanceMin = 0;
                            vCourant = maCarte.getDataCarte(j, i).getVortexAnt().getMetaVortex(k);
                            for (int l = 0; l < maCarte.getDataCarte(j + 1, i).getVortexAnt().size(); l++) {
                                vCandidat = maCarte.getDataCarte(j + 1, i).getVortexAnt().getMetaVortex(l);
                                double distance = Math.sqrt(Math.pow(Math.abs(vCandidat.getBaricentre()[0] - vCourant.getBaricentre()[0]), 2) + Math.pow(Math.abs(vCandidat.getBaricentre()[1] - vCourant.getBaricentre()[1]), 2));
                                if ((distance <= deplacementMax) && (vCourant.getSens() == vCandidat.getSens())) {
                                    VortexAnt suiv = vCandidat;
                                    suiv.setPrecedent(vCourant);
                                    vCourant.setSuivant(vCandidat);
                                    vCandidat.setNum(vCourant.getNum());
                                }
                            }

                        }

                        for (int k = 0; k < maCarte.getDataCarte(j + 1, i).getVortexAnt().size(); k++) {
                            vCourant = maCarte.getDataCarte(j + 1, i).getVortexAnt().getMetaVortex(k);
                            if (vCourant.getPrecedent().isEmpty()) {
                                numUtilises++;
                                vCourant.setNum(numUtilises);
                            }
                        }

                        //Ecriture fichier
                        for (int k = 0; k < maCarte.getDataCarte(j, i).getVortexAnt().size(); k++) {
                            vCourant = maCarte.getDataCarte(j, i).getVortexAnt().getMetaVortex(k);
                            String strSuiv = "";
                            String strPrec = "";
                            if (!vCourant.getSuivant().isEmpty()) {
                                for (int y = 0; y < vCourant.getSuivant().size(); y++) {
                                    strSuiv += "" + vCourant.getSuivant(y).getNum() + ",";
                                }
                                strSuiv = strSuiv.substring(0, strSuiv.length() - 1);
                                if (!vCourant.getPrecedent().isEmpty()) {
                                    for (int y = 0; y < vCourant.getPrecedent().size(); y++) {
                                        strPrec += "" + vCourant.getPrecedent(y).getNum() + ",";
                                    }
                                    strPrec = strPrec.substring(0, strPrec.length() - 1);
                                    wtf.uneLigne("" + j + ";" + i + ";" + vCourant.getNum() + ";" + strPrec + ";" + strSuiv + ";" + calculCoordBary(vCourant)[0] + ";" + calculCoordBary(vCourant)[1] + ";" + vCourant.getAire() + ";" + vCourant.getSens());
                                } else {
                                    wtf.uneLigne("" + j + ";" + i + ";" + vCourant.getNum() + ";" + "null" + ";" + strSuiv + ";" + calculCoordBary(vCourant)[0] + ";" + calculCoordBary(vCourant)[1] + ";" + vCourant.getAire() + ";" + vCourant.getSens());
                                }
                            } else {
                                if (!vCourant.getPrecedent().isEmpty()) {
                                    for (int y = 0; y < vCourant.getPrecedent().size(); y++) {
                                        strPrec += "" + vCourant.getPrecedent(y).getNum() + ",";
                                    }
                                    strPrec = strPrec.substring(0, strPrec.length() - 1);
                                    wtf.uneLigne("" + j + ";" + i + ";" + vCourant.getNum() + ";" + strPrec + ";" + "null" + ";" + calculCoordBary(vCourant)[0] + ";" + calculCoordBary(vCourant)[1] + ";" + vCourant.getAire() + ";" + vCourant.getSens());
                                } else {
                                    wtf.uneLigne("" + j + ";" + i + ";" + vCourant.getNum() + ";" + "null" + ";" + "null" + ";" + calculCoordBary(vCourant)[0] + ";" + calculCoordBary(vCourant)[1] + ";" + vCourant.getAire() + ";" + vCourant.getSens());
                                }
                            }
                        }
                    }
                    int indI = maCarte.getNbDataCartesProf() - 1;
                    int indJ = maCarte.getNbDataCartesTps() - 1;
                    for (int k = 0; k < maCarte.getDataCarte(maCarte.getNbDataCartesTps() - 1, maCarte.getNbDataCartesProf() - 1).getVortexAnt().size(); k++) {
                        vCourant = maCarte.getDataCarte(maCarte.getNbDataCartesTps() - 1, maCarte.getNbDataCartesProf() - 1).getVortexAnt().getMetaVortex(k);
                        String strSuiv = "";
                        String strPrec = "";
                        if (!vCourant.getSuivant().isEmpty()) {
                            for (int y = 0; y < vCourant.getSuivant().size(); y++) {
                                strSuiv += "" + vCourant.getSuivant(y).getNum() + ",";
                            }
                            strSuiv = strSuiv.substring(0, strSuiv.length() - 1);
                            if (!vCourant.getPrecedent().isEmpty()) {
                                for (int y = 0; y < vCourant.getPrecedent().size(); y++) {
                                    strPrec += "" + vCourant.getPrecedent(y).getNum() + ",";
                                }
                                strPrec = strPrec.substring(0, strPrec.length() - 1);
                                wtf.uneLigne("" + indJ + ";" + indI + ";" + vCourant.getNum() + ";" + strPrec + ";" + strSuiv + ";" + calculCoordBary(vCourant)[0] + ";" + calculCoordBary(vCourant)[1] + ";" + vCourant.getAire() + ";" + vCourant.getSens());
                            } else {
                                wtf.uneLigne("" + indJ + ";" + indI + ";" + vCourant.getNum() + ";" + "null" + ";" + strSuiv + ";" + calculCoordBary(vCourant)[0] + ";" + calculCoordBary(vCourant)[1] + ";" + vCourant.getAire() + ";" + vCourant.getSens());
                            }
                        } else {
                            if (!vCourant.getPrecedent().isEmpty()) {
                                for (int y = 0; y < vCourant.getPrecedent().size(); y++) {
                                    strPrec += "" + vCourant.getPrecedent(y).getNum() + ",";
                                }
                                strPrec = strPrec.substring(0, strPrec.length() - 1);
                                wtf.uneLigne("" + indJ + ";" + indI + ";" + vCourant.getNum() + ";" + strPrec + ";" + "null" + ";" + calculCoordBary(vCourant)[0] + ";" + calculCoordBary(vCourant)[1] + ";" + vCourant.getAire() + ";" + vCourant.getSens());
                            } else {
                                wtf.uneLigne("" + indJ + ";" + indI + ";" + vCourant.getNum() + ";" + "null" + ";" + "null" + ";" + calculCoordBary(vCourant)[0] + ";" + calculCoordBary(vCourant)[1] + ";" + vCourant.getAire() + ";" + vCourant.getSens());
                            }
                        }
                    }
                }
                wtf.fermer();
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        F = null;
    }

    double[] calculCoordBary(VortexAnt vCourant) {
        return maCarte.getLonLat(vCourant.getBaricentre()[0], vCourant.getBaricentre()[1]);

    }
}
