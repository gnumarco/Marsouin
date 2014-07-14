/*
 * Fourmi.java
 *
 * Created on 10 septembre 2002, 15:10
 */

/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */

package ants;

import data.Boucle;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Random;



/** Une fourmi capable de se d�placer de fa�on autonome dans un champ de vecteurs �
 * deux dimensions.
 */
public class Fourmi implements constants.fourmi, constants.courant{
    
    private MoteurRecherche contexte;
    
    private ArrayList resultat=new ArrayList();
    
    private double Biais;
    
    /** coordonn�e courante en X de la fourmi */
    private int X;
    
    /** coordonn�e courante en X de la fourmi */    
    private int  Y;
    
    /** Biais de la fourmi. Compris entre -1 et 1. */    
    private double orient;
    
    /** M�moire contenant la liste des cellules sur lesquelles est pass�e la fourmi. */    
    private ArrayList Trace;
    /** Num�ro de l'esp�ce � laquelle appartient la fourmi */    
    private int espece;
    /** Identifiant de la fourmi � l'int�rieur de son esp�ce */    
    private int id;
    
    private double depot;
    Random rdGen;
    
    /** Creates a new instance of Fourmi
     * @param c Le moteur de recherche auquel est ratach�e la fourmi
     * @param b Le biais de la fourmi dans le choix de sa trajectoire (compris entre -1 et 1)
     * @param x Position initiale de la fourmi sur l'axe des X.
     * @param y Position initiale de la fourmi sur l'axe des Y.
     * @param esp Num�ro de l'esp�ce � laquelle appartient la fourmi.
     * @param ident Num�ro de la fourmi dans son esp�ce.
     * @param dep Quantit� de ph�romone d�pos�e par la fourmi lors d'une g�n�ration.
     */
    public Fourmi(MoteurRecherche c, double b, int x, int y, int esp, int ident, double dep, Random r) {
        contexte = c;
        depot = dep;
        Biais = b*(java.lang.Math.PI/3);
        X = x;
        Y = y;
        id = ident;
        espece = esp;
        orient = CalculAngle()+Biais;
        Trace = new ArrayList();
        rdGen = r;
    }
    
    /** M�thode permettant faire �voluer la fourmi d'une g�n�ration. */    
    public void Deplacer(){
        java.util.Vector candidates = new java.util.Vector();
        Geometrique loi = new Geometrique();
        Candidates cdtes = new Candidates();
        int NextCell = 0;
        
        if(contexte.getMaCarte().getC(X,Y).getXBase() == 0.0 && contexte.getMaCarte().getC(X,Y).getYBase() == 0.0)
            ReInit();
        else{
        orient = CalculAngle()+Biais;
        for(int i=0;i<8;i++){
            boolean valid = true;
            int posX = 0;
            int posY = 0;
            
            cdtes.getCand()[i].setDiff(java.lang.Math.min(java.lang.Math.abs(orient - cdtes.getCand()[i].getAngle()),java.lang.Math.abs(orient - (2*java.lang.Math.PI+cdtes.getCand()[i].getAngle()))));
            if(cdtes.getCand()[i].getDiff()<=(java.lang.Math.PI/2)){
                cdtes.getCand()[i].setChoosen(true);
                double fit = loi.calcul(0.5,((cdtes.getCand()[i].getDiff() * 90)/(java.lang.Math.PI/2))+1.0);
                cdtes.getCand()[i].setFitness(fit);
            }
            switch(i){
                case 0:
                    posX = X+1;
                    posY = Y;
                    if(X==contexte.getMaCarte().getTailleX()-1)
                        valid = false;
                    break;
                case 1:
                    posX = X+1;
                    posY = Y+1;
                    if(X==(contexte.getMaCarte().getTailleX()-1) || Y==(contexte.getMaCarte().getTailleY()-1))
                    
                        valid = false;
                    break;
                case 2:
                    posX = X;
                    posY = Y+1;
                    if(Y==(contexte.getMaCarte().getTailleY()-1))
                    
                        valid = false;
                    break;
                case 3:
                    posX = X-1;
                    posY = Y+1;
                    if(X==0 || Y==(contexte.getMaCarte().getTailleY()-1))
                    
                        valid = false;
                    break;
                case 4:
                    posX = X-1;
                    posY = Y;
                    if(X==0)
                        valid = false;
                    break;
                case 5:
                    posX = X-1;
                    posY = Y-1;
                    if(X==0 || Y==0)
                        valid = false;
                    break;
                case 6:
                    posX = X;
                    posY = Y-1;
                    if(Y==0)
                        valid = false;
                    break;
                case 7:
                    posX = X+1;
                    posY = Y-1;
                    if(X==(contexte.getMaCarte().getTailleX()-1) || Y==0)
                        valid = false;
                    break;
            }
            if(valid){
                if(contexte.getMaCarte().getC(posX,posY).getPheromone(espece) != 0f)
                    cdtes.getCand()[i].setFitness(cdtes.getCand()[i].getFitness() * 2f);
           }
        }
        double TotalFitness = 0.0;
        for(int i=0;i<8;i++){
            if(cdtes.getCand()[i].isChoosen())
                TotalFitness += cdtes.getCand()[i].getFitness();
        }
        double SpinVal = 0.0;
        double val = 0.0;
        SpinVal = rdGen.nextDouble()*TotalFitness;
        int i = 0;
        while(val < SpinVal && i < 8){
            if(cdtes.getCand()[i].isChoosen())
                val += cdtes.getCand()[i].getFitness();
            i++;
        }
        if (i > 0) 
            i--;
        
        NextCell = i;
        int NextX = 0;
        int NextY = 0;
        boolean OK = true;
        
        switch(NextCell){
            case 0:
                NextX = X+1;
                NextY = Y;
                if(X==(contexte.getMaCarte().getTailleX()-1))
                    OK = false;
                break;
            case 1:
                NextX = X+1;
                NextY = Y+1;
                if(X==(contexte.getMaCarte().getTailleX()-1) || (Y==contexte.getMaCarte().getTailleY()-1))
                
                    OK = false;
                break;
            case 2:
                NextX = X;
                NextY = Y+1;
                if(Y==(contexte.getMaCarte().getTailleY()-1))
                    OK = false;
                break;
            case 3:
                NextX = X-1;
                NextY = Y+1;
                if(X==0 || Y==(contexte.getMaCarte().getTailleY()-1))
                    OK = false;
                break;
            case 4:
                NextX = X-1;
                NextY = Y;
                if(X==0)
                    OK = false;
                break;
            case 5:
                NextX = X-1;
                NextY = Y-1;
                if(X==0 || Y==0)
                
                    OK = false;
                break;
            case 6:
                NextX = X;
                NextY = Y-1;
                if(Y==0)
                
                
                    OK = false;
                break;
            case 7:
                NextX = X+1;
                NextY = Y-1;
                if(X==(contexte.getMaCarte().getTailleX()-1) || Y==0)
                
                    OK = false;
                break;
        }
        if(Trace.size()>1)
            if((((Point)Trace.get(Trace.size()-2)).x == NextX) && (((Point)Trace.get(Trace.size()-2)).y == NextY))
                OK=false;
        if(OK){
            DeposePheromone();
            Trace.add(new Point(X,Y));
            X = NextX;
            Y = NextY;
            if(RechercheVortex())
                ReInit();
        }else{
            ReInit();
        }
        }
    }
    
    private void DeposePheromone(){
        contexte.getMaCarte().getC(X,Y).setPheromone(espece,id,contexte.getDepot());
    }
    
    private boolean RechercheVortex(){
       boolean res = false;
     
       if(VortexTrouve()){
           if(Trace.size() > 3){
            Trace.add(new Point(X,Y));
            java.util.Vector tmp = new java.util.Vector();
            int i = Trace.size()-2;
            tmp.addElement(new Point(X,Y));
            while(i>=0 && (((Point)Trace.get(i)).x != X || ((Point)Trace.get(i)).y != Y)){
               tmp.addElement((Point)Trace.get(i));
               i--;
            }
            tmp.addElement(new Point(X,Y));
            if(i>=0 && tmp.size()>=5){
                int[] max = new int[2];
                boolean orient = false;
                max[0]=0;
                max[1]=0;
                int[] tX = new int[tmp.size()];
                int[] tY = new int[tmp.size()];
                for(int j=0;j<tmp.size();j++){
                   tX[j]=((Point)tmp.elementAt(j)).x;
                   tY[j]=((Point)tmp.elementAt(j)).y;
                   if(tY[j]>max[1]){
                       max[0]=tX[j];
                       max[1]=tY[j];
                   }
                }
                if(contexte.getMaCarte().getC(max[0],max[1]).getXBase()>=0.0)
                    orient=true;
                else
                    orient=false;
                Boucle bcl=new Boucle(tX,tY,tX.length,id,orient);
                if(bcl.npoints>6 && bcl.npoints<40){
                    resultat.add(bcl);
                    res = true;
                }
                else
                    res = false;
            }
           }
           else
               res=false;
       }
       return res;
    }
    
    private boolean VortexTrouve(){
        boolean res = false;
        
        if(contexte.getMaCarte().getC(X,Y).getPheromone(espece,id) != 0f)
            res = true;
        
        return res;
    }
    
    /** M�thode de r�initialisation de la fourmi */    
    public void ReInit(){
        java.util.Random rd = new java.util.Random();
        
        X = rd.nextInt(contexte.getMaCarte().getTailleX());
        Y = rd.nextInt(contexte.getMaCarte().getTailleY());
        Trace = new ArrayList();
    }
    
    public double CalculAngle(){
        double theta=0.0;
        double costheta = 0.0;
        double sintheta = 0.0;
        double hyp = java.lang.Math.sqrt(contexte.getMaCarte().getC(X,Y).getYBase() *contexte.getMaCarte().getC(X,Y).getYBase() + contexte.getMaCarte().getC(X,Y).getXBase()*contexte.getMaCarte().getC(X,Y).getXBase());
        
        if (hyp != 0.0){
            if(contexte.getMaCarte().getC(X,Y).getXBase()>0 && contexte.getMaCarte().getC(X,Y).getYBase()>0)
                theta = java.lang.Math.acos(contexte.getMaCarte().getC(X,Y).getXBase()/hyp);
            if(contexte.getMaCarte().getC(X,Y).getXBase()<0 && contexte.getMaCarte().getC(X,Y).getYBase()>0)
                theta = java.lang.Math.PI - (java.lang.Math.acos(java.lang.Math.abs(contexte.getMaCarte().getC(X,Y).getXBase())/hyp));
            if(contexte.getMaCarte().getC(X,Y).getXBase()<0 && contexte.getMaCarte().getC(X,Y).getYBase()<0)
                theta = java.lang.Math.PI + (java.lang.Math.acos(java.lang.Math.abs(contexte.getMaCarte().getC(X,Y).getXBase())/hyp));
            if(contexte.getMaCarte().getC(X,Y).getXBase()>0 && contexte.getMaCarte().getC(X,Y).getYBase()<0)
                theta = (2*java.lang.Math.PI) - (java.lang.Math.acos(contexte.getMaCarte().getC(X,Y).getXBase()/hyp));
            
            if(contexte.getMaCarte().getC(X,Y).getXBase()==0 && contexte.getMaCarte().getC(X,Y).getYBase()>0)
                theta = java.lang.Math.PI/2;
            if(contexte.getMaCarte().getC(X,Y).getXBase()==0 && contexte.getMaCarte().getC(X,Y).getYBase()<0)
                theta = (3*java.lang.Math.PI)/2;
            if(contexte.getMaCarte().getC(X,Y).getXBase()>0 && contexte.getMaCarte().getC(X,Y).getYBase()==0)
                theta = 0.0;
            if(contexte.getMaCarte().getC(X,Y).getXBase()<0 && contexte.getMaCarte().getC(X,Y).getYBase()==0)
                theta = java.lang.Math.PI;
        }else 
            theta = 0.0;
        return theta;
    }
    
    public ArrayList getResultat(){ return resultat;}
    
    /** M�thode de lib�ration de la m�moire. */    
    protected void dispose(){resultat=null;}
}
