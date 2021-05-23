package com.ducnguyen46.soc.view.userpage;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.constant.Constant;
import com.ducnguyen46.soc.model.ChangePassword;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.UserRestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PasswordActivity extends AppCompatActivity {
    private EditText oldPwdEt, newPwdEt;
    private Button okBtn;
    private UserRestService userRestService;
    private String token;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Thay đổi mật khẩu");
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = sharedPreferences.getString(Constant.TOKEN_USER, null);
        userRestService = ApiRestUtils.getUserService();

        oldPwdEt = findViewById(R.id.oldPwdEt);
        newPwdEt = findViewById(R.id.newPwdEt);
        okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(v -> changePassword());
    }

    private void changePassword(){
        String oldPassword = oldPwdEt.getText().toString();
        String newPassword = newPwdEt.getText().toString();

        if(oldPassword.isEmpty()){
            oldPwdEt.requestFocus();
        } else if (newPassword.isEmpty()){
            newPwdEt.requestFocus();
        } else {
            ChangePassword changePassword = new ChangePassword(oldPassword, newPassword);
            userRestService.updateUserPassword("Bearer " + token, changePassword).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Constant.PASSWORD, newPassword);

                        editor.commit();
                        Toast.makeText(PasswordActivity.this, "Đã thay đổi mật khẩu!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(PasswordActivity.this, "Sai mật khẩu!", Toast.LENGTH_LONG).show();
                        oldPwdEt.requestFocus();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }
}