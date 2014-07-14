/*
 * BatchDataCarte.java
 *
 * Created on 6 mai 2003, 10:11
 */

/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */

package data;

import java.io.IOException;
import javax.swing.*;
import java.util.ArrayList;
import ucar.nc2.*;
import ucar.ma2.*;

public class BatchDataCarte extends DataCarte{
    
    private ArrayList listeDataCartes, listeDimensions;
    private int[] active;
    public int cpt = 0;
    private javax.swing.ProgressMonitor p;
    private int varX, varY;
    private double[][][][] listeMoyennes;
    private Timer timer;
    private int nbCartes;
    public final static int ONE_SECOND = 1000;
    
    /** Creates a new instance of BatchDataCarte */
    public BatchDataCarte(String f,int[] prof,int profInd, int[] time, int timeInd, int u, int uInd, int v, int vInd, String MVAtt, boolean noMV, javax.swing.ProgressMonitor prog) {
        super(f,prof[0],profInd,time[0],timeInd,u,uInd,v,vInd, MVAtt, noMV);
        p = prog;
        varX = u;
        varY = v;
        listeDataCartes = new ArrayList();
        listeDimensions = new ArrayList();
        
        active = new int[2];
        active[0]=0;
        active[1]=0;
        nbCartes = time.length*prof.length;
        try{
            NetcdfFile fch;
            fch = new NetcdfFile(f);
            Variable var = ((Variable)(fch.getVariables().get(uInd)));
            Array tab = var.read();
            Index ind = tab.getIndex();
            for(int i=0;i<var.getDimensions().size();i++){
                //names.add(var.getDimension(i).getName());
                Variable varTmp = ((Variable)(fch.findVariable(var.getDimension(i).getName())));
                //System.out.println(varTmp.getDataType().toString());
                double tmpTab[] = new double[var.getDimension(i).getLength()];
                Array tab2 = varTmp.read();
                Index ind2 = tab2.getIndex();
                
                for(int tt=0;tt<var.getDimension(i).getLength();tt++){
                    ind2.set(tt);
                    tmpTab[tt] = tab2.getDouble(ind2);
                    //System.out.println(tmpTab[tt]);
                }
                listeDimensions.add(tmpTab);
            }
            fch.close();
            fch=null;
        }catch(IOException e){System.out.println(e.toString());}
        cpt = 0;
        p.setMaximum(time.length*prof.length);
        for(int i=0;i<time.length;i++){
            listeDataCartes.add(new ArrayList());
            for(int k = 0;k<prof.length;k++){
                try{
                    if((i==0) && (k==0))
                        ((ArrayList)listeDataCartes.get(i)).add(new DataCarte(this));
                    else
                        ((ArrayList)listeDataCartes.get(i)).add(new DataCarte(f,prof[k],profInd,time[i],timeInd,u,uInd,v,vInd, MVAtt, noMV));
                }catch(Exception e){System.out.println(e);}
                cpt++;
                p.setProgress(cpt);
            }
        }
        setCarteActive(0,0);
        //t = null;
        
        //Calcul des moyennes
        listeMoyennes = new double[getDataCarte(0,0).getTailleX()][getDataCarte(0,0).getTailleY()][prof.length][2];
        for(int j = 0;j<getNbDataCartesProf();j++){
            
            for(int y=0;y<getDataCarte(0,0).getTailleX();y++){
                for(int z=0;z<getDataCarte(0,0).getTailleY();z++){
                    double tmpX = 0d;
                    double tmpY = 0d;
                    for(int i=0;i<getNbDataCartesTps();i++){
                        tmpX+=getDataCarte(i,j).getC(y,z).getXNorm();
                        tmpY+=getDataCarte(i,j).getC(y,z).getYNorm();
                    }
                    listeMoyennes[y][z][j][0] = tmpX/(double)(getNbDataCartesTps());
                    listeMoyennes[y][z][j][1] = tmpY/(double)(getNbDataCartesTps());
                }
            }
            
        }
        p.close();
    }
    
    public double[] getMoyenne(int x, int y, int prof){
        double[] tmp = new double[2];
        tmp[0] = listeMoyennes[x][y][prof][0];
        tmp[1] = listeMoyennes[x][y][prof][1];
        return tmp;
    }
    
    public double[] getLonLat(int x, int y){
        int tX= getTailleX();
        int tY= getTailleY();
        double[] tmp = new double[2];
        double ttX = Math.abs(getTabX()[getTabX().length-1]-getTabX()[0]);
        double ttY = Math.abs(getTabY()[getTabY().length-1]-getTabY()[0]);
        double baryX = ((double)x*ttX)/(double)tX;
        double baryY = ((double)y*ttY)/(double)tY;
        baryX= Math.min(getTabX()[0],getTabX()[getTabX().length-1]) + baryX;
        baryY= Math.min(getTabY()[0],getTabY()[getTabY().length-1]) + baryY;
        tmp[0]=baryX;
        tmp[1]=baryY;
        return tmp;
    }
    
    public double[] getLonLat(double x, double y){
        int tX= getTailleX();
        int tY= getTailleY();
        double[] tmp = new double[2];
        double ttX = Math.abs(getTabX()[getTabX().length-1]-getTabX()[0]);
        double ttY = Math.abs(getTabY()[getTabY().length-1]-getTabY()[0]);
        double baryX = (x*ttX)/(double)tX;
        double baryY = (y*ttY)/(double)tY;
        baryX= Math.min(getTabX()[0],getTabX()[getTabX().length-1]) + baryX;
        baryY= Math.min(getTabY()[0],getTabY()[getTabY().length-1]) + baryY;
        tmp[0]=baryX;
        tmp[1]=baryY;
        return tmp;
    }
    
    public boolean isLonLat(){return (listeDimensions.size()>0);}
    public double[] getTabX(){return ((double[])listeDimensions.get(varX));}
    public double[] getTabY(){return ((double[])listeDimensions.get(varY));}
    public int[] getActive(){ return active;}
    
    public final DataCarte getDataCarte(int idTps, int idProf){ return (DataCarte)((ArrayList)(listeDataCartes.get(idTps))).get(idProf);}
    public DataCarte getDataCarte(int[] id){ return (DataCarte)((ArrayList)(listeDataCartes.get(id[0]))).get(id[1]);}
    
    public final int getNbDataCartesTps(){ return listeDataCartes.size();}
    public final int getNbDataCartesProf(){ return ((ArrayList)listeDataCartes.get(0)).size();}
    public DataCarte getCarteActive(){ return getDataCarte(active);}
    
    public final void setCarteActive(int idTps, int idProf){
        active[0]=idTps;
        active[1]=idProf;
        this.setOcean(getDataCarte(active).getOcean());
        this.setTailleX(getDataCarte(active).getTailleX());
        this.setTailleY(getDataCarte(active).getTailleY());
        this.setNormeMax(this.calculNormeMax());
        this.setNbCellValides(this.CalculNbCellValides());
        this.setCollBoucle(getDataCarte(active).getCollBoucle());
        this.setCollVortexAnt(getDataCarte(active).getVortexAnt());
        this.setCollVortexGeom(getDataCarte(active).getVortexGeom());
        this.setCollVortexPhys(getDataCarte(active).getVortexPhys());
        this.setCollVortexStream(getDataCarte(active).getVortexStreamlines());
        this.setNomFichier(getDataCarte(active).getNomFichier());
    }
    
}
