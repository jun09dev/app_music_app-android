package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView rvMusicList;
    private MusicListAdapter adapter;
    private ArrayList<String> musicList = new ArrayList<>();

    private static final String MUSIC_LIST_API = "http://61.106.148.251:8080/api/get_file_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // ===== RecyclerView =====
        rvMusicList = findViewById(R.id.rvMusicList);
        rvMusicList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MusicListAdapter(musicList, filename -> {
            Intent intent = new Intent(HomeActivity.this, MusicPlayerActivity.class);
            intent.putExtra("filename", filename);
            startActivity(intent);
        });

        rvMusicList.setAdapter(adapter);

        // ===== Load music from API =====
        loadMusicFromServer();

        // ===== Bottom Navigation =====
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_music);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_music) {
                return true;
            } else if (id == R.id.nav_search) {
                startActivity(new Intent(this, SearchActivity.class));
                return true;
            } else if (id == R.id.nav_create) {
                startActivity(new Intent(this, CreateMusicActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void loadMusicFromServer() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(MUSIC_LIST_API)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("API_ERROR", "Failed to load music", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) return;

                try {
                    String json = response.body().string();
                    JSONArray array = new JSONArray(json);

                    musicList.clear();
                    for (int i = 0; i < array.length(); i++) {
                        musicList.add(array.getString(i));
                    }

                    // Update UI
                    runOnUiThread(() -> adapter.notifyDataSetChanged());

                } catch (Exception e) {
                    Log.e("PARSE_ERROR", e.getMessage());
                }
            }
        });
    }
}
