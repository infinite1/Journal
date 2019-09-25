package com.example.journal3;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class CalendarActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        myDate = (TextView) findViewById(R.id.myDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
//                (CalendarView view, int year, int month, int dayOfMonth)
                String date = (i1 + 1) + "/" +i2 + "/" + i;
                myDate.setText(date);
                Context context = getApplicationContext();
                System.out.println("date is "+date);

                if(date.compareTo("9/22/2019")==0) {
                    Toast.makeText(context,"Video Loading",Toast.LENGTH_SHORT).show();

                    openVideoPlay();

                }
                else {
                    Toast.makeText(context,"No Video Record for That Day",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openVideoPlay() {
        Intent intent = new Intent(this,VideoPlay.class);
        startActivity(intent);

    }


}
