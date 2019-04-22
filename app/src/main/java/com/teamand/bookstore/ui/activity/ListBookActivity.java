package com.teamand.bookstore.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.teamand.bookstore.R;
import com.teamand.bookstore.adapter.BookInfoAdapter;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.model.BookInfo;
import com.teamand.bookstore.ui.fragment.ListBookFragment;

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
    private TextView tvNotice;

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
                                                LinearLayoutManager.VERTICAL,false));
        loadBook();
    }

    private void loadBook() {
        retrofitManager = new RetrofitManager();
        String type = getIntent().getStringExtra("typeSearch");
        String key = getIntent().getStringExtra("keySearch");
        if (type.equals("byBookName")) {


            retrofitManager.getBookStoreService().getBookByName(key).enqueue(new Callback<List<BookInfo>>() {
                @Override
                public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                    if (response.isSuccessful()){
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
                    Helper.showToast(getApplicationContext(),t.toString());
                }
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
