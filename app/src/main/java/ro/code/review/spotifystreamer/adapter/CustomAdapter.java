package ro.code.review.spotifystreamer.adapter;

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
import ro.code.review.spotifystreamer.R;

/**
 * Created by oana on 6/14/15.
 */
public class CustomAdapter extends ArrayAdapter {
    private Context cont;

    public CustomAdapter(Context context, List<Artist> artists) {
        super(context, R.layout.list_item, artists);
        cont= context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_item, parent, false);
        Artist artist = (Artist) getItem(position);
        String artistDescription = artist.name;
        TextView textView = (TextView) customView.findViewById(R.id.textView);
        ImageView imageView =(ImageView)customView.findViewById(R.id.imageView);
        textView.setText(artistDescription);
        String image = cont.getString(R.string.defaultImage);
        if(artist.images.size()>0)
        image =artist.images.get(0).url;
        Picasso.with(cont).load(image).resize(200,200).into(imageView);

        return customView;
    }
}
