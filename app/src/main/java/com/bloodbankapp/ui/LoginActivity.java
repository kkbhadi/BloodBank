package com.bloodbankapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bloodbankapp.R;
import com.bloodbankapp.firebase.FirebaseNetworkCallback;
import com.bloodbankapp.firebase.FirebaseRepository;
import com.bloodbankapp.models.Doner;
import com.bloodbankapp.pref.SharedPrefUtil;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {
    EditText etEmail, etPwd;
    RelativeLayout rlProgress;

    FirebaseRepository repository;
    SharedPrefUtil sharedPrefUtil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rlProgress = findViewById(R.id.rlProgress);

        // Get Repo
        repository = new FirebaseRepository();

        // get Pref
        sharedPrefUtil = new SharedPrefUtil(this);

        // set click listener
        findViewById(R.id.tvSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etEmail = findViewById(R.id.etEmailLogin);
                etPwd = findViewById(R.id.etPasswordLogin);

                if (etEmail.getText().toString().trim().isEmpty()) {
                    showToast("Please enter email");
                } else if (!isValidEmailAddress(etEmail.getText().toString().trim())) {
                    showToast("Please enter valid email");
                } else if (etPwd.getText().toString().trim().isEmpty()) {
                    showToast("Please enter password");
                } else if (etPwd.getText().toString().trim().length() < 8) {
                    showToast("Password should be minimum of 8 characters");
                } else {

                    // show loader
                    rlProgress.setVisibility(View.VISIBLE);
                    repository.signInUser(etEmail.getText().toString().trim(), etPwd.getText().toString().trim(), new FirebaseNetworkCallback() {
                        @Override
                        public void onSuccess(Object data) {
                            // get user profile
                            repository.fetchUserData(((FirebaseUser) data).getUid(), new FirebaseNetworkCallback() {
                                @Override
                                public void onSuccess(Object data) {
                                    // save profile and move to home screen
                                    // hide loader
                                    Doner doner = (Doner) data;
                                    rlProgress.setVisibility(View.GONE);
                                    sharedPrefUtil.saveDonerProfile(doner);
                                    sharedPrefUtil.saveId(doner.getDonerId());
                                    navigateToHomeScreen();

                                }

                                @Override
                                public void onFailure(String error) {
                                    // hide loader
                                    rlProgress.setVisibility(View.GONE);
                                    showToast(error);
                                }
                            });
                        }

                        @Override
                        public void onFailure(String error) {
                            // hide loader
                            rlProgress.setVisibility(View.GONE);
                            showToast(error);
                        }
                    });
                }

            }
        });
    }

    public void navigateToHomeScreen() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}
