package com.intelmix.newzrobot.android.tasks;

import android.app.Dialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.intelmix.newzrobot.android.MainActivity;
import com.intelmix.newzrobot.android.data.Common;

import newzrobot.com.newzrobot.R;

/**
 * Created by mahdi on 12/25/15.
 */
public class GetUserTokenTask extends AsyncTask<Void, Void, String> {
    private MainActivity mActivity;
    private String mScope;
    private String mEmail;
    private Exception exception = null;

    private static String cachedToken = null;

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
        try {
            if ( cachedToken == null ) {
                cachedToken = GoogleAuthUtil.getToken(mActivity, mEmail, mScope);
            }

            return cachedToken;
        } catch (Exception e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String token) {
        if ( exception == null ) {
            new AuthenticateUserTask(mActivity, token).execute();
        } else if (exception instanceof GooglePlayServicesAvailabilityException) {
                // The Google Play services APK is old, disabled, or not present.
                // Show a dialog created by Google Play services that allows
                // the user to update the APK
                int statusCode = ((GooglePlayServicesAvailabilityException)exception)
                        .getConnectionStatusCode();
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                        mActivity,
                        Common.REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                dialog.show();
        } else if (exception instanceof UserRecoverableAuthException) {
            //this will happen only on the first ever time this app requires
            //authentication from user
            this.mActivity.startActivityForResult(
                    ((UserRecoverableAuthException)exception).getIntent(), Common.REQUEST_AUTHORIZATION);

        } else if (exception instanceof GoogleAuthException) {
            // Some other type of unrecoverable exception has occurred.
            Toast.makeText(this.mActivity, R.string.error_auth_google, Toast.LENGTH_LONG).show();

        } else if (exception instanceof IOException) {
            Toast.makeText(this.mActivity, R.string.error_auth_io, Toast.LENGTH_LONG).show();
        }
    }
}
