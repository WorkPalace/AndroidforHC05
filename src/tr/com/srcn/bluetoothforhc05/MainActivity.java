package tr.com.srcn.bluetoothforhc05;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.UUID;

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
import android.util.Log;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener, OnItemClickListener {
	
	Button quit, turnonoff, search, write, settime;
	TextView status,read_tv, datetime_tv;
	ListView myListView;
	EditText write_text;
	CheckBox led6;
	
	BluetoothAdapter myBluetoothAdapter;
	Boolean connected = false;
	
	ArrayList<BluetoothDevice> pairedDevices;	
	ArrayAdapter<String> listAdapter;
	
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
    protected static final int SUCCESS_CONNECT = 0;
    private static final int MESSAGE_READ = 1;
	private static final int REQUEST_ENABLE_BT = 2;

	ConnectedThread connectedThread; 

	final BroadcastReceiver bReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
		    // When discovery finds a device
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				
				// Get the BluetoothDevice object from the Intent
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
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
				
				connectedThread = new ConnectedThread((BluetoothSocket) msg.obj);	
				connectedThread.start();
				Toast.makeText(getApplicationContext(), "Connected.", Toast.LENGTH_SHORT).show();
				//Do Smt
				
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
		
		quit = (Button) findViewById(R.id.quit);
		turnonoff = (Button) findViewById(R.id.turnonoff);
		search = (Button) findViewById(R.id.search);
		write = (Button) findViewById(R.id.write);
		settime = (Button) findViewById(R.id.settime);

		status = (TextView) findViewById(R.id.status);
		read_tv = (TextView) findViewById(R.id.read);
		datetime_tv = (TextView) findViewById(R.id.datetime_tv);

		write_text = (EditText) findViewById(R.id.editText1);
		
		led6 = (CheckBox) findViewById(R.id.led6);
		
		myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(myBluetoothAdapter == null) {
			
			Toast.makeText(getApplicationContext(), "Your device does not support!!!",
					Toast.LENGTH_SHORT).show();
			turnonoff.setVisibility(View.GONE);
		}
		else{
			
			turnonoff.setOnClickListener(this);
		}
		
		if(connectedThread != null) {
			
			connectedThread.run();
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			send = send + '\n';
			connectedThread.write(send.getBytes());		
		}
		
		if(v.getId() == R.id.led6) {
			
			if(led6.isChecked()) {
				
				String on = "LED6";
				on = on + '\n';
				connectedThread.write(on.getBytes());	
			}
			else {
				
				String off = "LED6OFF";
				off = off + '\n';
				connectedThread.write(off.getBytes());
			}
		}
		
		if(v.getId() == R.id.settime) {
			


		}
		
	
	}
	
	
	
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     if (resultCode == RESULT_OK) {
	    	 Toast.makeText(getApplicationContext(),"You select Yes.",
		              Toast.LENGTH_SHORT).show();
				turnonoff.setText("Turn OFF");
				connected = true;
				status.setText("Status: Enable BT");
				
				


	     }
	     if(resultCode == RESULT_CANCELED) {
	    	 Toast.makeText(getApplicationContext(),"You select No.",
		              Toast.LENGTH_SHORT).show();
				turnonoff.setText("Turn ON");
				connected = false;
				status.setText("Status: Disable BT");

	     }
	  }
	
	
	/*
	public void listpaireddevice() {

		// get paired device
		devices = myBluetoothAdapter.getBondedDevices();
		// put it's one to the adapter
		listAdapter.clear();
		for(BluetoothDevice device : pairedDevices)
			listAdapter.add(device.getName()+ "\n" + device.getAddress());
			Toast.makeText(getApplicationContext(),"Show Paired Devices",
		              Toast.LENGTH_SHORT).show();
	}
	*/
	
	
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
		   //status.setText("Status: Connected");

	      if (!myBluetoothAdapter.isEnabled()) {
	    	 Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
	    	    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); 	 
             Toast.makeText(getApplicationContext(),"Bluetooth turned on" ,
	                 Toast.LENGTH_LONG).show();
             

	      }
	      else{
	    	  
	         Toast.makeText(getApplicationContext(),"Bluetooth is already on", 
	        		 Toast.LENGTH_LONG).show();
	      }
	   }
	   
	   public void off(){
		   
		   myBluetoothAdapter.disable();
		   status.setText("Status: Disable");
		   Toast.makeText(getApplicationContext(),"Bluetooth turned off",
		                 Toast.LENGTH_LONG).show();
		   
	       connected = false;
	   }
	   
	   
	   
	   
	   
	   private class ConnectThread extends Thread {
		    private final BluetoothSocket mmSocket;
		    private final BluetoothDevice mmDevice;
		 
		    public ConnectThread(BluetoothDevice device) {
		        // Use a temporary object that is later assigned to mmSocket,
		        // because mmSocket is final
		        BluetoothSocket tmp = null;
		        mmDevice = device;
		 
		        // Get a BluetoothSocket to connect with the given BluetoothDevice
		        try {
		            // MY_UUID is the app's UUID string, also used by the server code
		            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
		        } catch (IOException e) { }
		        mmSocket = tmp;
		    }
		 
		    public void run() {
		        // Cancel discovery because it will slow down the connection
		        myBluetoothAdapter.cancelDiscovery();
		 
		        try {
		            // Connect the device through the socket. This will block
		            // until it succeeds or throws an exception
		            mmSocket.connect();
		        } catch (IOException connectException) {
		            // Unable to connect; close the socket and get out
		            try {
		                mmSocket.close();
		            } catch (IOException closeException) { }
		            return;
		        }
		 
		        // Do work to manage the connection (in a separate thread)
		        mHandler.obtainMessage(SUCCESS_CONNECT, mmSocket).sendToTarget();
		    }

			/** Will cancel an in-progress connection, and close the socket */
		    public void cancel() {
		        try {
		            mmSocket.close();
		        } catch (IOException e) { }
		    }
		}
	   
	   
	   
	   
	   	   
	   private class ConnectedThread extends Thread {
			private final BluetoothSocket mmSocket;
		    private final InputStream mmInStream;
		    private final OutputStream mmOutStream;
		 
		    public ConnectedThread(BluetoothSocket socket) {
		        mmSocket = socket;
		        InputStream tmpIn = null;
		        OutputStream tmpOut = null;
		 
		        // Get the input and output streams, using temp objects because
		        // member streams are final
		        try {
		            tmpIn = socket.getInputStream();
		            tmpOut = socket.getOutputStream();
		        } catch (IOException e) { }
		 
		        mmInStream = tmpIn;
		        mmOutStream = tmpOut;
		    }
		    
		    
		    public void run() {
		    	byte[] buffer = new byte[1024];
	            int bytes = 0; 
		        // Keep listening to the InputStream until an exception occurs
		        while (true) {
		            try {
		            	bytes = mmInStream.read(buffer);            //read bytes from input buffer
	                    for(int i = 0; i<10; i++) {
	                    	if(buffer[i] == "#".getBytes()[0]) {
	                    		String readMessage = new String(buffer, i, 22);
	                    		//String readMessage = String.valueOf(buffer.length);
	                    		// Send the obtained bytes to the UI Activity
	                    		mHandler.obtainMessage(MESSAGE_READ, bytes, -1, readMessage)
	                            .sendToTarget();
	                    	}
	                    }	
		            } catch (IOException e) {
			            Log.e("Error2", "Hata var dayý");

		                break;
		            }
		        }
		    }
		  
		    /* Call this from the main activity to send data to the remote device */
		    public void write(byte[] bytes) {
		        try {
		            mmOutStream.write(bytes);
		        } catch (IOException e) { }
		    }
		 
		    /* Call this from the main activity to shutdown the connection */
		    public void cancel() {
		        try {
		            mmSocket.close();
		        } catch (IOException e) { }
		    }
		}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		BluetoothDevice selectdevice = pairedDevices.get(position);
		ConnectThread connectThread = new  ConnectThread(selectdevice);
		connectThread.start();
	}
	   
	   
}
