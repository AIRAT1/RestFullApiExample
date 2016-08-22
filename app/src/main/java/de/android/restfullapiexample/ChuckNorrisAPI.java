package de.android.restfullapiexample;

import de.android.restfullapiexample.data.models.Model;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ChuckNorrisAPI {
    @GET("jokes/random")
    Call<Model> randomJoke();
}
