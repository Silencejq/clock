package com.shijiaqi.clock;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class countdown_service extends Service {
    private countdown_binder binder = new countdown_binder();
    private Timer timer;
    private Long time = 0L;
    private String music;
    private Uri musicuri;
    private MediaPlayer mediaPlayer;
    private boolean doit = false;

    public class countdown_binder extends Binder {
        public Long gettime() {
            return time;
        }

        public void canceltimer() {
            timer.cancel();
        }

        public boolean stop() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                return true;
            }
            return false;
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
    public boolean onUnbind(Intent intent) {
        if (timer != null) timer.cancel();
        stopForeground(true);
        onDestroy();
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra("update", false)) time = intent.getLongExtra("time", 0L);
        doit = intent.getBooleanExtra("doit", false);
        music = intent.getStringExtra("music");
        if (time != 0L) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("ash", "ash", NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null);
            channel.enableVibration(false);
            manager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, "ash")
                    .setContentTitle("定时器功能")
                    .setContentText("定时器正在后台继续运行")
                    .setSmallIcon(R.mipmap.baseline_directions_run_black_48)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.baseline_directions_run_black_48))
                    .build();
            startForeground(1, notification);
        }
        if (time != 0L && doit) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (time == 0) {
                        Log.e("a", "??");
                        time = 0L;
                        if (music == "false") {
                            musicuri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                        } else {
                            musicuri = Uri.parse(music);
                        }
                        mediaPlayer = new MediaPlayer();
                        try {
                            mediaPlayer.setDataSource(getApplicationContext(), musicuri);
                        } catch (Exception e) {
                        }
                        mediaPlayer.setOnPreparedListener((MediaPlayer n) -> mediaPlayer.start());
                        mediaPlayer.prepareAsync();
                        cancel();
                        timer.purge();
                    } else {
                        time -= 10L;
                    }
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 10);
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
