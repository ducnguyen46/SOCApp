package com.ducnguyen46.soc.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ducnguyen46.soc.MainActivity;
import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.constant.Constant;
import com.ducnguyen46.soc.model.LoginUser;
import com.ducnguyen46.soc.model.User;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.UserRestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.ducnguyen46.soc.constant.Constant.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText usernameEt, pwdEt;
    private Button loginBtn, registerBtn, forgorPwdBtn;
    private UserRestService userService;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userService = ApiRestUtils.getUserService();
        initView();
        autoLogin();
    }

    private void autoLogin(){

        boolean isLogin = sharedPreferences.getBoolean(IS_LOGIN, false);
        if(isLogin){
            String username = sharedPreferences.getString(USERNAME, "");
            String password = sharedPreferences.getString(PASSWORD, "");

            LoginUser user = new LoginUser();
            user.setUsername(username);
            user.setPassword(password);

            login(user);
            Intent autoLoginIntent = new Intent(this, MainActivity.class);
            autoLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(autoLoginIntent);
        }
    }

    void initView(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        usernameEt = findViewById(R.id.usernameEt);
        pwdEt = findViewById(R.id.passwordEt);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        forgorPwdBtn = findViewById(R.id.forgorPwdBtn);

        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerBtn:
                Intent gotoRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(gotoRegister);
                break;

            case R.id.loginBtn:
                LoginUser user = new LoginUser();
                user.setUsername(usernameEt.getText().toString());
                user.setPassword(pwdEt.getText().toString());

                login(user);
                break;
        }
    }

    private void login(LoginUser user){
        userService.login(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String token = response.body();
                    SharedPreferences.Editor editor =  sharedPreferences.edit();
                    editor.putBoolean(IS_LOGIN, true);
                    editor.putString(TOKEN_USER, token);
                    editor.putString(USERNAME, user.getUsername());
                    editor.putString(PASSWORD, user.getPassword());

                    editor.commit();
                    Toast.makeText(LoginActivity.this,
                            "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    Intent autoLoginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    autoLoginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(autoLoginIntent);
                } else {
                    System.out.println("Fail: " + response.code());
                    Toast.makeText(LoginActivity.this,
                            "Tên đăng nhập hoặc mật khẩu sai!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(LoginActivity.this,
                        "Đã có lỗi xảy ra, xin hãy thử lại sau!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}