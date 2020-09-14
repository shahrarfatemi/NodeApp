package com.example.quizzy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    DatabaseHelper databaseHelper;

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

        databaseHelper = new DatabaseHelper(this);
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
            signupButton.setText("Signing Up");
            handleSignup(name,email,pass);
        }
        if(v == loginButton){
            onBackPressed();
            finish();
        }
    }

    private void handleSignup(String name, String email, final String pass){
        HashMap<String,String> hashMap = new HashMap<>();

        hashMap.put("name",name);
        hashMap.put("email",email);
        hashMap.put("password",pass);

        Call<Object> call = retrofitInterface.executeInfo(hashMap);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 201){
                    try {
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());
                        String token = jsonObject.getString("token");
                        JSONObject userjsonObject = (JSONObject) jsonObject.get("user");
                        String name = userjsonObject.getString("name");
                        String email = userjsonObject.getString("email");
                        Log.d("email ",email);
                        String _id = userjsonObject.getString("_id");
                        Log.d("_id",_id);
                        UserInfo userInfo = new UserInfo(name,email,token,pass);
                        userInfo.set_id(_id);
                        Toast.makeText(SignupActivity.this, "saved info successfully " + name +"\n"+email+ "\n"+token, Toast.LENGTH_LONG).show();
                        saveInfo(name, email, token);
                        Intent intent = new Intent(SignupActivity.this,HomeActivity.class);
                        intent.putExtra("MyInfo",userInfo);
                        startActivity(intent);
                        finish();
                    }catch (JSONException e){
                        Toast.makeText(SignupActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }else if(response.code() == 400){
                    signupButton.setText("Sign Up");
                    Toast.makeText(SignupActivity.this, "could not sign up", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                signupButton.setText("Sign Up");
                Toast.makeText(SignupActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void saveInfo(String name, String email, String token){
        if (databaseHelper.insertToDatabase(name, email, token) == -1) {
            Toast.makeText(SignupActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SignupActivity.this, "INSERTED", Toast.LENGTH_SHORT).show();
        }
    }
}