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
/**
 * Classe d'affichage du graphisme : afficher les vecteurs, un fond, les
 * informations resultats , animer les resultats pour afficher un maximum
 * d'infos
 */
package com.marsouin.visu;

import com.marsouin.data.VortexAnt;
import com.marsouin.data.BatchDataMap;
import com.marsouin.data.DataMap;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import static java.lang.Thread.yield;
import java.util.ArrayList;
import static java.util.ResourceBundle.getBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CanvRet extends CanGen implements com.marsouin.constants.Centre, com.marsouin.constants.Colors, com.marsouin.constants.Ants, com.marsouin.constants.Stream, javax.swing.Scrollable {

    private static final Logger log = Logger.getLogger(CanvRet.class.getName());
    private ThreadAnime threadAnime = null;

    private Graphics2D myGraphics = null;

    private int[][][] gradient = null;
    private int vSeul = 0;
    private double[][] retention = null;

    public CanvRet(Memory m, int moi) {
        mem = m;
        id = moi;
        log.setLevel(log.getParent().getLevel());
        // taille par defaut
        carteWidth = 600;
        carteHeight = 600;

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onMouseClicked(evt);
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(carteWidth, carteHeight);
    }

    public void setupSize(int x, int y) {

        int w = x;
        int h = y;
        int tX = mem.getDataCarte(id).getXSize();
        int tY = mem.getDataCarte(id).getYSize();
        int largeurCase = w / tX;
        int hauteurCase = h / tY;

        while ((largeurCase < MIN_LARGEUR_CASE) | (hauteurCase < MIN_HAUTEUR_CASE)) {
            w = (largeurCase < MIN_LARGEUR_CASE) ? ((w * 11) / 10) : w;
            h = (hauteurCase < MIN_HAUTEUR_CASE) ? ((h * 11) / 10) : h;
            largeurCase = w / tX;
            hauteurCase = h / tY;
        }

        carteWidth = largeurCase * tX;
        carteHeight = hauteurCase * tY;

        this.setSize(carteWidth, carteHeight);
    }

    public void zoomAvant() {
        carteWidth = (carteWidth * 15) / 13;
        carteHeight = (carteHeight * 15) / 13;
        this.setSize(carteWidth, carteHeight);
    }

    public void zoomArriere() {
        carteWidth = (carteWidth * 13) / 15;
        carteHeight = (carteHeight * 13) / 15;
        this.setSize(carteWidth, carteHeight);
    }

    public void processImage() {
        int w = carteWidth;
        int h = carteHeight;
        myImage = (BufferedImage) this.createImage(w, h);
        if (myImage == null) {
            System.out.println(getBundle("ressources/canvas").getString("CanvasCarte_:_ERREUR:_myImage=null"));
        }

        myGraphics = myImage.createGraphics();
        myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_SPEED);
        myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_COLOR_RENDERING, java.awt.RenderingHints.VALUE_COLOR_RENDER_SPEED);
        myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_FRACTIONALMETRICS, java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_DISABLE);

        if (mem.getAntialias(id)) {
            // Enable antialiasing for shapes
            myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);

            myGraphics.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }

        //************* FOND **************
        afficheTerre(myGraphics);
        afficheRet(myGraphics);

        //************* VECTEURS ***********
        afficheVecteurs(myGraphics);

        try {

            //Affichage latitude/longitude
            afficheGrille(myGraphics);

        } catch (Exception e) {
           log.log(Level.SEVERE,"Error in processing image",e);
        }
    }

    public Color[] getPalettePhys() {
        return COLOR_CENTRE_PHYSIQUE;
    }

    //@@@@@@@@@@@@@@@@@ LES FLECHES @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * affiche les fleches qui repr�sentent le Stream moyen en chaque point
     * d'estimation de la carte
     */
    private void afficheVecteurs(Graphics2D gra) {

        int X1, Y1, X2, Y2;
        int tX = mem.getDataCarte(id).getXSize();
        int tY = mem.getDataCarte(id).getYSize();
        int largeurCase = carteWidth / tX;
        int hauteurCase = carteHeight / tY;

        double offsetX = ((double) largeurCase) * COEFF_AFFICHAGE_COURANT;
        double offsetY = ((double) hauteurCase) * COEFF_AFFICHAGE_COURANT;

        gra.setColor(COLOR_VECTEUR);
        for (int i = 0; i < tX; i++) {
            for (int j = 0; j < tY; j++) {
                if (!mem.getBatchDataCarte(id).getC(i, j).getSurTerre()) {
                    X1 = (i * largeurCase + largeurCase / 2);
                    Y1 = ((tY - j - 1) * hauteurCase + hauteurCase / 2);
                    X2 = X1 + (int) (mem.getBatchDataCarte(id).getMoyenne(i, j, mem.getFrmVisu(id).getProfActive())[0] * offsetX);
                    Y2 = Y1 - (int) (mem.getBatchDataCarte(id).getMoyenne(i, j, mem.getFrmVisu(id).getProfActive())[1] * offsetY);
                    afficheFleche(gra, X1, Y1, X2, Y2);
                    //System.out.println("coords:"+mem.getDataCarte(id).getC(i,j).getXNorm());
                }
            }
        }
    }

    public Color getPaletteFond(float valeur) {
        int r = 0, g = 0, b = 0, a = 0;
        int indic;

        switch (TYPE_DEGRADE) {
            case NOIR_BLANC_NOIR:
                // black 0 0 0  white 255 255 255 black
                if (valeur < 0f) {
                    valeur = -valeur;
                }
                r = (int) (254f * valeur);
                g = r;
                b = r;
                a = 255;
                break;
            case BLEU_BLANC_NOIR:
                // blue : 0 0 255 white 255 255 255 black 0 0 0
                if (valeur > 0f) {
                    indic = 1;
                } else {
                    indic = -1;
                }
                if (indic < 0) {
                    valeur = -valeur;
                    r = 255 - (int) (valeur * 254f);
                    g = r;
                    b = 255;
                    a = 255;
                } else {
                    r = 255 - (int) (valeur * 254f);
                    g = r;
                    b = r;
                    a = 255;
                }
                break;
            case BLEU_BLANC_VERT:
                // blue : 0 0 255 white 255 255 255 green 0 255 0
                if (valeur > (float) -0.0000001) {
                    indic = 1;
                } else {
                    indic = -1;
                }
                if (indic < 0) {
                    valeur = -valeur;
                    r = 255 - (int) (valeur * 254f);
                    g = r;
                    b = 255;
                    a = 255;
                } else {
                    r = 255 - (int) (valeur * 254f);
                    g = 255;
                    b = r;
                    a = 255;
                }
                break;
            case ROUGE_BLANC_BLEU:
                // red 255 0 0  white 255 255 255 blue : 0 0 255
                if (valeur > 0f) {
                    indic = 1;
                } else {
                    indic = -1;
                }
                if (indic < 0) {
                    valeur = -valeur;
                    r = 255;
                    g = 255 - (int) (valeur * 254f);
                    b = g;
                    a = 255;
                } else {
                    r = 255 - (int) (valeur * 254f);
                    g = r;
                    b = 255;
                    a = 255;
                }
                break;
            case BLEU_GRIS_NOIR:
                // blue : 0 0 255 grisclair 220 220 220 black 0 0 0
                if (valeur > 0f) {
                    indic = 1;
                } else {
                    indic = -1;
                }
                if (indic < 0) {
                    valeur = -valeur;
                    r = 220 - (int) (valeur * 219f);
                    g = r;
                    b = 220 + (int) (valeur * 34f);
                    a = 255;
                } else {
                    r = 220 - (int) (valeur * 219f);
                    g = r;
                    b = r;
                    a = 255;
                }
                break;

            default: {
            }
        }

        return new Color(r, g, b, a);
    }

    public void clearRet() {
        gradient = null;
    }

    public void processRet(boolean clock) {
        gradient = new int[mem.getBatchDataCarte(id).getXSize()][mem.getBatchDataCarte(id).getYSize()][3];
        retention = new double[mem.getBatchDataCarte(id).getXSize()][mem.getBatchDataCarte(id).getYSize()];
        javax.swing.ProgressMonitor prog = new javax.swing.ProgressMonitor(mem.getFrmVisu(id), "Processing retention...", "Processing...", 0, 0);
        prog.setMaximum((mem.getBatchDataCarte(id).getXSize() * mem.getBatchDataCarte(id).getYSize() - mem.getBatchDataCarte(id).getNbOnGround()) * mem.getBatchDataCarte(id).getNbDataCartesTps());
        ProcessRetentionThread prt = new ProcessRetentionThread(mem, gradient, retention, id, prog, clock);
        prt.start();
    }

    private void afficheRet(Graphics2D gra) {

        if (gradient != null) {
            int X1, Y1, X2, Y2;
            int tX = mem.getDataCarte(id).getXSize();
            int tY = mem.getDataCarte(id).getYSize();
            int largeurCase = carteWidth / tX;
            int hauteurCase = carteHeight / tY;

            for (int x = 0; x < tX; x++) {
                for (int y = 0; y < tY; y++) {
                    if (!mem.getDataCarte(id).getC(x, y).getSurTerre()) {
                        int sum = 0;
                        X1 = (x * largeurCase);
                        Y1 = ((tY - y - 1) * hauteurCase);
                        gra.setColor(new java.awt.Color(gradient[x][y][0], gradient[x][y][1], gradient[x][y][2]));
                        gra.fillRect(X1, Y1, largeurCase, hauteurCase);
                    }
                }
            }
        }
    }

    private void afficheTerre(Graphics2D gra) {

        int X1, Y1;
        int tX = mem.getDataCarte(id).getXSize();
        int tY = mem.getDataCarte(id).getYSize();
        int largeurCase = carteWidth / tX;
        int hauteurCase = carteHeight / tY;
        gra.setColor(COLOR_TERRE);
        for (int i = 0; i < tX; i++) {
            for (int j = 0; j < tY; j++) {
                if (mem.getDataCarte(id).getC(i, j).getSurTerre()) {
                    X1 = (i * largeurCase);
                    Y1 = ((tY - j - 1) * hauteurCase);
                    gra.fillRect(X1, Y1, largeurCase, hauteurCase);
                }
            }
        }
    }

    @Override
    public void setSize(int param, int param1) {

        carteWidth = param;
        carteHeight = param1;
        if (mem.getFlagAffich(id)[AFF_FOND] != AFF_RIEN) {
            // $$$$$$$$*******Utiliser une interpolation******�������������������������$$$$$$$$$$$$$$$$$$$$$$$$$$
            if (AFFICHER_FOND_AVEC_INTERPOLATION_GRAND_TABLEAU) {
                mem.getDataCarte(id).getTable().changeTaille(carteWidth / DENOMINATEUR_TAILLE_INTERPOLATION, carteHeight / DENOMINATEUR_TAILLE_INTERPOLATION);
                mem.getDataCarte(id).getTable().normaliserGrandTab();
            } else {
                mem.getDataCarte(id).getTable().normaliserTab();
            }

        }
        super.setSize(carteWidth, carteHeight);
        processImage();
    }

    @Override
    public void setSize(Dimension dimension) {
        this.setSize(dimension.width, dimension.height);
    }

    //************************************************************************************************************
    public void onMouseClicked(java.awt.event.MouseEvent evt) {
        // ATTENTION
        int w = this.getWidth();  //carteWidth
        int h = this.getHeight(); //carteHeight
        // CLIC GAUCHE >>>>>>>>> MONTRER UN POINT : afficher 9 vecteurs en autre Colors
        if (!evt.isAltDown() & !evt.isControlDown() & !evt.isShiftDown() & !evt.isMetaDown()) {
            DataMap dc = mem.getDataCarte(id);
            int k, m;
            int X1, Y1, X2, Y2;
            int largeurCase = w / dc.getXSize();
            int hauteurCase = h / dc.getYSize();
            int i = (evt.getX()) / largeurCase;
            int j = dc.getYSize() - 1 - (evt.getY()) / hauteurCase;

            if (((i < dc.getXSize()) & (j < dc.getYSize())) & ((i > -1) & (j > -1))) {
                // on est dans les bornes de la carte

                double offsetX = (largeurCase / OFFSET_V);
                double offsetY = (hauteurCase / OFFSET_V);
                boolean okPlus = false, okX = false, okCarre = false;
                boolean[][] matrice;
                // recherche si plus :
                for (m = TYPE_PLUS; m < (NB_METHODES * NB_TYPES); m += NB_TYPES) {
                    okPlus = (okPlus | (dc.getC(i, j).getCfgCentre(m)));
                }
                // recherche si X :
                for (m = TYPE_X; m < (NB_METHODES * NB_TYPES); m += NB_TYPES) {
                    okX = (okX | (dc.getC(i, j).getCfgCentre(m)));
                }
                // recherche si O :
                for (m = TYPE_O; m < (NB_METHODES * NB_TYPES); m += NB_TYPES) {
                    okPlus = (okPlus | (dc.getC(i, j).getCfgCentre(m)));
                    okX = (okX | (dc.getC(i, j).getCfgCentre(m)));
                }
                // recherche si CARRE :
                for (m = TYPE_CARRE; m < (NB_METHODES * NB_TYPES); m += NB_TYPES) {
                    okCarre = (okCarre | (dc.getC(i, j).getCfgCentre(m)));
                }

                matrice = new boolean[3][3];
                for (k = 0; k < 3; k++) {
                    for (m = 0; m < 3; m++) {
                        if (!((okPlus | okX) | okCarre)) {
                            matrice[k][m] = true;
                        } else {
                            matrice[k][m] = false;
                            matrice[k][m] = (matrice[k][m]) | ((((m + k) % 2) == 0) & okX);
                            matrice[k][m] = (matrice[k][m]) | ((((m + k) % 2) == 1) & okPlus);
                            matrice[k][m] = (matrice[k][m]) | (((m == 1) & (k == 1)) & okPlus);
                            matrice[k][m] = (matrice[k][m]) | (((m > 0) & (k > 0)) & okCarre);
                        }
                    }
                }
                if (!((okPlus | okX) | okCarre)) {
                    myGraphics.setColor(COLOR_VECTEUR_CLICK);
                } else {
                    myGraphics.setColor(COLOR_C_CLICK);
                }
                for (k = -1; k < 2; k++) {
                    for (m = -1; m < 2; m++) {
                        // prend en charge le debordement !
                        try {
                            if (!matrice[m + 1][k + 1]) {
                                throw new Exception(getBundle("ressources/canvas").getString("_affichage_non_demamd�_"));
                            }
                            //if((m|k)==0) throw new Exception("on n affiche pas le vecteur du Centre !");
                            X1 = ((i + m) * largeurCase + largeurCase / 2);
                            Y1 = ((dc.getYSize() - (j + k) - 1) * hauteurCase + hauteurCase / 2);
                            X2 = X1 + (int) (dc.getC(i + m, j + k).getXBase() * offsetX);
                            Y2 = Y1 - (int) (dc.getC(i + m, j + k).getYBase() * offsetY);
                            afficheFleche(myGraphics, X1, Y1, X2, Y2);
                        } catch (Exception e) {
                        }
                    }
                }

            }
            this.getGraphics().drawImage(myImage, 0, 0, w, h, this);
        } // gauche + ctrl >>>>> POINTER
        else if (!evt.isAltDown() & evt.isControlDown() & !evt.isShiftDown() & !evt.isMetaDown()) {
            myGraphics.setColor(COLOR_C_CLICK);
            myGraphics.fillRect(evt.getX() - 3, evt.getY() - 3, 6, 6);
            this.getGraphics().drawImage(myImage, 0, 0, w, h, this);
        } // milieu >> RAFRAICHIR
        else if (evt.isAltDown() & !evt.isControlDown() & !evt.isShiftDown() & !evt.isMetaDown()) {
            log.info(getBundle("ressources/canvas").getString("bouton_du_milieu"));
        } // milieu + ctrl >> animation
        else if (evt.isAltDown() & evt.isControlDown() & !evt.isShiftDown() & !evt.isMetaDown()) {

        } // droit >>>>> Propri�t�s
        else if (!evt.isAltDown() & !evt.isControlDown() & !evt.isShiftDown() & evt.isMetaDown()) {
            DataMap dc = ((BatchDataMap) mem.getDataCarte(id)).getCarteActive();
            int largeurCase = w / dc.getXSize();
            int hauteurCase = h / dc.getYSize();
            int i = (evt.getX()) / largeurCase;
            int j = dc.getYSize() - 1 - (evt.getY()) / hauteurCase;
            vSeul = 0;
            ArrayList<VortexAnt> collect = dc.getVortexAnt();
            if ((collect != null) && (collect.size() > 0)) {
                while ((vSeul < collect.size()) && (!collect.get(vSeul).contains(i, j))) {
                    vSeul++;
                }
                if (vSeul < collect.size()) {
                    javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
                    javax.swing.JMenuItem menuProp = new javax.swing.JMenuItem(getBundle("ressources/canvas").getString("Proprietes_vortex_num_") + collect.get(vSeul).getNum());
                    javax.swing.JMenuItem menuAffSeul = new javax.swing.JMenuItem(getBundle("ressources/canvas").getString("Afficher_seul"));
                    javax.swing.JMenuItem menuAffTout = new javax.swing.JMenuItem(getBundle("ressources/canvas").getString("Afficher_tout"));
                    javax.swing.JMenuItem menuChgColor = new javax.swing.JMenuItem(getBundle("ressources/canvas").getString("Changer_la_couleur"));
                    menuChgColor.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            afficherChgColor(vSeul);
                        }
                    });
                    menuProp.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            afficherProp(vSeul);
                        }
                    });
                    menuAffSeul.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            afficherSeul();
                        }
                    });
                    menuAffTout.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseReleased(java.awt.event.MouseEvent evt) {
                            afficherTout();
                        }
                    });
                    popup.add(menuProp);
                    popup.add(new javax.swing.JPopupMenu.Separator());
                    popup.add(menuAffSeul);
                    popup.add(menuAffTout);
                    popup.add(menuChgColor);
                    popup.show(evt.getComponent(), evt.getX(), evt.getY());
                }
            }
        }

    }

    public void afficherSeul() {
        BatchDataMap d;
        d = mem.getBatchDataCarte(id);
        for (int i = 0; i < d.getNbDataCartesTps(); i++) {
            for (int j = 0; j < d.getDataCarte(i, 0).getVortexAnt().size(); j++) {
                if (d.getDataCarte(i, 0).getVortexAnt().get(j).getAffiche()) {
                    d.getDataCarte(i, 0).getVortexAnt().get(j).setAfficheSuiv(false);
                }
            }
        }
        d = mem.getBatchDataCarte(id);

        d.getVortexAnt().get(vSeul).setAfficheSuiv(true);
        d.getVortexAnt().get(vSeul).setAffichePrec(true);

        repaint();
    }

    public void afficherTout() {
        BatchDataMap d;
        d = mem.getBatchDataCarte(id);
        for (int i = 0; i < d.getNbDataCartesTps(); i++) {
            for (int j = 0; j < d.getDataCarte(i, 0).getVortexAnt().size(); j++) {
                if (!d.getDataCarte(i, 0).getVortexAnt().get(j).getAffiche()) {
                    d.getDataCarte(i, 0).getVortexAnt().get(j).setAfficheSuiv(true);
                }
            }
        }
        repaint();
    }

    void afficherChgColor(int vSeul) {
        BatchDataMap d = mem.getBatchDataCarte(id);
        CustomColorChooser cc = new CustomColorChooser();
        cc.setVisible(true);
        if (cc.ok) {
            d.getVortexAnt().get(vSeul).setCouleurPrec(cc.getCouleur());
            d.getVortexAnt().get(vSeul).setCouleurSuiv(cc.getCouleur());
            this.repaint();
        }
    }

    public void afficherProp(int num) {
        DataMap d = mem.getBatchDataCarte(id).getCarteActive();
        VortexProperties p = new VortexProperties(d.getVortexAnt().get(vSeul).getNum());
        p.setAire(d.getVortexAnt().get(vSeul).getAire());
        p.setRayonMoyen(d.getVortexAnt().get(vSeul).getRayonMoyen());
        p.setPeriode(d.getVortexAnt().get(vSeul).getPeriod(mem.getDataCarte(id).getOcean()));

        double[] tmp = mem.getBatchDataCarte(id).getLonLat(d.getVortexAnt().get(vSeul).getBaricentre()[0], d.getVortexAnt().get(vSeul).getBaricentre()[1]);
        p.setCentre(tmp[0], tmp[1]);
        p.setVisible(true);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(carteWidth, carteHeight);
    }

    @Override
    public int getScrollableBlockIncrement(java.awt.Rectangle rectangle, int param, int param2) {
        return 50;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override
    public int getScrollableUnitIncrement(java.awt.Rectangle rectangle, int param, int param2) {
        return 8;
    }

    @Override
    public java.awt.Graphics getGraphics() {
        java.awt.Graphics retValue;
        retValue = super.getGraphics();
        return retValue;
    }

    public int[] getCVprops() {
        int[] ret = new int[LENGTH_CV_PROPS];

        DataMap mer = mem.getDataCarte(id);
        int tX = mer.getXSize();
        ret[CV_TAILLE_Y] = mer.getYSize();
        ret[CV_CASE_WIDTH] = carteWidth / tX;
        ret[CV_CASE_HEIGHT] = carteHeight / ret[CV_TAILLE_Y];

        return ret;
    }

    public int[] getDimension() {
        int[] ret = {carteWidth, carteHeight};
        return ret;

    }

    public double[][] getRetention() {
        return retention;
    }

}
