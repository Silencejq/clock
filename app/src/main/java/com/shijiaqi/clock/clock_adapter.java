package com.shijiaqi.clock;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class clock_adapter extends BaseAdapter {
    private ArrayList<Map> clocks;
    private Context context;
    private String TAG = "ash";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private biglistener biglistener = new biglistener();
    private smalllistener smalllistener = new smalllistener();
    private Vibrator vb;

    clock_adapter(ArrayList<Map> a, Context b) {
        clocks = a;
        context = b;
        sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        vb = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
    }

    public void change(ArrayList<Map> a) {
        clocks = a;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return clocks.size();
    }

    @Override
    public Map getItem(int position) {
        return clocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        view_holder view_holder;
        if (convertView == null) {
//常规初始化
            convertView = LayoutInflater.from(context).inflate(R.layout.clock_item, parent, false);
            view_holder = new view_holder();
            view_holder.time = (TextView) convertView.findViewById(R.id.clock_item_time);
            view_holder.myswitch = (Switch) convertView.findViewById(R.id.clock_item_switch);
            view_holder.hide = (Button) convertView.findViewById(R.id.clock_item_hide);
            view_holder.show = (Button) convertView.findViewById(R.id.clock_item_show);
            view_holder.ring = (Button) convertView.findViewById(R.id.clock_item_ring);
            view_holder.vibrator = (ToggleButton) convertView.findViewById(R.id.clock_item_vibrator);
            view_holder.delete = (Button) convertView.findViewById(R.id.clock_item_delete);
            view_holder.t1 = (ToggleButton) convertView.findViewById(R.id.clock_item_1);
            view_holder.t2 = (ToggleButton) convertView.findViewById(R.id.clock_item_2);
            view_holder.t3 = (ToggleButton) convertView.findViewById(R.id.clock_item_3);
            view_holder.t4 = (ToggleButton) convertView.findViewById(R.id.clock_item_4);
            view_holder.t5 = (ToggleButton) convertView.findViewById(R.id.clock_item_5);
            view_holder.t6 = (ToggleButton) convertView.findViewById(R.id.clock_item_6);
            view_holder.t7 = (ToggleButton) convertView.findViewById(R.id.clock_item_7);
            convertView.setTag(view_holder);
//开始为每一个按钮打上序号并且绑定监听器
            ArrayList<View> n = new ArrayList<View>();
            n.add(view_holder.time);
            n.add(view_holder.myswitch);
            n.add(view_holder.ring);
            n.add(view_holder.vibrator);
            n.add(view_holder.delete);
            n.add(view_holder.t1);
            n.add(view_holder.t2);
            n.add(view_holder.t3);
            n.add(view_holder.t4);
            n.add(view_holder.t5);
            n.add(view_holder.t6);
            n.add(view_holder.t7);
            for (View a : n) {
                a.setTag(position);
                a.setOnClickListener(biglistener);
            }
            n.add(view_holder.hide);
            n.add(view_holder.show);
//hide和show的tag比较特殊，因为他不需要操作闹钟，他操作的是这个view
            view_holder.hide.setTag(n);
            view_holder.show.setTag(n);
            view_holder.hide.setOnClickListener(smalllistener);
            view_holder.show.setOnClickListener(smalllistener);
        } else {
            view_holder = (view_holder) convertView.getTag();
//开始为每一个按钮打上序号并且绑定监听器
            ArrayList<View> n = new ArrayList<View>();
            n.add(view_holder.time);
            n.add(view_holder.myswitch);
            n.add(view_holder.ring);
            n.add(view_holder.vibrator);
            n.add(view_holder.delete);
            n.add(view_holder.t1);
            n.add(view_holder.t2);
            n.add(view_holder.t3);
            n.add(view_holder.t4);
            n.add(view_holder.t5);
            n.add(view_holder.t6);
            n.add(view_holder.t7);
            for (View a : n) {
                a.setTag(position);
                a.setOnClickListener(biglistener);
            }
            n.add(view_holder.hide);
            n.add(view_holder.show);
//hide和show的tag比较特殊，因为他不需要操作闹钟，他操作的是这个view
            view_holder.hide.setTag(n);
            view_holder.show.setTag(n);
            view_holder.hide.setOnClickListener(smalllistener);
            view_holder.show.setOnClickListener(smalllistener);
        }
        Map clock = clocks.get(position);
        view_holder.time.setText((String) clock.get("time"));
        view_holder.myswitch.setChecked((boolean) clock.get("switch"));
        view_holder.vibrator.setChecked((boolean) clock.get("vibrator"));
//这里使用质数判断
        int again = (int) clock.get("again");
        if (again == (again / 2) * 2) {
            view_holder.t1.setChecked(true);
        }
        if (again == (again / 3) * 3) {
            view_holder.t2.setChecked(true);
        }
        if (again == (again / 5) * 5) {
            view_holder.t3.setChecked(true);
        }
        if (again == (again / 7) * 7) {
            view_holder.t4.setChecked(true);
        }
        if (again == (again / 11) * 11) {
            view_holder.t5.setChecked(true);
        }
        if (again == (again / 13) * 13) {
            view_holder.t6.setChecked(true);
        }
        if (again == (again / 17) * 17) {
            view_holder.t7.setChecked(true);
        }
//漫长的初始化完毕
        return convertView;
    }

    class biglistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            switch (v.getId()) {
                case R.id.clock_item_time: {
//调用另外一个活动来调整时间
                    Intent intent = new Intent();
                    intent.setAction("clockadd_action");
                    intent.addCategory("clockadd_category");
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                    break;
                }
                case R.id.clock_item_switch: {
                    Switch a = (Switch) v;
                    if (a.isChecked()) {
                        a.setChecked(true);
                        editor.putBoolean("switch" + position, true);
                    } else {
                        a.setChecked(false);
                        editor.putBoolean("switch" + position, false);
                    }
                    editor.commit();
                    Intent intent = new Intent(context,clock_service.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", clock_fragment.update(context));
                    intent.putExtras(bundle);
                    context.startService(intent);
                    break;
                }
                case R.id.clock_item_ring: {
//这边通过一个start新的活动再去startforresult选音乐活动，因为adapter不能直接调用startforresult
                    Intent intent = new Intent();
                    intent.setAction("clockset_action");
                    intent.addCategory("clockset_category");
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                    break;
                }
                case R.id.clock_item_vibrator: {
                    ToggleButton a = (ToggleButton) v;
                    if (a.isChecked()) {
                        a.setChecked(true);
                        vb.vibrate(500);
                        editor.putBoolean("vibrator" + position, true);
                    } else {
                        a.setChecked(false);
                        editor.putBoolean(("vibrator" + position), false);
                    }
                    editor.commit();
                    break;
                }
                case R.id.clock_item_delete: {
//开始删除，这边还是选择调整sp文件的结构使之删减遵循arraylist的模式
                    for (int n = position; n < clocks.size() - 1; n++) {
                        editor.putString("time" + n, (String) clocks.get(n + 1).get("time"));
                        editor.putBoolean("switch" + n, (boolean) clocks.get(n + 1).get("switch"));
                        editor.putInt("again" + n, (int) clocks.get(n + 1).get("again"));
                        editor.putString("clock_ring" + n, (String) clocks.get(n + 1).get("clock_ring"));
                        editor.putBoolean("vibrator" + n, (boolean) clocks.get(n + 1).get("vibrator"));
                    }
                    editor.remove("time" + (clocks.size() - 1));
                    editor.remove("switch" + (clocks.size() - 1));
                    editor.remove("again" + (clocks.size() - 1));
                    editor.remove("clock_ring" + (clocks.size() - 1));
                    editor.remove("vibrator" + (clocks.size() - 1));
                    editor.commit();
//刷新
                    SharedPreferences sharedPreferences = context.getSharedPreferences("clock", Context.MODE_PRIVATE);
                    Map all = sharedPreferences.getAll();
                    ArrayList<Map> m = new ArrayList<Map>();
                    int n = 0;
                    while (all.containsKey("time" + n)) {
                        Map map = new HashMap();
                        map.put("time", all.get("time" + n));
                        map.put("switch", all.get("switch" + n));
                        map.put("again", all.get("again" + n));
                        map.put("clock_ring", all.get("clock_ring" + n));
                        map.put("vibrator", all.get("vibrator" + n));
                        m.add(map);
                        n++;
                    }
                    change(m);
                    Intent intent = new Intent(context,clock_service.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("list", clock_fragment.update(context));
                    intent.putExtras(bundle);
                    context.startService(intent);
                    break;
                }
                case R.id.clock_item_1: {
                    int a = sharedPreferences.getInt(("again" + position), 4);
                    if (a == 4) {
                        break;
                    }
                    if (a == (a / 2) * 2) {
                        editor.putInt("again" + position, a / 2);
                    } else editor.putInt("again" + position, a * 2);
                    editor.commit();
                    break;
                }
                case R.id.clock_item_2: {
                    int a = sharedPreferences.getInt(("again" + position), 4);
                    if (a == 4) {
                        break;
                    }
                    if (a == (a / 3) * 3) {
                        editor.putInt("again" + position, a / 3);
                    } else editor.putInt("again" + position, a * 3);
                    editor.commit();
                    break;
                }
                case R.id.clock_item_3: {
                    int a = sharedPreferences.getInt(("again" + position), 4);
                    if (a == 4) {
                        break;
                    }
                    if (a == (a / 5) * 5) {
                        editor.putInt("again" + position, a / 5);
                    } else editor.putInt("again" + position, a * 5);
                    editor.commit();
                    break;
                }
                case R.id.clock_item_4: {
                    int a = sharedPreferences.getInt(("again" + position), 4);
                    if (a == 4) {
                        break;
                    }
                    if (a == (a / 7) * 7) {
                        editor.putInt("again" + position, a / 7);
                    } else editor.putInt("again" + position, a * 7);
                    editor.commit();
                    break;
                }
                case R.id.clock_item_5: {
                    int a = sharedPreferences.getInt(("again" + position), 4);
                    if (a == 4) {
                        break;
                    }
                    if (a == (a / 11) * 11) {
                        editor.putInt("again" + position, a / 11);
                    } else editor.putInt("again" + position, a * 11);
                    editor.commit();
                    break;
                }
                case R.id.clock_item_6: {
                    int a = sharedPreferences.getInt(("again" + position), 4);
                    if (a == 4) {
                        break;
                    }
                    if (a == (a / 13) * 13) {
                        editor.putInt("again" + position, a / 13);
                    } else editor.putInt("again" + position, a * 13);
                    editor.commit();
                    break;
                }
                case R.id.clock_item_7: {
                    int a = sharedPreferences.getInt(("again" + position), 4);
                    if (a == 4) {
                        break;
                    }
                    if (a == (a / 17) * 17) {
                        editor.putInt("again" + position, a / 17);
                    } else editor.putInt("again" + position, a * 17);
                    editor.commit();
                    break;
                }
                default: {
                }
            }
        }
    }

    class smalllistener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ArrayList<View> n = (ArrayList<View>) v.getTag();
            switch (v.getId()) {
                case R.id.clock_item_show: {
                    for (View a : n) {
                        a.setVisibility(View.VISIBLE);
                    }
                    n.get(12).setVisibility(View.VISIBLE);
                    n.get(13).setVisibility(View.GONE);
                    break;
                }
                case R.id.clock_item_hide: {
                    for (int s = 2; s < 12; s++) {
                        n.get(s).setVisibility(View.GONE);
                    }
                    n.get(12).setVisibility(View.GONE);
                    n.get(13).setVisibility(View.VISIBLE);
                    break;
                }
                default: {
                }
            }
        }
    }

    class view_holder {
        TextView time;
        Switch myswitch;
        Button hide;
        Button show;
        Button ring;
        ToggleButton vibrator;
        Button delete;
        ToggleButton t1;
        ToggleButton t2;
        ToggleButton t3;
        ToggleButton t4;
        ToggleButton t5;
        ToggleButton t6;
        ToggleButton t7;
    }


}
