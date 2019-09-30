package com.shijiaqi.clock;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class countdown_service extends Service {
//TODO 调成前台应用，防止被删除
    private countdown_binder binder = new countdown_binder();
    private Timer timer;
    private Long time =0L;

    public class countdown_binder extends Binder {
        public Long gettime(){
            return time;
        }
        public void canceltimer(){
            timer.cancel();
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
        time=intent.getLongExtra("time",0L);
        if(time!=0L){
            timer=new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    time-=10L;
                }
            },0,10);
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.e("555", "onDestroy: ");
        super.onDestroy();
    }
}
