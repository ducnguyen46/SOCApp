package com.ducnguyen46.soc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.constant.Constant;
import com.ducnguyen46.soc.model.Product;
import com.ducnguyen46.soc.view.ProductDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductCardHolder> {

    private ArrayList<Product> products;
    private Context context;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_card, null, false);
        return new ProductCardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardHolder holder, int position) {
        Product product = products.get(position);
        holder.nameProductTv.setText(product.getName());
        holder.priceProductTv.setText(String.format("%1$,.0f", product.getPrice()) + " VND");
        Picasso.get().load(Constant.BASE_URL + "/img/" +product.getImageURL()).into(holder.imgProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoDetail = new Intent(context, ProductDetailActivity.class);
                gotoDetail.putExtra("product", product);
                context.startActivity(gotoDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    class ProductCardHolder extends RecyclerView.ViewHolder{

        private ImageView imgProduct;
        private TextView nameProductTv;
        private TextView priceProductTv;

        public ProductCardHolder(@NonNull View view) {
            super(view);
            imgProduct = view.findViewById(R.id.imgProduct);
            nameProductTv = view.findViewById(R.id.nameProductTv);
            priceProductTv = view.findViewById(R.id.priceProductTv);
        }
    }
}
