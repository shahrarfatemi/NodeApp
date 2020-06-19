package com.example.nodeapp;

import androidx.appcompat.app.AppCompatActivity;

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

    Button saveButton;
    EditText nameEditText;
    EditText positionEditText;
    EditText jerseyEditText;

    String BaseUrl = "http://10.0.2.2:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        saveButton = (Button) findViewById(R.id.saveButton);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        positionEditText = (EditText) findViewById(R.id.positionEditText);
        jerseyEditText = (EditText) findViewById(R.id.jerseyEditText);

        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == saveButton){
            String name = nameEditText.getText().toString();
            String pos = positionEditText.getText().toString();
            String jersey = jerseyEditText.getText().toString();
            Toast.makeText(getApplicationContext(),"name : "+name+" position : "+pos+" jersey : "+jersey, Toast.LENGTH_LONG).show();
            handleCommunication(name,pos,jersey);
        }
    }

    private void handleCommunication(String name,String pos,String jersey){
        HashMap<String,String> hashMap = new HashMap<>();

        hashMap.put("name",name);
        hashMap.put("position",pos);
        hashMap.put("jersey",jersey);

        Call<Void> call = retrofitInterface.executeInfo(hashMap);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    Toast.makeText(MainActivity.this,"saved info successfully", Toast.LENGTH_LONG).show();
                }else if(response.code() == 400){
                    Toast.makeText(MainActivity.this, "wrong info", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }
}