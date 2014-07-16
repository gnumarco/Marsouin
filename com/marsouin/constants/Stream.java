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

public interface Stream {

    // pour les fourmis
    //public static final double SEUIL_FAIBLE_COURANT = 0.001;

    static final double SEUIL_NORME = 0.0000001;
    // permet de calculer les composantes unitaires si le vecteur n'est pas nul
    // permet aussi de reperer la terre. (vecteur nul)

    // taille de vecteur
    static final double OFFSET_V = 2.5;

    /** RAPPEL DES CONVENTIONS **/
    static final String uniteDistance = "metre";
    static final String uniteVitesse = "metre / seconde";

    /** pour les Boucles : pas de grille = 1km // unite distance= metre
      * l'unite de l'indice equivaut a PAS_DE_GRILLE unites de distance !  **/
    static final double PAS_DE_GRILLE = 1000.0;

    /** pour choisir quelle donnees mettre dans la table d'interpolation Datacarte.table */
    public static final int TB2D_CURL =1;
    public static final int TB2D_DIV =2;
    public static final int TB2D_EST_OUEST =3;
    public static final int TB2D_NORD_SUD =4;
    public static final int TB2D_NORME =5;
    public static final int TB2D_CURL_CU =6;
    public static final int TB2D_DIV_CU =7;

    // ************************* TABLODOUBLE 2D ******************************
    /** tolerance d'interpolation du TabloDouble*/
    static final double PROXIMITE_POUR_INTERPOLATION = 0.001;

  	/** diviseur de taille de carte, pour l'interpolation de grandTab, pour TabloDouble2D */
  	public static final int DENOMINATEUR_TAILLE_INTERPOLATION =2;

  	/** booleen qui permet d'utiliser une interpolation dans grandTab, lors de l'affichage de donnees en fond de carte */
  	public static final boolean AFFICHER_FOND_AVEC_INTERPOLATION_GRAND_TABLEAU = false;
    
    /** recherche d'extremas locaux */
    static final int BORNE_INF = -1;
    static final int BORNE_SUP = +1;
    // pourcentage de max pour differencier les seuils
    static final double SEUIL_MAX_LOCAL = 0.01; // = 10%
	// pourcentage de max pour differencier les seuils
    static final double SEUIL_MIN_LOCAL = 0.01; // = 10%

}
