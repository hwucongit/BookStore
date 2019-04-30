package com.teamand.bookstore.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;
import com.teamand.bookstore.R;
import com.teamand.bookstore.helper.Constants;
import com.teamand.bookstore.helper.Helper;
import com.teamand.bookstore.manager.RetrofitManager;
import com.teamand.bookstore.manager.SessionManager;
import com.teamand.bookstore.model.Cart;
import com.teamand.bookstore.model.Client;
import com.teamand.bookstore.model.Order;
import com.teamand.bookstore.model.ResponseCurrency;
import com.teamand.bookstore.model.ShippingInfo;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private Button btnConfirm;
    private EditText edtFullName, edtPhoneNumber, edtNumber, edtDistrict, edtCity, edtZipcode;
    private SessionManager sessionManager;
    public static final int REQUEST_CODE_PAYPAL = 1111;
    private double totalPrice;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Constants.PAYPAL_CLIENT_ID);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        init();
    }

    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.check_out);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        edtFullName = findViewById(R.id.edt_full_name);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtNumber = findViewById(R.id.edt_number);
        edtDistrict = findViewById(R.id.edt_district);
        edtCity = findViewById(R.id.edt_city);
        edtZipcode = findViewById(R.id.edt_zipcode);
        sessionManager = new SessionManager(this);
        radioGroup = findViewById(R.id.rg_payment_method);
        btnConfirm = findViewById(R.id.btn_checkout);
        btnConfirm.setOnClickListener(this);

        // start paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }
    private void openPaypalPaymentActivity(){
        final Double amount = Double.parseDouble(getIntent()
                .getStringExtra("totalPrice"));
        RetrofitManager.getInstance().getConvertMoneyService()
                .convertVNDtoUSD(Constants.ACCESS_KEY_API_CURRENCY,"VND", "USD", 1)
                .enqueue(new Callback<ResponseCurrency>() {
                    @Override
                    public void onResponse(Call<ResponseCurrency> call, Response<ResponseCurrency> response) {
                        totalPrice = amount/response.body().getQuotes().getUSDVND();
                        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(totalPrice),"USD",
                                "Thanh toán đơn hàng", PayPalPayment.PAYMENT_INTENT_ORDER);
                        Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
                        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
                        startActivityForResult(intent, REQUEST_CODE_PAYPAL);
                    }

                    @Override
                    public void onFailure(Call<ResponseCurrency> call, Throwable t) {
                    }
                });
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
        if(!edtFullName.getText().toString().isEmpty() && !edtPhoneNumber.getText().toString().isEmpty()
                && !edtNumber.getText().toString().isEmpty()
                && !edtDistrict.getText().toString().isEmpty()
                && !edtCity.getText().toString().isEmpty())
            return true;
        return false;
    }
    private void addOrder(String paymentMethod){
        Order order = new Order();
        String cartData = getIntent().getStringExtra("cart");
        Cart cart = new Gson().fromJson(cartData, Cart.class);

        //set cart
        order.setCart(cart);

        //set status
        order.setStatus("In Progress");

        //set method payment
        order.setPaymentMethod(paymentMethod);

        //set client
        Client client = new Client();
        if(sessionManager.isLoggedIn())
            client.setId(sessionManager.getCurrentUser().getId());
        order.setClient(client);

        //set ship address
        ShippingInfo shippingInfo = new ShippingInfo();
        shippingInfo.setReceiverName("Test");
        shippingInfo.setNumber("11");
        shippingInfo.setDistrict("hn");
        shippingInfo.setCity("DL");
        shippingInfo.setZipcode("123");
        order.setShippingInfo(shippingInfo);
        order.setDateCreate(new Date(new java.util.Date().getTime()));
        RetrofitManager.getInstance().getBookStoreService().addOrder(order)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if(response.isSuccessful()){
                            Helper.showToast(getApplicationContext(),"OK");
                        }else
                            Helper.showToast(getApplicationContext(),"Fail");
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
    }
    private void setBackgroundNormal(View view){
        view.setBackgroundResource(R.drawable.bg_edit_text_normal);
    }
    private void setBackgroundRed(View view){
        view.setBackgroundResource(R.drawable.bg_edit_text_red);
    }
    @Override
    public void onClick(View v) {
        if(checkValidate() == false){
            Helper.showToast(getApplicationContext(),"Điền đầy đủ thông tin yêu cầu");
        }else {
            if (radioGroup.getCheckedRadioButtonId() == R.id.rb_paypal) {
                openPaypalPaymentActivity();
            }else if(radioGroup.getCheckedRadioButtonId() == R.id.rb_shipcode){
                addOrder("shipcode");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, PayPalService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PAYPAL){
            if(resultCode == RESULT_OK){
                addOrder("paypal");
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(paymentConfirmation != null){
                    String rs = paymentConfirmation.toJSONObject().toString();
                    finish();
                    Helper.showToast(getApplicationContext(),"Xác nhận đơn hàng PayPal thành công!");
                }
            }else if(requestCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Helper.showToast(getApplicationContext(),"Không hợp lệ!");
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
