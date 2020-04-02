package com.example.assignment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment.R;
import com.example.assignment.adapter.VideoRecyclerAdapter;
import com.example.assignment.interfaces.YoutubeAPI;
import com.example.assignment.model.Item;
import com.example.assignment.model.SOAnswersResponse;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.assignment.activity.MainActivity.videos;

public class VideoDetailedActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private String key = "AIzaSyAwG2-55YQKkNoWHcd4iGuReXLOPxEGLgA";
    private static final int RECOVERY_REQUEST = 1;
    private String part = "snippet";
    private YouTubePlayerView youTubeView;
    private Item video;
    Context context;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private RecyclerView recyclerView;
    private YouTubePlayer player;
    private Button fast_forward,play_pause,fast_backword;
    private VideoRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detailed);

        context=this;
        video = getIntent().getExtras().getParcelable("VIDEO");

        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        TextView channel = findViewById(R.id.channel);
        title.setText(video.getSnippet().getTitle());
        description.setText(video.getSnippet().getDescription());
      //  channel.setText(video.getSnippet().getChannelTitle());

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(key, this);
        fast_forward=findViewById(R.id.ff);
        fast_backword=findViewById(R.id.ss);
        play_pause=findViewById(R.id.pp);

        fast_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                player.seekToMillis(player.getCurrentTimeMillis() +seekForwardTime);

            }
        });


        fast_backword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                player.seekToMillis(player.getCurrentTimeMillis() - seekBackwardTime);

            }
        });
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying())
                player.pause();
                else
                    player.play();

            }
        });
        initializeRecyclerView();
        initializeAdapter(videos);
    }

    private void initializeRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.playlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initializeAdapter(List<Item> videos)
    {
        adapter = new VideoRecyclerAdapter(videos);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(key, this);
        }
    }

    protected YouTubePlayerView getYouTubePlayerProvider()
    {
        return youTubeView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.cueVideo(video.getId().getVideoId());
            this.player = youTubePlayer;
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError())
        {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        }
        else
            {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }


}
