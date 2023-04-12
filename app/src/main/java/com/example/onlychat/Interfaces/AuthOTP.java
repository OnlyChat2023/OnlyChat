package com.example.onlychat.Interfaces;

public interface AuthOTP {
    public void autoFill(String sms_otp_code);
    public void onSuccess(String token);
}
