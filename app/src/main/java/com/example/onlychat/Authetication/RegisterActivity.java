package com.example.onlychat.Authetication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlychat.MainActivity;
import com.example.onlychat.R;

public class RegisterActivity extends AppCompatActivity {

    private ImageView showPasswordBtn;
    private EditText passwordInput, passwordConfirmInput, phoneNumberInput, usernameInput;

    private boolean isHidePassword = true;
    private Button LoginBtn, RegisterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_form);

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

                String phone            = phoneNumberInput.getText().toString();
                String name             = usernameInput.getText().toString();
                String password         = passwordInput.getText().toString();
                String passwordConfirm  = passwordConfirmInput.getText().toString();

                if (TextUtils.isEmpty(phone))
                    phoneNumberInput.setError("Phone number is required");

                if (TextUtils.isEmpty(name))
                    usernameInput.setError("User name is required");

                if (TextUtils.isEmpty(password))
                    passwordInput.setError("Password is required");

                if (TextUtils.isEmpty(passwordConfirm))
                    passwordConfirmInput.setError("Password confirm is required");

                if (!phone.matches("/^(03|05|07|08|09)\\d{8}$/"))
                    phoneNumberInput.setError("Phone number is not a valid number");

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
}