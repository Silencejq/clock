package com.shijiaqi.clock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

public class clock_ring extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ring);
        MediaPlayer mediaPlayer = new MediaPlayer();
        SharedPreferences sharedPreferences = getSharedPreferences("clock", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        int days = sharedPreferences.getInt("again"+id, 1);
//如果是单次闹钟先更新sp，再更新服务
        if (days == 1) {
            editor.putBoolean("switch" + id, false);
            editor.commit();
            clock_fragment.update_service(this);
//如果不是，则直接更新服务，恰好是一天判断一次
        } else {
            clock_fragment.update_service(this);
        }
        Boolean flag = true;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day){
            case 1:day=17;break;
            case 2:day=2;break;
            case 3:day=3;break;
            case 4:day=5;break;
            case 5:day=7;break;
            case 6:day=11;break;
            case 7:day=13;break;
            default:
        }
        if(days==(days/day)*day)flag=false;
            if (flag && days!=1) {
                finish();
            } else {
//是否震动
                Vibrator vb = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
                if (sharedPreferences.getBoolean("vibrator" + id, false)) {
                    vb.vibrate(100000);
                }
//开始播放选中的铃声
                String ring = sharedPreferences.getString("clock_ring" + id, "false");
                Uri ringuri;
                if (ring == "false") {
                    ringuri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                } else {
                    ringuri = Uri.parse(ring);
                }
                try {
                    mediaPlayer.setDataSource(this, ringuri);
                } catch (Exception e) {
                }
                mediaPlayer.setOnPreparedListener((MediaPlayer n) -> mediaPlayer.start());
                mediaPlayer.prepareAsync();
                new AlertDialog.Builder(this).setTitle("闹钟").setMessage("您定的闹钟时间到了！！！")
                        .setNegativeButton("关闭闹铃", (DialogInterface dialog, int which) -> {
                            mediaPlayer.stop();
                            mediaPlayer.release();
                            vb.cancel();
                            this.finish();
                        }).setPositiveButton("再睡十分钟！", (DialogInterface dialog, int which) -> {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    vb.cancel();
                    editor.putBoolean("switch" + id, true);
                    String minute = (sharedPreferences.getString("time" + id, "null")).substring(3);
                    String hour = (sharedPreferences.getString("time" + id, "null")).substring(0, 2);
                    if (Integer.parseInt(minute) < 50) {
                        String minute1 = "" + (Integer.parseInt(minute) + 10);
                        editor.putString("time" + id, hour + ":" + minute1);
                    } else {
                        String hour1 = "" + (Integer.parseInt(hour) + 1);
                        String minute1 = "" + (Integer.parseInt(minute) - 50);
                        editor.putString("time" + id, hour1 + ":" + minute1);
                    }
                    editor.commit();
                    clock_fragment.update_service(this);
                    this.finish();
                }).show();
            }
    }
}
