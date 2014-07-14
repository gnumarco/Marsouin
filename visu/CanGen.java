/*
 * CanGen.java
 *
 * Created on 4 mars 2006, 19:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package visu;

import java.awt.image.*;
import data.*;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.awt.Point;

/**
 *
 * @author marco
 */
public class CanGen extends javax.swing.JPanel implements constants.centre, constants.couleur, constants.fourmi, constants.courant, constants.physique{
    
    protected BufferedImage myImage = null;
    protected int id;
    protected Memoire mem;
    protected int carteWidth = 1600;
    protected int carteHeight = 1200;
    protected int zoomLatLon=1;
    
    /** Creates a new instance of CanGen */
    public CanGen() {
    }
    
    public synchronized java.awt.Image getImage() {
        return myImage;
    }
    
    // dessine ce composant :
    public void paintComponent(Graphics g) {
        g.drawImage(myImage,0,0,carteWidth,carteHeight,this);
    }
    
    protected void afficheGrille(Graphics2D gra){
        
        if(mem.getBatchDataCarte(id).isLonLat()){
            
            try{
                BatchDataCarte mer = mem.getBatchDataCarte(id);
                double[] tmpTab = mer.getTabX();
                int tX =mem.getDataCarte(id).getTailleX();
                int tY =mem.getDataCarte(id).getTailleY();
                int largeurCase = carteWidth/tX;
                int hauteurCase = carteHeight/tY;
                double offsetX = ((largeurCase/OFFSET_V) * COEFF_AFFICHAGE_COURANT);
                double offsetY = ((hauteurCase/OFFSET_V) * COEFF_AFFICHAGE_COURANT);
                for(int i=0;i<tmpTab.length;i+=zoomLatLon){
                    //System.out.println(tmpTab[i]);
                    int X1,Y1,X2,Y2;
                    gra.setColor(COLOR_VECTEUR);
                    
                    X1 = (i*largeurCase+largeurCase/2);
                    Y1 = 0;
                    X2 = X1;
                    Y2 = tY*hauteurCase;
                    gra.drawLine(X1, Y1, X2, Y2);
                    gra.drawString(String.valueOf(tmpTab[i]).substring(0,5)+"°",X2,Y2);
                    gra.drawString(String.valueOf(tmpTab[i]).substring(0,5)+"°",X1,hauteurCase);
                }
                tmpTab = mer.getTabY();
                for(int i=0;i<tmpTab.length;i+=zoomLatLon){
                    //System.out.println(tmpTab[i]);
                    int X1,Y1,X2,Y2;
                    gra.setColor(COLOR_VECTEUR);
                    
                    X1 = 0;
                    Y1 = ((tY -i -1)*hauteurCase+hauteurCase/2);;
                    X2 = tX*largeurCase;
                    Y2 = Y1;
                    gra.drawLine(X1, Y1, X2, Y2);
                    gra.drawString(String.valueOf(tmpTab[i]).substring(0,5)+"°",X1,Y1);
                    gra.drawString(String.valueOf(tmpTab[i]).substring(0,5)+"°",X2-largeurCase*3,Y2);
                }
            }catch(Exception e){}
        } else{
            mem.getFrmVisu(id).enableGrid(false);
        }
    }
    
    protected double simplifiAngle(double a) {
        while(a >= (Math.PI * 2.0)) a -= Math.PI * 2.0;
        while(a < 0.0) a += Math.PI * 2.0;
        return a;
    }
    
    protected void afficheFleche(Graphics2D g,int x1, int y1, int x2, int y2) {
        int DifX = x1 - x2;
        int DifY = y1 - y2;
        
        g.drawLine(x1,y1,x2,y2);
        
        double Hyp = Math.sqrt(DifX * DifX + DifY * DifY);
        double Angle = Math.acos(DifX / Hyp) * ((y1 > y2) ? -1 : 1);
        double anc = Angle;
        
        int []PolyX, PolyY;
        PolyX = new int[3];
        PolyY = new int[3];
        
        PolyX[0] = x2;
        PolyY[0] = y2;
        
        Angle = simplifiAngle(Angle - (0.85 * Math.PI));
        PolyX[1] = (int)(x2 - Math.cos(Angle) * 5.0);
        PolyY[1] = (int)(y2 + Math.sin(Angle) * 5.0);
        
        Angle = simplifiAngle(anc + (0.85 * Math.PI));
        PolyX[2] = (int)(x2 - Math.cos(Angle) * 5.0);
        PolyY[2] = (int)(y2 + Math.sin(Angle) * 5.0);
        
        g.fillPolygon(PolyX, PolyY, 3);
    }
    
    public void setZoomLatLon(int z){
        zoomLatLon = z;
        this.repaint();
    }
    
    public int getZoomLatLon(){
        return this.zoomLatLon;
    }
}


