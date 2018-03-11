package pt.olive.katappz.spotifysongpreviewplayer.presenters;

import android.text.TextUtils;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import pt.olive.katappz.spotifysongpreviewplayer.Consts;
import pt.olive.katappz.spotifysongpreviewplayer.R;
import pt.olive.katappz.spotifysongpreviewplayer.SearchSongsTask;
import pt.olive.katappz.spotifysongpreviewplayer.SongListAdapter;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.ICompletedPlaySongPreview;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.ICompletedSongSearchListener;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.IPlaySongPreviewView;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.ISearchSongsView;
import pt.olive.katappz.spotifysongpreviewplayer.models.Song;

/**
 * Search songs view presenter class.
 * @author Katy
 * @version 1.0
 * @since   2018-02-01
 */
public class SearchSongsPresenter
        implements ICompletedSongSearchListener, ICompletedPlaySongPreview {

    private ISearchSongsView view;
    private ArrayList<Song> model;
    private SongListAdapter listAdapter;

    /**
     * Constructor
     * @param _view the presenter's view instance.
     */
    public SearchSongsPresenter(ISearchSongsView _view) {
        view = _view;
        model = new ArrayList<>();
    }

    /**
     * Searches on spotify for songs that match the input query.
     * @param query user query.
     */
    public void doSearch(String query) {
        if (query != null && !TextUtils.isEmpty(query)) {

            if(TextUtils.isEmpty(Consts.SPOTIFY_CLIENT_ID) || TextUtils.isEmpty(Consts.SPOTIFY_SECRET)) {
                Toast.makeText(view.getViewContext(), R.string.missingCredentials, Toast.LENGTH_LONG).show();
                return;
            }
            view.showProgressBar();
            SearchSongsTask task = new SearchSongsTask(this);
            task.execute(query);
        }
    }

    /**
     * Handles the onCompleted song search event. (ICompletedSongSearchListener contract)
     * @param searchResults the search results found for the search song query.
     */
    @Override
    public void onCompletedSongSearch(ArrayList<Song> searchResults) {
        view.hideProgressBar();

        if (searchResults != null) {

            model = new ArrayList<>(searchResults);
            listAdapter = new SongListAdapter(view.getViewContext(), model);

            view.setSongListAdapter(listAdapter);

            if (searchResults.size() > 0) {
                view.hideNoResultsFoundMessage();
            }
            else {
                view.showNoResultsFoundMessage();
            }
        }
    }

    /**
     * Handles the view's onClicked play preview event.
     * @param index the list index of the clicked item.
     */
    public void onClickedPlayPreview(int index)
    {
        if (this.listAdapter != null) {

            Song song = (Song) listAdapter.getItem(index);

            if (song != null && song.preview_url != null && !TextUtils.isEmpty(song.preview_url)) {

                IPlaySongPreviewView playSongPreviewView = view.getPlaySongPreviewView();
                if(playSongPreviewView == null) {

                    HashMap<String, Serializable> data = new HashMap<>();
                    data.put(Consts.PREVIEW_TRACK_URL, song.preview_url);
                    data.put(Consts.TRACK_NAME, song.name);
                    data.put(Consts.TRACK_ARTIST, song.artist);

                    playSongPreviewView = view.showPlaySongPreviewView(data);
                }
                else {

                    PlaySongPreviewPresenter playSongPreviewPresenter = playSongPreviewView.getPresenterInstance();
                    if (playSongPreviewPresenter != null) {
                        playSongPreviewPresenter.setSong(song.preview_url, song.name, song.artist);
                    }
                }

                playSongPreviewView.setOnCompletedPlaySongPreviewListener(this);
            }
        }
    }

    /**
     * Handles the on close play song preview event (ICompletedPlaySongPreview contract).
     */
    @Override
    public void closePlaySongPreview() {
        view.hidePlaySongPreviewView();
    }

    /**
     * Handles the onSelectedMenu option event from the view.
     * @param optionId the menu option's Id
     * @return
     */
    public boolean onSelectedMenuOption(int optionId) {
        if (optionId == R.id.action_about) {
            view.showAbout();
        }
        return true;
    }
}

