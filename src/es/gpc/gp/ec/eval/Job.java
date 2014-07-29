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
package es.gpc.gp.ec.eval;

import es.gpc.gp.ec.Individual;
import es.gpc.gp.ec.EvolutionState;
import java.io.*;
import java.util.*;

/**
 * Job.java
 *

 This class stores information regarding a job submitted to a Slave: the individuals,
 the subpopulations in which they are stored, a scratch array for the individuals used
 internally, and various coevolutionary information (whether we should only count victories
 single-elimination-tournament style; which individuals should have their fitnesses updated).
 
 <p>Jobs are of two types: traditional evaluations (Slave.V_EVALUATESIMPLE), and coevolutionary
 evaluations (Slave.V_EVALUATEGROUPED).  <i>type</i> indicates the type of job.
 For traditional evaluations, we may submit a group of individuals all at one time.  
 Only the individuals and their subpopulation numbers are needed. 
 Coevolutionary evaluations require the number of individuals, the subpopulations they come from, the
 pointers to the individuals, boolean flags indicating whether their fitness is to be updated or
 not, and another boolean flag indicating whether to count only victories in competitive tournament.

 * @author Liviu Panait
 * @version 1.0 
 */

public class Job
    {
    // either Slave.V_EVALUATESIMPLE or Slave.V_EVALUATEGROUPED
    int type;

    boolean sent = false;
    Individual[] inds;   // original individuals
    Individual[] newinds;  // individuals that were returned -- may be different individuals!
    int[] subPops; 
    boolean countVictoriesOnly;
    boolean[] updateFitness;
    
    void copyIndividualsForward()
        {
        if (newinds == null || newinds.length != inds.length)
            newinds = new Individual[inds.length];
        for(int i=0; i < inds.length; i++)
            {
            newinds[i] = (Individual)(inds[i].clone());
            // delete the trials since they'll get remerged
            newinds[i].fitness.trials = null;
            // delete the context, since it'll get remerged
            newinds[i].fitness.setContext(null);
            }
        }
        
    void copyIndividualsBack(EvolutionState state)
        {
        for(int i = 0; i < inds.length; i++)
            inds[i].merge(state, newinds[i]);
        newinds = null;
        }
    }
