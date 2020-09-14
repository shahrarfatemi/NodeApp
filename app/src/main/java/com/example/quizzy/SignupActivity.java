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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    Button signupButton,loginButton;
    EditText nameEditText;
    EditText emailEditText;
    EditText passEditText;

    String BaseUrl = "https://contest-quiz-app.herokuapp.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passEditText = (EditText) findViewById(R.id.passEditText);

        loginButton.setOnClickListener(this);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == signupButton){
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String pass = passEditText.getText().toString();
            Toast.makeText(SignupActivity.this,"name : "+name+" email : "+email+" password : "+pass, Toast.LENGTH_LONG).show();
            handleSignup(name,email,pass);
        }
        if(v == loginButton){
            onBackPressed();
        }
    }

    private void handleSignup(String name,String email,String pass){
        HashMap<String,String> hashMap = new HashMap<>();

        hashMap.put("name",name);
        hashMap.put("email",email);
        hashMap.put("password",pass);

        Call<Void> call = retrofitInterface.executeInfo(hashMap);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 201){
                    Toast.makeText(SignupActivity.this,"saved info successfully "+response.body().toString(), Toast.LENGTH_LONG).show();
                }else if(response.code() == 400){
                    Toast.makeText(SignupActivity.this, "wrong info", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(SignupActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}