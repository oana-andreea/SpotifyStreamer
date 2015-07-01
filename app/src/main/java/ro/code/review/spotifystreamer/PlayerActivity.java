package ro.code.review.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import kaaes.spotify.webapi.android.models.Track;
import ro.code.review.spotifystreamer.utils.Utils;


public class PlayerActivity extends ActionBarActivity {
    private MediaPlayer mMediaPlayer;
    private int currentTrack;
    private CountDownTimer countdown;
    private boolean isPaused = false;
    private boolean isReset = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setTitle(getString(R.string.title));

        currentTrack = Utils.getNextPreviewPosition(getIntent().getIntExtra(Utils.TRACK, 0));

        if (currentTrack >= 0) {
            Track trackDetail = Utils.getTracks().get(currentTrack);

            setDesign(trackDetail);
            setMediaPlayer(trackDetail);
        } else Toast.makeText(this, "There are no previews available for this artist top 10", Toast.LENGTH_LONG).show();

        setPlayerListener();
    }

    public void mediaChange(int change) {
        currentTrack += change;
        mMediaPlayer.reset();
        currentTrack = Utils.getNextPreviewPosition(currentTrack);
        if (currentTrack >= 0) {
            Track trackDetail = Utils.getTracks().get(currentTrack);
            setDesign(trackDetail);
            String url = trackDetail.preview_url;
            try {
                mMediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mMediaPlayer.prepareAsync();
        }
    }


    public void setDesign(Track trackDetail) {

        TextView albumView = (TextView) findViewById(R.id.albumPlayer);
        albumView.setText(trackDetail.album.name);

        TextView artistView = (TextView) findViewById(R.id.artistPlayer);
        artistView.setText(trackDetail.artists.get(0).name);

        TextView trackView = (TextView) findViewById(R.id.trackDuration);
        trackView.setText(trackDetail.name);

        String image = trackDetail.album.images.get(0).url;
        image = (image == null) ? (String) getString(R.string.defaultImage) : image;

        ImageView imageView = (ImageView) findViewById(R.id.trackNamePlayer);
        Picasso.with(this).load(image).resize(700, 700).into(imageView);


    }

    public void fireRunnable() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int duration = mMediaPlayer.getDuration();
                final ProgressBar progress = (ProgressBar) findViewById(R.id.progressBar);
                progress.setMax(duration);
                countdown = new CountDownTimer(duration, 250) {
                    public void onTick(long millisUntilFinished) {
                        if (isReset) {
                            progress.setProgress(0);
                            isReset = false;
                        } else if (isPaused == false) {
                            progress.setProgress(progress.getProgress() + 250);
                        }
                    }

                    public void onFinish() {
                    }
                }.start();
            }
        };
        runnable.run();
    }

    public void setPlayerListener() {

        final ImageButton pause = (ImageButton) findViewById(R.id.play);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    pause.setImageResource(android.R.drawable.ic_media_play);
                    isPaused = true;
                } else {
                    mMediaPlayer.start();
                    pause.setImageResource(android.R.drawable.ic_media_pause);
                    isPaused = false;

                }
            }
        });
        ImageButton previous = (ImageButton) findViewById(R.id.back);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaChange(-1);
                isReset = true;
                pause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });

        ImageButton next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaChange(+1);
                isReset = true;
                pause.setImageResource(android.R.drawable.ic_media_pause);
            }
        });


    }

    public void setMediaPlayer(Track trackDetail) {

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }

            });
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        String url = trackDetail.preview_url;
        try {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer player) {
                player.start();
                fireRunnable();
            }
        });

        mMediaPlayer.prepareAsync();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
