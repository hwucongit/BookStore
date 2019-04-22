package com.teamand.bookstore.api;

import com.teamand.bookstore.model.Account;
import com.teamand.bookstore.model.Book;
import com.teamand.bookstore.model.BookInfo;
import com.teamand.bookstore.model.User;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface BookStoreService {
    @POST("bookstore/login")
    Call<User> doLogin(@Body Account account);

    @POST("bookstore/register")
    Call<User> register(@Body User user);

    @GET("bookstore/searchBookByName")
    Call<List<BookInfo>> getBookByName(@Query("bookName") String bookName);

    @GET("bookstore/getBookRecentlyAdd")
    Call<List<BookInfo>> getBookRecentlyAdd();

    @GET("bookstore/getBookById")
    Call<BookInfo> getBookById(@Query("id") int id);

    @GET("bookstore/getWishlist")
    Call<List<BookInfo>> getWishlist(@Query("userId") int userId);

    @GET("bookstore/addToWishlist")
    Call<List<Boolean>> addToWishlist(@Query("userId") int userId, @Query("bookId") int bookId);

}
