package ro.code.review.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import ro.code.review.spotifystreamer.adapter.TopTrackAdapter;
import ro.code.review.spotifystreamer.utils.Utils;

public class TopTrackActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title));
        setContentView(R.layout.activity_top_track);
        ListView listView = (ListView) findViewById(R.id.topTracksListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TopTrackActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });
        TracksTask task = new TracksTask();
        //Find out if an search artist ID was sent to this activity
        String artistSpotifyId = getIntent().getStringExtra(Utils.ARTIST);
        if (artistSpotifyId != null) { // if it was send, find out the top 10 tracks
            task.execute(artistSpotifyId);
            Utils.setArtistID(artistSpotifyId);
        } else {
            if (Utils.getArtistID() == null) //if there are no tracks to display, execute the default search
            {
                task.execute(getString(R.string.defaultSpotifyId));
            } else { //if there are already available tracks to display, nothing new to search
                ListAdapter listAdapter = new TopTrackAdapter(TopTrackActivity.this, Utils.getTracks());
                listView.setAdapter(listAdapter);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_track, menu);
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

    private class TracksTask extends AsyncTask<String, Void, List> {
        @Override
        protected List<Track> doInBackground(String... urls) {
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService service = api.getService();
                Map<String, Object> map = new HashMap<>();
                map.put(Utils.COUNTRY, getString(R.string.searchCountry));
                Tracks tracksPager = service.getArtistTopTrack((urls.length > 0) ? urls[0] : getString(R.string.defaultSpotifyId), map);
                return tracksPager.tracks;
            } catch (Exception e) {

                return null;
            }
        }

        @Override
        protected void onPostExecute(List result) {
            Utils.setTracks(result);
            if (result != null) {
                if (result.size() > 0) {
                    ListAdapter listAdapter = new TopTrackAdapter(TopTrackActivity.this, Utils.getTracks());
                    ListView listView = (ListView) findViewById(R.id.topTracksListView);
                    listView.setAdapter(listAdapter);
                } else {
                    Toast.makeText(TopTrackActivity.this, getString(R.string.NoTracksFound), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(TopTrackActivity.this, getString(R.string.SearchException), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
