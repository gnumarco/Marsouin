/*
 * CanvasCarte.java
 *
 * Created on 15 septembre 2002, 12:09
 */

/*
 * @author Segond, Mahler
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

/** Classe d'affichage du graphisme :
 * afficher les vecteurs, un fond, les informations resultats ,
 * animer les resultats pour afficher un maximum d'infos
 */
package visu;

import data.*;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.Point;

public class CanvasCarte extends CanGen implements constants.centre,constants.couleur,constants.fourmi,constants.courant,constants.physique, javax.swing.Scrollable {
    
    private final boolean dBugAff = true;
    private final boolean dBugAffPlus = false;
    private final boolean dBugDisplay = true;
    private ThreadAnime threadAnime = null;
    private Graphics2D myGraphics = null;
    private int nbPhaseAnime = 3;
    private Image imgFond =null;
    private String imgFileName="";
    private int vSeul = 0;
    
    public CanvasCarte( Memoire m, int moi) {
        mem = m;
        id = moi;
        
        // taille par defaut
        carteWidth = 600;
        carteHeight = 600;
        
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onMouseClicked(evt);
            }
        });
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(carteWidth,carteHeight);
    }
    
    public void setupSize(int x, int y) {
        
        int w = x;
        int h =y;
        int tX =mem.getDataCarte(id).getXSize();
        int tY =mem.getDataCarte(id).getYSize();
        int largeurCase = w/tX;
        int hauteurCase = h/tY;
        
        while ((largeurCase < MIN_LARGEUR_CASE)|(hauteurCase < MIN_HAUTEUR_CASE)) {
            w=(largeurCase < MIN_LARGEUR_CASE)?((w*11)/10):w;
            h=(hauteurCase < MIN_HAUTEUR_CASE)?((h*11)/10):h;
            largeurCase = w/tX;
            hauteurCase = h/tY;
        }
        
        carteWidth = largeurCase*tX;
        carteHeight = hauteurCase*tY;
        
        this.setSize(carteWidth,carteHeight);
    }
    
    /** M�thode permettant d'effectuer un zoom avant sur le canvas
     */
    public void zoomAvant() {
        carteWidth = (carteWidth*15)/13;
        carteHeight = (carteHeight*15)/13;
        this.setSize(carteWidth,carteHeight);
    }
    public void zoomArriere() {
        carteWidth = (carteWidth*13)/15;
        carteHeight = (carteHeight*13)/15;
        this.setSize(carteWidth,carteHeight);
    }
    
    public void setImageDeFond(String imgFN) throws Exception{
        if ((imgFN==null)||(imgFN=="")) {
            imgFileName = "";
            imgFond=null;
            throw new Exception(java.util.ResourceBundle.getBundle("ressources/canvas").getString("_ImageDeFond_non_initialis�e"));
        }
        try{
            imgFond = getToolkit().getImage(imgFN);
            imgFileName = imgFN;
        }catch(Exception e){imgFileName = "";
        throw e; }
        imgFond=null;
    }
    
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    
    public void processImage(){
        int w = carteWidth;
        int h = carteHeight;
        try{
        myImage = (BufferedImage)this.createImage(w,h);
        }catch(Exception e){System.out.println(java.util.ResourceBundle.getBundle("ressources/canvas").getString("CanvasCarte_:_ERREUR:_myImage=null"));}
        myGraphics = myImage.createGraphics();
        myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_SPEED);
        myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_SPEED);
        myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_DISABLE);
        
        if(mem.getAntialias(id)){
            // Enable antialiasing for shapes
            myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
            
            myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }else{
            myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
            
            myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        
        //************* FOND **************
        myGraphics.setColor(COLOR_OCEAN);
        myGraphics.clearRect(0,0,w,h);
        myGraphics.fillRect(0,0,w,h);
        if (mem.getFlagAffich(id)[AFF_FOND]!=AFF_RIEN)
            afficheDonneesEnFond(myGraphics);
        afficheTerre(myGraphics);
        
        //************* VECTEURS ***********
        afficheVecteurs(myGraphics);
        
        try{
            // ************* Analyse GEOMETRIQUE ***********
            if (mem.getUseMethod(id)[USE_METHOD_GEOMETRIQUE]) {
                
            }
            
            // ************* Streamlines ***********
            if (mem.getUseMethod(id)[USE_METHOD_STREAMLINES]) {
                afficheStream(myGraphics);
            }
            // *************** PHYSIQUE ****************
            if (mem.getUseMethod(id)[USE_METHOD_PHYSIQUE]) {
                
            }
            
            // ***************** FOURMIS *****************
            if (mem.getUseMethod(id)[USE_METHOD_FOURMI]) {
                
                this.afficheMeta(myGraphics);
                this.afficheCentreMeta(myGraphics);
                
            }
            
            //Affichage latitude/longitude
            afficheGrille(myGraphics);
            
        }catch(Exception e){System.out.println(e.getMessage());
        if (dBugDisplay) e.printStackTrace();
        }
    }
    
    // DESSIN DES BOUCLES ETC ...
    private void afficheBoucle(Graphics2D gra){
        DataMap mer = mem.getDataCarte(id);
        CollLoop collect = mer.getCollLoop();
        
        int tX =mer.getXSize();
        int tY =mer.getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        gra.setColor(COLOR_BOUCLE_STD);
        for (int i=0;i<collect.size();i++){
            this.dessineUneBoucle(gra,collect.getBoucle(i).getContour(),largeurCase,hauteurCase,tY);
        }
        
    }
    
    private void afficheStream(Graphics2D gra){
        DataMap mer = mem.getDataCarte(id);
        CollVortexStreamlines tab = mer.getVortexStreamlines();
        int X1,X2,Y1,Y2;
        Double temp;
        
        if(mer.collectionVortexStream != null){
            int tX =mer.getXSize();
            int tY =mer.getYSize();
            int ux = carteWidth/tX;
            int uy = carteHeight/tY;
            gra.setColor(COLOR_BOUCLE_STD);
            for(int i=0;i<tab.size();i++){
                for(int j=tab.getVortexStreamlines(i).getContour().length-1;j>0;j--){
                    temp = new Double(tab.getVortexStreamlines(i).getContour()[j].x);
                    X1 = (new Double(temp.doubleValue()*(double)ux+(double)ux/2d)).intValue();
                    temp = new Double(tab.getVortexStreamlines(i).getContour()[j].y);
                    Y1 = (new Double(((double)tY -1d -temp.doubleValue())*(double)uy+(double)uy/2d)).intValue();
                    temp = new Double(tab.getVortexStreamlines(i).getContour()[j-1].x);
                    X2 = (new Double(temp.doubleValue()*(double)ux+(double)ux/2d)).intValue();
                    temp = new Double(tab.getVortexStreamlines(i).getContour()[j-1].y);
                    Y2 = (new Double(((double)tY -1d -temp.doubleValue())*(double)uy+(double)uy/2d)).intValue();
                    afficheFleche(gra,X1,Y1,X2,Y2);
                }
                temp = new Double(tab.getVortexStreamlines(i).getContour()[0].x);
                X2 = (new Double(temp.doubleValue()*(double)ux+(double)ux/2d)).intValue();
                temp = new Double(tab.getVortexStreamlines(i).getContour()[0].y);
                Y2 = (new Double(((double)tY -1d -temp.doubleValue())*(double)uy+(double)uy/2d)).intValue();
                temp = new Double(tab.getVortexStreamlines(i).getContour()[tab.getVortexStreamlines(i).getContour().length-1].x);
                X1 = (new Double(temp.doubleValue()*(double)ux+(double)ux/2d)).intValue();
                temp = new Double(tab.getVortexStreamlines(i).getContour()[tab.getVortexStreamlines(i).getContour().length-1].y);
                Y1 = (new Double(((double)tY -1d -temp.doubleValue())*(double)uy+(double)uy/2d)).intValue();
                afficheFleche(gra,X1,Y1,X2,Y2);
            }
            
        }
    }
    
    private void afficheMeta(Graphics2D gra){
        DataMap mer = mem.getDataCarte(id);
        CollVortexAnt collect = mer.getVortexAnt();
        
        int tX =mer.getXSize();
        int tY =mer.getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        for (int i=0;i<collect.size();i++){
            if(collect.getMetaVortex(i).getAffiche()){
                gra.setColor(collect.getMetaVortex(i).getCouleur());
                this.dessineUneBoucle(gra,collect.getMetaVortex(i).getContour(),largeurCase,hauteurCase,tY);
            }
        }
    }
    
    private void dessineUneBoucle(Graphics2D gra,Point poly[], int ux,int uy,int ty) {
        int X1,Y1,Y2,X2,i;
        if(poly != null) {
            for(i = 0; i< poly.length-1;i++){
                X1 = poly[i].x*ux+ux/2;
                Y1 = (ty -1 -poly[i].y)*uy+uy/2;
                X2 = poly[i+1].x*ux+ux/2;
                Y2 = (ty -1 -poly[i+1].y)*uy+uy/2;
                afficheFleche(gra,X2,Y2,X1,Y1);
            }
            X1 = poly[0].x*ux+ux/2;
            Y1 = (ty -1 -poly[0].y)*uy+uy/2;
            X2 = poly[i].x*ux+ux/2;
            Y2 = (ty -1 -poly[i].y)*uy+uy/2;
            afficheFleche(gra,X2,Y2,X1,Y1);
        }
    }
    
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // DESSINER LE CENTRE DES BOUCLES ....
    
    private void afficheCentreMeta(Graphics2D gra){
        DataMap mer = mem.getDataCarte(id);
        CollVortexAnt collect = mer.getVortexAnt();
        VortexAnt b =null;
        int tX =mer.getXSize();
        int tY =mer.getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        float ux = (float) largeurCase;
        float uy = (float) hauteurCase;
        float offsetX = 2f*((float)largeurCase/5f);
        float offsetY = 2f*((float)hauteurCase/5f);
        int x,y;
        for (int i=0;i<collect.size();i++) {
            b = collect.getMetaVortex(i);
            if(b.getAffiche())
                if ((b!=null)&&(b.getBaricentre()!=null)) {
                x = (int)(((float)b.getBaricentre()[0])*ux +ux/2f -offsetX);
                y = (int)(((float)tY -1f -(float)b.getBaricentre()[1])*uy +uy/2f - offsetY);
                gra.setColor(COLOR_CENTRE_BOUCLE_STD);
                gra.fillOval(x,y,((int)(2f*offsetX)),((int)(2f*offsetY)));
                gra.setColor(COLOR_NUM_BOUCLE);
                gra.drawString(java.lang.Integer.toString(b.getNum()),x,y);
                }
        }
    }
    
    private void afficheCentreBoucle(Graphics2D gra){
        DataMap mer = mem.getDataCarte(id);
        CollLoop collect = mer.getCollLoop();
        Loop b =null;
        int tX =mer.getXSize();
        int tY =mer.getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        float ux = (float) largeurCase;
        float uy = (float) hauteurCase;
        float offsetX = 2f*((float)largeurCase/5f);
        float offsetY = 2f*((float)hauteurCase/5f);
        gra.setColor(COLOR_CENTRE_BOUCLE_STD);
        int x,y;
        for (int i=0;i<collect.size();i++) {
            b = collect.getBoucle(i);
            if ((b!=null)&&(b.getCentre()!=null)) {
                x = (int)(((float)b.getCentre()[0])*ux +ux/2f -offsetX);
                y = (int)(((float)tY -1f -(float)b.getCentre()[1])*uy +uy/2f - offsetY);
                gra.fillOval(x,y,((int)(2f*offsetX)),((int)(2f*offsetY)) );
            }
        }
    }
    //********************VortexPhys *************************************************************
    private void geomAfficheCentreVortexPhys(Graphics2D gra) throws Exception {
        
        DataMap mer = mem.getDataCarte(id);
        CollVortexPhys collect = mer.getVortexPhys();
        VortexPhys b =null;
        int tX =mer.getXSize();
        int tY =mer.getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        if ((largeurCase < 10)|(hauteurCase < 10)) {
            String errormsg = java.util.ResourceBundle.getBundle("ressources/canvas").getString("Fen�tre_trop_petite_pour_tout_afficher_:_agrandissez_la_fen�tre._");
            //mem.getFrmVisu(id).setStatusBar(errormsg,COLOR_ERROR);
            throw new Exception(errormsg);}
        
        Color[] palette = this.getPalettePhys();
        int nbInfos = 2*palette.length ;
        int decAngle  = 360/nbInfos;
        int x,y,cpt;
        
        for (int i=0;i<collect.size();i++) {
            b = collect.getVortexPhys(i);
            if ((b!=null)&&(b.getCentre()!=null)) {
                
                
                
                if (b.centreIsMin()) {
                    cpt = b.getTag() - TAG_MIN;
                    gra.setColor(palette[cpt]);
                } else if (b.centreIsMax()) {
                    cpt = palette.length + (b.getTag() - TAG_MAX);
                    gra.setColor(palette[b.getTag() - TAG_MAX]);
                } else {
                    cpt = b.getTag();
                    gra.setColor(Color.black);
                }
                
                x = ((b.getPointCentre().x)*largeurCase);
                y = ((tY -b.getPointCentre().y -1)*hauteurCase);
                gra.fillArc(x,y,largeurCase,hauteurCase, cpt*decAngle, decAngle);
            }
        }
        String msg = java.util.ResourceBundle.getBundle("ressources/canvas").getString("affichage_OK");
        //mem.getFrmVisu(id).setStatusBar(msg);
        
    }
    //****************************************************************************************************************************************
    
    public Color[] getPalettePhys() {
        return COLOR_CENTRE_PHYSIQUE;
    }
    //****************************************************************************************************************************************
    private void afficheVortexPhys(Graphics2D gra){
        DataMap mer = mem.getDataCarte(id);
        CollVortexPhys collect = mer.getVortexPhys();
        
        int tX =mer.getXSize();
        int tY =mer.getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        gra.setColor(COLOR_BOUCLE_STD);
        if (collect != null)
            for (int i=0;i<collect.size();i++){
            this.dessineUneBoucle(gra,collect.getVortexPhys(i).getContour(),largeurCase,hauteurCase,tY);
            }
        
    }
    
    //*****************************************************************************************
    private void afficheTabInt(Graphics2D gra, int[][] tab, int VALUE_TO_DISPLAY){
        DataMap mer = mem.getDataCarte(id);
        
        int tX =mer.getXSize();
        int tY =mer.getYSize();
        //verif
        if((tab!=null)&&((tX==tab.length)&&(tY==tab[0].length))) {
            
            int largeurCase = carteWidth/tX;
            int hauteurCase = carteHeight/tY;
            
            gra.setColor(COLOR_TAB_INT_STD);
            int i,j,x,y;
            for(i = 0 ; i<tX;i++)
                for(j = 0;j<tY;j++) {
                if ((!mer.getC(i,j).getSurTerre()) && (tab[i][j]==VALUE_TO_DISPLAY)) {
                    x = largeurCase*i;
                    y = (tY -j -1)*hauteurCase;
                    gra.fillRect(x,y,largeurCase,hauteurCase);
                }
                }
        }
    }
   
    
    
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //  AFFICHER LA PHEROMONE
    private void affichePhero(Graphics2D gra){
        DataMap mer = mem.getDataCarte(id);
        int tX =mer.getXSize();
        int tY =mer.getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        float offsetX = 2f*((float)largeurCase/5f);
        float offsetY = 2f*((float)hauteurCase/5f);
        double ph,phmax;
        gra.setColor(COLOR_PHEROMONE_STD);
        try {
            phmax =0.0;
            for(int i = 0 ; i<tX;i++)
                for(int j = 0;j<tY;j++)
                    if(mer.getC(i,j).hasPheromone()&&(phmax<mer.getC(i,j).getPheromoneTotale()))
                        phmax = mer.getC(i,j).getPheromoneTotale();
            if (phmax>0.001) {
                for(int i = 0 ; i<tX;i++)
                    for(int j = 0;j<tY;j++) {
                    if (mer.getC(i,j).hasPheromone()) {
                        ph=0.0;
                        ph = mer.getC(i,j).getPheromoneTotale();
                        ph = ph/phmax;
                        gra.fillOval(
                                ((i*largeurCase)+largeurCase/2 - (int)(ph*offsetX)),
                                ((tY -1 -j)*hauteurCase + hauteurCase/2 - (int)(ph*offsetY)),
                                (int)(2f*ph*offsetX),(int)(2f*ph*offsetY));
                    }
                    }
            }
            
        }catch(Exception e) {System.out.println("CanvasCarte AffichePhero error " + e);}
    }
    
   
    
    /** affiche chaque pixel avec une couleur proportionnelle @ la valeur de la table de datacarte.
     * table */
    private void afficheDonneesEnFond(Graphics2D gra) {
        Color col = null;
        double[][] t =null;
        if (AFFICHER_FOND_AVEC_INTERPOLATION_GRAND_TABLEAU)
            //table initiale
            t = (mem.getDataCarte(id).getTable()).getGrandTab();
        else
            // tableau interpol�
            t = (mem.getDataCarte(id).getTable()).getTablo();
        
        int X1,Y1,X2,Y2;
        
        int BORD ;
        if (AFFICHER_FOND_AVEC_INTERPOLATION_GRAND_TABLEAU)
            BORD = 0;
        else BORD = 0;
        
        int tX =t.length;
        int tY =t[0].length;
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        X2 = largeurCase -2*BORD;
        Y2 = hauteurCase -2*BORD;
        int i=0,j=0 ;
        
        float r,g,b,a;
        
        // indicateurs de terre
        int indX,indY;
        int indtX =mem.getDataCarte(id).getXSize();
        int indtY =mem.getDataCarte(id).getYSize();
        double scaleX = (double)indtX /(double)tX;
        double scaleY = (double)indtY/(double)tY;
        
        
        for(i=0;i<tX;i++) {
            indX = (int)((double)i*scaleX);
            for(j=0; j<tY;j++) {
                indY = (int)((double)j*scaleY);
                if (!mem.getDataCarte(id).getC(indX,indY).getSurTerre()) {
                    
                    Y1 = tY-j-1;
                    System.out.println(java.util.ResourceBundle.getBundle("ressources/canvas").getString("valeur=_")+(float)t[i][j]);
                    try{ // exception si on est en dehors des bornes du tableau
                        col = getPaletteFond((float)t[i][j]);
                    } catch (Exception e) {
                        col = new Color(0.2f,0.3f,0.4f,0.6f);e.printStackTrace();}
                    
                    gra.setColor(col);
                    X1 = largeurCase*i+ BORD ;
                    Y1 = hauteurCase*(Y1)+BORD ;
                    gra.fillRect(X1, Y1,X2,Y2);
                }
            }
        }
    }
    
    //@@@@@@@@@@@@@@@@@ LES FLECHES @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /** affiche les fleches qui representent le courant moyen en chaque point d'estimation de la carte */
    private void afficheVecteurs(Graphics2D gra) {
        
        int X1,Y1,X2,Y2;
        int tX =mem.getDataCarte(id).getXSize();
        int tY =mem.getDataCarte(id).getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        
        double offsetX = ((double)largeurCase)* COEFF_AFFICHAGE_COURANT;
        double offsetY = ((double)hauteurCase)* COEFF_AFFICHAGE_COURANT;
        
        
        gra.setColor(COLOR_VECTEUR);
        for(int i = 0 ; i<tX;i++)
            for(int j = 0;j<tY;j++){
            if (!mem.getDataCarte(id).getC(i,j).getSurTerre()) {
                X1 = (i*largeurCase+largeurCase/2);
                Y1 = ((tY -j -1)*hauteurCase+hauteurCase/2);
                X2 = X1+(int)(mem.getDataCarte(id).getC(i,j).getXNorm()*offsetX);
                Y2 = Y1-(int)(mem.getDataCarte(id).getC(i,j).getYNorm()*offsetY);
                afficheFleche(gra,X1,Y1,X2,Y2);
                //System.out.println("coords:"+mem.getDataCarte(id).getC(i,j).getXNorm());
            }
            }
    }
    
    
    /** renvoie la couleur associ�e � la valeur
     *  DEUX TYPES DE DEGRADES : si 0<val<1
     *                           si -1<val<0  */
    public Color getPaletteFond(float valeur) {
        int r=0,g=0,b=0,a=0;
        int indic = 0;
        
        switch (TYPE_DEGRADE) {
            case NOIR_BLANC_NOIR :
                // black 0 0 0  white 255 255 255 black
                if(valeur<0f) valeur = -valeur;
                r = (int)(254f*valeur); g=r; b=r; a=255;
                break;
            case BLEU_BLANC_NOIR :
                // blue : 0 0 255 white 255 255 255 black 0 0 0
                if (valeur> 0f) indic = 1; else indic =-1;
                if (indic<0) {
                    valeur  =  - valeur;
                    r = 255 - (int)(valeur*254f); g=r; b=255; a=255;} else {
                    r = 255 - (int)(valeur*254f); g=r; b=r; a=255;}
                break;
            case BLEU_BLANC_VERT :
                // blue : 0 0 255 white 255 255 255 green 0 255 0
                if (valeur> (float)-0.0000001) indic = 1; else indic =-1;
                if (indic<0) {
                    valeur  =  - valeur;
                    r = 255 - (int)(valeur*254f); g=r; b=255; a=255;} else {
                    r = 255 - (int)(valeur*254f); g=255; b=r; a=255;}
                break;
            case ROUGE_BLANC_BLEU :
                // red 255 0 0  white 255 255 255 blue : 0 0 255
                if (valeur> 0f) indic = 1; else indic =-1;
                if (indic<0) {
                    valeur  =  - valeur;
                    r = 255; g= 255 - (int)(valeur*254f); b=g; a=255;} else {
                    r = 255 - (int)(valeur*254f); g=r; b=255; a=255;}
                break;
            case BLEU_GRIS_NOIR :
                // blue : 0 0 255 grisclair 220 220 220 black 0 0 0
                if (valeur> 0f) indic = 1; else indic =-1;
                if (indic<0) {
                    valeur  =  - valeur;
                    r = 220 - (int)(valeur*219f); g=r; b=220 + (int)(valeur*34f); a=255;} else {
                    r = 220 - (int)(valeur*219f); g=r; b=r; a=255;}
                break;
                
            default : {}
        }
        
        return new Color(r,g,b,a);
    }
    
    //************************************************************************
    private void geomAfficheCombinerGC(Graphics2D gra) {
        
        int X1,Y1,X2,Y2;
        int tX =mem.getDataCarte(id).getXSize();
        int tY =mem.getDataCarte(id).getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        int offsetX = largeurCase/2;
        int offsetY = hauteurCase/2;
        
        gra.setColor(COLOR_COMBINER_GC);
        for(int i = 0 ; i<tX;i++){
            for(int j = 0;j<tY;j++){
                if (mem.getDataCarte(id).getC(i,j).getCfgCentre(COMBINER_GC)) {
                    X1 = (i*largeurCase);
                    Y1 = ((tY -j -1)*hauteurCase);
                    gra.fillOval(X1+offsetX/2,Y1+offsetY/2,offsetX,offsetY);
                }
            }
        }
    }
    
    //*********************************************************************************
    /** affiche la surface coti�re, renseign�e par le booleens getSurTerre() en chaque point */
    private void afficheTerre(Graphics2D gra) {
        
        int X1,Y1,X2,Y2;
        int tX =mem.getDataCarte(id).getXSize();
        int tY =mem.getDataCarte(id).getYSize();
        int largeurCase = carteWidth/tX;
        int hauteurCase = carteHeight/tY;
        gra.setColor(COLOR_TERRE);
        for(int i = 0 ; i<tX;i++)
            for(int j = 0;j<tY;j++){
            if (mem.getDataCarte(id).getC(i,j).getSurTerre()) {
                X1 = (i*largeurCase);
                Y1 = ((tY -j -1)*hauteurCase);
                gra.fillRect(X1,Y1,largeurCase,hauteurCase);
            }
            }
    }
    
    //*************************************************************************************************
    public void setSize(int param, int param1) {
        
        carteWidth = param;
        carteHeight = param1;
        if (mem.getFlagAffich(id)[AFF_FOND]!=AFF_RIEN) {
            // $$$$$$$$*******Utiliser une interpolation******�������������������������$$$$$$$$$$$$$$$$$$$$$$$$$$
            if (AFFICHER_FOND_AVEC_INTERPOLATION_GRAND_TABLEAU) {
                mem.getDataCarte(id).getTable().changeTaille(carteWidth/DENOMINATEUR_TAILLE_INTERPOLATION,carteHeight/DENOMINATEUR_TAILLE_INTERPOLATION);
                mem.getDataCarte(id).getTable().normaliserGrandTab();
            } else mem.getDataCarte(id).getTable().normaliserTab();
            
        }
        super.setSize(carteWidth,carteHeight);
        processImage();
    }
    public void setSize(Dimension dimension) {
        this.setSize(dimension.width,dimension.height);
    }
    
    //************************************************************************************************************
    public void onMouseClicked(java.awt.event.MouseEvent evt) {
        // ATTENTION
        int w = this.getWidth();  //carteWidth
        int h = this.getHeight(); //carteHeight
        // CLIC GAUCHE >>>>>>>>> MONTRER UN POINT : afficher 9 vecteurs en autre couleur
        if (!evt.isAltDown() & !evt.isControlDown() & !evt.isShiftDown() & !evt.isMetaDown()) {
            DataMap dc = mem.getDataCarte(id);
            int k,m;
            int X1,Y1,X2,Y2;
            int largeurCase = w/dc.getXSize();
            int hauteurCase = h/dc.getYSize();
            int i = (evt.getX())/largeurCase;
            int j = dc.getYSize() -1 -(evt.getY())/hauteurCase ;
            
            if (((i<dc.getXSize())&(j<dc.getYSize()))&((i>-1)&(j>-1))){
                // on est dans les bornes de la carte
                
                double offsetX = (largeurCase/OFFSET_V);
                double offsetY = (hauteurCase/OFFSET_V);
                boolean okPlus= false,okX=false ,okCarre = false;
                boolean[][] matrice;
                // recherche si plus :
                for(m = TYPE_PLUS;m <(NB_METHODES*NB_TYPES); m+=NB_TYPES)
                    okPlus = (okPlus | (dc.getC(i,j).getCfgCentre(m)));
                // recherche si X :
                for(m = TYPE_X;m <(NB_METHODES*NB_TYPES); m+=NB_TYPES)
                    okX = (okX | (dc.getC(i,j).getCfgCentre(m)));
                // recherche si O :
                for(m = TYPE_O;m <(NB_METHODES*NB_TYPES); m+=NB_TYPES) {
                    okPlus = (okPlus | (dc.getC(i,j).getCfgCentre(m)));
                    okX = (okX | (dc.getC(i,j).getCfgCentre(m)));
                }
                // recherche si CARRE :
                for(m = TYPE_CARRE;m <(NB_METHODES*NB_TYPES); m+=NB_TYPES)
                    okCarre = (okCarre | (dc.getC(i,j).getCfgCentre(m)));
                
                
                
                matrice = new boolean[3][3];
                for(k=0;k<3;k++)
                    for(m=0;m<3;m++) {
                    if(!((okPlus|okX)|okCarre))
                        matrice[k][m] = true;
                    else {
                        matrice[k][m] = false;
                        matrice[k][m] = (matrice[k][m])|((((m+k)%2)==0)& okX);
                        matrice[k][m] = (matrice[k][m])|((((m+k)%2)==1)& okPlus);
                        matrice[k][m] = (matrice[k][m])|(((m==1)&(k==1))& okPlus);
                        matrice[k][m] = (matrice[k][m])|(((m>0)&(k>0))& okCarre);
                    }
                    }
                if(!((okPlus|okX)|okCarre))
                    myGraphics.setColor(COLOR_VECTEUR_CLICK);
                else
                    myGraphics.setColor(COLOR_C_CLICK);
                for(k=-1;k<2;k++)
                    for(m=-1;m<2;m++) {
                    // prend en charge le debordement !
                    try{
                        if (!matrice[m+1][k+1]) throw new Exception(java.util.ResourceBundle.getBundle("ressources/canvas").getString("_affichage_non_demamd�_"));
                        //if((m|k)==0) throw new Exception("on n affiche pas le vecteur du centre !");
                        X1 = ((i+m)*largeurCase+largeurCase/2);
                        Y1 = ((dc.getYSize() -(j+k) -1)*hauteurCase+hauteurCase/2);
                        X2 = X1+(int)(dc.getC(i+m,j+k).getXBase()*offsetX);
                        Y2 = Y1-(int)(dc.getC(i+m,j+k).getYBase()*offsetY);
                        afficheFleche(myGraphics,X1,Y1,X2,Y2);
                    }catch (Exception e){}
                    }
                
            }
            this.getGraphics().drawImage(myImage,0,0,w,h,this);
        }
        // gauche + ctrl >>>>> POINTER
        else if (!evt.isAltDown() & evt.isControlDown() & !evt.isShiftDown() & !evt.isMetaDown()) {
            myGraphics.setColor(COLOR_C_CLICK);
            myGraphics.fillRect( evt.getX()-3, evt.getY()-3, 6, 6);
            this.getGraphics().drawImage(myImage,0,0,w,h,this);
        }
        // milieu >> RAFRAICHIR
        else if (evt.isAltDown() & !evt.isControlDown() & !evt.isShiftDown() & !evt.isMetaDown()) {
            System.out.println(java.util.ResourceBundle.getBundle("ressources/canvas").getString("bouton_du_milieu"));
        }
        // milieu + ctrl >> animation
        else if (evt.isAltDown() & evt.isControlDown() & !evt.isShiftDown() & !evt.isMetaDown()) {
            
        }
        // droit >>>>> Propri�t�s
        else if (!evt.isAltDown() & !evt.isControlDown() & !evt.isShiftDown() & evt.isMetaDown()) {
            DataMap dc = ((BatchDataMap)mem.getDataCarte(id)).getCarteActive();
            int largeurCase = w/dc.getXSize();
            int hauteurCase = h/dc.getYSize();
            int i = (evt.getX())/largeurCase;
            int j = dc.getYSize() -1 -(evt.getY())/hauteurCase ;
            vSeul = 0;
            CollVortexAnt collect = dc.getVortexAnt();
            if((collect != null) && (collect.size()>0)){
                while((vSeul<collect.size()) && (!collect.getMetaVortex(vSeul).contains(i,j)))
                    vSeul++;
                if(vSeul<collect.size()){
                    javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
                    javax.swing.JMenuItem menuProp = new javax.swing.JMenuItem(java.util.ResourceBundle.getBundle("ressources/canvas").getString("Proprietes_vortex_num_")+collect.getMetaVortex(vSeul).getNum());
                    javax.swing.JMenuItem menuAffSeul = new javax.swing.JMenuItem(java.util.ResourceBundle.getBundle("ressources/canvas").getString("Afficher_seul"));
                    javax.swing.JMenuItem menuAffTout = new javax.swing.JMenuItem(java.util.ResourceBundle.getBundle("ressources/canvas").getString("Afficher_tout"));
                    javax.swing.JMenuItem menuChgColor = new javax.swing.JMenuItem(java.util.ResourceBundle.getBundle("ressources/canvas").getString("Changer_la_couleur"));
                    menuChgColor.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            afficherChgColor(vSeul);
                        }
                    });
                    menuProp.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            afficherProp(vSeul);
                        }
                    });
                    menuAffSeul.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            afficherSeul();
                        }
                    });
                    menuAffTout.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            afficherTout();
                        }
                    });
                    popup.add(menuProp);
                    popup.add(new javax.swing.JPopupMenu.Separator());
                    popup.add(menuAffSeul);
                    popup.add(menuAffTout);
                    popup.add(menuChgColor);
                    popup.show(evt.getComponent(),evt.getX(),evt.getY());
                }
            }
        }
        
    }
    
    public void afficherSeul(){
        BatchDataMap d;
        d = mem.getBatchDataCarte(id);
        for(int i=0;i<d.getNbDataCartesTps();i++){
            for(int j=0;j<d.getDataCarte(i,0).getVortexAnt().size();j++){
                if(d.getDataCarte(i,0).getVortexAnt().getMetaVortex(j).getAffiche()){
                    d.getDataCarte(i,0).getVortexAnt().getMetaVortex(j).setAfficheSuiv(false);
                }
            }
        }
        d = mem.getBatchDataCarte(id);
        
        d.getVortexAnt().getMetaVortex(vSeul).setAfficheSuiv(true);
        d.getVortexAnt().getMetaVortex(vSeul).setAffichePrec(true);
        
        repaint();
    }
    
    public void afficherTout(){
        BatchDataMap d;
        d = mem.getBatchDataCarte(id);
        for(int i=0;i<d.getNbDataCartesTps();i++){
            for(int j=0;j<d.getDataCarte(i,0).getVortexAnt().size();j++){
                if(!d.getDataCarte(i,0).getVortexAnt().getMetaVortex(j).getAffiche()){
                    d.getDataCarte(i,0).getVortexAnt().getMetaVortex(j).setAfficheSuiv(true);
                }
            }
        }
        repaint();
    }
    
    void afficherChgColor(int vSeul){
        BatchDataMap d = mem.getBatchDataCarte(id);
        CustomColorChooser cc = new CustomColorChooser();
        cc.setVisible(true);
        if(cc.ok){
            d.getVortexAnt().getMetaVortex(vSeul).setCouleurPrec(cc.getCouleur());
            d.getVortexAnt().getMetaVortex(vSeul).setCouleurSuiv(cc.getCouleur());
            this.repaint();
        }
    }
    
    public void afficherProp(int num){
        DataMap d = mem.getBatchDataCarte(id).getCarteActive();
        ProprietesVortex p = new ProprietesVortex(d.getVortexAnt().getMetaVortex(vSeul).getNum());
        p.setUnit(d.getUnit());
        p.setAire(d.getVortexAnt().getMetaVortex(vSeul).getAire());
        p.setRayonMoyen(d.getVortexAnt().getMetaVortex(vSeul).getRayonMoyen());
        p.setPeriode(d.getVortexAnt().getMetaVortex(vSeul).getPeriod(mem.getDataCarte(id).getOcean()));
        
        double[] tmp = mem.getBatchDataCarte(id).getLonLat(d.getVortexAnt().getMetaVortex(vSeul).getBaricentre()[0], d.getVortexAnt().getMetaVortex(vSeul).getBaricentre()[1]);
        p.setCentre(tmp[0],tmp[1]);
        p.setVisible(true);
    }
    
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(carteWidth,carteHeight);
    }
    
    public int getScrollableBlockIncrement(java.awt.Rectangle rectangle, int param, int param2) {
        return 50;
    }
    
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
    
    public int getScrollableUnitIncrement(java.awt.Rectangle rectangle, int param, int param2) {
        return 8;
    }
    
    public java.awt.Graphics getGraphics() {
        java.awt.Graphics retValue;
        retValue = super.getGraphics();
        //retValue = (Graphics)myGraphics;
        return retValue;
    }
    
    public int[] getCVprops() {
        int[] ret = new int[LENGTH_CV_PROPS];
        
        DataMap mer = mem.getDataCarte(id);
        int tX =mer.getXSize();
        ret[CV_TAILLE_Y] =mer.getYSize();
        ret[CV_CASE_WIDTH] = carteWidth/tX;
        ret[CV_CASE_HEIGHT] = carteHeight/ret[CV_TAILLE_Y];
        
        return ret;
    }
    public int[] getDimension() {
        int[] ret = {carteWidth,carteHeight};
        return ret;
        
    }
    
}
