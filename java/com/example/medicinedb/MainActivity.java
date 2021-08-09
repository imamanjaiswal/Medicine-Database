package com.example.medicinedb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Button btndate,btntime,addbtn, viewbtn;
    TextView text_date,text_time;
    EditText text_name;
    int cyear,cmonth,cday;
    int chour,cmin;
    String time;
    DatabaseHelper DB;
    Spinner timeofday;
//    RecyclerView rcv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timeofday = (Spinner)findViewById(R.id.timeofday);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeofday.setAdapter(adapter);
        timeofday.setOnItemSelectedListener(this);


        text_name =(EditText)findViewById(R.id.name_editText);

        btndate =(Button)findViewById(R.id.btn_date);
//        btntime =(Button)findViewById(R.id.btn_time);

        text_date =(TextView)findViewById(R.id.date_textview);
        text_time =(TextView)findViewById(R.id.time_textview);

        btndate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show current date
                final Calendar calendar = Calendar.getInstance();
                cyear = calendar.get(Calendar.YEAR);
                cmonth = calendar.get(Calendar.MONTH);
                cday = calendar.get(Calendar.DAY_OF_MONTH);

                //Launch datepicker Dialog
                DatePickerDialog datePickerDialog= new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        text_date.setText(dayOfMonth+"/"+(month +1)+"/"+year);
                    }
                },cyear,cmonth,cday);
                datePickerDialog.show();
            }
        });

//        btntime.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Calendar calendar=Calendar.getInstance();
//                chour = calendar.get(Calendar.HOUR_OF_DAY);
//                cmin = calendar.get(Calendar.MINUTE);
//
//                TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                        text_time.setText(hourOfDay+":"+minute);
//                    }
//                },chour,cmin,false);
//                timePickerDialog.show();
//            }
//        });
        addbtn =findViewById(R.id.add_item);
        viewbtn = findViewById(R.id.view_item);
        DB = new DatabaseHelper(this);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = text_name.getText().toString();
                String Date = text_date.getText().toString();
                Boolean check = DB.addData(Name,time,Date);
//                Log.d("Testing Time", "onItemSelected:" + time);
                if(check)
                    Toast.makeText(MainActivity.this, "New Entry Inserted",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this,"New Entry Not Inserted", Toast.LENGTH_SHORT).show();

                Calendar cal = Calendar.getInstance();

                cal.set(Calendar.HOUR_OF_DAY, 14);
                cal.set(Calendar.MINUTE,34);
                cal.set(Calendar.SECOND,0);

//                cal.set(Calendar.HOUR_OF_DAY, chour);
//                cal.set(Calendar.MINUTE,cmin);
//                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MONTH,cmonth);
                cal.set(Calendar.YEAR,cyear);
                cal.set(Calendar.DATE,cday);

                setAlarm(cal);
            }
        });

        viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = DB.getdata();
                if(res.getCount()==0){
                    Toast.makeText(MainActivity.this, "No Entry",Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Name: "+ res.getString(0)+"\n");
                    buffer.append("Date: "+ res.getString(1)+"\n");
                    buffer.append("Time: "+ res.getString(2)+"\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Data");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        time = parent.getItemAtPosition(position).toString();
//        Log.d("Testing", "onItemSelected:" + time);
        if(time.equals("Morning")){
            chour = 8;
            cmin = 00;
        }else if(time.equals("Afternoon")){
            chour = 12;
            cmin = 30;
        }else if(time.equals("Evening")){
            chour = 17;
            cmin = 30;
        }else{
            chour = 21;
            cmin = 00;
        }
    }

    private void setAlarm(Calendar calendar) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(this, ReminderBroadcas.class);

        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        Calendar c = Calendar.getInstance();
        long currentTime = c.getTimeInMillis();
        long diffTime = calendar.getTimeInMillis() - currentTime;

        am.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + diffTime,pi);
//        am.setExact(am.RTC_WAKEUP,calendar.getTimeInMillis(),pi);

//        Log.d("Testing", "Time:"+diffTime);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "medicine";
//            String description = "database";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("notify", name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}