package inma.guideagent;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GuideAgentActivity extends Activity {
	 GuideAgent ga;
	 private BluetoothAdapter btAdapter;
	 
	 // Actualización de la interfaz
	 public final static int ADD_TIME=0;	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        ga=new GuideAgent(this,btAdapter);
        ga.runAgent();
        
        final Button button = (Button) findViewById(R.id.sendButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ga.throwEvent(new ButtonEvent());
            }
        });
    }

	@Override
	public void onBackPressed() {
		ga.killAgent();
		super.onBackPressed();
	}
	
	public void addTime(Long time){
    	Bundle bundle=new Bundle();
    	bundle.putInt("label", ADD_TIME);
    	bundle.putLong("content",time);
    	Message message=new Message();
    	message.setData(bundle);
    	handler.sendMessage(message);
    }
    
	
	private Handler handler=new Handler(){
			
	    	public void handleMessage(Message msg){
	    		int label;
	    	
				label=msg.getData().getInt("label");
				
				switch(label){
				case ADD_TIME:
					Long time=msg.getData().getLong("content");
					TextView text=(TextView)findViewById(R.id.text);
					text.setText(time+"");
					break;
				}
				
	    	}
	    };
	    
    
}