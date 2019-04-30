package com.teamand.bookstore.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.teamand.bookstore.R;
import com.teamand.bookstore.adapter.BookInfoAdapter;
import com.teamand.bookstore.api.BookStoreService;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.manager.CartManager;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.manager.WishListManager;
import com.teamand.bookstore.model.BookInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListActivity extends AppCompatActivity {
    private RecyclerView rvListBook;
    private List<BookInfo> bookInfoList;
    private Toolbar toolbar;
    private BookStoreService bookStoreService;
    private int currentUserId;
    private WishListManager wishListManager;
    private SessionManager session;
    private BookInfoAdapter adapter;
    private List<Integer> listBookId;
    private TextView tvTotalItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        init();
    }

    private void init() {
        wishListManager = new WishListManager(this);
        rvListBook = findViewById(R.id.rv_list_book);
        bookInfoList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.wishlist));
        rvListBook.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvListBook.setHasFixedSize(true);
        bookStoreService = new RetrofitManager().getBookStoreService();
        tvTotalItem = findViewById(R.id.tv_quantity_book);
        tvTotalItem.setText(CartManager.getInstance(this).getTotalItem()+"");
        listBookId = wishListManager.getWishList();
    }

    private void loadWishList() {
        session = new SessionManager(this);
        if (session.isLoggedIn()) {
            int userId = session.getCurrentUser().getId();
            loadWishListUser(userId);
        } else {
            loadWishListInternal();
        }
    }

    private void loadWishListInternal() {
        bookInfoList.clear();
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Void doInBackground(Object... objects) {
                if (listBookId != null) {
                    for (int j = 0; j < listBookId.size(); j++) {
                        try {
                            BookInfo bookInfo = bookStoreService.getBookById(listBookId.get(j)).execute().body();
                            bookInfoList.add(bookInfo);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                adapter = new BookInfoAdapter(bookInfoList, Constants.ITEM_BOOK_TYPE_HOR);
                rvListBook.setAdapter(adapter);
            }
        };
        asyncTask.execute();
    }

    private void loadWishListUser(int userId) {
        bookInfoList.clear();
        bookStoreService.getWishlist(userId).enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                if (response.isSuccessful()) {
                    bookInfoList = response.body();
                    BookInfoAdapter adapter = new BookInfoAdapter(bookInfoList, Constants.ITEM_BOOK_TYPE_HOR);
                    rvListBook.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWishList();
        Log.d("Wish", "on resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Wish", "on pause");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
