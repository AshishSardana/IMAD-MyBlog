package io.hasura.myblog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.start;

public class AuthenticationActivity extends AppCompatActivity {

    //Global declaration because we need to use them in different functions inside the class.
    EditText username, password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        username = (EditText) findViewById(R.id.usernameText);
        password = (EditText) findViewById(R.id.passwordText);

        Button signInButton = (Button) findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()) {
                    performSignIn();
                }
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()) {
                    performRegistration();
                }
            }
        });

        //Initializing progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait.");
    }

    private Boolean isFormValid() {
        if (username.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Username or password cannot be left empty.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void performSignIn() {
        //Mock an API call to sign in.
        //new SignInTask().execute(username.getText().toString(), password.getText().toString());

        showProgressDialog(true);
        ApiManager.getApiInterface().login(new AuthenticationRequest(username.getText().toString().trim(), password.getText().toString().trim()))
            .enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                    showProgressDialog(false);
                    //Checks if the status code of the response is in between 200-300
                    if (response.isSuccessful()) {
                        //showAlert("Welcome", response.body().getMessage());
                        navigateToArticleListActivity();
                    } else {
                        //If the username/password is incorrect
                        //Convert the response into ErrorResponse body
                        try {
                            String errorMessage = response.errorBody().string();
                            try {
                                ErrorResponse errorResponse = new Gson().fromJson(errorMessage, ErrorResponse.class);
                                showAlert("SignIn Failed", errorResponse.getError());
                            } catch (JsonSyntaxException jsonException) {
                                jsonException.printStackTrace();
                                showAlert("SignIn Failed", "Something went wrong_1");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            showAlert("SignIn Failed", "Something went wrong_2");
                        }
                    }
                }
                //Called when the request has failed due to problems like "No Internet Connection"
                @Override
                public void onFailure (Call < MessageResponse > call, Throwable t){
                    showProgressDialog(false);
                    Log.i("AuthenticationActivity", "Sign In has failed inside onfailure");
                   showAlert("SignIn Failed", "Something went wrong onFailure");
                }
            });
    }

    private void performRegistration() {
        showProgressDialog(true);
        ApiManager.getApiInterface().registration(new AuthenticationRequest(username.getText().toString().trim(), password.getText().toString().trim()))
            .enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                showProgressDialog(false);
                //Checks if the status code of the response is in between 200-300
                if (response.isSuccessful()) {
                    showAlert("Welcome", response.body().getMessage());
                } else {
                    //If the username/password is incorrect
                    //Convert the response into ErrorResponse body
                    try {
                        String errorMessage = response.errorBody().string();
                        try {
                            ErrorResponse errorResponse = new Gson().fromJson(errorMessage, ErrorResponse.class);
                            showAlert("Registration Failed", errorResponse.getError());
                        } catch (JsonSyntaxException jsonException) {
                            jsonException.printStackTrace();
                            showAlert("Registration Failed", "Something went wrong_1");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("Registration Failed", "Something went wrong_2");
                    }
                }
            }
            @Override
            public void onFailure (Call < MessageResponse > call, Throwable t){
                showProgressDialog(false);
                showAlert("Registration Failed", "Something went wrong onFailure.");
            }
        });
    }

    private void showProgressDialog(Boolean shouldShow) {
        if (shouldShow)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNeutralButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    class SignInTask extends AsyncTask<String, Void, Boolean> {

        //To test the functionality out without fetching these values from the database
        String mockUsername = "test";
        String mockPassword = "password";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog(true);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            showProgressDialog(false);

            if (aBoolean)
                showAlert("Welcome", "You have successfully signed in.");
            else
                showAlert("Failed", "Username/password entered in incorrect.");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String username = params[0];
            String password = params[1];

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return username.contentEquals(mockUsername) && password.contentEquals(mockPassword);
        }

    }
    private void navigateToArticleListActivity(){
        Intent intent = new Intent(this, ArticleListActivity.class);
        startActivity(intent);
    }
}
