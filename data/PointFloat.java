/*
 * PointDouble.java
 *
 * Created on 9 aout 2004, 20:52
 */

package data;


/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */
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
