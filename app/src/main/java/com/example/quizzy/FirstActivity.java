package com.example.quizzy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstActivity extends AppCompatActivity {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    String BaseUrl = "https://contest-quiz-app.herokuapp.com/";
    DatabaseHelper databaseHelper;
    public static int SPLASH_SCREEN = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        databaseHelper = new DatabaseHelper(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                navigation();
            }
        },SPLASH_SCREEN);
//        Toast.makeText(FirstActivity.this," token : "+token, Toast.LENGTH_LONG).show();
    }
    
    public void navigation(){
//        removeInfo();
        String token = databaseHelper.findValidToken();
        Toast.makeText(FirstActivity.this," token : "+token, Toast.LENGTH_LONG).show();
        if(!token.equals("")){
            moveToHomePage(token);
        }
        else{
            Intent intent = new Intent(FirstActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void moveToHomePage(final String header){
        Call<Object> call = retrofitInterface.executeHomeNavigation("Bearer "+header);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //Toast.makeText(FirstActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());
                        String name = jsonObject.getString("name");
                        String email = jsonObject.getString("email");
                        String _id = jsonObject.getString("_id");
                        UserInfo userInfo = new UserInfo(name,email,header);
                        userInfo.set_id(_id);
                        Toast.makeText(FirstActivity.this, "navigate to home page " + name +"\n"+email, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(FirstActivity.this,HomeActivity.class);
                        intent.putExtra("MyInfo",userInfo);
                        startActivity(intent);
                        finish();
                    }catch (JSONException e){
                        Toast.makeText(FirstActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                    }
                }else if(response.code() == 400){
                    Toast.makeText(FirstActivity.this, "token not authorized", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(FirstActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void removeInfo(){
        int id = databaseHelper.getIdFromEmail("asif@gmail.com");
        if(id != -1){
            int isDeleted = databaseHelper.deleteDatabase(id);
            if(isDeleted == 0){
                Toast.makeText(FirstActivity.this,"token could not be deleted \n",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(FirstActivity.this,"token deleted \n",Toast.LENGTH_LONG).show();
            }
        }

    }
}