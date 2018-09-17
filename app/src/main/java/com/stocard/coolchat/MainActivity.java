package com.stocard.coolchat;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView chatListView;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatListView = findViewById(R.id.chat_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchMessages();

    }

    private AsyncTask<String, String, String> fetchMessages() {
        return new JsonTask().execute("https://android-hackschool.herokuapp.com");
    }

    /**
     * Borrowed form https://stackoverflow.com/a/37525989/570168.
     */
    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                Thread.sleep(1000); // for some more drama on fast networks ;-)
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d("Response: ", "> " + line);
                }

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pd.isShowing()) {
                pd.dismiss();
            }

            Moshi moshi = new Moshi.Builder().build();
            Type type = Types.newParameterizedType(List.class, Message.class);
            JsonAdapter<List<Message>> jsonAdapter = moshi.adapter(type);
            List<Message> messages = null;
            try {
                messages = jsonAdapter.fromJson(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            showMessages(messages);
        }
    }

    private void showMessages(List<Message> messages) {
        List<String> messageItems = new ArrayList<>();
        for (Message message : messages) {
            messageItems.add(message.getName() + ": " + message.getMessage());
        }
        chatListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messageItems));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Refresh").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        fetchMessages();
        return true;
    }
}
