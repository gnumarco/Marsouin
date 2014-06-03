/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.gpc.utils;

import java.util.HashMap;



/**
 *
 * @author marc
 */
public class Message {
    public String type;
    public double[] value;
    public HashMap<String, String> config;
    
    public void Message(){
        type = null;
        value = null;
        config = new HashMap<>();
    }
    
    public void message(String t, double[] v){
        type = t;
        value = v;
        config = new HashMap<>();
    }
    
}
