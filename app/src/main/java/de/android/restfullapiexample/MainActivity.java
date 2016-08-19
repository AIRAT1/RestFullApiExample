package de.android.restfullapiexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

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
        Ion.with(this)
                .load("http://api.icndb.com/jokes/random")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String data) {
                        setChuckNorrisJokeToView(data);
                    }
                });
    }

    private void setChuckNorrisJokeToView(String data) {
        try {
            JSONObject response = new JSONObject(data);
            String joke = response.getJSONObject("value").getString("joke");
            chuckNorrisTextView.setText(joke);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
