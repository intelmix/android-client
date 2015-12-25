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
public class AuthenticateUserTask extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private String token;
    private String resString;

    public AuthenticateUserTask(Activity a, String token)
    {
        this.activity = a;
        this.token = token;
    }

    //TODO: start caching expensive data items
    //TODO: change namespaces include intelmix on android and server
    @Override
    protected Void doInBackground(Void... params) {
        try {
            String url = "http://newzrobot.com:8090/auth";

            AuthRequest ar = new AuthRequest();
            ar.setToken(this.token);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            AuthResponse result = restTemplate.postForObject(url, ar, AuthResponse.class);
            resString = result.getToken();
        } catch (Exception e) {
            resString = e.getMessage();

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Toast.makeText(this.activity.getApplicationContext(),
                resString, Toast.LENGTH_LONG)
                .show();

        new GetNewsTask((MainActivity)activity, null).execute();
        new GetNewsCountTask((MainActivity)activity).execute();
    }
}
