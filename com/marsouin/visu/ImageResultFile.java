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

import java.awt.*;
import javax.imageio.*;

import java.awt.image.*;
import java.io.*;
import static java.lang.Math.max;
import static javax.imageio.ImageIO.write;

public class ImageResultFile extends Thread implements com.marsouin.constants.Colors {

    protected Memory mem;
    protected int id;
    // coef de qualite : best = 1.0f , top=0.75f , mid = 0.5f , less = 0.25f
    private final float JPEG_QUALITY = 1.0f;
    /**
     * compteur cpt pour enregistrer images a la volee !
     */
    private long cpt = 0;
    private final boolean batch;
    private final javax.swing.ProgressMonitor prog;

    public ImageResultFile(Memory mem, int id, boolean b, javax.swing.ProgressMonitor p) {
        this.mem = mem;
        this.id = id;
        cpt = 0;
        batch = b;
        prog = p;
    }

    @Override
    public void run() {
        javax.swing.JFileChooser F = new javax.swing.JFileChooser(new java.io.File(mem.getDataCarte(id).getFileName()).getParent());
        F.setDialogTitle(" Choose the name of your image file ");
        // F.setFileFilter( new FileFilter().accept(new File("*.jp*")));
        int returnVal = F.showSaveDialog(mem.getFrmVisu(id));
        try {
            if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                if (!batch) {
                    prog.close();
                    this.saveImage(this.getCaptureFinale(),
                            F.getSelectedFile().getAbsolutePath() + ".jpg");

                } else {
                    prog.setMaximum(mem.getBatchDataCarte(id).getNbDataCartesProf() * mem.getBatchDataCarte(id).getNbDataCartesTps() - 1);
                    for (int i = 0; i < mem.getBatchDataCarte(id).getNbDataCartesProf(); i++) {
                        for (int j = 0; j < mem.getBatchDataCarte(id).getNbDataCartesTps(); j++) {
                            mem.getBatchDataCarte(id).setCarteActive(j, i);
                            mem.getFrmVisu(id).getMonCanvas().processImage();
                            this.saveImage(this.getCaptureFinale(), F.getSelectedFile().getAbsolutePath() + "." + mem.getBatchDataCarte(id).getCarteActive().getDate() +/*"."+(i*mem.getBatchDataCarte(id).getNbDataCartesTps()+j)+*/ ".jpg");
                            prog.setNote("Saving image: " + F.getSelectedFile().getAbsolutePath() + "." + mem.getBatchDataCarte(id).getCarteActive().getDate() + ".jpg");
                            prog.setProgress(i * mem.getBatchDataCarte(id).getNbDataCartesTps() + j);
                        }
                    }
                }
                prog.close();
            } else {
                prog.close();
            }

        } catch (Exception e) {
            String msg = " ImageResultatFile: erreur d'ecriture de la capture ! " + e;
            e.printStackTrace();
            //mem.getFrmVisu(id).setStatusBar(msg,COLOR_ERROR);
            System.out.println(msg);
        }
        this.interrupt();
    }

    public void sauverSystematiquement() {

        String rad = mem.getDataCarte(id).getFileName();
        String suff = ".jpg", img = "img.jpg";
        long i = cpt;
        File fimg;
        boolean fileHere = true;
        while (fileHere) {
            fileHere = false;
            suff = "_" + Long.toString(i) + ".jpg";
            img = rad + suff;
            try {
                fimg = new File(img);
                fileHere = fimg.exists();
                if (fileHere) {
                    i++;
                }
                fimg = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        cpt = i;

        try {
            this.saveImage(this.getCaptureFinale(), img);

        } catch (Exception e) {
            String msg = " ImageResultatFile: erreur d'ecriture de la capture systematique ! " + e;
            e.printStackTrace();
            //mem.getFrmVisu(id).setStatusBar(msg,COLOR_ERROR);
            System.out.println(msg);
        }

        System.out.println("++  sauvegarde  OK dans " + img);
    }

    /*
     protected synchronized void sauverEncoreImage(Image i) {
     sauverImage(i,new String(radical + Integer.toString(cpt) + ext ));
     cpt++;
     }
     */
    private Image getCaptureFinale() {
        Image iPlan = mem.getFrmVisu(id).getActivePane().getImage();
        //Image iPlan = mem.getFrmVisu(id).getMyImage();
        int ipw = mem.getFrmVisu(id).getMonCanvas().getWidth();
        int iph = mem.getFrmVisu(id).getMonCanvas().getHeight();

        Image iLgd = null;
        int ilw = 0;
        int ilh = 0;

        BufferedImage bi = new BufferedImage(ipw + ilw, max(iph, ilh), ((BufferedImage) iPlan).getType());

        Graphics2D gra = bi.createGraphics();
        gra.setColor(COLOR_OCEAN);
        gra.clearRect(0, 0, ipw + ilw, max(iph, ilh));
        gra.fillRect(0, 0, ipw + ilw, max(iph, ilh));

        gra.drawImage(iPlan, 0, 0, ipw, iph, mem.getFrmVisu(id));
        gra.setColor(COLOR_INFO);
        //gra.drawString(mem.getDataCarte(id).getFileName(),0,iph);

        if (iLgd != null) {
            gra.drawImage(iLgd, ipw, 0, ilw, ilh, mem.getFrmVisu(id));
        }

        return bi;

    }

    protected synchronized void saveImage(Image i, String filename) {
        this.saveImage((BufferedImage) i, filename);
    }

    protected synchronized void saveImage(BufferedImage bufferedImage, String filename) {
        try {
            // Encode in JPEG
            //mem.getFrmVisu(id).setStatusBar("capture en cours ...");
            try ( // Create sample buffered image
            //final BufferedImage bufferedImage = (BufferedImage) i;
            // Initialize output stream
                    FileOutputStream out = new FileOutputStream(filename)) {
                // Encode in JPEG
                //mem.getFrmVisu(id).setStatusBar("capture en cours ...");
                System.out.println("Processing capture...");
                
                write(bufferedImage, "jpeg", out);
                System.out.println("Encoding...");
            }
            System.out.println("Capture finished ");

        } catch (IOException e) { //mem.getFrmVisu(id).setStatusBar("Erreur pendant la capture !",COLOR_ERROR);
            System.out.println("ImageResultatFile : save image exception  " + e);
            e.printStackTrace();

        }
    }
}
