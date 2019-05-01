package com.teamand.bookstore.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.teamand.bookstore.R;
import com.teamand.bookstore.adapter.BookInCartAdapter;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.CartManager;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.model.BookInfo;
import com.teamand.bookstore.model.Cart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private RecyclerView rvListBook;
    private List<BookInfo> bookInfoList;
    private HashMap<Integer,Integer> hashMap;
    private CartManager cartManager;
    private TextView tvTotalPrice;
    private BookInCartAdapter adapter;
    private int total;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rvListBook = findViewById(R.id.rv_list_book);
        rvListBook.setHasFixedSize(true);
        rvListBook.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        findViewById(R.id.btn_continue_shop).setOnClickListener(this);
        findViewById(R.id.btn_checkout).setOnClickListener(this);
        findViewById(R.id.imb_wish_lish).setOnClickListener(this);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        cartManager = CartManager.getInstance(this);
        bookInfoList = new ArrayList<>();
        hashMap = new HashMap<>();
        total = 0;
        loadCart();
    }

    private void loadCart(){
        bookInfoList = cartManager.getListBookInCart();
        adapter = new BookInCartAdapter(getApplicationContext(),bookInfoList,hashMap);
        loadTotalPrice();
        adapter.setIbook(new BookInCartAdapter.IBook() {
            @Override
            public void onChangeTotalPrice(int price) {
                tvTotalPrice.setText(price + " VNĐ");
            }
        });
        rvListBook.setAdapter(adapter);
    }
    private void loadTotalPrice(){
        for (int i = 0; i < bookInfoList.size(); i++) {
            BookInfo bookInfo = bookInfoList.get(i);
            if(bookInfo.getDiscount() > 0){
                total += (bookInfo.getPrice() - bookInfo.getPrice()*bookInfo.getDiscount()/100)
                        * bookInfo.getQuantity();
            }else {
                total += bookInfoList.get(i).getPrice() * bookInfo.getQuantity();
            }
        }
        tvTotalPrice.setText(total+ " VNĐ");

    }
    private void openCheckoutActivity(){
        bookInfoList = cartManager.getListBookInCart();
        if(bookInfoList.size() > 0) {
            Intent intent = new Intent(this, CheckoutActivity.class);
            intent.putExtra("totalPrice", tvTotalPrice.getText().toString().replace(" VNĐ", ""));

            Cart cart = new Cart();
            cart.setListBook(bookInfoList);
            String data = new Gson().toJson(cart);
            intent.putExtra("cart", data);
            startActivity(intent);
        }else {
            Helper.showToast(getApplicationContext(),"Không có sách nào trong giỏ hàng");
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue_shop:
                finish();
                break;
            case R.id.imb_wish_lish:
                startActivity(new Intent(this, WishListActivity.class));
                finish();
                break;
            case R.id.btn_checkout:
                openCheckoutActivity();
                break;
            default:
                break;
        }
    }
}
