package com.ducnguyen46.soc.view.cart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ducnguyen46.soc.MainActivity;
import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.adapter.ProductCartAdapter;
import com.ducnguyen46.soc.constant.Constant;
import com.ducnguyen46.soc.model.ProductInCart;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.ProductCartRestService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment {

    private TextView thongbao;
    private RecyclerView rcProduct;
    private ProductCartAdapter productCartAdapter;
    private Button buyBtn;
    private ProductCartRestService productCartRestService;
    private ArrayList<ProductInCart> listProductInCart;
    private String token;
    private ActionBar actionBar;

    public CartFragment() { }

    @Override
    public void onResume() {
        super.onResume();
        loadProductInCart();
        actionBar.setTitle("Giỏ hàng");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Giỏ hàng");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        token = sharedPreferences.getString(Constant.TOKEN_USER, null);
        productCartRestService = ApiRestUtils.getProductCartService();

        productCartAdapter = new ProductCartAdapter(getContext());
        loadProductInCart();

        buyBtn = view.findViewById(R.id.buyBtn);
        rcProduct = view.findViewById(R.id.rcProduct);

        rcProduct.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        rcProduct.setAdapter(productCartAdapter);
        thongbao = view.findViewById(R.id.thongbao);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PayActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private void loadProductInCart(){
        productCartRestService.getAllProductInCart("Bearer "+ token).enqueue(new Callback<List<ProductInCart>>() {
            @Override
            public void onResponse(Call<List<ProductInCart>> call, Response<List<ProductInCart>> response) {
                if(response.isSuccessful()){
                    listProductInCart = new ArrayList<>(response.body());
                } else {
                    listProductInCart = new ArrayList<>();
                }
                productCartAdapter.setData(listProductInCart);
                if(listProductInCart.size() == 0){
                    thongbao.setEnabled(true);
                    buyBtn.setEnabled(false);
                } else {
                    thongbao.setEnabled(false);
                    buyBtn.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<List<ProductInCart>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}