package com.teamand.bookstore.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.teamand.bookstore.R;
import com.teamand.bookstore.adapter.OrderAdapter;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.model.Order;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView rv_list_order;
    private List<Order> orderList;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.orders));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rv_list_order = findViewById(R.id.rv_list_order);
        rv_list_order.setLayoutManager(
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        rv_list_order.setHasFixedSize(true);
        sessionManager = new SessionManager(this);
        loadOrder();
    }
    private void loadOrder(){
        RetrofitManager.getInstance().getBookStoreService().getOrderByUserId(sessionManager.getCurrentUser().getId())
                .enqueue(new Callback<List<Order>>() {
                    @Override
                    public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                        orderList = response.body();
                        Collections.sort(orderList, new Comparator<Order>() {
                            @Override
                            public int compare(Order o1, Order o2) {
                                return o2.getId() - o1.getId();
                            }
                        });
                        OrderAdapter adapter = new OrderAdapter(getApplicationContext(),orderList);
                        rv_list_order.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<List<Order>> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
