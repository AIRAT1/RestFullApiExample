package de.android.restfullapiexample;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import stanford.androidlib.SimpleActivity;
import stanford.androidlib.xml.XML;

public class AddCatsImagesActivity extends SimpleActivity {
    @BindView(R.id.grid) GridLayout gridLayout;
    private int photoSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cats_images);
        ButterKnife.bind(this);
        photoSize = (getWindowManager().getDefaultDisplay().getWidth() - 32) / 2;
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
        try {
            gridLayout.removeAllViews();
            JSONObject json = XML.toJSONObject(data);
            JSONArray array = json.getJSONObject("response")
                    .getJSONObject("data")
                    .getJSONObject("images")
                    .getJSONArray("image");
            for (int i = 0; i < array.length(); i++) {
                String url = array.getJSONObject(i).getString("url");
                ImageView imageView = new ImageView(this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                imageView.setLayoutParams(params);
                Picasso.with(this)
                        .load(url)
                        .resize(photoSize, photoSize)
                        .into(imageView);
                gridLayout.addView(imageView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
