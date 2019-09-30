package com.shijiaqi.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

public class clock_addnew extends AppCompatActivity {
    Toolbar toolbar;
    TimePicker timePicker;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int position = -1;

    String TAG = "ash";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_addnew);
        toolbar = (Toolbar) findViewById(R.id.worldtime_toolbar);
        setSupportActionBar(toolbar);
        timePicker = (TimePicker) findViewById(R.id.clock_addnew_pick);
        sharedPreferences = getSharedPreferences("clock", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (getIntent() != null) {
            Intent intent = getIntent();
            position = intent.getIntExtra("position", -1);
        }
    }


    public void back(View view) {
        finish();
    }

    public void commit(View view) {
        String hour;
        String minute;
        if (timePicker.getHour() < 10) {
            hour = "0" + timePicker.getHour();
        } else {
            hour = "" + timePicker.getHour();
        }
        if (timePicker.getMinute() < 10) {
            minute = "0" + timePicker.getMinute();
        } else {
            minute = "" + timePicker.getMinute();
        }
        if (position == -1) {
            int n = 0;
            while (sharedPreferences.contains("time" + n)) {
                n++;
            }
            editor.putString(("time" + n), hour + ":" + minute);
            editor.putBoolean(("switch" + n), true);
            editor.putInt(("again" + n), 1);
            editor.putString(("clock_ring" + n), "content://media/internal/audio/media/88");
            editor.putBoolean(("vibrator" + n), true);
            editor.commit();
            Intent intent = new Intent(this,clock_service.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("list",clock_fragment.update(this));
            intent.putExtras(bundle);
            startService(intent);
            finish();
        } else {
            editor.putString(("time" + position), "" + hour + ":" + minute);
            editor.commit();
            Intent intent = new Intent(this,clock_service.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("list",clock_fragment.update(this));
            intent.putExtras(bundle);
            startService(intent);
            finish();
        }
    }
}
