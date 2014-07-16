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
package com.marsouin.constants;

public interface DefaultValues {

// **************************************GENERAL*************************************

    // CAPTURER toutes les recherches !!
    static final boolean SAUVER_CHAQUE_RECHERCHE = true;

    /** booleens qui indiquent les methodes � employer par defaut .
     * ils sont cochent par d�faut les cases des methodes, en bas de la fenetre de configuration
     */
    static final boolean[] DEFAULT_USE_METHOD = {
        false,    // METHODE GEOMETRIQUE
        true,    // METHODE DES FOUMRIS
        false,     // METHODE PHYSIQUE (rot, div etc...)
        false,
	false
    };

    // Pourra servir pour tout ! //
	/** textes de l�gendes sur frmLegende */
    static final String[] LEGENDE = {
        "Vecteurs_" + "PLUS ",
        "Vecteurs_" + "X ",
        "Vecteurs_" + "O ",
        "Vecteurs_" + "CARRE ",
        "Vecteurs_U_" + "PLUS ",
        "Vecteurs_U_" + "X ",
        "Vecteurs_U_" + "O ",
        "Vecteurs_U_" + "CARRE ",
        "V_Tangents_"+ "PLUS ",
        "V_Tangents_"+ "X ",
        "V_Tangents_"+ "O ",
        "V_Tangents_"+ "CARRE ",
        "V_Tangents_U_" + "PLUS ",
        "V_Tangents_U_" + "X ",
        "V_Tangents_U_" + "O ",
        "V_Tangents_U_" + "CARRE ",
        "V_Centrifuges_"+ "PLUS ",
        "V_Centrifuges_"+ "X ",
        "V_Centrifuges_"+ "O ",
        "V_Centrifuges_"+ "CARRE ",
        "V_Centrifuges_U_"+ "PLUS ",
        "V_Centrifuges_U_"+ "X ",
        "V_Centrifuges_U_"+ "O ",
        "V_Centrifuges_U_"+ "CARRE ",
        "Combiner GeomC, seuil:"
    };

    
	/** valeur du minimum de ponderation que l'on peut attribuer @ une methode :
	 * cela conditionne la JSlideBar */
    static final int MIN_COMBINER_GC = 1;
  
    /** valeur du mAxImUm de ponderation que l'on peut attribuer @ une methode :
	 * cela conditionne la JSlideBar */
    static final int MAX_COMBINER_GC = 4;

	/** valeur par defaut du SEUIL de ponderation :
  minimum de points de pertinence @ posseder pour afficher un Centre
	 */
    static final int DEFAULT_SEUIL_COMBINER_GC = 2;

	/** valeur du curseur de ponderation, par defaut, que l'on peut attribuer @ une methode :
	 * cela positionne le curseur de la JSlideBar */
    static final int[] DEFAULT_COMBINER_GC = {
        1,    // "V_" + "PLUS ",
        1,    // "V_" + "X ",
        1,    // "V_" + "O ",
        1,    // "V_" + "CARRE ",
        1,    // "V_U_" + "PLUS ",
        1,    // "V_U_" + "X ",
        1,    // "V_U_" + "O ",
        1,    // "V_U_" + "CARRE ",
        1,    // "V_T_"+ "PLUS ",
        1,    // "V_T_"+ "X ",
        1,    // "V_T_"+ "O ",
        1,    // "V_T_"+ "CARRE ",
        1,    // "V_T_U_" + "PLUS ",
        1,    // "V_T_U_" + "X ",
        1,    // "V_T_U_" + "O ",
        1,    // "V_T_U_" + "CARRE ",
        1,    // "V_CF_"+ "PLUS ",
        1,    // "V_CF_"+ "X ",
        1,    // "V_CF_"+ "O ",
        1,    // "V_CF_"+ "CARRE ",
        1,    // "V_CF_U_"+ "PLUS ",
        1,    // "V_CF_U_"+ "X ",
        1,    // "V_CF_U_"+ "O ",
        1    // "V_CF_U_"+ "CARRE ",
    };

    /** valeur du seuil attribu� � chaque methode */
    static final double[] DEFAULT_LIMITC = {
        0.2,    // "V_" + "PLUS ",
        0.2,    // "V_" + "X ",
        0.2,    // "V_" + "O ",
        0.2,    // "V_" + "CARRE ",
        0.5,    // "V_U_" + "PLUS ",
        0.5,    // "V_U_" + "X ",
        0.5,    // "V_U_" + "O ",
        0.5,    // "V_U_" + "CARRE ",
        0.2,    // "V_T_"+ "PLUS ",
        0.2,    // "V_T_"+ "X ",
        0.2,    // "V_T_"+ "O ",
        0.2,    // "V_T_"+ "CARRE ",
        0.43,    // "V_T_U_" + "PLUS ",
        0.35,    // "V_T_U_" + "X ",
        0.2,    // "V_T_U_" + "O ",
        0.3,    // "V_T_U_" + "CARRE ",
        0.2,    // "V_CF_"+ "PLUS ",
        0.2,    // "V_CF_"+ "X ",
        0.2,    // "V_CF_"+ "O ",
        0.5,    // "V_CF_"+ "CARRE ",
        0.25,    // "V_CF_U_"+ "PLUS ",
        0.4,    // "V_CF_U_"+ "X ",
        0.5,    // "V_CF_U_"+ "O ",
        0.35    // "V_CF_U_"+ "CARRE ",
    };

    /** selection de chaque methode :
     * cela conditionne case @ cocher */
    static final boolean[] DEFAULT_CHECKBOXC = {
        false,    // "V_" + "PLUS ",
        false,    // "V_" + "X ",
        false,    // "V_" + "O ",
        false,    // "V_" + "CARRE ",
        false,    // "V_U_" + "PLUS ",
        false,    // "V_U_" + "X ",
        false,    // "V_U_" + "O ",
        false,    // "V_U_" + "CARRE ",
        false,    // "V_T_"+ "PLUS ",
        false,    // "V_T_"+ "X ",
        false,    // "V_T_"+ "O ",
        false,    // "V_T_"+ "CARRE ",
        true,    // "V_T_U_" + "PLUS ",
        true,    // "V_T_U_" + "X ",
        false,    // "V_T_U_" + "O ",
        false,    // "V_T_U_" + "CARRE ",
        false,    // "V_CF_"+ "PLUS ",
        false,    // "V_CF_"+ "X ",
        false,    // "V_CF_"+ "O ",
        false,    // "V_CF_"+ "CARRE ",
        false,    // "V_CF_U_"+ "PLUS ",
        false,    // "V_CF_U_"+ "X ",
        false,    // "V_CF_U_"+ "O ",
        false,    // "V_CF_U_"+ "CARRE ",
        false     // COMBINAISON de methodes geometriques

    };

    //***************************************FOURMIS**************************************
    static final boolean[] DEFAULT_FOURMI_BAFF = {
        false,     // FOURMI_IMPOSE_COURBURE               // pour le tableau de FOURMIS_BAFF bool[] indice
        false,     //  FOURMI_AFF_ANIM
        true,     //  FOURMI_AFF_BOUCLES
        true,     //  FOURMI_AFF_CENTRES_BOUCLES
        false,     //  FOURMI_AFF_VORTEX
        false,     //  FOURMI_AFF_PHEROMONE
        false,      //  FOURMI_AFF_LEGENDE
        false      //  FOURMI_AFF_SUIVI
    };
    // constantes d'effectif
    static final int[] DEFAULT_FOURMI_NB = {
        4, // FOURMI_NB_ESPECES nombre de pheromones distinctes // pour le tableau de Fourmis_NB int[] indice
        1000, // FOURMI_NB_GENERATIONS;
        1,    // FOURMI_NB_ANIMEES
        5,   // nombre de fourmis par esp�ce
        4   // nombre de runs pas carte (multistart)
    };
    // constantes float
    static final double[] DEFAULT_FOURMI_COEFF = {
         0.01,   // FOURMI_COEFF_EVAPORATION // pour le tableau de Fourmis_Coef float[] indice
         10.0,   // FOURMI_QTE_DEPOT de pheromone
         0.0   //  FOURMI_TAUX_COURBURE; -1.0,+1.0
    };
    
    //********************streamlines
    
    static final double[] DEFAULT_STREAMLINES_PARAM = {
         0.5,   // STREAM_SEUIL_BOUCLAGE
         0.5,   // STREAM_SEUIL_POINT_CRITIQUE
         1.0   //  STREAM_PRECISION
    };

    //**************************************Fond*********************************************
    
    //***************************************PHYSIQUE, meca fluides ...********************************
    /** indique quelle donn�e est affich�e en fond de carte, parmis :
	constantes.Stream.TB2D_CURL (ou + "_CU")
  constantes.Stream.TB2D_DIV  (ou + "_CU")
  constantes.Stream.TB2D_EST_OUEST
  constantes.Stream.TB2D_NORD_SUD
  constantes.Stream.TB2D_NORME
	ou
	constantes.Centre.AFF_RIEN : Colors unie ou image !
     */
    static final int DEFAULT_AFFICHE_EN_FOND = com.marsouin.constants.Stream.TB2D_CURL;
    



	/** QUELS EXTREMA MONTRER ? */
     static final boolean[] DEFAULT_PHYSIQUE_BAFF =
		 {    		  		/* rappel */
		 		    false , 	// DIV
		 		    false  , 	//CURL
		 		    false  , 	//DIV_CU
		 		    false ,	    //CURL_CU
		 		    true ,      //BAFF_CONCENTRATION
		 		    false       //CONCENTRATION_SEUIL_ABSOLU
		};

     static final double[] DEFAULT_LIMIT_PHYSIQUE = {
         0.17, // indice : LIMIT_SEUIL
         0.5  //  indice : LIMIT_COMPACITE
     };


     /* QUEL DEGRADE DE COULEURS POUR LE FOND DE CARTE ?
      *parmis
      * constantes.Colors.NOIR_BLANC_NOIR
	  * constantes.Colors.BLEU_BLANC_NOIR
	  *	constantes.Colors.BLEU_BLANC_VERT
	  *	constantes.Colors.ROUGE_BLANC_BLEU
	  */
     static final int TYPE_DEGRADE = com.marsouin.constants.Colors.BLEU_GRIS_NOIR;

     //static final boolean AFFICHER_SIGMA =true;

     //static final boolean AFFICHER_ZONES_SIMGA = false;

     //static final boolean AFFICHER_VORTEX_PHYSIQUE =false;


//***************************** GENERAL ********************
		//FLAG_AFFICHE :
		/* fla[AFF_CENTRE]=AFF_C_CAMEMBERT;
           fla[AFF_COMBINER_GC]= SEUIL_COMBINER_GC;
           fla[AFF_FOND]= DEFAULT_AFFICHE_EN_FOND;
           fla[AFF_ZONE_CONCENTRATION] = DEFAULT_ZONE_CONCENTRATION;*/
        static final int[] DEFAULT_FLAG_AFFICH = {
        	Centre.AFF_C_CAMEMBERT,
        	DEFAULT_SEUIL_COMBINER_GC,
        	DEFAULT_AFFICHE_EN_FOND,
        };

}
