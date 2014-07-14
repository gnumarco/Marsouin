/*
 * CollVortexStreamlines.java
 *
 * Created on 17 aout 2004, 20:25
 */

package data;

import java.util.ArrayList;
/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */
public class CollVortexStreamlines {
    
    private VortexStreamlines lot[] = null;
    
    /** inutile Creates a new instance of candidates */
    public CollVortexStreamlines(int n) {
	if(n>0) {
	    lot = new VortexStreamlines[n];
	    for(int i=0;i<n;i++){
		lot[n] = new VortexStreamlines();
	    }
	}
    }
    public CollVortexStreamlines() {
	
    }
    
    public void ajouter(VortexStreamlines b) {
	if (lot==null) {
	    lot = new VortexStreamlines[1];
	    lot[0] = b;
	}
	else {
	    int s = lot.length;
	    VortexStreamlines[] tmp = lot;
	    lot = null;
	    lot = new VortexStreamlines[s+1];
	    for(s=0;s<tmp.length;s++)
		lot[s] = tmp[s];
	    lot[tmp.length] = b;
	    tmp =null;
	}
    }

    
    public VortexStreamlines getVortexStreamlines(int num) {
	VortexStreamlines ret =null;
	if ((lot!=null)&&((num>-1)&(num<lot.length)))
	    ret = lot[num];
	return ret;
    }
    
    
    public VortexStreamlines[] getVortexStreamlines() {
	VortexStreamlines[] ret =lot;
	return ret;
    }
    
    public void effacerVortexStreamlines(int num) {
	if ((lot!=null)&&((num>-1)&(num<lot.length))) {
	    int s = lot.length;
	    ArrayList tmp = new ArrayList(s);//new ArrayList((java.util.Collection)lot);
	    for(s=0;s<lot.length;s++)
		tmp.add(lot[s]);
	    
	    tmp.remove(num);
	    lot = null;
	    if (tmp.size()>0) {
		lot = new VortexStreamlines[tmp.size()];
		tmp.toArray(lot);
	    }
	}
    }
    public void effacerVortexStreamlines(int from, int to) {
	if ((lot!=null)&&((from>-1)&(to<lot.length))) {
	    int s = lot.length ;
	    ArrayList tmp = new ArrayList(s);//new ArrayList((java.util.Collection)lot);
	    for(s=0;s<lot.length;s++)
		tmp.add(lot[s]);
	    
	    for(s=from;s<=to; s++)
		tmp.remove(from);
	    lot = null;
	    if (tmp.size()>0) {
		lot = new VortexStreamlines[tmp.size()];
		tmp.toArray(lot);
	    }
	}
    }
    
    public void effacerTout() {
	if (lot!=null)
	    for(int s=0;s<lot.length;s++)
		lot[s]=null;
	lot = null;
    }
    
    /**    rend la memoire ! */
    public void dispose() {
	if (lot!=null)
	    for(int s=0;s<lot.length;s++) {
		lot[s]=null;
	    }
	lot = null;
    }
   
    
    public int size() {
	int ret=0;
	if (lot!=null)
	    ret = lot.length;
	return ret;
    }
    
}

