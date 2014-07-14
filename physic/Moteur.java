/*
 * Moteur.java
 *
 * Created on 7 avril 2003, 19:58
 */
package physic;

import java.util.ArrayList;
import java.awt.Point;
import data.DataCarte;
import data.CollVortexPhys;
import data.TabloDouble2D;

/**
 *
 *@author  Mahler
 *@society LIL
 */
public class Moteur implements constants.courant, constants.physique{

	
    private static boolean dBug = true;

    /** indique la methode � employer pour deriver */
    private byte methode_derivation = SANS_INTERPOLATION + DERIVEE_DISCRETISATION_CORPETTI;

    /** tableau de TabloDouble2D pour chercher les Maxima Locaux de chaque methode*/
    public TabloDouble2D[] tabFitness = null;

    /** enregistre quelle methode sera employee pour deriver
     * @param meth un byte  de l'iterface de contantes physique */
    public void setMethodeDerivation(byte meth) {
        methode_derivation = meth;
    }

    /** Creates a new instance of Moteur */
    public Moteur() {
        methode_derivation = SANS_INTERPOLATION + DERIVEE_DISCRETISATION_CORPETTI;
        tabFitness = new TabloDouble2D[LENGTH_TB2D];
    }
    	/** destructeur */
	protected void finalize(){
			free(tabFitness);
	}
	/** libere la memoire */
	public void dispose(){
				free(tabFitness);
	}

	/** libere la memoire et la carte */
	public void reinit(DataCarte mer){
				this.dispose();
				mer.getVortexPhys().dispose();
	}

	private void free(TabloDouble2D[] t) {
		if ((t!=null)&&(t.length>0)) {
			int i;
			for (i=0;i<t.length;i++) {
				if(t[i]!=null) {
					t[i].dispose();
					t[i]=null;
				}
			}
		}
		t=null;
	}
	private void free(int[][] t) {
		if ((t!=null)&&(t.length>0)) {
			int i;
			for (i=0;i<t.length;i++) {
				if(t[i]!=null) {
					t[i]=null;
				}
			}
		}
		t=null;
	}

    //pas besoin de Moteur m !! dans les arguments
    // moteur ne fait que des methodes !!

    //$$$$$$$$$$$$$$$$$$$$$$$$$$ OUTILS $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    /** calcule la distance quadratique entre 2 nombres
     *@return renvoie sqrt(x�+y�)
     */
    private  double calculNorme(double vx,double vy) {
         if ((java.lang.Math.abs(vx) > SEUIL_NORME)|(java.lang.Math.abs(vy) > SEUIL_NORME))
            return java.lang.Math.sqrt(vx*vx + vy*vy);
        else
            return 0d;
    }
        /** permet d'affirmer que l'on peut trouver une d�riv�e en ce point ! */
    public static boolean courantDerivable(DataCarte mer, int x,  int y)
    {
        boolean ret = mer.isCorrect(x,y) & !mer.isBorderMap(x,y);
        ret = ret & !(mer.isNearCoast(x,y));
    return ret;
    }

    private boolean courantIsDerivable(DataCarte mer, int x,  int y)
    { return courantDerivable(mer, x, y); }

    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ DERIVATION $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    /** FONCTION DERIVEE : derive on renseigne les 5 premiers termes !
    */
    public double derive( double f_xmoins2h, double f_xmoinsh, double f_x, double f_xplush, double f_xplus2h ) {
        double ret = -0.1;
        switch (methode_derivation) {
            case (byte)(SANS_INTERPOLATION + DERIVEE_SIMPLE) :
                ret = deriveeSimple(f_x, f_xplush);
                break;
            case (byte)(SANS_INTERPOLATION + DERIVEE_SYMETRIQUE) :
                ret = deriveeSymetrique(f_xmoinsh,f_xplush);
                break;
            case (byte)(SANS_INTERPOLATION + DERIVEE_DISCRETISATION_CORPETTI) :
                ret = deriveeCorpetti(f_xmoins2h, f_xmoinsh, f_x, f_xplush);
                break;
            default :  { ret = -0.1; System.out.println("physique.Moteur : aucune methode definie ! "); }
        }
        return ret;

    }

    /** simple approximation de derivee */
    private double deriveeSimple(double f_x, double f_xplush) {
    return (f_xplush - f_x);
    }

    /** deuxieme approximation de derivee */
    private double deriveeSymetrique(double f_xmoinsh, double f_xplush) {
    return ((f_xplush - f_xmoinsh)/2.0);
    }
    /** Troisieme approximation de derivee assymetrique*/
    private double deriveeCorpetti(double f_xmoins2h, double f_xmoinsh, double f_x, double f_xplush) {
    return ((3.0*f_x + f_xmoins2h -6.0*f_xmoinsh + 2.0*f_xplush)/6.0);
    }

    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ DERVATION DES COMPOSANTES DU CHAMP VECTORIEL ����������
    /** la deriv�e du courant(U,W) en x,y selon  : dU/dx
     *@return renvoie
     *@param on suppose que lengthpoint est valable
     */
    public double dUdx(DataCarte mer, int posX,  int posY)
    {
        double [] param = new double[5];
        int i=0;
        try{if(!mer.getC(posX-2,posY).getSurTerre()) param[i]=mer.getC(posX-2,posY).getXBase(); else param[i] = 0.0;}catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX-1,posY).getSurTerre()) param[i]= mer.getC(posX-1,posY).getXBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY).getSurTerre()) param[i]= mer.getC(posX,posY).getXBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX+1,posY).getSurTerre()) param[i]= mer.getC(posX+1,posY).getXBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX+2,posY).getSurTerre()) param[i]= mer.getC(posX+2,posY).getXBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}

        double ret = derive(param[0], param[1],param[2],param[3],param[4]);
            return ret;
    }

     /** la deriv�e du courant(U,W) en x,y selon y : du/dy
     *@return renvoie
     */
    public double dUdy(DataCarte mer, int posX,  int posY)
    {
        double [] param = new double[5];
        int i=0;
        try{if(!mer.getC(posX,posY-2).getSurTerre()) param[i]=mer.getC(posX,posY-2).getXBase(); else param[i] = 0.0;}catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY-1).getSurTerre()) param[i]= mer.getC(posX,posY-1).getXBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY).getSurTerre()) param[i]=mer.getC(posX,posY).getXBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY+1).getSurTerre()) param[i]= mer.getC(posX,posY+1).getXBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY+2).getSurTerre()) param[i]= mer.getC(posX,posY+2).getXBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}

        double ret = derive(param[0], param[1],param[2],param[3],param[4]);
        return ret;
    }
    /** la deriv�e du courant(U,W) en x,y selon x : dW/dx
     *@return renvoie
     */
    public double dWdx(DataCarte mer, int posX,  int posY)
    {
        double [] param = new double[5];
        int i=0;
        try{if(!mer.getC(posX-2,posY).getSurTerre()) param[i]=mer.getC(posX-2,posY).getYBase(); else param[i] = 0.0;}catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX-1,posY).getSurTerre()) param[i]= mer.getC(posX-1,posY).getYBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY).getSurTerre()) param[i]= mer.getC(posX,posY).getYBase();  else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX+1,posY).getSurTerre()) param[i]= mer.getC(posX+1,posY).getYBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX+2,posY).getSurTerre()) param[i]= mer.getC(posX+2,posY).getYBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}

        double ret = derive(param[0], param[1],param[2],param[3],param[4]);
        return ret;
    }
    /** la deriv�e du courant(U,W) en x,y selon y : dW/dy
     *@return renvoie
     */
    public double dWdy(DataCarte mer, int posX,  int posY)
    {
        double [] param = new double[5];
        int i=0;
        try{if(!mer.getC(posX,posY-2).getSurTerre()) param[i]=mer.getC(posX,posY-2).getYBase(); else param[i] = 0.0;}catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY-1).getSurTerre()) param[i]= mer.getC(posX,posY-1).getYBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY).getSurTerre()) param[i]= mer.getC(posX,posY).getYBase(); else param[i] = 0.0;} catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY+1).getSurTerre()) param[i]= mer.getC(posX,posY+1).getYBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY+2).getSurTerre()) param[i]= mer.getC(posX,posY+2).getYBase(); else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}

        double ret = derive(param[0], param[1],param[2],param[3],param[4]);
        return ret;
    }

    // $$$$$$$$$$$$$$$$$$$$$$$$$ DERVATION DES COMPOSANTES DU CHAMP VECTORIEL �� UNITAIRE �� ����������
    /** la deriv�e du courant unitaire (U,W) en x,y selon  : dU/dx
     *@return renvoie
     *@param on suppose que le point est valable
     */
    public double dUdx_CU(DataCarte mer, int posX,  int posY)
    {
        double [] param = new double[5];
        int i=0;
        try{if(!mer.getC(posX-2,posY).getSurTerre()) param[i]=mer.getC(posX-2,posY).uX; else param[i] = 0.0;}catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX-1,posY).getSurTerre()) param[i]= mer.getC(posX-1,posY).uX; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY).getSurTerre()) param[i]= mer.getC(posX,posY).uX; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX+1,posY).getSurTerre()) param[i]= mer.getC(posX+1,posY).uX; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX+2,posY).getSurTerre()) param[i]= mer.getC(posX+2,posY).uX; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}

        double ret = derive(param[0], param[1],param[2],param[3],param[4]);
            return ret;
    }

     /** la deriv�e du courant unitaire (U,W) en x,y selon y : du/dy
     *@return renvoie
     */
    public double dUdy_CU(DataCarte mer, int posX,  int posY)
    {
        double [] param = new double[5];
        int i=0;
        try{if(!mer.getC(posX,posY-2).getSurTerre()) param[i]=mer.getC(posX,posY-2).uX; else param[i] = 0.0;}catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY-1).getSurTerre()) param[i]= mer.getC(posX,posY-1).uX; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY).getSurTerre()) param[i]=mer.getC(posX,posY).uX; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY+1).getSurTerre()) param[i]= mer.getC(posX,posY+1).uX; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY+2).getSurTerre()) param[i]= mer.getC(posX,posY+2).uX; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}

        double ret = derive(param[0], param[1],param[2],param[3],param[4]);
        return ret;
    }
    /** la deriv�e du courant unitaire (U,W) en x,y selon x : dW/dx
     *@return renvoie
     */
    public double dWdx_CU(DataCarte mer, int posX,  int posY)
    {
        double [] param = new double[5];
        int i=0;
        try{if(!mer.getC(posX-2,posY).getSurTerre()) param[i]=mer.getC(posX-2,posY).uY; else param[i] = 0.0;}catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX-1,posY).getSurTerre()) param[i]= mer.getC(posX-1,posY).uY; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY).getSurTerre()) param[i]= mer.getC(posX,posY).uY;  else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX+1,posY).getSurTerre()) param[i]= mer.getC(posX+1,posY).uY; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX+2,posY).getSurTerre()) param[i]= mer.getC(posX+2,posY).uY; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}

        double ret = derive(param[0], param[1],param[2],param[3],param[4]);
        return ret;
    }
    /** la deriv�e du courant unitaire (U,W) en x,y selon y : dW/dy
     *@return renvoie
     */
    public double dWdy_CU(DataCarte mer, int posX,  int posY)
    {
        double [] param = new double[5];
        int i=0;
        try{if(!mer.getC(posX,posY-2).getSurTerre()) param[i]=mer.getC(posX,posY-2).uY; else param[i] = 0.0;}catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY-1).getSurTerre()) param[i]= mer.getC(posX,posY-1).uY; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY).getSurTerre()) param[i]= mer.getC(posX,posY).uY; else param[i] = 0.0;} catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY+1).getSurTerre()) param[i]= mer.getC(posX,posY+1).uY; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}
        i++;
        try{if(!mer.getC(posX,posY+2).getSurTerre()) param[i]= mer.getC(posX,posY+2).uY; else param[i] = 0.0; }catch (Exception e){param[i]=0.0;}

        double ret = derive(param[0], param[1],param[2],param[3],param[4]);
        return ret;
    }


	// $$$$$$$$$$$$$$$$$$$$$$$$$$$$ CALCUL �� CELLULAIRE �� : POUR UN POINT PARTICULER $$$$$$$$$$$$$$$
    /** calcule le rotationnel !
     */
    public double calculerCurl(DataCarte mer,int  posX,  int posY) {
        if (this.courantIsDerivable(mer,posX,posY)){ return (dUdy(mer,posX,posY) - dWdx(mer,posX,posY) ); }
        else { return 0.0;}
    }

    /** calcule la divergence !
     *
     */
    public double calculerDiv(DataCarte mer,int  posX,  int posY) {
        if (this.courantIsDerivable(mer,posX,posY)){ return (dUdx(mer,posX,posY) + dWdy(mer,posX,posY) ); }
        else { return 0.0;}
    }

	// $$$$$$$$$$$$$$$$$ CALCUL �� CELLULAIRE �� sur vecteurs UNITAIRES : POUR UN POINT PARTICULER $$$$$$$$$$$$$$$
    /** calcule le rotationnel !
     */
    public double calculerCurl_CU(DataCarte mer,int  posX,  int posY) {
        if (this.courantIsDerivable(mer,posX,posY)){ return ( dUdy_CU(mer,posX,posY) - dWdx_CU(mer,posX,posY) ); }
        else { return 0.0;}
    }

    /** calcule la divergence !

     */
    public double calculerDiv_CU(DataCarte mer,int  posX,  int posY) {
        if (this.courantIsDerivable(mer,posX,posY)){ return ( dUdx_CU(mer,posX,posY) + dWdy_CU(mer,posX,posY) ); }
        else { return 0.0;}
    }

// ****************************************************************************************************
// ****************** Fonctions d'appel par Memoire : *************************************************
// ****************************************************************************************************
    /** calcule le rotationnel dans chaque point de la carte  */
    public void majCurl(DataCarte c) {
        int posX,posY;

        for(posX=0; posX<c.getTailleX(); posX++) {
            for(posY=0;posY<c.getTailleY();posY++) {
               c.getC(posX,posY).setCurl( calculerCurl(c, posX, posY ));
            }
        }
    }

    public void calculerFitnessCurl(DataCarte c) {
        int posX,posY;

		tabFitness[CURL]= new TabloDouble2D(c.getTailleX(),c.getTailleY());
		TabloDouble2D t = tabFitness[CURL];

        for(posX=0; posX<c.getTailleX(); posX++) {
            for(posY=0;posY<c.getTailleY();posY++) {
                t.setTablo( posX,posY,calculerCurl(c, posX, posY ));
            }
        }
    }

    /** calcule la divergence dans chaque point de la carte  */
    public void majDiv(DataCarte c) {
       int posX,posY;

        for(posX=0; posX<c.getTailleX(); posX++) {
            for(posY=0;posY<c.getTailleY();posY++) {
                c.getC(posX,posY).setDiv(calculerDiv(c, posX, posY ));
            }
        }
    }

    public void calculerFitnessDiv(DataCarte c) {
       int posX,posY;

       	tabFitness[DIV]= new TabloDouble2D(c.getTailleX(),c.getTailleY());
		TabloDouble2D t = tabFitness[DIV];

        for(posX=0; posX<c.getTailleX(); posX++) {
            for(posY=0;posY<c.getTailleY();posY++) {
                t.setTablo( posX,posY,calculerDiv(c, posX, posY ));
            }
        }
    }

// $$$$$$$$$$$$$$$$$$$$$$ Calculer avec le champ unitaire $$$$$$$$$$$$$$$$$$$$$$$$
    /** calcule le rotationnel dans chaque point de la carte  */
    public void majCurl_CU(DataCarte c) {
        int posX,posY;

        // calcul du champ unitaire
        for(posX=0; posX<c.getTailleX(); posX++)
            for(posY=0;posY<c.getTailleY();posY++)
                c.getC(posX,posY).calculCU(1,0);

        for(posX=0; posX<c.getTailleX(); posX++) {
            for(posY=0;posY<c.getTailleY();posY++) {
                c.getC(posX,posY).setCurl( calculerCurl_CU(c, posX, posY ));
            }
        }
    }
    public void calculerFitnessCurl_CU(DataCarte c) {
        int posX,posY;

       	tabFitness[CURL_CU]= new TabloDouble2D(c.getTailleX(),c.getTailleY());
		TabloDouble2D t = tabFitness[CURL_CU];

        // calcul du champ unitaire
        for(posX=0; posX<c.getTailleX(); posX++)
            for(posY=0;posY<c.getTailleY();posY++)
                c.getC(posX,posY).calculCU(1,0);

        for(posX=0; posX<c.getTailleX(); posX++) {
            for(posY=0;posY<c.getTailleY();posY++) {
                t.setTablo( posX,posY, calculerCurl_CU(c, posX, posY ));
            }
        }
    }

    /** calcule la divergence dans chaque point de la carte  */
    public void majDiv_CU(DataCarte c) {
       int posX,posY;

        // calcul du champ unitaire
        for(posX=0; posX<c.getTailleX(); posX++)
            for(posY=0;posY<c.getTailleY();posY++)
                c.getC(posX,posY).calculCU(1,0);

        for(posX=0; posX<c.getTailleX(); posX++) {
            for(posY=0;posY<c.getTailleY();posY++) {
                c.getC(posX,posY).setDiv(calculerDiv_CU(c, posX, posY ));
            }
        }
    }
    public void calculerFitnessDiv_CU(DataCarte c) {
       int posX,posY;

       	tabFitness[DIV_CU]= new TabloDouble2D(c.getTailleX(),c.getTailleY());
		TabloDouble2D t = tabFitness[DIV_CU];

        // calcul du champ unitaire
        for(posX=0; posX<c.getTailleX(); posX++)
            for(posY=0;posY<c.getTailleY();posY++)
                c.getC(posX,posY).calculCU(1,0);

        for(posX=0; posX<c.getTailleX(); posX++) {
            for(posY=0;posY<c.getTailleY();posY++) {
                t.setTablo( posX,posY,calculerDiv_CU(c, posX, posY ));
            }
        }
    }

	/** pour acceder aux grandeurs calcul�es 
	 * FLAG == DIV ou DIV_CU ou CURL ou CURL_CU */
    public TabloDouble2D getTable(int FLAG) {
		return tabFitness[FLAG];
    }


    /** pour transferer les resultats en fond de carte */
    public void setDonneesEnFond(DataCarte c, int RESULT_TO_DISPLAY ) {

    if ( RESULT_TO_DISPLAY==TB2D_DIV )
    	{c.setTable(tabFitness[DIV].cloneMe());
    	 c.getTable().normaliserEnConservantLeSigne(); }
    	 //c.getTable().normaliserTab();}
    if ( RESULT_TO_DISPLAY==TB2D_CURL )
    	{c.setTable(tabFitness[CURL].cloneMe());
    	 c.getTable().normaliserEnConservantLeSigne(); }
    	 //c.getTable().normaliserTab(); }
    if ( RESULT_TO_DISPLAY==TB2D_DIV_CU )
    	{c.setTable(tabFitness[DIV_CU].cloneMe());
    	 c.getTable().normaliserEnConservantLeSigne(); }
    	 //c.getTable().normaliserTab(); }
    if ( RESULT_TO_DISPLAY==TB2D_CURL_CU )
    	{c.setTable(tabFitness[CURL_CU].cloneMe());
    	 c.getTable().normaliserEnConservantLeSigne(); }
    	 //c.getTable().normaliserTab(); }
    }

    /** trouver les extremas du champ scalaire Curl_CU : */
    public void extremaCurl_CU(DataCarte mer) {
       extremaAreCenters(tabFitness[CURL_CU],mer,CURL_CU);
  	}
    /** trouver les extremas du champ scalaire Curl : */
    public void extremaCurl(DataCarte mer) {
       extremaAreCenters(tabFitness[CURL],mer,CURL);
  	}
    /** trouver les extremas du champ scalaire Div : */
    public void extremaDiv(DataCarte mer) {
       extremaAreCenters(tabFitness[DIV], mer, DIV);
  	}
  	/** trouver les extremas du champ scalaire Div_CU : */
    public void extremaDiv_CU(DataCarte mer) {
       extremaAreCenters(tabFitness[DIV_CU],mer,DIV_CU);
  	}


    private void extremaAreCenters(TabloDouble2D tab, DataCarte mer,int tag) {
       int i;
       ArrayList min,max;
       min = tab.getListeMinimaEnMer(mer);
       max = tab.getListeMaximaEnMer(mer);

       if (dBug) System.out.println( " * " +min.size()+" min et "+max.size()+" max LOCAUX, trouv�s pour tag "+tag+" de methodes physiques ");
       if (min!=null)
	       for (i=0;i<min.size();i++)
    	   		mer.getVortexPhys().ajouterCentre((Point)min.get(i),tag + TAG_MIN);
	   if (max!=null)
	       for (i=0;i<max.size();i++)
    	   		mer.getVortexPhys().ajouterCentre((Point)max.get(i),tag + TAG_MAX);
	}

	/** sigma indicateur de segmentation de zones */
	private int[][] tabsigma = null;
	
	/** getter of tabsigma for display animation */
	public int[][] getSigma() { return tabsigma;}
	
	/** cloner : � chaque new iteration **/
	private int[][]  clone( int[][] ti) {
		boolean yes = true;
		int[][] tf = ti;
		
		if (ti==null) 
			{ System.out.println(" physique.moteur.clone : tab ini = null! "); yes = false;}
		if (ti[0]==null) 
			{ System.out.println(" physique.moteur.clone : tab ini[0] = null! "); yes = false;}
		
		if (ti.length==0) 
			{ System.out.println(" physique.moteur.clone : tab ini = longueur nulle! "); yes = false;}
		if (ti[0].length==0) 
			{ System.out.println(" physique.moteur.clone : tab ini[0] = longueur nulle ! "); yes = false;}
		
		if (yes)
		{
			tf = new int[ti.length][(ti[0].length)];
			int i,j;
			for(i=0;i<ti.length;i++)
			for(j=0;j<ti[i].length;j++)
				tf[i][j] = ti[i][j];
		}
		
		return tf;
		
	}
	/** segmenter le zones de concentration */
	public int[][] segmenterZone(int flag, DataCarte mer, double seuil, double compacite)
	{	
		int[][] ret = null;
		try{
			TabloDouble2D data = null;
			switch(flag)
			{
				case DIV :
				data = tabFitness[DIV];
				break;
				case DIV_CU :
				data = tabFitness[DIV_CU];
				break;
				case CURL :
				data = tabFitness[CURL];
				break;
				case CURL_CU :
				data = tabFitness[CURL_CU];
				break;
				default : {data = null;}
			}
                        if (data!=null)
			ret = segmentationZone(data,mer,seuil,compacite);
			}catch(Exception e){e.printStackTrace();}
			return ret;
	}
    
    /** fait un seuillage sur les zones POSITIVES !! */    
	public int[][] segmenterZonePositive(int flag, DataCarte mer, double seuil, double compacite)
	{	
		int[][] ret = null;
		try{
			TabloDouble2D datapos=null, data = null;
			switch(flag)
			{
				case DIV :
				data = tabFitness[DIV];
				break;
				case DIV_CU :
				data = tabFitness[DIV_CU];
				break;
				case CURL :
				data = tabFitness[CURL];
				break;
				case CURL_CU :
				data = tabFitness[CURL_CU];
				break;
				default : {data = null;}
			}
            if (data!=null) {
				datapos = data.cloneMe();
				datapos.selectPositif();
				ret = segmentationZone(datapos,mer,seuil,compacite);
			}
			}catch(Exception e){e.printStackTrace();}
			return ret;
	}
	
	/** fait un seuillage sur les zones Negatives !! */    
	public int[][] segmenterZoneNegative(int flag, DataCarte mer, double seuil, double compacite)
	{	
		int[][] ret = null;
		try{
			TabloDouble2D dataneg=null, data = null;
			switch(flag)
			{
				case DIV :
				data = tabFitness[DIV];
				break;
				case DIV_CU :
				data = tabFitness[DIV_CU];
				break;
				case CURL :
				data = tabFitness[CURL];
				break;
				case CURL_CU :
				data = tabFitness[CURL_CU];
				break;
				default : {data = null;}
			}
            if (data!=null) {
				dataneg = data.cloneMe();
				dataneg.doSymetry();
				dataneg.selectPositif();
				ret = segmentationZone(dataneg,mer,seuil,compacite);
				
			}
			}catch(Exception e){e.printStackTrace();}
			return ret;
	}
	
    /** trouve les zones de concentration , par seuillage, et les discretiser */
        public void trouverConcentration(int flag, DataCarte mer, double seuil, double compacite)
	{	
		int[][] tab = null;
		try{
			TabloDouble2D data = null;
			switch(flag)
			{
				case DIV :
				data = tabFitness[DIV];
				break;
				case DIV_CU :
				data = tabFitness[DIV_CU];
				break;
				case CURL :
				data = tabFitness[CURL];
				break;
				case CURL_CU :
				data = tabFitness[CURL_CU];
				break;
				default : {data = null;}
			}
            if (data!=null) {
                tab = segmentationZone(data,mer,seuil,compacite);
                if (dBug) System.out.println("_ segmentation ok ");
            }
                
            }catch(Exception e){e.printStackTrace();}
            
            //si on veut r�cup�rer des vortex diff�rents
            if (DISCRETISER_LES_DIFFERENTES_ZONES) {
	            try {
	               if (tab!=null) {
	                   discretiserZones(tab,mer,flag);
	                   if (dBug) System.out.println("_ discretisation ok "); 
	                }
	                else
	                if (dBug) System.out.println(" pas de discretisation ");
	                }catch(Exception e){e.printStackTrace();}
	         }
	}


	private static int CONCENTRATION = 1; 
	private static int NO_INFO = -1;
     
     // recurence
    private void remplir(int x, int y, DataCarte mer, int[][] sigma, int[][] discret, int monFlag) {
		try {
			// eliminer
			if (!mer.isCorrect(x,y)) throw new Exception("hors de carte");
			if (mer.getC(x,y).getSurTerre()) throw new Exception("sur terre");
			if (sigma[x][y]!=CONCENTRATION) throw new Exception(" pas de concentration");
			// deja rempli
			if (discret[x][y]!=NO_INFO) throw new Exception(" d�j� rempli ");
			// sinon mettre le flag
			discret[x][y] = monFlag;
			
			remplir (x+1,y,mer,sigma,discret,monFlag);
			remplir (x,y-1,mer,sigma,discret,monFlag);
			remplir (x-1,y,mer,sigma,discret,monFlag);
			remplir (x,y+1,mer,sigma,discret,monFlag);
			
		}catch(Exception e){}
    } 

    // recurence 
    private void trouverBord(int x, int y, DataCarte mer, int[][] discret, int monFlag, ArrayList vecteur) {
		try {
			// eliminer
			if (!mer.isCorrect(x,y)) throw new Exception("hors de carte");
			if (mer.getC(x,y).getSurTerre()) throw new Exception("sur terre");
			if (discret[x][y]!=monFlag) throw new Exception(" hors boucle ");
			if (vecteur.contains(new Point(x,y))) throw new Exception(" d�ja trouv� bord ");
			// sinon mettre le flag
			boolean bord = false;
			int i,j;
			
			for(i=-1; i<=1; i++) {
                for(j=-1;j<=1;j++) {
			        if ((i!=0)|(j!=0)){
			        	if (!bord)	{
							try {
								bord = bord | (!mer.isCorrect(i,j));
								bord = bord | (mer.getC(i,j).getSurTerre()); // exception si incorrect
								bord = bord | (discret[i][j]!=monFlag);
							}catch(Exception e){}
						}
					}
				}
			}
			if (bord) {
				vecteur.add(new Point(x,y));
				trouverBord (x+1,y,mer,discret,monFlag,vecteur);
				trouverBord (x,y-1,mer,discret,monFlag,vecteur);
				trouverBord (x-1,y,mer,discret,monFlag,vecteur);
				trouverBord (x,y+1,mer,discret,monFlag,vecteur);
			}
		}catch(Exception e){}
    } 
	
    /**
     * apres la mise � jour de sigma, on consid�re que 
     * seuls les points int�r�ssants sont gard�s !
     * D'abord on remplit un tableau d'int, avec un flag par vortex;
     * puis on rentre les vortex;
     * DiscretiserZone va ajouter � la DataCarte des VortexPhys,
     */
	private int[][] discretiserZones(int[][] sigma, DataCarte c, int flag) {
        int posX,posY;
        int i,cpt=0,lng = 0;

        if (dBug) System.out.println("_ discretisation debut ");
        
        int[][] tab = new int[c.getTailleX()][c.getTailleY()];
        
        for(posX=0; posX<c.getTailleX(); posX++)
            for(posY=0;posY<c.getTailleY();posY++)
                tab[posX][posY] = NO_INFO;

        for(posX=0; posX<c.getTailleX(); posX++) {
            for(posY=0;posY<c.getTailleY();posY++) {
                // si on est sur dans un vortex 
                if (!c.getC(posX,posY).getSurTerre())
                   	if (sigma[posX][posY]==CONCENTRATION)
                   		// si le point n'est pas tagu�
                    	if(tab[posX][posY]==NO_INFO) {
                    		remplir(posX,posY,c,sigma,tab,cpt);
                    		cpt++;
                    	}
            }
        }
       
        lng = cpt;
        if (lng>0) {
        	if (dBug) System.out.println("_ discretisation : "+lng+" zones trouv�es ");
       
            ArrayList vecteur;
	        CollVortexPhys maCollection = c.getVortexPhys();
	        for (cpt=0;cpt<lng;cpt++) {
		        vecteur = new ArrayList();
	            for(posX=0; posX<c.getTailleX(); posX++) {
		            for(posY=0;posY<c.getTailleY();posY++) {
		                // si on est sur dans un vortex 
		                if (!c.getC(posX,posY).getSurTerre())
		                   	if (sigma[posX][posY]==CONCENTRATION)
		                   		// si le point n'est pas tagu�
		                    	if(tab[posX][posY]==cpt) {
		                    		trouverBord(posX,posY,c,tab,cpt, vecteur);
		                    	}
			        }
			    }
			    maCollection.ajouter(vecteur, flag);
			}
		}
		if (dBug) System.out.println("_ discretisation fin ");
        System.out.println(" *** "+cpt+"vortex trouves apres discretisation pour le flag "+flag+"!");
        return tab;
        }


// ******************* segmentation ***************
	private int[][] segmentationZone(TabloDouble2D data, DataCarte mer, double seuil, double compacite)
						throws Exception {
		// algo de segmentation : annexe D th�se de Thomas Corpetti
		// data contient le champ scalaire dont on veut la segmentation des zones concentr�es
		// seuil = valeur positive !

		if ((data==null)|(mer==null)|(seuil<0d)) throw new Exception("�� ERREUR physique.moteur.segmentationZone() : arguments non valides");
		if ((data.getTailleX()!=mer.getTailleX())|(data.getTailleY()!=mer.getTailleY())) throw new Exception("�� ERREUR physique.moteur.segmentationZone() : dimensions non valides");
		
		if (dBug) System.out.println("_ segmentation debut ");
       
		int i,j,tX,tY;
		double cost,oldcost;
		int[][] oldsigma=null;
		double localcost;
		boolean goon = true;

		tX = data.getTailleX();
		tY = data.getTailleY();

		// ************** initialisation ****************
		//free(sigma);
		int[][] sigma = null;
		sigma = new int[tX][tY];
		// sigma de chaque point indique si la concentration de donn�e de data est suffisante
		for(i=0;i<tX;i++)
			for(j=0;j<tY;j++) {
				try {
					if (mer.getC(i,j).getSurTerre()) { throw new Exception(" point sur terre ! ");}

					if (Math.abs(data.getTablo(i,j))>seuil)
						sigma[i][j] = +1;
					else
						sigma[i][j] = -1;
					}catch(Exception e){}
				}

		cost = this.calculerCoutGlobalSegmentation(data,mer,seuil,compacite,sigma);
		if (dBug) System.out.println("_ segmentation cout initial = "+cost);
        while (goon) {
			// ********* backtracking **********
			oldsigma = clone(sigma);
			
			// ********* maj energie locale ******
			for(i=0;i<tX;i++)
				for(j=0;j<tY;j++) {
					try {
						if (mer.getC(i,j).getSurTerre()) { throw new Exception(" point sur terre ! ");}

						localcost = this.calculerCoutLocalSegmentation
												(data,mer,seuil,compacite,oldsigma,i,j);
						if (localcost>0d)
							sigma[i][j] = +1;
						else
							sigma[i][j] = -1;
						}catch(Exception e){}
					}
			
			if (oldsigma.equals(sigma))
				if (dBug) System.out.println("@@@@@\n @@@@@@\n @@@@@ OLDSIGMA==SIGMA ! ");
        		
			
			// ********* maj cout global *******
			oldcost = cost;
			cost = this.calculerCoutGlobalSegmentation(data,mer,seuil,compacite,sigma);
			if (dBug) System.out.println("_ segmentation cout= "+cost+"; oldcost= "+oldcost);
        	
        	// ********* minimiser cout *************
			if (Math.abs(oldcost)>SEUIL_NORME) {
				if (dBug) System.out.println("_ segmentation difference "+((oldcost-cost)/oldcost)+" intervalle (0..1)");
  				// minimiser : oldcost>cost
  				goon = ( ((oldcost-cost)/Math.abs(oldcost)) > SEUIL_COUT_MEILLEUR );
  			}
			else System.out.println("!!! ERREUR physique.moteur.segmentationZone : cout nul !! ");
		
			// affichage : 
			tabsigma = clone(sigma);
		}
		if (oldsigma!=null) 
			tabsigma = clone(oldsigma);
		else if (dBug) System.out.println("@@@ segmentation OLDSIGMA NULL"); 
		return oldsigma;
		
	}
	
	//fonction d'energie locale : i,j n'est pas sur terre !!
	private double calculerCoutLocalSegmentation(TabloDouble2D data, DataCarte mer,
							double seuil, double compacite, int[][] sigma, int i, int j)
	{
		double val = 0d, poids = 0d, cost = 0d;
		int sum=0;
		int m,n, voisinsOK = 0;

		if (TYPE_VOISINAGE == QUATRE_VOISINAGE)
		{
			// i,j n'est pas sur terre !
			val = Math.abs(data.getTablo(i,j));
			cost = (val - seuil);
			cost = cost / (val + seuil);

			// FirstPArt OK

			// Balayage du voisinage !!
			m = i-1; n = j;
			try {
				if (!mer.isCorrect(m,n)) { throw new Exception(" point hors carte ! ");}
				if (mer.getC(m,n).getSurTerre()) { throw new Exception(" point sur terre ! ");}
				sum = sum + sigma[m][n];
				voisinsOK++;
			}catch (Exception e) {}

			m = i+1; n = j;
			try {
				if (!mer.isCorrect(m,n)) { throw new Exception(" point hors carte ! ");}
				if (mer.getC(m,n).getSurTerre()) { throw new Exception(" point sur terre ! ");}
				sum = sum + sigma[m][n];
				voisinsOK++;
			}catch (Exception e) {}

			m = i; n = j-1;
			try {
				if (!mer.isCorrect(m,n)) { throw new Exception(" point hors carte ! ");}
				if (mer.getC(m,n).getSurTerre()) { throw new Exception(" point sur terre ! ");}
				sum = sum + sigma[m][n];
				voisinsOK++;
			}catch (Exception e) {}

			m = i; n = j+1;
			try {
				if (!mer.isCorrect(m,n)) { throw new Exception(" point hors carte ! ");}
				if (mer.getC(m,n).getSurTerre()) { throw new Exception(" point sur terre ! ");}
				sum = sum + sigma[m][n];
				voisinsOK++;
			}catch (Exception e) {}

			// selection des points possedant un voisinage en partie maritime
			if (voisinsOK > 0) {
				 poids = ((double)voisinsOK)/4d;
				 val = poids * compacite * ((double)sum);
				 cost = (poids * cost) + val;
			} // sinon on veut �liminer ce point >> donc engendrer sigma = -1
			// d'o�
			else cost = -1d;
		}
		return cost;
	}

	private double calculerCoutGlobalSegmentation(TabloDouble2D data, DataCarte mer,
							double seuil, double compacite, int[][] sigma)
	{
		double val=0d,ponderation=0d, cost = 0d, costmp1 = 0d,costmp2=0d;
		int sum=0;
		int m,n, voisinsOK = 0;
		int i,j,tX,tY;
		tX = data.getTailleX();
		tY = data.getTailleY();

		if (TYPE_VOISINAGE == QUATRE_VOISINAGE)
		{
			for(i=0;i<tX;i++)
			for(j=0;j<tY;j++) {
				try {
					if (mer.getC(i,j).getSurTerre()) { throw new Exception(" point sur terre ! ");}

					voisinsOK = 0;
					sum = 0;
					// 1st PART
					val = Math.abs(data.getTablo(i,j));
					costmp1 = (seuil - val)*(double)sigma[i][j];
					costmp1 = costmp1 / (seuil + val);

					// i,j n'est pas sur terre !
					// Balayage du voisinage !!
					m = i-1; n = j;
					try {
						if (!mer.isCorrect(m,n)) { throw new Exception(" point hors carte ! ");}
						if (mer.getC(m,n).getSurTerre()) { throw new Exception(" point sur terre ! ");}
						sum = sum + sigma[m][n]*sigma[i][j];
						voisinsOK++;
					}catch (Exception e) {}

					m = i+1; n = j;
					try {
						if (!mer.isCorrect(m,n)) { throw new Exception(" point hors carte ! ");}
						if (mer.getC(m,n).getSurTerre()) { throw new Exception(" point sur terre ! ");}
						sum = sum + sigma[m][n]*sigma[i][j];
						voisinsOK++;
					}catch (Exception e) {}

					m = i; n = j-1;
					try {
						if (!mer.isCorrect(m,n)) { throw new Exception(" point hors carte ! ");}
						if (mer.getC(m,n).getSurTerre()) { throw new Exception(" point sur terre ! ");}
						sum = sum + sigma[m][n]*sigma[i][j];
						voisinsOK++;
					}catch (Exception e) {}

					m = i; n = j+1;
					try {
						if (!mer.isCorrect(m,n)) { throw new Exception(" point hors carte ! ");}
						if (mer.getC(m,n).getSurTerre()) { throw new Exception(" point sur terre ! ");}
						sum = sum + sigma[m][n]*sigma[i][j];
						voisinsOK++;
					}catch (Exception e) {}

					// selection des points possedant un voisinage en partie maritime
					if (voisinsOK > 0) {
						 ponderation = ((double)voisinsOK)/4d; 
						 // ponderation : si 1 ou 2 ou 3 voisins
						 costmp1 = ponderation * costmp1;
						 costmp2 = ponderation * compacite * ((double)sum);
					} // sinon on ne compte pas ce point d'o�
					else {costmp2 = 0d;costmp1=0d;}

				}catch (Exception e) {costmp1 = 0d; costmp2=0d;}
				cost = cost + (costmp1-costmp2);
			}
		}
		return cost;
	}

// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
// $$$$$$$$$$$$$$$$$$$$$$$$$$$ FONCTIONS DE SAUVEGARDE $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    public void sauverCurl(DataCarte mer, String nom_fichier) {
        ResultatFile res = new ResultatFile(nom_fichier);
        this.majCurl(mer);
        res.sauverLeCurl(mer);
    }

    public String FICHIER_RESULTAT_CURL = "curl.xls";

    public void sauverLaValeurAbsolueDuCurl(DataCarte mer, String nom_fichier) {
        ResultatFile res = new ResultatFile(nom_fichier);
        this.majCurl(mer);
        res.sauverLaValeurAbsolueDuCurl(mer);
    }

    public void sauverCurl(DataCarte mer) {
        sauverCurl(mer,FICHIER_RESULTAT_CURL);
    }

    public void sauverDiv(DataCarte mer, String nom_fichier) {
        ResultatFile res = new ResultatFile(nom_fichier);
        this.majDiv(mer);
        res.sauverLaDiv(mer);
    }
    public void sauverLaValeurAbsolueDeLaDivergence(DataCarte mer, String nom_fichier) {
        ResultatFile res = new ResultatFile(nom_fichier);
        this.majDiv(mer);
        res.sauverLaValeurAbsolueDeLaDivergence(mer);
    }

    public String FICHIER_RESULTAT_DIV = "div.xls";

    public void sauverDiv(DataCarte mer) {
        sauverDiv(mer,FICHIER_RESULTAT_DIV);
    }

    
}    
    
    
    
    
    
    

    /** recherche d'enveloppes !
     *  complete une collection de boucles.
     *  le seuil est absolut (> 0) !
     */
//    public void trouvertoutesEnveloppes (DataCarte mer, double seuil) {
//        int ret = trouverEnveloppesCycloniques(mer ,  seuil);
//        ret += trouverEnveloppesAntiCycloniques(mer ,  seuil);
//        System.out.println(ret + " enveloppes trouvees ");
//    }

    /** recherche d'enveloppes !
     *  complete une collection de boucles.
     *  le seuil est absolut (> 0) !
     */
//    public int trouverEnveloppesCycloniques (DataCarte mer, double seuil) {
//    return 0;}

    /** recherche d'enveloppes !
     *  complete une collection de boucles.
     *  le seuil est absolut (> 0) !
     */
//    public int trouverEnveloppesAntiCycloniques (DataCarte mer, double seuil) {
//    return 0;}

    /** recherche d'enveloppes avec seuil proportionnel � l'extrema du centre !
     *  complete une collection de boucles.
     *  le seuil est absolut (> 0) !
     */
//    public void trouvertoutesEnveloppesProportion (DataCarte mer, double seuil) {
//    }

    /** recherche d'enveloppes avec seuil proportionnel � l'extrema du centre !
     *  complete une collection de boucles.
     *  le seuil est absolut (> 0) !
     */
//    public void trouverEnveloppesCycloniquesProportion (DataCarte mer, double seuil) {
//    }

    /** recherche d'enveloppes avec seuil proportionnel � l'extrema du centre !
     *  complete une collection de boucles.
     *  le seuil est absolut (> 0) !
     */
//    public void trouverEnveloppesAntiCycloniquesProportion (DataCarte mer, double seuil) {
//    }



