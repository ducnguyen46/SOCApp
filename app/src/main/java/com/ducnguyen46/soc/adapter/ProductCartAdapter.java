package com.ducnguyen46.soc.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducnguyen46.soc.MainActivity;
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

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ProductCardHolder> {
    private Context context;
    private ArrayList<ProductInCart> listProductInCart;
    private ProductCartRestService productCartService;
    SharedPreferences sharedPreferences;
    String token;

    public ProductCartAdapter(Context context) {
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
    public ProductCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_cart_card, null, false);
        return new ProductCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardHolder holder, int position) {
        ProductInCart productInCart = listProductInCart.get(position);
        Product product = productInCart.getProduct();

        holder.nameProductTv.setText(product.getName());
        holder.priceProductTv.setText(String.format("%1$,.0f", product.getPrice()) + " VND");
        Picasso.get().load(Constant.BASE_URL + "/img/" +product.getImageURL()).into(holder.imgProduct);
        holder.quantityTv.setText(String.valueOf(productInCart.getQuantity()));

        holder.increBtn.setOnClickListener(v -> {
            if(token != null){

                productCartService.updateProductInCartQuantity("Bearer " + token,
                        new ProductAddCart(product.getId(), productInCart.getQuantity()+1)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            productInCart.setQuantity(productInCart.getQuantity() + 1);
                           notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        });

        holder.decreBtn.setOnClickListener(v -> {
            if(token != null){
                productCartService.updateProductInCartQuantity("Bearer " + token,
                        new ProductAddCart(product.getId(), productInCart.getQuantity()-1)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            productInCart.setQuantity(productInCart.getQuantity() - 1);
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
            }
        });

        holder.delProductBtn.setOnClickListener(v -> {
            if(token != null){
                productCartService.deleteProductInCart("Bearer " + token, productInCart.getId())
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()){
                                    listProductInCart.remove(position);
                                    notifyDataSetChanged();
                                } else if(response.code() == 404){
                                    Toast.makeText(context, "Không tìm thấy sản phẩm để xóa", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                System.out.println(t.getMessage());
                            }
                        });
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent gotoDetail = new Intent(context, ProductDetailActivity.class);
            gotoDetail.putExtra("product", product);
            context.startActivity(gotoDetail);
        });
    }

    @Override
    public int getItemCount() {
        return listProductInCart!= null ? listProductInCart.size() : 0;
    }

    class ProductCardHolder extends RecyclerView.ViewHolder{

        private ImageView imgProduct;
        private TextView nameProductTv;
        private TextView priceProductTv;
        private ImageButton increBtn, decreBtn, delProductBtn;
        private TextView quantityTv;

        public ProductCardHolder(@NonNull View view) {
            super(view);
            imgProduct = view.findViewById(R.id.imgProductCart);
            nameProductTv = view.findViewById(R.id.nameProductCartTv);
            priceProductTv = view.findViewById(R.id.priceProductCartTv);
            increBtn = view.findViewById(R.id.incrementBtn);
            decreBtn = view.findViewById(R.id.decrementBtn);
            delProductBtn = view.findViewById(R.id.delProductCartBtn);
            quantityTv = view.findViewById(R.id.quantityTv);
        }
    }
}
