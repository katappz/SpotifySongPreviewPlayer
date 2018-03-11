package pt.olive.katappz.spotifysongpreviewplayer.interfaces;

import java.util.ArrayList;

import pt.olive.katappz.spotifysongpreviewplayer.models.Song;

/**
 * OnCompleted the song search listener contract.
 * @author Katy
 * @version 1.0
 * @since   2018-02-03
 */
public interface ICompletedSongSearchListener {
    /**
     * Handles the on completed song search.
     * @param searchResults the search results found for the search song query.
     */
    void onCompletedSongSearch(ArrayList<Song> searchResults);
}
