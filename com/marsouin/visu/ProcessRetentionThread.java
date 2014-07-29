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
package com.marsouin.visu;

import static java.lang.Math.round;

/**
 *
 * @author marco
 */
public class ProcessRetentionThread extends Thread{
    Memory mem;
    int id;
    javax.swing.ProgressMonitor prog;
    private int[][][] degrade = null;
    private double[][] retention = null;
    boolean clockwize = false;

    public ProcessRetentionThread(Memory m, int[][][] d, double[][] r, int i, javax.swing.ProgressMonitor p, boolean clock) {
        mem = m;
        degrade = d;
        retention = r;
        id = i;
        prog = p;
        clockwize = clock;
    }
    
    @Override
    public void run(){
        int tmpCpt = 0;
        int tX =mem.getDataCarte(id).getXSize();
        int tY =mem.getDataCarte(id).getYSize();
        for(int x=0;x<tX;x++){
            for(int y=0;y<tY;y++){
                if (!mem.getDataCarte(id).getC(x,y).getSurTerre()) {
                    int sum = 0;
                    for(int i=0;i<mem.getBatchDataCarte(id).getNbDataCartesTps();i++){
                        int z=0;
                        while((z<mem.getBatchDataCarte(id).getDataCarte(i,mem.getFrmVisu(id).getProfActive()).getVortexAnt().size()) && (!mem.getBatchDataCarte(id).getDataCarte(i,mem.getFrmVisu(id).getProfActive()).getVortexAnt().get(z).contains(x,y)))
                            z++;
                        if((z<mem.getBatchDataCarte(id).getDataCarte(i,mem.getFrmVisu(id).getProfActive()).getVortexAnt().size()) && (mem.getBatchDataCarte(id).getDataCarte(i,mem.getFrmVisu(id).getProfActive()).getVortexAnt().get(z).getSens()==clockwize))
                            sum++;
                        tmpCpt++;
                        prog.setProgress(tmpCpt);
                    }
                    retention[x][y] = (double)sum/(double)mem.getBatchDataCarte(id).getNbDataCartesTps();
                    degrade[x][y] = calculDegrade(retention[x][y]);
                }
            }
        }
        mem.getFrmVisu(id).MajTaille();
        prog.close();
    }
    
    public int[] calculDegrade(double v){
        int colors[] = new int[3];
        if(v<0.5){
            colors[0]=0;
            colors[1]=round(510*(float)v);
            colors[2]=round(-510*(float)v+255);
        }else{
            colors[0]=round(510*(float)v-255);
            colors[1]=round(-510*(float)v+510);
            colors[2]=0;
        }
        return colors;
    }
}
