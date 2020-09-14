package com.example.quizzy;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtil {
    Retrofit retrofit;
    RetrofitInterface retrofitInterface;
    JSONObject jsonObject;//for catching jsonObject
    JSONArray jsonArray;//for catching jsonArray
    List<Object> jsonList;//for catching list of jsonObjects

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = new JSONObject();
        this.jsonObject = jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public List<Object> getJsonList() {
        return jsonList;
    }

    public void setJsonList(List<Object> jsonList) {
        this.jsonList.clear();
        int len = jsonList.size();
        for(int i = 0 ; i < len ; i++) {
            this.jsonList.add(i,jsonList.get(i));
        }
    }

    String BaseUrl = "https://contest-quiz-app.herokuapp.com/";

    private static NetworkUtil networkUtil;
    private boolean taskDone;

    public boolean isTaskDone() {
        return taskDone;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }

    private NetworkUtil(){
        retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        taskDone = false;
        jsonObject = new JSONObject();
        jsonArray = new JSONArray();
        jsonList = new ArrayList<>();
    }

    public static NetworkUtil getInstance(){
        if(networkUtil == null){
            networkUtil = new NetworkUtil();
        }
        return networkUtil;
    }

    public void logOut(String header, @Nullable final UiInterface callBack){
        Call<Void> call = retrofitInterface.executeLogout("Bearer "+header);
//        Toast.makeText(HomeActivity.this,"inside this method",Toast.LENGTH_LONG).show();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    try {
                        callBack.logOut();
//                        Toast.makeText(HomeActivity.this, "logged out successfully ", Toast.LENGTH_LONG).show();
//                        removeInfo();
//                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
//                        startActivity(intent);
//                        finish();
                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }else if(response.code() == 400){
                    Log.d("response ","code => "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });

    }

    public void deleteMyAccount(String header, @Nullable final UiInterface callBack){
        Call<Object> call = retrofitInterface.executeDeleteMyAccount("Bearer "+header);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    try {
//                        Toast.makeText(HomeActivity.this, "deleted successfully ", Toast.LENGTH_LONG).show();
                        Log.d("call => ","before jsonObject");
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());
                        Log.d("call => ","object Got");
                        UserInfo userInfo = BuildObjects.buildUserInfo(jsonObject);
                        Log.d("user",userInfo.toString());
                        callBack.deleteUser(userInfo);
                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }else if(response.code() == 500){
                    Log.d("response ","code => "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });
    }

    public void logoutAllDevice(String header, @Nullable final UiInterface callBack){
        //format of the header is : header-name "Authorization", header-value "Bearer <token>"
        Call<Void> call = retrofitInterface.executeLogoutAll("Bearer "+header);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    try {
//                        Toast.makeText(HomeActivity.this, "logged out from all devices successfully ", Toast.LENGTH_LONG).show();
                        callBack.logOut();
                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }else if(response.code() == 500){
                    Log.d("response ","code => "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });

    }

    public void editMyAccount(String header, String name, String password, @Nullable final UiInterface callBack){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("password",password);
        Call<Object> call = retrofitInterface.executeEditMyAccount("Bearer "+header, hashMap);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    try {
                        //the server sends back the name,email,_id etc of the updated user,actually it can respond with other info too,
                        // check the doc for that we can get any string in the same way as below so pera naai
                        // new email and name has been assigned to the user and then updated in the sqlite database
                        Log.d("call => ","before jsonObject");
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());
                        UserInfo userInfo = BuildObjects.buildUserInfo(jsonObject);
                        Log.d("call => ","object Got");
                        Log.d("user",userInfo.toString());
                        callBack.showUser(userInfo);

                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }else if(response.code() == 400){
//                    Toast.makeText(HomeActivity.this, "could not edit", Toast.LENGTH_LONG).show();
                    Log.d("response ","code => "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });
    }

    public void showMyQuiz(String header, @Nullable final UiInterface callBack){
        Call<Object> call = retrofitInterface.executeGetMyQuiz(header);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    try{
                        List<Object> quizzes = (ArrayList<Object>) response.body();//all the JSONObect quizzes are here
                        List<Quiz> quizList = new ArrayList<>();//will derive quizList from the quizzes
                        //building quiz list,
                        for(int i = 0 ; i < quizzes.size() ; i++){
                            JSONObject jsonObject = new JSONObject((LinkedTreeMap) quizzes.get(i));
                            Quiz quiz = BuildObjects.buildQuiz(jsonObject);
                            quizList.add(quiz);
                        }
                        callBack.showMyQuizzes(quizList);
                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }
                else if(response.code() == 500){
                    Log.d("response ","code => "+response.code());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });
    }


    public void postMyQuiz(String header, Quiz quiz, @Nullable final UiInterface callBack){
        Call<Object> call = retrofitInterface.executeCreateQuiz(header, quiz);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 201){
                    try{
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());//the posted quiz is returned from the server
//                        showDataTextView.setText("ekhaaneeeee");
                        if(jsonObject != null) {
                            Quiz quizObject = BuildObjects.buildQuiz(jsonObject);//the posted quiz is converted into quiz object
//                            showDataTextView.setText(quizObject.getResponses().get(0).getMessage()+"\n"+quizObject.get_id());//just showing the first response
                            callBack.createQuiz(quizObject);
                        }

                        //show whatever you want,you have the quizObject
                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }
                else if(response.code() == 400){
                    Log.d("response ","code => "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });
    }

    public void deleteQuiz(String header, String id){
        Call<Void> call = retrofitInterface.executeDeleteQuiz(header, id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    try{
                        Log.d("delete Q ","deleted quiz successfully");
                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }
                else if(response.code() == 500){
                    Log.d("response ","code => "+response.code());
                }
                else if(response.code() == 404){
                    Log.d("response ","code => "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });
    }

    public void showRecentQuizzes(String header, HashMap<String, String> hashMap, int skip, int limit, @Nullable final UiInterface callBack){

        Call<Object> call;
        if(hashMap.containsKey("title")) {
            if(hashMap.containsKey("tag")) {//tag title both ase
                String title = hashMap.get("title");
                String tag = hashMap.get("tag");
                call = retrofitInterface.executeRecentQuiz(header, title, tag, skip, limit);
            }
            else{
                String title = hashMap.get("title");
                call = retrofitInterface.executeRecentQuizByTitle(header, title, skip, limit);
            }
        }
        else if(hashMap.containsKey("tag")){
            String tag = hashMap.get("tag");
            call = retrofitInterface.executeRecentQuizByTag(header, tag, skip, limit);
        }
        else{
            call = retrofitInterface.executeRecentQuizSimple(header, skip, limit);
        }
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    try{

                        List<Object> quizzes = (ArrayList<Object>) response.body();//all the JSONObect quizzes are her
                        List<Quiz> quizList = new ArrayList<>();//will derive quizList from the quizzes
                        //building quiz list,
                        Log.d("top feed => ", "before "+quizzes.size());
                        for(int i = 0 ; i < quizzes.size() ; i++){
                            JSONObject jsonObject = new JSONObject((LinkedTreeMap) quizzes.get(i));
                            Quiz quiz = BuildObjects.buildQuizExtra(jsonObject);
                            quizList.add(quiz);
                        }
                        Log.d("top feed => ", "after "+quizList.size());
                        callBack.showTopFeedQuizzes(quizList);
                        Log.d("top feed => ", "at last "+quizList.size());

                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }
                else if(response.code() == 500){
                    Log.d("response ","code => "+response.code());
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });

    }

    public void editMyQuiz(String header, Quiz quiz, @Nullable final UiInterface callBack){
        Call<Object> call = retrofitInterface.executeEditQuiz(header, quiz.get_id(), quiz);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    try{
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());//the posted quiz is returned from the server
                        if(jsonObject != null) {
                            Quiz quizObject = BuildObjects.buildQuiz(jsonObject);//the posted quiz is converted into quiz object
                            callBack.createQuiz(quizObject);
//                            showDataTextView.setText(quizObject.getResponses().get(0).getMessage());//just showing the first response
                        }

                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }
                else if(response.code() == 400){
                    Log.d("response ","code => "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });

    }

    public void getQuestionsForAQuiz(String header, String quizId, HashMap<String, String> hashMap, @Nullable final UiInterface callBack){

        Call<Object> call;

        if(hashMap.containsKey("pwd")) {
            call = retrofitInterface.executeQuizQuestionPrivate("Bearer "+header, quizId, hashMap.get("pwd"));
        }
        else{
            call = retrofitInterface.executeQuizQuestionPublic("Bearer "+header, quizId);
        }

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    try{
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());//the posted quiz is returned from the server
                        if(jsonObject != null) {
                            Quiz quizObject = BuildObjects.buildQuizForQuestions(jsonObject);//the posted quiz is converted into quiz object
                            callBack.getQuestionsFromQuiz(quizObject);


//                            showDataTextView.setText(quizObject.getResponses().get(0).getMessage());//just showing the first response
                        }

                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }
                else if(response.code() == 400){
                    Log.d("response ","code => sth unknown went wrong "+response.code());
                }
                else if(response.code() == 401){
                    Log.d("response ","code => incorrect password "+response.code());
                }
                else if(response.code() == 404){
                    Log.d("response ","code => the id was not found "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });

    }

    public void getAnswerScript(String header, String _id, AnswerScript answerScript, @Nullable final UiInterface callBack){

        Call<Object> call = retrofitInterface.executeSubmitAnswer("Bearer "+header, _id,
                answerScript);


        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 201){
                    try{
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());//the posted quiz is returned from the server
                        if(jsonObject != null) {
                            AnswerScript answerScript1 = BuildObjects.buildAnswerScript(jsonObject);//the posted quiz is converted into quiz object
                            callBack.getAnswerScript(answerScript1);


//                            showDataTextView.setText(quizObject.getResponses().get(0).getMessage());//just showing the first response
                        }

                    }catch (Exception e){
                        Log.d("exception ",e.getMessage());
                    }
                }
                else if(response.code() == 500){
                    Log.d("response ","code => sth unknown went wrong "+response.code());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("failure ",t.toString());
            }
        });

    }


}
