package com.teamand.bookstore.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.teamand.bookstore.model.User;


public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final String PREF_NAME = "session";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String IS_LOGIN = "isLoggedIn";

    public SessionManager(Context context){
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public void createLoginSession(int id, String name, String email){
        editor.putInt(KEY_ID,id);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_EMAIL,email);
        editor.putBoolean(IS_LOGIN,true);
        editor.commit();
    }
    public void checkLoggedIn(){
    }

    public User getCurrentUser(){
        User user = new User();
        user.setId(sharedPreferences.getInt(KEY_ID,0));
        user.setName(sharedPreferences.getString(KEY_NAME,null));
        user.setEmail(sharedPreferences.getString(KEY_EMAIL,null));
        return user;
    }
    public void logOutUser(){
        editor.clear();
        editor.commit();
    }
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN,false);
    }
}
