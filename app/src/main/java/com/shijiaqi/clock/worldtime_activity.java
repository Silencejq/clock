package com.shijiaqi.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class worldtime_activity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private Context context;
    private HashMap countries0;
    private Map selected;
    private ArrayList<HashMap> countries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worldtime);
        toolbar = (Toolbar) findViewById(R.id.worldtime_toolbar);
        setSupportActionBar(toolbar);
//设置结束后返回一个信号使得主页面刷新
        setResult(1);
//ListView设置
        listView = (ListView) findViewById(R.id.worldtime_list);
        context = worldtime_activity.this;
        SharedPreferences sharedPreferences = context.getSharedPreferences("worldtime", Context.MODE_PRIVATE);
        selected = sharedPreferences.getAll();
        countries = new ArrayList<>();
        for (String country : TimeZone.getAvailableIDs()) {
            countries0 = new HashMap();
            countries0.put("name", country);
            if (selected.containsValue(country)) countries0.put("ischecked", true);
            else countries0.put("ischecked", false);
            countries.add(countries0);
        }
        worldtime_adapter adapter = new worldtime_adapter(countries, context);
        listView.setAdapter(adapter);
    }

//不知道有没有用
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
