package org.serial;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import meshlium.MeshliumApplication;

/**
 *
 * @author Migue
 */
public class TCPListener extends Thread{
    private Integer TCP_PORT = null;
    private MeshliumApplication launcher = null;
    private boolean end;

    public TCPListener(Integer tcpport, MeshliumApplication ma) {
        TCP_PORT = tcpport;
        launcher = ma;
        end = false;
    }
    
    @Override
    public void run(){
        TCPConnection conn;
        
        try{
            ServerSocket listener=new ServerSocket(TCP_PORT);
            Socket server;
            while(!end){
                server=listener.accept();
                conn=new TCPConnection(server,this);
                
                conn.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
