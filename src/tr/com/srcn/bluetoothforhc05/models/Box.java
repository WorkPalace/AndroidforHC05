package tr.com.srcn.bluetoothforhc05.models;

import java.io.Serializable;
import java.util.Date;

public class Box implements Serializable, Comparable<Box>{
	private static final String TAG = Box.class.getSimpleName();
	
	private final int MAX_BOX_SIZE = 28;
	private int boxNumer;
	private Date dateTime;
	private BoxStates boxState;
	
	public static enum BoxStates{
		EMPTY_CLSOE("EMPTY_CLOSE"),
		EMPTY_OPEN("EMPTY_OPEN"),
		FULL_COSE("FULL_CLOSE"),
		FULL_OPEN("FULL_OPEN");
		
		private final String tag;
		
		BoxStates(String tag){
			this.tag = tag;
		}
		
		@Override
		public String toString() {
			return tag;
		}
	}
	
	//Constructors
	public Box(int boxNumber){
		this(boxNumber, null);
	}
	public Box(int boxNumber, Date dateTime){
		setBoxNumber(boxNumber);
		setDateTime(dateTime);
		setBoxState(BoxStates.EMPTY_CLSOE);
	}
	
	//Setters Getters
	public int getBoxNumber(){
		return boxNumer;
	}
	public void setBoxNumber(int boxNumber){
		if(boxNumber >= 0 && boxNumber <= MAX_BOX_SIZE) // between 0 and MAXX_BOX_SIZE
			this.boxNumer = boxNumber;
		else
			this.boxNumer = -1;
	}
	
	public Date getDateTime(){
		return dateTime;
	}
	public void setDateTime(Date newDateTime){
		if(newDateTime == null)
			this.dateTime = new Date();
		else
			this.dateTime = new Date(newDateTime.getTime());
	}
	
	public BoxStates getBoxState(){
		return boxState;
	}
	public void setBoxState(BoxStates boxState){
		this.boxState = boxState;
	}
	
	@Override
	public int compareTo(Box otherBox) {
		if(getDateTime() == null || otherBox.getDateTime() == null)
			return 0;
		
		return getDateTime().compareTo(otherBox.getDateTime());
	}
	
	/** Return SudoJson string for box instance with alarmTime is long:
	 * JsonString: {"boxNumer":1,"boxState":0,"alarmTime":1445258741847}
	 * SudoJsonString: [1,0,1445258741847]
	 */
	public String toBluetoothSudoJson(){
		long alarmTime = getDateTime().getTime();
		int bState = 0; //all boxes initially EMPTY_CLOSE
		switch (getBoxState()) {
			case EMPTY_CLSOE:
				bState = 0;
				break;
	
			case EMPTY_OPEN:
				bState = 1;
				break;
			
			case FULL_COSE:
				bState = 2;
				break;
				
			case FULL_OPEN:
				bState = 3;
				break;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"bN\":");sb.append(getBoxNumber());sb.append(",");
		sb.append("\"bS\":");sb.append(bState);sb.append(",");
		sb.append("\"aT\":");sb.append((alarmTime/1000));
		sb.append("}");
		
		return sb.toString();
	}
	
	
	
	
	
}
