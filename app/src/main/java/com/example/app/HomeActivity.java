package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.util.PrefManager;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnLogout;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnLogout = findViewById(R.id.btnLogout);

        pref = new PrefManager(this);
        String username = pref.getUsername();
        tvWelcome.setText(username != null ? "Xin chào, " + username : "Xin chào");

        btnLogout.setOnClickListener(v -> {
            pref.clear();
            // optionally call backend logout endpoint if you have one
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();
        });
    }
}
