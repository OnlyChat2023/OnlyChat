package com.example.onlychat;

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

public class RegisterActivity extends AppCompatActivity {

    private ImageView showPasswordBtn;
    private EditText passwordInput, passwordConfirmInput;

    private boolean isHidePassword = true;
    private Button LoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register_form);

        Intent loginIntent = new Intent(this, MainActivity.class);

        LoginBtn = (Button) findViewById(R.id.signInBtn);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
                finishAffinity();
            }
        });

        showPasswordBtn = (ImageView) findViewById(R.id.showPassword);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        passwordConfirmInput = (EditText) findViewById(R.id.confirmPasswordInput);

        showPasswordBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isHidePassword == true)
                {
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