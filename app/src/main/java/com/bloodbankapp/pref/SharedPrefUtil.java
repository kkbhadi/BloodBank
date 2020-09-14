package com.bloodbankapp.pref;


import android.content.Context;
import android.content.SharedPreferences;

import com.bloodbankapp.models.Donor;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public  class SharedPrefUtil {

    //Keys
    private static final String USER_PROFILE = "userProfile";
    private static final String USER_ID = "userId";

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.bloodbankapp";

    public SharedPrefUtil(Context context) {
        mPreferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }

    public void saveDonorProfile(Donor donor) {
        String donorJson = new Gson().toJson(donor);
        mPreferences.edit().putString(USER_PROFILE, donorJson).apply();
    }

    public void saveId(String id) {
        mPreferences.edit().putString(USER_ID, id).apply();
    }

    public String getId() {
        return mPreferences.getString(USER_ID, "");
    }

    public Donor fetchDonorProfile(Donor donor) {
        Donor userProfile;
        String donorJson = mPreferences.getString(USER_PROFILE, "");
        assert donorJson != null;
        if (!donorJson.isEmpty()) {
            userProfile = new Gson().fromJson(donorJson, Donor.class);
        } else {
            userProfile = new Donor();
        }
        return userProfile;
    }

    public void clearPrefsData() {
        mPreferences.edit()
                .remove(USER_PROFILE)
                .remove(USER_ID).apply();
    }
}
