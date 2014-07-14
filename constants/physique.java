/*
 * physique.java
 *
 * Created on 8 april 2003, 16:13
 */

/** sert � d�finir les constantes du pachackge de methodes physiques.
 * @author Mahler
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package constants;

public interface physique {

    /** petite variation de la variable a deriver, si il y a une interpolation !*/
    static final double PETITE_VARIATION_H = 0.0001;

    /** pour utiliser la derivee avec une interpolation du champ ! */
    static final byte AVEC_INTERPOLATION = 127;
    /** pour utiliser la derivee sans interpolation du champ ! */
    static final byte SANS_INTERPOLATION = 0;

    /** pour utiliser la derivee simple ! (f_x+h - f_x )/ h */
    static final byte DERIVEE_SIMPLE = 1;

    /** pour utiliser la derivee symetrique ! (f_x+h - f_x-h ) / 2*h */
    static final byte DERIVEE_SYMETRIQUE = 2;

    /** pour utiliser la derivee a partir de la discretisation de Corpetti
     * 1/6 * (3*f_x +1*f_x-2pas -6*f_x-1pas + 2*f_x+1pas )
     * s'utilise TOUJOURS SANS_INTERPOLATION
     */
    static final byte DERIVEE_DISCRETISATION_CORPETTI = 3;

    /** indices du tableau de ! resultats ! TabloDouble2D */
    // ********* ILS SERVENT AUSSI au tableau de visualisation des extremas, dans memoire et frmVisu !
    static final int DIV = 0;
    static final int CURL = 1;
    static final int DIV_CU = 2;
    static final int CURL_CU = 3;
    //indices de segmentation :
    static final int BAFF_CONCENTRATION =4;
	static final int CONCENTRATION_SEUIL_ABSOLU =5;


    // tag a ajouter pour les VortexPhys >> utiliser ensuite centreIsMin() centreIsMax()
    static final int TAG_MIN = 0;
    static final int TAG_MAX = 128;


    /** longueur du tableau du moteur physique*/
    static final int LENGTH_TB2D = 4;

    /** longueur du tableau pour l'affichage */
    static final int LENGTH_PHYSIQUE_BAFF = 4+2 ;

    /** longueur du tableau de parametres doubles pour segmentation
     */
    static final int LENGTH_LIMIT_PHYSIQUE = 2;
    /** ses indices : */
    static final int LIMIT_SEUIL = 0;
    static final int LIMIT_COMPACITE = 1;

    // fonction de cout :
    static final double SEUIL_COUT_MEILLEUR = 0.00001d; // minimisation de plus de 5% du cout = efficace

	// segmentation : type de voisinage
	static final byte QUATRE_VOISINAGE = 4;
	static final byte HUIT_VOISINAGE = 8;

	static final byte TYPE_VOISINAGE = QUATRE_VOISINAGE;
	
	// discretiser un tableau de sigma
	static final boolean DISCRETISER_LES_DIFFERENTES_ZONES = false;


}