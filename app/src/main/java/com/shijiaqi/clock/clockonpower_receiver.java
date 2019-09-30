package com.shijiaqi.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class clockonpower_receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.e("ash", "onReceive: ");
            //TODO 8.0新特性，要启动前台服务才行
            clock_fragment.update_service(context);
        }
    }
}
