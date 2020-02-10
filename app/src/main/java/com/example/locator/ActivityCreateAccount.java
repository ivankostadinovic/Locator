package com.example.locator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ActivityCreateAccount extends ActivityBase {
    private EditText editEmail, editPass, editConfirmPass, editName;
    private Button btnCreateAccount;
    private int REQUEST_IMAGE_CAPTURE = 555;
    private User u = new User();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        initializeComponents();

        btnCreateAccount = findViewById(R.id.btn_create_account);
        btnCreateAccount.setOnClickListener(v -> {
            if (!validate())
                return;
            u.setEmail(editEmail.getText().toString());
            u.setPassword(editPass.getText().toString());
            u.setName(editName.getText().toString());

            LocatorData.getInstance().registerUser(u, ActivityCreateAccount.this);
        });

        findViewById(R.id.btn_camera).setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 50);
            else {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    private boolean validate() {

        return validateEmail(editEmail) && emptyCheck(new EditText[]{editName}) && passCheck(editPass, editConfirmPass) && u.getProfilePicture()!= null && !TextUtils.isEmpty(u.getProfilePicture());
    }


    @Override
    public void initializeComponents() {
        editEmail = findViewById(R.id.edit_email_create);
        editPass = findViewById(R.id.edit_pass_create);
        editConfirmPass = findViewById(R.id.edit_confirm_pass_create);
        editName = findViewById(R.id.edit_name_create);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 50) {
            if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                final Bitmap imageBitmap = (Bitmap) extras.get("data");
                ((ImageView) findViewById(R.id.imageView)).setImageBitmap(imageBitmap);
                new Thread(() -> u.setProfilePicture(Tools.BitMapToString(imageBitmap))).start();
            }
        }
    }
}
