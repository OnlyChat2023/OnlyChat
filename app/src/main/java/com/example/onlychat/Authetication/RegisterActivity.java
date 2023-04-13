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

import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.MainActivity;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.R;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private ImageView showPasswordBtn;
    private EditText passwordInput, passwordConfirmInput, phoneNumberInput, usernameInput;

    private boolean isHidePassword = true;
    private Button LoginBtn, RegisterBtn;
    private Boolean validationOK = true;

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

                validationOK = true;

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

                if (validationOK) {
                    String phoneNumber = "+84" + phone.substring(1);

                    HttpManager httpRequest = new HttpManager(RegisterActivity.this);
                    httpRequest.validateAccount(phoneNumber, new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Intent OTPIntent = new Intent(RegisterActivity.this, OTP.class);

                            Bundle args = new Bundle();
                            args.putString("phone", phoneNumber);
                            OTPIntent.putExtras(args);

                            startActivity(OTPIntent);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("VALIDATE", error);
                            phoneNumberInput.setError("This phone has already been used");
                        }
                    });
                }
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