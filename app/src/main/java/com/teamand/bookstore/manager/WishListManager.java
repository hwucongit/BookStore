package com.teamand.bookstore.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teamand.bookstore.model.User;

import java.lang.reflect.Type;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;


public class WishListManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final String PREF_NAME = "wish_list";
    private static final String KEY_DATA = "data";
    private List<Integer> listBookId;

    public WishListManager(Context context) {
        this.context = context;
        this.sharedPreferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void addToWishList(int bookId) {
        listBookId = new ArrayList<>();
        Gson gson = new Gson();
        String data = sharedPreferences.getString(KEY_DATA, null);
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        if(data!= null)
            listBookId = gson.fromJson(data, type);
        if(!isLikedBook(bookId)){
            listBookId.add(bookId);
            String newData = gson.toJson(listBookId);
            editor.putString(KEY_DATA, newData);
            editor.commit();
        }
    }
    public void deleteFromWishList(int bookId){
        listBookId = new ArrayList<>();
        Gson gson = new Gson();
        String data = sharedPreferences.getString(KEY_DATA, null);
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        if(data!= null)
            listBookId = gson.fromJson(data, type);
        if(data!= null && isLikedBook(bookId)){
            int index = listBookId.indexOf(bookId);
            listBookId.remove(index);
            String newData = gson.toJson(listBookId);
            editor.putString(KEY_DATA, newData);
            editor.commit();
        }
    }
    public List<Integer> getWishList() {
        List<Integer> listBookId = null;
        Gson gson = new Gson();
        String data = sharedPreferences.getString(KEY_DATA, null);
        Type type = new TypeToken<List<Integer>>() {
        }.getType();
        listBookId = gson.fromJson(data, type);
        return listBookId;
    }

    public boolean isLikedBook(int bookId) {

        List<Integer> listBookId = getWishList();
        if (listBookId != null) {
            for (Integer id : listBookId) {
                if (id == bookId)
                    return true;
            }
        }
        return false;
    }
    public void clearWishList(){
        editor.clear();
        editor.commit();
    }
}
