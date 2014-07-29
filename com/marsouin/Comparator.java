package com.marsouin;

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
import java.io.IOException;
import static java.lang.System.gc;
import ucar.nc2.*;
import ucar.ma2.*;
import static ucar.nc2.NetcdfFile.open;

public class Comparator {
    
    private int ref[][][], detect[][][], FP, TP, maxTP, maxFP;
    
    
    /** Creates a new instance of Comparateur */
    public Comparator() {
    }
    
    private void openData(String r, String d){
        int tailleX, tailleY, times;
        
        System.out.println("Lecture des exemples");
        try{
            NetcdfFile f = open(r);
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
            f.close();
        }catch(IOException e){System.out.println(e.toString());}
        
        System.out.println("lecture des detections");
        try{
            NetcdfFile f = open(d);
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
            f.close();
        }catch(IOException e){System.out.println(e.toString());}
        gc();
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
    
    private void printResuls(){
        System.out.println("nbr TP:"+TP);
        System.out.println("nbr FP:"+FP);
        System.out.println("max TP:"+maxTP);
        System.out.println("max FP:"+maxFP);
    }
    
    public static void main(String[] args) {
        Comparator c = new Comparator();
        c.openData(args[0],args[1]);
        c.compare();
        c.printResuls();
    }
    
}
