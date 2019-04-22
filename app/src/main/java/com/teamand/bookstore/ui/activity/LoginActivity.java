package com.teamand.bookstore.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.teamand.bookstore.R;
import com.teamand.bookstore.api.BookStoreService;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.model.Account;
import com.teamand.bookstore.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private SessionManager session;
    private RetrofitManager retrofitManager;
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        session = new SessionManager(this);
        retrofitManager = new RetrofitManager();
        retrofitManager.initRetrofit(Constants.apiBookStore);
        dialog = new ProgressDialog(this);


    }

    private void login(final String username, String password) {
        dialog.setTitle(getString(R.string.login));
        dialog.setMessage(getString(R.string.please_wait));
        dialog.show();
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        BookStoreService bookStoreService = retrofitManager.getRetrofit().create(BookStoreService.class);
        bookStoreService.doLogin(account).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    session.createLoginSession(user.getId(), user.getName(), user.getEmail());
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (response.code() == 401) {
                    dialog.dismiss();
                    Helper.showToast(getApplicationContext(), getString(R.string.infor_wrong));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.dismiss();
                Helper.showToast(getApplicationContext(), getString(R.string.fail_tryagain));
            }
        });

    }

    @Override
    public void onClick(View v) {
        String username = edtUsername.getText().toString();
        String password = edtPassword.getText().toString();
        switch (v.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    Helper.showToast(getApplicationContext(),getString(R.string.type_both_username_pass));
                }else
                    login(username, password);
                break;
            case R.id.btn_register:
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
            default:
                break;
        }
    }
}
