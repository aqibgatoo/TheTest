package com.aqibgatoo.thetest.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aqibgatoo.thetest.R;
import com.aqibgatoo.thetest.controller.TestApplication;
import com.kinvey.android.Client;
import com.kinvey.java.User;
import com.kinvey.java.core.KinveyClientCallback;

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText mUserNameField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private TextView mSignUpText;
    private Client mKinveyClient;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mKinveyClient = TestApplication.getInstance();
        mUserNameField = (EditText) findViewById(R.id.id_username);
        mPasswordField = (EditText) findViewById(R.id.id_password);
        mLoginButton = (Button) findViewById(R.id.id_login);
        mSignUpText = (TextView) findViewById(R.id.id_signup_text);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("please wait...");
        mProgressDialog.setIndeterminate(true);


        mSignUpText.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);


    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.id_login) {
            String userName = mUserNameField.getText().toString().trim();
            String password = mPasswordField.getText().toString().trim();
            if (userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter credentials", Toast.LENGTH_LONG).show();
            } else {
                mProgressDialog.show();
                mKinveyClient.user().login(userName, password, new KinveyClientCallback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, ImageActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        mProgressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
        if (id == R.id.id_signup_text) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }
    }


}
