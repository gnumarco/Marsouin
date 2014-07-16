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
package com.marsouin.ants;

public class Geometric {

    private double p;

    public Geometric() {
        p = 0.5;
    }

    public Geometric(double proba) {
        p = proba;
    }

    public int tirage() {
        int n = 0;
        double U;
        do {
            U = Math.random();
            n++;
        } while (U > p);
        return (n);
    }

    public double calcul(double p, double x) {
        double res;

        res = java.lang.Math.pow((1 - p), (x - 1)) * p;

        return res;
    }
}
