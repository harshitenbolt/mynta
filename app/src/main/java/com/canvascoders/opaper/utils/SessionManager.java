package com.canvascoders.opaper.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.canvascoders.opaper.activity.LoginActivity;


public class SessionManager {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "opaper";
    private static final String IS_LOGIN = "IsLoggedIn";

    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLogin(String agent_id, String tkn, String name, String email, String rh_id, String emp_id, String mobile, String city/*, String ismobileVerify*/) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(Constants.KEY_AGENTID, agent_id);
        editor.putString(Constants.KEY_RH_ID, rh_id);
        editor.putString(Constants.KEY_TOKEN, tkn);
        editor.putString(Constants.KEY_EMP_ID, emp_id);
        editor.putString(Constants.KEY_NAME, name);
        editor.putString(Constants.KEY_EMAIL, email);
        editor.putString(Constants.KEY_VENDOR_MOBILE, mobile);
        editor.putString(Constants.KEY_EMP_CITY, city);
        //editor.putString(Constants.KEY_IS_MOBILE_VERIFY, ismobileVerify);
        editor.commit();
    }

    public void saveData(String key, String value) {
        SharedPreferences.Editor prefsEditor = pref.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (pref != null) {
            return pref.getString(key, "");
        }
        return "";
    }


    public String getMailID() {
        return pref.getString(Constants.KEY_EMAIL, null);
    }

    public String getAgentID() {
        return pref.getString(Constants.KEY_AGENTID, null);
    }

    public String getMobile() {
        return pref.getString(Constants.KEY_VENDOR_MOBILE, null);
    }

    public String getIsmobileVerify() {
        return pref.getString(Constants.KEY_IS_MOBILE_VERIFY, null);
    }

    public String getName() {
        return pref.getString(Constants.KEY_NAME, null);
    }

    public String getEmail() {
        return pref.getString(Constants.KEY_EMAIL, null);
    }

    public String getRHID() {
        return pref.getString(Constants.KEY_RH_ID, null);
    }

    public String getEmpId() {
        return pref.getString(Constants.KEY_EMP_ID, null);
    }

    public String getToken() {
        return pref.getString(Constants.KEY_TOKEN, null);
    }

    public void logoutUser(final Context context) {
        String fcmId = getData(Constants.KEY_FCM_ID);
        editor.clear();
        editor.commit();
        saveData(Constants.KEY_FCM_ID, fcmId);
        Toast.makeText(_context, "Logged Out", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(_context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _context.startActivity(intent);
        ((Activity) _context).finish();

    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
