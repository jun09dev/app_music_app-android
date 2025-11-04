package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnSignup, btnForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ánh xạ view
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnForgot = findViewById(R.id.btnForgot);

        // Nút Login hiện tại chỉ là placeholder (sau này bạn thêm logic login riêng)
        btnLogin.setOnClickListener(v -> {
            // TODO: thực hiện login ở đây (hiện tại chỉ thông báo)
            String user = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
            if (user.isEmpty()) {
                Toast.makeText(MainActivity.this, "Nhập tên đăng nhập trước", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Nút Login chưa được triển khai", Toast.LENGTH_SHORT).show();
            }
        });

        // Mở RegisterActivity
        btnSignup.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        // Mở ForgotPasswordActivity
        btnForgot.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class)));
    }
}
