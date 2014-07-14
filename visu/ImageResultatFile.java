/*
 * ImageResultatFile.java
 *
 * Created on 17 octobre 2002, 01:00
 */
package visu;

import java.awt.*;
import javax.imageio.*;

import java.awt.image.*;
import java.io.*;

public class ImageResultatFile extends Thread implements constants.couleur {

    protected Memoire mem;
    protected int id;
    // coef de qualite : best = 1.0f , top=0.75f , mid = 0.5f , less = 0.25f
    private final float JPEG_QUALITY = 1.0f;
    /**
     * compteur cpt pour enregistrer images a la volee !
     */
    private long cpt = 0;
    private boolean batch;
    private javax.swing.ProgressMonitor prog;

    public ImageResultatFile(Memoire mem, int id, boolean b, javax.swing.ProgressMonitor p) {
        this.mem = mem;
        this.id = id;
        cpt = 0;
        batch = b;
        prog = p;
    }

    public void run() {
        javax.swing.JFileChooser F = new javax.swing.JFileChooser(new java.io.File(mem.getDataCarte(id).getNomFichier()).getParent());
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

        String rad = mem.getDataCarte(id).getNomFichier();
        String suff = ".jpg", img = "img.jpg";
        long i = cpt;
        File fimg = null;
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

        BufferedImage bi = new BufferedImage(ipw + ilw, java.lang.Math.max(iph, ilh), ((BufferedImage) iPlan).getType());

        Graphics2D gra = bi.createGraphics();
        gra.setColor(COLOR_OCEAN);
        gra.clearRect(0, 0, ipw + ilw, java.lang.Math.max(iph, ilh));
        gra.fillRect(0, 0, ipw + ilw, java.lang.Math.max(iph, ilh));

        gra.drawImage(iPlan, 0, 0, ipw, iph, mem.getFrmVisu(id));
        gra.setColor(COLOR_INFO);
        //gra.drawString(mem.getDataCarte(id).getNomFichier(),0,iph);

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
            // Create sample buffered image
            //final BufferedImage bufferedImage = (BufferedImage) i;
            // Initialize output stream
            FileOutputStream out = new FileOutputStream(filename);
            // Encode in JPEG
            //mem.getFrmVisu(id).setStatusBar("capture en cours ...");
            System.out.println("Processing capture...");

            ImageIO.write(bufferedImage, "jpeg", out);
            System.out.println("Encoding...");
            out.close();
            System.out.println("Capture finished ");

        } catch (IOException e) { //mem.getFrmVisu(id).setStatusBar("Erreur pendant la capture !",COLOR_ERROR);
            System.out.println("ImageResultatFile : save image exception  " + e);
            e.printStackTrace();

        }
    }
}
/*
 protected synchronized void sauverImage(Image i, String fn) {
 String fn0 = this.fileName;
 this.fileName = new String(fn);
 sauverImage(i);
 this.fileName = new String(fn0);
 fn0=null;
 }
     
     
 public synchronized void sauverEncoreJPanel(javax.swing.JPanel p) {
     
 Image i = p.createImage(p.getWidth(),p.getHeight());
 sauverImage(i,new String(radical + Integer.toString(cpt) + ext ));
 cpt++;
 }
 public synchronized void sauverEncoreCanvasCarte(CanvasCarte c) {
     
 Image i = c.createImage(c.getWidth(),c.getHeight());
 sauverImage(i,new String(radical + Integer.toString(cpt) + ext ));
 cpt++;
 }
     
 public synchronized void sauverJPanel(javax.swing.JPanel p, String fn) throws Exception {
 if (fn==null) throw new Exception(" ImageResultatFile : erreur de creation : nom de fichier null ");
 if(fn=="") throw  new Exception(" ImageResultatFile : erreur de creation : nom de fichier vide ");
 fileName= fn;
 this.sauverJPanel(p);
 }
     
 public synchronized void sauverJPanel(javax.swing.JPanel p) {
 Image i = p.createImage(p.getWidth(),p.getHeight());
 sauverImage(i);
 }
     
 */
