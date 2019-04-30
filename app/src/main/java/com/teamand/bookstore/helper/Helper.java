package com.teamand.bookstore.helper;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

public class Helper {
    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
