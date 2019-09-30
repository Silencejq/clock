package com.shijiaqi.clock;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Map;

public class time_adapter extends BaseAdapter {
    private Map selected;
    private Context context;
    private String selected0;

    public time_adapter(Map selected, Context context) {
        this.selected = selected;
        this.context = context;
    }

    public void change(Map selected) {
        this.selected = selected;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return selected.size();
    }

    @Override
    public String getItem(int position) {
        return (String) selected.get("" + position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        selected0 = (String) selected.get("" + position);
        view_holder view_holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.time_item, parent, false);
            view_holder = new view_holder();
            view_holder.name = (TextView) convertView.findViewById(R.id.time_item_name);
            view_holder.aa = (TextClock) convertView.findViewById(R.id.time_item_aa);
            view_holder.hmm = (TextClock) convertView.findViewById(R.id.time_item_hmm);
            view_holder.more = (TextClock) convertView.findViewById(R.id.time_item_more);
            convertView.setTag(view_holder);
        }
        else {
            view_holder=(view_holder) convertView.getTag();
        }
        view_holder.name.setText(selected0);
        view_holder.aa.setTimeZone(selected0);
        view_holder.hmm.setTimeZone(selected0);
        view_holder.more.setTimeZone(selected0);
        return convertView;
    }

    class view_holder {
        TextView name;
        TextClock aa;
        TextClock hmm;
        TextClock more;
    }
}