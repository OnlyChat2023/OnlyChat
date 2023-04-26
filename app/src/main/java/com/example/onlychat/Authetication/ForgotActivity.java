package com.example.onlychat.Authetication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.MainActivity;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.R;

import org.json.JSONObject;

public class ForgotActivity extends AppCompatActivity {

    private Button LoginBtn, ForgotBtn;
    private TextView phoneNumberInput;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_to_left, R.anim.fixed);

        setContentView(R.layout.forgot_password_form);

        Intent loginIntent = new Intent(this, MainActivity.class);

        LoginBtn = (Button) findViewById(R.id.signInBtn);
        ForgotBtn = (Button) findViewById(R.id.forgotBtn);
        phoneNumberInput = (TextView) findViewById(R.id.phoneInput);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        ForgotBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String phone = phoneNumberInput.getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    phoneNumberInput.setError("Phone number is required");
                    phoneNumberInput.requestFocus();
                }
                else if (!phone.matches("^(03|05|07|08|09)\\d{8}$")) {
                    phoneNumberInput.setError("Phone number is not a valid number");
                    phoneNumberInput.requestFocus();
                }
                else {
                    enableRegister(false);

                    HttpManager httpRequest = new HttpManager(ForgotActivity.this);
                    httpRequest.validateForgotAccount(phone, new HttpResponse() {
                        @Override
                        public void onSuccess(JSONObject response) {

                            String phoneNumber = "+84" + phone.substring(1);

                            Intent OTPIntent = new Intent(ForgotActivity.this, OTP.class);

                            Bundle args = new Bundle();
                            args.putString("phone", phoneNumber);
                            args.putBoolean("reset", true);
                            OTPIntent.putExtras(args);

                            startActivity(OTPIntent);

                            enableRegister(true);
                        }

                        @Override
                        public void onError(String error) {
                            enableRegister(true);

                            Log.e("VALIDATE", error);
                            phoneNumberInput.setError("This phone does not exists account");
                            phoneNumberInput.requestFocus();
                        }
                    });
                }
            }
        });

        LoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
                finishAffinity();
            }
        });
    }

    private void enableRegister(boolean enable) {
        if (enable)
            loadingBar.setVisibility(View.GONE);
        else
            loadingBar.setVisibility(View.VISIBLE);
        ForgotBtn.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
    }
}