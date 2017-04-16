package io.hasura.myblog;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ashish.sardana on 16-04-2017.
 */

public class MessageResponse {

    @SerializedName("message")
    String message;

    public String getMessage(){
        return message;
    }
}
