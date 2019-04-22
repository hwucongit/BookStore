package com.teamand.bookstore.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import com.teamand.bookstore.R;
import com.teamand.bookstore.api.BookStoreService;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.manager.WishListManager;
import com.teamand.bookstore.model.BookInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvBookName, tvBookPrice, tvDiscount, tvDescription, tvQuantity, tvToExpand, tvExpand;
    private boolean isExpended;
    private Button btnAddQuantity, btnSubQuantity;
    private ImageButton imbFavorite;
    private BookInfo bookInfo;
    private int idBook;
    private Toolbar toolbar;
    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new SessionManager(this);
        tvBookName = findViewById(R.id.tv_book_name);
        tvBookPrice = findViewById(R.id.tv_book_price);
        tvDiscount = findViewById(R.id.tv_discount);
        tvDescription = findViewById(R.id.tv_description);
        tvQuantity = findViewById(R.id.tv_quantity);
        tvExpand = findViewById(R.id.tv_expand);
        tvToExpand = findViewById(R.id.tv_to_expand);
        isExpended = false;
        tvToExpand.setOnClickListener(this);
        imbFavorite = findViewById(R.id.imb_favorite);
        findViewById(R.id.btn_add_quantity).setOnClickListener(this);
        findViewById(R.id.btn_sub_quantity).setOnClickListener(this);
        findViewById(R.id.imb_favorite).setOnClickListener(this);
        loadPropertyBook();

    }

    private void loadPropertyBook() {
        idBook = getIntent().getIntExtra("bookId", 0);
        BookStoreService bookStoreService = new RetrofitManager().getBookStoreService();
        bookStoreService.getBookById(idBook).enqueue(new Callback<BookInfo>() {
            @Override
            public void onResponse(Call<BookInfo> call, Response<BookInfo> response) {
                if (response.isSuccessful()) {
                    BookInfo bookInfo = response.body();
                    if (bookInfo.getDiscount() > 0) {
                        tvDiscount.setVisibility(View.VISIBLE);
                        tvDiscount.setText(bookInfo.getDiscount() + " %");
                    }
                    if (bookInfo.getDescription() != null)
                        tvDescription.setText(bookInfo.getDescription());
                    tvBookName.setText(bookInfo.getName());
                    tvBookPrice.setText(bookInfo.getPrice() + " VNĐ");

                    WishListManager manager = new WishListManager(getApplicationContext());
                    if (manager.isLikedBook(idBook)) {
                        imbFavorite.setBackgroundResource(R.drawable.ic_favorite_red_700_48dp);
                    } else {
                        imbFavorite.setBackgroundResource(R.drawable.btn_favorite_unselected);
                    }

                }
            }

            @Override
            public void onFailure(Call<BookInfo> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_to_expand:
                if (isExpended == false) {
                    tvDescription.setVisibility(View.VISIBLE);
                    tvExpand.setText("-");
                    isExpended = true;
                } else {
                    tvDescription.setVisibility(View.GONE);
                    tvExpand.setText("+");
                    isExpended = false;
                }
                break;
            case R.id.btn_add_quantity:
                tvQuantity.setText(Integer.parseInt(tvQuantity.getText().toString()) + 1 + "");
                break;
            case R.id.btn_sub_quantity:
                if (Integer.parseInt(tvQuantity.getText().toString()) > 1)
                    tvQuantity.setText(Integer.parseInt(tvQuantity.getText().toString()) - 1 + "");
                break;
            case R.id.imb_favorite:
                handleButtonFavorite();
                break;
            default:
                break;
        }
    }

    private void handleButtonFavorite() {
            WishListManager manager = new WishListManager(this);
            BookStoreService bookStoreService = new RetrofitManager().getBookStoreService();
            if (!manager.isLikedBook(idBook)) {
                manager.addToWishList(idBook);
                imbFavorite.setBackgroundResource(R.drawable.ic_favorite_red_700_48dp);
                if(session.isLoggedIn()) {
                    bookStoreService.addToWishlist(session.getCurrentUser().getId(),idBook).enqueue(
                            new Callback<List<Boolean>>() {
                        @Override
                        public void onResponse(Call<List<Boolean>> call, Response<List<Boolean>> response) {
                            if(response.isSuccessful()){
                                imbFavorite.setBackgroundResource(R.drawable.ic_favorite_red_700_48dp);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Boolean>> call, Throwable t) {

                        }
                    });
                }
            } else {
                manager.deleteFromWishList(idBook);
                imbFavorite.setBackgroundResource(R.drawable.btn_favorite_unselected);
            }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
