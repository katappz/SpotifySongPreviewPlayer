package pt.olive.katappz.spotifysongpreviewplayer.views;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.Serializable;
import java.util.HashMap;

import pt.olive.katappz.spotifysongpreviewplayer.Consts;
import pt.olive.katappz.spotifysongpreviewplayer.R;
import pt.olive.katappz.spotifysongpreviewplayer.SongListAdapter;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.IPlaySongPreviewView;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.ISearchSongsView;
import pt.olive.katappz.spotifysongpreviewplayer.presenters.SearchSongsPresenter;

/**
 * Search songs on spotify view.
 * @author Katy
 * @version 1.0
 * @since   2018-02-05
 */
public class SearchSongsActivity extends AppCompatActivity implements ISearchSongsView {

    private ListView lv_songs;
    private CardView cv_playPreviewContainer;
    private PlaySongPreviewFragment playSongPreviewFragment = null;

    private FrameLayout progressBarFrameLayout = null;
    private ProgressBar progressBar = null;

    SearchSongsPresenter presenter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_song);

        setTitle("");

        lv_songs = (ListView) findViewById(R.id.lv_songs);
        cv_playPreviewContainer = (CardView)findViewById(R.id.cv_playPreviewContainer);

        progressBarFrameLayout = (FrameLayout) findViewById(R.id.fl_loading);
        progressBar = (ProgressBar) findViewById(R.id.pb_loading);

        presenter = new SearchSongsPresenter(this);
    }

    /**
     * Gets the view's context.
     * @return Context instance
     */
    @Override
    public Context getViewContext() {
        return this;
    }


    /**
     * Handles the search action intent and searches songs on spotify with the given search query.
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent)
    {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String searchQuery = intent.getStringExtra(SearchManager.QUERY);
            presenter.doSearch(searchQuery);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        presenter.onSelectedMenuOption(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgressBar() {
        if(progressBarFrameLayout != null)
            progressBarFrameLayout.setVisibility(View.VISIBLE);
        if(progressBar != null)
            progressBar.setIndeterminate(true);
    }

    @Override
    public void hideProgressBar() {
        if (progressBarFrameLayout != null)
            progressBarFrameLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * shows the no results found message.
     */
    @Override
    public void showNoResultsFoundMessage() {
        View v = findViewById(R.id.tv_noResultsSearchTracks);
        if(v != null)
            v.setVisibility(View.VISIBLE);
    }

    /**
     * hides the no results found message.
     */
    @Override
    public void hideNoResultsFoundMessage() {
        View v = findViewById(R.id.tv_noResultsSearchTracks);
        if(v != null)
            v.setVisibility(View.INVISIBLE);
    }


    /**
     * Sets the song list listAdapter instance.
     * @param adapter list adapter instance.
     */
    @Override
    public void setSongListAdapter(SongListAdapter adapter) {
        if(lv_songs != null) {
            lv_songs.setAdapter(adapter);
            lv_songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    presenter.onClickedPlayPreview(i);
                }
            });
        }
    }

    /**
     * Shows the play song preview view.
     * @param bundleArguments hashmap with key value pairs with arguments to pass to the play preview view.
     * @return the play song preview view contract.
     */
    @Override
    public IPlaySongPreviewView showPlaySongPreviewView(HashMap<String, Serializable> bundleArguments) {
        if (cv_playPreviewContainer != null)
            cv_playPreviewContainer.setVisibility(View.VISIBLE);

        playSongPreviewFragment = new PlaySongPreviewFragment();
        if(bundleArguments != null) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(Consts.PREVIEW_TRACK_URL, bundleArguments.get(Consts.PREVIEW_TRACK_URL));
            arguments.putSerializable(Consts.TRACK_NAME, bundleArguments.get(Consts.TRACK_NAME));
            arguments.putSerializable(Consts.TRACK_ARTIST, bundleArguments.get(Consts.TRACK_ARTIST));
            playSongPreviewFragment.setArguments(arguments);
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.cv_playPreviewContainer, playSongPreviewFragment);
        ft.commit();

        return playSongPreviewFragment;
    }

    /**
     * Hides the play song preview view.
     */
    @Override
    public void hidePlaySongPreviewView() {
        FragmentManager fm =  getSupportFragmentManager();
        if (playSongPreviewFragment != null && fm != null  ) {
            fm.beginTransaction().remove(playSongPreviewFragment).commit();
        }
        if(cv_playPreviewContainer != null)
            cv_playPreviewContainer.setVisibility(View.GONE);
        playSongPreviewFragment = null;
    }

    /**
     * Gets the play song preview view instance.
     * @return
     */
    @Override
    public IPlaySongPreviewView getPlaySongPreviewView() {
        return playSongPreviewFragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_spotify_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        if (searchView != null) {
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);

            searchView.setFocusable(true);
            searchView.requestFocusFromTouch();
        }
        return true;
    }

    /**
     * Shows some info about this app and katappz aka me :)
     */
    @Override
    public void showAbout() {
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("aboutDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        AboutDialogFragment newFragment = new AboutDialogFragment();
        newFragment.show(ft, "aboutDialog");
    }
}
