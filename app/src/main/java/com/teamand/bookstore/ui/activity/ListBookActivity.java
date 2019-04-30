package com.teamand.bookstore.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.teamand.bookstore.R;
import com.teamand.bookstore.adapter.BookInfoAdapter;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.CartManager;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.model.BookInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListBookActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private List<BookInfo> bookInfoList;
    private RetrofitManager retrofitManager;
    private RecyclerView rvListBook;
    private TextView tvNotice, tvTotalItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book);
        init();
    }


    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvNotice = findViewById(R.id.tv_notice);
        bookInfoList = new ArrayList<>();
        rvListBook = findViewById(R.id.rv_list_book);
        rvListBook.setHasFixedSize(true);
        rvListBook.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
        tvTotalItem = findViewById(R.id.tv_quantity_book);
        tvTotalItem.setText(CartManager.getInstance(getApplicationContext()).getTotalItem()+"");
        loadBook();
    }

    private void loadBook() {
        String type = getIntent().getStringExtra("typeSearch");
        if (type.equals("byBookName")) {
            loadBookByBookName();
        } else if (type.equals("byCategoryId")) {
            loadBookByCategory();
        } else if (type.equals("byPublisherId")) {
            loadBookByPublisher();
        }

    }

    private void loadBookByBookName(){
        String key = getIntent().getStringExtra("keySearch");
        RetrofitManager.getInstance().getBookStoreService().getBookByName(key)
                .enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                if (response.isSuccessful()) {
                    bookInfoList = response.body();
                    if (bookInfoList.size() == 0)
                        tvNotice.setVisibility(View.VISIBLE);
                    else {
                        BookInfoAdapter adapter = new BookInfoAdapter(bookInfoList, Constants.ITEM_BOOK_TYPE_HOR);
                        rvListBook.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Helper.showToast(getApplicationContext(), t.toString());
            }
        });
    }

    private void loadBookByCategory() {

        int key = getIntent().getIntExtra("keySearch", 0);
        RetrofitManager.getInstance().getBookStoreService().searchBookByCategory(key)
                .enqueue(new Callback<List<BookInfo>>() {
                    @Override
                    public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                        if (response.isSuccessful()) {
                            bookInfoList = response.body();
                            tvNotice.setText(getString(R.string.book_category) + " : " + getIntent().getStringExtra("title"));
                            tvNotice.setVisibility(View.VISIBLE);
                            BookInfoAdapter adapter = new BookInfoAdapter(bookInfoList, Constants.ITEM_BOOK_TYPE_HOR);
                            rvListBook.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BookInfo>> call, Throwable t) {

                    }
                });
    }

    private void loadBookByPublisher() {
        int key = getIntent().getIntExtra("keySearch", 0);
        RetrofitManager.getInstance().getBookStoreService().searchBookByPublisher(key)
                .enqueue(new Callback<List<BookInfo>>() {
                    @Override
                    public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                        if (response.isSuccessful()) {
                            bookInfoList = response.body();
                            tvNotice.setText(getIntent().getStringExtra("title"));
                            tvNotice.setVisibility(View.VISIBLE);
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
    protected void onResume() {
        super.onResume();
        tvTotalItem.setText(CartManager.getInstance(this).getTotalItem()+"");
    }
}
