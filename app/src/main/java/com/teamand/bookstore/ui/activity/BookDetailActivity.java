package com.teamand.bookstore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.teamand.bookstore.R;
import com.teamand.bookstore.api.BookStoreService;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.CartManager;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.manager.WishListManager;
import com.teamand.bookstore.model.BookInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvBookName, tvBookPrice,
            tvDiscount, tvDescription, tvQuantity, tvToExpand, tvExpand, tvTotalItem, tvOriginPrice;
    private boolean isExpended;
    private Button btnAddQuantity, btnSubQuantity, btnAddToCart, btnBuyNow;
    private ImageButton imbFavorite;
    private ImageView ivThumbnail;
    private BookInfo bookInfo;
    private int idBook;
    private Toolbar toolbar;
    private SessionManager session;
    private CartManager cartManager;

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
        tvTotalItem = findViewById(R.id.tv_quantity_book);
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
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnBuyNow = findViewById(R.id.btn_buy_now);
        ivThumbnail = findViewById(R.id.imv_thumbnail);
        tvOriginPrice = findViewById(R.id.tv_book_price_origin);
        findViewById(R.id.btn_add_quantity).setOnClickListener(this);
        findViewById(R.id.btn_sub_quantity).setOnClickListener(this);
        findViewById(R.id.imb_favorite).setOnClickListener(this);
        btnAddToCart.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        cartManager = CartManager.getInstance(this);
        loadPropertyBook();
        loadTotalItem();

    }

    private void loadTotalItem(){
        int total = cartManager.getTotalItem();
        tvTotalItem.setText(total + "");
    }
    private void loadPropertyBook() {
        idBook = getIntent().getIntExtra("bookId", 0);
        BookStoreService bookStoreService = new RetrofitManager().getBookStoreService();
        bookStoreService.getBookById(idBook).enqueue(new Callback<BookInfo>() {
            @Override
            public void onResponse(Call<BookInfo> call, Response<BookInfo> response) {
                if (response.isSuccessful()) {
                    bookInfo = response.body();
                    if (bookInfo.getDiscount() > 0) {
                        tvDiscount.setVisibility(View.VISIBLE);
                        tvDiscount.setText(bookInfo.getDiscount() + " %");
                        tvOriginPrice.setText(bookInfo.getPrice() + " VNĐ");
                        tvBookPrice.setText(bookInfo.getPrice() - bookInfo.getPrice()*bookInfo.getDiscount()/100 +" VNĐ");
                    } else {
                        tvBookPrice.setText(bookInfo.getPrice() + " VNĐ");
                        tvOriginPrice.setVisibility(View.GONE);
                    }
                    if (bookInfo.getDescription() != null)
                        tvDescription.setText(bookInfo.getDescription());
                    tvBookName.setText(bookInfo.getName());
                    if(bookInfo.getImgUrl() != null){
                        String url = Constants.urlImage + bookInfo.getImgUrl();
                        Picasso.with(getApplicationContext())
                                .load(url)
                                .placeholder(R.drawable.logo_book_default)
                                .into(ivThumbnail);
                    }
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
            case R.id.btn_add_to_cart:
                addToCart();
                break;
            case R.id.btn_buy_now:
                buyNow();
                break;
            default:
                break;
        }
    }

    private void buyNow() {
        btnBuyNow.setVisibility(View.GONE);
        findViewById(R.id.pgb_buy_now).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_buy_now).setVisibility(View.GONE);
        boolean resultAdd = cartManager.addToCart(bookInfo.getId(),Integer.valueOf(tvQuantity.getText().toString()));
        btnBuyNow.setVisibility(View.VISIBLE);
        findViewById(R.id.pgb_buy_now).setVisibility(View.GONE);
        if(resultAdd == true){
            startActivity(new Intent(this, CartActivity.class));
        }else {
            Helper.showToast(getApplicationContext(),"Đã tồn tại sách trong giỏ hàng");
        }
        tvTotalItem.setText(cartManager.getTotalItem()+"");

    }

    private void addToCart(){
        btnAddToCart.setVisibility(View.GONE);
        findViewById(R.id.pgb_add_to_cart).setVisibility(View.VISIBLE);
        boolean resultAdd = cartManager.addToCart(bookInfo.getId(),Integer.valueOf(tvQuantity.getText().toString()));
        findViewById(R.id.pgb_add_to_cart).setVisibility(View.GONE);
        btnAddToCart.setVisibility(View.VISIBLE);
        if(resultAdd == true){
            Helper.showToast(getApplicationContext(),"Đã thêm vào giỏ hàng");
        }else {
            Helper.showToast(getApplicationContext(),"Đã tồn tại sách trong giỏ hàng");
        }
        tvTotalItem.setText(cartManager.getTotalItem()+"");
    }
    private void handleButtonFavorite() {
            WishListManager manager = new WishListManager(this);
            BookStoreService bookStoreService = new RetrofitManager().getBookStoreService();
            if (!manager.isLikedBook(idBook)) {
                manager.addToWishList(idBook);
                imbFavorite.setBackgroundResource(R.drawable.ic_favorite_red_700_48dp);
                if(session.isLoggedIn()) {
                    bookStoreService.addToWishlist(session.getCurrentUser().getId(),idBook).enqueue(
                            new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.isSuccessful()){
                                imbFavorite.setBackgroundResource(R.drawable.ic_favorite_red_700_48dp);
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });
                }
            } else {
                manager.deleteFromWishList(idBook);
                if(session.isLoggedIn()){
                    bookStoreService.removeFromWishlist(session.getCurrentUser().getId(),idBook)
                            .enqueue(new Callback<Boolean>() {
                                @Override
                                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                    if(response.isSuccessful()){
                                        imbFavorite.setBackgroundResource(R.drawable.btn_favorite_unselected);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Boolean> call, Throwable t) {

                                }
                            });
                }
                imbFavorite.setBackgroundResource(R.drawable.btn_favorite_unselected);
            }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
