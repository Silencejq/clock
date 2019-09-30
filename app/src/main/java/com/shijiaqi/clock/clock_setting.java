package com.shijiaqi.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

public class clock_setting extends AppCompatActivity {
    private int position = -1;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock_setting);
        if (getIntent() != null) {
            Intent intent = getIntent();
            position = intent.getIntExtra("position", -1);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("clock", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Intent intent2 = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent2.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        intent2.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent2.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent2.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置铃声");
        startActivityForResult(intent2, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    editor.putString("clock_ring"+position, "" + uri);
                    editor.commit();
                    finish();
                }
            }
        }else {finish();}
    }
}
