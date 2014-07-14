/*
 * CollVortexPhys.java
 *
 * Created on 09 april 2003, 11:26
 */

/*
 * @author Mahler
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 *
 */

package data;

import java.util.ArrayList;
import java.awt.Point;

/** cet objet est un conteneur de VortexPhys */
 public class CollVortexPhys {

    private VortexPhys lot[] = null;

    /** inutile Creates a new instance of candidates */
    public CollVortexPhys(int n) {
        if(n>0) {
            lot = new VortexPhys[n];
            for(int i=0;i<n;i++){
                lot[n] = new VortexPhys();
            }
        }
    }
    public CollVortexPhys() {
		lot=null;
    }

    public void ajouter(VortexPhys b) {
        if (lot==null) {
            lot = new VortexPhys[1];
            lot[0] = b;
        }
        else {
            int s = lot.length;
            VortexPhys[] tmp = lot;
            lot = null;
            lot = new VortexPhys[s+1];
            for(s=0;s<tmp.length;s++)
                lot[s] = tmp[s];
            lot[tmp.length] = b;
            tmp =null;
        }
    }

    public void ajouter(int[] xpts, int[] ypts, int npts,int tag) {
        VortexPhys b = new VortexPhys(xpts,ypts,npts,tag);
        if (lot==null) {
            lot = new VortexPhys[1];
            lot[0] = b;
        }
        else {
            int s = lot.length;
            VortexPhys[] tmp = lot;
            lot = null;
            lot = new VortexPhys[s+1];
            for(s=0;s<tmp.length;s++)
                lot[s] = tmp[s];
            lot[tmp.length] = b;
            tmp =null;
        }
    }
    
    public void ajouter(ArrayList v,int tag) {
        int[] x = null;
        int[] y = null;
        int n = 0;
        if  ((v!=null)&&(v.size()!=0)) {
            //extraireX(ArrayList v)
            x= new int[v.size()];
            for(int i=0; i<v.size(); i++)
                x[i]=((Point)v.get(i)).x;

            // extraireY(ArrayList v)
            y= new int[v.size()];
            for(int i=0; i<v.size(); i++)
                y[i]=((Point)v.get(i)).y;
            n=v.size();
        }
        this.ajouter(x,y,n,tag);
    }
    
    /** ajouter un vortex en donnant son centre
     *renvoie le numero du new vortex */
    public int ajouterCentre(Point p,int tag) {
        VortexPhys b = new VortexPhys(p,tag);
        int ret;
        if (lot==null) {
            lot = new VortexPhys[1];
            lot[0] = b;
            ret = 0;
        }
        else {
            int s = lot.length;
            VortexPhys[] tmp = lot;
            lot = null;
            lot = new VortexPhys[s+1];
            for(s=0;s<tmp.length;s++)
                lot[s] = tmp[s];
            lot[tmp.length] = b;
            ret = tmp.length;
            tmp =null;
        }
        return ret;
    }

    public VortexPhys getVortexPhys(int num) {
        VortexPhys ret =null;
        if ((lot!=null)&&((num>-1)&(num<lot.length)))
            ret = lot[num];
        return ret;
    }


    public VortexPhys[] getVortexPhys() {
        VortexPhys[] ret =lot;
        return ret;
    }

    public void effacerVortexPhys(int num) {
        if ((lot!=null)&&((num>-1)&(num<lot.length))) {
            int s = lot.length;
            ArrayList tmp = new ArrayList(s);//new ArrayList((java.util.Collection)lot);
            for(s=0;s<lot.length;s++)
                tmp.add(lot[s]);

            tmp.remove(num);
            lot = null;
            if (tmp.size()>0) {
                lot = new VortexPhys[tmp.size()];
                tmp.toArray(lot);
            }
        }
    }
    public void effacerVortexPhys(int from, int to) {
        if ((lot!=null)&&((from>-1)&(to<lot.length))) {
            int s = lot.length ;
            ArrayList tmp = new ArrayList(s);//new ArrayList((java.util.Collection)lot);
            for(s=0;s<lot.length;s++)
                tmp.add(lot[s]);

            for(s=from;s<=to; s++)
                tmp.remove(from);
            lot = null;
            if (tmp.size()>0) {
                lot = new VortexPhys[tmp.size()];
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

    public void dispose() {
        if (lot!=null)
            for(int s=0;s<lot.length;s++) {
                lot[s].dispose();
                lot[s]=null;
            }
        lot = null;
    }
    
    protected void finalize() {
        this.dispose();
    }
    
    public int size() {
        int ret=0;
        if (lot!=null)
            ret = lot.length;
        return ret;
    }

}
