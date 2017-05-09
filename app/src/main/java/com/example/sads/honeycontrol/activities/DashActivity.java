package com.example.sads.honeycontrol.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sads.honeycontrol.R;
import com.example.sads.honeycontrol.Utils.Util;
import com.example.sads.honeycontrol.fragments.ClientFragment;
import com.example.sads.honeycontrol.fragments.ProductFragment;

public class DashActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences prefs;
    private int id;
    private String passw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);
        prefs =getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        if(!TextUtils.isEmpty(Util.getPassPrefs(prefs)) && Util.getIdPrefs(prefs)>0){
            id=Util.getIdPrefs(prefs);
            passw=Util.getPassPrefs(prefs);
        }else {
            id = getIntent().getExtras().getInt("id");
            passw = getIntent().getExtras().getString("pass");
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.naview);
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        navigationView.setNavigationItemSelectedListener(clicOptions);
        setToolbar();
    }

    private NavigationView.OnNavigationItemSelectedListener clicOptions = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            boolean fragmentTransaction = false;
            Fragment fragment = null;

            switch (item.getItemId()){
                case R.id.menu_client:
                    fragment = new ClientFragment();
                    fragmentTransaction=true;
                    break;
                case R.id.menu_products:
                    fragment = new ProductFragment();
                    fragmentTransaction=true;
                    break;
               /* case R.id.menu_info:
                    fragment = new InfoFragment();
                    fragmentTransaction=true;
                    break;*/
            }

            if(fragmentTransaction){
                item.setChecked(false);
                changeFragment(fragment,item);
                drawerLayout.closeDrawers();
            }

            return true;
        }
    };

    private void changeFragment(Fragment fragment, MenuItem item){
        Bundle data = new Bundle();
        data.putString("id", "1");
        data.putString("pass",passw);
        fragment.setArguments(data);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,fragment).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
    }

    public void setToolbar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_logout:
                logOut();
                return true;
            case R.id.menu_forget_logout:
                Util.removeSharedPreferences(prefs);
                logOutForget();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void logOutForget(){
        Util.removeSharedPreferences(prefs);
        logOut();
    }
    private void logOut(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }



}
