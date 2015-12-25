package newzrobot.com.newzrobot.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import newzrobot.com.newzrobot.MainActivity;
import newzrobot.com.newzrobot.data.AuthRequest;
import newzrobot.com.newzrobot.data.AuthResponse;

/**
 * Created by mahdi on 12/25/15.
 */
public class AuthenticateUserTask extends AsyncTask<Void, Void, String> {

    private MainActivity activity;
    private String token;
    private String resString;
    private static RestTemplate restTemplate = new RestTemplate();

    static {
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public AuthenticateUserTask(MainActivity a, String token)
    {
        this.activity = a;
        this.token = token;
    }

    //TODO: change namespaces include intelmix on android and server
    @Override
    protected String doInBackground(Void... params) {
        try {
            String url = "http://newzrobot.com:8090/auth";

            AuthRequest ar = new AuthRequest();
            ar.setToken(this.token);

            AuthResponse result = restTemplate.postForObject(url, ar, AuthResponse.class);
            resString = result.getToken();
        } catch (Exception e) {
            resString = e.getMessage();

        }

        return resString;
    }

    @Override
    protected void onPostExecute(String token) {
        /*Toast.makeText(this.activity.getApplicationContext(),
                resString, Toast.LENGTH_LONG)
                .show();*/

        //inform main activity that user is authenticated so news will be refreshed
        this.activity.onUserAuthenticated(token);
    }
}
