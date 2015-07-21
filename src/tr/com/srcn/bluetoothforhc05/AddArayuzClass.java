package tr.com.srcn.bluetoothforhc05;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class AddArayuzClass extends AppCompatActivity {
	EditText title_et, description_et, etday, ettime;
	AutoCompleteTextView db_list;
	Button xdate, xtime;
	final DatabaseHandler db = new DatabaseHandler(this);
	List<Kayýt> kayýtlar;
	AlarmManager alarmManager; 
	String selectdate; 
	String parçadate[]; 
	String sDay; 
	String sMonth; 
	String sYear;
	String selecttime;
	String parçatime[];
	String sHour;
	String sMinute;
	PendingIntent pendingIntent;
	ArrayList<PendingIntent> intentArray; 	
	int i = 0, xxx;
	int user_selected;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_arayuz);
		setTitle("Creat Task");
				
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();       
        Bundle bundle = intent.getExtras();
		xxx = (Integer) bundle.getSerializable("selected_database");	
		user_selected = xxx;
        
        title_et = (EditText) findViewById(R.id.editText1);
        description_et = (EditText) findViewById(R.id.editText2);
        etday = (EditText) findViewById(R.id.editText3);
		ettime = (EditText) findViewById(R.id.editText4);
		xdate = (Button) findViewById(R.id.button1);
		xtime = (Button) findViewById(R.id.button2);
		db_list = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);		
		//--------------------------------------------------------------------
		ArrayList<String> itemList = new ArrayList<String>();
		List<DatabaseList> databases = db.getAllDatabaseListItems();
		for (DatabaseList cn : databases) {
			itemList.add(cn.getName());
		}
		UsersAdapter aAdpt = new UsersAdapter(getApplicationContext(), itemList);
		db_list.setText(itemList.get(xxx));
		db_list.setAdapter(aAdpt);
        db_list.setTextColor(Color.BLACK);

		db_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db_list.showDropDown();
			}
		});
		db_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				user_selected = position;
			}
		});
		//--------------------------------------------------------------------
		if(etday.getText().toString().length() == 0) {
			ettime.setEnabled(false);
			xtime.setEnabled(false);
		}	
		else {
			ettime.setEnabled(true);
			xtime.setEnabled(true);
		}
		
		etday.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "text", 1000).show();
				Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker;
                
                mDatePicker = new DatePickerDialog(AddArayuzClass.this, new OnDateSetListener() {
                	
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        
                        if(selectedday<10 && selectedmonth<10){
                        	etday.setText("0" + selectedday + "/" + "0" + selectedmonth + "/" + selectedyear);
	            		}
	            		else if(selectedday<10 && !(selectedmonth<10)){
                        	etday.setText("0" + selectedday + "/"  + selectedmonth + "/" + selectedyear);
	            		}
	            		else if(!(selectedday<10) && selectedmonth<10){
                        	etday.setText(selectedday + "/" + "0" + selectedmonth + "/" + selectedyear);
	            		}
	            		else {
                        	etday.setText(selectedday + "/" + selectedmonth + "/" + selectedyear);
	            		}
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
			}			
		});
		
		ettime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
	            int mHour = c.get(Calendar.HOUR_OF_DAY);
	            int mMinute = c.get(Calendar.MINUTE);
	            
	            
	            TimePickerDialog tpd = new TimePickerDialog(AddArayuzClass.this, new OnTimeSetListener() {	 
	                        
	            	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            		// Display Selected time in textbox
	            		if(minute<10 && hourOfDay<10){
	            			ettime.setText("0" + hourOfDay + ":" + "0" + minute);
	            		}
	            		else if(minute<10 && !(hourOfDay<10)){
	            			ettime.setText(hourOfDay + ":" + "0" + minute);
	            		}
	            		else if(!(minute<10) && hourOfDay<10){
	            			ettime.setText("0" + hourOfDay + ":" + minute);
	            		}
	            		else {
		            		ettime.setText(hourOfDay + ":" + minute);
	            		}
	            	}
	            }, mHour, mMinute, true);
	            
	            tpd.show();	            
			} 
		});
		
		xdate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				etday.setText("");
				ettime.setEnabled(false);
				xtime.setEnabled(false);
			}
		});
		
		xtime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ettime.setText("");
			}
		});
		
		etday.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				ettime.setEnabled(true);
				xtime.setEnabled(true);
			}
		});
	}
		
	
	public boolean onCreateOptionsMenu(Menu menu) {
	    // TODO Auto-generated method stub
	    getMenuInflater().inflate(R.menu.menuadd, menu);
	    return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onOptionsItemSelected(item);
		
		Intent intent = new Intent(this, MainActivity.class);
		Bundle bundle = new Bundle();

		switch(item.getItemId()){
		case R.id.cancel:
			bundle.putSerializable("user_selected", xxx);
			intent.putExtras(bundle);
            startActivity(intent);
            finish();
			break;
		case R.id.done:
						
			String invaliddate = "no";
			
			if(!(etday.getText().toString().equals(""))){
				
				Calendar mcurrentDate = Calendar.getInstance();
	            int mYear = mcurrentDate.get(Calendar.YEAR);
	            int mMonth = mcurrentDate.get(Calendar.MONTH);
	            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
	            mMonth = mMonth + 1;
				selectdate = etday.getText().toString();
				parçadate = selectdate.split("/");
				sDay = parçadate[0];
				sMonth = parçadate[1];
				sYear = parçadate[2];
				
				if(!(ettime.getText().toString().equals(""))){
				
					int mHour = mcurrentDate.get(Calendar.HOUR_OF_DAY);
					int mMinute = mcurrentDate.get(Calendar.MINUTE);
					selecttime = ettime.getText().toString();
					parçatime = selecttime.split(":");
					sHour = parçatime[0];
					sMinute = parçatime[1];
	            
					if(mYear > Integer.parseInt(sYear))	{
	            	
						Toast.makeText(this, "Invalid Year...", 1000).show();
						invaliddate = "yes";
						break;
					}
					if(mYear == Integer.parseInt(sYear))	{
						if(mMonth > Integer.parseInt(sMonth))	{
		            	
							Toast.makeText(this, "Invalid Month...", 1000).show();
							invaliddate = "yes";
							break;
						}	
					}
					if(mYear <= Integer.parseInt(sYear))	{
						if(mMonth == Integer.parseInt(sMonth))	{
							if(mDay > Integer.parseInt(sDay))	{
	    	            	
								Toast.makeText(this, "Invalid Day...", 1000).show();
								invaliddate = "yes";
								break;
							}
						}	
					}
					if(mYear == Integer.parseInt(sYear) & mMonth == Integer.parseInt(sMonth) & mDay == Integer.parseInt(sDay)) {
	            	
						if(mHour > Integer.parseInt(sHour)) {
	            		
							Toast.makeText(this, "Invalid Time...", 1000).show();
							invaliddate = "yes";
							break;
						}
						if(mHour == Integer.parseInt(sHour)) {
							if(mMinute > Integer.parseInt(sMinute)){
	            			
								Toast.makeText(this, "Invalid Time...", 1000).show();
								invaliddate = "yes";
								break;
								
							}
						}
					}
				}
			}
	
			if(invaliddate.equals("no") & !(etday.getText().toString().equals("")) & !(ettime.getText().toString().equals(""))){
				

				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
				String length = sharedPrefs.getString("snooze_length", "-1"); 

				List<DatabaseList> databases = db.getAllDatabaseListItems();
				DatabaseList database = databases.get(user_selected);

				Random generator = new Random();
				i = generator.nextInt();
				//Alarm
				alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				Intent alarmintent = new Intent(this, AlarmReceiver.class);
				alarmintent.setAction("com.manish.alarm.ACTION");
				alarmintent.putExtra("TASK_ID", i);
				alarmintent.putExtra("selected_database", database.getTitle());
				PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, alarmintent, 0);
				Calendar setalarm = Calendar.getInstance();
				
				setalarm.set(Calendar.MONTH, Integer.parseInt(sMonth)-1);
				setalarm.set(Calendar.YEAR, Integer.parseInt(sYear));
				setalarm.set(Calendar.DAY_OF_MONTH, Integer.parseInt(sDay));
				setalarm.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sHour));
				setalarm.set(Calendar.MINUTE, Integer.parseInt(sMinute));
				setalarm.set(Calendar.SECOND, 0);

			    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, setalarm.getTimeInMillis(), Integer.parseInt(length), pendingIntent);
			}
            	
            	
            	
			if(title_et.getText().toString().trim().equals("")){
				Toast.makeText(this, "Empty Title...", 1000).show();
				break;
			}
			if(!(etday.getText().toString().equals(""))){
				if(ettime.getText().toString().equals("")) {
					Toast.makeText(this, "Empty Time...", 1000).show();
					break;
				}
			}	

			db.addKayýt(new Kayýt(title_et.getText().toString(), description_et.getText().toString(), "false", "N", 
					              etday.getText().toString(), ettime.getText().toString(), "null", Integer.toString(i)), user_selected);
	        
			/*
	        for(int i=0; i<100; i++){
	        	
	        	db.addKayýt(new Kayýt(Integer.toString(i), "", "false", "N", 
			              etday.getText().toString(), ettime.getText().toString(), "null", Integer.toString(0)), user_selected);
	        }
	        */
			db.close();
			bundle.putSerializable("user_selected", user_selected);
			intent.putExtras(bundle);
            startActivity(intent);
            finish();
			break;
		case android.R.id.home:
			bundle.putSerializable("user_selected", xxx);
			intent.putExtras(bundle);
            startActivity(intent);
            finish();
			break;
		}
		return true;	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        //Do what you want here
			Bundle bundle = new Bundle();
			bundle.putSerializable("user_selected", xxx);
			
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtras(bundle);
            startActivity(intent);
            finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
}







class UsersAdapter extends ArrayAdapter<String> {
	ArrayList<String> post_item;
	LayoutInflater mInflater;
	
	public UsersAdapter(Context context, ArrayList<String> users) {
		super(context, R.id.srw2_text, users);
		this.post_item = users;
    	mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		// Check if an existing view is being reused, otherwise inflate the view

			convertView = mInflater.inflate(R.layout.single_row_2, parent, false);
			TextView tvName = (TextView) convertView.findViewById(R.id.srw2_text);
			tvName.setText(post_item.get(position));
	        tvName.setTextColor(Color.BLACK);

		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return post_item.size();
	}

	@Override
	public String getItem(int position) {
		// TODO Auto-generated method stub
		return post_item.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}

