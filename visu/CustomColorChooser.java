/*
 * CustomColorChooser.java
 *
 * Created on 5 avril 2005, 21:12
 */

package visu;

/**
 *
 * @author  Marc Segond
 */
public class CustomColorChooser extends javax.swing.JDialog {
    
    public boolean ok = false;
    
    /** Creates new form CustomColorChooser */
    public CustomColorChooser() {
        initComponents();
        this.setModal(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        SelecteurCouleur = new javax.swing.JColorChooser();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Choisissez la couleur");
        setResizable(false);
        SelecteurCouleur.setDoubleBuffered(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(SelecteurCouleur, gridBagConstraints);

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jButton1, gridBagConstraints);

        jButton2.setText("Annuler");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnnulClicked(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jButton2, gridBagConstraints);

        pack();
    }//GEN-END:initComponents

    private void AnnulClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnnulClicked
        this.setVisible(false);
    }//GEN-LAST:event_AnnulClicked

    private void OKClicked(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKClicked
        ok = true;
        this.setVisible(false);
    }//GEN-LAST:event_OKClicked
    
    public java.awt.Color getCouleur(){
        return SelecteurCouleur.getColor();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JColorChooser SelecteurCouleur;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    // End of variables declaration//GEN-END:variables
    
}