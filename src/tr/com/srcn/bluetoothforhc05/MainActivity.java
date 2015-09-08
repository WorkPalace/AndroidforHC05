package tr.com.srcn.bluetoothforhc05;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnItemClickListener {
	
	Button turnonoff, search, write, settime, addalarm;
	TextView status, read_tv, datetime_tv;
	ListView myListView;
	EditText write_text;
	CheckBox led6;
	
	BluetoothDevice device;
	BluetoothAdapter myBluetoothAdapter;
	
	Boolean connected = false;
	Boolean connected_device = false;
	
	ArrayList<BluetoothDevice> pairedDevices;	
	ArrayAdapter<String> listAdapter;
	
    TextClock textclock;
		
    protected static final int SUCCESS_CONNECT = 0;
    static final int MESSAGE_READ = 1;
	private static final int REQUEST_ENABLE_BT = 2;
    protected static final int REQUEST_DISABLE_BT = 3;

	static ConnectedThread connectedThread; 

	final BroadcastReceiver bReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
	        device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

		    // When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				
				// add the name and the MAC address of the object to the arrayAdapter
				pairedDevices.add(device);
				listAdapter.add(device.getName() + "\n" + device.getAddress());
				listAdapter.notifyDataSetChanged();
			}       
		}
	};

	Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch(msg.what) {
			
			case SUCCESS_CONNECT:
				
				connectedThread = new ConnectedThread((BluetoothSocket) msg.obj, mHandler);	
				connectedThread.start();
				Toast.makeText(getApplicationContext(), "Connected.", Toast.LENGTH_SHORT).show();
				status.setText("Connect to " + device.getName());
				connected_device = true;
			 break;
				
			case MESSAGE_READ:
				String read = (String) msg.obj;
				read_tv.setText(read);
			break;
			
			
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		turnonoff = (Button) findViewById(R.id.turnonoff);
		search = (Button) findViewById(R.id.search);
		write = (Button) findViewById(R.id.write);
		settime = (Button) findViewById(R.id.settime);

		status = (TextView) findViewById(R.id.status);
		read_tv = (TextView) findViewById(R.id.read);
		datetime_tv = (TextView) findViewById(R.id.datetime_tv);

		write_text = (EditText) findViewById(R.id.editText1);
		
		led6 = (CheckBox) findViewById(R.id.led6);
		
		textclock = (TextClock) findViewById(R.id.textClock1);
		
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		if(myBluetoothAdapter == null) {
			
			Toast.makeText(getApplicationContext(), "Your device does not support!!!",
					Toast.LENGTH_SHORT).show();
			turnonoff.setVisibility(View.GONE);
		}
		else{
			
			turnonoff.setOnClickListener(this);
		}		
		
		search.setOnClickListener(this);
		write.setOnClickListener(this);
		led6.setOnClickListener(this);
		settime.setOnClickListener(this);
		
		myListView = (ListView) findViewById(R.id.listView1);
		listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		pairedDevices = new ArrayList<BluetoothDevice>(); 
		myListView.setAdapter(listAdapter);
		myListView.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.create_alarm) {
			
			Toast.makeText(getApplicationContext(),"Create Alarm",
		              Toast.LENGTH_SHORT).show();
			
			Bundle bundle = new Bundle();
			Intent intent = new Intent(this, CreateAlarmActivity.class);
			//bundle.putSerializable("selected_database", xxx);
			intent.putExtras(bundle);
			startActivity(intent);				
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.turnonoff) {
			if(connected == false) {
				
				on();
				turnonoff.setText("TURN OFF");
			}
			else {
				
				off();
				turnonoff.setText("TURN ON");
			}
		}
		
		if(v.getId() == R.id.search) {
			
			search();
		}
		
		if(v.getId() == R.id.write) {
			
			String send = write_text.getText().toString();			

			
			if(connected_device == true) {
				connectedThread.write(send.getBytes());
			}	
		}
		
		if(v.getId() == R.id.led6) {
			
			if(led6.isChecked()) {
				
				String on = "LED6";
				on = on + '\n';
				if(connected_device == true)
					connectedThread.write(on.getBytes());	
			}
			else {
				
				String off = "LED6OFF";
				off = off + '\n';
				if(connected_device == true)
					connectedThread.write(off.getBytes());
			}
		}
		
		if(v.getId() == R.id.settime) {
			

						long msTime = System.currentTimeMillis();
						Date curDateTime = new Date(msTime);
						DateFormat dateFormat = new SimpleDateFormat("#HH:mm:ss:dd:MM:yyyy");
						String datetime = dateFormat.format(curDateTime);
						
						Calendar cal = Calendar.getInstance();
						cal.setTime(curDateTime);
						int dayofweek = cal.get(Calendar.DAY_OF_WEEK)-1;
						
						if(dayofweek == 0) {
							
							dayofweek=7;
						}
						
						datetime = datetime + ":" + String.valueOf(dayofweek);
						
						datetime_tv.setText(datetime);
						
						datetime = datetime + '\n';
						//if(connected_device == true)
							connectedThread.write(datetime.getBytes());	


		}
		
	}
	
	
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     if (resultCode == RESULT_OK) {

				turnonoff.setText("Turn OFF");
				connected = true;
				status.setText("BT Enable");
	     }
	     if(resultCode == RESULT_CANCELED) {
	    	 
				turnonoff.setText("Turn ON");
				connected = false;
				status.setText("BT Disable");

	     }
	  }
	
	
	
	public void search() {

		if (myBluetoothAdapter.isDiscovering()) {
			
			// the button is pressed when it discovers, so cancel the discovery
			myBluetoothAdapter.cancelDiscovery();
		}
		else {
			
			listAdapter.clear();
			myBluetoothAdapter.startDiscovery();         
			registerReceiver(bReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));   
			Toast.makeText(getApplicationContext(),"Searching Bluetooth Devices",
		              Toast.LENGTH_SHORT).show();
		}   
	}

	
	
	   public void on(){
		   
           connected = true;

	      if (!myBluetoothAdapter.isEnabled()) {
	    	 Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); 	
	    	 //IntentFilter disableBTIntent = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED) ; 
	    	 //registerReceiver(bReceiver, disableBTIntent);

             Toast.makeText(getApplicationContext(),"Bluetooth turned on" ,
	                 Toast.LENGTH_LONG).show();
             

	      }
	      else{
	    	  
	         Toast.makeText(getApplicationContext(),"Bluetooth is already on",
	        		 Toast.LENGTH_LONG).show();
	         status.setText("BT Enable");
	      }
	   }
	   
	   public void off(){
		   
		   myBluetoothAdapter.disable();
		   status.setText("BT Disable");
		   Toast.makeText(getApplicationContext(),"Bluetooth turned off",
		                 Toast.LENGTH_LONG).show();
		   
	       connected = false;
	   }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BluetoothDevice selectdevice = pairedDevices.get(position);
		ConnectThread connectThread = new  ConnectThread(selectdevice, mHandler, myBluetoothAdapter);
		connectThread.start();
	}
}