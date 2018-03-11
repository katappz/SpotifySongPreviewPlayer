package pt.olive.katappz.spotifysongpreviewplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import pt.olive.katappz.spotifysongpreviewplayer.interfaces.ICompletedLoadThumbnailListener;
import pt.olive.katappz.spotifysongpreviewplayer.models.Song;

/**
 * Song list adapter implementation.
 * @author Katy
 * @version 1.0
 * @since   2018-02-03
 */
public class SongListAdapter extends BaseAdapter {

    private List<Song> songs;
    private Context context;

    public SongListAdapter(Context _context, List<Song> _songs) {
        context = _context;
        songs = _songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return songs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SongViewHolder viewHolder;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_song, viewGroup, false);

            viewHolder = createSongViewHolder(view);
            view.setTag(viewHolder);
        }
        else {
            viewHolder = (SongViewHolder) view.getTag();
        }

        final Song currentItem = (Song) getItem(i);

        if (currentItem != null) {
            bindSongToViewHolder(viewHolder, currentItem, i);
        }
        return view;
    }

    private SongViewHolder createSongViewHolder(View view)
    {
        SongViewHolder viewHolder = new SongViewHolder();
        viewHolder.iv_thumbnail = (ImageView) view.findViewById(R.id.imgV_thumbnail);
        viewHolder.pb_loading = (ProgressBar) view.findViewById(R.id.pb_thumbnail);
        viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        viewHolder.tv_artist = (TextView) view.findViewById(R.id.tv_artist);
        viewHolder.iv_playButton = (ImageView) view.findViewById(R.id.iv_playPreview);
        viewHolder.iv_playButtonBackground = view.findViewById(R.id.iv_playPreviewBackground);
        return viewHolder;
    }

    private void bindSongToViewHolder(final SongViewHolder viewHolder, Song track, int indexInList)
    {
        viewHolder.tv_name.setText(track.name);
        viewHolder.tv_artist.setText(track.artist);

        //preview
        if (viewHolder.iv_playButton != null && track.preview_url != null) {
            viewHolder.iv_playButton.setTag(indexInList);
        }

        //thumbnail:
        if (track.thumbnail != null) {
            viewHolder.iv_thumbnail.setImageBitmap(track.thumbnail);
            viewHolder.pb_loading.setVisibility(View.GONE);
            viewHolder.iv_playButton.setVisibility(View.VISIBLE);
            viewHolder.iv_playButtonBackground.setVisibility(View.VISIBLE);
        }
        else if (track.thumbnail_url != null && !TextUtils.isEmpty(track.thumbnail_url)) {

            LoadSongThumbnailTask loadImageTask = new LoadSongThumbnailTask(viewHolder.iv_thumbnail.getWidth(), viewHolder.iv_thumbnail.getHeight(), new ICompletedLoadThumbnailListener() {
                @Override
                public void onCompletedLoadSongThumbnail(Bitmap thumbnailImage) {
                    if (viewHolder.pb_loading != null)
                        viewHolder.pb_loading.setVisibility(View.INVISIBLE);
                    if (viewHolder.iv_thumbnail != null && thumbnailImage != null)
                        viewHolder.iv_thumbnail.setImageBitmap(thumbnailImage);

                    if (viewHolder.iv_playButton != null)
                        viewHolder.iv_playButton.setVisibility(View.VISIBLE);
                    if (viewHolder.iv_playButtonBackground != null)
                        viewHolder.iv_playButtonBackground.setVisibility(View.VISIBLE);

                }
            });
            loadImageTask.execute(track);
        }
    }

    class SongViewHolder {

        TextView tv_name;
        TextView tv_artist;
        ImageView iv_thumbnail;
        ProgressBar pb_loading;

        //play song
        ImageView iv_playButton;
        View iv_playButtonBackground;
    }

}
