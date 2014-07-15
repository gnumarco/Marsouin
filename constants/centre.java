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
package constants;

public interface centre extends default_values {



    static final long REFRESH_DELAY = 900;
    // delay de rafraichissement pour le CanvasCarte, utilise par le thread ThreadRefresh

    public static long ANIME_DELAY = 1500;
    // delay de rafraichissement pour le CanvasCarte, utilise par le thread ThreadAnime


    //*******************************************************************************************
    // constantes de maniere d'affichage AFF ou flagAff
    // INDICES
    static final int AFF_CENTRE=0; // pour le tableau de flag int[] indice
    static final int AFF_COMBINER_GC=1; // indice
    static final int AFF_FOND =2; // indice pour determiner ce qui est affiche en fond : image, donnee (couleur = f(valeur) ) , rien
    static final int AFF_ZONE_CONCENTRATION =3; // indice pour determiner si on cherche des zone concentrees
        
    static final int LENGTH_FLAG_AFFICH = 4;// longueur du tableau
    
    // valeurs :
    static final int AFF_C_CAMEMBERT=1;
    static final int AFF_C_CAMEMBERT_ANIME = 2;
    static final int AFF_C_PILE = 3;
    static final int AFF_C_PILE_ANIME = 4;

    // valeurs pour le fond :
    public static final int AFF_RIEN = 0;
		// fond et AFF_CONCENTRATION :    
	    static final int AFF_CURL = courant.TB2D_CURL;
	    static final int AFF_DIV = courant.TB2D_DIV;
	    static final int AFF_CURL_CU = courant.TB2D_CURL_CU;
	    static final int AFF_DIV_CU = courant.TB2D_DIV_CU;
	// fond
    static final int AFF_EST_OUEST = courant.TB2D_EST_OUEST;
    static final int AFF_NORD_SUD = courant.TB2D_NORD_SUD;
    static final int AFF_NORME = courant.TB2D_NORME;

    //*****************************METHODES GEOMETRIQUES****************************************
    static final int NB_TYPES = 4;
    static final int NB_METHODES = 6;
    // flag pour differencier les 6 methodes et 4 variantes (+ x o [] => mod = 4)
    //***************************************************
    //** appel : numero_Methode*nb_types + numero_type **
    //***************************************************

    static final int COMBINER_GC = NB_METHODES*NB_TYPES ;
    //ajout du booleen qui indique si le centre a assez de points

    // ******************************************************longueur de tableaux
    static final int LENGTH_LIMITC = NB_METHODES*NB_TYPES ;
    static final int LENGTH_COMBINER_GC = LENGTH_LIMITC;
    // longueur de cboxc et cfgconfig (avec COMBINER_GC)
    static final int LENGTH_CFGCENTRE = NB_METHODES*NB_TYPES + 1;

    // somme des vecteurs de courant
    static final int VECTEURS=0;
    // somme des vecteurs unitaires de courant
    static final int VECTEURS_CU =1;

    // somme des vecteurs tangents a la circonference du phenomene
    static final int VECTEURS_TANGENTS=2;
    // somme des vecteurs unitaires tangents a la circonference du phenomene
    static final int VECTEURS_TANGENTS_CU =3;

    // somme des vecteurs fuyant le centre du phenomene
    static final int VECTEURS_CENTRIFUGES =4;
    // somme des vecteurs unitaires fuyant le centre du phenomene
    static final int VECTEURS_CENTRIFUGES_CU =5;

    //centre de 4 points (le centre est un point de mesure)
    static final int TYPE_PLUS =0;
    static final int TYPE_X =1;
    // centre de 8 points (le centre est un point de mesure)
    static final int TYPE_O =2;

    //centre de 4 points en carre (le centre N'EST PAS un point de mesure)
    static final int TYPE_CARRE =3;

    static final String[] COMMENT_METHODE = {" Somme des Vecteurs ", " Somme des Vecteurs Tangents " , " Somme des Vecteurs centrifuges " };

    // facteur multiplicatif qui simule des mesures equidistantes du centre, dans l'etude de 8 points
    static final double FACTEUR_CORRECTIF  = 0.9; //(1.0/java.lang.Math.sqrt(2.0));

    // facteur multiplicatif qui determine l'importance accordee au courant du point central
    static final double[] FACTEUR_CENTRE = {
        0.1  ,   // "V_" + "PLUS ",
        0.1  ,   // "V_" + "X ",
        0.1  ,   // "V_" + "O ",
        0.0  ,   // "V_" + "CARRE ",
        0.1  ,   // "V_U_" + "PLUS ",
        0.1  ,   // "V_U_" + "X ",
        0.1  ,   // "V_U_" + "O ",
        0.0  ,   // "V_U_" + "CARRE ",
        0.5  ,   // "V_T_"+ "PLUS ",
        0.5  ,   // "V_T_"+ "X ",
        0.5  ,   // "V_T_"+ "O ",
        0.0  ,   // "V_T_"+ "CARRE ",
        0.1  ,   // "V_T_U_" + "PLUS ",
        0.1  ,   // "V_T_U_" + "X ",
        0.1  ,   // "V_T_U_" + "O ",
        0.0  ,   // "V_T_U_" + "CARRE ",
        0.1  ,   // "V_CF_"+ "PLUS ",
        0.1  ,   // "V_CF_"+ "X ",
        0.1  ,   // "V_CF_"+ "O ",
        0.0  ,   // "V_CF_"+ "CARRE ",
        0.1  ,   // "V_CF_U_"+ "PLUS ",
        0.1  ,   // "V_CF_U_"+ "X ",
        0.1  ,   // "V_CF_U_"+ "O ",
        0.0  // "V_CF_U_"+ "CARRE "
    };

    // facteur DIVISEUR qui pondere le vecteur final par rapport au seuil fixe  >> ponderer (les alentours + le centre)
    static final double[] POIDS_TOTAL = {
        0.40+0.05  ,   // "V_" + "PLUS ",
        0.40+0.05  ,   // "V_" + "X ",
        0.40+0.05  ,   // "V_" + "O ",
        0.40  ,   // "V_" + "CARRE ",
        4.0+0.5  ,   // "V_U_" + "PLUS ",
        4.0+0.5  ,   // "V_U_" + "X ",
        9.0+0.5  ,   // "V_U_" + "O ",
        4.0 ,   // "V_U_" + "CARRE ",
        (4.0+0.5)*2.0  ,   // "V_T_"+ "PLUS ",
        (4.0+0.5)*2.0  ,   // "V_T_"+ "X ",
        (9.0+0.5)*2.0  ,   // "V_T_"+ "O ",
        (4.0)*2.0  ,   // "V_T_"+ "CARRE ",
        (4.0+0.5)/4.0  ,   // "V_T_U_" + "PLUS ",
        (4.0+0.5)/4.0  ,   // "V_T_U_" + "X ",
        (9.0+0.5)/4.0  ,   // "V_T_U_" + "O ",
        (4.0)/4.0  ,   // "V_T_U_" + "CARRE ",
        (4.0+0.5)/8.0  ,   // "V_CF_"+ "PLUS ",
        (4.0+0.5)/8.0  ,   // "V_CF_"+ "X ",
        (9.0+0.5)/8.0  ,   // "V_CF_"+ "O ",
        (4.0)/8.0  ,   // "V_CF_"+ "CARRE ",
        (4.0+0.5)/10.0  ,   // "V_CF_U_"+ "PLUS ",
        (4.0+0.5)/10.0  ,   // "V_CF_U_"+ "X ",
        (9.0+0.5)/10.0  ,   // "V_CF_U_"+ "O ",
        (4.0)/10.0  // "V_CF_U_"+ "CARRE "
    };

    static final String[] COMMENT = {
        " Centre selon "+" SOMME_VECTEURS " + " TYPE_PLUS ",
        " Centre selon "+" SOMME_VECTEURS " + " TYPE_X ",
        " Centre selon "+" SOMME_VECTEURS " + " TYPE_O ",
        " Centre selon "+" SOMME_VECTEURS " + " TYPE_CARRE ",
        " Centre selon "+" SOMME_VECTEURS_UNITAIRES " + " TYPE_PLUS ",
        " Centre selon "+" SOMME_VECTEURS_UNITAIRES " + " TYPE_X ",
        " Centre selon "+" SOMME_VECTEURS_UNITAIRES " + " TYPE_O ",
        " Centre selon "+" SOMME_VECTEURS_UNITAIRES " + " TYPE_CARRE ",
        " Centre selon "+" SOMME_VECTEURS_TANGENTS "+ " TYPE_PLUS ",
        " Centre selon "+" SOMME_VECTEURS_TANGENTS "+ " TYPE_X ",
        " Centre selon "+" SOMME_VECTEURS_TANGENTS "+ " TYPE_O ",
        " Centre selon "+" SOMME_VECTEURS_TANGENTS "+ " TYPE_CARRE ",
        " Centre selon "+" SOMME_VECTEURS_TANGENTS_UNITAIRES " + " TYPE_PLUS ",
        " Centre selon "+" SOMME_VECTEURS_TANGENTS_UNITAIRES " + " TYPE_X ",
        " Centre selon "+" SOMME_VECTEURS_TANGENTS_UNITAIRES " + " TYPE_O ",
        " Centre selon "+" SOMME_VECTEURS_TANGENTS_UNITAIRES " + " TYPE_CARRE ",
        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES "+ " TYPE_PLUS ",
        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES "+ " TYPE_X ",
        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES "+ " TYPE_O ",
        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES "+ " TYPE_CARRE ",
        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES_UNITAIRES "+ " TYPE_PLUS ",
        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES_UNITAIRES "+ " TYPE_X ",
        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES_UNITAIRES "+ " TYPE_O ",
        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES_UNITAIRES "+ " TYPE_CARRE ",
        " Centre selon "+" COMBINAISON de methodes geometriques "
    };

    static final String[] STRINGCENTRE = {
        "V_" + "PLUS ",
        "V_" + "X ",
        "V_" + "O ",
        "V_" + "CARRE ",
        "V_U_" + "PLUS ",
        "V_U_" + "X ",
        "V_U_" + "O ",
        "V_U_" + "CARRE ",
        "V_T_"+ "PLUS ",
        "V_T_"+ "X ",
        "V_T_"+ "O ",
        "V_T_"+ "CARRE ",
        "V_T_U_" + "PLUS ",
        "V_T_U_" + "X ",
        "V_T_U_" + "O ",
        "V_T_U_" + "CARRE ",
        "V_CF_"+ "PLUS ",
        "V_CF_"+ "X ",
        "V_CF_"+ "O ",
        "V_CF_"+ "CARRE ",
        "V_CF_U_"+ "PLUS ",
        "V_CF_U_"+ "X ",
        "V_CF_U_"+ "O ",
        "V_CF_U_"+ "CARRE ",
        "Combiner Geom"
    };

	static final String[] STRING_FOND = {
			 	"",	// ne rien afficher AFF_RIEN = 0
				"vorticite" , // TB2D_CURL =1;
				"divergence" ,		// TB2D_DIV =2;
				"vitesse Est->Ouest" ,	// TB2D_EST_OUEST =3;
				"vitesse Nord->Sud" ,	// TB2D_NORD_SUD =4;
				"norme du courant" ,	// TB2D_NORME =5;
				"vorticite courant unitaire" ,	// TB2D_CURL_CU =6;
				"divergence courant unitaire"  // TB2D_DIV_CU =7;
	};

     static final String[] COMMENT_FOND = {
		 	"",	// ne rien afficher AFF_RIEN = 0
			"degrade de couleur de la vorticite en fond de carte" , // TB2D_CURL =1;
			"degrade de couleur de la divergence en fond de carte" ,		// TB2D_DIV =2;
			"degrade de couleur de la vitesse Est->Ouest en fond de carte" ,	// TB2D_EST_OUEST =3;
			"degrade de couleur de la vitesse Nord->Sud en fond de carte" ,	// TB2D_NORD_SUD =4;
			"degrade de couleur de la norme en fond de carte" ,	// TB2D_NORME =5;
			"degrade de couleur de la vorticite du courant unitaire en fond de carte" ,	// TB2D_CURL_CU =6;						// TB2D_DIV_CU =7;
			"degrade de couleur de la divergence du courant unitaire en fond de carte"  // TB2D_DIV_CU =7;


			/*
	        " Centre selon "+" SOMME_VECTEURS " + " TYPE_PLUS ",
	        " Centre selon "+" SOMME_VECTEURS " + " TYPE_X ",
	        " Centre selon "+" SOMME_VECTEURS " + " TYPE_O ",
	        " Centre selon "+" SOMME_VECTEURS " + " TYPE_CARRE ",
	        " Centre selon "+" SOMME_VECTEURS_UNITAIRES " + " TYPE_PLUS ",
	        " Centre selon "+" SOMME_VECTEURS_UNITAIRES " + " TYPE_X ",
	        " Centre selon "+" SOMME_VECTEURS_UNITAIRES " + " TYPE_O ",
	        " Centre selon "+" SOMME_VECTEURS_UNITAIRES " + " TYPE_CARRE ",
	        " Centre selon "+" SOMME_VECTEURS_TANGENTS "+ " TYPE_PLUS ",
	        " Centre selon "+" SOMME_VECTEURS_TANGENTS "+ " TYPE_X ",
	        " Centre selon "+" SOMME_VECTEURS_TANGENTS "+ " TYPE_O ",
	        " Centre selon "+" SOMME_VECTEURS_TANGENTS "+ " TYPE_CARRE ",
	        " Centre selon "+" SOMME_VECTEURS_TANGENTS_UNITAIRES " + " TYPE_PLUS ",
	        " Centre selon "+" SOMME_VECTEURS_TANGENTS_UNITAIRES " + " TYPE_X ",
	        " Centre selon "+" SOMME_VECTEURS_TANGENTS_UNITAIRES " + " TYPE_O ",
	        " Centre selon "+" SOMME_VECTEURS_TANGENTS_UNITAIRES " + " TYPE_CARRE ",
	        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES "+ " TYPE_PLUS ",
	        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES "+ " TYPE_X ",
	        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES "+ " TYPE_O ",
	        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES "+ " TYPE_CARRE ",
	        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES_UNITAIRES "+ " TYPE_PLUS ",
	        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES_UNITAIRES "+ " TYPE_X ",
	        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES_UNITAIRES "+ " TYPE_O ",
	        " Centre selon "+" SOMME_VECTEURS_CENTRIFUGES_UNITAIRES "+ " TYPE_CARRE ",
	        " Centre selon "+" COMBINAISON de methodes geometriques "
	        */
	    };

	static final String[] STRING_ZONE_CONCENTRATION = {
				"concentration de divergence" ,	// DIV =0;
			 	"concentration de vorticite" , // CURL =1;
				"concentration de divergence courant unitaire" , // DIV_CU =2;
				"concentration de vorticite courant unitaire" // CURL_CU =3;
	};
	static final String[] COMMENT_ZONE_CONCENTRATION = {
				"concentration de divergence" ,	// DIV =0;
			 	"concentration de vorticite" , // CURL =1;
				"concentration de divergence courant unitaire" , // DIV_CU =2;
				"concentration de vorticite courant unitaire" // CURL_CU =3;
	};
     

}
