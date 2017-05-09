package com.example.sads.honeycontrol.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.sads.honeycontrol.R;
import com.example.sads.honeycontrol.Utils.Util;
import com.example.sads.honeycontrol.models.result;
import com.example.sads.honeycontrol.service.ApiAdapter;
import com.example.sads.honeycontrol.service.response.ResponseLogin;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText pass;
    private EditText user;
    private Switch remember;
    private Button btnLogin;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE);
        user = (EditText) findViewById(R.id.editTextUser);
        pass = (EditText) findViewById(R.id.editTextPass);
        remember = (Switch) findViewById(R.id.switchRemember);
        btnLogin= (Button) findViewById(R.id.buttonLogin);
        setCredentialsIfExist();
        btnLogin.setOnClickListener(clicLogin);
    }

    private View.OnClickListener clicLogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String users = user.getText().toString().trim();
            String password = pass.getText().toString().trim();
            if(Util.isValidText(users) && Util.isValidText(password)){
                if(remember.isChecked()){
                    saveOnPreferences(users,password);
                }
                obtenerDatos(users,password);
            }else{
                Toast.makeText(getApplicationContext(),"Campos vacios, verifica",Toast.LENGTH_LONG).show();
            }
        }
    };

    private void setCredentialsIfExist(){
        String UserPrefe = Util.getUserPrefs(prefs);
        String passPrefe = Util.getPassPrefs(prefs);
        int id  = Util.getIdPrefs(prefs);
        if(!TextUtils.isEmpty(UserPrefe) && !TextUtils.isEmpty(passPrefe)){
            user.setText(UserPrefe);
            pass.setText(passPrefe);
            remember.setChecked(true);
        }
    }

    private void saveOnPreferences(String email, String password){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user",email);
            editor.putString("password",password);
            editor.commit();
            editor.apply();
    }
    public void obtenerDatos(String user, String pass){
        Call<ResponseLogin> call = ApiAdapter.getApiService().getLogin(user,pass);
        call.enqueue( new ResponsableCallBack());
    }

    class ResponsableCallBack implements Callback<ResponseLogin> {

        @Override
        public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
            if(response.isSuccessful()){
                ResponseLogin responsable= response.body();
                if(responsable.isResponde()){
                    //Toast.makeText(getApplicationContext(),"pass"+responsable.getPass(),Toast.LENGTH_LONG).show();

                    openDash(responsable.getResult());
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario o contraseña incorrecta, verifique",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"Error de conexión",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseLogin> call, Throwable t) {
            Toast.makeText(getApplicationContext(),"sadsgdfgfd"+t,Toast.LENGTH_LONG).show();
        }
    }

    public void openDash(ArrayList<result> result){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("id",result.get(0).getId());
        editor.commit();
        editor.apply();
        Intent intent = new Intent(this, DashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id",result.get(0).getId());
        intent.putExtra("pass",result.get(0).getPass());
        startActivity(intent);
    }
}
