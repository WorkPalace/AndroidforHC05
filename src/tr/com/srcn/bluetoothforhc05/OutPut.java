package tr.com.srcn.bluetoothforhc05;

import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OutPut extends AppCompatActivity implements OnClickListener {
	TextView tv_title,tv_description,tv_timedate;
	String title,description,time,date,message;
	int id,return_database;
	Button dismiss,snooze;
	int selected_database_position;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_output);
		setTitle("Due task");

		ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

		tv_title = (TextView) findViewById(R.id.textView1);
		tv_description = (TextView) findViewById(R.id.textView2);
		tv_timedate = (TextView) findViewById(R.id.textView3);
		dismiss = (Button) findViewById(R.id.dismissButton);
		snooze = (Button) findViewById(R.id.snoozeButton);

		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		description = intent.getStringExtra("description");
		date = intent.getStringExtra("date");
		time = intent.getStringExtra("time");
		id =  intent.getIntExtra("Task_Id", id);
		return_database =  intent.getIntExtra("return_database", return_database);//ss
		selected_database_position =  intent.getIntExtra("selected_database_position", selected_database_position);//ss
		
		message = "Due date: " + date + ", " + time;

		tv_title.setText(title);
		tv_description.setText(description);
		tv_timedate.setText(message);
		
		dismiss.setOnClickListener(this);
		snooze.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent mainintent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();///ss

		switch(v.getId()){
		case R.id.snoozeButton:
			bundle.putSerializable("user_selected", return_database);//ss
			mainintent.putExtras(bundle);
			
		    mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainintent);
	        finish();
			break;
			
		case R.id.dismissButton:
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			Intent alarmintent = new Intent(this, AlarmReceiver.class);
			alarmintent.setAction("com.manish.alarm.ACTION");
			PendingIntent pi = PendingIntent.getBroadcast(this, id, alarmintent, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.cancel(pi);
			
			bundle.putSerializable("user_selected", return_database);//ss
			mainintent.putExtras(bundle);
			
		    mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mainintent);
	        finish();
			break;	
		}
	}	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        //Do what you want here
			Intent intent = new Intent(this, MainActivity.class);
			
			Bundle bundle = new Bundle();///ss
			bundle.putSerializable("user_selected", return_database);
			intent.putExtras(bundle);

		    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}


}






