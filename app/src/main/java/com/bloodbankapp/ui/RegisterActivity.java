package com.bloodbankapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.bloodbankapp.R;
import com.bloodbankapp.firebase.FirebaseNetworkCallback;
import com.bloodbankapp.firebase.FirebaseRepository;
import com.bloodbankapp.models.Donor;
import com.bloodbankapp.pref.SharedPrefUtil;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class RegisterActivity extends BaseActivity {
    EditText etEmail, etName, etAddress, etCity, etPass, etNumber;
    CheckBox checkBox;
    Spinner spinner;
    String selectedBloodGroup = "";
    RelativeLayout rlProgress;

    FirebaseRepository repository;
    SharedPrefUtil sharedPrefUtil;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rlProgress = findViewById(R.id.rlProgress);

        // Get Repo
        repository = new FirebaseRepository();

        // get Pref
        sharedPrefUtil = new SharedPrefUtil(this);


        Button button = (Button) findViewById(R.id.btnSignUpRegister);

        checkBox = (CheckBox) findViewById(R.id.checkboxRegister);
        spinner = (Spinner) findViewById(R.id.spinner);
        final ArrayList<String> bloodGroup = new ArrayList<>();
        bloodGroup.add("Select");
        bloodGroup.add("AB+");
        bloodGroup.add("AB-");
        bloodGroup.add("A+ ");
        bloodGroup.add("A- ");
        bloodGroup.add("O+ ");
        bloodGroup.add("O- ");
        bloodGroup.add("B+ ");
        bloodGroup.add("B- ");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bloodGroup);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0 && checkBox.isChecked()) {
                    selectedBloodGroup = bloodGroup.get(position);
                } else {
                    selectedBloodGroup = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner.setEnabled(false);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doLogin();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    selectedBloodGroup = "";
                    spinner.setEnabled(false);
                    spinner.setSelection(0);
                } else {
                    spinner.setEnabled(true);
                }
            }
        });
    }


    public void doLogin() {
        etEmail = (EditText) findViewById(R.id.etEmailRegister);
        etPass = (EditText) findViewById(R.id.etPasswordRgister);
        etName = (EditText) findViewById(R.id.etNameRegister);
        etCity = (EditText) findViewById(R.id.etCityRgister);
        etAddress = (EditText) findViewById(R.id.etAAddressRegister);
        etNumber = (EditText) findViewById(R.id.etNumberRgister);
        if (etName.getText().toString().trim().isEmpty()) {
            showToast("Please enter name");
        } else if (etEmail.getText().toString().trim().isEmpty()) {
            showToast("Please enter email");
        } else if (!isValidEmailAddress(etEmail.getText().toString().trim())) {
            showToast("Please enter valid email");
        } else if (etPass.getText().toString().trim().isEmpty()) {
            showToast("Please enter password");
        } else if (etPass.getText().toString().trim().length() < 8) {
            showToast("Password should be minimum of 8 characters");
        } else if (!etNumber.getText().toString().trim().isEmpty() && (etNumber.getText().toString().trim().length() < 10 || etNumber.getText().toString().trim().length() > 10)) {
            showToast("Please enter valid number");
        } else if (etCity.getText().toString().trim().isEmpty()) {
            showToast("Please enter city");
        } else if (etAddress.getText().toString().trim().isEmpty()) {
            showToast("Please enter address");

        } else if (checkBox.isChecked() && selectedBloodGroup.isEmpty()) {
            showToast("Please choose a valid blood group");
        } else {
            // show loader
            rlProgress.setVisibility(View.VISIBLE);
            repository.signUpUser(etEmail.getText().toString().trim(), etPass.getText().toString().trim(), new FirebaseNetworkCallback() {
                @Override
                public void onSuccess(Object data) {
                    final Donor donor = new Donor();
                    donor.setAddress(etAddress.getText().toString().trim());
                    donor.setEmail(etEmail.getText().toString().trim());
                    donor.setName(etName.getText().toString().trim());

                    if (!etNumber.getText().toString().trim().isEmpty())
                        donor.setPhone(Long.parseLong(etNumber.getText().toString().trim()));
                    donor.setCity(etCity.getText().toString().trim());
                    donor.setDonorId(((FirebaseUser) data).getUid());
                    donor.setAddress(etAddress.getText().toString().trim());
                    if (checkBox.isChecked() && !selectedBloodGroup.isEmpty()) {
                        donor.setBloodGroup(selectedBloodGroup);
                        donor.setDonor(true);
                    }

                    // now store user info in db.
                    repository.saveUserData(donor, new FirebaseNetworkCallback() {
                        @Override
                        public void onSuccess(Object data) {
                            // hide loader
                            rlProgress.setVisibility(View.GONE);
                            // SAVE DATA in Prefs and move to next Screen
                            showToast("User Registered");
                            sharedPrefUtil.saveDonorProfile(donor);
                            sharedPrefUtil.saveId(donor.getDonorId());

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

    public void navigateToHomeScreen() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        }, 1000);
    }
}
