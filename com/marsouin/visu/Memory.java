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

import com.marsouin.data.OpenNetCdfFile;
import com.marsouin.data.BatchDataMap;
import com.marsouin.data.WriteTextFile;
import com.marsouin.data.AttributesChoice;
import com.marsouin.data.DataMap;
import com.marsouin.tracking.TrackEngine;
import java.io.File;
import java.io.IOException;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ProgressMonitor;
import ucar.nc2.*;
import ucar.ma2.*;
import static ucar.nc2.NetcdfFileWriter.createNew;

public class Memory implements com.marsouin.constants.Centre, com.marsouin.constants.Colors, com.marsouin.constants.Ant, com.marsouin.constants.DefaultValues, com.marsouin.constants.Stream, com.marsouin.constants.Streamlines {

    private static final Logger log = Logger.getLogger(Memory.class.getName());

    private boolean Ok3D = false;

    private String memCheminExplo = null;

    private ArrayList<BatchDataMap> listeDataCarte = null;

    private ArrayList<FrmMap> listeFrmVisu = null;

    // liste de int[]
    private ArrayList<int[]> listeFlagAffich = null;
    private ArrayList<int[]> listeFourmiNB = null;
    private ArrayList<boolean[]> listeTypeInit = null;

    // liste les coeff (double) pour la frmConfig de chaque carte
    private ArrayList<double[]> listeFourmiCoeff = null;
    private ArrayList<double[]> listeStreamlinesParam = null;

    // liste de booleens
    private ArrayList<boolean[]> listeUseMethod = null;
    private ArrayList<Boolean> listeModeBatch = null; //true = batch, false = no batch
    private ArrayList<Boolean> listeAntialias = null;

    // liste de String
    private final ConfigHistoFile configHistoFile = null;
    //private ArrayList listeFConfigHisto=null;
    //private ArrayList listeFResultat=null;

    private FrmConf frmConfig = null;

    /**
     * Creates a new instance of Memoire
     */
    public Memory() {
        log.setLevel(log.getParent().getLevel());
    }

    public FrmMap getFrmVisu(int id) {
        return listeFrmVisu.get(id);
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
        log.log(Level.INFO,"Starting add map procedure");
        listeDataCarte = new ArrayList<>();
        listeFrmVisu = new ArrayList<>();
        // liste de booleens
        listeUseMethod = new ArrayList<>();
        listeTypeInit = new ArrayList<>();
        listeModeBatch = new ArrayList<>();
        listeAntialias = new ArrayList<>();
        // double
        listeFourmiCoeff = new ArrayList<>();
        listeStreamlinesParam = new ArrayList<>();
        //int
        listeFlagAffich = new ArrayList<>();
        listeFourmiNB = new ArrayList<>();

        int id;

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

        log.log(Level.INFO,"All config done");
        log.log(Level.INFO,"Opening NetCDF file");
        OpenNetCdfFile f = new OpenNetCdfFile(frmConfig, true, fln);
        f.setVisible(true);
        if (f.getOK()) {
            try {
                AttributesChoice c = new AttributesChoice(fln, f.getUIndex(), f.getLevelsDimName(), f.getDatesDimName(), frmConfig, true);
                c.setVisible(true);
                javax.swing.ProgressMonitor p = new javax.swing.ProgressMonitor(this.getFrmConfig(), "Opening maps", "Opening...", 0, 0);
                OpenMapsThread omt = new OpenMapsThread(fln, c.getProfs(), f.getProfsIndex(), c.getDates(), f.getTimesIndex(), c.getTailleX(), f.getUIndex(), c.getTailleY(), f.getVIndex(), f.getMissingValueAttribute(), f.getNoMissingValueState(), p, listeDataCarte, Ok3D, listeFrmVisu, id, this);
                omt.start();
                c.dispose();
            } catch (java.lang.Exception e) {
                log.log(Level.SEVERE, "Failed to add the map", e);
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
        return listeFlagAffich.get(id);
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

    public final boolean getAntialias(int id) {
        return listeAntialias.get(id);
    }

    public void setFourmiCoeff(int id, double[] tb) {
        listeFourmiCoeff.set(id, ((double[]) tb.clone()));
    }

    public void setFourmiNB(int id, int[] tb) {
        listeFourmiNB.set(id, ((int[]) tb.clone()));
    }

    public void setUseMethod(int id, boolean[] tb) {
        listeUseMethod.set(id, ((boolean[]) tb.clone()));
    }

    public void setStreamlinesParam(int id, double[] tb) {
        listeStreamlinesParam.set(id, ((double[]) tb.clone()));
    }

    public void setAntialias(int id, boolean b) {
        listeAntialias.set(id, b);
    }

    public void modifierParametres(int id) {
        try{
            for (Object listeFrmVisu1 : listeFrmVisu) {
                frmConfig.toFront();
            }

            frmConfig.setUseMethod((boolean[]) listeUseMethod.get(id));
            frmConfig.setStreamlinesParam((double[]) listeStreamlinesParam.get(id));
            //frmConfig.setFourmiCoeff((double[])listeFourmiCoeff.get(id));
            frmConfig.setFourmiNB((int[]) listeFourmiNB.get(id));

        }catch(Exception e){
            log.log(Level.SEVERE,"Problem in updating parameters",e);
        }
    }

    public void exporterDetection(int num, java.awt.Component mum) {
        BatchDataMap d = this.getBatchDataCarte(num);

        String cheminDefaut;

        if (getMemCheminExplo() != null) {
            cheminDefaut = getMemCheminExplo();
        } else {
            cheminDefaut = getProperty("user.home");
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
                    NetcdfFileWriter out = createNew(NetcdfFileWriter.Version.netcdf3, fichs.getAbsolutePath(), null);

                    out.addVariable(null, "Tags", DataType.INT, "time lon lat");

                    out.create();

                    out.flush();
                    out.close();

                    //System.out.println("Fichier cre");
                } else {
                    log.info("No file selected");
                }
            }
        } catch (IOException e) {
            System.out.println(" FrmConf : erreur de sauvegarde :" + e.toString());
        }
    }

    public void saveMatrix(int i) {
        com.marsouin.data.WriteTextFile wtf;
        int[][] tab;
        javax.swing.JFileChooser F = new javax.swing.JFileChooser("../");
        F.setDialogTitle("Choose a name to save vortices");
        int returnVal = F.showSaveDialog(null);
        try {
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                String file = F.getSelectedFile().getAbsolutePath();
                if (!listeModeBatch.get(i)) {
                    //System.out.println("Mode simple");
                    wtf = new WriteTextFile(file);
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
                    String ligne = valueOf(dat.getXSize()) + " " + valueOf(dat.getYSize());
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
                        wtf = new WriteTextFile(file);
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
                        String ligne = valueOf(dat.getXSize()) + " " + valueOf(dat.getYSize());
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
            log.log(Level.SEVERE, "Error is saving a vortex", e);
        }
    }

    public void appliquerParametres(int id) {

        try {
            listeUseMethod.set(id, frmConfig.getUseMethod().clone());

            listeStreamlinesParam.set(id, frmConfig.getStreamParams().clone());
            listeFourmiNB.set(id, frmConfig.getFourmiNB().clone());

        } catch (Exception e) {
            log.log(Level.SEVERE, "problem in applying the parameters", e);
        };

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
            }
        } else {
            log.info("No map to remove");
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
        TrackEngine s = new TrackEngine((listeDataCarte.get(id)));
        s.LancerSuivi();
    }

    public void demarrer(int id) {

        this.appliquerParametres(id);
        BatchDataMap bdc = listeDataCarte.get(id);
        FrmMap frmv = ((FrmMap) (listeFrmVisu.get(id)));
        ProgressMonitor prog = new ProgressMonitor(this.getFrmVisu(id), "Processing detection...", "Processing...", 0, 0);
        int tmpAv = 0;
        //**************** FOURMIS ***********************

        if (this.getUseMethod(id)[USE_METHOD_ANTS]) {
            //System.out.println("Moteur Ant mode batch");
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

        //demarrage des moteurs
        if (this.getUseMethod(id)[USE_METHOD_STREAMLINES]) {
            startStreamlines(id, prog);
        }
        if (this.getUseMethod(id)[USE_METHOD_ANTS]) {
            findLoopAnt(id, prog);
        }
    }

    public void findLoopAnt(int id, javax.swing.ProgressMonitor p) {
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
        generations = this.getFourmiNB(id)[FOURMI_NB_GENERATIONS];
        evap = this.getFourmiCoeff(id)[FOURMI_COEFF_EVAPORATION];
        depot = this.getFourmiCoeff(id)[FOURMI_QTE_DEPOT];
        runs = getFourmiNB(id)[FOURMI_NB_RUNS];

        ((BatchDataMap) (dc)).setCarteActive(0, 0);

        com.marsouin.ants.SearchEngine moteur = new com.marsouin.ants.SearchEngine(((BatchDataMap) (dc)), getFrmVisu(id), ind, esp, generations, depot, evap, runs, p);
        moteur.start();

        if (((BatchDataMap) (dc)).getNbDataCartesTps() > 1) {
            ((FrmMap) (listeFrmVisu.get(id))).EnableTracking();
        }
    }

    private void startStreamlines(int id, javax.swing.ProgressMonitor p) {

        ((BatchDataMap) listeDataCarte.get(id)).resetVortexStreamlines();
        //System.out.println("Streamlines mode batch");
        ((BatchDataMap) listeDataCarte.get(id)).setCarteActive(0, 0);
        com.marsouin.streamlines.StreamlinesEngine moteur = new com.marsouin.streamlines.StreamlinesEngine((BatchDataMap) listeDataCarte.get(id), this, id, p);
        moteur.start();
    }
}
