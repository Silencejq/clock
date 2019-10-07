package com.shijiaqi.clock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class countdown_service extends Service {
    private countdown_binder binder = new countdown_binder();
    private Timer timer;
    private Long time = 0L;
    private boolean doit = false;

    public class countdown_binder extends Binder {
        public Long gettime() {
            return time;
        }

        public void canceltimer() {
            timer.cancel();
        }

        public boolean getdoit() {
            return doit;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("ash", "ash", NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel(channel);
        Notification notification = new Notification.Builder(this, "ash")
                .setContentTitle("定时器功能")
                .setContentText("定时器正在后台继续运行")
                .setSmallIcon(R.mipmap.baseline_directions_run_black_48)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.baseline_directions_run_black_48))
                .build();
        startForeground(1, notification);
        if (intent.getBooleanExtra("update", false)) time = intent.getLongExtra("time", 0L);
        doit = intent.getBooleanExtra("doit", false);
        boolean drop = intent.getBooleanExtra("drop", false);
        if (drop) {
            if (timer != null) timer.cancel();
            stopForeground(true);
            onDestroy();
        }
        if (time != 0L && doit) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    time -= 10L;
                }
            }, 0, 10);
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
