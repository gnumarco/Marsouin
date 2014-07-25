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
package com.marsouin.data;

import java.util.ArrayList;

public class ArrayDouble2DResultatFile {

    private final String fileFullName;

    public ArrayDouble2DResultatFile(String s) {
        fileFullName = s;
    }

    public double COEFF_MULTIPLICATIF = 1.0;

    public void sauver(double[][] t) {
        try {
            int i, j;
            WriteTextFile ecrit;
            ArrayList<String> meslignes = new ArrayList<>();
            String ligne;

            ligne = t.length + "" + t[0].length;
            meslignes.add(ligne);

            for (j = 0; j < t.length; j++) {
                ligne = "";
                for (i = 0; i < t[j].length; i++) {
                    ligne = ligne + "\t " + (long) (t[j][i] * COEFF_MULTIPLICATIF);
                }
                meslignes.add(ligne);
            }
            // ecriture
            ecrit = new WriteTextFile(fileFullName);
            for (i = 0; i < meslignes.size(); i++) {
                ecrit.uneLigne((String) meslignes.get(i));
            }

            ecrit.fermer();
        } catch (Exception e) {
            System.out.println(" erreur d ecriture de ResultFile ");
            e.printStackTrace();
        }
    }
}
