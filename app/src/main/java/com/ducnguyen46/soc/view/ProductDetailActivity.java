package com.ducnguyen46.soc.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.constant.Constant;
import com.ducnguyen46.soc.model.Product;
import com.ducnguyen46.soc.model.ProductAddCart;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.ProductCartRestService;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private TextView nameProductTv, priceProductTv, desProductTv, madeProductTv, brandProductTv;
    private Button addBtn;
    private EditText quantityProductEt;

    private Product product;
    private ProductCartRestService productCartService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        productCartService = ApiRestUtils.getProductCartService();

        imgProduct = findViewById(R.id.imgProductDetail);
        nameProductTv = findViewById(R.id.nameProductDetailTv);
        priceProductTv = findViewById(R.id.priceProductDetailTv);
        desProductTv = findViewById(R.id.desProductDetailTv);
        madeProductTv = findViewById(R.id.madeProductDetailTv);
        brandProductTv = findViewById(R.id.brandProductDetailTv);
        addBtn = findViewById(R.id.addBtn);
        addBtn.setEnabled(false);
        quantityProductEt = findViewById(R.id.quantityProductEt);
        quantityProductEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println("Count: " + count);

                if(s.length() == 0){
                    addBtn.setEnabled(false);
                } else {
                    addBtn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loadData();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }

    private void loadData(){
        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");

        Picasso.get().load(Constant.BASE_URL + "/img/" +product.getImageURL()).into(imgProduct);
        nameProductTv.setText(product.getName());
        priceProductTv.setText(String.format("%1$,.0f", product.getPrice()) + " VND");
        desProductTv.setText(product.getDescription());
        madeProductTv.setText("Made in " + product.getMadein());
        brandProductTv.setText("Made by " + product.getBrand());
    }

    private void addProduct(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = sharedPreferences.getString(Constant.TOKEN_USER, null);
        if(token != null){
            productCartService.addProductToCart("Bearer " + token,
                    new ProductAddCart(product.getId(), Integer.parseInt(quantityProductEt.getText().toString())))
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                Toast.makeText(ProductDetailActivity.this, "Đã thêm sản phẩm vào giỏ hàng",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            System.out.println(t.getMessage());
                        }
                    });
        }
    }
}