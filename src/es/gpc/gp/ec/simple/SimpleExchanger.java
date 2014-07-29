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
import es.gpc.gp.ec.steadystate.SteadyStateExchangerForm;
import es.gpc.gp.ec.EvolutionState;
import es.gpc.gp.ec.Population;
import es.gpc.gp.ec.Exchanger;
import es.gpc.gp.ec.util.Parameter;

/* 
 * SimpleExchanger.java
 * 
 * Created: Tue Aug 10 21:59:17 1999
 * By: Sean Luke
 */

/**
 * A SimpleExchanger is a default Exchanger which, well, doesn't do anything.
 * Most applications don't need Exchanger facilities; this simple version
 * will suffice.
 * 
 * <p>The SimpleExchanger implements the SteadyStateExchangerForm, mostly
 * because it does nothing with individuals.  For this reason, it is final;
 * implement your own Exchanger if you need to do something more advanced.
 *
 * @author Sean Luke
 * @version 1.0 
 */

public final class SimpleExchanger extends Exchanger implements SteadyStateExchangerForm
    {
    public void setup(final EvolutionState state, final Parameter base) { }

    /** Doesn't do anything. */
    public void initializeContacts(final EvolutionState state)
        {
        // don't care
        return;
        }

    /** Doesn't do anything. */
    public void reinitializeContacts(final EvolutionState state)
        {
        // don't care
        return;
        }

    /** Simply returns state.population. */
    public Population preBreedingExchangePopulation(final EvolutionState state)
        {
        // don't care
        return state.population;
        }

    /** Simply returns state.population. */
    public Population postBreedingExchangePopulation(final EvolutionState state)
        {
        // don't care
        return state.population;
        }

    /** Doesn't do anything. */
    public void closeContacts(final EvolutionState state, final int result)
        {
        // don't care
        return;
        }

    /** Always returns null */
    public String runComplete(final EvolutionState state)
        {
        return null;
        }

    }
