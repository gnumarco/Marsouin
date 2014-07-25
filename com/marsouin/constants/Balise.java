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

public interface Balise {
    // pour tous les fichiers
    static final char CARACTERE_DE_COMMENTAIRE = '#';

    // debut de nom de fichiers de Type_2
    static final String PREFIXE_TYPE_2_VX = "U_";
    static final String PREFIXE_TYPE_2_VY = "V_";

    // noms de fichiers
    static final String CONFIG_TYPE = ".dat";
    static final String CONFIG_FILE = "config"+CONFIG_TYPE;
    static final String CONFIG_DIR = ".marsouin" ;


    // balises dans lengthfichier confighistofile
    static final String NEW_CONFIG = "NOUVELLE_CONFIGURATION";
    static final String CHECKBOXC = "CHECKBOXC";
    static final String LIMITC = "LIMITC";
    static final String FLAGAFFICH = "FLAGAFFICH";
    static final String COMBI_GC = "COMBINER_GC";
    static final String END_CONFIG = "FIN_DE_CONFIGURATION";

    static final String NEW_IMPORT_CONFIG = "DEBUT_D_IMPORTATION";
    static final String END_IMPORT_CONFIG = "FIN_D_IMPORTATION";

    static final String NOM_CONFIG = "NOM_DE_LA_CONFIGURATION";
    static final String COMMENTAIRE_CONFIG = "COMMENTAIRE_DE_LA_CONFIGURATION";
    static final String END_COMMENTAIRE_CONFIG = "FIN_DU_COMMENTAIRE_DE_LA_CONFIGURATION";


    /** coef multiplicatif pour les courants dans les fichiers de chaque type 
     *  ( 10e+2 changement d'unite) 
     *  on cherche a obtenir tout en m/s 
     */
    /** unite du type 2 : m/s */
    static final double COEF_TYPE_2 = 1.0;
    
    /** unite du type 1 : cm/s */
    static final double COEF_TYPE_1 = 0.01;
    

}
