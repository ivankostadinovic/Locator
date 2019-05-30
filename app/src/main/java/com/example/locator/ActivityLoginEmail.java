package com.example.locator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityLoginEmail extends AppCompatActivity implements View.OnClickListener {


    private TextView txtCreateAccount,txtForgotPassword;
    private EditText editEmail,editPass;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        editEmail=(EditText)findViewById(R.id.edit_email);
        editPass=(EditText)findViewById(R.id.edit_pass);
        txtCreateAccount=(TextView)findViewById(R.id.textCreateAcc);
        txtForgotPassword=(TextView)findViewById(R.id.textForgotPass);
        btnLogin=(Button)findViewById(R.id.btn_login);

        txtForgotPassword.setClickable(true);
        txtCreateAccount.setClickable(true);
        txtCreateAccount.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_login_email:

                break;
            case R.id.textCreateAcc:
                startActivity(new Intent(this,ActivityCreateAccount.class));
                break;
            case R.id.textForgotPass:
                startActivity(new Intent(this,ActivityForgotPassword.class));
                break;
        }
    }
}
