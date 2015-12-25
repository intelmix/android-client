package com.intelmix.newzrobot.android.tasks;

import android.os.AsyncTask;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.intelmix.newzrobot.android.MainActivity;
import com.intelmix.newzrobot.android.data.AuthMessage;

import newzrobot.com.newzrobot.R;

/**
 * Created by mahdi on 12/25/15.
 */
public class AuthenticateUserTask extends AsyncTask<Void, Void, String> {

    private MainActivity activity;
    private String token;
    private static RestTemplate restTemplate = new RestTemplate();
    private String errorString = null;
    private static String cachedServerToken = null;

    static {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public AuthenticateUserTask(MainActivity activity, String token)
    {
        this.activity = activity;
        this.token = token;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            if ( cachedServerToken == null ) {
                String url = "http://newzrobot.com:8090/auth";

                AuthMessage result = restTemplate.postForObject(url, new AuthMessage(this.token), AuthMessage.class);

                cachedServerToken = result.getToken();
            }

            return cachedServerToken;
        } catch (Exception e) {
            errorString = e.getMessage();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String serverToken) {
        if ( serverToken != null ) {
            //inform main activity that user is authenticated so news will be refreshed
            this.activity.onUserAuthenticated(serverToken);
        } else {
            String message = this.activity.getString(R.string.error_auth_server, errorString);
            Toast.makeText(this.activity, message, Toast.LENGTH_LONG).show();
        }
    }
}
