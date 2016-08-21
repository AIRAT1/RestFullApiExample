package de.android.restfullapiexample;

import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import stanford.androidlib.SimpleActivity;
import stanford.androidlib.xml.XML;

public class AddCatsImagesActivity extends SimpleActivity {
    @BindView(R.id.grid) GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cats_images);
        ButterKnife.bind(this);
    }

    public void onCatsClick(View view) {
        Ion.with(this)
                .load("http://thecatapi.com/api/images/get?results_per_page=4&format=xml")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String data) {
                        processCatData(data);
                    }
                });
    }

    private void processCatData(String data) {
        JSONObject json = XML.toJSONObject(data);

    }
}
