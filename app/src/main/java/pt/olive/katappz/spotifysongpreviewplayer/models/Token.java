package pt.olive.katappz.spotifysongpreviewplayer.models;

import java.util.Calendar;

/**
 * Authentication token information.
 * @author Katy
 * @version 1.0
 * @since   2018-02-03
 */
public class Token {

    public String access_token;
    public int expires_in;
    public Calendar expireDate;

    public boolean isTokenExpired() {
        return Calendar.getInstance().compareTo(expireDate) < 0;
    }
}


