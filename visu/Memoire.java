/*
 * Memoire.java
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

import data.*;
import streamlines.*;
import java.io.File;
import ucar.nc2.*;
import ucar.ma2.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class Memoire implements constants.physique, constants.centre, constants.couleur, constants.fourmi, constants.valeurs_par_defaut, constants.courant, constants.streamlines {
    
    private int progress;
    private boolean done=false;
    private boolean Ok3D = false;
    
    private static boolean dBug = false;
    private static boolean dBugPlus = false;
    private static boolean dBugFileInterpoler = false;
    
    
    private static int nbTrouverCentre=0;
    
    private String memCheminExplo = null;
    
    private java.util.Vector listeDataCarte=null;
    
    private java.util.Vector listeFrmVisu=null;
    
    // liste de int[]
    private java.util.Vector listeFlagAffich=null;
    private java.util.Vector listeFourmiNB=null;
    private java.util.Vector listeTypeInit=null;
    private java.util.Vector listeFourmiParEspece=null;
    
    // liste les coeff (double) pour la frmConfig de chaque carte
    private java.util.Vector listeFourmiCoeff=null;
    private java.util.Vector listeStreamlinesParam=null;
    
    // liste de booleens
    private java.util.Vector listeUseMethod=null;
    private java.util.Vector listePhysiqueBAff=null;
    private java.util.Vector listeModeBatch=null; //true = batch, false = pas batch
    private java.util.Vector listeAntialias=null;
    
    java.util.Vector listeAvancement=null;
    
    // liste de String
    private ConfigHistoFile configHistoFile = null;
    //private java.util.Vector listeFConfigHisto=null;
    //private java.util.Vector listeFResultat=null;
    
    private FrmConf frmConfig=null;
    
    private ants.SearchEngine moteurFourmi=null;
    private java.util.Vector moteursFourmi = null;
    
    
    /** Creates a new instance of Memoire */
    public Memoire() {
        //configHistoFile = new ConfigHistoFile();
        //moteurFourmi = new fourmi.SearchEngine();
    }
    
    public FrmCarte getFrmVisu(int id) {
        return (FrmCarte) listeFrmVisu.elementAt(id);
    }
    
    public FrmConf getFrmConfig() {
        return frmConfig;
    }
    
    public String getMemCheminExplo(){
        return memCheminExplo;
    }
    
    public void setMemCheminExplo(String s){
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
        
        listeDataCarte = new java.util.Vector();
        listeFrmVisu = new java.util.Vector();
        // liste de booleens
        listeUseMethod=new java.util.Vector();
        listeTypeInit=new java.util.Vector();
        listeModeBatch = new java.util.Vector();
        listeAntialias = new java.util.Vector();
        // double
        listeFourmiCoeff = new java.util.Vector();
        listeStreamlinesParam = new java.util.Vector();
        //int
        listeFlagAffich = new java.util.Vector();
        listeFourmiNB = new java.util.Vector();
        
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
        for( i=0; i< cbUse.length; i++)
            cbUse[i] = DEFAULT_USE_METHOD[i];
        for( i=0; i< fourmiNB.length; i++)
            fourmiNB[i] = DEFAULT_FOURMI_NB[i];
        for( i=0; i< cbFourmi.length; i++)
            cbFourmi[i] = DEFAULT_FOURMI_BAFF[i];
        for( i=0; i< fourmiCoeff.length; i++)
            fourmiCoeff[i] = DEFAULT_FOURMI_COEFF[i];
        for( i=0; i< streamlinesParam.length; i++)
            streamlinesParam[i] = DEFAULT_STREAMLINES_PARAM[i];
        initType[0]=false;
        fla[0]=1;
        
        
        listeUseMethod.addElement(cbUse);
        id = listeUseMethod.indexOf(cbUse);
        
        listeFlagAffich.addElement(fla);
        
        listeFourmiCoeff.addElement(fourmiCoeff);
        listeFourmiNB.addElement(fourmiNB);
        listeTypeInit.addElement(initType);
        
        listeAntialias.addElement(new java.lang.Boolean(false));
        listeModeBatch.addElement(new Boolean(false));
        listeStreamlinesParam.addElement(streamlinesParam);

        OpenNetCdfFile f = new OpenNetCdfFile(frmConfig, true, fln);
        f.setVisible(true);
        if(f.getOK()){
            try{
                attributesChoice c = new attributesChoice(fln,f.getUIndex(),f.getLevelsDimName(),f.getDatesDimName(),frmConfig,true);
                c.setVisible(true);
                javax.swing.ProgressMonitor p = new javax.swing.ProgressMonitor(this.getFrmConfig(),"Opening maps","Opening...",0,0);
                OpenMapsThread omt = new OpenMapsThread(fln,c.getProfs(),f.getProfsIndex(),c.getDates(),f.getTimesIndex(),c.getTailleX(),f.getUIndex(),c.getTailleY(),f.getVIndex(),f.getMissingValueAttribute(),f.getNoMissingValueState(),p,listeDataCarte, Ok3D, listeFrmVisu, id, this);
                omt.start();
                c.dispose();
            }catch(java.lang.Exception e){System.out.println(e.toString());}
            
        }
        f.dispose();
        return id;
    }
    public final BatchDataMap getBatchDataCarte(int id){ return (BatchDataMap) listeDataCarte.elementAt(id);}
    public final DataMap getDataCarte(int id){ return (DataMap) listeDataCarte.elementAt(id);}
    
    public final int getNbDataCarte(){ return listeDataCarte.size();}
    
    //public final String getFRes(int id) { return (String)listeFResultat.elementAt(id);}
    //public final String getFConfigHisto(int id) { return (String)listeFConfigHisto.elementAt(id);}
    
    public final int[] getFlagAffich(int id) { return (int[]) listeFlagAffich.elementAt(id);}
    public final boolean get3D() { return Ok3D; }
    public final boolean[] getUseMethod(int id) { return (boolean[])listeUseMethod.elementAt(id); }
    public final double[] getStreamParams(int id) { return (double[])listeStreamlinesParam.elementAt(id); }
    public final double[] getFourmiCoeff(int id) { return (double[])listeFourmiCoeff.elementAt(id); }
    public final int[] getFourmiNB(int id) { return (int[])listeFourmiNB.elementAt(id); }
    public final boolean[] getTypeInit(int id) { return (boolean[])listeTypeInit.elementAt(id); }
    public final boolean[] getPhysiqueBaff(int id) { return (boolean[])listePhysiqueBAff.elementAt(id); }
    public final boolean getAntialias(int id) { return ((Boolean)listeAntialias.elementAt(id)).booleanValue(); }
    
    public void setFourmiCoeff(int id,double[] tb) { listeFourmiCoeff.setElementAt(((double[])tb.clone()),id); }
    public void setFourmiNB(int id,int[] tb) { listeFourmiNB.setElementAt(((int[])tb.clone()),id); }
    public void setUseMethod(int id,boolean[] tb) { listeUseMethod.setElementAt(((boolean[])tb.clone()),id); }
    public void setPhysiqueBAff(int id,boolean[] tb) { listePhysiqueBAff.setElementAt(((boolean[])tb.clone()),id); }
    public void setStreamlinesParam(int id,double[] tb) { listeStreamlinesParam.setElementAt(((double[])tb.clone()),id); }
    public void setAntialias(int id, boolean b) {listeAntialias.setElementAt(new Boolean(b),id); }
    
    public void modifierParametres(int id) {
        if (frmConfig==null) System.out.println(" memoire : PAS de formConfig !");
        else {
            for(int i = 0; i<listeFrmVisu.size();i++)
                frmConfig.toFront();
            
            frmConfig.setUseMethod((boolean[])listeUseMethod.elementAt(id));
            frmConfig.setStreamlinesParam((double[])listeStreamlinesParam.elementAt(id));
            //frmConfig.setFourmiCoeff((double[])listeFourmiCoeff.elementAt(id));
            frmConfig.setFourmiNB((int[])listeFourmiNB.elementAt(id));
            
        }
    }
    
    public void exporterDetection(int num, java.awt.Component mum){
        BatchDataMap d = this.getBatchDataCarte(num);
        
        String cheminDefaut;
        
        if(getMemCheminExplo() != null)
            cheminDefaut = getMemCheminExplo();
        else
            cheminDefaut = System.getProperty("user.home");
        
        javax.swing.JFileChooser F = new javax.swing.JFileChooser(cheminDefaut);
        
        F.setDialogTitle("Define the name or your NetCDF file");
        F.setMultiSelectionEnabled(false);
        
        int returnVal=F.showOpenDialog(mum);
        File fichs=null;
        try{
            if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION){
                fichs = F.getSelectedFile();
                setMemCheminExplo(F.getCurrentDirectory().getAbsolutePath());
                if(fichs!=null){
                    ArrayInt.D3 arrTags = new ArrayInt.D3(d.getNbDataCartesTps(), d.getDataCarte(0, 0).getYSize(),d.getDataCarte(0, 0).getXSize());
                    for(int i=0;i<d.getNbDataCartesTps();i++){
                        for(int j=0;j<d.getDataCarte(i, 0).getXSize();j++){
                            for(int k=0;k<d.getDataCarte(i, 0).getYSize();k++){
                                int z = 0;
                                while((z<d.getDataCarte(i, 0).getVortexAnt().getMetaVortex().length) && (!d.getDataCarte(i, 0).getVortexAnt().getMetaVortex(z).contains(j,k)))
                                    z++;
                                if(z==d.getDataCarte(i, 0).getVortexAnt().getMetaVortex().length)
                                    arrTags.set(i,k,j,0);
                                else
                                    arrTags.set(i,k,j,1);
                            }
                        }
                    }
                    //System.out.println("Ouverture du fichier:"+fichs.getAbsolutePath());
                    NetcdfFileWriteable out = new NetcdfFileWriteable();
                    out.setName(fichs.getAbsolutePath());
                    //System.out.println("Fichier ouvert");
                    ucar.nc2.Dimension[] dim = new ucar.nc2.Dimension[3];
                    dim[0] = out.addDimension("time", d.getNbDataCartesTps());
                    dim[1] = out.addDimension("lon", d.getDataCarte(0, 0).getYSize());
                    dim[2] = out.addDimension("lat", d.getDataCarte(0, 0).getXSize());
                    
                    out.addVariable("Tags",java.lang.Integer.TYPE , dim);
                    
                    out.create();
                    
                    out.write("Tags", arrTags);
                    
                    out.flush();
                    out.close();
                    
                    //System.out.println("Fichier cre");
                }else
                    System.out.println("Nom de fichier = null");
            }
        }catch(Exception e){ System.out.println(" FrmConf : erreur de sauvegarde :"+e.toString());}
    }
    
    public void saveMatrix(int i){
        data.writeTextFile wtf;
        int[][] tab;
        javax.swing.JFileChooser F = new javax.swing.JFileChooser("../");
        F.setDialogTitle(" Choose a name to save vortices ");
        int returnVal=F.showSaveDialog(null);
        try{
            if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                String file = F.getSelectedFile().getAbsolutePath();
                if(!((java.lang.Boolean)(listeModeBatch.elementAt(i))).booleanValue()){
                    //System.out.println("Mode simple");
                    wtf=new writeTextFile(file);
                    DataMap dat = getDataCarte(i);
                    tab = new int[dat.getXSize()][dat.getYSize()];
                    for(int j=0;j<dat.getXSize();j++)
                        for(int k=0;k<dat.getYSize();k++){
                        int z = 0;
                        while((z<dat.getVortexAnt().getMetaVortex().length) && (!dat.getVortexAnt().getMetaVortex(z).contains(j,k)))
                            z++;
                        if(z==dat.getVortexAnt().getMetaVortex().length)
                            tab[j][k]=0;
                        else
                            tab[j][k]=z+1;
                        }
                    
                    //Sauvegarde du tabeau dans un fichier texte
                    String ligne=String.valueOf(dat.getXSize()) + " " + String.valueOf(dat.getYSize());
                    wtf.uneLigne(ligne);
                    for (int j=dat.getYSize()-1;j>=0;j--) {
                        ligne=" ";
                        for (int k=0;k<dat.getXSize();k++)
                            ligne=ligne+tab[k][j]+" ";
                        wtf.uneLigne(ligne);
                    }
                    wtf.fermer();
                } else {
                    //System.out.println("Mode batch");
                    BatchDataMap dat2 = (BatchDataMap)(getDataCarte(i));
                    for(int h=0;h<dat2.getNbDataCartesTps();h++){
                        file = F.getSelectedFile().getAbsolutePath() + "-" + (h+1)+".csv";
                        wtf=new writeTextFile(file);
                        DataMap dat = dat2.getDataCarte(h,0);
                        tab = new int[dat.getXSize()][dat.getYSize()];
                        for(int j=0;j<dat.getXSize();j++)
                            for(int k=0;k<dat.getYSize();k++){
                            int z = 0;
                            while((z<dat.getVortexAnt().getMetaVortex().length) && (!dat.getVortexAnt().getMetaVortex(z).contains(j,k)))
                                z++;
                            if(z==dat.getVortexAnt().getMetaVortex().length)
                                tab[j][k]=0;
                            else
                                tab[j][k]=z+1;
                            }
                        
                        //Sauvegarde du tabeau dans un fichier texte
                        String ligne=String.valueOf(dat.getXSize()) + " " + String.valueOf(dat.getYSize());
                        wtf.uneLigne(ligne);
                        for (int j=dat.getYSize()-1;j>=0;j--) {
                            ligne=" ";
                            for (int k=0;k<dat.getXSize();k++)
                                ligne=ligne+tab[k][j]+" ";
                            wtf.uneLigne(ligne);
                        }
                        wtf.fermer();
                    }
                }
            }
        }catch(Exception e){ System.out.println(" FrmVisu : erreur sauver Vortex ");}
    }
    
    public void appliquerParametres(int id){
        if (frmConfig==null) System.out.println(" memoire : PAS de formConfig !");
        else {
            
            try{
                listeUseMethod.setElementAt(((boolean[])frmConfig.getUseMethod().clone()),id);
                
                listeStreamlinesParam.setElementAt(((double[])frmConfig.getStreamParams().clone()),id);
                listeFourmiNB.setElementAt(((int[])frmConfig.getFourmiNB().clone()),id);
                
                
            }catch(Exception e){System.out.println("memoire : appliquer parametres : pb"); e.printStackTrace();};
            
        }
    }
    
    private boolean hasOnlyOneLeft(int id){
        boolean b=true;
        int i = 0;
        while(b && (i<listeUseMethod.size())){
            b=( ((i==id)&(listeUseMethod.elementAt(i)!=null))
            |((i!=id)&(listeUseMethod.elementAt(i)==null) )) ;
            i++;}
        return b;
        
    }
    
    public void retraitCarte(int id) {
        
        if (!listeDataCarte.isEmpty()) {
            
            if (this.hasOnlyOneLeft(id)) {
  
                listeDataCarte.removeAllElements();
                ((FrmCarte)listeFrmVisu.elementAt(id)).dispose();
                listeFrmVisu.removeAllElements();
                
                // liste les limites (double) de chaque carte
                listeFourmiCoeff.removeAllElements();
                // liste de booleens
                listeUseMethod.removeAllElements();
                listeModeBatch.removeAllElements();
                //  liste d int
                listeFlagAffich.removeAllElements();
                listeFourmiNB.removeAllElements();
                listeTypeInit.removeAllElements();
                
                // liste de String
                // listeFConfigHisto.removeAllElements();
                // listeFResultat.removeAllElements();
                
                System.gc();
            } else {
                
                //System.out.println(" memoire : retrait d'une carte ");
                listeDataCarte.setElementAt(null,id);
                ((FrmCarte)listeFrmVisu.elementAt(id)).dispose();
                listeFrmVisu.setElementAt(null,id);
                
                // liste les limites (double) de carte
                listeFourmiCoeff.setElementAt(null,id);
                // liste de booleens
                listeUseMethod.setElementAt(null,id);
                listeTypeInit.setElementAt(null,id);
                listeModeBatch.setElementAt(null,id);
                // liste d int
                listeFlagAffich.setElementAt(null,id);
                listeFourmiNB.setElementAt(null,id);
                
                listeAvancement.setElementAt(null,id);
                // liste de String
                //listeFConfigHisto.setElementAt(null,id);
                //listeFResultat.setElementAt(null,id);
                
                
            }
        }
        
        else System.out.println(" memoire : pas de carte a retirer ");
        
    }
    public void retraitDeTouteCarte() {
        if (listeDataCarte!=null)
            if (!listeDataCarte.isEmpty()) {
            int j = listeDataCarte.size();
            for(int i=0;i<j;i++)
                retraitCarte(i);
            }
    }
    
    public void suivi(int id){
        //System.out.println("Moteur suivi");
        tracking.MoteurSuivi s = new tracking.MoteurSuivi(((BatchDataMap)(listeDataCarte.elementAt(id))));
        s.LancerSuivi();
    }
    
    public void demarrer(int id) {
        
        this.appliquerParametres(id);
        BatchDataMap bdc = ((BatchDataMap)(listeDataCarte.elementAt(id)));
        FrmCarte frmv = ((FrmCarte)(listeFrmVisu.elementAt(id)));
        javax.swing.ProgressMonitor prog = new javax.swing.ProgressMonitor(this.getFrmVisu(id),"Processing detection...","Processing...",0,0);
        int tmpAv = 0;
        //**************** FOURMIS ***********************
        
        if (this.getUseMethod(id)[USE_METHOD_FOURMI]){
            //System.out.println("Moteur fourmi mode batch");
            moteursFourmi = new java.util.Vector();
            for(int i=0;i<bdc.getNbDataCartesTps();i++)
                for(int j=0;j<bdc.getNbDataCartesProf();j++){
                //moteursFourmi.addElement(new fourmi.SearchEngine());
                //((fourmi.SearchEngine)(moteursFourmi.elementAt(i))).reInit();
                bdc.getDataCarte(i,j).initCollections();
                frmv.avMax += getFourmiNB(id)[FOURMI_NB_GENERATIONS];
                }
            tmpAv+=bdc.getNbDataCartesTps()*bdc.getNbDataCartesProf()*getFourmiNB(id)[FOURMI_NB_GENERATIONS];
        }
        
        if (this.getUseMethod(id)[USE_METHOD_STREAMLINES]){
            for(int i=0;i<bdc.getNbDataCartesTps();i++)
                for(int j=0;j<bdc.getNbDataCartesProf();j++){
                frmv.avMax += bdc.getCarteActive().getOcean()[0].length;
                }
          tmpAv+=bdc.getNbDataCartesTps()*bdc.getNbDataCartesProf()*bdc.getCarteActive().getOcean()[0].length;  
        }
        prog.setMaximum(tmpAv);
        //		****************** le  fond :*****************
        if (this.getFlagAffich(id)[AFF_FOND]!=AFF_RIEN) {
            
            if (dBug) System.out.println("maj tab ini");
            
            if (AFFICHER_FOND_AVEC_INTERPOLATION_GRAND_TABLEAU) {
                // Enc apsuler avant d'utiliser
                int[] dim = getFrmVisu(id).getMonCanvas().getDimension();
                
                if (dBug) System.out.println("maj ok ini : maj grand tab");
                
                getDataCarte(id).getTable().changeTaille(dim[0]/DENOMINATEUR_TAILLE_INTERPOLATION,dim[1]/DENOMINATEUR_TAILLE_INTERPOLATION);
                if (dBug) System.out.println("maj grand tab ok");
                if (dBugFileInterpoler) getDataCarte(id).getTable().saveGrandTab("c:\\Grandtabini.xls");
                
                getDataCarte(id).getTable().normaliserGrandTab();
                if (dBugFileInterpoler) getDataCarte(id).getTable().saveGrandTab("c:\\grandtabNORME.xls");
            }
            
        }
             
        //demarrage des moteurs
        if (this.getUseMethod(id)[USE_METHOD_STREAMLINES]){
            demarrerStreamlines(id, prog);
        }
        if (this.getUseMethod(id)[USE_METHOD_FOURMI]){
            trouverFourmiBoucle(id, prog);
        }
    }
    
    
    public void trouverFourmiBoucle(int id, javax.swing.ProgressMonitor p){
        // on suppose que la mis a jour est faite !!
        int i,j,ind,esp,runs;
        DataMap dc = (DataMap)listeDataCarte.elementAt(id);
        
        esp = this.getFourmiNB(id)[FOURMI_NB_ESPECES];
        ind = this.getFourmiNB(id)[FOURMI_NB_INTRA_ESPECE];
        //System.out.println("especes : " + esp + " nb fourmis : " + ind);
        //dc.majSurTerre();
        for (i=0; i<dc.getXSize(); i++)
            for (j=0; j<dc.getYSize(); j++) {
            dc.getC(i,j).calculNorme();
            dc.getC(i,j).calculCU(1,0);
            dc.getC(i,j).resetPheromone(ind,esp);
            }
        
        dc.getCollLoop().effacerTout();
        
        ((FrmCarte) listeFrmVisu.elementAt(id)).toFront();
        int generations;
        double evap,depot;
        boolean intType;
        generations = this.getFourmiNB(id)[FOURMI_NB_GENERATIONS];
        evap = this.getFourmiCoeff(id)[FOURMI_COEFF_EVAPORATION];
        depot = this.getFourmiCoeff(id)[FOURMI_QTE_DEPOT];
        intType = getTypeInit(id)[0];
        runs = getFourmiNB(id)[FOURMI_NB_RUNS];
        
        ((BatchDataMap)(dc)).setCarteActive(0,0);
        
        ants.SearchEngine moteur = new ants.SearchEngine(((BatchDataMap)(dc)),getFrmVisu(id),ind, esp, generations, depot, evap, runs, p);
        moteur.start();
        
        if(((BatchDataMap)(dc)).getNbDataCartesTps()>1)
            ((FrmCarte)(listeFrmVisu.elementAt(id))).EnableTracking();
    }
    
    
    private void demarrerSuivi(int id){
        tracking.MoteurSuivi m = new tracking.MoteurSuivi(((BatchDataMap)(listeDataCarte.elementAt(id))));
    }    
    
    private void demarrerStreamlines(int id, javax.swing.ProgressMonitor p){

        ((BatchDataMap)listeDataCarte.elementAt(id)).getVortexStreamlines().effacerTout();
        //System.out.println("Streamlines mode batch");
        java.util.Vector moteursStream = new java.util.Vector();
        progress = 0;
        int cpt=0;
        done = false;
        ((BatchDataMap)listeDataCarte.elementAt(id)).setCarteActive(0,0);
        streamlines.moteurStreamlines moteur = new streamlines.moteurStreamlines((BatchDataMap)listeDataCarte.elementAt(id),this,id,p);
        moteur.start();
    }
    
    
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //***************************************************************************************
    /** un genre de destructeur */
    public void fin(){
        configHistoFile = null;
        frmConfig = null;
        System.gc();
        System.out.println(" memoire : GC ok ;   Fin. ");
        System.exit(0);
    }
    
}


