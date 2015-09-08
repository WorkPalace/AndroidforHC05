package tr.com.srcn.bluetoothforhc05;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class ConnectedThread extends Thread {
	private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
 
    Handler mHandler;
    
    
    public ConnectedThread(BluetoothSocket socket, Handler handler) {
        
    	mHandler = handler;
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
    	
    	final byte delimiter = 10; //This is the ASCII code for a newline character
  	  	int readBufferPosition = 0;
  	  	byte[] readBuffer = new byte[1024];
  	  	
        // Keep listening to the InputStream until an exception occurs
        while (true) {

            try 
            {
                int bytesAvailable = mmInStream.available();                        
                if(bytesAvailable > 0)
                {
                    byte[] packetBytes = new byte[bytesAvailable];
                    mmInStream.read(packetBytes);
                    for(int i=0;i<bytesAvailable;i++)
                    {
                        byte b = packetBytes[i];
                        if(b == delimiter)
                        {
                        	byte[] encodedBytes = new byte[readBufferPosition];
                        	System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                        	final String data = new String(encodedBytes, "US-ASCII");
                        	if(data.trim().replace(" ", "").startsWith("#"))

                        		mHandler.obtainMessage(MainActivity.MESSAGE_READ, readBufferPosition , -1, data)
                        		.sendToTarget();
          
                        readBufferPosition = 0;

                        }
                        else
                        {
                            readBuffer[readBufferPosition++] = b;
                        }
                    }
                }
  
            }catch (IOException e) {
	            Log.e("Error2", "Hata var");
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
