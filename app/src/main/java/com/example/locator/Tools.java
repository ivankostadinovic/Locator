package com.example.locator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public  class Tools {

    private static Toast toast;

    public static void log(String msg) {
        Log.d("**com.manigo**", msg);
    }

    public static void showMsg(Context ctx, String msg) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

}
