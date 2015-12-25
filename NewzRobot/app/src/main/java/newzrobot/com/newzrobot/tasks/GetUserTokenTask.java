package newzrobot.com.newzrobot.tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

import newzrobot.com.newzrobot.MainActivity;
import newzrobot.com.newzrobot.data.Common;

/**
 * Created by mahdi on 12/25/15.
 */
//TODO: change name of this and move all classes to other files
public class GetUserTokenTask extends AsyncTask<Void, Void, String> {
    MainActivity mActivity;
    String mScope;
    String mEmail;
    String temp;

    static String googleToken = null;

    public GetUserTokenTask(MainActivity activity, String name, String scope) {
        this.mActivity = activity;
        this.mScope = scope;
        this.mEmail = name;
    }

    /**
     * Executes the asynchronous job. This runs when you call execute()
     * on the AsyncTask instance.
     */
    @Override
    protected String doInBackground(Void... params) {
        String token = null;
        try {
            try {
                token = fetchToken();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            if (token != null) {
                // **Insert the good stuff here.**
                // Use the token to access the user's Google data.
                temp = "12";
            }
        } catch (IOException e) {
            // The fetchToken() method handles Google-specific exceptions,
            // so this indicates something went wrong at a higher level.
            // TIP: Check for network connectivity before starting the AsyncTask.
        }
        return token;
    }

    /**
     * Gets an authentication token from Google and handles any
     * GoogleAuthException that may occur.
     */
    protected String fetchToken() throws IOException, GoogleAuthException {
        if ( googleToken != null ) return googleToken;

        try {
            String token = GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
            googleToken = token;
            return token;
        } catch (UserRecoverableAuthException e) {
            // GooglePlayServices.apk is either old, disabled, or not present
            // so we need to show the user some UI in the activity to recover.
            //mActivity.handleException(userRecoverableException);
            temp = "12";

            //this will happen only on the first ever time this app requires
            //authentication from user
            this.mActivity.startActivityForResult(e.getIntent(), Common.REQUEST_AUTHORIZATION);
            //String token2 = GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
        } catch (GoogleAuthException fatalException) {
            // Some other type of unrecoverable exception has occurred.
            // Report and log the error as appropriate for your app.
            temp = "12";
        }
        return null;
    }

    @Override
    protected void onPostExecute(String token) {
        new AuthenticateUserTask(mActivity, token).execute();
    }
}
