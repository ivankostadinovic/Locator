package com.example.locator;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityCreateAccount extends ActivityBase {
    private EditText editEmail, editPass, editConfirmPass, editName;
    private Button btnCreateAccount;

    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initializeComponents();

        btnCreateAccount = (Button) findViewById(R.id.btn_create_account);
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User u = new User();
                if (!validate())
                    return;
                u.setEmail(editEmail.getText().toString());
                u.setPassword(editPass.getText().toString());
                u.setName(editName.getText().toString());

                LocatorData.getInstance().registerUser(u,ActivityCreateAccount.this);
            }
        });

    }

    private boolean validate() {

        boolean flag = true;
        if (!validateEmail(editEmail))
            flag = false;
        if (!emptyCheck(new EditText[]{editName}))
            flag = false;
        if (!passCheck(editPass, editConfirmPass))
            flag = false;
        return flag;
    }



    @Override
    public void initializeComponents() {
        editEmail = (EditText) findViewById(R.id.edit_email_create);
        editPass = (EditText) findViewById(R.id.edit_pass_create);
        editConfirmPass = (EditText) findViewById(R.id.edit_confirm_pass_create);
        editName = (EditText) findViewById(R.id.edit_name_create);

        db = FirebaseDatabase.getInstance().getReference();


    }
}
