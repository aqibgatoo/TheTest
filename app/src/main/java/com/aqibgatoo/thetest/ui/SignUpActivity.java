package com.aqibgatoo.thetest.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aqibgatoo.thetest.R;
import com.aqibgatoo.thetest.controller.TestApplication;
import com.kinvey.android.Client;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;

public class SignUpActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText mUserNameField;
    private EditText mPasswordField;
    private EditText mEmailField;
    private Button mSignUpButton;

    private ProgressDialog mProgressDialog;
    private Client mKinveyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUserNameField = (EditText) findViewById(R.id.id_username);
        mPasswordField = (EditText) findViewById(R.id.id_password);
        mEmailField = (EditText) findViewById(R.id.id_email);
        mSignUpButton = (Button) findViewById(R.id.id_signup);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("please wait...");
        mProgressDialog.setIndeterminate(true);

        mKinveyClient = TestApplication.getInstance();

        mSignUpButton.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.id_signup) {

            String username = mUserNameField.getText().toString().trim();
            String password = mPasswordField.getText().toString().trim();
            String email = mEmailField.getText().toString().trim();
            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please enter your credentials properly", Toast.LENGTH_LONG).show();
            } else {
                mProgressDialog.show();
                mKinveyClient.user().create(username, password, new KinveyClientCallback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        mProgressDialog.dismiss();
                        Intent imageIntent = new Intent(SignUpActivity.this, ImageActivity.class);
                        imageIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(imageIntent);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        mProgressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }

                });
            }


        }


    }
}
