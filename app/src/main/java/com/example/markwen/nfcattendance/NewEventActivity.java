package com.example.markwen.nfcattendance;

import android.content.Intent;
import android.icu.util.Output;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class NewEventActivity extends AppCompatActivity {
    Long start_time = 0L;
    Long end_time = 0L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        setTitle("Add New Event");
        Button createButton = (Button) findViewById(R.id.buttonCreate);
        Button pickButton = (Button) findViewById(R.id.buttonTime);
        final String strSdPath = Environment.getExternalStorageDirectory().getAbsolutePath(); //find external storage path

        final View dialogView = View.inflate(this, R.layout.datetimepicker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        dialogView.findViewById(R.id.start_time_set).setOnClickListener(new View.OnClickListener() {
            //used code from http://stackoverflow.com/questions/2055509/datetime-picker-in-android-application
            //for creating datetime picker display
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                start_time = calendar.getTimeInMillis();
                Snackbar.make(findViewById(R.id.activity_event), "Start time saved", Snackbar.LENGTH_LONG).show();
            }
        });
        dialogView.findViewById(R.id.end_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                end_time = calendar.getTimeInMillis();
                Snackbar.make(findViewById(R.id.activity_event), "End time saved", Snackbar.LENGTH_LONG).show();
            }
        });
        dialogView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText titleET = (EditText) findViewById(R.id.titleEditText);
                String title = "";
                title = titleET.getText().toString();
                if (title.equals("")) {
                    Snackbar.make(findViewById(R.id.activity_event), "Please enter a title", Snackbar.LENGTH_LONG).show();
                } else if (start_time == 0L) {
                    Snackbar.make(findViewById(R.id.activity_event), "Please choose a start time first", Snackbar.LENGTH_LONG).show();
                } else if (end_time == 0L) {
                    Snackbar.make(findViewById(R.id.activity_event), "Please choose an end time first", Snackbar.LENGTH_LONG).show();
                } else {
                    final File directory = new File(strSdPath + "/NFCAttendance/");
                    directory.mkdir();
                    Snackbar.make(findViewById(R.id.activity_event), title + " directory created at " + strSdPath + "/NFCAttendance", Snackbar.LENGTH_LONG).show();
                    try {
                        File txtFile = new File(strSdPath + "/NFCAttendance/" + title + ".txt");
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(new FileOutputStream(txtFile));
                        JSONArray students = new JSONArray();
                        try {
                            JSONObject obj1 = new JSONObject();
                            obj1.put("students", students);
                            obj1.put("end_time", end_time);
                            obj1.put("start_time", start_time);
                            obj1.put("title", title);

                            String str = obj1.toString();
                            myOutWriter.append(str);
                            myOutWriter.close();
                            Snackbar.make(findViewById(R.id.activity_event), "Data file written", Snackbar.LENGTH_LONG).show();
                            moveToP(v);
                        } catch (Exception e) {
                            Snackbar.make(findViewById(R.id.activity_event), e.getMessage(), Snackbar.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Snackbar.make(findViewById(R.id.activity_event), e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });
        pickButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog.setView(dialogView); //show datetimepicker.xml
                alertDialog.show();
            }
        });
    }
    public void moveToP(View v) {
        Intent mIntent = new Intent(this, ProfessorActivity.class);
        startActivity(mIntent);
    }
}
