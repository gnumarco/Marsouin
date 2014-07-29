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

public interface Ants {
    //**********************************************************
    // classe Fourmi

    // pour toutes les boucles
    public static final int DEPLACEMENT_MIN = 4;

    public static final int TRACE_MIN = 3;

    // on multiplie Cette Angle par lengthTaux de courbure d'une Ants
    // pour obtenir l'amplitude finale ( acceleration centripete) de cette Ants
    public final static double AMPLITUDE_DE_COURBURE = java.lang.Math.PI/7.0;// 7d;

    // nombre de cibles candidates pour acceuillir la Ants
    public final static int NB_CHOIX = 8;

    // utile lors de la selection basee sur la difference entre l'angle intention et l' angle d'un candidat
    public final static double AMPLITUDE_DE_VISIBILITE = java.lang.Math.PI/2.0;

    // seuil de fitnesstotal minimum
    public static final double SEUIL_FITNESS = 0.000001;

    // coeff multiplicatif applique a la qualite ( fitness) si lengthcandidat contient de la pheromone
    public static final double COEFF_FITNESS_SUIVRE_PHEROMONE = 2.0;


    // nombre d'utilisation de toute suite aleatoire
    public static final long RESET_RANDOM = 1000;

    // nombre de tirage  max pour se replacer
    public static final int CPT_RANDOM_REPLACE = 10;

    // coeff multiplicatif applique a la qte de Pheromone Deposee Par Une Fourmi
    // pour donner le SEUIL DE PHEROMONE qui revele une boucle;
    public static final double COEFF_PHEROMONE_SUR_BOUCLE = 0.00001;

    //**********************************************************
    // classe Courant
    public static final double PHEROMONE_ABSENTE = 0.0001;

    //**********************************************************

    //**********************************************************
    // classe fourmiAnim
    // pour toutes les boucles
    public static final long ANIM_DELAY = 100;

    //**********************************************************
    // classe MEMOIRE et cie

    //@@@@@@@ TABLEAUX de coefficients @@@@@@@@

    // constantes d'objets affich�s
    // INDICES
    //public static final int FOURMI_IMPOSE_COURBURE=0; // pour le tableau de FOURMIS_BAFF bool[] indice

    public static final int LENGTH_FOURMI_BAFF = 0;// longueur du tableau

    // constantes d'effectif
    // INDICES
    public static final int FOURMI_NB_ESPECES=0; // pour le tableau de Fourmis_EFF int[] indice
    public static final int FOURMI_NB_GENERATIONS=1;
    public static final int FOURMI_NB_ANIMEES=2;
    public static final int FOURMI_NB_INTRA_ESPECE=3;
    public static final int FOURMI_NB_RUNS=4;

    public static final int LENGTH_FOURMI_NB = 5;// longueur du tableau

    // constantes de pheromone, et taux
    // INDICES
    public static final int FOURMI_COEFF_EVAPORATION=0; // pour le tableau de Fourmis_Coef double[] indice
    public static final int FOURMI_QTE_DEPOT=1;
    public static final int FOURMI_TAUX_COURBURE=2;

    public static final int LENGTH_FOURMI_COEFF = 3;// longueur du tableau


}
