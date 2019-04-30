package com.teamand.bookstore.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.teamand.bookstore.R;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;

public class ShippingAddressActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText edtFullName, edtPhoneNumber, edtNumber, edtDistrict, edtCity, edtZipcode;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.shipping_address);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtFullName = findViewById(R.id.edt_full_name);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtNumber = findViewById(R.id.edt_number);
        edtDistrict = findViewById(R.id.edt_district);
        edtCity = findViewById(R.id.edt_city);
        edtZipcode = findViewById(R.id.edt_zipcode);
        sessionManager = new SessionManager(this);
        loadShippingAddress();
    }

    private void loadShippingAddress() {
        if(sessionManager.isLoggedIn()){

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
