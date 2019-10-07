package com.shijiaqi.clock;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class clock_service extends Service {
    private String TAG = "ash";
    private Bundle bundle;
    private ArrayList<AlarmManager> alarmlist;
    private ArrayList<PendingIntent> intentlist;

    public clock_service() {
        alarmlist = new ArrayList<>();
        intentlist = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationChannel channel = new NotificationChannel("ash", "ash", NotificationManager.IMPORTANCE_HIGH);
//        manager.createNotificationChannel(channel);
//        Notification notification = new Notification.Builder(this, "ash")
//                .setContentTitle("闹钟功能")
//                .setContentText("闹钟设置完毕~")
//                .setSmallIcon(R.mipmap.baseline_alarm_black_48)
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.baseline_alarm_black_48))
//                .build();
//        startForeground(2, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//这里选择删除全部alarm再重新生成，省下很多代码但是效率不高,不过一般来说不会有很多闹钟。
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("ash", "ash", NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(this, "ash")
                .setContentTitle("闹钟功能")
                .setContentText("闹钟设置完毕~")
                .setSmallIcon(R.mipmap.baseline_alarm_black_48)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.baseline_alarm_black_48))
                .build();
        startForeground(2, notification);
        int m = 0;
        for (AlarmManager n : alarmlist) {
            n.cancel(intentlist.get(m));
            m++;
        }
        intentlist.clear();
        alarmlist.clear();
        bundle = intent.getExtras();
        ArrayList clocks = bundle.getParcelableArrayList("list");
//如果闹钟列表空，关闭前台广播
        if (clocks.isEmpty()) {
            stopForeground(true);
            onDestroy();
        }
//注意这里，之前有个剩余时间一直固定的bug，原因就在这个calendar，单例生成的它不是动态的，所以要在使用前调用，而不是在oncreate
        Calendar calendar = Calendar.getInstance();
        boolean flag = true;
        int n = 0;
        for (Map clock : (ArrayList<Map>) clocks) {
            if ((boolean) (clock.get("switch"))) {
                flag = false;
                int hour0 = calendar.get(Calendar.HOUR_OF_DAY);
                int minute0 = calendar.get(Calendar.MINUTE);
                int secend0 = calendar.get(Calendar.SECOND);
                int hour = Integer.parseInt(((String) clock.get("time")).substring(0, 2));
                int minute = Integer.parseInt(((String) clock.get("time")).substring(3));
                Long time = 0L;
                if (hour > hour0) {
                    time = ((hour - hour0) * 3600 + (minute - minute0) * 60 - secend0) * 1000L;
                } else if (hour == hour0 && minute > minute0) {
                    time = ((minute - minute0) * 60 - secend0) * 1000L;
                } else {
                    time = (24 * 3600 + (hour - hour0) * 3600 + (minute - minute0) * 60 - secend0) * 1000L;
                }
                Intent intentnew = new Intent(this, clock_ring.class);
                intentnew.putExtra("id", n);
//注意这里requestcode必须不同，不然会被覆盖
                PendingIntent pendingIntent = PendingIntent.getActivity(this, n, intentnew, FLAG_UPDATE_CURRENT);
                intentlist.add(pendingIntent);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + time, pendingIntent);
                alarmlist.add(alarmManager);
            }
            n++;
        }
//如果闹钟列表全是关闭的，也关闭前台广播
        if (flag) {
            stopForeground(true);
            onDestroy();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
