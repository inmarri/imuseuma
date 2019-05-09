package inma.visitoragent;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

public class VisitorAgentActivity extends Activity {
	private VisitorAgent va;
	
    /** Called when the activity is first created. */
	private BluetoothAdapter btAdapter = null;
	
	 // Actualización de la interfaz
	 public final static int ADD_MSG=0;
	 public final static int ADD_STATUS = 1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
		/* Turn off multicast filter */
		WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE); 
		WifiManager.MulticastLock multicastLock = wm.createMulticastLock("mydebuginfo"); 
		multicastLock.acquire();
        
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        va=new VisitorAgent(this,btAdapter);
        va.runAgent();
    }

	@Override
	public void onBackPressed() {
		va.killAgent();
		super.onBackPressed();
	}
	
	public void addMessage(String content){
    	Bundle bundle=new Bundle();
    	bundle.putInt("label", ADD_MSG);
    	bundle.putString("content",content);
    	Message message=new Message();
    	message.setData(bundle);
    	handler.sendMessage(message);
    }
	
	/*public void addStatus(String content){
    	Bundle bundle=new Bundle();
    	bundle.putInt("label", ADD_STATUS);
    	bundle.putString("content",content);
    	Message message=new Message();
    	message.setData(bundle);
    	handler.sendMessage(message);
    }*/
	
	private Handler handler=new Handler(){
		
    	public void handleMessage(Message msg){
    		int label;
    	
			label=msg.getData().getInt("label");
			
			switch(label){
			case ADD_MSG:
				String content=msg.getData().getString("content");
				TextView text=(TextView)findViewById(R.id.text);
				text.setText(content+"");
				break;
				
			/*case ADD_STATUS:
				String content1=msg.getData().getString("content");
				TextView text1=(TextView)findViewById(R.id.textView3);
				text1.setText(content1+"");
				break;*/
			}
			
    	}
    };
    
}