package io.hasura.myblog;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ashish.sardana on 16-04-2017.
 */

public interface ApiInterface {
    @POST(NetworkURL.LOGIN)
    Call<MessageResponse> login(@Body AuthenticationRequest body);

    @POST(NetworkURL.REGISTRATION)
    Call<MessageResponse> registration(@Body AuthenticationRequest body);
}
