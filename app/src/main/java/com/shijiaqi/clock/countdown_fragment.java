package com.shijiaqi.clock;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;

public class countdown_fragment extends Fragment {
    private TextView maintime;
    private TextView more;
    private Button start;
    private Button clear;
    private Button setting;
    private View line;
    private View view;
    private TextView settime;
    private Button[] number;
    private Button back;
    private Button commit;
    private ArrayList<View> view1;
    private ArrayList<View> view2;
    private String time_set;
    private Long time_set0 = 0L;
    private Timer timer;
    private mytask timerTask;
    private Message message;
    private Handler handler;
    private boolean is = false;
    private boolean isstart = false;
    private Drawable startpic;
    private Drawable stoppic;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private MediaPlayer mediaPlayer;
    private Uri musicuri;
    private countdown_service.countdown_binder binder;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
//非正常杀死才回调
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (countdown_service.countdown_binder) service;
            if (binder != null) {
                if(binder.stop()){
                    time_set0 = 0L;
                    hide(view1);
                    show(view2);
                }
                if (binder.gettime() != 0L) {
                    Log.e("ash", ""+binder.gettime());
                    time_set0=binder.gettime();
                    transform_extra(time_set0, maintime, more);
                    hide(view2);
                    show(view1);
                    if (binder.getdoit()) {
                        timerTask = new mytask();
                        timer = new Timer();
                        isstart = true;
                        timer.scheduleAtFixedRate(timerTask, 0, 10);
                        start.setTag("stop");
                        start.setBackground(stoppic);
                        clear.setTag("clear");
                    }
                }

            }
            getActivity().unbindService(conn);
            binder = null;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.countdown, container, false);
        view1 = new ArrayList<>();
        view2 = new ArrayList<>();
//绑定后端计时服务

//主页面的设置
        maintime = view.findViewById(R.id.countdown_time);
        more = view.findViewById(R.id.countdown_more);
        start = view.findViewById(R.id.countdown_start);
        clear = view.findViewById(R.id.countdown_clear);
        setting = view.findViewById(R.id.countdown_setting);
        line = view.findViewById(R.id.countdown_line);
        view1.add(maintime);
        view1.add(more);
        view1.add(start);
        view1.add(clear);
        view1.add(setting);
        view1.add(line);
        button_listener listener1 = new button_listener();
        start.setOnClickListener(listener1);
        clear.setOnClickListener(listener1);
        setting.setOnClickListener(listener1);
        startpic = getResources().getDrawable(R.drawable.ic_play_circle);
        stoppic = getResources().getDrawable(R.drawable.ic_time_out);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                transform_extra(time_set0, maintime, more);
                if (time_set0 == 0 && isstart) {
                    Toast.makeText(getContext(), "时间到啦～", Toast.LENGTH_LONG).show();
                }
            }
        };
        sharedPreferences = getActivity().getSharedPreferences("countdownmusic", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置铃声");
//设置时间页面的设置
        setting_listener listener2 = new setting_listener();
        settime = view.findViewById(R.id.countdown_before_time);
        back = view.findViewById(R.id.c_b_back);
        commit = view.findViewById(R.id.c_b_commit);
        number = new Button[10];
        number[0] = view.findViewById(R.id.c_b_0);
        number[1] = view.findViewById(R.id.c_b_1);
        number[2] = view.findViewById(R.id.c_b_2);
        number[3] = view.findViewById(R.id.c_b_3);
        number[4] = view.findViewById(R.id.c_b_4);
        number[5] = view.findViewById(R.id.c_b_5);
        number[6] = view.findViewById(R.id.c_b_6);
        number[7] = view.findViewById(R.id.c_b_7);
        number[8] = view.findViewById(R.id.c_b_8);
        number[9] = view.findViewById(R.id.c_b_9);
        view2.add(settime);
        view2.add(back);
        view2.add(commit);
        for (int n = 0; n < 10; n++) {
            view2.add(number[n]);
            number[n].setOnClickListener(listener2);
        }
        back.setOnClickListener(listener2);
        commit.setOnClickListener(listener2);
        time_set = "000000";
        return view;
    }

    class button_listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button s = (Button) v;
            switch ((String) s.getTag()) {
                case "start": {
//timer和播放器都要重新声明一个，因为之前的释放掉了
                    isstart = true;
                    timer = new Timer();
                    timerTask = new mytask();
                    timer.scheduleAtFixedRate(timerTask, 0, 10);
                    start.setTag("stop");
                    start.setBackground(stoppic);
                    clear.setTag("clear");
                    break;
                }
                case "stopmusic": {
//不要释放资源
                    mediaPlayer.stop();
                    start.setTag("start");
                    start.setBackground(startpic);
                    break;
                }
                case "stop": {
//stop时不要释放资源，因为stop完再clear会导致两次释放的bug
                    isstart = false;
                    timerTask.cancel();
                    timer.purge();
                    start.setTag("start");
                    start.setBackground(startpic);
                    break;
                }
                case "clear": {
//注意clear时一定要把资源都释放了，防止线程不同步问题，应把isstart设为0
                    isstart = false;
                    timerTask.cancel();
                    timer.cancel();
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                    }
                    start.setTag("start");
                    start.setBackground(startpic);
                    time_set0 = 0L;
                    hide(view1);
                    show(view2);
                    break;
                }
                case "setting": {
                    startActivityForResult(intent, 1);
                    break;
                }
                default: {
                    break;
                }

            }
        }
    }

    class setting_listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button s = (Button) v;
            switch ((String) s.getTag()) {
                case "commit": {
                    time_set0 = (Long.parseLong(time_set.substring(0, 2)) * 3600L
                            + Long.parseLong(time_set.substring(2, 4)) * 60L
                            + Long.parseLong(time_set.substring(4))) * 1000;
                    transform_extra(time_set0, maintime, more);
                    clear.setTag("nooooclear");
                    hide(view2);
                    show(view1);
                    time_set = "000000";
                    settime.setText("00小时00分00秒");
                    break;
                }
                case "back": {
                    time_set = "000000";
                    settime.setText("00小时00分00秒");
                    break;
                }
                default: {
                    time_set = time_set + s.getText();
                    time_set = time_set.substring(time_set.length() - 6);
                    settime.setText(time_set.substring(0, 2) + "小时" + time_set.substring(2, 4) + "分" + time_set.substring(4) + "秒");
                }
            }
        }
    }

    //把控件放在一起减少代码量
    void hide(ArrayList<View> n) {
        for (View a : n) {
            a.setVisibility(View.GONE);
        }
    }

    void show(ArrayList<View> n) {
        for (View a : n) {
            a.setVisibility(View.VISIBLE);
        }
    }

    public static void transform_extra(Long n, TextView time1, TextView time2) {
        String time = count_adapter.transform(n);
        time1.setText(time.substring(0, time.length() - 2));
        time2.setText(time.substring(time.length() - 2));
    }

    class mytask extends TimerTask {
        @Override
        public void run() {
            if (isstart) {
                if (time_set0 == 0) {
                    String music = sharedPreferences.getString("music", "false");
                    if (music == "false") {
                        musicuri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                    } else {
                        musicuri = Uri.parse(music);
                    }
                    mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(getActivity(), musicuri);
                    } catch (Exception e) {
                    }
                    mediaPlayer.setOnPreparedListener((MediaPlayer n) -> mediaPlayer.start());
                    mediaPlayer.prepareAsync();
                    isstart = false;
                    timerTask.cancel();
                    timer.purge();
                    start.setTag("stopmusic");
                    start.setBackground(stoppic);
                    time_set0 = 0L;
                } else {
                    time_set0 -= 10L;
                    message = new Message();
                    handler.sendMessage(message);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (uri != null) {
                    editor.putString("music", "" + uri);
                    editor.commit();
                }
            }
        }
    }

    @CallSuper
    public void onStop() {
        super.onStop();
        if (time_set0 != 0L) {
            Intent intent8 = new Intent(getActivity(), countdown_service.class);
            String music = sharedPreferences.getString("music", "false");
            intent8.putExtra("music", music);
            intent8.putExtra("time", time_set0);
            intent8.putExtra("doit", isstart);
            intent8.putExtra("update", true);
            getActivity().startForegroundService(intent8);
            if (isstart) {
                timerTask.cancel();
                timer.purge();
                start.setTag("start");
                start.setBackground(startpic);
            }
        }else {
            Intent intent8 = new Intent(getActivity(), countdown_service.class);
            intent8.putExtra("time", 0L);
            intent8.putExtra("update", true);
            getActivity().startForegroundService(intent8);
        }
    }

    @CallSuper
    public void onResume() {
        super.onResume();
        Intent intent9 = new Intent(getActivity(), countdown_service.class);
        getActivity().bindService(intent9, conn, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
