package com.bloodbankapp.pref;


import android.content.Context;
import android.content.SharedPreferences;

import com.bloodbankapp.models.Doner;
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

    public void saveDonerProfile(Doner doner) {
        String donerJson = new Gson().toJson(doner);
        mPreferences.edit().putString(USER_PROFILE, donerJson).apply();
    }

    public void saveId(String id) {
        mPreferences.edit().putString(USER_ID, id).apply();
    }

    public String getId() {
        return mPreferences.getString(USER_ID, "");
    }

    public Doner fetchDonerProfile(Doner doner) {
        Doner userProfile;
        String donerJson = mPreferences.getString(USER_PROFILE, "");
        assert donerJson != null;
        if (!donerJson.isEmpty()) {
            userProfile = new Gson().fromJson(donerJson, Doner.class);
        } else {
            userProfile = new Doner();
        }
        return userProfile;
    }

    public void clearPrefsData() {
        mPreferences.edit()
                .remove(USER_PROFILE)
                .remove(USER_ID).apply();
    }
}
