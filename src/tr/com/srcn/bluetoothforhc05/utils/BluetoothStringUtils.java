package tr.com.srcn.bluetoothforhc05.utils;

import tr.com.srcn.bluetoothforhc05.MyApplication;
import tr.com.srcn.bluetoothforhc05.models.MyLab;

public class BluetoothStringUtils {
	private static final String TAG = BluetoothStringUtils.class.getSimpleName();
	
	private static final String KEY_MESSGE = "\"mS\"";
	private static final String KEY_CURRENT_TIME = "\"cT\"";
	
	private static final int VALUE_SET_ALL_BOXES = 1;
	private static final int VALUE_SYNCH = 2;
	
	public static String setAllBoxesActionBluetoothString(){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		sb.append("{");
		sb.append(KEY_MESSGE+":");sb.append(VALUE_SET_ALL_BOXES);sb.append(",");
		sb.append(KEY_CURRENT_TIME+":");sb.append(System.currentTimeMillis()/1000);
		sb.append("}");sb.append(",");
		
		sb.append(MyLab.get(MyApplication.sApplicationContext).getBluetoothSudoJsonForAllBoxes());
		
		sb.append("]");
		return sb.toString();
	}
}
