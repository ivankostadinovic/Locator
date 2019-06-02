package com.example.locator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityForgotPassword extends ActivityBase {

    private EditText editEmail;
    private Button btnSend, btnCancel;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeComponents();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityForgotPassword.this.finish();
            }
        });


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

                            Toast.makeText(ActivityForgotPassword.this, "The recovery email was sent to your address.", Toast.LENGTH_LONG).show();
                            ActivityForgotPassword.this.finish();
                        }
                        else
                        {
                            Toast.makeText(ActivityForgotPassword.this, "Invalid email.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void initializeComponents() {
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(layoutParams);
        int width=dm.widthPixels;
        int height=dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*0.25)) ;

        editEmail=(EditText) findViewById(R.id.edit_email_forgot);
        btnSend=(Button) findViewById(R.id.btn_forgot);
        btnCancel=(Button)findViewById(R.id.btn_Cancel);
        auth=FirebaseAuth.getInstance();


    }
}
