package com.ducnguyen46.soc.view.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ducnguyen46.soc.MainActivity;
import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.adapter.ProductPayAdapter;
import com.ducnguyen46.soc.constant.Constant;
import com.ducnguyen46.soc.model.Bill;
import com.ducnguyen46.soc.model.ProductInCart;
import com.ducnguyen46.soc.model.User;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.BillRestService;
import com.ducnguyen46.soc.service.ProductCartRestService;
import com.ducnguyen46.soc.service.UserRestService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity {

    private EditText namePayEt, phonePayEt, addressPayEt;
    private TextView totalAmountTv;
    private Button payBtn;
    private RecyclerView rcPayProduct;
    private CheckBox confirmCb;
    private ArrayList<ProductInCart> listProductCart = new ArrayList<>();
    private ProductPayAdapter adapter;
    private UserRestService userRestService;
    private BillRestService billRestService;
    private ProductCartRestService productCartRestService;
    private String token;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        init();
    }

    private void init() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString(Constant.TOKEN_USER, null);

        productCartRestService = ApiRestUtils.getProductCartService();
        userRestService = ApiRestUtils.getUserService();
        billRestService = ApiRestUtils.getBillService();
        adapter = new ProductPayAdapter(this);
        loadProductInCart();


        rcPayProduct = findViewById(R.id.rcPayProduct);
        namePayEt = findViewById(R.id.namePayEt);
        phonePayEt = findViewById(R.id.phonePayEt);
        addressPayEt = findViewById(R.id.addressPayEt);
        confirmCb = findViewById(R.id.comfirmCb);
        totalAmountTv = findViewById(R.id.totalAmountTv);
        payBtn = findViewById(R.id.payBtn);

        rcPayProduct.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rcPayProduct.setAdapter(adapter);

        confirmCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    payBtn.setEnabled(isChecked);
                } else {
                    payBtn.setEnabled(isChecked);
                }
                System.out.println("Mua hàng? " + isChecked);
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addressPayEt.getText().toString().isEmpty()){
                    addressPayEt.requestFocus();
                    return;
                }

                Bill bill = new Bill();
                bill.setAddress(addressPayEt.getText().toString());
                billRestService.createBill("Bearer " + token, bill).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(PayActivity.this,
                                    "Thành công! Chúng tôi sẽ tiến hành giao hàng sớm nhất!",
                                    Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PayActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

    }

    private void loadProductInCart() {
        productCartRestService.getAllProductInCart("Bearer " + token).enqueue(new Callback<List<ProductInCart>>() {
            @Override
            public void onResponse(Call<List<ProductInCart>> call, Response<List<ProductInCart>> response) {
                if (response.isSuccessful()) {
                    listProductCart = new ArrayList<>(response.body());
                } else {
                    listProductCart = new ArrayList<>();
                }
                adapter.setData(listProductCart);
                loadTotalAmount();
                loadUserData();
            }

            @Override
            public void onFailure(Call<List<ProductInCart>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void loadUserData() {
        userRestService.getUserInfo("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    namePayEt.setText(user.getName());
                    namePayEt.setEnabled(false);
                    phonePayEt.setText(user.getPhoneNumber());
                    phonePayEt.setEnabled(false);
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void loadTotalAmount() {
        double amount = 0;
        for (int i = 0; i < listProductCart.size(); i++) {
            amount += (listProductCart.get(i).getQuantity() * listProductCart.get(i).getProduct().getPrice());
        }
        totalAmountTv.setText("TỔNG TIỀN: " + String.format("%1$,.0f", amount) + " VND");
    }
}