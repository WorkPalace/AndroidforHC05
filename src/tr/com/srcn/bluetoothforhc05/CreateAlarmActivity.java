package tr.com.srcn.bluetoothforhc05;

import java.util.Calendar;
import java.util.Random;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CreateAlarmActivity extends AppCompatActivity implements OnClickListener, OnItemSelectedListener {

	Button setDate,setTime, createAlarm, setSolenoid;
	TextView time=null,date=null;
	CheckBox motorDirection, startMotor;;
	String[] content = { "Box1", "Box2", "Box3","Box4","Box5"};
	Spinner box_spinner;
	
	String box_number;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_alarm);
		
		setDate = (Button) findViewById(R.id.set_date);
		setTime = (Button) findViewById(R.id.set_time);
		createAlarm = (Button) findViewById(R.id.create_alarm);
		setSolenoid = (Button) findViewById(R.id.setsolenoid);
		
		box_spinner = (Spinner) findViewById(R.id.spinner1);
		
		motorDirection = (CheckBox) findViewById(R.id.motordirection);
		startMotor = (CheckBox) findViewById(R.id.startmotor);
		
		time = (TextView) findViewById(R.id.time);
		date = (TextView) findViewById(R.id.date);
		
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				  android.R.layout.simple_spinner_item, content);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		box_spinner.setAdapter(adapter);
		
		box_spinner.setOnItemSelectedListener(this);
		
		setDate.setOnClickListener(this);
		setTime.setOnClickListener(this);
		createAlarm.setOnClickListener(this);
		motorDirection.setOnClickListener(this);
		startMotor.setOnClickListener(this);
		setSolenoid.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.setsolenoid) {
			
			String set = "SET";
			set = set + '\n';
			MainActivity.connectedThread.write(set.getBytes());	
		}
		if(v.getId() == R.id.motordirection) {
			
			if(motorDirection.isChecked()) {
				
				String cw = "CW";
				cw = cw + '\n';
				MainActivity.connectedThread.write(cw.getBytes());	
			}
			else {
				
				String ccw = "CCW";
				ccw = ccw + '\n';
				MainActivity.connectedThread.write(ccw.getBytes());
			}
		}
		if(v.getId() == R.id.startmotor) {
			
			if(startMotor.isChecked()) {
				
				String start = "START";
				start = start + '\n';
				MainActivity.connectedThread.write(start.getBytes());	
			}
			else {
				
				String stop = "STOP";
				stop = stop + '\n';
				MainActivity.connectedThread.write(stop.getBytes());
			}
		}
		
		if(v.getId() == R.id.set_date) {
			// TODO Auto-generated method stub
			// Toast.makeText(getApplicationContext(), "text", 1000).show();
			Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker;
            
            mDatePicker = new DatePickerDialog(CreateAlarmActivity.this, new OnDateSetListener() {
            	
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
                    selectedmonth = selectedmonth + 1;
                    
                    if(selectedday<10 && selectedmonth<10){
                    	date.setText("0" + selectedday + ":" + "0" + selectedmonth + ":" + selectedyear);
            		}
            		else if(selectedday<10 && !(selectedmonth<10)){
                    	date.setText("0" + selectedday + ":"  + selectedmonth + ":" + selectedyear);
            		}
            		else if(!(selectedday<10) && selectedmonth<10){
                    	date.setText(selectedday + ":" + "0" + selectedmonth + ":" + selectedyear);
            		}
            		else {
                    	date.setText(selectedday + ":" + selectedmonth + ":" + selectedyear);
            		}
                }
            }, mYear, mMonth, mDay);
            mDatePicker.setTitle("Select Date");
            mDatePicker.show();
			
		}
		if(v.getId() == R.id.set_time) {
			
			Calendar c = Calendar.getInstance();
            int mHour = c.get(Calendar.HOUR_OF_DAY);
            int mMinute = c.get(Calendar.MINUTE);
            
            
            TimePickerDialog tpd = new TimePickerDialog(CreateAlarmActivity.this, new OnTimeSetListener() {	 
                        
            	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            		// Display Selected time in textbox
            		if(minute<10 && hourOfDay<10){
            			time.setText("0" + hourOfDay + ":" + "0" + minute);
            		}
            		else if(minute<10 && !(hourOfDay<10)){
            			time.setText(hourOfDay + ":" + "0" + minute);
            		}
            		else if(!(minute<10) && hourOfDay<10){
            			time.setText("0" + hourOfDay + ":" + minute);
            		}
            		else {
	            		time.setText(hourOfDay + ":" + minute);
            		}
            	}
            }, mHour, mMinute, true);
            
            tpd.show();	            
		}
		
		if(v.getId() == R.id.create_alarm) {
			
			if(time.getText().toString() != null && date.getText().toString() != null){
				
				String[] divideTime;
				divideTime = time.getText().toString().split(":");
				int sHour = Integer.valueOf(divideTime[0]);
				int sMinute = Integer.valueOf(divideTime[1]);
				
				String[] divideDate;
				divideDate = date.getText().toString().split(":");
				int sDay = Integer.valueOf(divideDate[0]);
				int sMonth = Integer.valueOf(divideDate[1]);
				int sYear = Integer.valueOf(divideDate[2]);
				
				Random generator = new Random();
				int i = generator.nextInt();
				//Alarm
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				Intent alarmintent = new Intent(this, AlarmReceiver.class);
				alarmintent.setAction("com.manish.alarm.ACTION");
				alarmintent.putExtra("TASK_ID", i);
				alarmintent.putExtra("date",  date.getText().toString());
				alarmintent.putExtra("time",  time.getText().toString());

				PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, alarmintent, 0);
				
				Calendar setalarm = Calendar.getInstance();
				
				setalarm.set(Calendar.MONTH, sMonth-1);
				setalarm.set(Calendar.YEAR, sYear);
				setalarm.set(Calendar.DAY_OF_MONTH, sDay);
				setalarm.set(Calendar.HOUR_OF_DAY, sHour);
				setalarm.set(Calendar.MINUTE, sMinute);
				setalarm.set(Calendar.SECOND, 0);
				int alarminmill = (int) (setalarm.getTimeInMillis()/1000);//get alarm in milisecond
				
			    alarmManager.set(AlarmManager.RTC_WAKEUP, setalarm.getTimeInMillis(), pendingIntent);
			    
			    int dayOfWeek = setalarm.get(Calendar.DAY_OF_WEEK)-1;
			    
			    if(dayOfWeek == 0) {
					
					dayOfWeek=7;
				}
			    String send = '*' + box_number + ":" + String.valueOf(alarminmill) + '\n'; 
			    
			    Toast.makeText(this, send, Toast.LENGTH_SHORT).show();

			    
			   // Bundle bundle = new Bundle();
				//Intent intent = new Intent(this, MainActivity.class);
				//bundle.putSerializable("date&time", send);
				//intent.putExtras(bundle);
				//startActivity(intent);	
				//connectedThread.run();
				MainActivity.connectedThread.write(send.getBytes());	
				
				//finish();

			}
			
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		String boxnumber = parent.getItemAtPosition(position).toString();
        // Toast mesajýný yazdýrdýk.
		
		if(boxnumber.equalsIgnoreCase("Box1"))
			box_number = "1";
		if(boxnumber.equalsIgnoreCase("Box2"))
			box_number = "2";
		if(boxnumber.equalsIgnoreCase("Box3"))
			box_number = "3";
		if(boxnumber.equalsIgnoreCase("Box4"))
			box_number = "4";
		if(boxnumber.equalsIgnoreCase("Box5"))
			box_number = "5";
		
        Toast.makeText(getApplicationContext(), String.valueOf(box_number), Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
}
