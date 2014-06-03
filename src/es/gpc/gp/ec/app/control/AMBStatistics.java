/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.gpc.gp.ec.app.control;

import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.simple.SimpleProblemForm;
import es.gpc.gp.ec.gp.GPNode;
import es.gpc.gp.ec.gp.GPIndividual;
import es.gpc.gp.ec.gp.GPSpecies;
import es.gpc.gp.ec.Individual;
import es.gpc.gp.ec.EvolutionState;
import java.io.*;
import es.gpc.gp.ec.gp.koza.KozaFitness;

/**
 *
 * @author marc
 */
public class AMBStatistics extends es.gpc.gp.ec.simple.SimpleStatistics {

    public Individual[] getBestSoFar() {
        return best_of_run;
    }
    /** log file parameter */
    public static final String P_STATISTICS_FILE = "file";
    /** The Statistics' log */
    public int statisticslog;
    /** The best individual we've found so far */
    public Individual[] best_of_run;
    /** compress? */
    public static final String P_COMPRESS = "gzip";
    public static final String P_FULL = "gather-full";
    public static final String LOG_INDIV = "log_indiv.dat";
    boolean doFull;
    // total number of individuals
    long numInds;
    // timings
    long lastTime;
    long initializationTime;
    long breedingTime;
    long evaluationTime;
    long nodesInitialized;
    long nodesEvaluated;
    long nodesBred;
    long breedGen;
    // memory usage info
    long lastUsage = 0;
    long initializationUsage = 0;
    long breedingUsage = 0;
    long evaluationUsage = 0;

    public AMBStatistics() {
        best_of_run = null;
        statisticslog = 0; /* stdout */ }

    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);

        File statisticsFile = state.parameters.getFile(
                base.push(P_STATISTICS_FILE), null);

        if (statisticsFile != null) {
            try {
                statisticslog = state.output.addLog(statisticsFile,
                        !state.parameters.getBoolean(base.push(P_COMPRESS), null, false),
                        state.parameters.getBoolean(base.push(P_COMPRESS), null, false));
            } catch (IOException i) {
                state.output.fatal("An IOException occurred while trying to create the log " + statisticsFile + ":\n" + i);
            }
        }

        doFull = true;
        //doFull = state.parameters.getBoolean(base.push(P_FULL),null,false);
        nodesInitialized = nodesEvaluated = nodesBred = 0;
        breedingTime = evaluationTime = 0;
    }

    public void preInitializationStatistics(final EvolutionState state) {
        super.preInitializationStatistics(state);
        if (doFull) {
            Runtime r = Runtime.getRuntime();
            lastTime = System.currentTimeMillis();
            lastUsage = r.totalMemory() - r.freeMemory();
        }
    }

    public void postInitializationStatistics(final EvolutionState state) {
        super.postInitializationStatistics(state);

        // set up our best_of_run array -- can't do this in setup, because
        // we don't know if the number of subpopulations has been determined yet
        best_of_run = new Individual[state.population.subpops.length];

        // gather timings       
        if (doFull) {
            Runtime r = Runtime.getRuntime();
            long curU = r.totalMemory() - r.freeMemory();
            if (curU > lastUsage) {
                initializationUsage = curU - lastUsage;
            }
            initializationTime = System.currentTimeMillis() - lastTime;

            // Determine how many nodes we have
            for (int x = 0; x < state.population.subpops.length; x++) {
                // check to make sure they're the right class
                if (!(state.population.subpops[x].species instanceof GPSpecies)) {
                    state.output.fatal("Subpopulation " + x
                            + " is not of the species form GPSpecies."
                            + "  Cannot do timing statistics with KozaStatistics.");
                }

                for (int y = 0; y < state.population.subpops[x].individuals.length; y++) {
                    GPIndividual i =
                            (GPIndividual) (state.population.subpops[x].individuals[y]);
                    for (int z = 0; z < i.trees.length; z++) {
                        nodesInitialized += i.trees[z].child.numNodes(GPNode.NODESEARCH_ALL);
                    }
                }
            }
        }
    }

    public void preBreedingStatistics(final EvolutionState state) {
        super.preBreedingStatistics(state);
        if (doFull) {
            Runtime r = Runtime.getRuntime();
            lastTime = System.currentTimeMillis();
            lastUsage = r.totalMemory() - r.freeMemory();
        }
    }

    public void postBreedingStatistics(final EvolutionState state) {
        //super.postBreedingStatistics(state);
        // gather timings
        if (doFull) {
            Runtime r = Runtime.getRuntime();
            long curU = r.totalMemory() - r.freeMemory();
            if (curU > lastUsage) {
                breedingUsage += curU - lastUsage;
            }
            breedingTime += System.currentTimeMillis() - lastTime;
            breedGen = System.currentTimeMillis() - lastTime;

            // Determine how many nodes we have
            for (int x = 0; x < state.population.subpops.length; x++) {
                // check to make sure they're the right class
                if (!(state.population.subpops[x].species instanceof GPSpecies)) {
                    state.output.fatal("Subpopulation " + x
                            + " is not of the species form GPSpecies."
                            + "  Cannot do timing statistics with KozaStatistics.");
                }

                for (int y = 0; y < state.population.subpops[x].individuals.length; y++) {
                    GPIndividual i =
                            (GPIndividual) (state.population.subpops[x].individuals[y]);
                    for (int z = 0; z < i.trees.length; z++) {
                        nodesBred += i.trees[z].child.numNodes(GPNode.NODESEARCH_ALL);
                    }
                }
            }
        }
    }

    public void preEvaluationStatistics(final EvolutionState state) {
        super.preEvaluationStatistics(state);
        if (doFull) {
            Runtime r = Runtime.getRuntime();
            lastTime = System.currentTimeMillis();
            lastUsage = r.totalMemory() - r.freeMemory();
        }
    }

    public void postEvaluationStatistics(final EvolutionState state) {
        super.postEvaluationStatistics(state);

        // Gather statistics

        File file = new File(LOG_INDIV);
        try {


            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            // for now we just print the best fitness per subpopulation.
            Individual[] best_i = new Individual[state.population.subpops.length];  // quiets compiler complaints
            bw.write("\n");
            bw.flush();
            for (int x = 0; x < state.population.subpops.length; x++) {
                best_i[x] = state.population.subpops[x].individuals[0];
                bw.write(((GPIndividual) state.population.subpops[x].individuals[0]).trees[0].child.makeCTree(true, true, true) + "\n");
                bw.write(state.population.subpops[x].individuals[0].fitness.fitness() + "\n");
                for (int y = 1; y < state.population.subpops[x].individuals.length; y++) {
                    bw.write(((GPIndividual) state.population.subpops[x].individuals[y]).trees[0].child.makeLispTree() + "\n");
                    bw.write("" + state.population.subpops[x].individuals[y].fitness.fitness() + "\n");
                    if (state.population.subpops[x].individuals[y].fitness.betterThan(best_i[x].fitness)) {
                        best_i[x] = state.population.subpops[x].individuals[y];
                    }
                }
                // now test to see if it's the new best_of_run
                if (best_of_run[x] == null || best_i[x].fitness.betterThan(best_of_run[x].fitness)) {
                    best_of_run[x] = (Individual) (best_i[x].clone());
                }

            }
            bw.flush();
            fw.close();
        } catch (Exception e) {
            state.output.message(e.toString());
        }

        Runtime r = Runtime.getRuntime();
        long curU = r.totalMemory() - r.freeMemory();
        if (curU > lastUsage) {
            evaluationUsage += curU - lastUsage;
        }
        if (doFull) {
            evaluationTime += System.currentTimeMillis() - lastTime;
        }
        long GenEvalTime = System.currentTimeMillis() - lastTime;

        //state.output.println("\n\n\nGeneration " + state.generation + "\n================",statisticslog);

        Individual[] best_i = new Individual[state.population.subpops.length];
        for (int x = 0; x < state.population.subpops.length; x++) {
            //state.output.println("\nSubpopulation " + x + "\n----------------",statisticslog);

            // gather timings
            if (doFull) {
                long totNodesPerGen = 0;
                long totDepthPerGen = 0;
                String message = "";
                // check to make sure they're the right class
                if (!(state.population.subpops[x].species instanceof GPSpecies)) {
                    state.output.fatal("Subpopulation " + x
                            + " is not of the species form GPSpecies."
                            + "  Cannot do timing statistics with KozaStatistics.");
                }

                long[] numNodes = new long[((GPIndividual) (state.population.subpops[x].species.i_prototype)).trees.length];
                long[] numDepth = new long[((GPIndividual) (state.population.subpops[x].species.i_prototype)).trees.length];

                for (int y = 0; y < state.population.subpops[x].individuals.length; y++) {
                    GPIndividual i =
                            (GPIndividual) (state.population.subpops[x].individuals[y]);
                    for (int z = 0; z < i.trees.length; z++) {
                        nodesEvaluated += i.trees[z].child.numNodes(GPNode.NODESEARCH_ALL);
                        numNodes[z] += i.trees[z].child.numNodes(GPNode.NODESEARCH_ALL);
                        numDepth[z] += i.trees[z].child.depth();
                    }
                }

                for (int tr = 0; tr < numNodes.length; tr++) {
                    totNodesPerGen += numNodes[tr];
                }
                message += "Eval_time: " + GenEvalTime + "," + "Breeding_time: " + breedGen + "," + "Tot_Nodes: " + totNodesPerGen + "," + "Avg_Nodes: " + ((double) totNodesPerGen) / state.population.subpops[x].individuals.length + "," + "Nodes/tree:_[";
                for (int tr = 0; tr < numNodes.length; tr++) {
                    if (tr > 0) {
                        state.output.message("|");
                    }
                    message += "" + ((double) numNodes[tr]) / state.population.subpops[x].individuals.length;
                }
                message += "],";


                for (int tr = 0; tr < numDepth.length; tr++) {
                    totDepthPerGen += numDepth[tr];
                }
                message += "Avg_Depth: " + ((double) totDepthPerGen)
                        / (state.population.subpops[x].individuals.length * numDepth.length) + ",";
                state.output.print("Depth/tree:[", statisticslog);
                state.output.flush();
                for (int tr = 0; tr < numDepth.length; tr++) {
                    if (tr > 0) {
                        state.output.print("|", statisticslog);
                    }
                    state.output.flush();
                    state.output.print("" + ((double) numDepth[tr]) / state.population.subpops[x].individuals.length, statisticslog);
                    state.output.flush();
                }
                state.output.print("],", statisticslog);
                state.output.flush();
                state.output.message(message);
            }


            float meanStandardized = 0.0f;
            float meanAdjusted = 0.0f;
            long hits = 0;

            if (!(state.population.subpops[x].species.f_prototype instanceof KozaFitness)) {
                state.output.fatal("Subpopulation " + x
                        + " is not of the fitness KozaFitness.  Cannot do timing statistics with KozaStatistics.");
            }

            int cptInd = 0;
            java.util.List<GPIndividual> bestTen = new java.util.ArrayList<>();
            double minFit = Double.MIN_VALUE;
            best_i[x] = state.population.subpops[x].individuals[0];
            for (int y = 0; y < state.population.subpops[x].individuals.length; y++) {
                // best individual
                if (state.population.subpops[x].individuals[y].fitness.betterThan(best_i[x].fitness)) {
                    best_i[x] = state.population.subpops[x].individuals[y];
                }
                //Best 10
                if (cptInd < 10) {
                    if (((es.gpc.gp.ec.gp.koza.KozaFitness) state.population.subpops[x].individuals[y].fitness).adjustedFitness() < minFit) {
                        minFit = ((es.gpc.gp.ec.gp.koza.KozaFitness) state.population.subpops[x].individuals[y].fitness).adjustedFitness();
                    }
                    bestTen.add((GPIndividual) state.population.subpops[x].individuals[y]);
                    cptInd++;
                } else {
                    java.util.Collections.sort(bestTen);
                    if (((es.gpc.gp.ec.gp.koza.KozaFitness) state.population.subpops[x].individuals[y].fitness).adjustedFitness() > minFit) {
                        bestTen.remove(0);
                        bestTen.add(0, (GPIndividual) state.population.subpops[x].individuals[y]);
                        minFit = ((es.gpc.gp.ec.gp.koza.KozaFitness) state.population.subpops[x].individuals[y].fitness).adjustedFitness();
                    }
                }

                // mean for population
                meanStandardized += ((KozaFitness) (state.population.subpops[x].individuals[y].fitness)).standardizedFitness();
                meanAdjusted += ((KozaFitness) (state.population.subpops[x].individuals[y].fitness)).adjustedFitness();
                hits += ((KozaFitness) (state.population.subpops[x].individuals[y].fitness)).hits;
            }
            state.output.message("10 bests:");
            for (int y = 0; y < bestTen.size(); y++) {

                state.output.message(bestTen.get(y).fitness.fitness() + " --> " + bestTen.get(y).trees[0].child.makeCTree(true, true, true));
            }


            // compute fitness stats
            meanStandardized /= state.population.subpops[x].individuals.length;
            meanAdjusted /= state.population.subpops[x].individuals.length;
            state.output.print("Mean_fitness_raw: " + meanStandardized + " adjusted: " + meanAdjusted + " hits: " + ((double) hits) / state.population.subpops[x].individuals.length, statisticslog);
            state.output.flush();

            state.output.println("", statisticslog);
            state.output.flush();

            // compute inds stats
            numInds += state.population.subpops[x].individuals.length;
        }

        // now test to see if it's the new best_of_run
        for (int x = 0; x < state.population.subpops.length; x++) {
            if (best_of_run[x] == null || best_i[x].fitness.betterThan(best_of_run[x].fitness)) {
                best_of_run[x] = (Individual) (best_i[x].clone());
            }

            // print the best-of-generation individual
            //state.output.println("\nBest Individual of Generation:",statisticslog);
            //best_i[x].printIndividualForHumans(state,statisticslog);
            //state.output.message("Subpop " + x + " best fitness of generation: " + best_i[x].fitness.fitnessToStringForHumans());
        }
    }

    /** Logs the best individual of the run. */
    public void finalStatistics(final EvolutionState state, final int result) {
        super.finalStatistics(state, result);

        state.output.println("\n\n\nFinal Statistics\n================", statisticslog);

        state.output.println("Total Individuals Evaluated: " + numInds, statisticslog);
        // for now we just print the best fitness 

        state.output.println("\nBest Individual of Run:", statisticslog);
        for (int x = 0; x < state.population.subpops.length; x++) {
            best_of_run[x].printIndividualForHumans(state, statisticslog);
            //state.output.message("Subpop " + x + " best fitness of run: " + best_of_run[x].fitness.fitnessToStringForHumans());
            try {
                java.io.PrintWriter w = new java.io.PrintWriter("test.ind");
                best_of_run[x].printIndividual(state, w);
                w.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // finally describe the winner if there is a description
            ((SimpleProblemForm) (state.evaluator.p_problem.clone())).describe(state, best_of_run[x], x, 0, statisticslog);
        }

        // Output timings
        if (doFull) {
            state.output.message("\n\n\nTimings\n=======");

            state.output.message("Initialization: " + ((float) initializationTime) / 1000 + " secs total, " + nodesInitialized + " nodes, " + nodesInitialized / (((float) initializationTime) / 1000) + " nodes/sec");
            state.output.message("Evaluating: " + ((float) evaluationTime) / 1000 + " secs total, " + nodesEvaluated + " nodes, " + nodesEvaluated / (((float) evaluationTime) / 1000) + " nodes/sec");
            state.output.message("Breeding: " + ((float) breedingTime) / 1000 + " secs total, " + nodesBred + " nodes, " + nodesBred / (((float) breedingTime) / 1000) + " nodes/sec");

            state.output.message("\n\n\nMemory Usage\n==============");
            state.output.message("Initialization: " + ((float) initializationUsage) / 1024 + " KB total, " + nodesInitialized + " nodes, " + nodesInitialized / (((float) initializationUsage) / 1024) + " nodes/KB");
            state.output.message("Evaluating: " + ((float) evaluationUsage) / 1024 + " KB total, " + nodesEvaluated + " nodes, " + nodesEvaluated / (((float) evaluationUsage) / 1024) + " nodes/KB");
            state.output.message("Breeding: " + ((float) breedingUsage) / 1024 + " KB total, " + nodesBred + " nodes, " + nodesBred / (((float) breedingUsage) / 1024) + " nodes/KB");
        }

    }
}
