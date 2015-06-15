package ro.code.review.spotifystreamer.utils;

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

}
