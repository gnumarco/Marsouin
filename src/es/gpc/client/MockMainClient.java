/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.gpc.client;

/**
 *
 * @author marc
 */

public class MockMainClient {
    
    static MockControlClient mock = null;

    public static void main(String[] args) throws Exception {
       
        mock = new MockControlClient();
        mock.setURL("http://localhost:8080/control/");
        mock.startDSystemForLearning();      
    }
    
}
