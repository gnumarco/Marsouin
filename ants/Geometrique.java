/**
 *
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */

package ants;

public class Geometrique {

	private double p;

	public Geometrique() {
		p=0.5;
	}

	public Geometrique(double proba) {
		p=proba;
	}
	
	public int tirage() {
		int n=0;
		double U;
	  do{
		U=Math.random();
		n++;
	  }
	  while (U>p);
	  return(n);
	}
        
        public double calcul(double p, double x){
            double res =0.0;
            
            res= java.lang.Math.pow((1-p), (x-1))*p;
            
            return res;
        }
}