/* 
 * Copyright (C) 2014 Marc Segond <dr.marc.segond@gmail.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package es.gpc.gp.ec.simple;
import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.util.Code;
import es.gpc.gp.ec.Fitness;
import es.gpc.gp.ec.EvolutionState;
import java.io.*;

/*
 * SimpleFitness.java
 *
 * Created: Tue Aug 10 20:10:42 1999
 * By: Sean Luke
 */

/**
 * A simple default fitness, consisting of a single floating-point value
 * where fitness A is superior to fitness B if and only if A > B.  
 * Fitness values may range from (-infinity,infinity) exclusive -- that is,
 * you may not have infinite fitnesses.  
 *
 * <p>Some kinds of selection methods require a more stringent definition of
 * fitness.  For example, FitProportionateSelection requires that fitnesses
 * be non-negative (since it must place them into a proportionate distribution).
 * You may wish to restrict yourself to values in [0,1] or [0,infinity) in
 * such cases.
 *
 <p><b>Default Base</b><br>
 simple.fitness

 * @author Sean Luke
 * @version 1.0
 */

public class SimpleFitness extends Fitness
    {
    protected float fitness;
    protected boolean isIdeal;

    public Parameter defaultBase()
        {
        return SimpleDefaults.base().push(P_FITNESS);
        }

    /**
       Deprecated -- now redefined to set the fitness but ALWAYS say that it's not ideal.
       If you need to specify that it's ideal, you should use the new function 
       setFitness(final EvolutionState state, float _f, boolean _isIdeal).
       @deprecated
    */
    public void setFitness(final EvolutionState state, float _f)
        {
        setFitness(state,_f,false);
        }
        
    public void setFitness(final EvolutionState state, float _f, boolean _isIdeal)
        {
        // we now allow f to be *any* value, positive or negative
        if (_f == Float.POSITIVE_INFINITY || _f == Float.NEGATIVE_INFINITY || Float.isNaN(_f))
            {
            state.output.warning("Bad fitness: " + _f + ", setting to 0.");
            fitness = 0;
            }
        else fitness = _f;
        isIdeal = _isIdeal;
        }

    public float fitness()
        {
        return fitness;
        }

    public void setup(final EvolutionState state, Parameter base) 
        {
        super.setup(state,base);  // unnecessary but what the heck
        }

    public boolean isIdealFitness()
        {
        return isIdeal;
        }

    public boolean equivalentTo(final Fitness _fitness)
        {
        return _fitness.fitness() == fitness();
        }

    public boolean betterThan(final Fitness _fitness)
        {
        return _fitness.fitness() < fitness();
        }

    public String fitnessToString()
        {
        return FITNESS_PREAMBLE + Code.encode(fitness());
        }
        
    public String fitnessToStringForHumans()
        {
        return FITNESS_PREAMBLE + fitness();
        }

    /** Presently does not decode the fact that the fitness is ideal or not */
    public void readFitness(final EvolutionState state, 
        final LineNumberReader reader)
        throws IOException
        {
        setFitness(state, Code.readFloatWithPreamble(FITNESS_PREAMBLE, state, reader));

/*
  int linenumber = reader.getLineNumber();
  String s = reader.readLine();
  if (s==null || s.length() < FITNESS_PREAMBLE.length()) // uh oh
  state.output.fatal("Reading Line " + linenumber + ": " +
  "Bad Fitness.");
  DecodeReturn d = new DecodeReturn(s, FITNESS_PREAMBLE.length());
  Code.decode(d);
  if (d.type!=DecodeReturn.T_FLOAT)
  state.output.fatal("Reading Line " + linenumber + ": " +
  "Bad Fitness.");
  setFitness(state,(float)d.d,false);
*/
        }

    public void writeFitness(final EvolutionState state,
        final DataOutput dataOutput) throws IOException
        {
        dataOutput.writeFloat(fitness);
        dataOutput.writeBoolean(isIdeal);
        writeTrials(state, dataOutput);
        }

    public void readFitness(final EvolutionState state,
        final DataInput dataInput) throws IOException
        {
        fitness = dataInput.readFloat();
        isIdeal = dataInput.readBoolean();
        readTrials(state, dataInput);
        }

    public void setToMeanOf(EvolutionState state, Fitness[] fitnesses)
        {
        // this is not numerically stable.  Perhaps we should have a numerically stable algorithm for sums
        // we're presuming it's not a very large number of elements, so it's probably not a big deal,
        // since this function is meant to be used mostly for gathering trials together.
        double f = 0;
        boolean ideal = true;
        for(int i = 0; i < fitnesses.length; i++)
            {
            SimpleFitness fit = (SimpleFitness)(fitnesses[i]);
            f += fit.fitness;
            ideal = ideal && fit.isIdeal;
            }
        f /= fitnesses.length;
        fitness = (float)f;
        isIdeal = ideal;
        }
    }
