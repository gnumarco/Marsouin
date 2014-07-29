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
package es.gpc.gp.ec.app.control;

import es.gpc.gp.ec.Subpopulation;
import es.gpc.gp.ec.EvolutionState;
import es.gpc.gp.ec.util.Checkpoint;

/* 
 * SimpleEvolutionState.java
 * 
 * Created: Tue Aug 10 22:14:46 1999
 * By: Sean Luke
 */
/**
 * A SimpleEvolutionState is an EvolutionState which implements a simple form of
 * generational evolution.
 *
 * <p>
 * First, all the individuals in the population are created.
 * <b>(A)</b>Then all individuals in the population are evaluated. Then the
 * population is replaced in its entirety with a new population of individuals
 * bred from the old population. Goto <b>(A)</b>.
 *
 * <p>
 * Evolution stops when an ideal individual is found (if quitOnRunComplete is
 * set to true), or when the number of generations (loops of <b>(A)</b>) exceeds
 * the parameter value numGenerations. Each generation the system will perform
 * garbage collection and checkpointing, if the appropriate parameters were set.
 *
 * <p>
 * This approach can be readily used for most applications of Genetic Algorithms
 * and Genetic Programming.
 *
 * @author Sean Luke
 * @version 1.0
 */
public class ControlEvolutionState extends EvolutionState {

    
    private static final long serialVersionUID = 1;
    
    /**
     *
     */
    @Override
    public void startFresh() {
        output.message("Setting up");
        setup(this, null);  // a garbage Parameter

        // POPULATION INITIALIZATION
        output.message("Initializing Generation 0");
        statistics.preInitializationStatistics(this);
        population = initializer.initialPopulation(this, 0); // unthreaded
        statistics.postInitializationStatistics(this);

        // Compute generations from evaluations if necessary
        if (numEvaluations > UNDEFINED) {
            // compute a generation's number of individuals
            int generationSize = 0;
            for (Subpopulation subpop : population.subpops) {
                generationSize += subpop.individuals.length; // so our sum total 'generationSize' will be the initial total number of individuals
            }

            if (numEvaluations < generationSize) {
                numEvaluations = generationSize;
                numGenerations = 1;
                output.warning("Using evaluations, but evaluations is less than the initial total population size (" + generationSize + ").  Setting to the populatiion size.");
            } else {
                if (numEvaluations % generationSize != 0) {
                    output.warning("Using evaluations, but initial total population size does not divide evenly into it.  Modifying evaluations to a smaller value ("
                            + ((numEvaluations / generationSize) * generationSize) + ") which divides evenly.");  // note integer division
                }
                numGenerations = (int) (numEvaluations / generationSize);  // note integer division
                numEvaluations = numGenerations * generationSize;
            }
            output.message("Generations will be " + numGenerations);
        }

        // INITIALIZE CONTACTS -- done after initialization to allow
        // a hook for the user to do things in Initializer before
        // an attempt is made to connect to island models etc.
        exchanger.initializeContacts(this);
        evaluator.initializeContacts(this);
    }

    @Override
    public int evolve() {
        glog.generation = generation;
        if (generation > 0) {
            glog.state = "Evaluating generation " + generation + "...";
            output.message("Generation " + generation);
        }

        // EVALUATION
        statistics.preEvaluationStatistics(this);
        evaluator.evaluatePopulation(this);
        statistics.postEvaluationStatistics(this);

        // SHOULD WE QUIT?
        if (evaluator.runComplete(this) && quitOnRunComplete) {
            output.message("Found Ideal Individual");
            return R_SUCCESS;
        }

        // SHOULD WE QUIT?
        if (generation == numGenerations - 1) {
            return R_FAILURE;
        }

        glog.state = "Breeding generation " + generation + "...";
        // PRE-BREEDING EXCHANGING
        statistics.prePreBreedingExchangeStatistics(this);
        population = exchanger.preBreedingExchangePopulation(this);
        statistics.postPreBreedingExchangeStatistics(this);

        String exchangerWantsToShutdown = exchanger.runComplete(this);
        if (exchangerWantsToShutdown != null) {
            output.message(exchangerWantsToShutdown);
            /*
             * Don't really know what to return here.  The only place I could
             * find where runComplete ever returns non-null is 
             * IslandExchange.  However, that can return non-null whether or
             * not the ideal individual was found (for example, if there was
             * a communication error with the server).
             * 
             * Since the original version of this code didn't care, and the
             * result was initialized to R_SUCCESS before the while loop, I'm 
             * just going to return R_SUCCESS here. 
             */

            return R_SUCCESS;
        }

        // BREEDING
        statistics.preBreedingStatistics(this);

        population = breeder.breedPopulation(this);

        // POST-BREEDING EXCHANGING
        statistics.postBreedingStatistics(this);

        // POST-BREEDING EXCHANGING
        statistics.prePostBreedingExchangeStatistics(this);
        population = exchanger.postBreedingExchangePopulation(this);
        statistics.postPostBreedingExchangeStatistics(this);

        // INCREMENT GENERATION AND CHECKPOINT
        generation++;
        if (checkpoint && generation % checkpointModulo == 0) {
            output.message("Checkpointing");
            statistics.preCheckpointStatistics(this);
            Checkpoint.setCheckpoint(this);
            statistics.postCheckpointStatistics(this);
        }

        return R_NOTDONE;
    }

    /**
     * @param result
     */
    @Override
    public void finish(int result) {
        //Output.message("Finishing");
        /* finish up -- we completed. */
        statistics.finalStatistics(this, result);
        finisher.finishPopulation(this, result);
        exchanger.closeContacts(this, result);
        evaluator.closeContacts(this, result);
    }

    /**
     *
     */
    @Override
    public void startFromCheckpoint() {

    }

    /**
     * Starts the run. <i>condition</i> indicates whether or not the run was
     * restarted from a checkpoint (C_STARTED_FRESH vs
     * C_STARTED_FROM_CHECKPOINT). At the point that run(...) has been called,
     * the parameter database has already been set up, as have the random number
     * generators, the number of threads, and the Output facility. This method
     * should call this.setup(...) to set up the EvolutionState object if
     * condition equals C_STARTED_FRESH.
     *
     * @param condition
     */
    @Override

    public void run(int condition) {
        if (condition == C_STARTED_FRESH) {
            startFresh();
        } else // condition == C_STARTED_FROM_CHECKPOINT
        {
            startFromCheckpoint();
        }

        /* the big loop */
        int result = R_NOTDONE;
        while (result == R_NOTDONE) {
            result = evolve();
        }

        finish(result);
    }

}
