package io.hasura.myblog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
        new SignInTask().execute(username.getText().toString(), password.getText().toString());
    }

    private void performRegistration() {

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
}
