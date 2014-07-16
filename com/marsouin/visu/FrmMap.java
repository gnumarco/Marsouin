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
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import static java.lang.System.gc;
import static java.lang.System.getProperty;
/**
 *
 * @author  Marc Segond
 */
public class FrmMap extends javax.swing.JFrame implements com.marsouin.constants.Colors {

    protected Memory mem = null;
    protected int id;
    private CanvasMap monCanvas = null;
    private CanvRet monCanvasRet = null;
    public int avMax = 0;

    /** Creates new form FrmCarte */
    public FrmMap(Memory m, int moi, boolean mode) {
        initComponents();
        mem = m;
        id = moi;
        monCanvas = new CanvasMap(mem, id);
        jScrollPane2D.setViewportView(monCanvas);
        jSlider1.setMinimum(0);
        jSlider1.setMaximum(((BatchDataMap) (mem.getDataCarte(id))).getNbDataCartesTps() - 1);
        jSlider1.setVisible(true);
        jSlider2.setMinimum(0);
        jSlider2.setMaximum(((BatchDataMap) (mem.getDataCarte(id))).getNbDataCartesProf() - 1);
        jSlider2.setVisible(true);
        if (((BatchDataMap) (mem.getDataCarte(id))).getNbDataCartesTps() == 1) {
            jSlider1.setVisible(false);
        }
        if (((BatchDataMap) (mem.getDataCarte(id))).getNbDataCartesProf() == 1) {
            jSlider2.setVisible(false);
        }
        MajNom();
        mem.getFrmConfig().setNbGen((((BatchDataMap) (mem.getDataCarte(id))).getXSize() + ((BatchDataMap) (mem.getDataCarte(id))).getYSize()));
        double tmp = ((((BatchDataMap) (mem.getDataCarte(id))).getXSize() * ((BatchDataMap) (mem.getDataCarte(id))).getYSize() / 500d) * 4d);
        mem.getFrmConfig().setNbColo((int) (round(sqrt(tmp))));
        mem.getFrmConfig().setNbFourmis((int) (round(sqrt(tmp))));
    }

    @Override
    public java.awt.Image getIconImage() {
        return (new javax.swing.ImageIcon("logo_carre.gif").getImage());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jToggleButtonRetentionMap = new javax.swing.JToggleButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        gridSlider = new javax.swing.JSlider();
        jSlider1 = new javax.swing.JSlider();
        jSlider2 = new javax.swing.JSlider();
        jTabbedPaneCanvas = new javax.swing.JTabbedPane();
        jScrollPane2D = new javax.swing.JScrollPane();
        jMenuBar = new javax.swing.JMenuBar();
        jMenuExport = new javax.swing.JMenu();
        jMenuItemSaveCurrentImage = new javax.swing.JMenuItem();
        jMenuItemSaveAllImages = new javax.swing.JMenuItem();
        jMenuItemSaveRetention = new javax.swing.JMenuItem();
        jMenuItemExportDetection = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Marsouin v2.99");
        setIconImage(getIconImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                closing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jButton1.setText("Detection");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DemarrerDetection(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton3.setText("Tracking");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DemarrerSuivi(evt);
            }
        });
        jToolBar1.add(jButton3);

        jToggleButtonRetentionMap.setText("Retention Map");
        jToggleButtonRetentionMap.setEnabled(false);
        jToggleButtonRetentionMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonRetentionMapActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButtonRetentionMap);

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Clock");
        jRadioButton1.setEnabled(false);
        jRadioButton1.setFocusable(false);
        jRadioButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRadioButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jRadioButton1);

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Anticlock");
        jRadioButton2.setEnabled(false);
        jRadioButton2.setFocusable(false);
        jRadioButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRadioButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jRadioButton2);

        jButton4.setText("Zoom in");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZoomAvant(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton5.setText("Zoom out");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZoomArriere(evt);
            }
        });
        jToolBar1.add(jButton5);

        jToggleButton1.setText("Antialiasing");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jToggleButton1);

        gridSlider.setMajorTickSpacing(1);
        gridSlider.setMaximum(20);
        gridSlider.setMinimum(1);
        gridSlider.setPaintTicks(true);
        gridSlider.setSnapToTicks(true);
        gridSlider.setToolTipText("Grid resolution");
        gridSlider.setValue(10);
        gridSlider.setName("Grid resolution"); // NOI18N
        gridSlider.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                gridSliderPropertyChange(evt);
            }
        });
        gridSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                gridSliderStateChanged(evt);
            }
        });
        jToolBar1.add(gridSlider);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1000.0;
        getContentPane().add(jToolBar1, gridBagConstraints);

        jSlider1.setMinorTickSpacing(1);
        jSlider1.setPaintTicks(true);
        jSlider1.setSnapToTicks(true);
        jSlider1.setToolTipText("Dates");
        jSlider1.setValue(0);
        jSlider1.setName("Dates"); // NOI18N
        jSlider1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                ChangeActiveCard(evt);
            }
        });
        jSlider1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ChangeActiveCard(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        getContentPane().add(jSlider1, gridBagConstraints);

        jSlider2.setMinorTickSpacing(1);
        jSlider2.setOrientation(javax.swing.JSlider.VERTICAL);
        jSlider2.setPaintTicks(true);
        jSlider2.setSnapToTicks(true);
        jSlider2.setToolTipText("Levels");
        jSlider2.setValue(0);
        jSlider2.setName("Levels"); // NOI18N
        jSlider2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                ChangeActiveCard(evt);
            }
        });
        jSlider2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                ChangeActiveCard(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        getContentPane().add(jSlider2, gridBagConstraints);

        jTabbedPaneCanvas.setDoubleBuffered(true);

        jScrollPane2D.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                Vue2DResized(evt);
            }
        });
        jTabbedPaneCanvas.addTab("2D view", jScrollPane2D);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1000.0;
        gridBagConstraints.weighty = 1000.0;
        getContentPane().add(jTabbedPaneCanvas, gridBagConstraints);

        jMenuExport.setText("Export");

        jMenuItemSaveCurrentImage.setText("Save current image");
        jMenuItemSaveCurrentImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveCurrentImageActionPerformed(evt);
            }
        });
        jMenuExport.add(jMenuItemSaveCurrentImage);

        jMenuItemSaveAllImages.setText("Save all images");
        jMenuItemSaveAllImages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveAllImagesActionPerformed(evt);
            }
        });
        jMenuExport.add(jMenuItemSaveAllImages);

        jMenuItemSaveRetention.setText("Save retention data");
        jMenuItemSaveRetention.setEnabled(false);
        jMenuItemSaveRetention.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSaveRetentionActionPerformed(evt);
            }
        });
        jMenuExport.add(jMenuItemSaveRetention);

        jMenuItemExportDetection.setText("Item");
        jMenuItemExportDetection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemExportDetectionActionPerformed(evt);
            }
        });
        jMenuExport.add(jMenuItemExportDetection);

        jMenuBar.add(jMenuExport);

        setJMenuBar(jMenuBar);

        setSize(new java.awt.Dimension(800, 600));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closing
        mem.retraitCarte(id);
    }//GEN-LAST:event_closing

    private void jMenuItemSaveRetentionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveRetentionActionPerformed
        String cheminDefaut;

        if (mem.getMemCheminExplo() != null) {
            cheminDefaut = mem.getMemCheminExplo();
        } else {
            cheminDefaut = getProperty("user.home");
        }

        javax.swing.JFileChooser F = new javax.swing.JFileChooser(cheminDefaut);

        F.setDialogTitle("Define the name or your CSV file");
        F.setMultiSelectionEnabled(false);

        int returnVal = F.showOpenDialog(this);

        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                com.marsouin.data.WriteTextFile wtf = new com.marsouin.data.WriteTextFile(F.getSelectedFile().getAbsolutePath() + ".csv");
                double[][] tmp = monCanvasRet.getRetention();
                String s;
                for (double[] tmp1 : tmp) {
                    s = "";
                    for (int j = 0; j < tmp[0].length; j++) {
                        s = s + tmp1[j] + ";";
                    }
                    wtf.uneLigne(s);
                }
                wtf.fermer();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }//GEN-LAST:event_jMenuItemSaveRetentionActionPerformed

    private void jMenuItemExportDetectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemExportDetectionActionPerformed
        mem.exporterDetection(id, this);
    }//GEN-LAST:event_jMenuItemExportDetectionActionPerformed

    private void jMenuItemSaveCurrentImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveCurrentImageActionPerformed
        javax.swing.ProgressMonitor p = new javax.swing.ProgressMonitor(this, "Saving all maps", "Saving...", 0, 0);
        ImageResultFile capture = new ImageResultFile(mem, id, false, p);
        capture.start();
    }//GEN-LAST:event_jMenuItemSaveCurrentImageActionPerformed

    private void jMenuItemSaveAllImagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSaveAllImagesActionPerformed
        javax.swing.ProgressMonitor p = new javax.swing.ProgressMonitor(this, "Saving all maps", "Saving...", 0, 0);
        ImageResultFile capture = new ImageResultFile(mem, id, true, p);
        capture.start();
    }//GEN-LAST:event_jMenuItemSaveAllImagesActionPerformed

    private void jToggleButtonRetentionMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonRetentionMapActionPerformed
        if (jToggleButtonRetentionMap.isSelected()) {
            if (buttonGroup1.getSelection() == jRadioButton1.getModel()) {
                javax.swing.JScrollPane jScrollPaneRet = new javax.swing.JScrollPane();
                monCanvasRet = new CanvRet(mem, id);
                jScrollPaneRet.setViewportView(monCanvasRet);
                jTabbedPaneCanvas.addTab("Retention", jScrollPaneRet);
                monCanvasRet.setZoomLatLon(gridSlider.getValue());
                jMenuItemSaveRetention.setEnabled(true);
                monCanvasRet.processRet(true);
            } else {
                javax.swing.JScrollPane jScrollPaneRet = new javax.swing.JScrollPane();
                monCanvasRet = new CanvRet(mem, id);
                jScrollPaneRet.setViewportView(monCanvasRet);
                jTabbedPaneCanvas.addTab("Retention", jScrollPaneRet);
                monCanvasRet.setZoomLatLon(gridSlider.getValue());
                jMenuItemSaveRetention.setEnabled(true);
                monCanvasRet.processRet(false);
            }
        } else {
            jMenuItemSaveRetention.setEnabled(false);
            monCanvasRet.clearRet();
            jTabbedPaneCanvas.remove(jTabbedPaneCanvas.getTabCount() - 1);
            monCanvasRet = null;
            gc();
        }
    }//GEN-LAST:event_jToggleButtonRetentionMapActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        if (jToggleButton1.isSelected()) {
            mem.setAntialias(id, true);
        } else {
            mem.setAntialias(id, false);
        }
        monCanvas.processImage();
        monCanvas.repaint();
        if (monCanvasRet != null) {
            monCanvasRet.processImage();
            monCanvasRet.repaint();
        }

    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void gridSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_gridSliderStateChanged
        monCanvas.setZoomLatLon(gridSlider.getValue());
        monCanvas.processImage();
        if (monCanvasRet != null) {
            monCanvasRet.setZoomLatLon(gridSlider.getValue());
            monCanvasRet.processImage();
        }
    }//GEN-LAST:event_gridSliderStateChanged

    private void gridSliderPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_gridSliderPropertyChange
        try {
            if (monCanvas.getZoomLatLon() != gridSlider.getValue()) {
                monCanvas.setZoomLatLon(gridSlider.getValue());
                monCanvas.processImage();
            }
            if ((monCanvasRet != null) && (monCanvasRet.getZoomLatLon() != gridSlider.getValue())) {
                monCanvasRet.setZoomLatLon(gridSlider.getValue());
                monCanvasRet.processImage();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_gridSliderPropertyChange

    private void DemarrerSuivi(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DemarrerSuivi
        mem.suivi(id);
    }//GEN-LAST:event_DemarrerSuivi
    public final void MajNom() {
        setTitle(((BatchDataMap) (mem.getDataCarte(id))).getCarteActive().getDate());
    }
    private void ChangeActiveCard(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ChangeActiveCard
        if ((jSlider1.getValue() != (((BatchDataMap) (mem.getDataCarte(id))).getActive())[0]) || (jSlider2.getValue() != (((BatchDataMap) (mem.getDataCarte(id))).getActive())[1])) {
            ((BatchDataMap) (mem.getDataCarte(id))).setCarteActive(jSlider1.getValue(), jSlider2.getValue());
            MajNom();
            monCanvas.processImage();
            monCanvas.repaint();
        }
    }//GEN-LAST:event_ChangeActiveCard

    private void ZoomArriere(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZoomArriere
        monCanvas.zoomArriere();
        if (monCanvasRet != null) {
            monCanvasRet.zoomArriere();
        }
    }//GEN-LAST:event_ZoomArriere

    private void ZoomAvant(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZoomAvant
        monCanvas.zoomAvant();
        if (monCanvasRet != null) {
            monCanvasRet.zoomAvant();
        }
    }//GEN-LAST:event_ZoomAvant

    private void Vue2DResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_Vue2DResized
        MajTaille();
    }//GEN-LAST:event_Vue2DResized

    public int getProfActive() {
        return jSlider2.getValue();
    }

    public CanvasMap getMonCanvas() {
        return monCanvas;
    }

    public CanvRet getMonCanvasRet() {
        return monCanvasRet;
    }

    public void EnableTracking() {
        this.jButton3.setEnabled(true);
        this.jToggleButtonRetentionMap.setEnabled(true);
        this.jRadioButton1.setEnabled(true);
        this.jRadioButton2.setEnabled(true);
    }

    public void enableGrid(boolean b) {
        gridSlider.setEnabled(b);
    }

    public void MajTaille() {
        monCanvas.setupSize(jScrollPane2D.getWidth(), jScrollPane2D.getHeight());
        if (monCanvasRet != null) {
            monCanvasRet.setupSize(jScrollPane2D.getWidth(), jScrollPane2D.getHeight());
        }
    }

    private void DemarrerDetection(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DemarrerDetection
        mem.appliquerParametres(id);
        mem.demarrer(id);
    }//GEN-LAST:event_DemarrerDetection

    public CanGen getActivePane() {
        return (CanGen) ((javax.swing.JScrollPane) (jTabbedPaneCanvas.getSelectedComponent())).getViewport().getView();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JSlider gridSlider;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenu jMenuExport;
    private javax.swing.JMenuItem jMenuItemExportDetection;
    private javax.swing.JMenuItem jMenuItemSaveAllImages;
    private javax.swing.JMenuItem jMenuItemSaveCurrentImage;
    private javax.swing.JMenuItem jMenuItemSaveRetention;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane2D;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JSlider jSlider2;
    protected javax.swing.JTabbedPane jTabbedPaneCanvas;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButtonRetentionMap;
    protected javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
