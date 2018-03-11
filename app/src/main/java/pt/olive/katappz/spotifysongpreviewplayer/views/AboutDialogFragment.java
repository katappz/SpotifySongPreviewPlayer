package pt.olive.katappz.spotifysongpreviewplayer.views;


import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import pt.olive.katappz.spotifysongpreviewplayer.BuildConfig;
import pt.olive.katappz.spotifysongpreviewplayer.R;


/**
 * Dialog with some info about this app.
 * @author Katy
 * @version 1.0
 * @since   2018-02-13
 */
public class AboutDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View v = View.inflate(getActivity(), R.layout.fragment_about_dialog, null);

        Dialog dialog = new Dialog(getActivity(), R.style.DialogFragment);
        dialog.setContentView(v);

        TextView tv_version = (TextView) v.findViewById(R.id.tv_version);
        String versionText = tv_version.getText() + " " + BuildConfig.VERSION_NAME;
        tv_version.setText(versionText);

        dialog.setTitle(R.string.about);
        return dialog;
    }

}
