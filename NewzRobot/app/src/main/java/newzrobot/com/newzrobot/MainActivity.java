package newzrobot.com.newzrobot;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


       listView = (ListView) findViewById(R.id.list);
//        NewsItem[] greeting = new NewsItem[3];
//        greeting[0] = new NewsItem("A", "B", "C", 1);
//        greeting[1] = new NewsItem("A1", "B1", "C1", 2);
//        greeting[2] = new NewsItem("A2", "B2", "C2", 3);
//
//        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, greeting);
//
//        // Assign adapter to ListView
//        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                NewsItem itemValue = (NewsItem) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + itemValue.getLink(), Toast.LENGTH_LONG)
                        .show();

            }

        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        new HttpRequestTask(this).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class HttpRequestTask extends AsyncTask<Void, Void, NewsItem[]> {

        private MainActivity activity;

        public HttpRequestTask(MainActivity a)
        {
            this.activity = a;
        }

        @Override
        protected NewsItem[] doInBackground(Void... params) {
            try {
                final String url = "http://newzrobot.com:8090/news?name=AndroidClient";
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

            // Assign adapter to ListView
            listView.setAdapter(adapter);

        }

    }
}
