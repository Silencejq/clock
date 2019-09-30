package com.shijiaqi.clock;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class mypager_adapter extends FragmentPagerAdapter {
    private time_fragment time;
    private clock_fragment clock;
    private countdown_fragment countdown;
    private count_fragment count;

    public mypager_adapter(FragmentManager fm) {
        super(fm);
        time = new time_fragment();
        clock = new clock_fragment();
        countdown = new countdown_fragment();
        count = new count_fragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object instantiateItem(ViewGroup vg, int position) {
        return super.instantiateItem(vg, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

//注意position是从0开始的，所以case也要从0开始
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return time;
            case 1:
                return clock;
            case 2:
                return countdown;
            case 3:
                return count;
        }
        return clock;
    }
}
