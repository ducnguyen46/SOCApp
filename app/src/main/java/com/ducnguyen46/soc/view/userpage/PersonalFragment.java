package com.ducnguyen46.soc.view.userpage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ducnguyen46.soc.MainActivity;
import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.constant.Constant;
import com.ducnguyen46.soc.model.User;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.UserRestService;
import com.ducnguyen46.soc.view.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalFragment extends Fragment {
    private TextView nameUserTv, usernameUserTv, phoneUserTv;
    private Button viewBillBtn, changePwdBtn, logoutBtn;
    private User user;
    private UserRestService userRestService;
    private String token;
    private ActionBar actionBar;

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle("Trang cá nhân");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle("Trang cá nhân");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        token = sharedPreferences.getString(Constant.TOKEN_USER, null);

        userRestService = ApiRestUtils.getUserService();
        nameUserTv = view.findViewById(R.id.nameUserTv);
        usernameUserTv = view.findViewById(R.id.usernameUserTv);
        phoneUserTv = view.findViewById(R.id.phoneUserTv);
        viewBillBtn = view.findViewById(R.id.viewBillBtn);
        changePwdBtn = view.findViewById(R.id.changePwdBtn);
        logoutBtn = view.findViewById(R.id.logoutBtn);

        loadUserData();

        viewBillBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ViewBillActivity.class);
            startActivity(intent);
        });

        changePwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PasswordActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constant.USERNAME, "");
                editor.putString(Constant.PASSWORD, "");
                editor.putString(Constant.TOKEN_USER, "");
                editor.putBoolean(Constant.IS_LOGIN, false);
                editor.commit();

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadUserData() {
        userRestService.getUserInfo("Bearer " + token).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    nameUserTv.setText("Họ và tên: " + user.getName());
                    phoneUserTv.setText("Số điện thoại: " + user.getPhoneNumber());
                    usernameUserTv.setText("Tên đăng nhập: " + user.getUsername());
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
}