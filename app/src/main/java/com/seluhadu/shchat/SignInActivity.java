package com.seluhadu.shchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private FirebaseAuth mFireBaseAuth;
    private EditText mEmail, mPassword;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mFireBaseAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        AppCompatButton mSignIn = findViewById(R.id.sign_in);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser(mEmail, mPassword);
            }
        });
        TextView mSignUp = findViewById(R.id.sign_up);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendTo = new Intent(SignInActivity.this, SignUpActivity.class);
                sendTo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(sendTo);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mFireBaseAuth.getCurrentUser() != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void signInUser(EditText mEmail, EditText mPassword) {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                showProgress();
                mFireBaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendToMain();
                        } else {
                            showErrorMessage(task);
                        }
                        hideProgress();
                    }
                });
        } else {
            Toast.makeText(this, "Please fill all.", Toast.LENGTH_LONG).show();
        }
    }

    private void showErrorMessage(@Nullable Task<AuthResult> task) {
        assert task != null;
        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
        Toast.makeText(SignInActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "showErrorMessage: " + errorMessage);
    }

    private void showProgress() {
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void hideProgress() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
