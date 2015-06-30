package ro.code.review.spotifystreamer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setTitle(getString(R.string.title));

        Track trackDetail = Utils.getTracks().get(getIntent().getIntExtra(Utils.TRACK, 0));


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

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }

        });
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        String url = trackDetail.preview_url;
        try {
            mMediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer player) {
                player.start();
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
