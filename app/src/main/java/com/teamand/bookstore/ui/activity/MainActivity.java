package com.teamand.bookstore.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.teamand.bookstore.R;
import com.teamand.bookstore.adapter.BookAdapter;
import com.teamand.bookstore.adapter.BookInfoAdapter;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.CartManager;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.manager.WishListManager;
import com.teamand.bookstore.model.Book;
import com.teamand.bookstore.model.BookInfo;
import com.teamand.bookstore.model.Category;
import com.teamand.bookstore.model.Publisher;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private RecyclerView rv_book_recently, rv_book_discount;
    private List<BookInfo> bookRecentlyList;
    private List<BookInfo> bookDiscountList;
    private Button btnNavUser;
    private ImageButton btnSearch, btnWishList, btnCart;
    private TextView tvTotalItem;
    private SessionManager session;
    private RetrofitManager retrofitManager;
    private DrawerLayout drawer;
    private WishListManager wishListManager;
    private List<Category> categoryList;
    private List<Publisher> publisherList;
    private NavigationView navigationView;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        btnNavUser = headerLayout.findViewById(R.id.btn_nav_user);
        btnSearch = findViewById(R.id.imb_search);
        btnSearch.setOnClickListener(this);
        btnNavUser.setOnClickListener(this);
        tvTotalItem = findViewById(R.id.tv_quantity_book);
        findViewById(R.id.imb_wish_lish).setOnClickListener(this);
        findViewById(R.id.imb_cart).setOnClickListener(this);
        loadUser();

        rv_book_recently = findViewById(R.id.rv_recently_book);
        rv_book_discount = findViewById(R.id.rv_discount_book);
        bookRecentlyList = new ArrayList<>();
        bookDiscountList = new ArrayList<>();
        rv_book_recently.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_book_discount.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        loadCategory();
        loadPublisher();
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
                        loadBook();
                    }
                }

                @Override
                public void onFailure(Call<List<BookInfo>> call, Throwable t) {

                }
            });
        }else {
            loadBook();
        }
    }
    private void loadBook(){
        RetrofitManager.getInstance().getBookStoreService().getBookRecentlyAdd(10).enqueue(new Callback<List<BookInfo>>() {
            @Override
            public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                if(response.isSuccessful()) {
                    bookRecentlyList = response.body();
                    BookInfoAdapter adapter = new BookInfoAdapter(bookRecentlyList, Constants.ITEM_BOOK_TYPE_VER);
                    rv_book_recently.setAdapter(adapter);
                    loadBookDiscount();
                }
            }

            @Override
            public void onFailure(Call<List<BookInfo>> call, Throwable t) {
                Helper.showToast(getApplicationContext(),t.toString());
            }
        });

    }

    private void loadBookDiscount(){
        RetrofitManager.getInstance().getBookStoreService().getDiscountBook(10).enqueue(
                new Callback<List<BookInfo>>() {
                    @Override
                    public void onResponse(Call<List<BookInfo>> call, Response<List<BookInfo>> response) {
                        if(response.isSuccessful()){
                            bookDiscountList = response.body();
                            BookInfoAdapter adapter = new BookInfoAdapter(bookDiscountList, Constants.ITEM_BOOK_TYPE_VER);
                            rv_book_discount.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BookInfo>> call, Throwable t) {

                    }
                }
        );
    }
    private void loadUser() {
        session = new SessionManager(this);
        if (session.isLoggedIn()) {
            btnNavUser.setText(session.getCurrentUser().getName() + " | " + getString(R.string.my_account));
        } else {
            btnNavUser.setText(getString(R.string.guest_loggin));
        }
    }

    private void loadCategory(){

        RetrofitManager.getInstance().getBookStoreService().getAllCategory().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    categoryList = response.body();
                    loadMenuCategory();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }
    private void loadMenuCategory(){
        Menu menuCategory = navigationView.getMenu().getItem(1).getSubMenu();
        for (int i = 0; i < categoryList.size(); i++) {
            menuCategory.add(R.id.category,categoryList.get(i).getId(),Menu.NONE,categoryList.get(i).getName())
                    .setIcon(R.drawable.ic_book_teal_500_48dp);
        }
    }

    private void loadPublisher(){
        RetrofitManager.getInstance().getBookStoreService().getAllPublisher()
                .enqueue(new Callback<List<Publisher>>() {
            @Override
            public void onResponse(Call<List<Publisher>> call, Response<List<Publisher>> response) {
                if(response.isSuccessful()){
                    publisherList = response.body();
                    loadMenuPublisher();
                }
            }

            @Override
            public void onFailure(Call<List<Publisher>> call, Throwable t) {

            }
        });
    }

    private void loadMenuPublisher() {
        Menu publisherMenu = navigationView.getMenu().getItem(2).getSubMenu();
        for (int i = 0; i < publisherList.size(); i++) {
            publisherMenu.add(R.id.publisher,publisherList.get(i).getId(),i,publisherList.get(i).getName())
                    .setIcon(R.drawable.ic_book_teal_500_48dp);
        }
    }

    public void openLoginActivity() {
        drawer.closeDrawers();
        startActivity(new Intent(this, LoginActivity.class));
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int groupId = item.getGroupId();
        int id = item.getItemId();

        if(groupId == R.id.category){
            openListBookActivity("byCategoryId", id, item.getTitle().toString());
        }else if(groupId == R.id.publisher){
            openListBookActivity("byPublisherId", id, item.getTitle().toString());
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void openListBookActivity(String typeSearch, int keySearch, String title){
        Intent intent = new Intent(this,ListBookActivity.class);
        intent.putExtra("typeSearch",typeSearch);
        intent.putExtra("keySearch",keySearch);
        intent.putExtra("title", title);
        startActivity(intent);
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
            case R.id.imb_cart:
                startActivity(new Intent(this, CartActivity.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        tvTotalItem.setText(CartManager.getInstance(getApplicationContext()).getTotalItem()+"");
    }
}
