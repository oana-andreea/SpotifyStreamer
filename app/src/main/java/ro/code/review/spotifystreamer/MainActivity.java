package ro.code.review.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.internal.Util;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import ro.code.review.spotifystreamer.adapter.CustomAdapter;
import ro.code.review.spotifystreamer.utils.Utils;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);
        if (Utils.getArtists() != null) { //artists already available
            ListAdapter listAdapter = new CustomAdapter(this, Utils.getArtists());
            listView.setAdapter(listAdapter);
        } else { //initial results
            SearchTask task = new SearchTask();
            task.execute(getString(R.string.defaultArtistSearch));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                intent = new Intent(MainActivity.this, TopTrackActivity.class);
                intent.putExtra(Utils.ARTIST, Utils.getArtists().get(position).id);
                startActivity(intent);

            }
        });
        final EditText edittext = (EditText) findViewById(R.id.extractEditText);
        if (Utils.getArtistSearch() != null) {
            edittext.setText(Utils.getArtistSearch());
            int textLength = edittext.getText().length();
            edittext.setSelection(textLength, textLength);// set cursor at the end when coming back
        }

        edittext.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Utils.setArtistSearch(String.valueOf(edittext.getText()));
                    SearchTask task = new SearchTask();
                    task.execute(Utils.getArtistSearch());
                    return true;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class SearchTask extends AsyncTask<String, Void, List> {
        @Override
        protected List<Artist> doInBackground(String... urls) {
            try {
                SpotifyApi api = new SpotifyApi();
                SpotifyService service = api.getService();
                String search = urls.length > 0 ? urls[0] : getString(R.string.defaultArtistSearch);
                ArtistsPager artistsPager = service.searchArtists(search);
                return artistsPager.artists.items;
            } catch (Exception e) {

                return null;
            }
        }


        @Override
        protected void onPostExecute(List result) {
            Utils.setArtists(result);
            if(result!=null){
            if (result.size() > 0) {
                ListAdapter listAdapter = new CustomAdapter(MainActivity.this, Utils.getArtists());
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(listAdapter);
            } else
                Toast.makeText(MainActivity.this, getString(R.string.NoArtistFound), Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(MainActivity.this, getString(R.string.SearchException), Toast.LENGTH_SHORT).show();
        }
    }
}