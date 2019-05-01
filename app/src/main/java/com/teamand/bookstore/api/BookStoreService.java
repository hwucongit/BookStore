package com.teamand.bookstore.api;

import com.teamand.bookstore.model.Account;
import com.teamand.bookstore.model.Book;
import com.teamand.bookstore.model.BookInfo;
import com.teamand.bookstore.model.Cart;
import com.teamand.bookstore.model.Category;
import com.teamand.bookstore.model.Order;
import com.teamand.bookstore.model.Publisher;
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

    @GET("bookstore/getRecentBook")
    Call<List<BookInfo>> getBookRecentlyAdd(@Query("number") int number);

    @GET("bookstore/getBookById")
    Call<BookInfo> getBookById(@Query("id") int id);

    @GET("bookstore/searchBookByCategory")
    Call<List<BookInfo>> searchBookByCategory(@Query("categoryId") int categoryId);

    @GET("bookstore/searchBookByPublisher")
    Call<List<BookInfo>> searchBookByPublisher(@Query("publisherId") int publisherId);

    @GET("bookstore/getWishlist")
    Call<List<BookInfo>> getWishlist(@Query("userId") int userId);

    @GET("bookstore/addToWishlist")
    Call<Boolean> addToWishlist(@Query("userId") int userId, @Query("bookId") int bookId);

    @GET("bookstore/removeFromWishlist")
    Call<Boolean> removeFromWishlist(@Query("userId") int userId, @Query("bookId") int bookId);

    @GET("bookstore/getAllCategory")
    Call<List<Category>> getAllCategory();

    @GET("bookstore/getAllPublisher")
    Call<List<Publisher>> getAllPublisher();

    @POST("bookstore/addOrder")
    Call<Boolean> addOrder(@Body Order order);

    @GET("bookstore/getDiscountBook")
    Call<List<BookInfo>> getDiscountBook(@Query("number") int number);

    @GET("bookstore/getOrderByUserId")
    Call<List<Order>> getOrderByUserId(@Query("userId") int userId);

    @GET("bookstore/getUserById")
    Call<User> getUserById(@Query("userId") int userId);

    @POST("bookstore/changeInfo")
    Call<Boolean> changeInfo(@Body User user);
}
