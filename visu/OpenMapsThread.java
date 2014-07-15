/*
 * OpenMapsThread.java
 *
 * Created on 16 mars 2006, 14:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package visu;

import java.util.ArrayList;

/**
 *
 * @author marco
 */
public class OpenMapsThread extends Thread {
    
    int[] profondeurs, dates;
    int indexProfondeurs, indexDates, uZ, indexU, vZ, indexV, id;
    String missingValue, fichier;
    boolean noMissingValue, Ok3D;
    javax.swing.ProgressMonitor prog;
    ArrayList listeDataCartes, listeFrmVisu;
    Memory mem;
    
    /** Creates a new instance of OpenMapsThread */
    public OpenMapsThread(String f,int[] prof,int profInd, int[] time, int timeInd, int u, int uInd, int v, int vInd, String MVAtt, boolean noMV, javax.swing.ProgressMonitor p, ArrayList liste, boolean troisD, ArrayList liste2, int i, Memory m) {
        fichier = f;
        profondeurs = prof;
        dates = time;
        indexProfondeurs = profInd;
        indexDates = timeInd;
        uZ = u;
        vZ = v;
        indexU = uInd;
        indexV = vInd;
        missingValue = MVAtt;
        noMissingValue = noMV;
        prog = p;
        listeDataCartes = liste;
        listeFrmVisu = liste2;
        mem = m;
    }
    
    @Override
    public void run(){
        listeDataCartes.add(new data.BatchDataMap(fichier,profondeurs,indexProfondeurs,dates,indexDates,uZ,indexU,vZ,indexV,missingValue,noMissingValue,prog));
        try{
            if(Ok3D)
                listeFrmVisu.add( new FrmCarte3D(mem,id, true) );
            else
                listeFrmVisu.add( new FrmMap(mem,id, true) );
            ((FrmMap) listeFrmVisu.get(id)).setVisible(true);
            ((FrmMap) listeFrmVisu.get(id)).toFront();
            ((FrmMap) listeFrmVisu.get(id)).MajTaille();
        }catch (Exception e){System.out.println("Mem : erreur a l'ajout de FrmVisu !");
        e.printStackTrace();
        mem.retraitCarte(id);
        id = -1;
        mem.modifierParametres(id);}
        
    }
    
}
