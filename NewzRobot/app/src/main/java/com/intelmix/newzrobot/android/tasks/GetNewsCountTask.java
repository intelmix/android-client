package com.intelmix.newzrobot.android.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.intelmix.newzrobot.android.MainActivity;
import newzrobot.com.newzrobot.R;

/**
 * Created by mahdi on 12/25/15.
 */
public class GetNewsCountTask extends AsyncTask<Void, Void, Integer> {

    private MainActivity activity;

    public GetNewsCountTask(MainActivity a)
    {
        this.activity = a;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        try {
            String url = "http://newzrobot.com:8090/newsCount";

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            String result = restTemplate.getForObject(url, String.class);

            return Integer.valueOf(result);
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer count) {
        EditText searchBox = (EditText) this.activity.findViewById(R.id.search_box);
        searchBox.setHint("Search through " + count.toString() + " news...");
    }
}


