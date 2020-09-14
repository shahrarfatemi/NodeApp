package com.example.quizzy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    DatabaseHelper databaseHelper;

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

        databaseHelper = new DatabaseHelper(this);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupNavigationButton = (Button) findViewById(R.id.signupNavigationButton);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passEditText = (EditText) findViewById(R.id.passEditText);

        loginButton.setOnClickListener(this);
        signupNavigationButton.setOnClickListener(this);

        //db
//        String token = databaseHelper.findValidToken();
//        Toast.makeText(MainActivity.this," token : "+token, Toast.LENGTH_LONG).show();
//        if(!token.equals("")){
//
//            moveToHomePage(token);
//        }
    }

    @Override
    public void onClick(View v) {
        if(v == loginButton){
            String email = emailEditText.getText().toString();
            String pass = passEditText.getText().toString();
            Toast.makeText(getApplicationContext()," email : "+email+" password : "+pass, Toast.LENGTH_LONG).show();
            handleLogin(email,pass);
            loginButton.setText("Logging in");
        }
        if(v == signupNavigationButton){
            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
        }
    }

    private void handleLogin(String email, final String pass){
        HashMap<String,String> hashMap = new HashMap<>();

        hashMap.put("email",email);
        hashMap.put("password",pass);

        Call<Object> call = retrofitInterface.executeLogin(hashMap);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());
                        String token = jsonObject.getString("token");
                        JSONObject userjsonObject = (JSONObject) jsonObject.get("user");
                        String name = userjsonObject.getString("name");
                        String email = userjsonObject.getString("email");
                        String _id = userjsonObject.getString("_id");
                        UserInfo userInfo = new UserInfo(name,email,token,pass);
                        userInfo.set_id(_id);
                        Toast.makeText(MainActivity.this, "saved info successfully " + name +"\n"+email+ "\n"+token, Toast.LENGTH_LONG).show();
                        saveInfo(name,email,token);
                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        intent.putExtra("MyInfo",userInfo);
                        startActivity(intent);
                        finish();
                    }catch (JSONException e){
                        Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }else if(response.code() == 400){
                    loginButton.setText("Log In");
                    Toast.makeText(MainActivity.this, "wrong credentials,could not log in", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                loginButton.setText("Log In");
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void saveInfo(String name, String email, String token){
        if (databaseHelper.insertToDatabase(name, email, token) == -1) {
            Toast.makeText(MainActivity.this, "not inserted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "INSERTED", Toast.LENGTH_SHORT).show();

        }
    }


}