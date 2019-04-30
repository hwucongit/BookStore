package com.teamand.bookstore.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.teamand.bookstore.model.Address;
import com.teamand.bookstore.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private RetrofitManager retrofitManager;
    private SessionManager sessionManager;
    private EditText edtUsername, edtFullName, edtEmail, edtPassword, edtRetypePass, edtNumber;
    private Button btnRegister;
    private Toolbar toolbar;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        edtUsername = findViewById(R.id.edt_username);
        edtFullName = findViewById(R.id.edt_full_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtRetypePass = findViewById(R.id.edt_retype_password);
        edtNumber = findViewById(R.id.edt_phone_number);
        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.register);

        sessionManager = new SessionManager(this);
        retrofitManager = new RetrofitManager();
        retrofitManager.initRetrofit(Constants.apiBookStore);

        dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.register));
        dialog.setMessage(getString(R.string.please_wait));
    }

    @Override
    public void onClick(View v) {
        String username = edtUsername.getText().toString();
        String fullName = edtFullName.getText().toString();
        String password = edtPassword.getText().toString();
        String retypePass = edtRetypePass.getText().toString();
        String email = edtEmail.getText().toString();
        String phone = edtNumber.getText().toString();

        if(username.isEmpty() || fullName.isEmpty() || password.isEmpty() || retypePass.isEmpty() || email.isEmpty() || phone.isEmpty()){
            Helper.showToast(getApplicationContext(),getString(R.string.fill_full_infor));
        }else if(isContainSpace(username)){
            Helper.showToast(getApplicationContext(),getString(R.string.username_no_space));
        }else if(!password.equals(retypePass)){
            Helper.showToast(getApplicationContext(),getString(R.string.pass_must_same));
        }else {
            User user;
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(password);
            Address address = new Address();
            address.setCountry(getString(R.string.vietnam));
            user = new User.Builder(account,fullName)
                    .email(email)
                    .address(address)
                    .role(getString(R.string.customer))
                    .build();
            register(user);
        }

    }
    private void register(final User user){
        dialog.show();
        BookStoreService bookStoreService = retrofitManager.getBookStoreService();
        bookStoreService.register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    sessionManager.createLoginSession(user.getId(), user.getName(), user.getEmail());
                    dialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if(response.code() == 409){
                    Helper.showToast(getApplicationContext(),getString(R.string.account_exist));
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.dismiss();
                Helper.showToast(getApplicationContext(),getString(R.string.fail_tryagain));

            }
        });
    }
    private boolean isContainSpace(String content){
        for(Character c: content.toCharArray()){
            if (Character.isWhitespace(c)) return true;
        }
        return false;
    }
}
