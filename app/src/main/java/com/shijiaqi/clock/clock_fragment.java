package com.shijiaqi.clock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class clock_fragment extends Fragment {
    private String TAG = "555";
    private ListView listView;
    private Button addbutton;
    private ArrayList<Map> clocks;
    private SharedPreferences sharedPreferences;
    private Map all;
    private Context context;
    private clock_adapter clock_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.clock, container, false);
        listView = view.findViewById(R.id.clock_list);
        context = getContext();
        addbutton = view.findViewById(R.id.clock_add);
        addbutton.setOnClickListener((View n)->{
            Intent intent =new Intent(getContext(),clock_addnew.class);
            startActivity(intent);
        });
        sharedPreferences = context.getSharedPreferences("clock",Context.MODE_PRIVATE);
        all = sharedPreferences.getAll();
        clocks = new ArrayList<Map>();
        int n=0;
        while(all.containsKey("time"+n)){
            Map map = new HashMap();
            map.put("time",all.get("time"+n));
            map.put("switch",all.get("switch"+n));
            map.put("again",all.get("again"+n));
            map.put("clock_ring",all.get("clock_ring"+n));
            map.put("vibrator",all.get("vibrator"+n));
            clocks.add(map);
            n++;
        }
        clock_adapter = new clock_adapter(clocks,context);
        listView.setAdapter(clock_adapter);
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        clock_adapter.change(update(context));
    }

    public static ArrayList update(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("clock",Context.MODE_PRIVATE);
        Map all = sharedPreferences.getAll();
        ArrayList clocks = new ArrayList<Map>();
        int n=0;
        while(all.containsKey("time"+n)){
            Map map = new HashMap();
            map.put("time",all.get("time"+n));
            map.put("switch",all.get("switch"+n));
            map.put("again",all.get("again"+n));
            map.put("clock_ring",all.get("clock_ring"+n));
            map.put("vibrator",all.get("vibrator"+n));
            clocks.add(map);
            n++;
        }
        return clocks;
    }

    public static void update_service(Context context){
        Intent intent = new Intent(context, clock_service.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", clock_fragment.update(context));
        intent.putExtras(bundle);
        context.startForegroundService(intent);
//TODO 关于systemUI
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: ");
    }
}

