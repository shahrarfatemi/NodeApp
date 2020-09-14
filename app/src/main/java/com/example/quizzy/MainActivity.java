package com.example.nodeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    Button loginButton;
    Button signupNavigationButton;
    EditText emailEditText;
    EditText passEditText;

    String BaseUrl = "https://contest-quiz-app.herokuapp.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupNavigationButton = (Button) findViewById(R.id.signupNavigationButton);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passEditText = (EditText) findViewById(R.id.passEditText);

        loginButton.setOnClickListener(this);
        signupNavigationButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == loginButton){
            String email = emailEditText.getText().toString();
            String pass = passEditText.getText().toString();
            Toast.makeText(getApplicationContext()," email : "+email+" password : "+pass, Toast.LENGTH_LONG).show();
            handleLogin(email,pass);
        }
        if(v == signupNavigationButton){
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        }
    }

    private void handleLogin(String email,String pass){
        HashMap<String,String> hashMap = new HashMap<>();

        hashMap.put("email",email);
        hashMap.put("password",pass);

        Call<Object> call = retrofitInterface.executeLogin(hashMap);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    Toast.makeText(MainActivity.this,"saved info successfully "+response.body().toString(), Toast.LENGTH_LONG).show();
                }else if(response.code() == 400){
                    Toast.makeText(MainActivity.this, "wrong info", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}