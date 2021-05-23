package com.ducnguyen46.soc.view;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ducnguyen46.soc.R;
import com.ducnguyen46.soc.model.User;
import com.ducnguyen46.soc.service.ApiRestUtils;
import com.ducnguyen46.soc.service.UserRestService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nameEt, usernameEt, pwdEt, phoneEt;
    private Button registerBtn;
    private ImageButton backBtn;
    private UserRestService userService;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Đăng ký");
        actionBar.setDisplayHomeAsUpEnabled(true);
        initView();

        userService = ApiRestUtils.getUserService();
    }

    void initView(){
        nameEt = findViewById(R.id.nameEt);
        usernameEt = findViewById(R.id.usernameEt);
        pwdEt = findViewById(R.id.passwordEt);
        phoneEt = findViewById(R.id.phonedEt);
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backBtn:
                finish();
                break;
            case R.id.registerBtn:

                if(nameEt.getText().toString().trim().isEmpty()
                    || usernameEt.getText().toString().trim().isEmpty()
                    || pwdEt.getText().toString().trim().isEmpty()
                    || phoneEt.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "Điền đầy đủ thông tin trước khi tiếp tục!", Toast.LENGTH_SHORT).show();
                }else {
                    registerUser();
                }
                break;
        }
    }

    private void registerUser(){
        User user = new User();
        user.setName(nameEt.getText().toString());
        user.setUsername(usernameEt.getText().toString());
        user.setPassword(pwdEt.getText().toString());
        user.setPhoneNumber(phoneEt.getText().toString());

        userService.register(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    System.out.println("Chay roi nhe");
                    Toast.makeText(RegisterActivity.this,
                            "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    System.out.println("Fail: " + response.code());
                    Toast.makeText(RegisterActivity.this,
                            "Tên, tên đăng nhập, mật khẩu không được để trống hoặc tên đăng nhập đã bị trùng!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(t.getMessage());
                Toast.makeText(RegisterActivity.this,
                        "Đã có lỗi xảy ra, xin hãy thử lại sau!", Toast.LENGTH_LONG).show();
            }
        });
    }
}