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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.MainActivity;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPassword extends AppCompatActivity {

    private ImageView showPasswordBtn;
    private EditText passwordInput, passwordConfirmInput;
    private ProgressBar loadingBar;

    private boolean isHidePassword = true;
    private Button LoginBtn, ResetBtn;
    private Boolean validationOK = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_form);

        Bundle bundle = getIntent().getExtras();
        String token = bundle.getString("token");

        Intent loginIntent = new Intent(this, MainActivity.class);

        passwordInput           = (EditText) findViewById(R.id.passwordInput);
        passwordConfirmInput    = (EditText) findViewById(R.id.confirmPasswordInput);

        showPasswordBtn         = (ImageView) findViewById(R.id.showPassword);
        LoginBtn                = (Button) findViewById(R.id.signInBtn);
        ResetBtn                = (Button) findViewById(R.id.resetBtn);

        loadingBar              = (ProgressBar) findViewById(R.id.loadingBar);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
                finishAffinity();
            }
        });

        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validationOK = true;

                String password         = passwordInput.getText().toString();
                String passwordConfirm  = passwordConfirmInput.getText().toString();

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

                if (!password.equals(passwordConfirm)) {
                    validationOK = false;
                    passwordConfirmInput.setError("Confirm password is not match");
                }

                if (validationOK) {
                    enableReset(false);

                    HttpManager httpRequest = new HttpManager(ResetPassword.this);
                    httpRequest.ResetPassword(password, passwordConfirm, token, new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) throws JSONException {
                            enableReset(true);
                            String phoneNumber = response.getString("phonenumber");

                            Toast.makeText(ResetPassword.this, "Congratulations! Your password has been successfully changed.", Toast.LENGTH_SHORT).show();

                            Intent LoginActivityIntent = new Intent(ResetPassword.this, LoginActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putString("phonenumber", "0" + phoneNumber.substring(3));
                            LoginActivityIntent.putExtras(bundle);

                            startActivity(LoginActivityIntent);
                            finishAffinity();
                        }

                        @Override
                        public void onError(String error) {
                            enableReset(true);

                            Log.e("VALIDATE", error);
                            passwordInput.setError("This phone has already been used");
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

    private void enableReset(boolean enable) {
        if (enable)
            loadingBar.setVisibility(View.GONE);
        else
            loadingBar.setVisibility(View.VISIBLE);
        ResetBtn.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
    }
}