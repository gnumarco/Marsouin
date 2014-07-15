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
package ants;

import data.Loop;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Random;



/** Une fourmi capable de se d�placer de fa�on autonome dans un champ de vecteurs �
 * deux dimensions.
 */
public class Ant implements constants.fourmi, constants.courant{
    
    private final SearchEngine context;
    
    private ArrayList<Loop> result=new ArrayList<>();
    
    private final double bias;
    
    /** coordonn�e courante en X de la fourmi */
    private int X;
    
    /** coordonn�e courante en X de la fourmi */    
    private int  Y;
    
    /** bias de la fourmi. Compris entre -1 et 1. */    
    private double orient;
    
    /** M�moire contenant la liste des cellules sur lesquelles est pass�e la fourmi. */    
    private ArrayList<Point> trace;
    /** Num�ro de l'esp�ce � laquelle appartient la fourmi */    
    private int specie;
    /** Identifiant de la fourmi � l'int�rieur de son esp�ce */    
    private int id;
    
    private double drop;
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
    public Ant(SearchEngine c, double b, int x, int y, int esp, int ident, double dep, Random r) {
        context = c;
        drop = dep;
        bias = b*(java.lang.Math.PI/3);
        X = x;
        Y = y;
        id = ident;
        specie = esp;
        orient = computeAngle()+bias;
        trace = new ArrayList<>();
        rdGen = r;
    }
    
    /** M�thode permettant faire �voluer la fourmi d'une g�n�ration. */    
    public void move(){
        Geometric loi = new Geometric();
        Candidates cdtes = new Candidates();
        int NextCell = 0;
        
        if(context.getMaCarte().getC(X,Y).getXBase() == 0.0 && context.getMaCarte().getC(X,Y).getYBase() == 0.0)
            reInit();
        else{
        orient = computeAngle()+bias;
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
                    if(X==context.getMaCarte().getXSize()-1)
                        valid = false;
                    break;
                case 1:
                    posX = X+1;
                    posY = Y+1;
                    if(X==(context.getMaCarte().getXSize()-1) || Y==(context.getMaCarte().getYSize()-1))
                    
                        valid = false;
                    break;
                case 2:
                    posX = X;
                    posY = Y+1;
                    if(Y==(context.getMaCarte().getYSize()-1))
                    
                        valid = false;
                    break;
                case 3:
                    posX = X-1;
                    posY = Y+1;
                    if(X==0 || Y==(context.getMaCarte().getYSize()-1))
                    
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
                    if(X==(context.getMaCarte().getXSize()-1) || Y==0)
                        valid = false;
                    break;
            }
            if(valid){
                if(context.getMaCarte().getC(posX,posY).getPheromone(specie) != 0f)
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
                if(X==(context.getMaCarte().getXSize()-1))
                    OK = false;
                break;
            case 1:
                NextX = X+1;
                NextY = Y+1;
                if(X==(context.getMaCarte().getXSize()-1) || (Y==context.getMaCarte().getYSize()-1))
                
                    OK = false;
                break;
            case 2:
                NextX = X;
                NextY = Y+1;
                if(Y==(context.getMaCarte().getYSize()-1))
                    OK = false;
                break;
            case 3:
                NextX = X-1;
                NextY = Y+1;
                if(X==0 || Y==(context.getMaCarte().getYSize()-1))
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
                if(X==(context.getMaCarte().getXSize()-1) || Y==0)
                
                    OK = false;
                break;
        }
        if(trace.size()>1)
            if((((Point)trace.get(trace.size()-2)).x == NextX) && (((Point)trace.get(trace.size()-2)).y == NextY))
                OK=false;
        if(OK){
            dropPheromone();
            trace.add(new Point(X,Y));
            X = NextX;
            Y = NextY;
            if(searchVortex())
                reInit();
        }else{
            reInit();
        }
        }
    }
    
    private void dropPheromone(){
        context.getMaCarte().getC(X,Y).setPheromone(specie,id,context.getDepot());
    }
    
    private boolean searchVortex(){
       boolean res = false;
     
       if(foundVortex()){
           if(trace.size() > 3){
            trace.add(new Point(X,Y));
            ArrayList<Point> tmp = new ArrayList<>();
            int i = trace.size()-2;
            tmp.add(new Point(X,Y));
            while(i>=0 && (((Point)trace.get(i)).x != X || ((Point)trace.get(i)).y != Y)){
               tmp.add((Point)trace.get(i));
               i--;
            }
            tmp.add(new Point(X,Y));
            if(i>=0 && tmp.size()>=5){
                int[] max = new int[2];
                boolean orientBool = false;
                max[0]=0;
                max[1]=0;
                int[] tX = new int[tmp.size()];
                int[] tY = new int[tmp.size()];
                for(int j=0;j<tmp.size();j++){
                   tX[j]=((Point)tmp.get(j)).x;
                   tY[j]=((Point)tmp.get(j)).y;
                   if(tY[j]>max[1]){
                       max[0]=tX[j];
                       max[1]=tY[j];
                   }
                }
                if(context.getMaCarte().getC(max[0],max[1]).getXBase()>=0.0)
                    orientBool=true;
                else
                    orientBool=false;
                Loop bcl=new Loop(tX,tY,tX.length,id,orientBool);
                if(bcl.npoints>6 && bcl.npoints<40){
                    result.add(bcl);
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
    
    private boolean foundVortex(){
        boolean res = false;
        
        if(context.getMaCarte().getC(X,Y).getPheromone(specie,id) != 0f)
            res = true;
        
        return res;
    }
    
    /** M�thode de r�initialisation de la fourmi */    
    public void reInit(){
        java.util.Random rd = new java.util.Random();
        
        X = rd.nextInt(context.getMaCarte().getXSize());
        Y = rd.nextInt(context.getMaCarte().getYSize());
        trace = new ArrayList();
    }
    
    public final double computeAngle(){
        double theta=0.0;
        double hyp = java.lang.Math.sqrt(context.getMaCarte().getC(X,Y).getYBase() *context.getMaCarte().getC(X,Y).getYBase() + context.getMaCarte().getC(X,Y).getXBase()*context.getMaCarte().getC(X,Y).getXBase());
        
        if (hyp != 0.0){
            if(context.getMaCarte().getC(X,Y).getXBase()>0 && context.getMaCarte().getC(X,Y).getYBase()>0)
                theta = java.lang.Math.acos(context.getMaCarte().getC(X,Y).getXBase()/hyp);
            if(context.getMaCarte().getC(X,Y).getXBase()<0 && context.getMaCarte().getC(X,Y).getYBase()>0)
                theta = java.lang.Math.PI - (java.lang.Math.acos(java.lang.Math.abs(context.getMaCarte().getC(X,Y).getXBase())/hyp));
            if(context.getMaCarte().getC(X,Y).getXBase()<0 && context.getMaCarte().getC(X,Y).getYBase()<0)
                theta = java.lang.Math.PI + (java.lang.Math.acos(java.lang.Math.abs(context.getMaCarte().getC(X,Y).getXBase())/hyp));
            if(context.getMaCarte().getC(X,Y).getXBase()>0 && context.getMaCarte().getC(X,Y).getYBase()<0)
                theta = (2*java.lang.Math.PI) - (java.lang.Math.acos(context.getMaCarte().getC(X,Y).getXBase()/hyp));
            
            if(context.getMaCarte().getC(X,Y).getXBase()==0 && context.getMaCarte().getC(X,Y).getYBase()>0)
                theta = java.lang.Math.PI/2;
            if(context.getMaCarte().getC(X,Y).getXBase()==0 && context.getMaCarte().getC(X,Y).getYBase()<0)
                theta = (3*java.lang.Math.PI)/2;
            if(context.getMaCarte().getC(X,Y).getXBase()>0 && context.getMaCarte().getC(X,Y).getYBase()==0)
                theta = 0.0;
            if(context.getMaCarte().getC(X,Y).getXBase()<0 && context.getMaCarte().getC(X,Y).getYBase()==0)
                theta = java.lang.Math.PI;
        }else 
            theta = 0.0;
        return theta;
    }
    
    public ArrayList getResult(){ return result;}
    
}
