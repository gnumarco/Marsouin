/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package es.gpc.gp.ec.app.control;
import es.gpc.gp.ec.gp.GPData;

/* 
 * ControlData.java
 * 
 * Created: Wed Nov  3 18:32:13 1999
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @version 1.0 
 */

public class ControlData extends GPData
    {
    // return value
    public double x;
    public double[] b;

    public void copyTo(final GPData gpd) 
        { ((ControlData)gpd).x = x;
        ((ControlData)gpd).b = b;}
    }
