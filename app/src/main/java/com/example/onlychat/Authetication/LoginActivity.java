package com.example.onlychat.Authetication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.R;

public class LoginActivity extends AppCompatActivity {
    private final Boolean isLogin = false;

    private ImageView showPasswordBtn;
    private EditText passwordInput, phoneInput;
    private TextView forgotPasswordBtn;

    private boolean isHidePassword = true;
    private Button LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_to_left, R.anim.fixed);

        if (!isLogin) {
            setContentView(R.layout.login_activity);

            Intent registerIntent = new Intent(this, RegisterActivity.class);
            Intent forgotIntent = new Intent(this, ForgotActivity.class);

            LoginBtn = (Button) findViewById(R.id.registerBtn);
            LoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(registerIntent);
                    overridePendingTransition(R.anim.right_to_left, R.anim.fixed);
                }
            });

            passwordInput = (EditText) findViewById(R.id.passwordInput);
            phoneInput = (EditText) findViewById(R.id.phoneNumberInput);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String phoneNumber = bundle.getString("phonenumber");
                phoneInput.setText(phoneNumber);
            }

            forgotPasswordBtn = (TextView) findViewById(R.id.forgotPasswordBtn);

            forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(forgotIntent);
                }
            });

            showPasswordBtn = (ImageView) findViewById(R.id.showPassword);
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
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fixed, R.anim.left_to_right);
    }
}