package pt.olive.katappz.spotifysongpreviewplayer.presenters;

import java.io.Serializable;
import java.util.Map;
import pt.olive.katappz.spotifysongpreviewplayer.Consts;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.IPlaySongPreviewView;
import pt.olive.katappz.spotifysongpreviewplayer.models.Song;

/**
 * Play song 30 seconds preview view presenter class.
 * @author Katy
 * @version 1.0
 * @since   2018-02-07
 */
public class PlaySongPreviewPresenter {

    private IPlaySongPreviewView view;
    private Song model;

    /**
     * Constructor
     * @param _view the presenter's view instance.
     */
    public PlaySongPreviewPresenter(IPlaySongPreviewView _view)
    {
        view = _view;
        model = new Song();
    }

    /**
     * Handles the onCreateView event from the view.
     */
    public void onCreateView() {

        Map<String, Serializable> args = view.getBundleArguments();
        if(args != null) {
            if (args.containsKey(Consts.PREVIEW_TRACK_URL))
                model.preview_url = (String) args.get(Consts.PREVIEW_TRACK_URL);
            if (args.containsKey(Consts.TRACK_NAME))
                model.name = (String) args.get(Consts.TRACK_NAME);
            if (args.containsKey(Consts.TRACK_ARTIST))
                model.artist = (String) args.get(Consts.TRACK_ARTIST);

            view.setSongName(model.name);
            view.setArtistName(model.artist);
            view.initializePlayer();

            onClickedPlay();
        }
    }

    /**
     * Handles the onClicked play event from the view.
     */
    public void onClickedPlay() {

        if(!view.isPlayerInitialized())
            view.initializePlayer();

        //pause:
        if(view.isPlaying()){
            view.pausePlayer();
            return;
        }
        //play
        if(model.preview_url != null) {
            String url = model.preview_url;
            if (!model.preview_url.contains(".mp3"))
                url = model.preview_url + ".mp3";

            view.playSong(url);
        }
    }

    /**
     * Handles the on change player position event from the view.
     * @param position the intended position (0 to 30)
     */
    public void onChangePlayerPosition(int position) {
        if(!view.isPlayerInitialized())
            view.initializePlayer();
        view.setPlayerPosition(position);
    }

    /**
     * Sets the current model data (i.e. song info), stops the view's media player and initializes it with the new song.
     * @param songPreviewUrl the song's preview url provided by spotify.
     * @param name the song's name
     * @param artist the song's artist name.
     */
    public void setSong(String songPreviewUrl, String name, String artist)
    {
        model.preview_url = songPreviewUrl;
        model.name = name;
        model.artist = artist;

        view.stopPlayer();
        view.setSongName(model.name);
        view.setArtistName(model.artist);
        view.initializePlayer();
    }
}
