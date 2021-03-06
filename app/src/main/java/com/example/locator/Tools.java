package com.example.locator;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;

public class Tools {

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

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 25, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public static boolean locationPermissionGiven(FragmentActivity context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED;
    }

    public static void showInfoDialog(Context ctx, String text) {
        new MaterialAlertDialogBuilder(ctx)
            .setTitle("Info")
            .setMessage(text)
            .setPositiveButton("ok", (dialog, which) -> {
                dialog.dismiss();
            })
            .show();

    }
}
