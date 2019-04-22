package com.teamand.bookstore.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.teamand.bookstore.R;
import com.teamand.bookstore.adapter.BookAdapter;
import com.teamand.bookstore.adapter.BookInfoAdapter;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.manager.WishListManager;
import com.teamand.bookstore.model.Book;
import com.teamand.bookstore.model.BookInfo;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private RecyclerView rv_book_recently, rv_book_best_sell;
    private List<BookInfo> bookRecentlyList;
    private TextView tvQuote;
    private Button btnNavUser;
    private ImageButton btnSearch, btnWishList, btnCart;
    private SessionManager session;
    private RetrofitManager retrofitManager;
    private DrawerLayout drawer;
    private WishListManager wishListManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        btnNavUser = headerLayout.findViewById(R.id.btn_nav_user);
        btnSearch = findViewById(R.id.imb_search);
        btnSearch.setOnClickListener(this);
        btnNavUser.setOnClickListener(this);
        findViewById(R.id.imb_wish_lish).setOnClickListener(this);
        findViewById(R.id.imb_cart).setOnClickListener(this);
        loadUser();

        rv_book_recently = findViewById(R.id.rv_recently_book);
        rv_book_best_sell = findViewById(R.id.rv_best_sell_book);
        bookRecentlyList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_book_recently.setLayoutManager(linearLayoutManager);
        loadBook();
        writeWishListToSharePreference();

    }

    private void writeWishListToSharePreference(){
        wishListManager = new WishListManager(this);
        if(session.isLoggedIn()) {
            wishListManager.clearWishList();
            RetrofitManager retrofitManager = new RetrofitManager();
            retrofitManager.getBookStoreService().getWishlist(session.getCurrentUser().getId()).enqueue(new Callback<List<BookInfo>>() {
                @Override
                public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                    if (response.isSuccessful()) {
                        List<BookInfo> bookInfoList = response.body();
                        if (bookInfoList != null) {
                            for (BookInfo bookInfo : bookInfoList) {
                                wishListManager.addToWishList(bookInfo.getId());
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<BookInfo>> call, Throwable t) {

                }
            });
        }
    }
    private void loadBook(){
        RetrofitManager retrofitManager = new RetrofitManager();
        retrofitManager.getBookStoreService().getBookRecentlyAdd().enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                if(response.isSuccessful()) {
                    bookRecentlyList = response.body();
                    BookInfoAdapter adapter = new BookInfoAdapter(bookRecentlyList, Constants.ITEM_BOOK_TYPE_VER);
                    rv_book_recently.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Helper.showToast(getApplicationContext(),t.toString());
            }
        });
    }

    private void loadUser() {
        session = new SessionManager(this);
        if (session.isLoggedIn()) {
            btnNavUser.setText(session.getCurrentUser().getName() + " | " + getString(R.string.my_account));
        } else {
            btnNavUser.setText(getString(R.string.guest_loggin));
        }
    }


    public void openLoginActivity() {
        drawer.closeDrawers();
        startActivity(new Intent(this, LoginActivity.class));
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_nav_user:
                if (!session.isLoggedIn()) {
                    openLoginActivity();
                } else {
                    openProfileActivity();
                }
                break;
            case R.id.imb_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.imb_wish_lish:
                startActivity(new Intent(this,WishListActivity.class));
                break;
            default:
                break;
        }
    }

    private void openProfileActivity() {
        drawer.closeDrawers();
        Intent intent = new Intent(this,ProfileUserActivity.class);
        startActivity(intent);
    }
}
