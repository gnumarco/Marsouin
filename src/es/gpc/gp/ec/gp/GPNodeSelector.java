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
package es.gpc.gp.ec.gp;
import es.gpc.gp.ec.Prototype;
import es.gpc.gp.ec.EvolutionState;

/* 
 * GPNodeSelector.java
 * 
 * Created: Tue Oct 12 17:08:10 1999
 * By: Sean Luke
 */

/**
 * GPNodeSelector is a Prototype which describes algorithms which
 * select random nodes out of trees, typically marking them for
 * mutation, crossover, or whatnot.  GPNodeSelectors can cache information
 * about a tree, as they may receive the pickNode(...) method more than
 * once on a tree.  But this should really only be done if it can be
 * done relatively efficiently; it's not all that common.  A GPNodeSelector
 * will be called reset() just before it is pressed into service in
 * selecting nodes from a new tree, which gives it the chance to
 * reset caches, etc.
 *
 * @author Sean Luke
 * @version 1.0 
 */

public interface GPNodeSelector extends Prototype 
    {
    /** Picks a node at random from tree and returns it.   The tree
        is located in ind, which is located in s.population[subpopulation].
        This method will be preceded with a call to reset();
        afterwards, pickNode(...) may be called several times for the
        same tree.
    */

    public abstract GPNode pickNode(final EvolutionState s,
        final int subpopulation,
        final int thread,
        final GPIndividual ind,
        final GPTree tree);

    /** Resets the Node Selector before a new series of pickNode()
        if need be. */
    public abstract void reset();

    }
