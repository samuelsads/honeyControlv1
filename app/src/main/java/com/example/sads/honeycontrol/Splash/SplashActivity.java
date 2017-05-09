package com.example.sads.honeycontrol.Splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.sads.honeycontrol.R;
import com.example.sads.honeycontrol.Utils.Util;
import com.example.sads.honeycontrol.activities.DashActivity;
import com.example.sads.honeycontrol.activities.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        Intent intentLogin = new Intent(this, LoginActivity.class);
        Intent intentMain = new Intent(this, DashActivity.class);
        if(!TextUtils.isEmpty(Util.getUserPrefs(prefs)) && !TextUtils.isEmpty(Util.getPassPrefs(prefs))){
            Toast.makeText(this,"asdadadad" +Util.getUserPrefs(prefs),Toast.LENGTH_LONG).show();
            startActivity(intentMain);
        }else{
            startActivity(intentLogin);
        }
        finish();
    }


}
