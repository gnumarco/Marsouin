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
package tracking;

import data.*;
import data.VortexAnt;
import constants.suivi;
import java.io.File;
import static java.lang.Math.abs;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.System.getProperty;

/**
 *
 * @author segond
 */
public class MoteurSuivi extends java.lang.Thread implements suivi {

    BatchDataMap maCarte;
    int deplacementMax = 6;
    String fichierLog = null;

    /**
     * Creates a new instance of MoteurSuivi
     */
    public MoteurSuivi(BatchDataMap d) {
        maCarte = d;
    }

    public void LancerSuivi() {
        this.start();
    }

    @Override
    public void run() {

        VortexAnt vCourant;
        VortexAnt vCandidat;

        javax.swing.JFileChooser F = new javax.swing.JFileChooser(getProperty("user.home"));

        F.setDialogTitle("Fichier de sauvegarde");
        F.setMultiSelectionEnabled(false);

        int returnVal = F.showOpenDialog(null);
        File fichs;
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
                            vCourant = maCarte.getDataCarte(j, i).getVortexAnt().get(k);
                            for (int l = 0; l < maCarte.getDataCarte(j + 1, i).getVortexAnt().size(); l++) {
                                vCandidat = maCarte.getDataCarte(j + 1, i).getVortexAnt().get(l);
                                double distance = sqrt(pow(abs(vCandidat.getBaricentre()[0] - vCourant.getBaricentre()[0]), 2) + pow(abs(vCandidat.getBaricentre()[1] - vCourant.getBaricentre()[1]), 2));
                                if ((distance <= deplacementMax) && (vCourant.getSens() == vCandidat.getSens())) {
                                    VortexAnt suiv = vCandidat;
                                    suiv.setPrecedent(vCourant);
                                    vCourant.setSuivant(vCandidat);
                                    vCandidat.setNum(vCourant.getNum());
                                }
                            }

                        }

                        for (int k = 0; k < maCarte.getDataCarte(j + 1, i).getVortexAnt().size(); k++) {
                            vCourant = maCarte.getDataCarte(j + 1, i).getVortexAnt().get(k);
                            if (vCourant.getPrecedent().isEmpty()) {
                                numUtilises++;
                                vCourant.setNum(numUtilises);
                            }
                        }

                        //Ecriture fichier
                        for (int k = 0; k < maCarte.getDataCarte(j, i).getVortexAnt().size(); k++) {
                            vCourant = maCarte.getDataCarte(j, i).getVortexAnt().get(k);
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
                        vCourant = maCarte.getDataCarte(maCarte.getNbDataCartesTps() - 1, maCarte.getNbDataCartesProf() - 1).getVortexAnt().get(k);
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
    }

    double[] calculCoordBary(VortexAnt vCourant) {
        return maCarte.getLonLat(vCourant.getBaricentre()[0], vCourant.getBaricentre()[1]);

    }
}
