package com.example.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.network.ApiClient;
import com.example.app.network.AuthService;
import com.example.app.network.model.RegisterRequest;
import com.example.app.network.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private static final String BASE_URL = "http://61.106.148.251:8080/";

    private EditText etRegUsername, etRegPassword, etRegPasswordConfirm;
    private Button btnRegister;
    private ProgressBar progress;
    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegUsername = findViewById(R.id.etRegUsername);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegPasswordConfirm = findViewById(R.id.etRegPasswordConfirm);
        btnRegister = findViewById(R.id.btnRegister);
        progress = findViewById(R.id.progressRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        tvBackToLogin.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {
            String username = etRegUsername.getText().toString().trim();
            String password = etRegPassword.getText().toString();
            String confirm  = etRegPasswordConfirm.getText().toString();

            if (!validateInput(username, password, confirm)) return;

            performRegister(username, password);
        });
    }

    private boolean validateInput(String username, String password, String confirm) {
        if (TextUtils.isEmpty(username)) {
            etRegUsername.setError("Nhập tên đăng nhập");
            return false;
        }
        if (TextUtils.isEmpty(password) || password.length() < 4) {
            etRegPassword.setError("Mật khẩu >= 4 ký tự");
            return false;
        }
        if (!password.equals(confirm)) {
            etRegPasswordConfirm.setError("Mật khẩu không khớp");
            return false;
        }
        return true;
    }

    private void performRegister(String username, String password) {
        progress.setVisibility(View.VISIBLE);
        btnRegister.setEnabled(false);

        Retrofit retrofit = ApiClient.getClient(BASE_URL);
        AuthService service = retrofit.create(AuthService.class);

        RegisterRequest request = new RegisterRequest(username, password);

        service.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progress.setVisibility(View.GONE);
                btnRegister.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this,
                            "Đăng ký thành công",
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,
                            "Đăng ký thất bại",
                            Toast.LENGTH_LONG).show();
                    Log.w(TAG, "register failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                btnRegister.setEnabled(true);
                Toast.makeText(RegisterActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(TAG, "register error", t);
            }
        });
    }
}
