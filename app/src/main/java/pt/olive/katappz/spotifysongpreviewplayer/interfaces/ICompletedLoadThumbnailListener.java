package pt.olive.katappz.spotifysongpreviewplayer.interfaces;

import android.graphics.Bitmap;

/**
 * OnCompleted load song thumbnail image listener contract.
 * @author Katy
 * @version 1.0
 * @since   2018-02-07
 */
public interface ICompletedLoadThumbnailListener {
    /**
     * Listener to execute when thumbnail image has been loaded.
     * @param thumbnailImage the thumbnail bitmap
     */
    void onCompletedLoadSongThumbnail(Bitmap thumbnailImage);
}
