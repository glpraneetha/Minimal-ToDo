package com.example.praneethagangisetty.todominimal;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import mani.itachi.toaster.Toaster;

public class AddToDo extends AppCompatActivity {
    Realm realm;
    static int hour, min, datetoday, month, year, shour, smin, sdate, smonth, syear;
    EditText title;
    EditText date;
    EditText time;
    TextView at;
    Switch s;
    RealmHelper helper;
    DataItems d;
    FloatingActionButton fab;
    static Intent intent;
    static Calendar c1 = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);
        initViews();
        initFake();
        editTodo();
        try {
            if (intent.getExtras() != null && s.isChecked())
                getSelectedDateandTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
                if (s.isChecked())
                    startAlarm(c1, title.getText().toString());
            }
        });

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchSetVisibility(s);
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newfragment = new TimePickerClass();
                newfragment.show(getSupportFragmentManager(), "Time picker");
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newdatefragment = new DatePickerClass();
                newdatefragment.show(getSupportFragmentManager(), "Date picker");
            }
        });
    }

    private void getSelectedDateandTime() throws ParseException {
        Date date;
        String input = intent.getStringExtra("time");
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat dateOutputFormat = new SimpleDateFormat("hh:mm");
        date = df.parse(input);
        String output = dateOutputFormat.format(date);
        shour = Integer.parseInt(output.substring(0, 2));
        smin = Integer.parseInt(output.substring(3, 5));
        String d = intent.getStringExtra("date").substring(0, 2);
        sdate = Integer.parseInt(d);
        String m = intent.getStringExtra("date").substring(3, 5);
        smonth = Integer.parseInt(m);
        month--;
        String y = intent.getStringExtra("date").substring(6, 10);
        syear = Integer.parseInt(y);
    }

    private void editTodo() {
        intent = getIntent();
        if (intent.getExtras() != null) {
            CharSequence cs;
            if (intent.getStringExtra("date").toString().equalsIgnoreCase(""))
                cs = intent.getStringExtra("title");
            else {
                s.setChecked(true);
                switchSetVisibility(s);
                date.setText(intent.getStringExtra("date"));
                time.setText(intent.getStringExtra("time"));
                cs = intent.getStringExtra("title") + " at " + intent.getStringExtra("date") + " and " + intent.getStringExtra("time");
            }
            //Toast.makeText(AddToDo.this, cs, Toast.LENGTH_SHORT).show();
            title.setText(intent.getStringExtra("title"));
        }
    }

    private void initFake() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();
    }

    private void initViews() {
        title = findViewById(R.id.content);
        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        fab = findViewById(R.id.fab);
        s = findViewById(R.id.switch1);
    }

    private void switchSetVisibility(Switch s) {
        date = findViewById(R.id.date);
        at = findViewById(R.id.at);
        time = findViewById(R.id.time);
        if (s.isChecked()) {
            title.onEditorAction(EditorInfo.IME_ACTION_DONE); //hide keyboard
            date.setVisibility(View.VISIBLE);
            at.setVisibility(View.VISIBLE);
            time.setVisibility(View.VISIBLE);
        } else {
            date.setVisibility(View.INVISIBLE);
            at.setVisibility(View.INVISIBLE);
            time.setVisibility(View.INVISIBLE);
        }
    }

    private void sendData() {
        int c;
        String string = title.getText().toString();
        if (string.equalsIgnoreCase("")) {
            Toaster.success(AddToDo.this, "Enter Title", Toast.LENGTH_SHORT).setIcon(R.drawable.sademoji).setBackgroundColor(Color.parseColor("#303F9F")).setTextColor(Color.parseColor("#FFFFFF")).show();
            //Toast.makeText(AddToDo.this, "Enter Title", Toast.LENGTH_SHORT).show();
        } else {
            c = check();
            if (c == 0 && s.isChecked()) {
                Toaster.success(AddToDo.this, "Enter valid date and time", Toast.LENGTH_SHORT).setIcon(R.drawable.sademoji).setBackgroundColor(Color.parseColor("#303F9F")).setTextColor(Color.parseColor("#FFFFFF")).show();
            } else {
                if (intent.getExtras() != null) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            d = realm.where(DataItems.class).equalTo("title", intent.getStringExtra("title")).equalTo("date", intent.getStringExtra("date")).equalTo("time", intent.getStringExtra("time")).findFirst();
                            if (s.isChecked()) {
                                d.setTitle(title.getText().toString());
                                d.setDate(date.getText().toString());
                                d.setTime(time.getText().toString());
                            } else {
                                d.setTitle(title.getText().toString());
                                d.setDate("");
                                d.setTime("");
                            }
                        }
                    });
                } else {
                    d = new DataItems();
                    helper = new RealmHelper(realm);
                    if (s.isChecked()) {
                        d.setTitle(title.getText().toString());
                        d.setDate(date.getText().toString());
                        d.setTime(time.getText().toString());
                    } else {
                        d.setTitle(title.getText().toString());
                        d.setDate("");
                        d.setTime("");
                    }
                    helper.save(d);
                }
                AddToDo.this.finish();
            }
        }
    }

    private int check() {
        Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);
        datetoday = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);
        if (!date.getText().toString().equalsIgnoreCase("") && time.getText().toString().equalsIgnoreCase(""))
            return 0;
        else if (date.getText().toString().equalsIgnoreCase("") && !time.getText().toString().equalsIgnoreCase(""))
            return 0;
        else if (s.isChecked() && date.getText().toString().equalsIgnoreCase("") && time.getText().toString().equalsIgnoreCase("")) {
            return 0;
        } else {
            if (syear == year) {
                if (smonth > month)
                    return 1;
                else if (smonth < month) {
                    return 0;
                } else {
                    if (sdate > datetoday)
                        return 1;
                    else if (sdate < datetoday)
                        return 0;
                    else {
                        if (shour > hour)
                            return 1;
                        else if (shour < hour)
                            return 0;
                        else {
                            if (smin > min)
                                return 1;
                            else
                                return 0;
                        }
                    }
                }
            } else if (syear < year)
                return 0;
            else
                return 1;
        }
    }

    public static class TimePickerClass extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);
            if (intent.getExtras() != null && !intent.getStringExtra("date").equalsIgnoreCase("")) {
                String input = intent.getStringExtra("time");
                SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
                SimpleDateFormat dateOutputFormat = new SimpleDateFormat("hh:mm");
                Date date;
                try {
                    date = df.parse(input);
                    String output = dateOutputFormat.format(date);
                    hour = Integer.parseInt(output.substring(0, 2));
                    min = Integer.parseInt(output.substring(3, 5));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return new TimePickerDialog(getActivity(), this, hour, min, false);
        }

        @Override
        public void onTimeSet(android.widget.TimePicker timePicker, int i, int i1) {
            c1.set(Calendar.HOUR_OF_DAY, i);
            c1.set(Calendar.MINUTE, i1);
            c1.set(Calendar.SECOND, 0);
            shour = i;
            smin = i1;
            String apm = "AM";
            EditText time = getActivity().findViewById(R.id.time);
            if (i >= 12) {
                if (i != 12)
                    i = i - 12;
                apm = "PM";
            }
            if (i == 0) {
                i = i + 12;
            }
            if (i1 < 10)
                time.setText(i + ":0" + i1 + " " + apm);
            else
                time.setText(i + ":" + i1 + " " + apm);
        }
    }

    public static class DatePickerClass extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year, month, day;
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            if (intent.getExtras() != null && !intent.getStringExtra("date").equalsIgnoreCase("")) {
                String d = intent.getStringExtra("date").substring(0, 2);
                day = Integer.parseInt(d);
                String m = intent.getStringExtra("date").substring(3, 5);
                month = Integer.parseInt(m);
                month--;
                String y = intent.getStringExtra("date").substring(6, 10);
                year = Integer.parseInt(y);
            }
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            c1.set(Calendar.YEAR, i);
            c1.set(Calendar.MONTH, i1);
            c1.set(Calendar.DAY_OF_MONTH, i2);
            EditText date = getActivity().findViewById(R.id.date);
            sdate = i2;
            smonth = i1;
            syear = i;
            i1++;
            if (i2 < 10 && i1 < 10)
                date.setText("0" + i2 + "/" + "0" + i1 + "/" + i);
            else if (i2 > 10 && i1 < 10)
                date.setText(i2 + "/" + "0" + i1 + "/" + i);
            else if (i2 < 10 && i1 > 10)
                date.setText("0" + i2 + "/" + i1 + "/" + i);
            else
                date.setText(i2 + "/" + i1 + "/" + i);
        }
    }

    private void startAlarm(Calendar c1, String s) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title", s);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c1.getTimeInMillis(), pendingIntent);
    }

}