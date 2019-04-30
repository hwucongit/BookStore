package com.teamand.bookstore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.paypal.android.sdk.payments.ShippingAddress;
import com.teamand.bookstore.R;
import com.teamand.bookstore.manager.SessionManager;

public class ProfileUserActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton imbClose;
    private SessionManager sessionManager;
    private ImageView imvAvatar;
    private TextView tvUsername, tvEmail;
    private Button btnOrder, btnShipAddress, btnChangeProfile, btnLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        init();
    }

    private void init() {
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        imbClose = findViewById(R.id.imb_close);
        btnLogout = findViewById(R.id.btn_logout);
        imbClose.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        findViewById(R.id.btn_order).setOnClickListener(this);
        findViewById(R.id.btn_shipping_address).setOnClickListener(this);
        findViewById(R.id.btn_change_profile).setOnClickListener(this);

        sessionManager = new SessionManager(getApplicationContext());
        tvUsername.setText(sessionManager.getCurrentUser().getName());
        tvEmail.setText(sessionManager.getCurrentUser().getEmail());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imb_close:
                finish();
                break;
            case R.id.btn_logout:
                logoutUser();
                break;
            case R.id.btn_shipping_address:
                startActivity(new Intent(this, ShippingAddressActivity.class));
                break;
            case R.id.btn_order:
                startActivity(new Intent(this,OrdersActivity.class));
                break;
            case R.id.btn_change_profile:
                startActivity(new Intent(this, SettingAccountActivity.class));
                break;
            default:
                break;
        }
    }

    private void logoutUser() {
        sessionManager.logOutUser();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
