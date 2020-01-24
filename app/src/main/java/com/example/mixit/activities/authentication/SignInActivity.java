package com.example.mixit.activities.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mixit.R;
import com.example.mixit.activities.StartActivity;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SigIn";

    private AutoCompleteTextView mEmailField;
    private EditText mPasswordField;

    private FireBaseAuth fireBaseAuth;

    Button mSignInButton, mForgotPasswordButton, mBackButton, mRegister_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        setContentView(R.layout.activity_sign_in);

        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);

        mBackButton = findViewById(R.id.back_button);
        mSignInButton = findViewById(R.id.sign_in_button);
        mForgotPasswordButton = findViewById(R.id.forgot_password_button);
        mRegister_button = findViewById(R.id.register_button);

        mSignInButton.setOnClickListener(this);
        mForgotPasswordButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);
        mRegister_button.setOnClickListener(this);

        fireBaseAuth = new FireBaseAuth(this, this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.forgot_password_button) {
            sendPasswordResetEmail(mEmailField.getText().toString());
        }
        else if (i == R.id.sign_in_button) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        else if (i == R.id.back_button){
            onBackPressed();
        }
        else if (i == R.id.register_button){
            Intent intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);
        }
    }

    private void sendPasswordResetEmail (final String email) {
        if (!validateEmail()) {
            return;
        }
        fireBaseAuth.getmAuth().sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this, R.string.txt_successful_password_reset, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignInActivity.this, R.string.txt_error_password_reset, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private boolean validateEmail() {
        boolean valid = true;
        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }
        return valid;
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }
        fireBaseAuth.getmAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = fireBaseAuth.getmAuth().getCurrentUser();
                            Toast.makeText(SignInActivity.this, R.string.txt_welcome_again, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignInActivity.this, StartActivity.class);
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, R.string.txt_error_login,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Required.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

}
