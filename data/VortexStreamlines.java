/*
 * VortexStreamlines.java
 *
 * Created on 17 aout 2004, 20:31
 */

package data;

/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */
public class VortexStreamlines{
    
    private PointFloat[] mesPoints = null;
    
    /** Creates a new instance of MetaVortex */
    public VortexStreamlines() {
    }
    
    public VortexStreamlines(PointFloat[] tab) {
        mesPoints = tab;
    }
    
    public VortexStreamlines(double[] xp, double[] yp){
        mesPoints = new PointFloat[xp.length];
        for(int i=0;i<xp.length;i++)
            mesPoints[i] = new PointFloat(xp[i],yp[i]);
    }
    
    public PointFloat[] getContour(){return mesPoints;}
    
    public void addPoint(PointFloat p){
        if(mesPoints == null){
            mesPoints = new PointFloat[1];
            mesPoints[0] = p;
        }else{
            java.util.ArrayList temp = new java.util.ArrayList();
            for (PointFloat mesPoint : mesPoints) {
                temp.add(mesPoint);
            }
            temp.add(p);
            mesPoints = new PointFloat[temp.size()];
            temp.toArray(mesPoints);
            temp = null;
        }
    }
    
    public double getPerimetre(){
        double somme = 0d;
        
        for(int i=0;i<mesPoints.length-1;i++){
            somme += Math.sqrt(Math.pow((mesPoints[i].x-mesPoints[i+1].x),2)+Math.pow((mesPoints[i].y-mesPoints[i+1].y),2));
        }
        somme += Math.sqrt(Math.pow((mesPoints[mesPoints.length-1].x-mesPoints[0].x),2)+Math.pow((mesPoints[mesPoints.length-1].y-mesPoints[0].y),2));
        return somme;
    }
    
    public boolean contains(PointFloat p) {
        int crossings = 0;
        double x = p.x, y = p.y;
        for (int i = 0; i < mesPoints.length-1; i++) {
            int j = i + 1;
            boolean cond1 = (mesPoints[i].y <= y) && (y < mesPoints[j].y);
            boolean cond2 = (mesPoints[j].y <= y) && (y < mesPoints[i].y);
            boolean cond3 = x < (mesPoints[j].x - mesPoints[i].x) * (y - mesPoints[i].y) / (mesPoints[j].y - mesPoints[i].y) + mesPoints[i].x;
            if ((cond1 || cond2)  && cond3) crossings++;
        }
        boolean cond1 = (mesPoints[mesPoints.length-1].y <= y) && (y < mesPoints[0].y);
        boolean cond2 = (mesPoints[0].y <= y) && (y < mesPoints[mesPoints.length-1].y);
        boolean cond3 = x < (mesPoints[0].x - mesPoints[mesPoints.length-1].x) * (y - mesPoints[mesPoints.length-1].y) / (mesPoints[0].y - mesPoints[mesPoints.length-1].y) + mesPoints[mesPoints.length-1].x;
        if ((cond1 || cond2)  && cond3) crossings++;
        if (crossings % 2 == 1) return true;
        else                    return false;
    }
    
    public PointFloat getCentre(){
        double sommeX, sommeY;
        
        sommeX = 0d;
        sommeY = 0d;
        for (PointFloat mesPoint : mesPoints) {
            sommeX += mesPoint.x;
            sommeY += mesPoint.y;
        }
        double resX = sommeX/mesPoints.length;
        double resY = sommeY/mesPoints.length;
        
        PointFloat p = new PointFloat(resX, resY);
        
        return p;
    }
    

}
