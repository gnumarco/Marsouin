/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.gpc.utils;

import java.io.Serializable;

/**
 *
 * @author marc
 */
public class Memory implements Serializable{
    
    public double[] sens;
    public boolean newSens=false;
    public double[] act;
    public boolean newAct=false;
    public boolean indivFinished = false;
    public double fit;
}
