package tr.com.srcn.bluetoothforhc05;

public class DatabaseList {
	int _id;
	String _title;
	String _rename;

	public DatabaseList() {       
    }
	
	public DatabaseList(int id, String title, String rename) {
		this._id = id;
		this._title = title;
		this._rename = rename;
	}
	public DatabaseList(String title, String rename){
		this._title = title;
		this._rename = rename;
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
	public String getName() {
		return this._rename;
	}
		 
	public void setName(String rename) {
		this._rename = rename;
	}
    //----------------------------------------------------------------	 
}
