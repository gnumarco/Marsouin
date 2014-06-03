/*
 Copyright 2006 by Sean Luke
 Licensed under the Academic Free License version 3.0
 See the file "LICENSE" for more information
 */
package es.gpc.gp.ec.app.control.func;

import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.util.DecodeReturn;
import es.gpc.gp.ec.util.Code;
import es.gpc.gp.ec.gp.ADFStack;
import es.gpc.gp.ec.gp.GPData;
import es.gpc.gp.ec.gp.ERC;
import es.gpc.gp.ec.gp.GPIndividual;
import es.gpc.gp.ec.gp.GPNode;
import es.gpc.gp.ec.app.control.ControlData;
import es.gpc.gp.ec.Problem;
import es.gpc.gp.ec.EvolutionState;
import java.io.*;


/* 
 * RegERC.java
 * 
 * Created: Wed Nov  3 18:26:37 1999
 * By: Sean Luke
 */
/**
 * @author Sean Luke
 * @version 1.0
 */
public class RegERC extends ERC {

    public double value;
    int sigDig = 9;

    // Koza claimed to be generating from [-1.0, 1.0] but he wasn't,
    // given the published simple-lisp code.  It was [-1.0, 1.0).  This is
    // pretty minor, but we're going to go with the code rather than the
    // published specs in the books.  If you want to go with [-1.0, 1.0],
    // just change nextDouble() to nextDouble(true, true)
    public void resetNode(final EvolutionState state, final int thread) {
        Double upBound; 
        Parameter p;
        p = new Parameter("eval.problem");
        upBound = state.parameters.getDouble(p.push("upBound"), null);
        sigDig = state.parameters.getInt(p.push("precision"), null);
        value = roundToNumberOfSignificantDigits(((state.random[thread].nextDouble() * 2d * upBound) - upBound),sigDig);
    }

    public int nodeHashCode() {
        // a reasonable hash code
        return this.getClass().hashCode() + Float.floatToIntBits((float) value);
    }

    public static double roundToNumberOfSignificantDigits(double num, int n) {

        final double maxPowerOfTen = Math.floor(Math.log10(Double.MAX_VALUE));

        if (num == 0) {
            return 0;
        }

        final double d = Math.ceil(Math.log10(num < 0 ? -num : num));
        final int power = n - (int) d;

        double firstMagnitudeFactor = 1.0;
        double secondMagnitudeFactor = 1.0;
        if (power > maxPowerOfTen) {
            firstMagnitudeFactor = Math.pow(10.0, maxPowerOfTen);
            secondMagnitudeFactor = Math.pow(10.0, (double) power - maxPowerOfTen);
        } else {
            firstMagnitudeFactor = Math.pow(10.0, (double) power);
        }

        double toBeRounded = num * firstMagnitudeFactor;
        toBeRounded *= secondMagnitudeFactor;

        final long shifted = Math.round(toBeRounded);
        double rounded = ((double) shifted) / firstMagnitudeFactor;
        rounded /= secondMagnitudeFactor;
        return rounded;
    }

    public boolean nodeEquals(final GPNode node) {
        // check first to see if we're the same kind of ERC -- 
        // won't work for subclasses; in that case you'll need
        // to change this to isAssignableTo(...)
        if (this.getClass() != node.getClass()) {
            return false;
        }
        // now check to see if the ERCs hold the same value
        return (((RegERC) node).value == value);
    }

    public void readNode(final EvolutionState state, final DataInput dataInput) throws IOException {
        value = dataInput.readDouble();
    }

    public void writeNode(final EvolutionState state, final DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(value);
    }

    public String encode() {
        return Code.encode(value);
    }

    public boolean decode(DecodeReturn dret) {
        // store the position and the string in case they
        // get modified by Code.java
        int pos = dret.pos;
        String data = dret.data;

        // decode
        Code.decode(dret);

        if (dret.type != DecodeReturn.T_DOUBLE) // uh oh!
        {
            // restore the position and the string; it was an error
            dret.data = data;
            dret.pos = pos;
            return false;
        }

        // store the data
        value = dret.d;
        return true;
    }

    public String toStringForHumans() {
        String res;
        if (value < 0) {
            res=  String.format("%."+sigDig+"f",value);
        } else {
            res= "" + String.format("%."+sigDig+"f",value);
        }
        return res;
    }

    public void eval(final EvolutionState state,
            final int thread,
            final GPData input,
            final ADFStack stack,
            final GPIndividual individual,
            final Problem problem) {
        ControlData rd = ((ControlData) (input));
        rd.x = value;
    }
}
