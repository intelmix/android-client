package com.intelmix.newzrobot.android.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.intelmix.newzrobot.android.MainActivity;
import com.intelmix.newzrobot.android.data.NewsItem;
import newzrobot.com.newzrobot.R;

/**
 * Created by mahdi on 12/25/15.
 */
public class GetNewsTask extends AsyncTask<Void, Void, NewsItem[]> {

    private MainActivity activity;
    private String searchText;

    public GetNewsTask(MainActivity a, String searchText)
    {
        this.activity = a;
        this.searchText = searchText;
    }

    @Override
    protected NewsItem[] doInBackground(Void... params) {
        try {
            String url = "http://newzrobot.com:8090/news";

            if ( searchText != null ) {
                url = "http://newzrobot.com:8090/search/"+searchText;
            }

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            NewsItem[] result = restTemplate.getForObject(url, NewsItem[].class);

            return result;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(NewsItem[] greeting) {
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(activity, R.layout.itemlistrow, greeting);
        ListView listView = (ListView) this.activity.findViewById(R.id.list);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

    }

    private class MySimpleArrayAdapter extends ArrayAdapter<NewsItem> {
        private final Context context;
        private final NewsItem[] values;

        public MySimpleArrayAdapter(Context context, int resource, NewsItem[] items) {
            super(context, resource, items);
            this.context = context;
            this.values = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsItem p = this.getItem(position);

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.itemlistrow, null);
            }

            if (p != null) {
                TextView txt_source = (TextView) v.findViewById(R.id.news_source);
                TextView txt_ts = (TextView) v.findViewById(R.id.news_timestamp);
                TextView txt_title = (TextView) v.findViewById(R.id.news_title);

                txt_source.setText(p.getSource());
                txt_ts.setText(Long.toString(p.getTimestamp()));
                txt_title.setText(p.getTitle());

                v.setTag(p.getLink());
            }

            return v;
        }
    }
}