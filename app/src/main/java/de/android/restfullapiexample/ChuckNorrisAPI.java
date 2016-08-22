package de.android.restfullapiexample;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ChuckNorrisAPI {
    @GET("jokes/random")
    Call<String> randomJoke();
}
