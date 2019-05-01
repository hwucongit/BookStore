package com.teamand.bookstore.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.teamand.bookstore.model.Book;
import com.teamand.bookstore.model.BookInfo;
import com.teamand.bookstore.model.Cart;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CartManager {
    private static CartManager cartManager;
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static String PREF_NAME = "cart";
    private static String KEY_DATA = "data";
    private static String KEY_TOTAL_ITEM = "total_item";
    private List<BookInfo> bookInfoList;
    private Gson gson;
    private Type type;

    public CartManager(Context context) {
        this.context = context;
        gson = new Gson();
        bookInfoList = new ArrayList<>();
        type = new TypeToken<List<BookInfo>>() {
        }.getType();
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized CartManager getInstance(Context context) {
        if (cartManager == null) {
            cartManager = new CartManager(context);
        }
        return cartManager;
    }

    public int getTotalItem() {
        return sharedPreferences.getInt(KEY_TOTAL_ITEM, 0);
    }


    public boolean isExistedItem(BookInfo bookInfo) {
        String data = sharedPreferences.getString(KEY_DATA,null);
        if(data != null){
            bookInfoList = gson.fromJson(data,type);
            for (int i = 0; i < bookInfoList.size(); i++) {
                if(bookInfoList.get(i).getId() == bookInfo.getId())
                    return true;
            }
        }
        return false;
    }

    public boolean addToCart(BookInfo bookInfo,int quantity) {
        if (!isExistedItem(bookInfo)) {
            String data = sharedPreferences.getString(KEY_DATA, null);
            if (data != null)
                bookInfoList = gson.fromJson(data, type);
            bookInfo.setQuantity(quantity);
            bookInfoList.add(bookInfo);
            String newData = gson.toJson(bookInfoList);
            editor.putString(KEY_DATA, newData);
            editor.putInt(KEY_TOTAL_ITEM, sharedPreferences.getInt(KEY_TOTAL_ITEM, 0) + 1);
            editor.commit();
            return true;
        }
        return false;
    }

    public void deleteFromCart(BookInfo bookInfo) {
        String data = sharedPreferences.getString(KEY_DATA, null);
        if (data != null) {
            bookInfoList = gson.fromJson(data, type);
            for (int i = 0; i < bookInfoList.size(); i++) {
                if(bookInfoList.get(i).getId() == bookInfo.getId()){
                    bookInfoList.remove(bookInfoList.get(i));
                    break;
                }
            }
            String newData = gson.toJson(bookInfoList, type);
            editor.putString(KEY_DATA, newData);
            if (getTotalItem() > 0) {
                editor.putInt(KEY_TOTAL_ITEM, sharedPreferences.getInt(KEY_TOTAL_ITEM, 0) - 1);
            }
            editor.commit();
        }
    }

    public void updateQuantity(int bookId, int quantity) {
        String data = sharedPreferences.getString(KEY_DATA, null);
        if (data != null) {
            bookInfoList = gson.fromJson(data, type);
            for (int i = 0; i < bookInfoList.size(); i++) {
                if (bookId == bookInfoList.get(i).getId()) {
                    BookInfo bookInfo = bookInfoList.get(i);
                    bookInfo.setQuantity((quantity));
                    bookInfoList.set(i,bookInfo);
                    String newData = gson.toJson(bookInfoList);
                    editor.putString(KEY_DATA, newData);
                    editor.commit();
                    break;
                }
            }
        }
    }
    public List<BookInfo> getListBookInCart(){
        bookInfoList = new ArrayList<>();
        String data = sharedPreferences.getString(KEY_DATA, null);
        if(data!= null){
            bookInfoList = gson.fromJson(data,type);
        }
        return bookInfoList;
    }

    public void clearCart(){
        editor.clear();
        editor.commit();
    }
}
