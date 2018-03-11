package pt.olive.katappz.spotifysongpreviewplayer.interfaces;

import android.content.Context;

import java.io.Serializable;
import java.util.HashMap;

import pt.olive.katappz.spotifysongpreviewplayer.SongListAdapter;

/**
 * Search song view contract.
 * @author Katy
 * @version 1.0
 * @since   2018-01-31
 */
public interface ISearchSongsView {

    /**
     * shows the progress bar.
     */
    void showProgressBar();

    /**
     * hides the progress bar.
     */
    void hideProgressBar();

    /**
     * shows the no results found message.
     */
    void showNoResultsFoundMessage();

    /**
     * hides the no results found message.
     */
    void hideNoResultsFoundMessage();

    /**
     * Sets the song list listAdapter instance.
     * @param adapter list adapter instance.
     */
    void setSongListAdapter(SongListAdapter adapter);

    /**
     * Shows the play song preview view.
     * @param bundleArguments hashmap with key value pairs with arguments to pass to the play preview view.
     * @return the play song preview view contract.
     */
    IPlaySongPreviewView showPlaySongPreviewView(HashMap<String, Serializable> bundleArguments);

    /**
     * Hides the play song preview view.
     */
    void hidePlaySongPreviewView();

    /**
     * Gets the play song preview view instance.
     * @return
     */
    IPlaySongPreviewView getPlaySongPreviewView();

    /**
     * Gets the view's context.
     * @return Context instance
     */
    Context getViewContext();

    /**
     * Shows some info about this app and katappz aka me :)
     */
    void showAbout();
}
