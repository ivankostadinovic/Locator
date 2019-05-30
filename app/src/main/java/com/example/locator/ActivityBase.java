package com.example.locator;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.widget.EditText;
public abstract class ActivityBase extends AppCompatActivity

{
    private ProgressDialog progressDialog;

    public void showProgress()
    {
        if(progressDialog==null)
        {
            progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Ucitavanje");

        }
        progressDialog.show();
    }
    public String splitString(String string)
    {
        String p;
        if(string.contains(", "))
        {
            String rez[] = string.split(", ");
            p = rez[rez.length - 1];
        }
        else
            p=string;
        return p;

    }
    public boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean flag=activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if(!flag)

        {
            //new AlertDialog.Builder(this, R.style.CustomAlertDialog).setTitle("Obavestenje").setMessage("Da bi aplikacija funkcionisala, obezbedite internet konekciju.").setCancelable(true).show();
        }
        return flag;
    }
    public void hideProgress()
    {
        if(progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public boolean emptyCheck(EditText[] editTexts) {
        boolean flag = true;
        for (int i = 0; i < editTexts.length; i++) {
            if (editTexts[i].getText().toString().isEmpty()) {
                editTexts[i].setError("Polje ne sme biti prazno!");
                flag = false;
            }
        }
        return flag;

    }

    public abstract void initializeComponents();


    public boolean passCheck(EditText editPass,EditText editConfirmPass) {
        if (editPass.getText().toString().length() < 6) {
            editPass.setError("Password mora biti minum 6 karaktera");
            editConfirmPass.setError("Password mora biti minum 6 karaktera");
            return false;
        }
        if (!editPass.getText().toString().equals(editConfirmPass.getText().toString())) {
            editPass.setError("Ne poklapaju se passwordi!");
            editConfirmPass.setError("Ne poklapaju se passwordi!");
            return false;
        }
        return true;
    }

    public boolean validateEmail(EditText editEmail) {
        String email = editEmail.getText().toString();
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editEmail.setError("Nevalidan email!");
            return false;
        }
        return  true;
    }
}
