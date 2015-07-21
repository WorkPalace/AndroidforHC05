package tr.com.srcn.bluetoothforhc05;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class AlarmReceiver extends BroadcastReceiver {
	private final String SOMEACTION = "com.manish.alarm.ACTION";
	List<Kayýt> editkayýtlar;
	Kayýt kayýt;
	int position = 0, xxx, db_position = 0;	
	String et_title,description,date,time,messagex; 	
	DatabaseHandler db;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		db = new DatabaseHandler(context);
		String action = intent.getAction();
		if (SOMEACTION.equals(action)) {

			Bundle bundle = intent.getExtras();
			int id = bundle.getInt("TASK_ID");
			String database = bundle.getString("selected_database");
			
			DatabaseHandler db = new DatabaseHandler(context);
			List<DatabaseList> databases = db.getAllDatabaseListItems();

			for (DatabaseList cn : databases) {
				if(cn.getTitle().equals(database)){	
					break;
				}
				db_position++;
			}		
			xxx = db_position;
	        editkayýtlar = db.getAllKayýtlar(xxx);

	        
	        for (Kayýt cn : editkayýtlar) {
	    		if(cn.getPendingId().equals(Integer.toString(id))){
	    			break;
	    		}
	    		else{
	    			position++;
	    		}      	
	        }
	        
	        kayýt = editkayýtlar.get(position);	        
	        et_title =kayýt.getTitle();
			description = kayýt.getDescription();
		    date = kayýt.getDate();
		    time = kayýt.getTime();
			generateNotification(context,et_title, id, et_title, description, date, time);
			
			
		}
	}
	
	@SuppressWarnings("deprecation")
	private void generateNotification(Context context, String message, int id, String ntitle, String ndescription, String ndate, String ntime) {
		  messagex = "Task due: " + message;
		  int icon = R.drawable.ic_launcher;
		  long when = System.currentTimeMillis();
		  NotificationManager notificationManager = (NotificationManager) context
		    .getSystemService(Context.NOTIFICATION_SERVICE);
		  Notification notification = new Notification(icon, messagex, when);
		  String title = messagex;
		  String subTitle = "Touch to see due task.";
		  Intent notificationIntent = new Intent(context, OutPut.class);
	      
		  notificationIntent.putExtra("title", messagex);
		  notificationIntent.putExtra("description", ndescription);
		  notificationIntent.putExtra("date", ndate);
		  notificationIntent.putExtra("time", ntime);
		  notificationIntent.putExtra("Task_Id", id);
		  notificationIntent.putExtra("return_database", xxx);
		  notificationIntent.putExtra("selected_database_position", position);

		  List<Kayýt> mKayýtListesi = db.getAllKayýtlar(xxx);
		  Kayýt cn = mKayýtListesi.get(position);
		  cn.setIsaretliMi("true");
		  db.updateContact(cn, xxx);

		  SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		  String strRingtonePreference = sharedPrefs.getString("audio", "DEFAULT_SOUND"); 
		  Boolean defValue = sharedPrefs.getBoolean("vibrate", true);
		  
		  if(defValue == true) {
			  
			  notification.defaults |= Notification.DEFAULT_VIBRATE;
		  }
		  
		  notification.sound = Uri.parse(strRingtonePreference);

		  PendingIntent intent = PendingIntent.getActivity(context, id, notificationIntent, 0);
		  //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		  //  | Intent.FLAG_ACTIVITY_SINGLE_TOP);		 
		  notification.setLatestEventInfo(context, title, subTitle, intent);
		  //To play the default sound with your notification:
		  //notification.defaults |= Notification.DEFAULT_SOUND;
		  notification.flags |= Notification.FLAG_AUTO_CANCEL;
		  notificationManager.notify(id, notification);
		  
    }
}