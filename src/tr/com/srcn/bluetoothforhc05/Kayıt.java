package tr.com.srcn.bluetoothforhc05;

public class Kayýt {
    
	int _id;
	String _title;
	String _description;
    String _isaretli_mi;
    String _indent;
    String _date;
    String _time;
    String _pending_intent;
    String _pending_id;
    
    // Empty constructor
    public Kayýt() {
         
    }
		
	public Kayýt(int id, String title, String description, String isaretli_mi, String indent, String date, String time, String pending_intent, String pending_id) {
		this._id = id;
		this._title = title;
		this._description = description;
		this._isaretli_mi = isaretli_mi;
		this._indent = indent;
		this._date = date;
		this._time = time;
		this._pending_intent = pending_intent;
		this._pending_id = pending_id;
	}
	
	public Kayýt(String title, String description, String isaretli_mi, String indent, String date, String time, String pending_intent, String pending_id) {
		this._title = title;
		this._description = description;
		this._isaretli_mi = isaretli_mi;
		this._indent = indent;
		this._date = date;
		this._time = time;
		this._pending_intent = pending_intent;
		this._pending_id = pending_id;
	}		
	
//---------------------------------------------------------------
    public int getID(){
        return this._id;
    }
     
    public void setID(int id){
        this._id = id;
    }
//---------------------------------------------------------------	 
	public String getTitle() {
	        return this._title;
	}
	 
	public void setTitle(String title) {
	        this._title = title;
	}
//----------------------------------------------------------------	 
	public String getDescription() {
	        return this._description;
	}
	 
	public void setDescription(String description) {
	        this._description = description;
	}
//-----------------------------------------------------------------	
	public String getIsaretliMi() {
        return this._isaretli_mi;
    }
 
    public void setIsaretliMi(String isaretli_mi) {
        this._isaretli_mi = isaretli_mi;
    }		
//-----------------------------------------------------------------
    public String getIndent(){
        return this._indent;
    }
     
    public void setIndent(String indent){
        this._indent = indent;
    }
//---------------------------------------------------------------	 
    public String getDate(){
        return this._date;
    }
     
    public void setDate(String date){
        this._date = date;
    }
//---------------------------------------------------------------	 
    public String getTime(){
        return this._time;
    }
     
    public void setTime(String time){
        this._time = time;
    }
//---------------------------------------------------------------	 
    public String getPendingIntent(){
        return this._pending_intent;
    }
     
    public void setPendingIntend(String pending_intent){
        this._pending_intent = pending_intent;
    }
//---------------------------------------------------------------	
    public String getPendingId(){
        return this._pending_id;
    }
     
    public void setPendingId(String pending_id){
        this._pending_id = pending_id;
    }
//---------------------------------------------------------------	     
}
