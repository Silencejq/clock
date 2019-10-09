package com.shijiaqi.clock;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class worldtime_adapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener {
    private ArrayList<HashMap> countries;
    private Context context;

    public worldtime_adapter(ArrayList<HashMap> countries, Context context) {
        this.countries = countries;
        this.context = context;
    }

    public void change(ArrayList<HashMap> countries) {
        this.countries = countries;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public HashMap getItem(int position) {
        return countries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view_holder view_holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.worldtime_item, parent, false);
            view_holder = new view_holder();
            view_holder.textView = (TextView) convertView.findViewById(R.id.worldtime_item_text);
            view_holder.checkBox = (CheckBox) convertView.findViewById(R.id.worldtime_item_button);
            convertView.setTag(view_holder);
            view_holder.checkBox.setTag(position);
        } else {
            view_holder = (view_holder) convertView.getTag();
            view_holder.checkBox.setTag(position);
        }
        view_holder.checkBox.setOnCheckedChangeListener(this);
        view_holder.checkBox.setChecked((boolean) (countries.get(position).get("ischecked")));
        view_holder.textView.setText((String) countries.get(position).get("name"));
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int index = (int) buttonView.getTag();
        if (isChecked) {
            countries.get(index).put("ischecked", true);
            SharedPreferences sharedPreferences = context.getSharedPreferences("worldtime", Context.MODE_PRIVATE);
            int n = 0;
//翻页触发监听器！所以要在储存前做一个判断！防止重复储存同一个时区！
            boolean exist = true;
            while (sharedPreferences.contains("" + n)) {
                if (sharedPreferences.getString("" + n, "null").equals((String) countries.get(index).get("name"))) {
                    exist = false;
                    break;
                }
                n++;
            }
            if (exist) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("" + n, (String) countries.get(index).get("name"));
                editor.commit();
            }

//这边为了主界面的效率，准备加工一下sharepreference

        } else {
            countries.get(index).put("ischecked", false);
            SharedPreferences sharedPreferences = context.getSharedPreferences("worldtime", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int j = 0;
            while (sharedPreferences.contains("" + j)) {
                j++;
            }
            for (int n = 0; n < countries.size() + 2; n++) {
                if (sharedPreferences.getString("" + n, "???qqqaaa").equals(countries.get(index).get("name"))) {
                    for (int m = n; m < (j - 1); m++) {
                        editor.putString("" + m, sharedPreferences.getString("" + (m + 1), "Africa/Asmara"));
                        editor.commit();
                    }
                    editor.remove("" + (j - 1));
                    editor.commit();
                    break;
                }
            }
        }
    }
//这边为了效率建了一个类来储存
    class view_holder {
        TextView textView;
        CheckBox checkBox;
    }

}
