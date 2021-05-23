package com.ducnguyen46.soc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.model.Bill;
import com.ducnguyen46.soc.model.Product;
import com.ducnguyen46.soc.model.ProductInCart;
import com.ducnguyen46.soc.model.ProductSold;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillHolder> {

    private Context context;
    private ArrayList<Bill> list;

    public BillAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<Bill> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_card, null, false);

        return new BillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillHolder holder, int position) {
        Bill bill = list.get(position);
        String createDate = new SimpleDateFormat("dd-MM-yyyy").format(bill.getCrateDate());
        ArrayList<ProductSold> listProductSold = new ArrayList<>(bill.getListProductHasSold());
        ArrayList<ProductInCart> listProductInCarts = converterInCartSold(listProductSold);

        holder.addressTv.setText("Địa chỉ giao hàng: "+  bill.getAddress());
        holder.dateTv.setText("Ngày mua hàng: " + createDate);
        holder.setData(listProductInCarts);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class BillHolder extends RecyclerView.ViewHolder {
        private TextView addressTv, dateTv, totalAmountTv;
        private RecyclerView rcProductBill;
        private ProductPayAdapter productCartAdapter;

        public BillHolder(@NonNull View itemView) {
            super(itemView);
            productCartAdapter = new ProductPayAdapter(context);
            addressTv = itemView.findViewById(R.id.addressBillTv);
            dateTv = itemView.findViewById(R.id.dateBillTv);
            totalAmountTv = itemView.findViewById(R.id.totalBillTv);
            rcProductBill = itemView.findViewById(R.id.rcProductBill);
            rcProductBill.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            rcProductBill.setAdapter(productCartAdapter);
        }

        public void setData(ArrayList<ProductInCart> listProductInCarts){
            productCartAdapter.setData(listProductInCarts);
        }
    }

    private ArrayList<ProductInCart> converterInCartSold(ArrayList<ProductSold> listIn){
        ArrayList<ProductInCart> listOut = new ArrayList<>();
        for(int i = 0; i < listIn.size(); i++){
            Product product = new Product(
                    listIn.get(i).getId(),
                    listIn.get(i).getName(),
                    listIn.get(i).getBrand(),
                    listIn.get(i).getMadein(),
                    listIn.get(i).getPrice(),
                    "No info",
                    listIn.get(i).getImageURL()
            );
            ProductInCart productInCart = new ProductInCart();
            productInCart.setQuantity(listIn.get(i).getQuantity());
            productInCart.setProduct(product);
            listOut.add(productInCart);
        }

        return listOut;
    }
}
