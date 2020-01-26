package com.example.mixit.activities.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mixit.R;
import com.example.mixit.activities.StartActivity;
import com.example.mixit.activities.TermsAndConditionsActivity;
import com.example.mixit.services.authentication.FireBaseAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccount";

    private Button mBackButton, mSignUpButton;
    private AutoCompleteTextView mEmailField, mNameField;
    private EditText mPasswordField, mPasswordField2;
    private FireBaseAuth fireBaseAuth;

    private TextView mTermsConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        fireBaseAuth = new FireBaseAuth(this, this);

        mTermsConditions = findViewById(R.id.terms_conditions);

        mNameField = findViewById(R.id.input_name);
        mEmailField = findViewById(R.id.email);
        mPasswordField = findViewById(R.id.password);
        mPasswordField2 = findViewById(R.id.confirmed_password);

        mBackButton = findViewById(R.id.back_button);
        mSignUpButton = findViewById(R.id.sign_up_button);

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }
        });
        mTermsConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountActivity.this, TermsAndConditionsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }
        fireBaseAuth.getmAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            fireBaseAuth.updateNameFireBase(mNameField.getText().toString(), StartActivity.class);
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(CreateAccountActivity.this, R.string.txt_error_register,
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = mNameField.getText().toString();
        if (TextUtils.isEmpty(name)) {
            mNameField.setError("Required.");
            valid = false;
        } else {
            mNameField.setError(null);
        }

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

        String password2 = mPasswordField2.getText().toString();
        if (TextUtils.isEmpty(password2)) {
            mPasswordField2.setError("Required.");
            valid = false;
        } else {
            if (!password2.equals(password)){
                mPasswordField2.setError("Las claves no coinciden");
                valid = false;
            }
            else {
                mPasswordField2.setError(null);
            }
        }
        return valid;
    }

}

     /*
//    TODO
//    private void sendEmailVerification() {
//        findViewById(R.id.verifyEmailButton).setEnabled(false);
//
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        findViewById(R.id.verifyEmailButton).setEnabled(true);
//
//                        if (task.isSuccessful()) {
//                            Toast.makeText(SignInActivity.this,
//                                    "Verification email sent to " + user.getEmail(),
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Log.e(TAG, "sendEmailVerification", task.getException());
//                            Toast.makeText(SignInActivity.this,
//                                    "Failed to send verification email.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
      */
