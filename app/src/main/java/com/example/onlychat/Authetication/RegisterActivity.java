package com.example.onlychat.Authetication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.MainActivity;
import com.example.onlychat.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private ImageView showPasswordBtn;
    private EditText passwordInput, passwordConfirmInput, phoneNumberInput, usernameInput;

    private boolean isHidePassword = true;
    private Button LoginBtn, RegisterBtn;
    private Boolean validationOK = true;

    FirebaseAuth auth;
    private String mVerificationId, mResendToken;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d("PHONE", "onVerificationCompleted:" + credential);

//            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w("PHONE_ERR", "onVerificationFailed", e);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }

            // Show a message and update the UI
        }
        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("SEND CODE", "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = String.valueOf(token);
        }
    };

    PhoneAuthOptions options;
//
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = auth.getCurrentUser();
//        Log.d("User", currentUser.toString());
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_to_left, R.anim.fixed);

        setContentView(R.layout.register_form);


//        FileInputStream serviceAccount = null;
//        try {
//            serviceAccount = new FileInputStream("serviceAccountKey.json");
//            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }

        Intent loginIntent = new Intent(this, MainActivity.class);

        phoneNumberInput        = (EditText) findViewById(R.id.phoneInput);
        usernameInput           = (EditText) findViewById(R.id.usernameInput);
        passwordInput           = (EditText) findViewById(R.id.passwordInput);
        passwordConfirmInput    = (EditText) findViewById(R.id.confirmPasswordInput);

        showPasswordBtn         = (ImageView) findViewById(R.id.showPassword);
        LoginBtn                = (Button) findViewById(R.id.signInBtn);
        RegisterBtn             = (Button) findViewById(R.id.registerBtn);

        auth = FirebaseAuth.getInstance();

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
                finishAffinity();
            }
        });

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone            = phoneNumberInput.getText().toString();
                String name             = usernameInput.getText().toString();
                String password         = passwordInput.getText().toString();
                String passwordConfirm  = passwordConfirmInput.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    phoneNumberInput.setError("Phone number is required");
                    validationOK = false;
                }

                if (TextUtils.isEmpty(name)) {
                    validationOK = false;
                    usernameInput.setError("User name is required");
                }

                if (TextUtils.isEmpty(password)) {
                    validationOK = false;
                    passwordInput.setError("Password is required");
                }

                if (TextUtils.isEmpty(passwordConfirm)) {
                    validationOK = false;
                    passwordConfirmInput.setError("Password confirm is required");
                }

                if (!phone.matches("^(03|05|07|08|09)\\d{8}$")) {
                    validationOK = false;
                    phoneNumberInput.setError("Phone number is not a valid number");
                }

                if (!password.equals(passwordConfirm)) {
                    validationOK = false;
                    passwordConfirmInput.setError("Confirm password is not match");
                }

                FirebaseAuthSettings firebaseAuthSettings = auth.getFirebaseAuthSettings();

// Configure faking the auto-retrieval with the whitelisted numbers.
//                firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phone.replace("0", "+84"), "123123");

                options = PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phone.replace("0", "+84"))       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(RegisterActivity.this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();

                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        showPasswordBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isHidePassword) {
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordConfirmInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else {
                    passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordConfirmInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

                passwordInput.setSelection(passwordInput.getText().toString().length());
                passwordConfirmInput.setSelection(passwordConfirmInput.getText().toString().length());
                isHidePassword = !isHidePassword;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
    }
}