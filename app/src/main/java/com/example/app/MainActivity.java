package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.dto.LoginRequest;
import com.example.app.dto.LoginResponse;
import com.example.app.network.ApiClient;
import com.example.app.network.AuthService;
import com.example.app.util.PrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnSignup, btnForgot;
    private TextView tvError;
    private ProgressBar progress;

    // Chỉnh base URL theo môi trường của bạn:
    // - Emulator (host localhost): "http://10.0.2.2:8080/"
    // - Device thật: "http://<HOST_IP>:8080/"
    private static final String BASE_URL = "http://61.106.148.251:8080/";

    private AuthService api;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // layout bạn đã cho

        // Ánh xạ view (giữ nguyên cấu trúc của bạn)
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnForgot = findViewById(R.id.btnForgot);

        // Các view bổ sung từ layout bạn gửi trước
        tvError = findViewById(R.id.tvError);
        progress = findViewById(R.id.progress);

        // Pref manager để lưu username/token
        pref = new PrefManager(this);

        // Nếu đã login trước đó, chuyển thẳng sang Home (tuỳ bạn muốn)
        String savedUser = pref.getUsername();
        if (savedUser != null && !savedUser.isEmpty()) {
            startHomeAndFinish();
            return;
        }

        // Retrofit client
        Retrofit retrofit = ApiClient.getClient(this, BASE_URL);
        api = retrofit.create(AuthService.class);

        // Listeners
        btnLogin.setOnClickListener(v -> attemptLogin());
        btnSignup.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
        btnForgot.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ForgotPasswordActivity.class)));
    }

    private void attemptLogin() {
        // clear previous error
        tvError.setVisibility(View.GONE);

        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        if (username.isEmpty() || password.isEmpty()) {
            showError("Vui lòng nhập tên đăng nhập và mật khẩu");
            return;
        }

        // show progress
        setUiLoading(true);

        // prepare request
        LoginRequest req = new LoginRequest(username, password);
        Call<String> call = api.login(req);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                setUiLoading(false);

                if (response.isSuccessful()) {
                    String token = response.body(); // đây là chuỗi token trả về
                    if (token != null && !token.isEmpty()) {
                        // token thường có ký tự newline hoặc URL-encoded %, bạn có thể trim
                        token = token.trim();
                        pref.saveToken(token);      // lưu token
                        pref.saveUsername(username); // lưu username
                        Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        startHomeAndFinish();
                        return;
                    } else {
                        showError("Server trả token rỗng.");
                        return;
                    }
                }

                // không thành công -> đọc errorBody
                String errMsg = "Lỗi server: " + response.code();
                try {
                    if (response.errorBody() != null) {
                        String errBody = response.errorBody().string();
                        if (!errBody.isEmpty()) errMsg = errBody;
                    }
                } catch (Exception ignored) {}
                showError(errMsg);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                setUiLoading(false);
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });

    }

    private void showError(String message) {
        tvError.setText(message);
        tvError.setVisibility(View.VISIBLE);
    }

    private void setUiLoading(boolean loading) {
        btnLogin.setEnabled(!loading);
        btnSignup.setEnabled(!loading);
        btnForgot.setEnabled(!loading);
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void startHomeAndFinish() {
        Intent i = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }
}
