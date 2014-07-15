/*
 * Memory.java
 *
 * Created on 24 septembre 2002, 19:00
 */

/*
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */
package visu;

import ants.SearchEngine;
import data.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import ucar.nc2.*;
import ucar.ma2.*;

public class Memory implements constants.physique, constants.centre, constants.couleur, constants.fourmi, constants.default_values, constants.courant, constants.streamlines {

    private boolean Ok3D = false;

    private static final boolean dBug = false;
    private static final boolean dBugFileInterpoler = false;

    private static int nbTrouverCentre = 0;

    private String memCheminExplo = null;

    private ArrayList listeDataCarte = null;

    private ArrayList listeFrmVisu = null;

    // liste de int[]
    private ArrayList listeFlagAffich = null;
    private ArrayList<int[]> listeFourmiNB = null;
    private ArrayList listeTypeInit = null;

    // liste les coeff (double) pour la frmConfig de chaque carte
    private ArrayList listeFourmiCoeff = null;
    private ArrayList<double[]> listeStreamlinesParam = null;

    // liste de booleens
    private ArrayList<boolean[]> listeUseMethod = null;
    private ArrayList listePhysiqueBAff = null;
    private ArrayList<Boolean> listeModeBatch = null; //true = batch, false = pas batch
    private ArrayList listeAntialias = null;

    ArrayList listeAvancement = null;

    // liste de String
    private ConfigHistoFile configHistoFile = null;
    //private ArrayList listeFConfigHisto=null;
    //private ArrayList listeFResultat=null;

    private FrmConf frmConfig = null;

    /**
     * Creates a new instance of Memoire
     */
    public Memory() {
        //configHistoFile = new ConfigHistoFile();
        //moteurFourmi = new fourmi.SearchEngine();
    }

    public FrmMap getFrmVisu(int id) {
        return (FrmMap) listeFrmVisu.get(id);
    }

    public FrmConf getFrmConfig() {
        return frmConfig;
    }

    public String getMemCheminExplo() {
        return memCheminExplo;
    }

    public void setMemCheminExplo(String s) {
        memCheminExplo = s;
    }

    public void setFrmConfig(FrmConf fcfg, boolean b) {
        frmConfig = fcfg;
        Ok3D = b;
    }

    public ConfigHistoFile getConfigHistoFile() {
        return configHistoFile;
    }

    public int ajoutCarte(String fln) {

        listeDataCarte = new ArrayList();
        listeFrmVisu = new ArrayList();
        // liste de booleens
        listeUseMethod = new ArrayList();
        listeTypeInit = new ArrayList();
        listeModeBatch = new ArrayList();
        listeAntialias = new ArrayList();
        // double
        listeFourmiCoeff = new ArrayList();
        listeStreamlinesParam = new ArrayList();
        //int
        listeFlagAffich = new ArrayList();
        listeFourmiNB = new ArrayList();

        int id;
        double[] limitC = new double[LENGTH_LIMITC];

        double[] fourmiCoeff = new double[LENGTH_FOURMI_COEFF];
        double[] streamlinesParam = new double[LENGTH_STREAMLINES_PARAM];
        boolean[] cbUse = new boolean[LENGTH_USE_METHOD];
        boolean[] cbFourmi = new boolean[LENGTH_FOURMI_BAFF];
        boolean[] initType = new boolean[1];

        int[] fla = new int[LENGTH_FLAG_AFFICH];
        int[] fourmiNB = new int[LENGTH_FOURMI_NB];

        int i;
        for (i = 0; i < cbUse.length; i++) {
            cbUse[i] = DEFAULT_USE_METHOD[i];
        }
        for (i = 0; i < fourmiNB.length; i++) {
            fourmiNB[i] = DEFAULT_FOURMI_NB[i];
        }
        for (i = 0; i < cbFourmi.length; i++) {
            cbFourmi[i] = DEFAULT_FOURMI_BAFF[i];
        }
        for (i = 0; i < fourmiCoeff.length; i++) {
            fourmiCoeff[i] = DEFAULT_FOURMI_COEFF[i];
        }
        for (i = 0; i < streamlinesParam.length; i++) {
            streamlinesParam[i] = DEFAULT_STREAMLINES_PARAM[i];
        }
        initType[0] = false;
        fla[0] = 1;

        listeUseMethod.add(cbUse);
        id = listeUseMethod.indexOf(cbUse);

        listeFlagAffich.add(fla);

        listeFourmiCoeff.add(fourmiCoeff);
        listeFourmiNB.add(fourmiNB);
        listeTypeInit.add(initType);

        listeAntialias.add(false);
        listeModeBatch.add(false);
        listeStreamlinesParam.add(streamlinesParam);

        OpenNetCdfFile f = new OpenNetCdfFile(frmConfig, true, fln);
        f.setVisible(true);
        if (f.getOK()) {
            try {
                attributesChoice c = new attributesChoice(fln, f.getUIndex(), f.getLevelsDimName(), f.getDatesDimName(), frmConfig, true);
                c.setVisible(true);
                javax.swing.ProgressMonitor p = new javax.swing.ProgressMonitor(this.getFrmConfig(), "Opening maps", "Opening...", 0, 0);
                OpenMapsThread omt = new OpenMapsThread(fln, c.getProfs(), f.getProfsIndex(), c.getDates(), f.getTimesIndex(), c.getTailleX(), f.getUIndex(), c.getTailleY(), f.getVIndex(), f.getMissingValueAttribute(), f.getNoMissingValueState(), p, listeDataCarte, Ok3D, listeFrmVisu, id, this);
                omt.start();
                c.dispose();
            } catch (java.lang.Exception e) {
                System.out.println(e.toString());
            }

        }
        f.dispose();
        return id;
    }

    public final BatchDataMap getBatchDataCarte(int id) {
        return (BatchDataMap) listeDataCarte.get(id);
    }

    public final DataMap getDataCarte(int id) {
        return (DataMap) listeDataCarte.get(id);
    }

    public final int getNbDataCarte() {
        return listeDataCarte.size();
    }

    //public final String getFRes(int id) { return (String)listeFResultat.get(id);}
    //public final String getFConfigHisto(int id) { return (String)listeFConfigHisto.get(id);}
    public final int[] getFlagAffich(int id) {
        return (int[]) listeFlagAffich.get(id);
    }

    public final boolean get3D() {
        return Ok3D;
    }

    public final boolean[] getUseMethod(int id) {
        return (boolean[]) listeUseMethod.get(id);
    }

    public final double[] getStreamParams(int id) {
        return (double[]) listeStreamlinesParam.get(id);
    }

    public final double[] getFourmiCoeff(int id) {
        return (double[]) listeFourmiCoeff.get(id);
    }

    public final int[] getFourmiNB(int id) {
        return (int[]) listeFourmiNB.get(id);
    }

    public final boolean[] getTypeInit(int id) {
        return (boolean[]) listeTypeInit.get(id);
    }

    public final boolean[] getPhysiqueBaff(int id) {
        return (boolean[]) listePhysiqueBAff.get(id);
    }

    public final boolean getAntialias(int id) {
        return ((Boolean) listeAntialias.get(id));
    }

    public void setFourmiCoeff(int id, double[] tb) {
        listeFourmiCoeff.set(id,((double[]) tb.clone()));
    }

    public void setFourmiNB(int id, int[] tb) {
        listeFourmiNB.set(id,((int[]) tb.clone()));
    }

    public void setUseMethod(int id, boolean[] tb) {
        listeUseMethod.set(id,((boolean[]) tb.clone()));
    }

    public void setPhysiqueBAff(int id, boolean[] tb) {
        listePhysiqueBAff.set(id,((boolean[]) tb.clone()));
    }

    public void setStreamlinesParam(int id, double[] tb) {
        listeStreamlinesParam.set(id,((double[]) tb.clone()));
    }

    public void setAntialias(int id, boolean b) {
        listeAntialias.set(id, b);
    }

    public void modifierParametres(int id) {
        if (frmConfig == null) {
            System.out.println(" memoire : PAS de formConfig !");
        } else {
            for (Object listeFrmVisu1 : listeFrmVisu) {
                frmConfig.toFront();
            }

            frmConfig.setUseMethod((boolean[]) listeUseMethod.get(id));
            frmConfig.setStreamlinesParam((double[]) listeStreamlinesParam.get(id));
            //frmConfig.setFourmiCoeff((double[])listeFourmiCoeff.get(id));
            frmConfig.setFourmiNB((int[]) listeFourmiNB.get(id));

        }
    }

    public void exporterDetection(int num, java.awt.Component mum) {
        BatchDataMap d = this.getBatchDataCarte(num);

        String cheminDefaut;

        if (getMemCheminExplo() != null) {
            cheminDefaut = getMemCheminExplo();
        } else {
            cheminDefaut = System.getProperty("user.home");
        }

        javax.swing.JFileChooser F = new javax.swing.JFileChooser(cheminDefaut);

        F.setDialogTitle("Define the name or your NetCDF file");
        F.setMultiSelectionEnabled(false);

        int returnVal = F.showOpenDialog(mum);
        File fichs;
        try {
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                fichs = F.getSelectedFile();
                setMemCheminExplo(F.getCurrentDirectory().getAbsolutePath());
                if (fichs != null) {
                    ArrayInt.D3 arrTags = new ArrayInt.D3(d.getNbDataCartesTps(), d.getDataCarte(0, 0).getYSize(), d.getDataCarte(0, 0).getXSize());
                    for (int i = 0; i < d.getNbDataCartesTps(); i++) {
                        for (int j = 0; j < d.getDataCarte(i, 0).getXSize(); j++) {
                            for (int k = 0; k < d.getDataCarte(i, 0).getYSize(); k++) {
                                int z = 0;
                                while ((z < d.getDataCarte(i, 0).getVortexAnt().size()) && (!d.getDataCarte(i, 0).getVortexAnt().get(z).contains(j, k))) {
                                    z++;
                                }
                                if (z == d.getDataCarte(i, 0).getVortexAnt().size()) {
                                    arrTags.set(i, k, j, 0);
                                } else {
                                    arrTags.set(i, k, j, 1);
                                }
                            }
                        }
                    }
                    //System.out.println("Ouverture du fichier:"+fichs.getAbsolutePath());
                    NetcdfFileWriter out = NetcdfFileWriter.createNew(NetcdfFileWriter.Version.netcdf3, fichs.getAbsolutePath(), null);

                    out.addVariable(null, "Tags", DataType.INT, "time lon lat");

                    out.create();

                    out.flush();
                    out.close();

                    //System.out.println("Fichier cre");
                } else {
                    System.out.println("Nom de fichier = null");
                }
            }
        } catch (IOException e) {
            System.out.println(" FrmConf : erreur de sauvegarde :" + e.toString());
        }
    }

    public void saveMatrix(int i) {
        data.writeTextFile wtf;
        int[][] tab;
        javax.swing.JFileChooser F = new javax.swing.JFileChooser("../");
        F.setDialogTitle(" Choose a name to save vortices ");
        int returnVal = F.showSaveDialog(null);
        try {
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                String file = F.getSelectedFile().getAbsolutePath();
                if (!listeModeBatch.get(i)) {
                    //System.out.println("Mode simple");
                    wtf = new writeTextFile(file);
                    DataMap dat = getDataCarte(i);
                    tab = new int[dat.getXSize()][dat.getYSize()];
                    for (int j = 0; j < dat.getXSize(); j++) {
                        for (int k = 0; k < dat.getYSize(); k++) {
                            int z = 0;
                            while ((z < dat.getVortexAnt().size()) && (!dat.getVortexAnt().get(z).contains(j, k))) {
                                z++;
                            }
                            if (z == dat.getVortexAnt().size()) {
                                tab[j][k] = 0;
                            } else {
                                tab[j][k] = z + 1;
                            }
                        }
                    }

                    //Sauvegarde du tabeau dans un fichier texte
                    String ligne = String.valueOf(dat.getXSize()) + " " + String.valueOf(dat.getYSize());
                    wtf.uneLigne(ligne);
                    for (int j = dat.getYSize() - 1; j >= 0; j--) {
                        ligne = " ";
                        for (int k = 0; k < dat.getXSize(); k++) {
                            ligne = ligne + tab[k][j] + " ";
                        }
                        wtf.uneLigne(ligne);
                    }
                    wtf.fermer();
                } else {
                    //System.out.println("Mode batch");
                    BatchDataMap dat2 = (BatchDataMap) (getDataCarte(i));
                    for (int h = 0; h < dat2.getNbDataCartesTps(); h++) {
                        file = F.getSelectedFile().getAbsolutePath() + "-" + (h + 1) + ".csv";
                        wtf = new writeTextFile(file);
                        DataMap dat = dat2.getDataCarte(h, 0);
                        tab = new int[dat.getXSize()][dat.getYSize()];
                        for (int j = 0; j < dat.getXSize(); j++) {
                            for (int k = 0; k < dat.getYSize(); k++) {
                                int z = 0;
                                while ((z < dat.getVortexAnt().size()) && (!dat.getVortexAnt().get(z).contains(j, k))) {
                                    z++;
                                }
                                if (z == dat.getVortexAnt().size()) {
                                    tab[j][k] = 0;
                                } else {
                                    tab[j][k] = z + 1;
                                }
                            }
                        }

                        //Sauvegarde du tabeau dans un fichier texte
                        String ligne = String.valueOf(dat.getXSize()) + " " + String.valueOf(dat.getYSize());
                        wtf.uneLigne(ligne);
                        for (int j = dat.getYSize() - 1; j >= 0; j--) {
                            ligne = " ";
                            for (int k = 0; k < dat.getXSize(); k++) {
                                ligne = ligne + tab[k][j] + " ";
                            }
                            wtf.uneLigne(ligne);
                        }
                        wtf.fermer();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(" FrmVisu : erreur sauver Vortex ");
        }
    }

    public void appliquerParametres(int id) {
        if (frmConfig == null) {
            System.out.println(" memoire : PAS de formConfig !");
        } else {

            try {
                listeUseMethod.set(id,frmConfig.getUseMethod().clone());

                listeStreamlinesParam.set(id,frmConfig.getStreamParams().clone());
                listeFourmiNB.set(id,frmConfig.getFourmiNB().clone());

            } catch (Exception e) {
                System.out.println("memoire : appliquer parametres : pb");
                e.printStackTrace();
            };

        }
    }

    private boolean hasOnlyOneLeft(int id) {
        boolean b = true;
        int i = 0;
        while (b && (i < listeUseMethod.size())) {
            b = (((i == id) & (listeUseMethod.get(i) != null))
                    | ((i != id) & (listeUseMethod.get(i) == null)));
            i++;
        }
        return b;

    }

    public void retraitCarte(int id) {

        if (!listeDataCarte.isEmpty()) {

            if (this.hasOnlyOneLeft(id)) {

                listeDataCarte.clear();
                ((FrmMap) listeFrmVisu.get(id)).dispose();
                listeFrmVisu.clear();

                // liste les limites (double) de chaque carte
                listeFourmiCoeff.clear();
                // liste de booleens
                listeUseMethod.clear();
                listeModeBatch.clear();
                //  liste d int
                listeFlagAffich.clear();
                listeFourmiNB.clear();
                listeTypeInit.clear();

            } else {

                //System.out.println(" memoire : retrait d'une carte ");
                listeDataCarte.set(id, null);
                ((FrmMap) listeFrmVisu.get(id)).dispose();
                listeFrmVisu.set(id, null);

                // liste les limites (double) de carte
                listeFourmiCoeff.set(id, null);
                // liste de booleens
                listeUseMethod.set(id, null);
                listeTypeInit.set(id, null);
                listeModeBatch.set(id, null);
                // liste d int
                listeFlagAffich.set(id, null);
                listeFourmiNB.set(id, null);

                listeAvancement.set(id, null);

            }
        } else {
            System.out.println(" memoire : pas de carte a retirer ");
        }

    }

    public void retraitDeTouteCarte() {
        if (listeDataCarte != null) {
            if (!listeDataCarte.isEmpty()) {
                int j = listeDataCarte.size();
                for (int i = 0; i < j; i++) {
                    retraitCarte(i);
                }
            }
        }
    }

    public void suivi(int id) {
        //System.out.println("Moteur suivi");
        tracking.MoteurSuivi s = new tracking.MoteurSuivi(((BatchDataMap) (listeDataCarte.get(id))));
        s.LancerSuivi();
    }

    public void demarrer(int id) {

        this.appliquerParametres(id);
        BatchDataMap bdc = ((BatchDataMap) (listeDataCarte.get(id)));
        FrmMap frmv = ((FrmMap) (listeFrmVisu.get(id)));
        javax.swing.ProgressMonitor prog = new javax.swing.ProgressMonitor(this.getFrmVisu(id), "Processing detection...", "Processing...", 0, 0);
        int tmpAv = 0;
        //**************** FOURMIS ***********************

        if (this.getUseMethod(id)[USE_METHOD_FOURMI]) {
            //System.out.println("Moteur fourmi mode batch");
            for (int i = 0; i < bdc.getNbDataCartesTps(); i++) {
                for (int j = 0; j < bdc.getNbDataCartesProf(); j++) {
                    bdc.getDataCarte(i, j).initCollections();
                    frmv.avMax += getFourmiNB(id)[FOURMI_NB_GENERATIONS];
                }
            }
            tmpAv += bdc.getNbDataCartesTps() * bdc.getNbDataCartesProf() * getFourmiNB(id)[FOURMI_NB_GENERATIONS];
        }

        if (this.getUseMethod(id)[USE_METHOD_STREAMLINES]) {
            for (int i = 0; i < bdc.getNbDataCartesTps(); i++) {
                for (int j = 0; j < bdc.getNbDataCartesProf(); j++) {
                    frmv.avMax += bdc.getCarteActive().getOcean()[0].length;
                }
            }
            tmpAv += bdc.getNbDataCartesTps() * bdc.getNbDataCartesProf() * bdc.getCarteActive().getOcean()[0].length;
        }
        prog.setMaximum(tmpAv);
        //		****************** le  fond :*****************
        if (this.getFlagAffich(id)[AFF_FOND] != AFF_RIEN) {

            if (dBug) {
                System.out.println("maj tab ini");
            }

            if (AFFICHER_FOND_AVEC_INTERPOLATION_GRAND_TABLEAU) {
                // Enc apsuler avant d'utiliser
                int[] dim = getFrmVisu(id).getMonCanvas().getDimension();

                if (dBug) {
                    System.out.println("maj ok ini : maj grand tab");
                }

                getDataCarte(id).getTable().changeTaille(dim[0] / DENOMINATEUR_TAILLE_INTERPOLATION, dim[1] / DENOMINATEUR_TAILLE_INTERPOLATION);
                if (dBug) {
                    System.out.println("maj grand tab ok");
                }
                if (dBugFileInterpoler) {
                    getDataCarte(id).getTable().saveGrandTab("c:\\Grandtabini.xls");
                }

                getDataCarte(id).getTable().normaliserGrandTab();
                if (dBugFileInterpoler) {
                    getDataCarte(id).getTable().saveGrandTab("c:\\grandtabNORME.xls");
                }
            }

        }

        //demarrage des moteurs
        if (this.getUseMethod(id)[USE_METHOD_STREAMLINES]) {
            demarrerStreamlines(id, prog);
        }
        if (this.getUseMethod(id)[USE_METHOD_FOURMI]) {
            trouverFourmiBoucle(id, prog);
        }
    }

    public void trouverFourmiBoucle(int id, javax.swing.ProgressMonitor p) {
        // on suppose que la mis a jour est faite !!
        int i, j, ind, esp, runs;
        DataMap dc = (DataMap) listeDataCarte.get(id);

        esp = this.getFourmiNB(id)[FOURMI_NB_ESPECES];
        ind = this.getFourmiNB(id)[FOURMI_NB_INTRA_ESPECE];
        //System.out.println("especes : " + esp + " nb fourmis : " + ind);
        //dc.majSurTerre();
        for (i = 0; i < dc.getXSize(); i++) {
            for (j = 0; j < dc.getYSize(); j++) {
                dc.getC(i, j).calculNorme();
                dc.getC(i, j).calculCU(1, 0);
                dc.getC(i, j).resetPheromone(ind, esp);
            }
        }

        dc.resetCollLoop();

        ((FrmMap) listeFrmVisu.get(id)).toFront();
        int generations;
        double evap, depot;
        boolean intType;
        generations = this.getFourmiNB(id)[FOURMI_NB_GENERATIONS];
        evap = this.getFourmiCoeff(id)[FOURMI_COEFF_EVAPORATION];
        depot = this.getFourmiCoeff(id)[FOURMI_QTE_DEPOT];
        intType = getTypeInit(id)[0];
        runs = getFourmiNB(id)[FOURMI_NB_RUNS];

        ((BatchDataMap) (dc)).setCarteActive(0, 0);

        ants.SearchEngine moteur = new ants.SearchEngine(((BatchDataMap) (dc)), getFrmVisu(id), ind, esp, generations, depot, evap, runs, p);
        moteur.start();

        if (((BatchDataMap) (dc)).getNbDataCartesTps() > 1) {
            ((FrmMap) (listeFrmVisu.get(id))).EnableTracking();
        }
    }

    private void demarrerStreamlines(int id, javax.swing.ProgressMonitor p) {

        ((BatchDataMap) listeDataCarte.get(id)).resetVortexStreamlines();
        //System.out.println("Streamlines mode batch");
        ((BatchDataMap) listeDataCarte.get(id)).setCarteActive(0, 0);
        streamlines.StreamlinesEngine moteur = new streamlines.StreamlinesEngine((BatchDataMap) listeDataCarte.get(id), this, id, p);
        moteur.start();
    }

    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //***************************************************************************************
    /**
     * un genre de destructeur
     */
    public void fin() {
        configHistoFile = null;
        frmConfig = null;
        System.gc();
        System.out.println(" memoire : GC ok ;   Fin. ");
        System.exit(0);
    }

}
