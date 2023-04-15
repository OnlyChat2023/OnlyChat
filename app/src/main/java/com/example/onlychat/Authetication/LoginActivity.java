package com.example.onlychat.Authetication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.MainActivity;
import com.example.onlychat.MainScreen.MainScreen;
import com.example.onlychat.Manager.GlobalPreferenceManager;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Boolean isLogin = false;

    private ImageView showPasswordBtn;
    private EditText passwordInput, phoneInput;
    private CheckBox rememberMe;
    private TextView forgotPasswordBtn;
    private ProgressBar loadingBar;
    private boolean isHidePassword = true;
    private Button RegisterBtn, LoginBtn;
    private Boolean validationOK = true;
    private GlobalPreferenceManager pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_to_left, R.anim.fixed);

        setContentView(R.layout.login_activity);

        pref = new GlobalPreferenceManager(LoginActivity.this);

        Intent registerIntent = new Intent(this, RegisterActivity.class);
        Intent forgotIntent = new Intent(this, ForgotActivity.class);

        RegisterBtn = (Button) findViewById(R.id.registerBtn);
        LoginBtn = (Button) findViewById(R.id.LoginBtn);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        rememberMe = (CheckBox) findViewById(R.id.rememberMe);
        phoneInput = (EditText) findViewById(R.id.phoneNumberInput);
        forgotPasswordBtn = (TextView) findViewById(R.id.forgotPasswordBtn);
        showPasswordBtn = (ImageView) findViewById(R.id.showPassword);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String phoneNumber = bundle.getString("phonenumber");
            phoneInput.setText(phoneNumber);
        }

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registerIntent);
                overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
            }
        });

        forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(forgotIntent);
            }
        });

        showPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isHidePassword)
                    passwordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                else
                    passwordInput.setTransformationMethod(PasswordTransformationMethod.getInstance());

                passwordInput.setSelection(passwordInput.getText().toString().length());
                isHidePassword = !isHidePassword;
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                validationOK = true;

                String phone            = phoneInput.getText().toString();
                String password         = passwordInput.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    validationOK = false;
                    phoneInput.setError("Phone number is required");
                    phoneInput.requestFocus();
                }

                if (TextUtils.isEmpty(password)) {
                    validationOK = false;
                    passwordInput.setError("Password is required");
                    passwordInput.requestFocus();
                }

                if (!phone.matches("^(03|05|07|08|09)\\d{8}$")) {
                    validationOK = false;
                    phoneInput.setError("Phone number is not a valid number");
                    phoneInput.requestFocus();
                }

                if (validationOK) {
                    enableLogin(false);

                    HttpManager httpRequest = new HttpManager(LoginActivity.this);

                    httpRequest.Login(phone, password, new HttpResponse() {

                        @Override
                        public void onSuccess(JSONObject response) throws JSONException {
                            enableLogin(true);

                            JSONObject userInfo = response.getJSONObject("user").getJSONObject("info");

                            com.example.onlychat.Model.UserModel userInformation = new Gson().fromJson(String.valueOf(userInfo), com.example.onlychat.Model.UserModel.class);
                            pref.Login(userInformation, rememberMe.isChecked());

                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isLogin", pref.getIsLoggedIn());

                            Intent intent = new Intent(LoginActivity.this, MainScreen.class);
                            intent.putExtras(bundle);

                            startActivity(intent);

                            finishAffinity();

//                                Log.e("Test",
//                                    userInformation.getToken() + "\n" +
//                                    userInformation.getName() + "\n" +
//                                            userInformation.getUsername() + "\n" +
//                                            userInformation.getId() + "\n" +
//                                            userInformation.getAvatar() + "\n" +
//                                            userInformation.getDescription() + "\n" +
//                                            userInformation.getEmail() + "\n" +
//                                            userInformation.getFacebook() + "\n" +
//                                            userInformation.getInstagram() + "\n" + userInformation.getPhone());
                        }

                        @Override
                        public void onError(String error) {
                            phoneInput.setError("Invalid phone number or password. Please try again.");
                            phoneInput.requestFocus();
                            enableLogin(true);
                        }
                    });
                }
            }
        });
    }

    private void enableLogin(boolean enable) {
        if (enable)
            loadingBar.setVisibility(View.GONE);
        else
            loadingBar.setVisibility(View.VISIBLE);
        LoginBtn.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
    }
}