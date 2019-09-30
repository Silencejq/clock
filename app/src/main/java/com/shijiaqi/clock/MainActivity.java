/**
 * auther:ash
 */
package com.shijiaqi.clock;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    private Toolbar toolbar;
    private mypager_adapter pager;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        pager = new mypager_adapter(getSupportFragmentManager());
        viewpager = (ViewPager)findViewById(R.id.main_viewpager);
        viewpager.setAdapter(pager);
        viewpager.setCurrentItem(0);
        viewpager.addOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_time:
                viewpager.setCurrentItem(0);
                return true;

            case R.id.button_clock:
                viewpager.setCurrentItem(1);
                return true;

            case R.id.button_countdown:
                viewpager.setCurrentItem(2);
                return true;

            case R.id.button_count:
                viewpager.setCurrentItem(3);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {}

    @Override
    public void onPageScrollStateChanged(int state){}

}
