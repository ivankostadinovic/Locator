package com.example.locator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLoginEmail extends ActivityBase implements View.OnClickListener {

    private EditText editEmail,editPass;
    private Button btnLogin,txtCreateAccount,txtForgotPassword;
    private FirebaseAuth auth;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
            LocatorData.getInstance().loadUser(user.getUid(), this);
        initializeComponents();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_login:
                loginAction();
                break;
            case R.id.textCreateAcc:
                startActivity(new Intent(this,ActivityCreateAccount.class));
                break;
            case R.id.textForgotPass:
                //new MaterialAlertDialogBuilder(this).setTitle("asdf").setMessage("asdf").setNegativeButton("asdf",null).setPositiveButton("asdf",null).show();
                startActivity(new Intent(this,ActivityForgotPassword.class));
                break;
        }
    }

    private void loginAction() {

        if(!emptyCheck(new EditText[]{editPass,editEmail}))
            return;



        loginUser(editEmail.getText().toString(),editPass.getText().toString());


    }

    private void loginUser(String email, String pass) {

        final OnCompleteListener<AuthResult> loginListener = new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                   FirebaseUser user=task.getResult().getUser();
                    LocatorData.getInstance().loadUser(user.getUid(),activity);

                } else {
                    Tools.showMsg(getApplicationContext(), "Wrong email/password.");

                }
            }
        };
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(ActivityLoginEmail.this, loginListener);
    }



    @Override
    public void initializeComponents() {
        editEmail=(EditText)findViewById(R.id.edit_email);
        editPass=(EditText)findViewById(R.id.edit_pass);
        txtCreateAccount=findViewById(R.id.textCreateAcc);
        txtForgotPassword=findViewById(R.id.textForgotPass);
        btnLogin=(Button)findViewById(R.id.btn_login);
        txtCreateAccount.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        activity=this;
        auth=FirebaseAuth.getInstance();

    }


}
