package pt.olive.katappz.spotifysongpreviewplayer.views;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import pt.olive.katappz.spotifysongpreviewplayer.Consts;
import pt.olive.katappz.spotifysongpreviewplayer.R;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.ICompletedPlaySongPreview;
import pt.olive.katappz.spotifysongpreviewplayer.interfaces.IPlaySongPreviewView;
import pt.olive.katappz.spotifysongpreviewplayer.presenters.PlaySongPreviewPresenter;

/**
 * 30 second song preview player view.
 * @author Katy
 * @version 1.0
 * @since   2018-02-05
 */
public class PlaySongPreviewFragment extends Fragment implements Runnable, IPlaySongPreviewView {

    private PlaySongPreviewPresenter presenter;
    private ImageView playButton = null;
    private SeekBar seekBar = null;
    private TextView tv_name = null;
    private TextView tv_artist = null;
    private MediaPlayer mediaPlayer = null;
    private ICompletedPlaySongPreview completedPlaySongPreviewListener;

    public PlaySongPreviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_play_song_preview, container, false);

        tv_name = (TextView) v.findViewById(R.id.tv_previewName);
        tv_artist = (TextView) v.findViewById(R.id.tv_previewArtist);

        playButton = (ImageView) v.findViewById(R.id.iv_play);
        playButton.setOnClickListener(onClickPlayListener);

        seekBar = (SeekBar) v.findViewById(R.id.seekBar_playMusic);

        if (seekBar != null) {
            seekBar.setProgress(0);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b)
                        presenter.onChangePlayerPosition(i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }

        presenter = new PlaySongPreviewPresenter(this);
        presenter.onCreateView();

        return v;
    }

    /**
     * Gets an instance of the view's presenter instance.
     * @return PlaySongPreviewPresenter instance.
     */
    @Override
    public PlaySongPreviewPresenter  getPresenterInstance() {
        return presenter;
    }

    /**
     * Gets the bundle arguments passed to the view.
     * @return A hashmap with the key value pairs of arguments passed to the view.
     */
    @Override
    public Map<String, Serializable> getBundleArguments() {
        Map<String, Serializable> arguments = new HashMap<>();
        Bundle args = getArguments();
        if (args != null) {
            Serializable previewUrl = args.getSerializable(Consts.PREVIEW_TRACK_URL);
            arguments.put(Consts.PREVIEW_TRACK_URL, previewUrl);
            Serializable name = args.getSerializable(Consts.TRACK_NAME);
            arguments.put(Consts.TRACK_NAME, name);
            Serializable artist = args.getSerializable(Consts.TRACK_ARTIST);
            arguments.put(Consts.TRACK_ARTIST, artist);
        }
        return arguments;
    }

    /**
     * Sets a reference to the OnCompletedPlaySongPreview listener contract.
     * @param listener a reference to the listener.
     */
    @Override
    public void setOnCompletedPlaySongPreviewListener(ICompletedPlaySongPreview listener) {
        completedPlaySongPreviewListener = listener;
    }

    /**
     * Shows the song's name on the play song preview view.
     * @param songName song name (Example : The Handler)
     */
    @Override
    public void setSongName(String songName) {
        if(tv_name != null)
            tv_name.setText(songName);
    }

    /**
     * Shows the artist's name on the play song preview view.
     * @param artistName artist name (example: Muse)
     */
    @Override
    public void setArtistName(String artistName) {
        if(tv_artist != null)
            tv_artist.setText(artistName);
    }

    /**
     * Indicates if the media player is initialized.
     * @return a value indicating if it is initialized or not.
     */
    @Override
    public boolean isPlayerInitialized()
    {
        return mediaPlayer != null;
    }

    /**
     * Initializes the media player.
     */
    @Override
    public void initializePlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(onPreparedListener);
            mediaPlayer.setOnCompletionListener(onCompletionListener);
        }
    }

    /**
     * Plays the song 30 seconds preview.
     * @param songUrl the song preview URL.
     */
    @Override
    public void playSong(String songUrl) {
        if (seekBar.getProgress() > 0) {
            playButton.setImageResource(R.drawable.ic_pause_white_24dp);
            mediaPlayer.start();
        } else {
            //prepare:
            playButton.setImageResource(R.drawable.ic_pause_white_24dp);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(songUrl);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stops playing the song preview.
     */
    @Override
    public void stopPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            seekBar.setProgress(0);
        }
        playButton.callOnClick();
    }

    /**
     * Pauses the song preview.
     */
    @Override
    public void pausePlayer() {
        playButton.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        mediaPlayer.pause();
    }

    /**
     * Indicates if the media player is playing the song 30 seconds preview.
     * @return a value indicating if the song preview is playing or not.
     */
    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    /**
     * Sets the media player's position.
     * @param i the position (0 - 30).
     */
    @Override
    public void setPlayerPosition(int i) {
        mediaPlayer.seekTo(i);
    }

    /**
     * play song thread.
     */
    @Override
    public void run() {
        int currentPosition = 0;
        if (mediaPlayer != null) {
            int total = mediaPlayer.getDuration();
            while (mediaPlayer != null
                    && currentPosition < total) {
                try {
                    Thread.sleep(50);
                    if (mediaPlayer != null) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    currentPosition = total;
                }
            }
        }
    }

    /**
     * on Click play button listener implementation.
     */
    private View.OnClickListener onClickPlayListener  = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            presenter.onClickedPlay();
        }
    };

    /**
     * Media player's on prepared listener implementation. Starts playing the song's preview.
     */
    private MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
            new Thread(PlaySongPreviewFragment.this).start();
        }
    };


    /**
     * Media player's on completed listener implementation (when song's preview has come to an end).
     * Cleans resources and closes the preview.
     */
    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {

            mediaPlayer.reset();
            mediaPlayer.release();

            if(completedPlaySongPreviewListener != null)
                completedPlaySongPreviewListener.closePlaySongPreview();
        }
    };

    /**
     * cleans resources.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            if(mediaPlayer != null) {
                mediaPlayer.release();
            }
        }
        catch (Exception e){
            Log.e("PlaySongPreviewFragment", "releasing player");
        }
    }
}
