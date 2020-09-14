package com.bloodbankapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bloodbankapp.R;
import com.bloodbankapp.adapter.HomeAdapter;
import com.bloodbankapp.firebase.FirebaseNetworkCallback;
import com.bloodbankapp.firebase.FirebaseRepository;
import com.bloodbankapp.models.Doner;
import com.bloodbankapp.pref.SharedPrefUtil;

import java.util.List;

public class HomeActivity extends BaseActivity {
    FirebaseRepository repository;
    SharedPrefUtil sharedPrefUtil;
    RelativeLayout rlProgress;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rlProgress = findViewById(R.id.rlProgress);

        // Get Repo
        repository = new FirebaseRepository();

        // get Pref
        sharedPrefUtil = new SharedPrefUtil(this);

        // get doner lists
        getDonerList();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);   //Selects the menu to use
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            exitApp();
        }
        return true;
    }

    public void getDonerList() {
        // show loader
        rlProgress.setVisibility(View.VISIBLE);
        repository.getDonerList(new FirebaseNetworkCallback() {
            @Override
            public void onSuccess(Object data) {
                // hide loader
                rlProgress.setVisibility(View.GONE);
                List<Doner> donerList = (List<Doner>) data;

                if (donerList.isEmpty()) {
                    findViewById(R.id.rlNoData).setVisibility(View.VISIBLE);
                } else {
                    mAdapter = new HomeAdapter(donerList);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(String error) {
                // hide loader
                rlProgress.setVisibility(View.GONE);
                showToast(error);
            }
        });
    }


    // Call this on logout click
    public void signuoutUser() {
        repository.signOutUser();
        sharedPrefUtil.clearPrefsData();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void exitApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        signuoutUser();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}


