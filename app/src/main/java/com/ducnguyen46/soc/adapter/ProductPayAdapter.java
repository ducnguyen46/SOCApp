package com.ducnguyen46.soc.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.constant.Constant;
import com.ducnguyen46.soc.model.Product;
import com.ducnguyen46.soc.model.ProductAddCart;
import com.ducnguyen46.soc.model.ProductInCart;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.ProductCartRestService;
import com.ducnguyen46.soc.view.ProductDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPayAdapter extends RecyclerView.Adapter<ProductPayAdapter.ProductPayHolder> {
    private Context context;
    private ArrayList<ProductInCart> listProductInCart;
    private ProductCartRestService productCartService;
    SharedPreferences sharedPreferences;
    String token;

    public ProductPayAdapter(Context context) {
        this.context = context;
        productCartService = ApiRestUtils.getProductCartService();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        token = sharedPreferences.getString(Constant.TOKEN_USER, null);
    }

    public void setData(ArrayList<ProductInCart> listProductInCart){
        this.listProductInCart = listProductInCart;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductPayHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_pay_card, null, false);
        return new ProductPayHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductPayHolder holder, int position) {
        ProductInCart productInCart = listProductInCart.get(position);
        Product product = productInCart.getProduct();

        holder.nameProductTv.setText(product.getName());
        holder.priceProductTv.setText(String.format("%1$,.0f", product.getPrice()) + " VND");
        Picasso.get().load(Constant.BASE_URL + "/img/" +product.getImageURL()).into(holder.imgProduct);
        holder.quantityTv.setText("Số lương: " + String.valueOf(productInCart.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return listProductInCart!= null ? listProductInCart.size() : 0;
    }

    class ProductPayHolder extends RecyclerView.ViewHolder{

        private ImageView imgProduct;
        private TextView nameProductTv;
        private TextView priceProductTv;
        private TextView quantityTv;

        public ProductPayHolder(@NonNull View view) {
            super(view);
            imgProduct = view.findViewById(R.id.imgProductPay);
            nameProductTv = view.findViewById(R.id.nameProductPayTv);
            priceProductTv = view.findViewById(R.id.priceProductPayTv);
            quantityTv = view.findViewById(R.id.quantityPayTv);
        }
    }
}
