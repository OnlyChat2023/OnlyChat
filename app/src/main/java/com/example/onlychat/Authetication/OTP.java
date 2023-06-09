package com.example.onlychat.Authetication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlychat.Interfaces.AuthOTP;
import com.example.onlychat.Interfaces.HttpResponse;
import com.example.onlychat.Manager.HttpManager;
import com.example.onlychat.R;
import com.example.onlychat.Services.FirebaseService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;

import org.json.JSONObject;

public class OTP extends AppCompatActivity {

    private EditText inputOTP1, inputOTP2, inputOTP3, inputOTP4, inputOTP5, inputOTP6;
    private TextView resendBtn, errorMessage;
    private Button continueBtn;
    private FirebaseService OTPService;
    private String username, phoneNumber, password, confirmPassword;
    private ProgressBar loadingBar;
    private HttpManager httpRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_to_left, R.anim.fixed);

        setContentView(R.layout.otp_form);

        Bundle bundle = getIntent().getExtras();
        boolean reset = bundle.getBoolean("reset", false);
        phoneNumber = bundle.getString("phone");

        inputOTP1 = (EditText) findViewById(R.id.inputOTP1);
        inputOTP2 = (EditText) findViewById(R.id.inputOTP2);
        inputOTP3 = (EditText) findViewById(R.id.inputOTP3);
        inputOTP4 = (EditText) findViewById(R.id.inputOTP4);
        inputOTP5 = (EditText) findViewById(R.id.inputOTP5);
        inputOTP6 = (EditText) findViewById(R.id.inputOTP6);
        continueBtn = (Button) findViewById(R.id.ContinueBtn);
        resendBtn = (TextView) findViewById(R.id.resendBtn);
        loadingBar = (ProgressBar) findViewById(R.id.loadingBar);

        errorMessage = (TextView) findViewById(R.id.errorMessage);

        httpRequest = new HttpManager(OTP.this);

        EditText[] otpEt = new EditText[] {
                inputOTP1, inputOTP2, inputOTP3, inputOTP4, inputOTP5, inputOTP6
        };

        setOtpEditTextHandler(otpEt);

        if (!reset)
            initOTPRegister(bundle, otpEt);
        else
            initOTPReset(otpEt);

        OTPService.sendOTPRequest(phoneNumber);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableRegister(false);

                String code = inputOTP1.getText().toString() + inputOTP2.getText().toString() + inputOTP3.getText().toString();
                code = code + inputOTP4.getText().toString() + inputOTP5.getText().toString() + inputOTP6.getText().toString();

                OTPService.signInWithPhoneAuthCredential(code);
            }
        });

        resendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OTPService.resendVerificationCode(phoneNumber);
            }
        });
    }

    private boolean checkValidOTP(EditText[] otpEt) {
        for (int i = 0; i < otpEt.length; i++) {
            if (TextUtils.isEmpty(otpEt[i].getText().toString()))
                return false;
        }
        return true;
    }

    private void setOtpEditTextHandler(EditText[] otpEt) { //This is the function to be called
        for (int i = 0; i < otpEt.length; i++) { //Its designed for 6 digit OTP
            final int iVal = i;

            otpEt[iVal].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(iVal == otpEt.length - 1 && !otpEt[iVal].getText().toString().isEmpty()) {
                        otpEt[iVal].clearFocus(); //Clears focus when you have entered the last digit of the OTP.
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(otpEt[iVal].getWindowToken(), 0);
                    } else if (!otpEt[iVal].getText().toString().isEmpty()) {
                        otpEt[iVal+1].requestFocus(); //focuses on the next edittext after a digit is entered.
                    }

                    continueBtn.setEnabled(checkValidOTP(otpEt));
                }
            });

            otpEt[iVal].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() != KeyEvent.ACTION_DOWN) {
                        return false; //Dont get confused by this, it is because onKeyListener is called twice and this condition is to avoid it.
                    }

                    if (keyCode == KeyEvent.KEYCODE_DEL && otpEt[iVal].getText().toString().isEmpty() && iVal != 0) {
                        //this condition is to handel the delete input by users.
                        otpEt[iVal-1].setText("");//Deletes the digit of OTP
                        otpEt[iVal-1].requestFocus();//and sets the focus on previous digit
                    }

                    return false;
                }
            });
        }
    }

    private void enableRegister(boolean enable) {
        if (enable)
            loadingBar.setVisibility(View.GONE);
        else
            loadingBar.setVisibility(View.VISIBLE);
        continueBtn.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
    }

    @Override
    public void onStop() {
        super.onStop();

        FirebaseAuth.getInstance().signOut();
    }

    public void initOTPRegister(Bundle bundle, EditText[] otpEt) {
        username = bundle.getString("username");
        password = bundle.getString("password");
        confirmPassword = bundle.getString("confirmPassword");

        OTPService = new FirebaseService(this, new AuthOTP() {
            @Override
            public void autoFill(String sms_otp_code) {
                Log.d("ETST", sms_otp_code);
                for (int i = 0; i < sms_otp_code.length(); i++) {
                    otpEt[i].setText(String.valueOf(sms_otp_code.charAt(i)));
                }
            }

            @Override
            public void onSuccess(String token) {
                Log.d("FragmentCreate","Token found from thread1 after expiry " + token);
                httpRequest.Register(username, phoneNumber, password, confirmPassword, token, new HttpResponse() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        enableRegister(true);
                        Toast.makeText(OTP.this, "Congratulations! Your account has been successfully created.", Toast.LENGTH_SHORT).show();

                        Intent LoginActivityIntent = new Intent(OTP.this, LoginActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("phonenumber", "0" + phoneNumber.substring(3));
                        LoginActivityIntent.putExtras(bundle);

                        startActivity(LoginActivityIntent);
                        finishAffinity();
                    }

                    @Override
                    public void onError(String error) {
                        enableRegister(true);
                    }
                });
            }

            @Override
            public void onValidateError() {
                errorMessage.setText("Invalid OTP entered. Please try again.");
            }
        });
    }

    public void initOTPReset(EditText[] otpEt) {
        OTPService = new FirebaseService(this, new AuthOTP() {
            @Override
            public void autoFill(String sms_otp_code) {
                for (int i = 0; i < sms_otp_code.length(); i++) {
                    otpEt[i].setText(String.valueOf(sms_otp_code.charAt(i)));
                }
            }

            @Override
            public void onSuccess(String token) {
                Intent ResetActivity = new Intent(OTP.this, ResetPassword.class);
                Bundle bundle = new Bundle();
                bundle.putString("token", token);

                ResetActivity.putExtras(bundle);
                startActivity(ResetActivity);

                finishAffinity();
            }

            @Override
            public void onValidateError() {
                errorMessage.setText("Invalid OTP entered. Please try again.");
            }
        });
    }
}