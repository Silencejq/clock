package com.shijiaqi.clock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class count_adapter extends BaseAdapter {
    private Context context;
    private ArrayList<Long[]> cutlist;

    count_adapter(ArrayList<Long[]> cutlist, Context context) {
        this.context = context;
        this.cutlist = cutlist;
    }

    public void change(ArrayList<Long[]> cutlist) {
        this.cutlist = cutlist;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cutlist.size();
    }

    @Override
    public Long[] getItem(int position) {
        return cutlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view_holder view_holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.count_item, parent, false);
            view_holder = new view_holder();
            view_holder.id = (TextView) convertView.findViewById(R.id.count_item_id);
            view_holder.cut1 = (TextView) convertView.findViewById(R.id.count_item_cut1);
            view_holder.cut2 = (TextView) convertView.findViewById(R.id.count_item_cut2);
            convertView.setTag(view_holder);
        } else {
            view_holder = (view_holder) convertView.getTag();
        }
        view_holder.id.setText("#" + (cutlist.size() - position));
        view_holder.cut1.setText(transform(cutlist.get(cutlist.size()-1-position)[0]));
        view_holder.cut2.setText(transform(cutlist.get(cutlist.size()-1-position)[1]));
        return convertView;
    }

    public static String transform(Long time) {
        String more;
        String second;
        String hour;
        String minute;

        Long more0 = time % 1000 / 10;
        if (more0 < 10) more = "0" + more0;
        else more = "" + more0;

        Long second0 = time / 1000;
        if ((second0 % 60) < 10) second = "0" + second0 % 60;
        else second = "" + second0 % 60;

        Long hour0 = second0 / 3600;
        if (hour0 == 0) hour = "";
        else if (hour0 < 10) hour = "0" + hour0 + ":";
        else hour = "" + hour0 + ":";

        Long minute0 = second0 / 60;
        if ((minute0 % 60) < 10) minute = "0" + minute0 % 60;
        else minute = "" + minute0 % 60;

        return hour + minute + ":" + second + ":" + more;
    }

    class view_holder {
        TextView id;
        TextView cut1;
        TextView cut2;
    }
}
