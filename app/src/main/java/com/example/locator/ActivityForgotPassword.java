package com.example.locator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityForgotPassword extends ActivityBase {

    private EditText editEmail;
    private Button btnSend;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeComponents();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateEmail(editEmail))
                    return;
                sendEmail(editEmail.getText().toString());

            }
        });
    }


    private void sendEmail(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(ActivityForgotPassword.this, "Email je poslat na vasu adresu.", Toast.LENGTH_LONG).show();
                            ActivityForgotPassword.this.finish();
                        }
                        else
                        {
                            Toast.makeText(ActivityForgotPassword.this, "Nepostojeci Email!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void initializeComponents() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.3)) ;

        editEmail=(EditText) findViewById(R.id.edit_email_forgot);
        btnSend=(Button) findViewById(R.id.btn_forgot);
        auth=FirebaseAuth.getInstance();


    }
}
