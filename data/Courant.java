/*
 * Courant.java
 *
 * Created on 10 septembre 2002, 14:26
 */

/*
 * @author Mahler, Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */
package data;

import java.util.ArrayList;

public class Courant implements constants.centre, constants.fourmi, constants.courant {

    // coordonnees du vecteur initial.
    private double XBase;
    private double YBase;

    private double XNorm;
    private double YNorm;

    // coordonnees de travail.
    public double X;
    public double Y;

    // coordonnees du vecteur unitaire de travail.
    public double uX;
    public double uY;

    private int repH;
    private int repV;

    // norme du vecteur
    public double norme;

    private boolean surTerre;
    private boolean[] cfgCentre;

    float bat;

    float sal;

    float temp;

    // FOURMIS
    private ArrayList pheromone;

    // VORTEX
    private boolean isVortex;

    private double curl = 0.0;

    private double div = 0.0;

    /**
     * Creates a new instance of Courant
     */
    public Courant() {
        XBase = 0.0;
        YBase = 0.0;
        X = 0.0;
        Y = 0.0;
        XNorm = 0d;
        YNorm = 0d;
        uX = 0.0;
        uY = 0.0;
        repH = 0;
        repV = 0;
        this.norme = 0.0;
        sal = 0f;
        bat = 0f;
        temp = 0f;

        this.iniBool();
        this.calculCU(1, 0);
        pheromone = null;
    }

    public Courant(double xx, double yy) {
        XBase = xx;
        YBase = yy;
        X = xx;
        Y = yy;
        repH = 0;
        repV = 0;
        sal = 0f;
        bat = 0f;
        temp = 0f;

        this.calculNorme();
        // calculer les coord dans de repere de base
        this.calculCU(1, 0);

        this.iniBool();

        double i = Math.sqrt(1 / (xx * xx + yy * yy));
        XNorm = xx * i;
        YNorm = yy * i;

        pheromone = null;
    }

    public Courant(double xx, double yy, float s, float b, float t) {
        XBase = xx;
        YBase = yy;
        X = xx;
        Y = yy;
        repH = 0;
        repV = 0;
        sal = s;
        temp = t;
        bat = b;

        this.calculNorme();
        // calculer les coord dans de repere de base
        this.calculCU(1, 0);

        this.iniBool();

        double i = Math.sqrt(1 / (xx * xx + yy * yy));
        XNorm = xx * i;
        YNorm = yy * i;

        pheromone = null;
    }

    private void iniBool() {
        int i;
        cfgCentre = new boolean[LENGTH_CFGCENTRE];
        for (i = 0; i < cfgCentre.length; i++) {
            cfgCentre[i] = false;
        }
        surTerre = false;
        isVortex = false;
    }

    //************************************FOURMIS
    public void resetPheromone(int nbF, int nbE) {
        pheromone = new ArrayList();
        for (int i = 0; i < nbE; i++) {
            double[] tmp = new double[nbF];
            for (int j = 0; j < nbF; j++) {
                tmp[j] = 0.0;
            }
            pheromone.add(tmp);
        }
    }

    public ArrayList getPheromone() {
        return pheromone;
    }

    public double getPheromone(int i) {
        double res = 0d;

        for (int j = 0; j < ((double[]) (pheromone.get(i))).length; j++) {
            res += ((double[]) (pheromone.get(i)))[j];
        }
        return res;
    }

    public double getPheromone(int i, int j) {
        return ((double[]) (pheromone.get(i)))[j];
    }

    public double getPheromoneTotale() {
        double ph = 0.0;
        for (Object pheromone1 : pheromone) {
            for (int j = 0; j < ((double[]) (pheromone1)).length; j++) {
                ph += ((double[]) (pheromone1))[j];
            }
        }

        return ph;
    }

    public void setPheromone(int i, int j, double value) {
        ((double[]) (pheromone.get(i)))[j] = value;
    }

    public void deposePheromone(int i, int j, double val) {
        ((double[]) (pheromone.get(i)))[j] += val;
    }

    public void evaporePheromone(int i, int j, double coefDEvaporation) {
        if ((coefDEvaporation < 0.0) | (coefDEvaporation > 1.0)) {
            System.out.println(" Courant : coef d'evaporation de la pheromone INCORRECT : doit etre compris entre 0.0 et 1.0" + coefDEvaporation);
        } else {
            ((double[]) (pheromone.get(i)))[j] = (1.0 - coefDEvaporation) * ((double[]) (pheromone.get(i)))[j];
        }
    }

    public void evaporePheromone(double coefDEvaporation) {
        if ((coefDEvaporation < 0.0) | (coefDEvaporation > 1.0)) {
            System.out.println(" Courant : coef d'evaporation de TAB pheromone INCORRECT : doit etre compris entre 0.0 et 1.0" + coefDEvaporation);
        } else {
            for (Object pheromone1 : pheromone) {
                for (int j = 0; j < ((double[]) (pheromone1)).length; j++) {
                    ((double[]) (pheromone1))[j] = (1.0 - coefDEvaporation) * ((double[]) (pheromone1))[j];
                }
            }
        }
    }

    public boolean hasPheromone() {
        boolean ret = false;
        ret = (pheromone != null);
        if (ret) {
            double ph = 0.0;
            ph = this.getPheromoneTotale();
            ret = (ph >= PHEROMONE_ABSENTE);
        }
        return ret;
    }

    public boolean hasPheromone(int i, int j) {
        boolean ret = false;
        ret = (pheromone != null);
        if (ret) {
            ret = (((double[]) (pheromone.get(i)))[j] > PHEROMONE_ABSENTE);
        }
        return ret;
    }

    public Courant(Courant c) {
        this.XBase = c.getXBase();
        this.YBase = c.getYBase();
        this.X = c.X;
        this.Y = c.Y;
        this.uX = c.uX;
        this.uY = c.uY;
        this.XNorm = c.XNorm;
        this.YNorm = c.YNorm;
        this.repH = c.getRepH();
        this.repV = c.getRepV();
        this.norme = c.norme;
        this.iniBool();
        if (c.getCfgCentre() != null) {
            this.cfgCentre = (boolean[]) c.getCfgCentre().clone();
        }
        this.surTerre = c.getSurTerre();
        this.isVortex = c.isVortex();
        if (c.getPheromone() != null) {
            this.pheromone = (ArrayList) c.getPheromone().clone();
        }

    }

    public double getXBase() {
        return XBase;
    }

    public double getYBase() {
        return YBase;
    }

    public int getRepH() {
        return repH;
    }

    public int getRepV() {
        return repV;
    }

    public boolean getSurTerre() {
        return surTerre;
    }

    public boolean[] getCfgCentre() {
        return (cfgCentre);
    }

    public boolean getCfgCentre(int i) {
        return cfgCentre[i];
    }

    public void setXBase(double xx) {
        this.XBase = xx;
        this.X = xx;
    }

    public void setYBase(double yy) {
        this.YBase = yy;
        this.Y = yy;
    }

    public void setXNorm(double xx) {
        this.XNorm = xx;
    }

    public void setYNorm(double yy) {
        this.YNorm = yy;
    }

    public void setSurTerre(boolean b) {
        this.surTerre = b;
        if (surTerre) {
            XBase = 0.0;
            YBase = 0.0;
            norme = 0.0;
        }
    }

    public void setCfgCentre(int i, boolean b) {
        cfgCentre[i] = b;
    }

    public void setCfgCentre(int i) {
        cfgCentre[i] = true;
    }

    public void resetCfgCentre() {
        for (int i = 0; i < cfgCentre.length; i++) {
            cfgCentre[i] = false;
        }
    }

    public boolean isVortex() {
        return this.isVortex;
    }

    public void setVortex(boolean isInAVortex) {
        this.isVortex = isInAVortex;
    }

    public final void calculNorme() {
        if (this.isViable()) {
            norme = java.lang.Math.sqrt(XBase * XBase + YBase * YBase);
        } else {
            norme = 0.0;
        }
    }

    public boolean isViable() {
        return ((!this.surTerre) & ((java.lang.Math.abs(XBase) > SEUIL_NORME) | (java.lang.Math.abs(YBase) > SEUIL_NORME)));
    }

    public double calculAngle() {
        double theta = 0.0;
        if (norme > SEUIL_NORME) {
            if (XBase > SEUIL_NORME && YBase > SEUIL_NORME) {
                theta = java.lang.Math.acos(uX);
            }
            if (XBase < -SEUIL_NORME && YBase > SEUIL_NORME) {
                theta = java.lang.Math.PI - (java.lang.Math.acos(java.lang.Math.abs(uX)));
            }
            if (XBase < -SEUIL_NORME && YBase < -SEUIL_NORME) {
                theta = java.lang.Math.PI + (java.lang.Math.acos(java.lang.Math.abs(uX)));
            }
            if (XBase > SEUIL_NORME && YBase < -SEUIL_NORME) {
                theta = (2.0 * java.lang.Math.PI) - java.lang.Math.acos(uX);
            }

            if ((java.lang.Math.abs(XBase) <= SEUIL_NORME) && YBase > SEUIL_NORME) {
                theta = java.lang.Math.PI / 2.0;
            }
            if ((java.lang.Math.abs(XBase) <= SEUIL_NORME) && YBase < -SEUIL_NORME) {
                theta = (3.0 * java.lang.Math.PI) / 2.0;
            }
            if ((java.lang.Math.abs(YBase) <= SEUIL_NORME) && XBase > SEUIL_NORME) {
                theta = 0.0;
            }
            if ((java.lang.Math.abs(YBase) <= SEUIL_NORME) && XBase < -SEUIL_NORME) {
                theta = java.lang.Math.PI;
            }
        } else {
            System.out.println("theta ??");
        }
        return theta;
    }

    public final void calculCU(int decH, int decV) {
        if (this.isViable()) {
            if ((decH == repH) & (decV == repV)) {
                X = XBase;
                Y = YBase;
                uX = X / norme;
                uY = Y / norme;
            } else {
                int pgcdH, pgcdV;
                pgcdH = pgcd(decH, repH);
                pgcdV = pgcd(decV, repV);
                if ((decH / pgcdH == repH / pgcdH) & (decV / pgcdV == repV / pgcdV)) {
                    X = XBase;
                    Y = YBase;
                    uX = X / norme;
                    uY = Y / norme;
                } else {
                    double dtmp = java.lang.Math.sqrt((double) (decH * decH + decV * decV));
                    double cosT, sinT;
                    cosT = (decH == 0) ? 0.0 : (((double) (decH)) / dtmp);
                    sinT = (decV == 0) ? 0.0 : (((double) (decV)) / dtmp);
                    this.X = this.XBase * cosT + this.YBase * sinT;
                    this.Y = -this.XBase * sinT + this.YBase * cosT;
                    uX = X / norme;
                    uY = Y / norme;
                    repH = decH;
                    repV = decV;
                }
            }
        } else {
            X = XBase;
            Y = YBase;
            uX = 0.0;
            uY = 0.0;
        }

    }

    private int pgcd(int a, int b) {
        if ((a < 0) || (b < 0)) {
            System.out.println("Veuillez passer 2 nombres positifs a et b ");
            return 1;
        }
        if ((a == 0) && (b == 0)) {
            return 1;
        }
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }

        int r;
        while (b > 0) {
            r = a % b;
            a = b;
            b = r;
        }
        return a;
    }

    public double getAngle() {
        double res = 0d;
        double YsurX = Math.abs(YBase) / Math.abs(XBase);

        if ((XBase >= 0) && (YBase >= 0)) {
            res = Math.atan(YsurX);
        }
        if ((XBase < 0) && (YBase >= 0)) {
            res = Math.PI - Math.atan(YsurX);
        }
        if ((XBase < 0) && (YBase < 0)) {
            res = Math.PI + Math.atan(YsurX);
        }
        if ((XBase >= 0) && (YBase < 0)) {
            res = (2 * Math.PI) - Math.atan(YsurX);
        }
        return res;
    }

    public double getXNorm() {
        return XNorm;
    }

    public double getYNorm() {
        return YNorm;
    }

    /**
     * donne le rotationnel de ce courant
     */
    public double getCurl() {
        return curl;
    }

    /**
     * donne la divergence de ce courant
     */
    public double getDiv() {
        return div;
    }

    /**
     * enregistre la divergence de ce courant
     */
    public void setDiv(double divergence) {
        div = divergence;
    }

    /**
     * enregistre le rotationnel de ce courant
     */
    public void setCurl(double my_curl) {
        curl = my_curl;
    }

    public void setTemp(float t) {
        temp = t;
    }

    public void setSal(float s) {
        sal = s;
    }

    public void setBat(float b) {
        bat = b;
    }

    public float getTemp() {
        return temp;
    }

    public float getSal() {
        return sal;
    }

    public float getBat() {
        return bat;
    }

    /**
     * garbage collector waiting
     */
}
