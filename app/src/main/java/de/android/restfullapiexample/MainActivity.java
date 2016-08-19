package de.android.restfullapiexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.chuckNorrisTextView) TextView chuckNorrisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }
    public void chuckNorrisClick(View view) {
        /*
        with Ion library
        */
//        Ion.with(this)
//                .load("http://api.icndb.com/jokes/random")
//                .asString()
//                .setCallback(new FutureCallback<String>() {
//                    @Override
//                    public void onCompleted(Exception e, String data) {
//                        setChuckNorrisJokeToView(data);
//                    }
//                });

        /*
        with async task
         */
        new ChuckNorrisAsyncTask().execute("http://api.icndb.com/jokes/random");
    }

        /*
        with Ion library
        */
    private void setChuckNorrisJokeToView(String data) {
        try {
            JSONObject response = new JSONObject(data);
            String joke = response.getJSONObject("value").getString("joke");
            chuckNorrisTextView.setText(joke);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    /*
    with async task
     */
    private class ChuckNorrisAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String text = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");
                conn.connect();
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        int ch = input.read();
                        if (ch == - 1) break;
                        sb.append((char) ch);
                    }
                    text = sb.toString();

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            try {
                JSONObject response = new JSONObject(data);
                String joke = response.getJSONObject("value").getString("joke");
                chuckNorrisTextView.setText(joke);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
