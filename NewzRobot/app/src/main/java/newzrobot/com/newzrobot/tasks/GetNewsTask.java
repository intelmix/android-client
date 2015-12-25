package newzrobot.com.newzrobot.tasks;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import newzrobot.com.newzrobot.MainActivity;
import newzrobot.com.newzrobot.MySimpleArrayAdapter;
import newzrobot.com.newzrobot.R;
import newzrobot.com.newzrobot.data.NewsItem;

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
//            EditText ed = (EditText) findViewById(R.id.edit_message);
//            ed.setText("Link is " + greeting[0].getLink()+" and title is "+greeting[0].getTitle()+
//                    "second ID is "+greeting[1].getLink()+" and second content is "+greeting[1].getTitle());

        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(activity, R.layout.itemlistrow, greeting);
        ListView listView = (ListView) this.activity.findViewById(R.id.list);
        // Assign adapter to ListView
        listView.setAdapter(adapter);

    }

}