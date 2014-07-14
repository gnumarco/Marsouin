

package data;

import ucar.nc2.*;
import ucar.ma2.*;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataMap implements constants.centre, constants.courant {
    
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
    private CollLoop collectionBoucle=null;
    
    private int NbCellValides = 0;
    private int nbSurTerre = 0;
    /** conteneur des resultats finaux des fourmis */
    private CollVortexAnt collectionVortexAnt = null;
    /** conteneur des resultats des methodes geometriques */
    private CollVortexGeom collectionVortexGeom = null;
    /** conteneur des resultats des methodes de mecanique des fluides */
    private CollVortexPhys collectionVortexPhys = null;
    
    public CollVortexStreamlines collectionVortexStream = null;
    
    /** tableau de valeurs doubles que l'on pourra interpoler :
     *  pour afficher sur la carte une valeur en chaque point, on partira de ce tableau
     * et on interpolera pour obtenir un tableau o� figurent plus d'indices.
     */
    private TabloDouble2D table = null;
    
    public DataMap(DataMap d){
        collectionVortexAnt = d.getVortexAnt();
        collectionVortexGeom = d.getVortexGeom();
        collectionVortexPhys = d.getVortexPhys();
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
        nbSurTerre = d.getNbSurTerre();
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
            
            myFile=null;
            nomConfig = nFichier + ".cfg";
            nomFichier = nFichier;
            
            this.initCollections();
            this.initTabloDouble();
            NbCellValides = computeNbValidCells();
            
            NetcdfFile f = new NetcdfFile(nFichier);
            Variable vr = ((Variable)(f.getVariables().get(indC)));
            Array tps = vr.read();
            String stmp = null;
            String[] tmpTimes = null;
            String[] times = new String[tps.getShape()[0]];
            Index ind = tps.getIndex();
            tmpTimes = java.util.TimeZone.getAvailableIDs();
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
            
        }catch(Exception e){System.out.println("DataCarte : pb a la construction ");e.printStackTrace();}
    }
    
    public int getNbSurTerre(){return nbSurTerre;}
    
    public final void initCollections() {
        collectionBoucle=new CollLoop();
        collectionVortexAnt = new CollVortexAnt();
        collectionVortexGeom = new CollVortexGeom();
        collectionVortexPhys = new CollVortexPhys();
        collectionVortexStream = new CollVortexStreamlines();
    }
    
    /** initialise le tableau qui peut servir pour interpoler des donn�es */
    private void initTabloDouble() {
        table = new TabloDouble2D(tailleX,tailleY);
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
    
    public void dispose() {
        ocean =null;
        collectionBoucle.dispose();
        collectionBoucle=null;
        collectionVortexGeom.dispose();
        collectionVortexAnt.dispose();
        collectionVortexPhys.dispose();
        
    }
    
    private void majSurTerre()  {
        int cpt,posX,posY,i,j;
        
        for(posX=0; posX<tailleX; posX++)
            for(posY=0 ;posY<tailleY;posY++ )
                ocean[posX][posY].setSurTerre(false);
        
        for(posX=0; posX<tailleX; posX++)
            for(posY=0 ;posY<tailleY;posY++ ){
            try{
                if (this.ocean[posX][posY].norme <= SEUIL_NORME ) {
                    // le vecteur est nul : soit on a un centre de phenomene
                    //                      soit on est sur la terre
                    cpt=0;
                    for( i= posX-1; i <= (posX +1) ; i++)
                        for(j= posY -1 ;j <= posY +1 ; j++){
                        if (this.ocean[i][j].norme <= SEUIL_NORME )
                            // on compte les vecteurs nuls autour du point
                            cpt++;
                        }
                    if (cpt >= 3) {
                        // 3 points = le point en question + 2 autres
                        for(i= posX-1; i <= (posX +1) ; i++)
                            for(j= posY -1 ;j <= posY +1 ; j++)
                                this.ocean[i][j].setSurTerre(this.ocean[i][j].norme <= SEUIL_NORME);
                        // on peut estimer que c'est la terre en chaque point de courant nul
                    }
                }
            }catch(Exception e){this.ocean[posX][posY].setSurTerre(this.ocean[posX][posY].norme <= SEUIL_NORME);}
            }
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
    
    public CollLoop getCollLoop(){ return collectionBoucle;}
    
    public CollVortexAnt getVortexAnt(){ return collectionVortexAnt;}
    public CollVortexGeom getVortexGeom(){ return collectionVortexGeom;}
    public CollVortexPhys getVortexPhys(){ return collectionVortexPhys;}
    public CollVortexStreamlines getVortexStreamlines(){ return collectionVortexStream;}
    
    public void setOcean(Stream[][] ocean) {
        this.ocean = ocean;
    }
    
    public void setTailleX(int tX){tailleX = tX;}
    public void setTailleY(int tY){tailleY = tY;}
    public void setNormeMax(double n){normeMax = n;}
    public void setNbCellValides(int n){NbCellValides = n;}
    public void setCollVortexAnt(CollVortexAnt coll){collectionVortexAnt = coll;}
    public void setCollVortexGeom(CollVortexGeom coll){collectionVortexGeom = coll;}
    public void setCollVortexPhys(CollVortexPhys coll){collectionVortexPhys = coll;}
    public void setCollVortexStream(CollVortexStreamlines coll){collectionVortexStream = coll;}
    public void setCollBoucle(CollLoop coll){collectionBoucle = coll;}
    protected void setNomFichier(String n){nomFichier = n;}
    
    public boolean[][][] getMapGeomCentre() {
        // retourne une copie complete de la carte de booleens centre,
        //     flags resultants des methodes geometriques.
        boolean[][][] tab = new boolean[tailleX][][];
        boolean[] cg = null;
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
        // copie la carte de booleens centre,
        //     flags resultants des methodes geometriques.
        // dans CET objet
        boolean[] cg = null;
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
        // copie la carte de booleens centre,
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
    public TabloDouble2D getTable() {
        return table;
    }
    
  
    public void setTable(TabloDouble2D maTable) {
        this.table.dispose();
        this.table = null;
        this.table = maTable.cloneMe();
    }
    
    /** charge la table avec des donn�es de la carte.
     * @param TB2D_DATA est une constante de constantes.courant, commencant par TD2D
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
