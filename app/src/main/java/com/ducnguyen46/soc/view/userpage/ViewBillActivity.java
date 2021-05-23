package com.ducnguyen46.soc.view.userpage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.adapter.BillAdapter;
import com.ducnguyen46.soc.constant.Constant;
import com.ducnguyen46.soc.model.Bill;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.BillRestService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewBillActivity extends AppCompatActivity {
    private RecyclerView rcBill;
    private BillAdapter billAdapter;
    private BillRestService billRestService;
    private ArrayList<Bill> listBill;
    String token;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

//    @Override
//    public boolean onNavigateUp() {
//        finish();
//        return true;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Xem hóa đơn");
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString(Constant.TOKEN_USER, null);

        rcBill = findViewById(R.id.rcBill);
        billAdapter = new BillAdapter(this);
        billRestService = ApiRestUtils.getBillService();
        rcBill.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rcBill.setAdapter(billAdapter);
        loadBill();
    }

    private void loadBill(){
        billRestService.getAllBill("Bearer " + token).enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                if (response.isSuccessful()){
                    listBill = new ArrayList<>(response.body());
                } else {
                    listBill = new ArrayList<>();
                }
                billAdapter.setData(listBill);
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}