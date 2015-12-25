package newzrobot.com.newzrobot;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;


import newzrobot.com.newzrobot.data.NewsItem;
import newzrobot.com.newzrobot.tasks.*;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    MainActivity me = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        me = this;

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
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
                String link = view.getTag().toString();

                // ListView Clicked item value
                NewsItem itemValue = (NewsItem) listView.getItemAtPosition(position);

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(link));
                startActivity(i);
//
//                // Show Alert
//                Toast.makeText(getApplicationContext(),
//                        "Position :" + itemPosition + "  ListItem : " + itemValue.getLink(), Toast.LENGTH_LONG)
//                        .show();


            }

        });


        EditText searchBox = (EditText) findViewById(R.id.search_box);

        searchBox.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();

                if ( searchText.length() == 0 ) searchText = null;

                new GetNewsTask(me, searchText).execute();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //TODO: better UI handling upon start
        pickUserAccount();


    }

    String mEmail; // Received from newChooseAccountIntent(); passed to getToken()
    //String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
    String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email";
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);

                SharedPreferences prefs = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("default_account", mEmail);
                editor.commit();

                // With the account name acquired, go get the auth token
                getUsername();

                //new GetNewsTask(this, null).execute();
            } else if (resultCode == RESULT_CANCELED) {
                // The account picker dialog closed without selecting an account.
                // Notify users that they must pick an account to proceed.
                Toast.makeText(this, "R.string.pick_account", Toast.LENGTH_SHORT).show();
            }
        }

        if ( requestCode == REQUEST_AUTHORIZATION ) {
            String x = data.getDataString();

            //again try to get the access token now that app is authorized by the user
            new GetUserTokenTask(MainActivity.this, mEmail, SCOPE).execute();
            //String token = GoogleAuthUtil.getToken(this, mEmail, SCOPE);

        }
    }

    /** Checks whether the device currently has a network connection */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * Attempts to retrieve the username.
     * If the account is not yet known, invoke the picker. Once the account is known,
     * start an instance of the AsyncTask to get the auth token and do work with it.
     */
    //TOTO: Get user token
    private void getUsername() {
        if (mEmail == null) {
            pickUserAccount();
        } else {
            if (isDeviceOnline()) {
                new GetUserTokenTask(MainActivity.this, mEmail, SCOPE).execute();
            } else {
                Toast.makeText(this, "R.string.not_online", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    public static final int REQUEST_AUTHORIZATION = 12121;

    private void pickUserAccount() {
        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);

        intent.addCategory("android.intent.category.DEFAULT");

        SharedPreferences prefs = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String defaultAccount = prefs.getString("default_account", null);

        if ( defaultAccount == null ) {
            startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
        } else {
            mEmail = defaultAccount;
            getUsername();
        }


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








}
