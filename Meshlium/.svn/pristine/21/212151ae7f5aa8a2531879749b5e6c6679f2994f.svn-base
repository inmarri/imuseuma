/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.serial;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author Migue
 */
class TCPConnection extends Thread{
    private Socket server;
    private TCPListener listener;
    private boolean end;
    private String ip;
    private String input,line;
    private BufferedReader reader;
    private PrintStream printer;
    
    
    TCPConnection(Socket s, TCPListener tcplistener) {
        server=s;
        listener=tcplistener;
        end=false;
        ip = server.getInetAddress().getHostAddress();
    }

    @Override
    public void run() {
        input = "";
        
        try{
            reader = new BufferedReader( new InputStreamReader ( server.getInputStream() ) );
            printer = new PrintStream   ( server.getOutputStream());
            while(!end){
                // lectura de mensaje.
                input = reader.readLine();
                if(input!=null){
                    this.sendMessage(input);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    synchronized public void sendMessage(String msg) {
        this.printer.println(msg.toString());
    } 
}
