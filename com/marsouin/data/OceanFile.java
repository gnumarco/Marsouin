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
package com.marsouin.data;

import java.io.IOException;
import static java.lang.Double.isNaN;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Math.abs;
import java.util.logging.Level;
import java.util.logging.Logger;
import ucar.nc2.*;
import ucar.ma2.*;
import static ucar.nc2.NetcdfFile.open;

public class OceanFile implements com.marsouin.constants.Balise {

    private static final Logger log = Logger.getLogger(OceanFile.class.getName());
    private boolean isType1 = false, isType2 = false, isNetCDF = false;

    private final String fileFullName;
    private String unit, MVAttribute;

    private boolean batch = false, noMV;
    private int sel, sel2, indU, indV, indSel2, indSel, varU, varV;
    private int nbSurTerre = 0;

    /**
     * cree une nouvelle instance de OceanFile
     *
     * @param nomFichier nom du fichier de carte = chemin et nom !
     *
     */
    public OceanFile(String nomFichier) {
        fileFullName = nomFichier;
        isNetCDF = testNetCDF(nomFichier);
        log.setLevel(log.getParent().getLevel());
    }

    public OceanFile(String nomFichier, int z, int indZ, int carte, int indCarte, int u, int inU, int v, int inV, String MVAtt, boolean nmv) {
        fileFullName = nomFichier;
        isNetCDF = testNetCDF(nomFichier);
        indSel2 = indCarte;
        sel2 = carte;
        indSel = indZ;
        sel = z;
        indU = u;
        varU = inU;
        indV = v;
        varV = inV;
        batch = true;
        MVAttribute = MVAtt;
        noMV = nmv;
        log.setLevel(log.getParent().getLevel());
    }

    private boolean testNetCDF(String f) {
        return (f.endsWith(".nc"));
    }

    public boolean isType1() {
        return isType1;
    }

    public boolean isType2() {
        return isType2;
    }

    public boolean isNetCDF() {
        return isNetCDF;
    }

    /**
     * Lecture du fichier des vitesses pour construire une carte de simu
     *
     * @throws Exception exception si mauvais type de fichier : informations
     * desordonn�es ...
     * @return renvoie un tableau bidimensionnel de Courants.
     */
    public final Stream[][] lire() throws Exception {
        Stream mer[][] = null;
        double max = -999999d;
        double mv;
        int tailleX = 0, tailleY = 0;
        if (isNetCDF) {
            if (batch) {
                try {
                    NetcdfFile f = open(fileFullName);
                    if (!noMV) {
                        mv = ((Variable) (f.getVariables().get(varU))).findAttribute(MVAttribute).getNumericValue().floatValue();
                    } else {
                        mv = parseDouble(MVAttribute);
                    }
                    tailleX = ((Variable) (f.getVariables().get(varU))).getDimension(indU).getLength();
                    tailleY = ((Variable) (f.getVariables().get(varV))).getDimension(indV).getLength();
                    mer = new Stream[tailleX][tailleY];
                    for (int r = 0; r < tailleX; r++) {
                        for (int t = 0; t < tailleY; t++) {
                            mer[r][t] = new Stream();
                        }
                    }
                    Variable var = ((Variable) (f.getVariables().get(varU)));
                    Array tab = var.read();
                    Index ind = tab.getIndex();
                    int[] tbl = new int[var.getDimensions().size()];
                    if (var.getDimensions().size() == 4) {
                        for (int i = 0; i < tailleX; i++) {
                            for (int j = 0; j < tailleY; j++) {
                                tbl[indU] = i;
                                tbl[indV] = j;
                                tbl[indSel2] = sel2;
                                tbl[indSel] = sel;
                                ind.set(tbl);
                                mer[i][j].setXBase(tab.getDouble(ind));
                                if ((abs(mer[i][j].getXBase()) > max) && (abs(mer[i][j].getXBase()) != mv)) {
                                    max = abs(mer[i][j].getXBase());
                                }

                            }
                        }
                    }
                    if (var.getDimensions().size() == 3) {
                        for (int i = 0; i < tailleX; i++) {
                            for (int j = 0; j < tailleY; j++) {
                                tbl[indU] = i;
                                tbl[indV] = j;
                                tbl[indSel2] = sel2;
                                ind.set(tbl);
                                mer[i][j].setXBase(tab.getDouble(ind));
                                if ((abs(mer[i][j].getXBase()) > max) && (abs(mer[i][j].getXBase()) != mv)) {
                                    max = abs(mer[i][j].getXBase());
                                }

                            }
                        }
                    }

                    var = (Variable) (f.getVariables().get(varV));
                    tab = var.read();
                    ind = tab.getIndex();
                    tbl = new int[var.getDimensions().size()];
                    if (var.getDimensions().size() == 4) {
                        for (int i = 0; i < tailleX; i++) {
                            for (int j = 0; j < tailleY; j++) {
                                tbl[indU] = i;
                                tbl[indV] = j;
                                tbl[indSel2] = sel2;
                                tbl[indSel] = sel;
                                ind.set(tbl);
                                mer[i][j].setYBase(tab.getDouble(ind));
                                if ((abs(mer[i][j].getYBase()) > max) && (abs(mer[i][j].getXBase()) != mv)) {
                                    max = abs(mer[i][j].getYBase());
                                }
                                if ((mer[i][j].getXBase() == mv && mer[i][j].getYBase() == mv) || (isNaN(mer[i][j].getXBase())) || (isNaN(mer[i][j].getYBase()))) {
                                    mer[i][j].setSurTerre(true);
                                    nbSurTerre++;
                                }
                            }
                        }
                    }
                    if (var.getDimensions().size() == 3) {
                        for (int i = 0; i < tailleX; i++) {
                            for (int j = 0; j < tailleY; j++) {
                                tbl[indU] = i;
                                tbl[indV] = j;
                                tbl[indSel2] = sel2;
                                ind.set(tbl);
                                mer[i][j].setYBase(tab.getDouble(ind));
                                if (abs(mer[i][j].getYBase()) > max) {
                                    max = abs(mer[i][j].getYBase());
                                }
                                if ((mer[i][j].getXBase() == mv && mer[i][j].getYBase() == mv) || (isNaN(mer[i][j].getXBase())) || (isNaN(mer[i][j].getYBase()))) {
                                    mer[i][j].setSurTerre(true);
                                    nbSurTerre++;
                                }
                            }
                        }
                    }

                    //Normalisation
                    for (int i = 0; i < tailleX; i++) {
                        for (int j = 0; j < tailleY; j++) {
                            if (!mer[i][j].getSurTerre()) {
                                mer[i][j].setXNorm(mer[i][j].getXBase() * (1d / max));
                                mer[i][j].setYNorm(mer[i][j].getYBase() * (1d / max));
                            }
                        }
                    }
                    f.close();

                } catch (IOException | NumberFormatException e) {
                    System.out.println(e.toString());
                }

            } else {
                OpenNetCdf dia = new OpenNetCdf(null, true, fileFullName);
                mer = dia.getMer();
            }
        } else {
            long cpt, total;

            java.util.ArrayList<String> line;
            try {
                ReadTextFile file = new ReadTextFile(fileFullName);

                // TYPE1
                isType1 = true;
                isType2 = false;

                file.lectureLigne();
                //ligne 2
                line = file.decomposeLigneEnMots();
                isType1 = isType1 && ((line != null) && (line.size() == 2));
                if (isType1) {
                    tailleX = parseInt(line.get(0));
                    tailleY = parseInt(line.get(1));

                    log.info("type1 1");

                    // comptage.
                    cpt = 0;
                    line = file.decomposeLigneEnMots();
                    while ((line != null) && (!line.isEmpty())) {
                        cpt = cpt + (long) line.size();
                        line = file.decomposeLigneEnMots();
                    }

                    total = (long) 2 * (long) tailleX * (long) tailleY;

                    log.info("type1 1 f cpt" + cpt + "total " + total);

                    isType1 = isType1 && (total == cpt);
                }
                file.fermer();

                if (isType1) {

                    log.info("type1 2");

                    mer = this.lireType1();

                    log.info("type1 OK");

                } else {
                    isType1 = false;
                    isType2 = true;

                    log.info("type2 1");

                    isType2 = isType2 && (((new java.io.File(fileFullName)).getName().startsWith(PREFIXE_TYPE_2_VX))
                            | ((new java.io.File(fileFullName)).getName().startsWith(PREFIXE_TYPE_2_VY)));

                    file = new ReadTextFile(fileFullName);
                    line = file.decomposeLigneEnMots();
                    isType2 = isType2 && (line != null);

                    log.info("type2 1 i");

                    if (isType2) {

                        log.info("type2 1 m");

                        tailleY = line.size();
                        tailleX = 1;
                        cpt = (long) line.size();

                        line = file.decomposeLigneEnMots();
                        while ((line != null) && (!line.isEmpty())) {
                            tailleX++;
                            cpt = cpt + (long) line.size();
                            line = file.decomposeLigneEnMots();
                        }
                        total = (long) tailleX * (long) tailleY;

                        log.log(Level.INFO, "type1 2 1 f cpt{0}total {1}", new Object[]{cpt, total});

                        isType2 = isType2 && (total == cpt);
                    }
                    file.fermer();

                    if (isType2) {

                        log.info("type2 2");

                        mer = this.lireType2(tailleX, tailleY);

                        log.info("type2 OK");

                    } else {
                        throw new Exception("Wrong file type");
                    }
                }

            } catch (Exception e) {
                log.log(Level.SEVERE, "Error in reading file", e);
                throw e;
            }
        }

        return mer;
    }

    public int getNbSurTerre() {
        return nbSurTerre;
    }

    public final Stream[][] lireType1() {
        /* Lecture du fichier des vitesses pour construire une carte de simu */

        Stream mer[][] = null;
        int r, t, cpt, tailleX, tailleY;
        try {
            ReadTextFile lect = new ReadTextFile(fileFullName);
            lect.lectureLigne();
            log.info("1st lect ligne");

            java.util.ArrayList val = lect.decomposeLigneEnMots();
            log.info(" ok dec en mot");

            tailleX = parseInt(val.get(0).toString());
            tailleY = parseInt(val.get(1).toString());
            //System.out.println(" carte de "+ tailleX + " colonnes et " + tailleY + " lignes");

            log.info("taille recup ok");

            mer = new Stream[tailleX][tailleY];
            log.info("mer tab ini ok");

            for (r = 0; r < tailleX; r++) {
                for (t = 0; t < tailleY; t++) {
                    mer[r][t] = new Stream();
                }
            }

            log.info("mer tab courant ini ok");

            val = lect.decomposeLigneEnMots();
            cpt = 0;
            /* Construction de la matrice des vitesses */
            while ((val != null) && (!val.isEmpty())) {
                for (r = 0; r < val.size(); r++) {
                    if (cpt / (tailleX * tailleY) == 0) {
                        if (val.get(r).toString().equalsIgnoreCase("nan")) {
                            mer[cpt % tailleX][cpt / tailleX].setXBase(0.0);
                        } else {
                            mer[cpt % tailleX][cpt / tailleX].setXBase(
                                    COEF_TYPE_1 * parseDouble(val.get(r).toString()));
                        }
                    } else if (val.get(r).toString().equalsIgnoreCase("nan")) {
                        mer[(cpt - (tailleX * tailleY)) % tailleX][(cpt - (tailleX * tailleY)) / tailleX].setYBase(0.0);
                    } else {
                        mer[(cpt - (tailleX * tailleY)) % tailleX][(cpt - (tailleX * tailleY)) / tailleX].setYBase(
                                COEF_TYPE_1 * parseDouble(val.get(r).toString()));
                    }
                    cpt++;
                }
                val = lect.decomposeLigneEnMots();
            }

            log.info("fin fichier");

            lect.fermer();

            log.info("fichier ferm�");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Error in reading velocity field file");
        }
        return mer;
    }

    public final Stream[][] lireType2(int tailleX, int tailleY) {
        /* Lecture du fichier des vitesses pour construire une carte de simu */
        ReadTextFile lect;
        Stream mer[][];
        java.io.File fparent, fnewVY, fnewVX;
        String fa, fVX = "", fVY = "";
        try {
            // cas du fichier de composantes horizontales
            if ((new java.io.File(fileFullName)).getName().startsWith(PREFIXE_TYPE_2_VX)) {
                fVX = fileFullName;
                fparent = (new java.io.File(fileFullName)).getParentFile();
                fa = (new java.io.File(fileFullName)).getName();
                fa = PREFIXE_TYPE_2_VY + fa.substring(PREFIXE_TYPE_2_VX.length());
                fnewVY = new java.io.File(fparent, fa);
                fVY = fnewVY.getAbsolutePath();
            } else {
                // cas du fichier de composantes verticales
                if ((new java.io.File(fileFullName)).getName().startsWith(PREFIXE_TYPE_2_VY)) {
                    fVY = fileFullName;
                    fparent = (new java.io.File(fileFullName)).getParentFile();
                    fa = (new java.io.File(fileFullName)).getName();
                    fa = PREFIXE_TYPE_2_VX + fa.substring(PREFIXE_TYPE_2_VY.length());
                    fnewVX = new java.io.File(fparent, fa);
                    fVX = fnewVX.getAbsolutePath();
                }
            }

            lect = new ReadTextFile(fVX);
            mer = this.lireType2(tailleX, tailleY, lect);
            lect.fermer();

            lect = new ReadTextFile(fVY);
            this.lireType2(tailleX, tailleY, lect, mer);
            lect.fermer();
        } catch (Exception e) {
            System.out.println("OceanFile : erreur type 2 " + e);
            mer = null;
        }

        return mer;
    }

    public Stream[][] lireType2(int tailleX, int tailleY, ReadTextFile lect) {
        /* Lecture du fichier des vitesses VX pour construire une carte de simu */

        Stream mer[][];
        int r, t, cpt;
        try {

            System.out.println(" carte de " + tailleX + " colonnes et " + tailleY + " lignes");
            mer = new Stream[tailleX][tailleY];
            for (r = 0; r < tailleX; r++) {
                for (t = 0; t < tailleY; t++) {
                    mer[r][t] = new Stream();
                }
            }

            log.info("mer tab courant ini ok");

            java.util.ArrayList val = lect.decomposeLigneEnMots();
            cpt = 0;
            /* Construction de la matrice des vitesses */
            while ((val != null) && (!val.isEmpty())) {
                if (val.size() != tailleY) {
                    throw new Exception("type2 ligne VX incorrecte");
                }
                for (r = 0; r < val.size(); r++) {
                    if (val.get(r).toString().equalsIgnoreCase("nan")) {
                        mer[cpt][r].setXBase(0.0);
                        mer[cpt][r].setSurTerre(true);
                    } else {
                        mer[cpt][r].setXBase(COEF_TYPE_2 * parseDouble(val.get(r).toString()));
                    }
                }
                cpt++;
                val = lect.decomposeLigneEnMots();
            }

            log.info("fin fichier");

            return mer;

        } catch (Exception e) {
            System.out.println("OceanFile : erreur de lecture de l'ocean : " + e);
            return null;
        }

    }

    public void lireType2(int tailleX, int tailleY, ReadTextFile lect, Stream[][] mer) {
        /* Lecture du fichier des vitesses VX pour construire une carte de simu */

        int r, t, cpt;
        try {
            java.util.ArrayList val = lect.decomposeLigneEnMots();
            cpt = 0;
            /* Construction de la matrice des vitesses */
            while ((val != null) && (!val.isEmpty())) {
                if (val.size() != tailleY) {
                    throw new Exception("type2 ligne VY incorrecte");
                }
                for (r = 0; r < val.size(); r++) {
                    if (val.get(r).toString().equalsIgnoreCase("nan")) {
                        mer[cpt][r].setYBase(0.0);
                        mer[cpt][r].setSurTerre(true);
                    } else {
                        mer[cpt][r].setYBase(COEF_TYPE_2 * parseDouble(val.get(r).toString()));
                    }
                }
                cpt++;
                val = lect.decomposeLigneEnMots();
            }

            log.info("fin fichier");

        } catch (Exception e) {
            System.out.println("OceanFile : erreur de lecture de l'ocean : " + e);
        }

    }

    public String getUnit() {
        return unit;
    }

}
