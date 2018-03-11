package pt.olive.katappz.spotifysongpreviewplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;

import pt.olive.katappz.spotifysongpreviewplayer.interfaces.ICompletedLoadThumbnailListener;
import pt.olive.katappz.spotifysongpreviewplayer.models.Song;

/**
 * Async task that loads the song's thumbnail image given it's url.
 * @author Katy
 * @version 1.0
 * @since   2018-02-07
 */
public class LoadSongThumbnailTask extends AsyncTask<Song, Void, Bitmap> {

    private int imageViewWidth, imageViewHeight;
    private ICompletedLoadThumbnailListener completedLoadListener;

    public LoadSongThumbnailTask(int _imageViewWidth, int _imageViewHeight, ICompletedLoadThumbnailListener _completedLoadListener) {
        imageViewWidth = _imageViewWidth;
        imageViewHeight = _imageViewHeight;
        completedLoadListener = _completedLoadListener;
    }

    @Override
    protected Bitmap doInBackground(Song... songs) {

        Bitmap image = null;
        if (songs != null && songs.length > 0) {
            Song song = songs[0];

            if (song.thumbnail_url != null && !TextUtils.isEmpty(song.thumbnail_url) && song.thumbnail_width > 0 && song.thumbnail_height > 0) {
                image = getImageFromURL(song.thumbnail_url, imageViewWidth, imageViewHeight, song.thumbnail_width, song.thumbnail_height);
                if (image != null)
                    song.thumbnail = image;
            }
        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(this.completedLoadListener != null)
            this.completedLoadListener.onCompletedLoadSongThumbnail(bitmap);
    }

    private Bitmap getImageFromURL(String imageURL, int imageViewWidth, int imageViewHeight, int imageWidth, int imageHeight) {

        Bitmap bitmap = null;
        // Determine how much to scale down the image
        int scaleFactor = 1;
        if (imageViewWidth != 0 && imageViewHeight != 0)
            scaleFactor = Math.min(imageWidth / imageViewWidth, imageHeight / imageViewHeight);

        // Decode the image file into a Bitmap sized to fill the View
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        try
        {
            InputStream in = new java.net.URL(imageURL).openStream();

            Rect outPadding = new Rect(0, 0, 0, 0);
            bitmap = BitmapFactory.decodeStream(in, outPadding, bmOptions);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}




