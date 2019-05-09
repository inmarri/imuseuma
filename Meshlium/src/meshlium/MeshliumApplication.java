/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package meshlium;

import org.serial.Meshlium;
import org.serial.TCPListener;

/**
 *
 * @author carmine
 */
public class MeshliumApplication  {
    private static Meshlium meshlium;
    private Integer TCP_PORT=8080;
    
    public void run(){
        TCPListener tcplistener = new TCPListener(TCP_PORT, this);
        tcplistener.start();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      System.out.println("Sniffer updated");
      meshlium = new Meshlium();
      meshlium.connect();
      
    }

}
