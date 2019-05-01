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
import com.teamand.bookstore.model.Address;
import com.teamand.bookstore.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingAddressActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private EditText edtFullName, edtPhoneNumber, edtNumber, edtDistrict, edtCity, edtZipcode;
    private SessionManager sessionManager;
    private User currentUser;
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
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        loadShippingAddress();
    }

    private void loadShippingAddress() {
        if(sessionManager.isLoggedIn()){
            RetrofitManager.getInstance().getBookStoreService().getUserById(sessionManager.getCurrentUser().getId())
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if(response.isSuccessful()){
                                User user = response.body();
                                currentUser = user;
                                edtFullName.setText(user.getName());
                                edtNumber.setText(user.getAddress().getNumber());
                                edtDistrict.setText(user.getAddress().getDistrict());
                                edtCity.setText(user.getAddress().getCity());
                                currentUser = user;
                            }
                            response.raw().close();
                        }


                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });
        }
    }
    private boolean checkValidate(){
        if(edtFullName.getText().toString().isEmpty()){
            setBackgroundRed(edtFullName);
        }else {
            setBackgroundNormal(edtFullName);
        }
        if (edtPhoneNumber.getText().toString().isEmpty()) {
            setBackgroundRed(edtPhoneNumber);
        }else {
            setBackgroundNormal(edtPhoneNumber);
        }
        if (edtNumber.getText().toString().isEmpty()) {
            setBackgroundRed(edtNumber);
        }else {
            setBackgroundNormal(edtNumber);
        }
        if (edtDistrict.getText().toString().isEmpty()) {
            setBackgroundRed(edtDistrict);
        }else {
            setBackgroundNormal(edtDistrict);
        }
        if (edtCity.getText().toString().isEmpty()) {
            setBackgroundRed(edtCity);
        }else {
            setBackgroundNormal(edtCity);
        }
        if (edtZipcode.getText().toString().isEmpty()){
            setBackgroundRed(edtZipcode);
        }else {
            setBackgroundNormal(edtZipcode);
        }
        if(!edtFullName.getText().toString().isEmpty() && !edtPhoneNumber.getText().toString().isEmpty()
                && !edtNumber.getText().toString().isEmpty()
                && !edtDistrict.getText().toString().isEmpty()
                && !edtCity.getText().toString().isEmpty()
                && !edtZipcode.getText().toString().isEmpty())
            return true;
        return false;
    }
    private void setBackgroundNormal(View view){
        view.setBackgroundResource(R.drawable.bg_edit_text_normal);
    }
    private void setBackgroundRed(View view){
        view.setBackgroundResource(R.drawable.bg_edit_text_red);
    }
    private void changeInfo(){
        if( checkValidate()){
            User user = currentUser;
            user.setName(edtFullName.getText().toString());
            Address address = new Address();
            address.setId(user.getAddress().getId());
            address.setNumber(edtNumber.getText().toString());
            address.setDistrict(edtDistrict.getText().toString());
            address.setCity(edtCity.getText().toString());
            user.setAddress(address);
            Log.d("userinfo",user.getId()+ user.getName());
            RetrofitManager.getInstance().getBookStoreService().changeInfo(user)
                    .enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if(response.isSuccessful()){
                                if(response.body()) {
                                    Helper.showToast(getApplicationContext(), "Thành công!");
                                }else
                                    Helper.showToast(getApplicationContext(), "Không thành công!");
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });
        }
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
