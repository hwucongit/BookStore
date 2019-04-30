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
    private static String KEY_DATA_CART = "data_cart";
    private static String KEY_TOTAL_ITEM = "total_item";
    private HashMap<Integer, Integer> hashMap;
    private List<BookInfo> bookInfoList;
    private Gson gson = new Gson();
    private Type type = new TypeToken<HashMap<Integer, Integer>>() {
    }.getType();

    public CartManager(Context context) {
        this.context = context;
        hashMap = new HashMap<>();
        bookInfoList = new ArrayList<>();
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

    public boolean addToCart(int bookId, int quantity) {
        if (!isExistedItem(bookId)) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<Integer, Integer>>() {
            }.getType();
            String data = sharedPreferences.getString(KEY_DATA, null);
            if (data != null) {
                hashMap = gson.fromJson(data, type);
            }
            hashMap.put(bookId, quantity);
            String newData = gson.toJson(hashMap);
            editor.putString(KEY_DATA, newData);
            editor.putInt(KEY_TOTAL_ITEM, sharedPreferences.getInt(KEY_TOTAL_ITEM, 0) + 1);
            editor.commit();
            return true;
        }
        return false;
    }

    public void removeItem(int bookId) {
        hashMap = new HashMap<>();

        String data = sharedPreferences.getString(KEY_DATA, null);
        if (data != null) {
            hashMap = gson.fromJson(data, type);
            hashMap.remove(bookId);
            String newData = gson.toJson(hashMap);
            editor.putString(KEY_DATA, newData);
            if (getTotalItem() > 0)
                editor.putInt(KEY_TOTAL_ITEM, sharedPreferences.getInt(KEY_TOTAL_ITEM, 0) - 1);
            editor.commit();
        }


    }

    public boolean isExistedItem(int bookId) {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<Integer, Integer>>() {
        }.getType();
        String data = sharedPreferences.getString(KEY_DATA, null);
        if (data != null) {
            hashMap = gson.fromJson(data, type);
        }
        if (hashMap.containsKey(bookId))
            return true;
        return false;
    }

    public HashMap<Integer, Integer> getCartInfo() {
        hashMap = new HashMap<>();
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<Integer, Integer>>() {
        }.getType();
        String data = sharedPreferences.getString(KEY_DATA, null);
        if (data != null) {
            hashMap = gson.fromJson(data, type);
        }
        return hashMap;
    }

    public void updateQuantityItem(int bookId, int quantity) {
        hashMap = new HashMap<>();
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<Integer, Integer>>() {
        }.getType();
        String data = sharedPreferences.getString(KEY_DATA, null);
        if (data != null) {
            hashMap = gson.fromJson(data, type);
            hashMap.put(bookId, quantity);
            String newData = gson.toJson(hashMap, type);
            editor.putString(KEY_DATA, newData);
            editor.commit();
        }
    }

    public Cart getCart() {
        Cart cart = new Cart();
        cart.setListBook(getBookInfoList());
        return cart;
    }

    private boolean addBookToCart(BookInfo bookInfo) {
        if (!isExistedItem(bookInfo.getId())) {
            String data = sharedPreferences.getString(KEY_DATA_CART, null);
            if (data != null)
                bookInfoList = gson.fromJson(data, type);
            bookInfoList.add(bookInfo);
            String newData = gson.toJson(bookInfoList);
            editor.putString(KEY_DATA_CART, newData);
            editor.commit();
            return true;
        }
        return false;
    }

    private void deleteBookFromCart(BookInfo info) {
        String data = sharedPreferences.getString(KEY_DATA_CART, null);
        if (data != null) {
            bookInfoList = gson.fromJson(data, type);
            bookInfoList.remove(bookInfoList.indexOf(info));
            String newData = gson.toJson(bookInfoList, type);
            editor.putString(KEY_DATA_CART, newData);
            if (getTotalItem() > 0) {
                editor.putInt(KEY_TOTAL_ITEM, sharedPreferences.getInt(KEY_TOTAL_ITEM, 0) - 1);
            }
            editor.commit();
        }
    }

    private void updateQuantityBook(int bookId, int quantity) {
        String data = sharedPreferences.getString(KEY_DATA_CART, null);
        if (data != null) {
            bookInfoList = gson.fromJson(data, type);
            for (int i = 0; i < bookInfoList.size(); i++) {
                if (bookId == bookInfoList.get(i).getId()) {
                    BookInfo bookInfo = bookInfoList.get(i);
                    bookInfo.setQuantity(quantity);
                    bookInfoList.remove(bookInfoList.get(i));
                    bookInfoList.add(i, bookInfo);
                    String newData = gson.toJson(bookInfoList);
                    editor.putString(KEY_DATA_CART, newData);
                    editor.commit();
                    break;
                }
            }
        }
    }

    public void loadListBookInCart() {
        bookInfoList = new ArrayList<>();
        hashMap = new HashMap<>();
        String data = sharedPreferences.getString(KEY_DATA, null);
        if (data != null) {
            hashMap = gson.fromJson(data, type);
        }
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Iterator<Integer> iterator = hashMap.keySet().iterator();
                while (iterator.hasNext()){
                    try {
                        BookInfo bookInfo = RetrofitManager.getInstance().getBookStoreService().getBookById(iterator.next())
                                .execute().body();
                        bookInfo.setQuantity(hashMap.get(iterator.next()));
                        bookInfoList.add(bookInfo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                setBookInfoList(bookInfoList);
            }
        };
        asyncTask.execute();
    }
    public void setBookInfoList(List<BookInfo> bookInfoList){
        this.bookInfoList = bookInfoList;
    }

    public List<BookInfo> getBookInfoList() {
        return bookInfoList;
    }
}
