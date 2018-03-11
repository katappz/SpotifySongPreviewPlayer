package pt.olive.katappz.spotifysongpreviewplayer.models;

import android.graphics.Bitmap;

import pt.olive.katappz.spotifysongpreviewplayer.models.spotify.Image;
import pt.olive.katappz.spotifysongpreviewplayer.models.spotify.Item;

/**
 * Song information.
 * @author Katy
 * @version 1.0
 * @since   2018-02-03
 */
public class Song  {

    public String name;
    public String artist;
    public String preview_url;

    //thumbnail
    public String thumbnail_url;
    public int thumbnail_width;
    public int thumbnail_height;

    public Bitmap thumbnail;

    public Song() {

    }

    public Song(Item item) {
        name = item.getName();
        preview_url = item.getPreview_url();

        if (item.getArtists().size() > 0)
            artist = item.getArtists().get(0).getName();

        Image img = null;
        if (item.getAlbum().getImages().size() > 1) {
            img = item.getAlbum().getImages().get(1);
        } else if (item.getAlbum().getImages().size() > 0) {
            img = item.getAlbum().getImages().get(0);
        }
        if (img != null) {
            thumbnail_url = img.getUrl();
            thumbnail_width = img.getWidth();
            thumbnail_height = img.getHeight();
        }
    }
}
