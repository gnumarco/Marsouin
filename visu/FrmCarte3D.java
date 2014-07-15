/*
 * FrmCarte3D.java
 *
 * Created on 3 decembre 2005, 23:04
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package visu;

import data.BatchDataMap;

/**
 *
 * @author Administrateur
 */
public class FrmCarte3D extends FrmMap{
    private final javax.swing.JButton jButton6;
    /** Creates a new instance of FrmCarte3D */
    public FrmCarte3D(Memory m, int moi, boolean mode) {
        super(m, moi, mode);
        jButton6 = new javax.swing.JButton();
        jButton6.setText("Afficher 3D");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Calculer3D(evt);
            }
        });
        jToolBar1.add(jButton6);
    }
    
    private void Calculer3D(java.awt.event.ActionEvent evt) {                            
        try{
            System.out.println("calculer3D");
            javax.swing.JScrollPane jScrollPane3D = new javax.swing.JScrollPane();;
            Canv3D tmp = new Canv3D((BatchDataMap)(mem.getDataCarte(id)));
            jScrollPane3D.setViewportView(tmp.getCanvas3D());
            jTabbedPaneCanvas.addTab("Vue 3D", jScrollPane3D);
        }catch(java.lang.Exception e){System.out.println("Java3D n'a pas ete detecte: "+e.toString());}
        
    } 
}
