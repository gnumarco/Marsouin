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

import com.marsouin.data.BatchDataMap;

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
