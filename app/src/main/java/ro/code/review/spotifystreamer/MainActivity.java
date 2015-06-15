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
import java.util.List;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.listView);
        if (Utils.getArtists() != null) {
            ListAdapter listAdapter = new CustomAdapter(this, Utils.getArtists());
            listView.setAdapter(listAdapter);
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
        if (Utils.getArtists() == null) {
            SearchTask task = new SearchTask();
            task.execute(new String[]{"Voltaj"});
        }
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
            SpotifyApi api = new SpotifyApi();
            SpotifyService service = api.getService();
            ArtistsPager artistsPager;
            if(urls.length>0)
              artistsPager = service.searchArtists(urls[0]);
            else
              artistsPager = service.searchArtists(getString(R.string.defaultArtistSearch));
            return artistsPager.artists.items;
        }


        @Override
        protected void onPostExecute(List result) {
            Utils.setArtists(result);
            if (result != null && result.size() > 0) {
                ListAdapter listAdapter = new CustomAdapter(MainActivity.this, Utils.getArtists());
                ListView listView = (ListView) findViewById(R.id.listView);
                listView.setAdapter(listAdapter);
            } else
                Toast.makeText(MainActivity.this, getString(R.string.NoArtistFound), Toast.LENGTH_SHORT).show();
        }
    }
}