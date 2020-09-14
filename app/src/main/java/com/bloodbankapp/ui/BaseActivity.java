package com.bloodbankapp.ui;


import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bloodbankapp.firebase.FirebaseRepository;
import com.bloodbankapp.pref.SharedPrefUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class BaseActivity extends AppCompatActivity {


    FirebaseRepository repository;
    SharedPrefUtil sharedPrefUtil;

    @Override
    protected void onStart() {
        super.onStart();

        // Get Repo
        repository = new FirebaseRepository();

        // get Pref
        sharedPrefUtil = new SharedPrefUtil(this);
    }


    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

}
