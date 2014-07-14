/*
 * couleur.java
 *
 * Created on 30 septembre 2002, 16:39
 */

/*
 * @author Mahler,Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package constants;

import java.awt.*;

public interface couleur {

    public static final double COEFF_AFFICHAGE_COURANT = 10.0;
    // pour les elements graphiques geres par autre chose que le canvas : recuperes par getCVprops();
    // indice de tableau de dimension :
    public static final int CV_CASE_WIDTH = 0;
    public static final int CV_CASE_HEIGHT = 1;
    public static final int CV_TAILLE_Y = 2;
    // longueur du tableau
    public static final int LENGTH_CV_PROPS = 3;

    //*********************************************************************************************************************
    //                       COULEURS GENERALES
    //*********************************************************************************************************************

    public static final int ICONE_LEGENDE_SENS = -1;

    // Tableau d'utilisation des methodes;
    public static final int USE_METHOD_GEOMETRIQUE = 0;
    public static final int USE_METHOD_FOURMI = 1;
    public static final int USE_METHOD_PHYSIQUE = 2;
	public static final int USE_METHOD_STREAMLINES = 3;
    
    // longueur du tableau
    public static final int LENGTH_USE_METHOD = 4;


    //barre de statut
    public static final Color COLOR_ERROR = Color.red;
    public static final Color COLOR_INFO = Color.black;

    // couleur de fond
    public static final Color COLOR_OCEAN = new Color(0,100,200); // bleu clair :> new Color(196,226,255);
    // vecteurs de courant
    //public static final Color COLOR_VECTEUR =  bleu :>new Color(0,100,200);
    public static final Color COLOR_VECTEUR =Color.white;
    // vecteurs de courant mis en evidence
    public static final Color COLOR_VECTEUR_CLICK = Color.magenta;
    // quand on clic : un rectangle apparait
    public static final Color COLOR_C_CLICK = Color.red;
    // terre
    public static final Color COLOR_TERRE = new Color(128,64,0);

    // BORDURES
    public static Color COLOR_BORDER_DEFAULT = new Color(153,153,153);

    public static Color COLOR_BORDER_GET_PARAM = new Color(255,153,0);
    public static Color COLOR_BORDER_MODIF_PARAM = new Color(255,153,0);
    public static Color COLOR_BORDER_SET_PARAM = new Color(51,255,0);


    public static Color COLOR_CFG_BORDER_GET_PARAM = new Color(255,153,0);
    public static Color COLOR_CFG_BORDER_SET_PARAM = new Color(51,255,0);
    public static Color COLOR_CFG_BORDER_MODIF_PARAM = new Color(255,255,0);

    public static final String TITRE_FRMCONFIG = "Marsouin Configuration ";
    public static final String TITRE_FRMVISU = "Marsouin, carte :";
    public static final String MENU_SAUVER = "Enregistrer ";

    public static final int MIN_LARGEUR_CASE = 10;
    public static final int MIN_HAUTEUR_CASE = 10;

    //*********************************************************************************************************************
    //                       MULTI-AGENT : FOURMI
    //*********************************************************************************************************************

    public static final Color COLOR_PHEROMONE_STD = new Color(0,255,100);

    public static final Color COLOR_FOURMI_ANIMEE_VORTEX_TROUVE = Color.white;
    public static final Color COLOR_FOURMI_ANIMEE_REINIT = Color.green;

    public static final Color[] COLOR_FOURMIS_ANIMEES = {
          Color.white,Color.orange,Color.cyan, Color.yellow,Color.magenta,Color.green,Color.red,Color.blue};

    public static final Color COLOR_BOUCLE_STD = Color.red;
    public static final Color COLOR_BOUCLE_STD_2 = Color.black;
    public static final Color COLOR_CENTRE_BOUCLE_STD = Color.orange;
    public static final Color COLOR_NUM_BOUCLE = Color.black;

    //*********************************************************************************************************************
    //                       ANALYSE GEOMETRIQUE
    //*********************************************************************************************************************

    // couleur pour une info NEGATIVE;
    public static final Color COLOR_NEUTRE = Color.gray;//black

    // COULEURS pours CHAQUE INFO POSITIVE
    public static final Color[] COLOR_C_PILE =
        { Color.green,Color.red,Color.cyan,Color.yellow,Color.magenta};

    public static final Color[] COLOR_C_PILE_ANIME = {
     // 3 couleurs primaires : R V J
         // 4 nuances : clair, fonce, lumineux, grise
     //jaune
     new Color(255,255,204),new Color(204,204,0),new Color(255,255,51),new Color(153,153,0),
     new Color(255,255,204),new Color(204,204,0),new Color(255,255,51),new Color(153,153,0),
     //vert
     new Color(204,255,204),new Color(0,204,0),new Color(102,255,51),new Color(102,153,0),
     new Color(204,255,204),new Color(0,204,0),new Color(102,255,51),new Color(102,153,0),
     //rouge
     new Color(255,204,204),new Color(204,0,0),new Color(255,51,51),new Color(153,0,51),
     new Color(255,204,204),new Color(204,0,0),new Color(255,51,51),new Color(153,0,51)
     };
     // pour onglets et etiquettes : 1 couleur/ 1 methode
     public static final Color[] COLOR_C_LT_PILE_ANIME = {
         new Color(255,255,51),new Color(255,255,51),
         new Color(102,255,51),new Color(102,255,51),
         new Color(255,51,51),new Color(255,51,51)
     };

    //8 couleurs
    //ici l'important est la sequence !
    public static final Color[] COLOR_C_CAMEMBERT = {
          Color.white,Color.orange,Color.cyan, Color.yellow,Color.magenta,Color.green,Color.red,Color.blue};

     public static final Color[] COLOR_C_CAMEMBERT_ANIME = {
     // 3 couleurs primaires : R V J
         // 4 nuances : clair, fonce, lumineux, grise
     //jaune
     new Color(255,255,204),new Color(204,204,0),new Color(255,255,51),new Color(153,153,0),
     new Color(255,255,204),new Color(204,204,0),new Color(255,255,51),new Color(153,153,0),
     //vert
     new Color(204,255,204),new Color(0,204,0),new Color(102,255,51),new Color(102,153,0),
     new Color(204,255,204),new Color(0,204,0),new Color(102,255,51),new Color(102,153,0),
     //rouge
     new Color(255,204,204),new Color(204,0,0),new Color(255,51,51),new Color(153,0,51),
     new Color(255,204,204),new Color(204,0,0),new Color(255,51,51),new Color(153,0,51)
     };
    // pour onglets et etiquettes : 1 couleur/ 1 methode
     public static final Color[] COLOR_C_LT_CAMEMBERT_ANIME = {
         new Color(255,255,51),new Color(255,255,51),
         new Color(102,255,51),new Color(102,255,51),
         new Color(255,51,51),new Color(255,51,51)
     };

     public static final Color[] COLOR_C_NEUTRE = {
	Color.lightGray,Color.lightGray,Color.lightGray,Color.lightGray,
	Color.lightGray,Color.lightGray,Color.lightGray,Color.lightGray,
	Color.lightGray,Color.lightGray,Color.lightGray,Color.lightGray,
	Color.lightGray,Color.lightGray,Color.lightGray,Color.lightGray,
	Color.lightGray,Color.lightGray,Color.lightGray,Color.lightGray,
	Color.lightGray,Color.lightGray,Color.lightGray,Color.lightGray
     };

     // pour onglets et etiquettes : 1 couleur/ 1 methode
     public static final Color[] COLOR_C_LT_NEUTRE = {
         Color.yellow,Color.yellow,
         Color.green,Color.green,
         Color.red,Color.red
     };

    // QUAND on Combine les methodes
    public static final Color COLOR_COMBINER_GC = Color.red;

    public static final Color[] COLOR_CENTRE =
        { Color.green,Color.red,Color.cyan,Color.white,
          Color.green,Color.red,Color.cyan,Color.white,
          Color.yellow,Color.magenta,Color.green,Color.white,
          Color.yellow,Color.magenta,Color.green,Color.white,
          Color.red,Color.cyan,Color.yellow,Color.white,
          Color.red,Color.cyan,Color.yellow,Color.white
        };
        
    //*********************************************************************************************************************
    //                       METHODES PHYSIQUEs
    //*********************************************************************************************************************
	public static final Color[] COLOR_CENTRE_PHYSIQUE =
       {    		  		/* rappel */   
		    Color.blue , 	// DIV = 0;
		    Color.red  , 	//CURL = 1;
		    Color.cyan  , 	//DIV_CU = 2;
		    Color.magenta 	//CURL_CU = 3;
		};
	// couleur pour afficher le sigma ou toute valeur unique entiere;
	public static final Color COLOR_TAB_INT_STD = Color.orange;
	
    //*********************************************************************************************************************
    //                       LE FOND : degrade
    //*********************************************************************************************************************

     /** TYPES DE DEGRADES sur les donnees affichees en fond */
    public static final int NOIR_BLANC_NOIR = 0;
	public static final int	BLEU_BLANC_NOIR = 1;
	public static final int	BLEU_BLANC_VERT = 2;
	public static final int	ROUGE_BLANC_BLEU = 3;
	public static final int	BLEU_GRIS_NOIR = 4;
	
	

}
