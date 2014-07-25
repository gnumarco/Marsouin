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

public class PointFloat {
    
    public double x,y;
    
    /** Creates a new instance of PointDouble */
    public PointFloat() {
	x=0d;
	y=0d;
    }
    
    public PointFloat(double i, double j) {
	x=i;
	y=j;
    }
    
    public PointFloat(float i, float j) {
	x=(double)i;
	y=(double)j;
    }
    
    public PointFloat(int i, int j) {
	x=(double)i;
	y=(double)j;
    }
    
}
