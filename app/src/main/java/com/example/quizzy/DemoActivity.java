package com.example.quizzy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemoActivity extends AppCompatActivity implements View.OnClickListener {
    Button button;
    TextView textView;

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    NetworkUtil networkUtil;
    String BaseUrl = "https://contest-quiz-app.herokuapp.com/";

    String header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        button = (Button) findViewById(R.id.buttonID);
        textView = (TextView) findViewById(R.id.textID);
        Intent intent = getIntent();
        UserInfo userInfo = (UserInfo) intent.getSerializableExtra("MyInfo");
        header = userInfo.getToken();
        networkUtil = NetworkUtil.getInstance();
        button.setOnClickListener(this);
        Log.d("demo","Demo te dhukse");
    }

    @Override
    public void onClick(View v) {
        if(v == button){
//            if(call == null) {
//                Toast.makeText(DemoActivity.this, "deleted successfully ", Toast.LENGTH_LONG).show();
//                doDeleteAccount(call);
//            }
        }
    }

    private void doDeleteAccount(String header) {
        Call<Object> call = retrofitInterface.executeDeleteMyAccount("Bearer "+header);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    try {
//                        Toast.makeText(HomeActivity.this, "deleted successfully ", Toast.LENGTH_LONG).show();
                        JSONObject jsonObject =(JSONObject) response.body();
                        String _id = jsonObject.getString("_id");
                        String name = jsonObject.getString("name");
                        String email = jsonObject.getString("email");
                        UserInfo userInfo = new UserInfo();
                        userInfo.set_id(_id);
                        userInfo.setEmail(email);
                        userInfo.setName(name);
                        textView.setText(userInfo.toString());
                    }catch (Exception e){
//                        Toast.makeText(HomeActivity.this,"exception "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }else if(response.code() == 500){
//                    Toast.makeText(HomeActivity.this, "could not delete", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
//                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showUser(UserInfo userInfo) {
        Toast.makeText(this,"show to sth",Toast.LENGTH_LONG).show();
        textView.setText(userInfo.toString());
    }
}
