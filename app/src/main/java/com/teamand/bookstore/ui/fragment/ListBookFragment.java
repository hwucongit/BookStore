package com.teamand.bookstore.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamand.bookstore.R;
import com.teamand.bookstore.adapter.BookAdapter;
import com.teamand.bookstore.api.BookStoreService;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.model.BookInfo;
import com.teamand.bookstore.ui.activity.ListBookActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListBookFragment extends Fragment {
    private RecyclerView rvListBook;
    private List<BookInfo> bookList;
    private RetrofitManager retrofitManager;
    private BookStoreService bookStoreService;
    private ListBookActivity listBookActivity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_book,container,false);
        init(view);
        return view;
    }

    private void init(View view) {
        rvListBook = view.findViewById(R.id.rv_list_book);
        bookList = new ArrayList<>();
        retrofitManager = new RetrofitManager();
        bookStoreService = retrofitManager.getBookStoreService();
        loadBook();
    }
    private void loadBook(){
        String key = listBookActivity.getIntent().getStringExtra("bookName");
        bookStoreService.getBookByName(key).enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                if(response.isSuccessful()) {
                    bookList = response.body();
                    Helper.showToast(listBookActivity.getApplicationContext(),bookList.get(0).getName());
                }
                else
                    Helper.showToast(getContext(),"hihi");
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ListBookActivity){
            this.listBookActivity = (ListBookActivity) context;
        }
    }
}
