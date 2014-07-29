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
import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.EvolutionState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Set;

/* 
 * GPSetType.java
 * 
 * Created: Fri Aug 27 20:55:42 1999
 * By: Sean Luke
 */

/**
 * A GPSetType is a GPType which contains GPAtomicTypes in a set, and is used
 * as a generic GP type.  For more information, see GPType
 *
 * GPSetTypes implement their set using both a hash table and an array.
 * if the size of the set is "significantly big", then the hash table
 * is used to look up membership in the set (O(1), but with a big constant).
 * If the size is small, then the array is used (O(n)).  The dividing line
 * is determined by the constant START_USING_HASH_BEYOND, which you might
 * play with to optimize for your system.
 *
 * @see ec.gp.GPType
 * @author Sean Luke
 * @version 1.0 
 */

public final class GPSetType extends GPType
    {
    public static final String P_MEMBER = "member";
    public static final String P_SIZE = "size";

    /** A packed, sorted array of atomic types in the set */
    public int[] types_packed;

    /** A sparse array of atomic types in the set */
    public boolean[] types_sparse;

    /** The hashtable of types in the set */
    public HashMap<GPType,GPType> types_h;

    /** You should not construct new types. */
    public GPSetType() { }


    /** Sets up the packed and sparse arrays based on the hashtable */
    public void postProcessSetType(int totalAtomicTypes)
        {
        // load the hashtable into the arrays
        int x=0;
        types_packed = new int[types_h.size()];
        types_sparse = new boolean[totalAtomicTypes];
        Collection<GPType> e = types_h.values();
        for(GPType t:e)
        //while(e.hasMoreElements())
            {
            //GPAtomicType t = (GPAtomicType)(e.nextElement());
            types_packed[x++] = t.type;
            types_sparse[t.type] = true;
            }

        // Sort the packed array
        java.util.Arrays.sort(types_packed);
        }


    public void setup(final EvolutionState state, Parameter base)
        {
        super.setup(state,base);
        
        // Make my Hashtable
        types_h = new HashMap<>();

        // How many atomic types do I have?
        int len = state.parameters.getInt(base.push(P_SIZE),null,1);
        if (len<=0) 
            state.output.fatal("The number of atomic types in the GPSetType " +
                name + " must be >= 1.",base.push(P_SIZE));

        // Load the GPAtomicTypes
        for(int x=0;x<len;x++)
            {
            String s = state.parameters.getString(base.push(P_MEMBER).push(""+x),null);
            if (s==null)
                state.output.fatal("Atomic type member #" + x + 
                    " is not defined for the GPSetType " + name +
                    ".",base.push(P_MEMBER).push(""+x));
            GPType t = GPType.typeFor(s,state);
            if (!(t instanceof GPAtomicType)) // uh oh
                state.output.fatal("Atomic type member #" + x +
                    " of GPSetType " + name +
                    " is not a GPAtomicType.",
                    base.push(P_MEMBER).push(""+x));

            if (types_h.get(t)!=null)
                state.output.warning("Atomic type member #" + x +
                    " is included more than once in GPSetType " + 
                    name + ".",
                    base.push(P_MEMBER).push(""+x));
            types_h.put(t,t);
            }
        }

    
    public final boolean compatibleWith(final GPInitializer initializer,final GPType t)
        {
        // if the type is me, then I'm compatible with it.
        if (t.type == type) return true;

        // if the type is an atomic type, then I'm compatible with it if I contain it.
        // Use the sparse array.
        else if (t.type < initializer.numAtomicTypes) // atomic type, faster than doing instanceof
            return types_sparse[t.type];

        // else the type is a set type.  I'm compatible with it if we contain
        // an atomic type in common.   Use the sorted packed array.
        else
            {
            GPSetType s = (GPSetType)t;
            int x=0; int y=0;
            for( ; x < types_packed.length && y < s.types_packed.length ; )
                {
                if (types_packed[x] == s.types_packed[y]) return true;
                else if (types_packed[x] < s.types_packed[y]) x++;
                else y++;
                }
            return false;
            }
        }
    }
