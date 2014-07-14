/*
 * Comparateur.java
 *
 * Created on 30 juin 2005, 11:09
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

/**
 *
 * @author segond
 */

import java.io.File;
import ucar.nc2.*;
import ucar.ma2.*;
import javax.swing.*;

public class Comparateur {
    
    private int ref[][][], detect[][][], FP, TP, maxTP, maxFP;
    
    
    /** Creates a new instance of Comparateur */
    public Comparateur() {
    }
    
    private void OpenData(String r, String d){
        int tailleX, tailleY, times;
        
        System.out.println("Lecture des exemples");
        try{
            NetcdfFile f = new NetcdfFile(r);
            times = ((Variable)(f.getVariables().get(2))).getDimension(0).getLength();
            System.out.println("times:"+times);
            tailleX = ((Variable)(f.getVariables().get(2))).getDimension(1).getLength();
            System.out.println("tailleX:"+tailleX);
            tailleY = ((Variable)(f.getVariables().get(2))).getDimension(2).getLength();
            System.out.println("tailleY:"+tailleY);
            ref = new int[times][tailleX][tailleY];
            Variable var = ((Variable)(f.getVariables().get(2)));
            Array tab = var.read();
            Index ind = tab.getIndex();
            int[] tbl = new int[var.getDimensions().size()];
            for(int z=0;z<times;z++)
                for(int i=0;i<tailleX;i++)
                    for(int j=0;j<tailleY;j++){
                tbl[1] = i;
                tbl[2] = j;
                tbl[0] = z;
                ind.set(tbl);
                if(tab.getInt(ind)>0)
                    ref[z][i][j] = 1;
                else
                    ref[z][i][j] = 0;
                    }
            tab = null;
            var = null;
            f.close();
        }catch(java.lang.Exception e){System.out.println(e.toString());}
        System.gc();
        
        System.out.println("lecture des detections");
        try{
            NetcdfFile f = new NetcdfFile(d);
            times = ((Variable)(f.getVariables().get(0))).getDimension(0).getLength();
            System.out.println("times:"+times);
            tailleX = ((Variable)(f.getVariables().get(0))).getDimension(1).getLength();
            System.out.println("tailleX:"+tailleX);
            tailleY = ((Variable)(f.getVariables().get(0))).getDimension(2).getLength();
            System.out.println("tailleY:"+tailleY);
            detect = new int[times][tailleX][tailleY];
            Variable var = ((Variable)(f.getVariables().get(0)));
            Array tab = var.read();
            Index ind = tab.getIndex();
            int[] tbl = new int[var.getDimensions().size()];
            System.out.println("dims de l'index:"+var.getDimensions().size());
            for(int z=0;z<times;z++)
                for(int i=0;i<tailleX;i++)
                    for(int j=0;j<tailleY;j++){
                tbl[1] = i;
                tbl[2] = j;
                tbl[0] = z;
                ind.set(tbl);
                if(tab.getInt(ind)!=0)
                    detect[z][i][j] = 1;
                else
                    detect[z][i][j] = 0;
                    }
            tab = null;
            var = null;
            f.close();
        }catch(java.lang.Exception e){System.out.println(e.toString());}
        System.gc();
    }
    
    private void compare(){
        FP = 0;
        TP = 0;
        maxTP = 0;
        maxFP = 0;
        for(int z=0;z<detect.length;z++){
            for(int i=0;i<ref[z+1].length;i++){
                for(int j=0;j<ref[z+1][i].length;j++){
                    if(ref[z+1][i][j] == 1)
                        maxTP++;
                    else
                        maxFP++;
                    if(detect[z][i][j] == 1){
                        if(ref[z+1][i][j] == 1)
                            TP++;
                        else
                            FP++;
                    }
                }
            }
        }
    }
    
    private void PrintResuls(){
        System.out.println("nbr TP:"+TP);
        System.out.println("nbr FP:"+FP);
        System.out.println("max TP:"+maxTP);
        System.out.println("max FP:"+maxFP);
    }
    
    public static void main(String[] args) {
        Comparateur c = new Comparateur();
        c.OpenData(args[0],args[1]);
        c.compare();
        c.PrintResuls();
    }
    
}
