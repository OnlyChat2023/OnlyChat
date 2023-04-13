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
import android.widget.ProgressBar;
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
    private ProgressBar loadingBar;

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

        Intent loginIntent = new Intent(this, MainActivity.class);

        phoneNumberInput        = (EditText) findViewById(R.id.phoneInput);
        usernameInput           = (EditText) findViewById(R.id.usernameInput);
        passwordInput           = (EditText) findViewById(R.id.passwordInput);
        passwordConfirmInput    = (EditText) findViewById(R.id.confirmPasswordInput);

        showPasswordBtn         = (ImageView) findViewById(R.id.showPassword);
        LoginBtn                = (Button) findViewById(R.id.signInBtn);
        RegisterBtn             = (Button) findViewById(R.id.registerBtn);

        loadingBar              = (ProgressBar) findViewById(R.id.loadingBar);

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
                    validationOK = false;
                    phoneNumberInput.setError("Phone number is required");
                    phoneNumberInput.requestFocus();
                }

                if (TextUtils.isEmpty(name)) {
                    validationOK = false;
                    usernameInput.setError("User name is required");
                    usernameInput.requestFocus();
                }

                if (TextUtils.isEmpty(password)) {
                    validationOK = false;
                    passwordInput.setError("Password is required");
                    passwordInput.requestFocus();
                }

                if (TextUtils.isEmpty(passwordConfirm)) {
                    validationOK = false;
                    passwordConfirmInput.setError("Password confirm is required");
                    passwordConfirmInput.requestFocus();
                }

                if (!phone.matches("^(03|05|07|08|09)\\d{8}$")) {
                    validationOK = false;
                    phoneNumberInput.setError("Phone number is not a valid number");
                    phoneNumberInput.requestFocus();
                }

                if (!password.equals(passwordConfirm)) {
                    validationOK = false;
                    passwordConfirmInput.setError("Confirm password is not match");
                }

                if (validationOK) {
                    enableRegister(false);

                    HttpManager httpRequest = new HttpManager(RegisterActivity.this);
                    httpRequest.validateAccount(phone, new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) {

                            String phoneNumber = "+84" + phone.substring(1);

                            Intent OTPIntent = new Intent(RegisterActivity.this, OTP.class);

                            Bundle args = new Bundle();
                            args.putString("phone", phoneNumber);
                            args.putString("username", name);
                            args.putString("password", password);
                            args.putString("confirmPassword", passwordConfirm);
                            OTPIntent.putExtras(args);

                            startActivity(OTPIntent);

                            enableRegister(true);
                        }

                        @Override
                        public void onError(String error) {
                            enableRegister(true);

                            Log.e("VALIDATE", error);
                            phoneNumberInput.setError("This phone has already been used");
                            phoneNumberInput.requestFocus();
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

    private void enableRegister(boolean enable) {
        if (enable)
            loadingBar.setVisibility(View.GONE);
        else
            loadingBar.setVisibility(View.VISIBLE);
        RegisterBtn.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
    }
}