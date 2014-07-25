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
package com.marsouin.data;

import java.io.IOException;
import ucar.nc2.*;
import ucar.ma2.*;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 *
 * @author  marco
 */
public class AttributesChoice extends javax.swing.JDialog {
    
    /** Creates new form attributesChoice */
    public AttributesChoice(String fi, int U, String prof, String time, java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        try{
            NetcdfFile f = NetcdfFile.open(fi);
            Variable v = ((Variable)(f.getVariables().get(U)));
            //System.out.println(v.getName());
            java.util.List<Dimension> l;
            l = v.getDimensions();
            String[] tmp = new String[l.size()];
            for(int i=0;i<l.size();i++){
                tmp[i] = v.getDimension(i).getShortName();
            }
            absc.setListData(tmp);
            absc.setSelectedIndex(0);
            ord.setListData(tmp);
            ord.setSelectedIndex(1);
            Array tps;
            String stmp;
            Index ind;
            String[] p;
            
            if(prof!=null){
                Dimension dd = ((NetcdfFile)f).findDimension(prof);
                //v = dd.getCoordinateVariable();
                v = ((NetcdfFile)f).findVariable(dd.getShortName());
                if(v!=null && v.isCoordinateVariable()){
                    tps = v.read();
                    p = new String[tps.getShape()[0]];
                    ind = tps.getIndex();
                    for(int i=0;i<(tps.getShape())[0];i++){
                        ind.set(i);
                        double t = tps.getDouble(ind);
                        stmp = Double.toString(t);
                        p[i]=stmp;
                    }
                }else{
                    p = new String[dd.getLength()];
                    for(int i=0;i<dd.getLength();i++){
                        p[i]=Integer.toString(i);
                    }
                }
            }else{
                p = new String[1];
                p[0] = "pas de profondeurs";
                profs.setEnabled(false);
            }
            profs.setListData(p);
            profs.setSelectedIndex(0);
            
            Dimension dd = ((NetcdfFile)f).findDimension(time);
            v = ((NetcdfFile)f).findVariable(dd.getShortName());
            String[] times;
            if(v!=null && v.isCoordinateVariable()){
                tps = v.read();
                times = new String[tps.getShape()[0]];
                ind = tps.getIndex();
                for(int i=0;i<(tps.getShape())[0];i++){
                    ind.set(i);
                    double t = tps.getDouble(ind);
                    long temp = (long)t;
                    GregorianCalendar cal = new GregorianCalendar(java.util.Locale.FRENCH);
                    cal.set(0000,java.util.Calendar.JANUARY,1,0,0,0);
                    Date ref = cal.getTime();
                    temp = temp * 1000;
                    temp = temp + ref.getTime();
                    Date cur = new Date(temp);
                    stmp = cur.toString();
                    times[i]=stmp;
                }
            }else{
                times = new String[dd.getLength()];
                for(int i=0;i<dd.getLength();i++){
                    times[i]=Integer.toString(i);
                }
            }
            dates.setListData(times);
            dates.setSelectedIndex(0);
            
            f.close();

        }catch(IOException e){System.out.println(e.toString());}
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        absc = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        ord = new javax.swing.JList();
        boutonOK = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        profs = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        dates = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Parameters choice");
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jLabel1.setText("Dimension to display on the x-axis:");
        getContentPane().add(jLabel1, new java.awt.GridBagConstraints());

        jLabel2.setText("Dimension to display on the y-axis:");
        getContentPane().add(jLabel2, new java.awt.GridBagConstraints());

        jScrollPane1.setViewportView(absc);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jScrollPane2.setViewportView(ord);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane2, gridBagConstraints);

        boutonOK.setText("OK");
        boutonOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        getContentPane().add(boutonOK, gridBagConstraints);

        jLabel3.setText("Levels:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jLabel3, gridBagConstraints);

        jLabel4.setText("Dates:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jLabel4, gridBagConstraints);

        jScrollPane3.setViewportView(profs);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane3, gridBagConstraints);

        jScrollPane4.setViewportView(dates);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane4, gridBagConstraints);

        setSize(new java.awt.Dimension(475, 491));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    
    private void boutonOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonOKActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_boutonOKActionPerformed
    
    public int getTailleX(){return absc.getSelectedIndex();}
    public int getTailleY(){return ord.getSelectedIndex();}
    public int[] getProfs(){return profs.getSelectedIndices();}
    public int[] getDates(){return dates.getSelectedIndices();}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList absc;
    private javax.swing.JButton boutonOK;
    private javax.swing.JList dates;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList ord;
    private javax.swing.JList profs;
    // End of variables declaration//GEN-END:variables
    
}
