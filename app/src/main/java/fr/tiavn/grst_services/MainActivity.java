package fr.tiavn.grst_services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    // Create a VideoView variable, a MediaPlayer variable, and an int to hold the current
    // video position.
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;

    private Button btn_inscription;
    private Button btn_connexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hook up the VideoView to our UI.
        videoBG = (VideoView) findViewById(R.id.videoView);

        // Build your video Uri
        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.network); // and then finally add your video resource. Make sure it is stored
        // in the raw folder.

        // Set the new Uri to our VideoView
        videoBG.setVideoURI(uri);
        // Start the VideoView
        videoBG.start();

        // Set an OnPreparedListener for our VideoView. For more information about VideoViews,
        // check out the Android Docs: https://developer.android.com/reference/android/widget/VideoView.html
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                // We want our video to play over and over so we set looping to true.
                mMediaPlayer.setLooping(true);
                // We then seek to the current posistion if it has been set and play the video.
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });

        btn_inscription = (Button) findViewById(R.id.button_inscription);
        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Inscription.class);
                startActivity(intent);
            }
        });

        btn_connexion = (Button) findViewById(R.id.button_connexion);
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Connexion.class);
                startActivity(intent);
            }
        });
    }

        /*================================ Important Section! ================================
    We must override onPause(), onResume(), and onDestroy() to properly handle our
    VideoView.
     */

    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current video position and pause the video.
        mCurrentVideoPosition = mMediaPlayer.getCurrentPosition();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the video when resuming the Activity
        videoBG.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // When the Activity is destroyed, release our MediaPlayer and set it to null.
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}