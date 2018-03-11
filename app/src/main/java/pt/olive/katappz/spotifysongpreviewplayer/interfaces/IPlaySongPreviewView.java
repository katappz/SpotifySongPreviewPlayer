package pt.olive.katappz.spotifysongpreviewplayer.interfaces;

import java.io.Serializable;
import java.util.Map;

import pt.olive.katappz.spotifysongpreviewplayer.presenters.PlaySongPreviewPresenter;

/**
 * 30 second song preview player view.
 * @author Katy
 * @version 1.0
 * @since   2018-02-05
 */
public interface IPlaySongPreviewView {

    /**
     * Shows the song's name on the play song preview view.
     * @param songName song name (Example : The Handler)
     */
    void setSongName(String songName);

    /**
     * Shows the artist's name on the play song preview view.
     * @param artistName artist name (example: Muse)
     */
    void setArtistName(String artistName);

    /**
     * Indicates if the media player is initialized.
     * @return a value indicating if it is initialized or not.
     */
    boolean isPlayerInitialized();

    /**
     * Initializes the media player.
     */
    void initializePlayer();

    /**
     * Plays the song 30 seconds preview.
     * @param songUrl the song preview URL.
     */
    void playSong(String songUrl);

    /**
     * Stops playing the song preview.
     */
    void stopPlayer();

    /**
     * Pauses the song preview.
     */
    void pausePlayer();

    /**
     * Indicates if the media player is playing the song 30 seconds preview.
     * @return a value indicating if the song preview is playing or not.
     */
    boolean isPlaying();

    /**
     * Sets the media player's position.
     * @param i the position (0 - 30).
     */
    void setPlayerPosition(int i);

    /**
     * Gets an instance of the view's presenter instance.
     * @return PlaySongPreviewPresenter instance.
     */
    PlaySongPreviewPresenter getPresenterInstance();

    /**
     * Gets the bundle arguments passed to the view.
     * @return A hashmap with the key value pairs of arguments passed to the view.
     */
    Map<String, Serializable> getBundleArguments();

    /**
     * Sets a reference to the OnCompletedPlaySongPreview listener contract.
     * @param listener a reference to the listener.
     */
    void setOnCompletedPlaySongPreviewListener(ICompletedPlaySongPreview listener);
}
