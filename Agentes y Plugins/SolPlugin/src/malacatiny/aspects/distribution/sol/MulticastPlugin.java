package malacatiny.aspects.distribution.sol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import malacatiny.aspects.representation.aurora.AuroraMessage;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;
import android.util.Log;
import candileja.core.aspect.distribution.DistributionAspect;

public class MulticastPlugin extends DistributionAspect implements Runnable{
	// debuging
	//private String tag="multicast";
	//private Boolean d=true;
	
	private String ip, aid;//,groupId;
	private int port;
	private Boolean end;
	private MulticastSocket multicastSocket;
	
	public MulticastPlugin(/*String g,*/String a,String i,int p){
		//groupId=g;
		ip=i;
		aid=a;
		port=p; 
		end=false;
		//this.getMediator().addActiveAspect(groupId, this);
	}

	// este método no se utiliza...
	@Override
	public Object handleOutputMessage(Object message) {
		return new Integer(0);
	}

	@Override
	public Object getAID() {
		return aid;
	}
	
	public void end(){
		end=true;
	}

	public void run() {	
		DatagramPacket pack = null;
		
        // Create the socket and bind it to port 'port'.
		multicastSocket = null;
		/*try {
			multicastSocket = new MulticastSocket(port);
			// join the multicast group
			multicastSocket.joinGroup(InetAddress.getByName(ip));
			System.out.println(InetAddress.getByName(ip));
		} catch (IOException e) {
			e.printStackTrace();
		}*/	
		// Now the socket is set up and we are ready to receive packets
		// Create a DatagramPacket and do a receive
		
		InetAddress group = null;
		try {
			group = InetAddress.getByName(ip.toString());
			multicastSocket = new MulticastSocket(port);
			System.out.println(group);
			// join the multicast group
			multicastSocket.joinGroup(group);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
      	/*try {
      		multicastSocket = new MulticastSocket(8080);
    		InetAddress address = InetAddress.getByName("224.2.2.3");
			multicastSocket.joinGroup(address);
			System.out.println(address);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		byte buf[];
		
		while(!end){
			//if(d)Log.d(tag,"MulticastPlugin is running at "+ip+":"+port);
			buf = new byte[1024];
			pack = new DatagramPacket(buf, buf.length);	
			try {
				multicastSocket.receive(pack);
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] content=pack.getData();
			boolean end=false;
			int i = content.length;
			boolean fin = false;
			while (i > 0 && !fin) {
				if(content[i-1] != 0) fin = true;
				i--;
			}
			
			byte[] contentAux = new byte[i+1];
			int j;
			for (j = 0; j <= i; j++) {
				contentAux[j] = content[j];
			}
			/*j--;
			byte[] cont = new byte[3];
			int s=2;
			for(int p=j;p>j-3;p--){
				cont[s]=contentAux[p];
				s--;
			}
			Log.d("loop",new String(cont));*/
			content = contentAux;
			String stringMessage=new String(content);
			AuroraMessage msg=new AuroraMessage(stringMessage);
			//if(d)Log.d(tag, "Se ha recibido el mensaje de multicast "+msg.toString());
			handleInputMessage(msg);
			/*multicastSocket.close();
			try {
				multicastSocket = new MulticastSocket(port);
				multicastSocket.joinGroup(group);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		// And when we have finished receiving data leave the multicast group and
		// close the socket
		try {
			multicastSocket.leaveGroup(InetAddress.getByName(ip));
			multicastSocket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@Override
	public void stopPlugin() {
		end=true;
		
	}
	
	

}
