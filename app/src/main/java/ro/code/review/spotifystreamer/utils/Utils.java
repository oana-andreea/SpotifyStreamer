package ro.code.review.spotifystreamer.utils;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by oana on 6/15/15.
 */
public class Utils {
    private static List<Artist> artists;
    private static List<Track> tracks;

    private static String artistID;
    private static String artistSearch;

    public static final String ARTIST = "artist";
    public static final String TRACK = "track";
    public static final String COUNTRY = "country";

    public static final String TRACK_NAME = "track name";
    public static final String TRACK_ALBUM = "track album";
    public static final String TRACK_ARTIST = "track artist";
    public static final String TRACK_ICON = "track album icon";

    public static final int TRACK_SPOTIFY_ID = 0;
    public static final int TRACK_NAME_ID = 1;
    public static final int TRACK_ALBUM_ID = 2;
    public static final int TRACK_ARTIST_ID = 3;
    public static final int TRACK_ICON_ID = 4;
    public static final int TRACK_DURATION = 5;


    public static List<Artist> getArtists() {
        return artists;
    }

    public static void setArtists(List<Artist> artists) {
        Utils.artists = artists;
    }

    public static List<Track> getTracks() {
        return tracks;
    }

    public static void setTracks(List<Track> tracks) {
        Utils.tracks = tracks;
    }

    public static String getArtistID() {
        return artistID;

    }

    public static void setArtistID(String artistID) {
        Utils.artistID = artistID;
    }

    public static String getArtistSearch() {
        return artistSearch;
    }

    public static void setArtistSearch(String artistSearch) {
        Utils.artistSearch = artistSearch;
    }

    public static ArrayList<String> getTrackDetails(int position) {
        Track track = tracks.get(position);
        ArrayList<String> trackDetails = new ArrayList<String>();
        trackDetails.add(track.id);
        trackDetails.add(track.name);
        trackDetails.add(track.album.name);
        String image = track.album.images.isEmpty() ? "" : track.album.images.get(0).url;
        trackDetails.add(image);
        String duration = track.duration_ms > 0 ? String.valueOf(track.duration_ms) : "";
        trackDetails.add(duration);
        return trackDetails;
    }
}
