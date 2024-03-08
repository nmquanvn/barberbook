package com.example.barbershop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

public class helper {
    public static String mode = "admin"; // user, staff, admin

    public static byte[] getDrawableAsByteArray(int d) {
        Resources res = MainActivity.main.getResources();
        Drawable drawable = res.getDrawable(d);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }
    public static int dpToPixel(int dp){
        return (int)MainActivity.main.getResources().getDisplayMetrics().density * dp;
    }
    public static void ShowMessage(Context context, String msg){
        new AlertDialog.Builder(context)
                //.setTitle("Your Alert")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...
                    }
                }).show();
    }
    public static void ShowMessage(Context context, String msg, DialogInterface.OnClickListener action){
        new AlertDialog.Builder(context)
                //.setTitle("Your Alert")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("ok", action).show();
    }
    public static String formatmoney(float number){
        String n = Math.round(number) + "";
        int i = n.length() - 1;
        String temp = "";
        int k = 0;
        while(i >= 0){
            temp += n.charAt(i);
            k++;
            if(k==3 && i > 0){
                temp += ".";
                k=0;
            }
            i--;
        }

        String ret = "";
        for(i=temp.length()-1; i>=0; i--){
            ret += temp.charAt(i);
        }

        return ret;
    }
}
