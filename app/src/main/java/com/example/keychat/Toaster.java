package com.example.keychat;

import android.app.Activity;
import android.widget.Toast;

public class Toaster {
    public static void toast(String msg, Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
