package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;



import org.json.JSONArray;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {

    private RecyclerView recyclerMusic;
    private MusicListAdapter adapter;
    private ArrayList<String> musicFiles = new ArrayList<>();
    private static final String API_URL = "http://61.106.148.251:8080/api/get_file_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        recyclerMusic = findViewById(R.id.recyclerMusic);
        recyclerMusic.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MusicListAdapter(musicFiles, filename -> {
            Intent intent = new Intent(MusicListActivity.this, MusicPlayerActivity.class);
            intent.putExtra("filename", filename);
            startActivity(intent);
        });

        recyclerMusic.setAdapter(adapter);
        loadMusicList();
    }

    private void loadMusicList() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                response -> {
                    parseMusicList(response);
                },
                error -> Log.e("API_ERROR", error.toString()));

        queue.add(request);
    }

    private void parseMusicList(JSONArray array) {
        musicFiles.clear();
        for (int i = 0; i < array.length(); i++) {
            musicFiles.add(array.optString(i));
        }
        adapter.notifyDataSetChanged();
    }
}
