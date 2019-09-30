package com.shijiaqi.clock;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class count_fragment extends Fragment {
    private Long time = 0L;
    private Button start;
    private Button clear;
    private Button cut;
    private TextView more;
    private Timer timer;
    private ArrayList<Long[]> cutlist;
    private int cutnumber;
    private count_adapter adapter;
    private Context context;
    private TextView maintime;
    private ListView listView;
    private Drawable startpic;
    private Drawable stoppic;
    private starttimertask timerTask;

    private String TAG = "ash";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.count, container, false);
        start = (Button) view.findViewById(R.id.count_start);
        clear = (Button) view.findViewById(R.id.count_clear);
        cut = (Button) view.findViewById(R.id.count_cut);
        more = (TextView) view.findViewById(R.id.count_more);
        button_listener listener1 = new button_listener();
        start.setOnClickListener(listener1);
        clear.setOnClickListener(listener1);
        cut.setOnClickListener(listener1);
        startpic = getResources().getDrawable(R.drawable.ic_play_circle);
        stoppic = getResources().getDrawable(R.drawable.ic_time_out);
        maintime = (TextView) view.findViewById(R.id.count_chronometer);
        cutlist = new ArrayList<Long[]>();
        cutnumber = 0;
        context = getActivity();
        adapter = new count_adapter(cutlist, context);
        listView = view.findViewById(R.id.count_list);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        start.setTag("start");
        start.setBackground(startpic);
    }

    class button_listener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Button a = (Button) v;
            switch ((String) a.getTag()) {
                case "start": {
                    timerTask = new starttimertask();
                    timer.scheduleAtFixedRate(timerTask, 0, 10);
                    start.setTag("stop");
                    start.setBackground(stoppic);
                    break;
                }
                case "stop": {
                    timerTask.cancel();
                    timer.purge();
                    start.setTag("start");
                    start.setBackground(startpic);
                    break;
                }
                case "clear": {
//因为timer和UI线程不在一起，在timer最后加入一个clear防止修改不到位
                    if(timerTask!=null)timerTask.cancel();
                    timer.purge();
                    timer.schedule(new endtimertask(), 0);
                    start.setTag("start");
                    start.setBackground(startpic);
//重新计时
                    time=0L;
//防止cut的bug
                    cutlist.clear();
                    adapter.change(cutlist);
                    cutnumber = 0;
                    break;
                }
                case "cut": {
                    if (cutnumber == 0) {
                        Long cut1 = time;
                        Long cut2 = time;
                        Long[] aaa = {cut1, cut2};
                        cutlist.add(aaa);
                        cutnumber++;
                        adapter.change(cutlist);
                    } else {
                        Long cut1 = time - (cutlist.get(cutnumber - 1))[1];
                        Long cut2 = time;
                        Long[] aaa = {cut1, cut2};
                        cutlist.add(aaa);
                        cutnumber++;
                        adapter.change(cutlist);
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    class starttimertask extends TimerTask {
        @Override
        public void run() {
            time += 10L;
            getActivity().runOnUiThread(() -> {
                String time0 = count_adapter.transform(time);
                maintime.setText(time0.substring(0, time0.length() - 3));
                more.setText(time0.substring(time0.length() - 2));
            });
        }
    }

    class endtimertask extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(() -> {
                maintime.setText("00:00");
                more.setText("00");
            });
        }
    }

}
