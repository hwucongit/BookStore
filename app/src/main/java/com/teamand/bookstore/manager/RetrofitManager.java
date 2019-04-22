package com.teamand.bookstore.manager;

import com.teamand.bookstore.api.BookStoreService;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.model.Account;
import com.teamand.bookstore.model.User;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitManager {
    private Retrofit retrofit;
    public static BookStoreService bookStoreService;
    public void initRetrofit(String apiURL){
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(apiURL)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
    public BookStoreService getBookStoreService(){
        if(bookStoreService == null) {
            initRetrofit(Constants.apiBookStore);
            bookStoreService = retrofit.create(BookStoreService.class);
        }
        return bookStoreService;
    }
}
