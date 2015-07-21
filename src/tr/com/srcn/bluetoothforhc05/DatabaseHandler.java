package tr.com.srcn.bluetoothforhc05;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static DatabaseHandler sSingleton;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "tasksManager";
 
    // Contacts table name
    //public static final String TABLE_KAYITLAR[] = {"My Task List"};
    public static final List<String> TABLE_KAYITLAR = new ArrayList<String>();    
    public static final String DATABASE_TABLE_LIST = "table_list";
    public static final String FIRST_OPEN = "first_open";


    // Contacts Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IS_MI = "isaretli_mi";
    public static final String KEY_INDENT = "indent";
    public static final String DROP = "drop";
    public static final String KEY_DATE = "date";
    public static final String KEY_TIME = "time";
    public static final String KEY_PENDING_INTENT = "pending_intent";
    public static final String KEY_PENDING_ID = "pending_id";
    
    public static final String KEY_NAME = "rename";
    
    SQLiteDatabase db; 
    

    
    int i=0;
    
    //---------------------------------------------------------------------------
 	synchronized static DatabaseHandler getInstance(Context ctxt) {
 		if (sSingleton == null) {
 			sSingleton = new DatabaseHandler(ctxt);
 		}
 		return sSingleton;
 	}
 	//-------------------------------------------------------------------
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }    
    //-------------------------------------------------------------------
	public DatabaseHandler openConnection() throws SQLException {
		if (db == null) {
			db = sSingleton.getWritableDatabase();
		}
		return this;
	}
	public synchronized void closeConnection() {
		if (sSingleton != null) {
			sSingleton.close();
			db.close();
			sSingleton = null;
			db = null;
		}
	}	
//--------------------------------------------------------------	
	
	
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
    	try {//sercan
    	db.beginTransaction();//sercan
    	
    	String CREATE_DATABASE_TABLE_LIST = "CREATE TABLE " + DATABASE_TABLE_LIST + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," +  KEY_NAME + " TEXT " + ")";
        db.execSQL(CREATE_DATABASE_TABLE_LIST);

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, "My Task List"); // Contact Name
        values.put(KEY_NAME, "My Task List"); // Contact Name
        db.insert(DATABASE_TABLE_LIST, null, values);
 
        String CREATE_KAYITLAR_TABLE = "CREATE TABLE " + "My_Task_List" + "("
                         + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                         + KEY_DESCRIPTION + " TEXT," + KEY_IS_MI + " TEXT," +  KEY_INDENT + " TEXT," 
                         + KEY_DATE + " TEXT," + KEY_TIME + " TEXT," + KEY_PENDING_INTENT + " TEXT," + KEY_PENDING_ID + " TEXT " + ")";             
        db.execSQL(CREATE_KAYITLAR_TABLE);
                 
        TABLE_KAYITLAR.add("My_Task_List");
        

        db.setTransactionSuccessful();//sercan
         
    	}
    	
    	finally {
			db.endTransaction();
		}

    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    	// Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_KAYITLAR.get(0));
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_KAYITLAR.get(1));
        // Create tables again
        onCreate(db);
    }
//******************************************************
    public void TableList(String TABLE_NAME) {
    	
    	TABLE_NAME = TABLE_NAME.trim().replace(" ", "_");
        TABLE_KAYITLAR.add(TABLE_NAME);
    }
    
    public void Create(String TABLE_NAME) {
    	
        	db = this.getWritableDatabase();
        	TABLE_NAME = TABLE_NAME.trim().replace(" ", "_");

    	    String CREATE_KAYITLAR_TABLE = "CREATE TABLE " + TABLE_NAME + "("
    	                         + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
    	                         + KEY_DESCRIPTION + " TEXT," + KEY_IS_MI + " TEXT," +  KEY_INDENT + " TEXT," 
    	                         + KEY_DATE + " TEXT," + KEY_TIME + " TEXT," + KEY_PENDING_INTENT + " TEXT," + KEY_PENDING_ID + " TEXT " + ")";             
    	                 db.execSQL(CREATE_KAYITLAR_TABLE);
    	                 
    	   //TABLE_KAYITLAR.add(TABLE_NAME.trim().replace(" ", ""));
    	   db.close();
    }
    
    public void deleteDB() {
    	
        TABLE_KAYITLAR.clear();
    }
    
//*******************************************************    
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    public void deleteTable(int location){
    	
        db = this.getWritableDatabase();   	
    	String deleteSQL = "DELETE FROM " + TABLE_KAYITLAR.get(location);
    	db.execSQL(deleteSQL);	
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KAYITLAR.get(location));
        db.close();
    }
    
    void addKayýt(Kayýt kayýt, int location) {
        db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, kayýt.getTitle()); // Contact Name
        values.put(KEY_DESCRIPTION, kayýt.getDescription()); // Contact Phone
        values.put(KEY_IS_MI, kayýt.getIsaretliMi()); // Contact Name
        values.put(KEY_INDENT, kayýt.getIndent()); // Contact Name
        values.put(KEY_DATE, kayýt.getDate()); // Contact Name
        values.put(KEY_TIME, kayýt.getTime()); // Contact Name
        values.put(KEY_PENDING_INTENT, kayýt.getPendingIntent()); // Contact Name
        values.put(KEY_PENDING_ID, kayýt.getPendingId()); // Contact Name

        // Inserting Row
        db.insert(TABLE_KAYITLAR.get(location), null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single contact
    public Kayýt getKayýt(int id, int location) {
        db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_KAYITLAR.get(location), new String[] { KEY_ID, KEY_TITLE, KEY_DESCRIPTION, KEY_IS_MI, KEY_INDENT, KEY_DATE, KEY_TIME, KEY_PENDING_INTENT, KEY_PENDING_ID }, KEY_ID + "=?",
                                                 new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Kayýt contact = new Kayýt(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
        // return contact
        cursor.close();//sercan
        db.close();
        return contact;
    }
     
    // Getting All Contacts
    public List<Kayýt> getAllKayýtlar(int location) {
    	
        SQLiteDatabase db; 

        List<Kayýt> kayýtList = new ArrayList<Kayýt>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_KAYITLAR.get(location);
 
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Kayýt kayýt = new Kayýt();
                kayýt.setID(Integer.parseInt(cursor.getString(0)));
                kayýt.setTitle(cursor.getString(1));
                kayýt.setDescription(cursor.getString(2));
                kayýt.setIsaretliMi(cursor.getString(3));
                kayýt.setIndent(cursor.getString(4));
                kayýt.setDate(cursor.getString(5));
                kayýt.setTime(cursor.getString(6));
                kayýt.setPendingIntend(cursor.getString(7));
                kayýt.setPendingId(cursor.getString(8));

                // Adding contact to list
                kayýtList.add(kayýt);
            } while (cursor.moveToNext());
        }

        cursor.close();//sercan
        db.close();
        // return contact list
        return kayýtList;
    }
 
    // Updating single contact
    public void updateContact(Kayýt kayýt, int location) {
        db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, kayýt.getTitle());
        values.put(KEY_DESCRIPTION, kayýt.getDescription());
        values.put(KEY_IS_MI, kayýt.getIsaretliMi());
        values.put(KEY_INDENT, kayýt.getIndent()); // Contact Name
        values.put(KEY_DATE, kayýt.getDate()); // Contact Name
        values.put(KEY_TIME, kayýt.getTime()); // Contact Name
        values.put(KEY_PENDING_INTENT, kayýt.getPendingIntent()); // Contact Name
        values.put(KEY_PENDING_ID, kayýt.getPendingId()); // Contact Name
        
        db.update(TABLE_KAYITLAR.get(location), values, KEY_ID + " = ?",
                        new String[] { String.valueOf(kayýt.getID()) });
        
        db.close();
		//return db.update(TABLE_KAYITLAR.get(location), values, KEY_ID + " = ?",
        //                new String[] { String.valueOf(kayýt.getID()) });
        
        // updating row
        //return db.update(TABLE_KAYITLAR.get(location), values, KEY_ID + " = ?",
        //        new String[] { String.valueOf(kayýt.getID()) });

    }
 
    // Deleting single contact
    public void deleteContact(Kayýt kayýt, int location) {
        db = this.getWritableDatabase();
        db.delete(TABLE_KAYITLAR.get(location), KEY_ID + " = ?",
                new String[] { String.valueOf(kayýt.getID()) });
        db.close();
    }
    
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_KAYITLAR;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }
    /**
     * Remove all users and groups from database.
     */
    public void removeAll(int location)
    {
        // db.delete(String tableName, String whereClause, String[] whereArgs);
        // If whereClause is null, it will delete all rows.
    	db = this.getWritableDatabase(); // helper is object extends SQLiteOpenHelper
        db.delete(DatabaseHandler.TABLE_KAYITLAR.get(location), null, null);
        db.close();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    // Getting All Contacts
    public List<Kayýt> getBeforeTo(int to) {
        List<Kayýt> kayýtList = new ArrayList<Kayýt>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_KAYITLAR;
 
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Kayýt kayýt = new Kayýt();
                kayýt.setID(Integer.parseInt(cursor.getString(0)));
                kayýt.setTitle(cursor.getString(1));
                kayýt.setDescription(cursor.getString(2));
                kayýt.setIsaretliMi(cursor.getString(3));
                kayýt.setIndent(cursor.getString(4));

                // Adding contact to list
                kayýtList.add(kayýt);
            } 
            while (cursor.moveToNext() & cursor.getPosition() != to); 

        }
 
        // return contact list
        return kayýtList;
    }
    public List<Kayýt> getAfterTo(int to) {
        List<Kayýt> kayýtList = new ArrayList<Kayýt>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_KAYITLAR;
 
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToPosition(to)) {
            do {
                Kayýt kayýt = new Kayýt();
                kayýt.setID(Integer.parseInt(cursor.getString(0)));
                kayýt.setTitle(cursor.getString(1));
                kayýt.setDescription(cursor.getString(2));
                kayýt.setIsaretliMi(cursor.getString(3));
                kayýt.setIndent(cursor.getString(4));

                // Adding contact to list
                kayýtList.add(kayýt);
            } 
            while (cursor.moveToNext()); 

        }
 
        // return contact list
        return kayýtList;
    }   
    
    
    public void dragdrop(int to, Kayýt item, int location) {
    	List<Kayýt> beforeto = new ArrayList<Kayýt>();
    	List<Kayýt> afterto = new ArrayList<Kayýt>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_KAYITLAR.get(location);

        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
         
        if(to != 0) {
        	if (cursor.moveToFirst()) {
        		do {
        			Kayýt kayýt = new Kayýt();
        			kayýt.setID(Integer.parseInt(cursor.getString(0)));
        			kayýt.setTitle(cursor.getString(1));
        			kayýt.setDescription(cursor.getString(2));
        			kayýt.setIsaretliMi(cursor.getString(3));
                    kayýt.setIndent(cursor.getString(4));
                    kayýt.setDate(cursor.getString(5));
                    kayýt.setTime(cursor.getString(6));
                    kayýt.setPendingIntend(cursor.getString(7));
                    kayýt.setPendingId(cursor.getString(8));

        			// Adding contact to list
        			beforeto.add(kayýt);
        		} 
        		while (cursor.moveToNext() & cursor.getPosition() != to);     
        	}
        }
        
        if (cursor.moveToPosition(to)) {
            do {
                Kayýt kayýt = new Kayýt();
                kayýt.setID(Integer.parseInt(cursor.getString(0)));
                kayýt.setTitle(cursor.getString(1));
                kayýt.setDescription(cursor.getString(2));
                kayýt.setIsaretliMi(cursor.getString(3));
                kayýt.setIndent(cursor.getString(4));
                kayýt.setDate(cursor.getString(5));
                kayýt.setTime(cursor.getString(6));
                kayýt.setPendingIntend(cursor.getString(7));
                kayýt.setPendingId(cursor.getString(8));

                // Adding contact to list
                afterto.add(kayýt);
            } 
            while (cursor.moveToNext()); 
        }
    
        db.delete(DatabaseHandler.TABLE_KAYITLAR.get(location), null, null);

        if(to != 0) {
    		for (Kayýt cn : beforeto) {
    		
    			 ContentValues values = new ContentValues();
    			 values.put(KEY_TITLE, cn.getTitle()); // Contact Name
    			 values.put(KEY_DESCRIPTION, cn.getDescription()); // Contact Phone
    			 values.put(KEY_IS_MI, cn.getIsaretliMi()); // Contact Name
    			 values.put(KEY_INDENT, cn.getIndent()); // Contact Name
    			 values.put(KEY_DATE, cn.getDate()); // Contact Name
    			 values.put(KEY_TIME, cn.getTime()); // Contact Name
    			 values.put(KEY_PENDING_INTENT, cn.getPendingIntent()); // Contact Name
    			 values.put(KEY_PENDING_ID, cn.getPendingId()); // Contact Name

    			 db.insert(TABLE_KAYITLAR.get(location), null, values);

    		}
    	}
        
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getTitle()); // Contact Name
        values.put(KEY_DESCRIPTION, item.getDescription()); // Contact Phone
        values.put(KEY_IS_MI, item.getIsaretliMi()); // Contact Name
		values.put(KEY_INDENT, item.getIndent()); // Contact Name
		values.put(KEY_DATE, item.getDate()); // Contact Name
		values.put(KEY_TIME, item.getTime()); // Contact Name
		values.put(KEY_PENDING_INTENT, item.getPendingIntent()); // Contact Name
		values.put(KEY_PENDING_ID, item.getPendingId()); // Contact Name

        // Inserting Row
        db.insert(TABLE_KAYITLAR.get(location), null, values);

    	for (Kayýt cn : afterto) {
    		
    		ContentValues values1 = new ContentValues();
			 values1.put(KEY_TITLE, cn.getTitle()); // Contact Name
			 values1.put(KEY_DESCRIPTION, cn.getDescription()); // Contact Phone
			 values1.put(KEY_IS_MI, cn.getIsaretliMi()); // Contact Name
			 values1.put(KEY_INDENT, cn.getIndent()); // Contact Name
			 values1.put(KEY_DATE, cn.getDate()); // Contact Name
			 values1.put(KEY_TIME, cn.getTime()); // Contact Name
			 values1.put(KEY_PENDING_INTENT, cn.getPendingIntent()); // Contact Name
			 values1.put(KEY_PENDING_ID, cn.getPendingId()); // Contact Name
			 
			 db.insert(TABLE_KAYITLAR.get(location), null, values1);

        }
    	
        cursor.close();
        db.close();

    }
//---------------------------------------------------------------------------------------------------    
    
    //***DatabaseList
    void addDatabaseListItem(DatabaseList databaselist) {
        db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, databaselist.getTitle()); // Contact Name
        values.put(KEY_NAME, databaselist.getName()); // Contact Name

        // Inserting Row
        db.insert(DATABASE_TABLE_LIST, null, values);
        db.close(); // Closing database connection
    }
      
    // Getting All Contacts
    public List<DatabaseList> getAllDatabaseListItems() {
    	
        SQLiteDatabase db; 

        List<DatabaseList> databaseList = new ArrayList<DatabaseList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE_LIST;
 
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DatabaseList database = new DatabaseList();
                database.setID(Integer.parseInt(cursor.getString(0)));
                database.setTitle(cursor.getString(1));
                database.setName(cursor.getString(2));

                // Adding contact to list
                databaseList.add(database);
            } while (cursor.moveToNext());
        }

        cursor.close();//sercan
        db.close();
        // return contact list
        return databaseList;
    }
 
    // Updating single contact
    public void updateDatabaseListItem(DatabaseList database) {
        db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, database.getTitle());
        values.put(KEY_NAME, database.getName());
        
        db.update(DATABASE_TABLE_LIST, values, KEY_ID + " = ?",
                new String[] { String.valueOf(database.getID()) });
        
        db.close();        
    }
 
    // Deleting single contact
    public void deleteDatabaseListItem(DatabaseList database) {
        db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE_LIST, KEY_ID + " = ?",
                new String[] { String.valueOf(database.getID()) });
        db.close();
    }
    
    // Getting contacts Count
    public int getDatabaseListItemCount() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE_LIST;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }   
    
    public DatabaseList getDatabaseListItem(int id) {
        db = this.getReadableDatabase();
 
        Cursor cursor = db.query(DATABASE_TABLE_LIST, new String[] { KEY_ID, KEY_TITLE, KEY_NAME }, KEY_ID + "=?",
                                                 new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        DatabaseList contact = new DatabaseList(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        cursor.close();//sercan 
        db.close();
        return contact;
    }
//-------------------------------------------------------------------------------------------------------------------
   
    
  
}
 