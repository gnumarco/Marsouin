/*
 * ProcessRetentionThread.java
 *
 * Created on 16 mars 2006, 20:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package visu;

/**
 *
 * @author marco
 */
public class ProcessRetentionThread extends Thread{
    Memoire mem;
    int id;
    javax.swing.ProgressMonitor prog;
    private int[][][] degrade = null;
    private double[][] retention = null;
    boolean clockwize = false;
    /** Creates a new instance of ProcessRetentionThread */
    public ProcessRetentionThread(Memoire m, int[][][] d, double[][] r, int i, javax.swing.ProgressMonitor p, boolean clock) {
        mem = m;
        degrade = d;
        retention = r;
        id = i;
        prog = p;
        clockwize = clock;
    }
    
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
                        while((z<mem.getBatchDataCarte(id).getDataCarte(i,mem.getFrmVisu(id).getProfActive()).getVortexAnt().getMetaVortex().length) && (!mem.getBatchDataCarte(id).getDataCarte(i,mem.getFrmVisu(id).getProfActive()).getVortexAnt().getMetaVortex(z).contains(x,y)))
                            z++;
                        if((z<mem.getBatchDataCarte(id).getDataCarte(i,mem.getFrmVisu(id).getProfActive()).getVortexAnt().getMetaVortex().length) && (mem.getBatchDataCarte(id).getDataCarte(i,mem.getFrmVisu(id).getProfActive()).getVortexAnt().getMetaVortex(z).getSens()==clockwize))
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
            colors[1]=Math.round(510*(float)v);
            colors[2]=Math.round(-510*(float)v+255);
        }else{
            colors[0]=Math.round(510*(float)v-255);
            colors[1]=Math.round(-510*(float)v+510);
            colors[2]=0;
        }
        return colors;
    }
}
