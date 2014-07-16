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

import java.util.ArrayList;
import ucar.nc2.*;
import ucar.ma2.*;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataMap implements com.marsouin.constants.Centre, com.marsouin.constants.Stream {
    
    /** booleens qui activent des println() pour un debuggage -manuel- avec "pelle et sceau" */
    private String maDate;
    private final boolean dBug = true;
    private final boolean dBugPlus = true;
    protected int profondeur = 0;
    protected int[] prof = null;
    protected int[] times = null;
    /** les donnees pricipales : un tableau 2D de vecteurs courants cf data.Stream */
    private Stream[][] ocean;
    /** taille du tableau */
    private int tailleX,tailleY;
    
    public int avancement = 0;
    public int avMax = 0;
    
    private double normeMax;
    /** concerne lengthfichier de carte */
    private String nomFichier,nomConfig,nomResultat, unit;
    
    /** conteneur des r�sultats des fourmis */
    private ArrayList<Loop> collectionBoucle=null;
    
    private int NbCellValides = 0;
    private int nbSurTerre = 0;
    /** conteneur des resultats finaux des fourmis */
    private ArrayList<VortexAnt> collectionVortexAnt = null;
    
    public ArrayList<VortexStreamlines> collectionVortexStream = null;
    
    /** tableau de valeurs doubles que l'on pourra interpoler :
     *  pour afficher sur la carte une valeur en chaque point, on partira de ce tableau
     * et on interpolera pour obtenir un tableau o� figurent plus d'indices.
     */
    private ArrayDouble2D table = null;
    
    public DataMap(DataMap d){
        collectionVortexAnt = d.getVortexAnt();
        collectionVortexStream = d.getVortexStreamlines();
        NbCellValides = d.getNbValidCells();
        collectionBoucle = d.getCollLoop();
        nomResultat = d.getResultName();
        nomFichier = d.getFileName();
        nomConfig = d.getConfigName();
        normeMax = d.getMaxNorm();
        tailleX = d.getXSize();
        tailleY = d.getYSize();
        ocean = d.getOcean();
        table = d.getTable();
        maDate = d.getDate();
        nbSurTerre = d.getNbOnGround();
    }
    
    public DataMap(Stream[][] mer,String nFichier,String nConfig) {
        this(mer,nFichier);
        
        nomConfig = nConfig;
        this.restoreConfig();
        
    }
    
    public DataMap(Stream[][] mer,String nFichier){
        ocean = mer;
        tailleX= mer.length;
        tailleY=mer[0].length;
        normeMax = computeMaxNorm();
        nomFichier = nFichier;
        nomConfig = nFichier + ".cfg";
        
        initCollections();
        initTabloDouble();
        NbCellValides = computeNbValidCells();
    }
    
    public DataMap(String nFichier, int z, int indZ, int c, int indC, int u, int indU, int v, int indV, String MVAtt, boolean noMV) {
        try{
            
            OceanFile myFile = new OceanFile(nFichier,  z, indZ, c, indC, u, indU, v, indV, MVAtt, noMV);
            ocean=myFile.lire();
            unit=myFile.getUnit();
            tailleX= ocean.length;
            tailleY=ocean[0].length;
            nbSurTerre = myFile.getNbSurTerre();
            normeMax = this.computeMaxNorm();
            
            nomConfig = nFichier + ".cfg";
            nomFichier = nFichier;
            
            this.initCollections();
            this.initTabloDouble();
            NbCellValides = computeNbValidCells();
            
            NetcdfFile f = NetcdfFile.open(nFichier);
            Variable vr = ((Variable)(f.getVariables().get(indC)));
            Array tps = vr.read();
            Index ind = tps.getIndex();
            ind.set(c);
            double t = tps.getDouble(ind);
            f.close();
            long temp = (long)t;
            GregorianCalendar cal = new GregorianCalendar(java.util.Locale.FRENCH);
            cal.set(0000,java.util.Calendar.JANUARY,1,0,0,0);
            Date ref = cal.getTime();
            temp = temp * 1000;
            temp = temp + ref.getTime();
            Date cur = new Date(temp);
            maDate = cur.toString();       
            
        }catch(Exception e){System.out.println("DataCarte : problem building the DataMap");
        e.toString();}
    }
    
    public int getNbOnGround(){return nbSurTerre;}
    
    public final void initCollections() {
        collectionBoucle=new ArrayList<>();
        collectionVortexAnt = new ArrayList<>();
        collectionVortexStream = new ArrayList<>();
    }
    
    /** initialise le tableau qui peut servir pour interpoler des donn�es */
    private void initTabloDouble() {
        table = new ArrayDouble2D(tailleX,tailleY);
    }
    
    public final double computeMaxNorm(){
        double nMax=0.0;
        int r,t;
        for(r=0;r<tailleX;r++){
            for(t=0;t<tailleY;t++) {
                this.ocean[r][t].calculNorme();
                nMax = java.lang.Math.max(nMax,ocean[r][t].norme);
            }
        }
        return nMax;
    }
    
    
    public Stream[][] getOcean() {
        return ocean;
    }
    
    public final int getXSize(){ return tailleX;}
    public final int getYSize(){return tailleY;}
    public final double getMaxNorm(){ return normeMax;}
    public final String getConfigName(){return nomConfig;}
    public final String getFileName(){return nomFichier;}
    public final String getResultName(){return nomResultat;}
    public final String getDate(){return maDate;}
    public final Stream getC(int i, int j){ return ocean[i][j];}
    
    public ArrayList<Loop> getCollLoop(){ return collectionBoucle;}
    public void resetCollLoop(){ collectionBoucle = new ArrayList<>();}
    
    public ArrayList<VortexAnt> getVortexAnt(){ return collectionVortexAnt;}
    public ArrayList<VortexStreamlines> getVortexStreamlines(){ return collectionVortexStream;}
    public void resetVortexStreamlines(){ collectionVortexStream = new ArrayList<>();}
    
    public void setOcean(Stream[][] ocean) {
        this.ocean = ocean;
    }
    
    public void setTailleX(int tX){tailleX = tX;}
    public void setTailleY(int tY){tailleY = tY;}
    public void setNormeMax(double n){normeMax = n;}
    public void setNbCellValides(int n){NbCellValides = n;}
    public void setCollVortexAnt(ArrayList<VortexAnt> coll){collectionVortexAnt = coll;}
    public void setCollVortexStream(ArrayList<VortexStreamlines> coll){collectionVortexStream = coll;}
    public void setCollBoucle(ArrayList<Loop> coll){collectionBoucle = coll;}
    protected void setNomFichier(String n){nomFichier = n;}
    
    public boolean[][][] getMapGeomCentre() {
        // retourne une copie complete de la carte de booleens Centre,
        //     flags resultants des methodes geometriques.
        boolean[][][] tab = new boolean[tailleX][][];
        boolean[] cg;
        int x,y,id;
        for(x=0;x<tailleX; x++) {
            tab[x]=new boolean[tailleY][];
            for(y=0;y<tailleY;y++) {
                cg = ocean[x][y].getCfgCentre();
                tab[x][y]=new boolean[cg.length];
                for(id = 0;id<cg.length;id++)
                    tab[x][y][id] = cg[id];
            }
        }
        return tab;
    }
    
    public boolean[][] getMapIsVortex() {
        // retourne une copie complete de la carte de booleen isVortex,
        //     flag resultat des fourmis.
        boolean[][] tab = new boolean[tailleX][];
        boolean iv ;
        int x,y;
        for(x=0;x<tailleX; x++) {
            tab[x]=new boolean[tailleY];
            for(y=0;y<tailleY;y++) {
                iv = ocean[x][y].isVortex();
                tab[x][y] = iv;
            }
        }
        return tab;
    }
    
    public void setCarteGeomCentre(boolean[][][] tab) {
        // copie la carte de booleens Centre,
        //     flags resultants des methodes geometriques.
        // dans CET objet
        int x,y,id;
        for(x=0;x<tailleX; x++) {
            for(y=0;y<tailleY;y++) {
                ocean[x][y].resetCfgCentre();
                for(id = 0;id<tab[x][y].length;id++)
                    ocean[x][y].setCfgCentre(id,tab[x][y][id]);
            }
        }
    }
    
    
    public void setCarteIsVortex(boolean[][] tab) {
        // copie la carte de booleens Centre,
        //     flags resultants des methodes geometriques.
        // dans CET objet
        int x,y;
        for(x=0;x<tailleX; x++) {
            for(y=0;y<tailleY;y++) {
                ocean[x][y].setVortex(tab[x][y]);
            }
        }
    }
    
    public boolean isCorrect(int x,int y) {
        // verifier que les coordonn�es sont sur la carte !
        boolean ret = true;
        ret = ret & ((x>=0) & (y>=0));
        ret = ret & ((x<tailleX) & (y<tailleY));
        try {
            if (ret)
                this.getC(x,y);
        }catch (Exception e) { System.out.print(" Courant non initiali� => incorrect !"); ret = false;}
        return ret;
    }
    
    public boolean isBorderLine(int x, int y) {
        // verifier que les coordonn�es sont sur LE BORD DE LA CARTE !
        boolean ret = false;
        ret = (ret | ((x==0) | (y==0)) );
        ret = (ret | ((x==tailleX) | (y==tailleY)) );
        return ret;
    }
    
    protected final int computeNbValidCells(){
        int cpt=0;
        for(int i=0;i<tailleX;i++)
            for(int j=0;j<tailleY;j++)
                if(!ocean[i][j].getSurTerre())
                    cpt++;
        return cpt;
    }
    
    public int getNbValidCells(){
        return NbCellValides;
    }
    
    private void restoreConfig() {}
    
    /** indique si les coordonn�es sont sur LE BORD DE LA CARTE */
    public boolean isBorderMap(int x, int y) {
        boolean ret = false;
        ret = (ret | ((x==0) | (y==0)) );
        ret = (ret | ((x==tailleX) | (y==tailleY)) );
        return ret;
    }
    
    /** indique si les coordonn�es sont sur la zone cotiere
     * <i> id est </i>si le point est � <i>un pas de grille </i>de distance de la terre */
    public boolean isNearCoast(int x, int y) {
        boolean ret=false, ok = isCorrect(x,y) && !isBorderMap(x,y);
        int i,j;
        if (ok) {
            for (i=-1;i<2;i++) {
                for(j=-1;j<2;j++) {
                    if (((i!=0)|(j!=0))& (this.isCorrect(x+i, y+j)))
                        ret = ret | this.getC(x+i,y+j).getSurTerre();
                }
            }
        }
        return ret;
    }
    
    /** Getter for property table : tabloDouble2D.
     * @return Value of property table.
     *
     */
    public ArrayDouble2D getTable() {
        return table;
    }
    
  
    public void setTable(ArrayDouble2D maTable) {
        this.table = maTable.cloneMe();
    }
    
    /** charge la table avec des donn�es de la carte.
     * @param TB2D_DATA est une constante de constantes.Stream, commencant par TD2D
     *
     */
    public void setTable(int TB2D_DATA) {
        
        int i,j;
        for(i=0;i<tailleX;i++)
            for(j=0;j<tailleY;j++) {
            if (!getC(i,j).getSurTerre()) {
                switch (TB2D_DATA) {
                    case TB2D_EST_OUEST :
                        
                        table.setTablo(i,j,100.0*this.getC(i,j).getXBase());
                        break;
                        
                    case TB2D_NORD_SUD :
                        
                        table.setTablo(i,j,100.0*this.getC(i,j).getYBase());
                        break;
                        
                    case TB2D_NORME :
                        
                        table.setTablo(i,j,100.0*this.getC(i,j).norme);
                        break;
                        
                    default : {}
                    
                }
            } else table.setTablo(i,j,0.0);
            }
        
        switch (TB2D_DATA) {
            case TB2D_EST_OUEST :
                table.normaliserEnConservantLeSigne();
                break;
                
            case TB2D_NORD_SUD :
                table.normaliserEnConservantLeSigne();
                break;
                
            case TB2D_NORME :
                table.normaliserTab();
                break;
            default : {}
            
        }
    }
    
    public String getUnit(){return unit;}
    
}
