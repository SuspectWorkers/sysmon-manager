package com.tablet.sysmon.preload;

import static com.tablet.sysmon.preload.SysPreload.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        var action = intent.getAction();
        if (!Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(action)
                && !Intent.ACTION_BOOT_COMPLETED.equals(action)
                && !"com.tablet.sysmon.preload.LAUNCH".equals(action)) {
            return;
        }
        try {
            context.startService(new Intent(context, SysPreloadService.class));
            Log.i(TAG, "SysPreloadService started from boot action: " + action);
        } catch (Throwable e) {

            Log.e(TAG, "Failed to start SysPreloadService from boot action: " + action, e);
        }
    }
}
