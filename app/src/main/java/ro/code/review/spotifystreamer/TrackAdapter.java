package ro.code.review.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by oana on 6/14/15.
 */
public class TrackAdapter extends ArrayAdapter {
    private Context cont;
    public TrackAdapter(Context context, List<Track> tracks) {
        super(context, R.layout.list_item, tracks);
        cont= context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_tracks, parent, false);
        Track track = (Track) getItem(position);
        String album =track.album.name;
        TextView albumView = (TextView) customView.findViewById(R.id.albumText);
        albumView.setText(album);

        String track1 =track.name;
        TextView trackView =  (TextView) customView.findViewById(R.id.trackText);
        trackView.setText(track1);

        String image = "https://i.scdn.co/image/4f2cf258c9be6b6940feb404652a24318695bfb2";//
        if(track.album!=null&& track.album.images.size()>0)
            image=track.album.images.get(0).url;

        ImageView imageView =(ImageView)customView.findViewById(R.id.imageView);

        //imageView.setImageResource(R.drawable.abc_btn_radio_to_on_mtrl_000);
        //track.href;
        Picasso.with(cont).load(image/*"https://i.scdn.co/image/4f2cf258c9be6b6940feb404652a24318695bfb2"*/).resize(200,200).into(imageView);

        return customView;
    }
}
