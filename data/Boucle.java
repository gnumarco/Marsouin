/*
 * Boucle.java
 *
 * Created on 20 november 2002, 17:26
 */

/*
 * @author Segond, Mahler
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package data;

import java.awt.*;
import java.awt.Polygon;
import java.util.ArrayList;


public class Boucle extends Polygon implements constants.courant, constants.couleur {
    
    private ArrayList suivant = null;
    private ArrayList precedent = null;
    
    private Color Couleur = COLOR_BOUCLE_STD;
    
    private int num = -1;
    
    /** utile pour indiquer quelle fourmis ou equipe a trouve la boucle ! */
    protected int tag;
    
    /** coordonnees du centre */
    protected double[] centre = null;
    
    protected double aire;
    
    /**     the counterclockwise boolean     */
    protected boolean aiguilleMontre;
    
    protected boolean affiche = true;
    
    public Boucle() {
        super();
        tag = 0;
        centre =null;
        aire = -1;
        aiguilleMontre=false;
        suivant = new ArrayList();
        precedent = new ArrayList();
        majSens();
    }
    
    public Boucle(int tag) {
        super();
        this.tag = tag;
        centre =null;
        aire = -1;
        aiguilleMontre=false;
        suivant = new ArrayList();
        precedent = new ArrayList();
        majSens();
    }
    
    public Boucle(int[] xpts, int[] ypts, int npts,int tagg) {
        super(xpts,ypts,npts);
        
        tag = tagg;
        if ( ((xpts!=null)&&(xpts.length!=0))
        && ((ypts!=null)&&(ypts.length!=0)) ) {
            this.calculerCentre();
        } else {
            aire = -1;
            centre=null;
        }
        aiguilleMontre = false;
        precedent = new ArrayList();
        suivant = new ArrayList();
        majSens();
    }
    
    public Boucle(int[] xpts, int[] ypts, int npts,int tagg, boolean s) {
        super(xpts,ypts,npts);
        
        tag = tagg;
        if ( ((xpts!=null)&&(xpts.length!=0))
        && ((ypts!=null)&&(ypts.length!=0)) ) {
            this.calculerCentre();
        } else {
            aire = -1;
            centre=null;
        }
        aiguilleMontre = s;
        precedent = new ArrayList();
        suivant = new ArrayList();
        majSens();
    }
    
    public Boucle(Boucle b) {
        super(((int[])b.xpoints.clone()),((int[])b.ypoints.clone()),b.npoints);
        if ( ((xpoints!=null)&&(xpoints.length!=0))
        && ((ypoints!=null)&&(ypoints.length!=0)) ) {
            this.calculerCentre();
        } else {
            aire = -1;
            centre=null;
        }
        tag = b.getTag();
        aiguilleMontre=b.getSens();
        precedent = b.getPrecedent();
        suivant = b.getSuivant();
        affiche = b.getAffiche();
        majSens();
    }
    
    public Point[] getContour() {
        Point[] ret = null;
        if (npoints>0) {
            ret = new Point[npoints];
            for(int i=0; i<npoints; i++)
                ret[i] = new Point(xpoints[i],ypoints[i]);
        }
        return ret;
    }
    
    public boolean getAffiche(){return affiche;}
    
    public double CalculAngle(int x, int y){
        double theta=0.0;
        double costheta = 0.0;
        double sintheta = 0.0;
        double hyp = java.lang.Math.sqrt(x*x+y*y);
        
        if (hyp != 0.0){
            if(x>0 && y>0)
                theta = java.lang.Math.acos(x/hyp);
            if(x<0 && y>0)
                theta = java.lang.Math.PI - (java.lang.Math.acos(java.lang.Math.abs(x)/hyp));
            if(x<0 && y<0)
                theta = java.lang.Math.PI + (java.lang.Math.acos(java.lang.Math.abs(x)/hyp));
            if(x>0 && y<0)
                theta = (2*java.lang.Math.PI) - (java.lang.Math.acos(x/hyp));
            
            if(x==0 && y>0)
                theta = java.lang.Math.PI/2;
            if(x==0 && y<0)
                theta = (3*java.lang.Math.PI)/2;
            if(x>0 && y==0)
                theta = 0.0;
            if(x<0 && y==0)
                theta = java.lang.Math.PI;
        }else
            theta = 0.0;
        return theta;
    }
    
    public void setCouleurPrec(Color c){
        Couleur = c;
        for(int i=0;i<precedent.size();i++)
            getPrecedent(i).setCouleurPrec(c);
    }
    
    public void setCouleurSuiv(Color c){
        Couleur = c;
        for(int i=0;i<suivant.size();i++)
            getSuivant(i).setCouleurSuiv(c);
    }
    
    public Color getCouleur(){
        return Couleur;
    }
    
    public final void majSens(){
        int x1, y1, x2, y2;
        double sommeAngles = 0d;
        for(int i=1; i<npoints-1; i++){
            x1 = xpoints[i]-xpoints[i-1];
            y1 = ypoints[i]-ypoints[i-1];
            x2 = xpoints[i+1]-xpoints[i];
            y2 = ypoints[i+1]-ypoints[i];
            sommeAngles += ((x1*y2)-(x2*y1));
        }
        sommeAngles = sommeAngles/(npoints);
        if(sommeAngles>0d){
            aiguilleMontre = true;
            Couleur = COLOR_BOUCLE_STD_2;
        }
        else
            aiguilleMontre = false;
    }
    
    public boolean getSens(){
        
        return aiguilleMontre;
    }
    
    public int getTag() {return tag;  }
    
    private void calculerCentre() {
        this.calculerCentreSimple();
    }
    
    private void calculerCentreSimple() {
        long sumX=0, sumY=0;
        double[] ret = new double[2];
        int i;
        for( i=0; i<npoints; i++) {
            sumX += xpoints[i];
            sumY += ypoints[i];
        }
        ret[0] = ((double)sumX)/((double)npoints);
        ret[1] = ((double)sumY)/((double)npoints);
        centre = ret;
    }
    
    private void calculerCentreMasse() {
        //              __n_
        //         1   \       2                 2
        //X    =  ___   \    (x   +  x  *  x  + x   ) * (y  - y   )
        // CM           /      i      i-1   i    i-1      i    i-1
        //        6   /__
        //             i = 1
        
        int[] x= new int[npoints+1];
        int[] y= new int[npoints+1];
        int i;
        x[0]=xpoints[0];
        y[0]=ypoints[0];
        for(i=0; i<npoints; i++) {
            x[i+1]=xpoints[i];
            y[i+1]=ypoints[i];
        }
        long sumX=0, sumY=0;
        for(i=1; i<=npoints; i++) {
            sumX += (( x[i]*x[i] + x[i-1]*x[i] + x[i-1]*x[i-1] ) * (y[i] - y[i-1]));
            sumY += (( y[i]*y[i] + y[i-1]*y[i] + y[i-1]*y[i-1] ) * (x[i] - x[i-1]));
        }
        double[] ret = new double[2];
        ret[0] = (double)sumX /6.0;
        ret[1] = (double)sumY /6.0;
        
        centre = ret;
    }
    
    private void calculerAire() {
        
        //            __n_
        //         1  \
        //Aire  = ___  \   (x  + x   ) (y  - y   )
        //            /     i    i-1    i    i-1
        //       2  /__
        //           i = 1
        
        int[] x= new int[npoints+1];
        int[] y= new int[npoints+1];
        int i;
        x[0]=xpoints[0];
        y[0]=ypoints[0];
        for(i=0; i<npoints; i++) {
            x[i+1]=xpoints[i];
            y[i+1]=ypoints[i];
        }
        long sum=0;
        for(i=1; i<=npoints; i++)
            sum += ((x[i-1] + x[i] )*( y[i] - y[i-1] ));
        double ret = Math.abs((double)sum /2.0);
        aire = ret;
    }
    
    public double[] getCentre() {
        return centre;
    }
    
    public double getRayonMoyenSuiv(){
        double somme = 0d;
        int sommeCpt = 0;
        for(int i=0;i<suivant.size();i++){
            somme += getSuivant(i).getRayonMoyenSuiv();
            sommeCpt++;
        }
        somme += getRayon();
        somme = somme/((double)sommeCpt+1d);
        return somme;
    }
    
    public double getRayonMoyenPrec(){
        double somme = 0d;
        int sommeCpt = 0;
        for(int i=0;i<precedent.size();i++){
            somme += getPrecedent(i).getRayonMoyenPrec();
            sommeCpt++;
        }
        somme += getRayon();
        somme = somme/((double)sommeCpt+1d);
        return somme;
    }
    
    public double getRayonMoyen(){
        double somme = (getRayonMoyenPrec() + getRayonMoyenSuiv())/2d;
        return somme;
    }
    
    public double getRayon(){
        int i=0;
        double tmp = 0d;
        
        for(i=0; i<npoints; i++) {
            tmp+=Math.sqrt(Math.pow((xpoints[i]-centre[0]),2d)+Math.pow(ypoints[i]-centre[1],2d));
        }
        
        return(tmp/i+1);
    }
    
    public double getAire(){
        calculerAire();
        return aire;
    }
    
    @Override
    public void addPoint(int param, int param1) {
        super.addPoint(param, param1);
        this.calculerCentre();
        this.calculerAire();
        majSens();
    }
    
    @Override
    public void invalidate() {
        super.invalidate();
        aire = -1;
        centre = null;
    }
    
    @Override
    public void reset() {
        super.reset();
        aire = -1;
        centre = null;
    }
    public void dispose() {
        xpoints = null;
        ypoints = null;
        npoints = 0;
        reset();
    }
    /** calcule une distance en pas de grille
     * entre deux points consecutifs Pn et Pn+1
     * ou dernier et premier point
     **/
    protected double getCote(int n) {
        if((n<0)|(n>=npoints)) {  return -1.0; }
        // indice du second point
        int ns = n+1;
        if ( n == npoints-1) {ns=0;}
        return java.lang.Math.sqrt((double)(((xpoints[n]-xpoints[ns])*(xpoints[n]-xpoints[ns]))
        + ((ypoints[n]-ypoints[ns])*(ypoints[n]-ypoints[ns]))) );
    }
    
    public double getPeriod(Courant [][] mer) {
        
        //            __n_
        //            \     distance P  P
        //Periode  =   \   ___________i  i-1
        //            /    vitesse en P
        //          /__                i-1
        //           i = 1
        
        
        if(npoints<=0) {return -1.0;}
        double ret=0.0;
        for (int i=0;i<npoints; i++) {
            ret = ret + ((getCote(i)*PAS_DE_GRILLE) / mer[xpoints[i]][ypoints[i]].norme);
        }
        return ret;
    }
    
    public double getDispersion(){
        
        //Dispersion  =  moyenne ( distance (Centre  Pi ))
        if(npoints<=0) {return -1.0;}
        double ret=0.0;
        double[] c = this.getCentre();
        for (int i=0;i<npoints; i++) {
            ret = ret + java.lang.Math.sqrt(  ((double)xpoints[i] - c[0])*((double)xpoints[i] - c[0])
            + ((double)ypoints[i] - c[1])*((double)ypoints[i] - c[1]) );
        }
        ret = ret / npoints;
        return ret;
    }
    
    public void setAfficheSuiv(boolean b){
        affiche = b;
        for(int i=0;i<suivant.size();i++)
            getSuivant(i).setAfficheSuiv(b);
    }
    
    public void setAffichePrec(boolean b){
        affiche = b;
        for(int i=0;i<precedent.size();i++)
            getPrecedent(i).setAffichePrec(b);
    }
    
    public void setSuivant(ArrayList b){ suivant = b;}
    
    public void setSuivant(Boucle b){ suivant.add(b);}
    
    public ArrayList getSuivant(){ return suivant;}
    
    public Boucle getSuivant(int i){ return (Boucle)(suivant.get(i));}
    
    public void setPrecedent(ArrayList b){ precedent = b;}
    
    public void setPrecedent(Boucle b){ precedent.add(b);}
    
    public ArrayList getPrecedent(){ return precedent;}
    
    public Boucle getPrecedent(int i){ return (Boucle)precedent.get(i);}
    
    public void setNum(int i){num = i;}
    
    public int getNum(){return num;}
    
}
