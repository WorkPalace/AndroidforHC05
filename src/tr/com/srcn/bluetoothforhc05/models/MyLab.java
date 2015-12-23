package tr.com.srcn.bluetoothforhc05.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;

/**
 * Singleton class that holds all Boxes
 */
public class MyLab {
	private static final String TAG = MyLab.class.getSimpleName();
	
	private static MyLab sMyLab;
	private List<Box> mBoxes;
	
	//Constructor private
	private MyLab(Context context){
		mBoxes = new ArrayList<>();
		create5DummyBoxes();
	}

	/** Singleton method to create MyLab instance */
	public static MyLab get(Context context){
		if(sMyLab == null)
			sMyLab = new MyLab(context);
		return sMyLab;
	}
	
	//Getters Setters
	public List<Box> getBoxes(){
		return mBoxes;
	}
	
	/** Return specific box with the boxNumber
	 *  If boxNumber doesnt exist return null */
	public Box getBox(int boxNumber){
		for(Box box : mBoxes){
			if(box.getBoxNumber() == boxNumber)
				return box;
		}
		return null;
	}
	
	public void addBox(Box box){
		mBoxes.add(box);
	}
	
	/** Return SudoJson array string for all boxes with long 
	 * Json array String: 
	 * [
	 *  {"boxNumber":0,"boxState":0,"alarmTime":1445587889660},
	 *  {"boxNumber":1,"boxState":0,"alarmTime":1445599889660}
	 * ]
	 * SudoJson array String:
	 * [
	 * 	[0,0,1445598789660],
	 * 	[1,0,1445599889660]
	 * ]
	 * */
	public String getBluetoothSudoJsonForAllBoxes(){
		StringBuilder sb = new StringBuilder();
		int i = 0;
		
		for(Box box : mBoxes){
			sb.append(box.toBluetoothSudoJson());
			if(i < mBoxes.size() - 1)
				sb.append(",");
			i++;
		}
		
		return sb.toString();
	}
	
	//Create 5 dummy boxes as seed
	public void create5DummyBoxes() {
		mBoxes.clear();
		Date d;
		Box box;
		for(int i = 0; i < 5; i++){
			//date objects with 1 hour intervall
			d = new Date(System.currentTimeMillis() + ((i+1)*1000*60*5));
			box = new Box(i, d);
			box.setBoxState(i%2 == 0 ? Box.BoxStates.FULL_COSE : Box.BoxStates.EMPTY_CLSOE);
			mBoxes.add(box);
		}
	}

	//Create 4 dummy boxes as seed with boxNumber: 0,2,4,1 with 45 sec. intervall.
		public void create4DummyBoxes() {
			mBoxes.clear();
			Date d;
			Box box;
			
			//objects with  45 sec intervall
			d = new Date(System.currentTimeMillis() + (1000*180));
			box = new Box(1, d);
			box.setBoxState(Box.BoxStates.EMPTY_CLSOE);
			mBoxes.add(box);
			
			d = new Date(d.getTime() + (1000*150));
			box = new Box(2, d);
			box.setBoxState(Box.BoxStates.EMPTY_CLSOE);
			mBoxes.add(box);

			d = new Date(d.getTime() + (1000*150));
			box = new Box(4, d);
			box.setBoxState(Box.BoxStates.EMPTY_CLSOE);
			mBoxes.add(box);
			
			d = new Date(d.getTime() + (1000*150));
			box = new Box(0, d);
			box.setBoxState(Box.BoxStates.EMPTY_CLSOE);
			mBoxes.add(box);
			
			d = new Date(d.getTime() + (1000*150));
			box = new Box(3, d);
			box.setBoxState(Box.BoxStates.EMPTY_CLSOE);
			mBoxes.add(box);
			Log.d(TAG, mBoxes.toString());
		}
}
