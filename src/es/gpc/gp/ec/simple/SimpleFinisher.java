/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package es.gpc.gp.ec.simple;
import es.gpc.gp.ec.Finisher;
import es.gpc.gp.ec.EvolutionState;
import es.gpc.gp.ec.util.Parameter;

/* 
 * SimpleFinisher.java
 * 
 * Created: Tue Aug 10 21:09:18 1999
 * By: Sean Luke
 */

/**
 * SimpleFinisher is a default Finisher which doesn't do anything.  Most
 * application's don't need Finisher facilities, so this version will work
 * fine.
 *
 * @author Sean Luke
 * @version 1.0 
 */

public class SimpleFinisher extends Finisher
    {

    public void setup(final EvolutionState state, final Parameter base) { }


    /** Doesn't do anything. */
    public void finishPopulation(final EvolutionState state, final int result)
        {
        // don't care
        return;
        }
    }
