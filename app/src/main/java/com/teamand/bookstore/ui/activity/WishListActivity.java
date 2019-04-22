package com.teamand.bookstore.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.teamand.bookstore.R;
import com.teamand.bookstore.adapter.BookInfoAdapter;
import com.teamand.bookstore.api.BookStoreService;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.manager.WishListManager;
import com.teamand.bookstore.model.BookInfo;

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
    private SessionManager session;
    private BookInfoAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);
        init();
    }

    private void init() {
        rvListBook = findViewById(R.id.rv_list_book);
        bookInfoList = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.wishlist));
        rvListBook.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rvListBook.setHasFixedSize(true);
        bookStoreService= new RetrofitManager().getBookStoreService();
        loadWishList();
    }

    private void loadWishList(){
        session = new SessionManager(this);
        if(session.isLoggedIn()){
            int userId = session.getCurrentUser().getId();
            loadWishListUser(userId);
        }else {
            loadWishListLocal();
        }
    }
    private void loadWishListLocal(){
        bookInfoList.clear();
        WishListManager wishListManager = new WishListManager(this);
        List<Integer> listBookId = wishListManager.getWishList();
        if (listBookId!= null) {
            for (Integer id : listBookId) {
                bookStoreService.getBookById(id).enqueue(new Callback<BookInfo>() {
                    @Override
                    public void onResponse(Call<BookInfo> call, Response<BookInfo> response) {
                        if (response.isSuccessful()) {
                            bookInfoList.add(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<BookInfo> call, Throwable t) {

                    }
                });
            }
        }
        adapter = new BookInfoAdapter(bookInfoList,Constants.ITEM_BOOK_TYPE_HOR);
        rvListBook.setAdapter(adapter);

    }
    private void loadWishListUser(int userId){
        bookStoreService.getWishlist(userId).enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                if(response.isSuccessful()){
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        loadWishList();
    }
}
