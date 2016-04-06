/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homework1.GATE;
import gate.util.GateException;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author Standard
 */
public class GATEEmbeded {
    public static void main(String[] args) throws IOException, GateException {
        
        GateClient client = new GateClient();        
        Map<String, ItemFeatures> output;    
        output = client.run1("http://www.bbc.com/news/world-europe-35966412");
        System.out.println(output);
    }
    
    
}



