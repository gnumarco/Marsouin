/**
 * OceanFile.java
 *
 * Created on 18 septembre 2002, 12:58
 */
package data;

import ucar.nc2.*;
import ucar.ma2.*;
import java.util.ArrayList;

/**
 * cet objet lit le fichier de carte et le m�morise dans un taleau de courants
 *
 * @author Mahler,Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */
//import ucar.nc2.*;
//import java.util.Iterator;
public class OceanFile implements constants.balise {

    private final boolean dBug = true;
    private boolean isType1 = false, isType2 = false, isNetCDF = false;

    private String nomCompletFichier, unit, MVAttribute;

    private boolean batch = false, noMV;
    private int sel, sel2, indU, indV, indSel2, indSel, varU, varV;
    private ArrayList dims;
    private int nbSurTerre = 0;

    /**
     * cree une nouvelle instance de OceanFile
     *
     * @param nomFichier nom du fichier de carte = chemin et nom !
     *
     */
    public OceanFile(String nomFichier) {
        nomCompletFichier = nomFichier;
        isNetCDF = testNetCDF(nomFichier);
    }

    public OceanFile(String nomFichier, int z, int indZ, int carte, int indCarte, int u, int inU, int v, int inV, String MVAtt, boolean nmv) {
        nomCompletFichier = nomFichier;
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
    }

    private boolean testNetCDF(String f) {
        return (f.endsWith(".nc"));
    }

    /**
     * <
     * PRE>indique  si le fichier est de type 1 :
     *    contient des donn�es en cm/s
     *    possede un entete
     * </PRE>
     *
     * @return vrai si le fichier est de type 1
     */
    public boolean isType1() {
        return isType1;
    }

    /**
     * <
     * PRE>indique  si le fichier est de type 2 :
     *    contient des donn�es en m/s
     *    ensemble de 2 fichiers contenant les projections selon chaque axe nord-sud , est-ouest.
     * </PRE>
     *
     * @return vrai si le fichier est de type 2
     */
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
    public final Courant[][] lire() throws Exception {
        Courant mer[][] = null;
        double max = -999999d;
        double mv = 0d;
        int tailleX = 0, tailleY = 0;
        java.util.ArrayList names = new java.util.ArrayList();
        dims = new java.util.ArrayList();
        if (isNetCDF) {
            if (batch) {
                try {
                    NetcdfFile f = new NetcdfFile(nomCompletFichier);
                    if (!noMV) {
                        mv = ((Variable) (f.getVariables().get(varU))).findAttribute(MVAttribute).getNumericValue().floatValue();
                    } else {
                        mv = Double.parseDouble(MVAttribute);
                    }
                    tailleX = ((Variable) (f.getVariables().get(varU))).getDimension(indU).getLength();
                    tailleY = ((Variable) (f.getVariables().get(varV))).getDimension(indV).getLength();
                    mer = new Courant[tailleX][tailleY];
                    for (int r = 0; r < tailleX; r++) {
                        for (int t = 0; t < tailleY; t++) {
                            mer[r][t] = new Courant();
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
                                if ((Math.abs(mer[i][j].getXBase()) > max) && (Math.abs(mer[i][j].getXBase()) != mv)) {
                                    max = Math.abs(mer[i][j].getXBase());
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
                                if ((Math.abs(mer[i][j].getXBase()) > max) && (Math.abs(mer[i][j].getXBase()) != mv)) {
                                    max = Math.abs(mer[i][j].getXBase());
                                }

                            }
                        }
                    }
                    tab = null;
                    var = null;
                    System.gc();

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
                                if ((Math.abs(mer[i][j].getYBase()) > max) && (Math.abs(mer[i][j].getXBase()) != mv)) {
                                    max = Math.abs(mer[i][j].getYBase());
                                }
                                if ((mer[i][j].getXBase() == mv && mer[i][j].getYBase() == mv) || (Double.isNaN(mer[i][j].getXBase())) || (Double.isNaN(mer[i][j].getYBase()))) {
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
                                if (Math.abs(mer[i][j].getYBase()) > max) {
                                    max = Math.abs(mer[i][j].getYBase());
                                }
                                if ((mer[i][j].getXBase() == mv && mer[i][j].getYBase() == mv) || (Double.isNaN(mer[i][j].getXBase())) || (Double.isNaN(mer[i][j].getYBase()))) {
                                    mer[i][j].setSurTerre(true);
                                    nbSurTerre++;
                                }
                            }
                        }
                    }
                    tab = null;
                    var = null;
                    System.gc();

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
                    tab = null;
                    var = null;
                    System.gc();

                } catch (Exception e) {
                    System.out.println(e.toString());
                }

            } else {
                openNetCdf dia = new openNetCdf(null, true, nomCompletFichier);
                mer = dia.getMer();
            }
        } else {
            long cpt, total;

            java.util.ArrayList line = new java.util.ArrayList();
            try {
                readTextFile file = new readTextFile(nomCompletFichier);
                // le fichier existe.
                // ***************************************DETECTION DU FORMAT DE FICHIER :
                // si le fichier ne comporte QUE DEUX ELEMENTS ENTIERS >0 SUR LA DEUXIEME LIGNE
                //    et la longueur de la ligne suivante est la 1er coord
                //      : FORMAT FORT.67 sur Mars : date CR Width Heigth CR DataX...CR DataX... CR DataY... CR...
                // sinon si la longueur des 10 premieres lignes est la meme
                //            et le fichier comence par "U_"
                //      : FORMAT U_D_M_Y.txt : (3) 2 fichiers U_... et V_... ne contenant que des projections axiales

                // TYPE1
                isType1 = true;
                isType2 = false;

                file.lectureLigne();
                //ligne 2
                line = file.decomposeLigneEnMots();
                isType1 = isType1 && ((line != null) && (line.size() == 2));
                if (isType1) {
                    tailleX = java.lang.Integer.parseInt(line.get(0).toString());
                    tailleY = java.lang.Integer.parseInt(line.get(1).toString());
                    if (dBug) {
                        System.out.println("type1 1");
                    }
                    // comptage.
                    cpt = 0;
                    line = file.decomposeLigneEnMots();
                    while ((line != null) && (!line.isEmpty())) {
                        cpt = cpt + (long) line.size();
                        line = file.decomposeLigneEnMots();
                    }

                    total = (long) 2 * (long) tailleX * (long) tailleY;
                    if (dBug) {
                        System.out.println("type1 1 f cpt" + cpt + "total " + total);
                    }
                    isType1 = isType1 && (total == cpt);
                }
                file.fermer();
                file = null;

                if (isType1) {
                    if (dBug) {
                        System.out.println("type1 2");
                    }
                    mer = this.lireType1();
                    if (dBug) {
                        System.out.println("type1 OK");
                    }
                } else {
                    isType1 = false;
                    isType2 = true;
                    if (dBug) {
                        System.out.println("type2 1");
                    }
                    isType2 = isType2 && (((new java.io.File(nomCompletFichier)).getName().startsWith(PREFIXE_TYPE_2_VX))
                            | ((new java.io.File(nomCompletFichier)).getName().startsWith(PREFIXE_TYPE_2_VY)));

                    file = new readTextFile(nomCompletFichier);
                    line = file.decomposeLigneEnMots();
                    isType2 = isType2 && (line != null);
                    if (dBug) {
                        System.out.println("type2 1 i");
                    }
                    if (isType2) {
                        if (dBug) {
                            System.out.println("type2 1 m");
                        }
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
                        if (dBug) {
                            System.out.println("type1 2 1 f cpt" + cpt + "total " + total);
                        }
                        isType2 = isType2 && (total == cpt);
                    }
                    file.fermer();
                    file = null;

                    if (isType2) {
                        if (dBug) {
                            System.out.println("type2 2");
                        }
                        mer = this.lireType2(tailleX, tailleY);
                        if (dBug) {
                            System.out.println("type2 OK");
                        }
                    } else {
                        throw new Exception(" mauvais type de fichier ");
                    }
                }

            } catch (Exception e) {
                throw e;
            }
        }

        return mer;
    }

    public int getNbSurTerre() {
        return nbSurTerre;
    }

    public final Courant[][] lireType1() {
        /* Lecture du fichier des vitesses pour construire une carte de simu */

        Courant mer[][];
        int r, t, cpt, tailleX, tailleY;
        try {
            readTextFile lect = new readTextFile(nomCompletFichier);
            lect.lectureLigne();
            if (dBug) {
                System.out.println("1st lect ligne");
            }
            java.util.ArrayList val = lect.decomposeLigneEnMots();
            if (dBug) {
                System.out.println(" ok dec en mot");
            }
            tailleX = java.lang.Integer.parseInt(val.get(0).toString());
            tailleY = java.lang.Integer.parseInt(val.get(1).toString());
            //System.out.println(" carte de "+ tailleX + " colonnes et " + tailleY + " lignes");

            if (dBug) {
                System.out.println("taille recup ok");
            }
            mer = new Courant[tailleX][tailleY];
            if (dBug) {
                System.out.println("mer tab ini ok");
            }
            for (r = 0; r < tailleX; r++) {
                for (t = 0; t < tailleY; t++) {
                    mer[r][t] = new Courant();
                }
            }
            if (dBug) {
                System.out.println("mer tab courant ini ok");
            }

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
                                    COEF_TYPE_1 * java.lang.Double.parseDouble(val.get(r).toString()));
                        }
                    } else if (val.get(r).toString().equalsIgnoreCase("nan")) {
                        mer[(cpt - (tailleX * tailleY)) % tailleX][(cpt - (tailleX * tailleY)) / tailleX].setYBase(0.0);
                    } else {
                        mer[(cpt - (tailleX * tailleY)) % tailleX][(cpt - (tailleX * tailleY)) / tailleX].setYBase(
                                COEF_TYPE_1 * java.lang.Double.parseDouble(val.get(r).toString()));
                    }
                    cpt++;
                }
                val = lect.decomposeLigneEnMots();
            }
            if (dBug) {
                System.out.println("fin fichier");
            }
            lect.fermer();
            if (dBug) {
                System.out.println("fichier ferm�");
            }
            return mer;

        } catch (Exception e) {
            System.out.println("OceanFile : erreur de lecture de l'ocean : " + e);
            return null;
        }

    }

    public final Courant[][] lireType2(int tailleX, int tailleY) {
        /* Lecture du fichier des vitesses pour construire une carte de simu */
        readTextFile lect;
        Courant mer[][];
        java.io.File fparent, fnewVY, fnewVX;
        String fa, fVX = "", fVY = "";
        try {
            // cas du fichier de composantes horizontales
            if ((new java.io.File(nomCompletFichier)).getName().startsWith(PREFIXE_TYPE_2_VX)) {
                fVX = nomCompletFichier;
                fparent = (new java.io.File(nomCompletFichier)).getParentFile();
                fa = (new java.io.File(nomCompletFichier)).getName();
                fa = PREFIXE_TYPE_2_VY + fa.substring(PREFIXE_TYPE_2_VX.length());
                fnewVY = new java.io.File(fparent, fa);
                fVY = fnewVY.getAbsolutePath();
            } else {
                // cas du fichier de composantes verticales
                if ((new java.io.File(nomCompletFichier)).getName().startsWith(PREFIXE_TYPE_2_VY)) {
                    fVY = nomCompletFichier;
                    fparent = (new java.io.File(nomCompletFichier)).getParentFile();
                    fa = (new java.io.File(nomCompletFichier)).getName();
                    fa = PREFIXE_TYPE_2_VX + fa.substring(PREFIXE_TYPE_2_VY.length());
                    fnewVX = new java.io.File(fparent, fa);
                    fVX = fnewVX.getAbsolutePath();
                }
            }

            lect = new readTextFile(fVX);
            mer = this.lireType2(tailleX, tailleY, lect);
            lect.fermer();

            lect = new readTextFile(fVY);
            this.lireType2(tailleX, tailleY, lect, mer);
            lect.fermer();
        } catch (Exception e) {
            System.out.println("OceanFile : erreur type 2 " + e);
            mer = null;
        }

        fparent = null;
        fnewVY = null;
        fnewVX = null;
        lect = null;

        return mer;
    }

    public Courant[][] lireType2(int tailleX, int tailleY, readTextFile lect) {
        /* Lecture du fichier des vitesses VX pour construire une carte de simu */

        Courant mer[][];
        int r, t, cpt;
        try {

            System.out.println(" carte de " + tailleX + " colonnes et " + tailleY + " lignes");
            mer = new Courant[tailleX][tailleY];
            for (r = 0; r < tailleX; r++) {
                for (t = 0; t < tailleY; t++) {
                    mer[r][t] = new Courant();
                }
            }
            if (dBug) {
                System.out.println("mer tab courant ini ok");
            }

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
                        mer[cpt][r].setXBase(COEF_TYPE_2 * java.lang.Double.parseDouble(val.get(r).toString()));
                    }
                }
                cpt++;
                val = lect.decomposeLigneEnMots();
            }
            if (dBug) {
                System.out.println("fin fichier");
            }
            return mer;

        } catch (Exception e) {
            System.out.println("OceanFile : erreur de lecture de l'ocean : " + e);
            return null;
        }

    }

    public void lireType2(int tailleX, int tailleY, readTextFile lect, Courant[][] mer) {
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
                        mer[cpt][r].setYBase(COEF_TYPE_2 * java.lang.Double.parseDouble(val.get(r).toString()));
                    }
                }
                cpt++;
                val = lect.decomposeLigneEnMots();
            }
            if (dBug) {
                System.out.println("fin fichier");
            }

        } catch (Exception e) {
            System.out.println("OceanFile : erreur de lecture de l'ocean : " + e);
        }

    }

    public String getUnit() {
        return unit;
    }

}
