package de.android.restfullapiexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.android.restfullapiexample.data.models.Model;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
/*
 * 5 different methods to become and parsing JSON from REST service
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG ="TAG";
    private static final String ACTION_DONE = "ACTION_DONE";
    @BindView(R.id.chuckNorrisTextView) TextView chuckNorrisTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DONE);
        registerReceiver(new MyBroadcastReceiver(), filter);
    }
    /*
     * ION + manual parsing
     */
    public void onION(View view) {
        Ion.with(this)
                .load("http://api.icndb.com/jokes/random")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String data) {
                        setResponseToView(data);
                    }
                });
    }

    /*
     * ION + GSON parsing
     */
    public void onION_plus_GSON(View view) {
        Ion.with(this)
                .load("http://api.icndb.com/jokes/random")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String data) {
                        setGsonResponseToView(data);
                    }
                });
    }

    /*
     * with async task
     */
    public void onAsyncTask(View view) {
        new ChuckNorrisAsyncTask().execute("http://api.icndb.com/jokes/random");
    }

    /*
     * Retrofit 2
     */
    public void onRetrofit(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.icndb.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ChuckNorrisAPI chuck = retrofit.create(ChuckNorrisAPI.class);
        Call<Model> joke = chuck.randomJoke();
        joke.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, Response<Model> response) {
                String data = response.body().getValue().getJoke();
                Log.d(TAG, data);
                chuckNorrisTextView.setText(data);
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

            }
        });
    }

    /*
     * BroadcastReceiver
     */
    public void onBroadcast(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String text = null;
                try {
                    URL url = new URL("http://api.icndb.com/jokes/random");
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
                        Intent done = new Intent();
                        done.setAction(ACTION_DONE);
                        done.putExtra("text", text);
                        sendBroadcast(done);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void setResponseToView (String data) {
        try {
            JSONObject response = new JSONObject(data);
            String joke = response.getJSONObject("value").getString("joke");
            chuckNorrisTextView.setText(joke);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setGsonResponseToView(String data) {
        try {
            String value = new JSONObject(data).getString("value");
            Gson gson = new Gson();
            Value pojoValue = gson.fromJson(value, Value.class);
            chuckNorrisTextView.setText(pojoValue.getJoke());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
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
            setResponseToView(data);
        }

    }
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("text");
            setResponseToView(data);
        }
    }
}
