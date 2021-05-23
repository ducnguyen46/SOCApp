package com.ducnguyen46.soc.view.home;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ducnguyen46.soc.MainActivity;
import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.adapter.ProductAdapter;
import com.ducnguyen46.soc.model.Product;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.ProductRestService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ducnguyen46.soc.MainActivity.getContext;
import static com.ducnguyen46.soc.constant.Constant.*;

public class HomeFragment extends Fragment {

    private RecyclerView rcProduct;
    private ArrayList<Product> products;
    private ProductAdapter productAdapter;
    private ProductRestService productRestService;
    private ActionBar actionBar;
    private SearchView productSv;
    private ImageView notfoundImg;

    public HomeFragment() { }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle("SOC");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("SOC");

        productRestService = ApiRestUtils.getProductService();

        notfoundImg = view.findViewById(R.id.notfoundImg);
        productSv = view.findViewById(R.id.productSv);
        rcProduct = view.findViewById(R.id.rcProduct);
        productAdapter = new ProductAdapter(getContext());
        loadProduct();
        rcProduct.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        rcProduct.setAdapter(productAdapter);
        productSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchProductByName(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchProductByName(newText);
                return false;
            }
        });

        return view;
    }

    private void loadProduct(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        String token = sharedPreferences.getString(TOKEN_USER, "");
        if(!token.equals("")){
            productRestService.getAllProducts("Bearer " + token).enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if(response.isSuccessful()){
                        products = new ArrayList<>(response.body());
                        productAdapter.setData(products);
                    } else {
                        System.out.println(response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    System.out.println("Loi roi: " + t.getMessage());
                }
            });
        }
    }

    private void searchProductByName(String name){
        if(name.equals("")){
            name = " ";
        }
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext());
        String token = sharedPreferences.getString(TOKEN_USER, null);
        productRestService.findProductByName("Bearer " + token, name).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                    products = new ArrayList<>(response.body());
                    notfoundImg.setVisibility(View.INVISIBLE);
                } else if(response.code() == 404){
                    products = new ArrayList<>();
                    notfoundImg.setVisibility(View.VISIBLE);
                }else {
                    System.out.println(response.code());
                }
                productAdapter.setData(products);
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                System.out.println("Loi roi: " + t.getMessage());
            }
        });
    }
}