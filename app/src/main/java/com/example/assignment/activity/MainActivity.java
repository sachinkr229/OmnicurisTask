package com.example.assignment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.assignment.R;
import com.example.assignment.RecyclerItemClickListener;
import com.example.assignment.adapter.VideoRecyclerAdapter;
import com.example.assignment.interfaces.YoutubeAPI;
import com.example.assignment.model.Item;
import com.example.assignment.model.SOAnswersResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String key = "AIzaSyAwG2-55YQKkNoWHcd4iGuReXLOPxEGLgA";
    private String part = "snippet";
    private RecyclerView recyclerView;
    private VideoRecyclerAdapter adapter;
    public static List<Item> videos;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        videos = new ArrayList<Item>();
        initializeRecyclerView();
        initializeAdapter(videos);
        youtubeAPICall("");

        context = this;

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, VideoDetailedActivity.class);
                        intent.putExtra("VIDEO", videos.get(position));
                        startActivity(intent);
                    }
                })
        );
    }







    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initializeRecyclerView()
    {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeAdapter(List<Item> videos) {
        adapter = new VideoRecyclerAdapter(videos);
        recyclerView.setAdapter(adapter);
    }

    private void youtubeAPICall(String query)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        YoutubeAPI youtubeAPI = retrofit.create(YoutubeAPI.class);
        Call<SOAnswersResponse> call = youtubeAPI.getAnswers(key, part, query, "video", 20);
        call.enqueue(new Callback<SOAnswersResponse>() {
            @Override
            public void onResponse(Call<SOAnswersResponse> call, Response<SOAnswersResponse> response)
            {

             //   Log.e("MAin Activity"," response from url:"+response.toString());

                videos = response.body().getItems();
                adapter.getVideos().clear();
                adapter.getVideos().addAll(videos);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SOAnswersResponse> call, Throwable t)
            {
                Toast.makeText(context, "Network error: failed to load the videos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}