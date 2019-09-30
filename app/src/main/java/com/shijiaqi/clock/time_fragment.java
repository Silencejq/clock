package com.shijiaqi.clock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.*;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.Map;

public class time_fragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private Context context;
    private ListView listView;
    private Map selected;
    private time_adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        sharedPreferences = context.getSharedPreferences("worldtime", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time, container, false);
        Button button = (Button) view.findViewById(R.id.time_worldtime);
        button.setOnClickListener(new time_worldtime_listener());
//加载世界时间
        listView = (ListView) view.findViewById(R.id.time_listview);
        selected = sharedPreferences.getAll();
        adapter = new time_adapter(selected, context);
        listView.setAdapter(adapter);
        return view;
    }

    class time_worldtime_listener implements OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction("worldtime_action");
            intent.addCategory("worldtime_category");
            startActivityForResult(intent, 1);
        }
    }

//刷新世界时间
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                selected = sharedPreferences.getAll();
                adapter.change(selected);
            }
        }
    }
}
