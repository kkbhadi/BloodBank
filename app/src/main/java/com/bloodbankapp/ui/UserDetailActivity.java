package com.bloodbankapp.ui;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bloodbankapp.R;
import com.bloodbankapp.models.Donor;

public class UserDetailActivity extends BaseActivity {

    Button btnToCall, btnToMail;
    TextView userName, userAddress, userBloodGroupName;
    String phone, email;
    String mailSubject = "Urgent requirement for blood";
    String mailBody = "Hi," +
            "We need urgent blood of your group.There is a serious condition,If you can help,please contact ASAP";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        btnToCall = (Button) findViewById(R.id.btnReachViaPhone);
        btnToMail = (Button) findViewById(R.id.btnReachViaEmail);
        userBloodGroupName = (TextView) findViewById(R.id.tvBloodGrpNameUserDetail);
        userAddress = (TextView) findViewById(R.id.tvAddressUserDetail);
        userName = (TextView) findViewById(R.id.expandedTxt);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Donor model = (Donor) bundle.getSerializable("donor");

        userBloodGroupName.setText(model.getBloodGroup());
        userAddress.setText(model.getAddress());
        userName.setText(model.getName());
        phone = model.getPhone().toString();
        email = model.getEmail();

        btnToCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialer(phone);
            }
        });
        btnToMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMail(email, mailSubject, mailBody);

            }
        });

    }


    // fun to open phone's dialer
    void openDialer(String phone) {
        startActivity((new Intent(Intent.ACTION_DIAL)).setData(Uri.parse("tel:" + phone)));
    }

    // fun to  open mail screen
    void openMail(String email, String mailSubject, String mailBody) {
        Uri uri = Uri.parse("mailto:" + email)
                .buildUpon()
                .appendQueryParameter("subject",mailSubject)
                .appendQueryParameter("body", mailBody)
                .build();

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(emailIntent);
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            showToast("No email app is found");
        }
    }

}
