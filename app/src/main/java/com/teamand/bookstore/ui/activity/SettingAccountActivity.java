package com.teamand.bookstore.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.teamand.bookstore.R;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.model.Account;
import com.teamand.bookstore.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText edtFullName, edtEmail, edtCurrentPass, edtNewPass, edtNewPassRetype;
    private SessionManager sessionManager;
    private User currentUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.action_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtFullName = findViewById(R.id.edt_full_name);
        edtEmail = findViewById(R.id.edt_email);
        edtCurrentPass = findViewById(R.id.edt_current_password);
        edtNewPass = findViewById(R.id.edt_new_password);
        edtNewPassRetype = findViewById(R.id.edt_retype_password);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        sessionManager = new SessionManager(this);
        loadInfo();
    }

    private void loadInfo() {
        if (sessionManager.isLoggedIn()) {
            RetrofitManager.getInstance().getBookStoreService().getUserById(sessionManager.getCurrentUser().getId())
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.isSuccessful()) {
                                User user = response.body();
                                currentUser = user;
                                edtFullName.setText(user.getName());
                                edtEmail.setText(user.getEmail());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
        }
    }

    private boolean checkValidate() {
        if (edtFullName.getText().toString().isEmpty()) {
            setBackgroundRed(edtFullName);
        } else {
            setBackgroundNormal(edtFullName);
        }
        if (edtEmail.getText().toString().isEmpty()) {
            setBackgroundRed(edtEmail);
        } else {
            setBackgroundNormal(edtEmail);
        }
        if (edtCurrentPass.getText().toString().isEmpty()) {
            setBackgroundRed(edtCurrentPass);
        } else {
            setBackgroundNormal(edtCurrentPass);
        }
        if (edtNewPass.getText().toString().isEmpty()) {
            setBackgroundRed(edtNewPass);
        } else {
            setBackgroundNormal(edtNewPass);
        }
        if (edtNewPassRetype.getText().toString().isEmpty()) {
            setBackgroundRed(edtNewPassRetype);
        } else {
            setBackgroundNormal(edtNewPassRetype);
        }
        if (!edtFullName.getText().toString().isEmpty()
                && !edtEmail.getText().toString().isEmpty()
                && !edtCurrentPass.getText().toString().isEmpty()
                && !edtNewPass.getText().toString().isEmpty()
                && !edtNewPassRetype.getText().toString().isEmpty())
            return true;
        return false;
    }

    private void changeInfo() {
        if (checkValidate()) {
            User user = currentUser;
            Log.d("userinfo",user.getAccount().getPassword());
            if (!edtCurrentPass.getText().toString().equals(user.getAccount().getPassword())) {
                Helper.showToast(getApplicationContext(), "Mật khẩu hiện tại không chính xác");
            } else if (!edtNewPass.getText().toString().equals(edtNewPassRetype.getText().toString())) {
                Helper.showToast(getApplicationContext(), " Mật khẩu mới không giống nhau");
            } else {
                Account account = user.getAccount();
                account.setId(user.getAccount().getId());
                account.setPassword(edtNewPass.getText().toString());
                user.setEmail(edtEmail.getText().toString());
                user.setAccount(account);
                RetrofitManager.getInstance().getBookStoreService().changeInfo(user)
                        .enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                if (response.isSuccessful()) {
                                    if (response.body()) {
                                        Helper.showToast(getApplicationContext(), "Thành công!");
                                        edtCurrentPass.setText("");
                                        edtNewPass.setText("");
                                        edtNewPassRetype.setText("");
                                    } else
                                        Helper.showToast(getApplicationContext(), "Không thành công!");
                                }
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
            }
        }
    }

    private void setBackgroundNormal(View view) {
        view.setBackgroundResource(R.drawable.bg_edit_text_normal);
    }

    private void setBackgroundRed(View view) {
        view.setBackgroundResource(R.drawable.bg_edit_text_red);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        changeInfo();
    }
}
