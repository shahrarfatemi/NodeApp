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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, UiInterface {

    Retrofit retrofit;
    RetrofitInterface retrofitInterface;

    DatabaseHelper databaseHelper;
    NetworkUtil networkUtil;

    public static int surName = 1;
    String BaseUrl = "https://contest-quiz-app.herokuapp.com/";
    String header;
    String _id = "5f5e0fbd230c7b0017f4aa51";
    String userId;
    Button logoutButton,deleteAccountButton,editAccountButton,allDeviceLogoutButton,createQuizButton,deleteQuizButton,seeMyQuizButton,
            editQuizButton,showNewsFeedButton,showRecentQuizButton;
    EditText enterPasswordText,quizTitleText,quizDurationText,quizPasswordText,quizTimeText,quizDateText;
    TextView showDataTextView;
    UserInfo userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        userInfo = (UserInfo) intent.getSerializableExtra("MyInfo");
        header = userInfo.getToken();
        userId = userInfo.get_id();
//        requestProfile(header);

        init();
    }


    public void init(){

        retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        networkUtil = NetworkUtil.getInstance();

        databaseHelper = new DatabaseHelper(this);

        logoutButton = (Button) findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);
        deleteAccountButton = (Button) findViewById(R.id.accountDeleteButton);
        deleteAccountButton.setOnClickListener(this);
        editAccountButton = (Button) findViewById(R.id.editAccountButton);
        editAccountButton.setOnClickListener(this);
        allDeviceLogoutButton = (Button) findViewById(R.id.allDeviceLogoutButton);
        allDeviceLogoutButton.setOnClickListener(this);
        createQuizButton = (Button) findViewById(R.id.quizCreateButton);
        createQuizButton.setOnClickListener(this);
        deleteQuizButton = (Button) findViewById(R.id.quizDeleteButton);
        deleteQuizButton.setOnClickListener(this);
        seeMyQuizButton = (Button) findViewById(R.id.showMyQuizButton);
        seeMyQuizButton.setOnClickListener(this);
        editQuizButton = (Button) findViewById(R.id.editQuizButton);
        editQuizButton.setOnClickListener(this);
        showNewsFeedButton = (Button) findViewById(R.id.showQuizButton);
        showNewsFeedButton.setOnClickListener(this);
        showRecentQuizButton = (Button) findViewById(R.id.showRecentQuizButton);
        showRecentQuizButton.setOnClickListener(this);

        enterPasswordText = (EditText) findViewById(R.id.enterPassWord);
        quizTitleText = (EditText) findViewById(R.id.quizTitleText);
        quizDurationText = (EditText) findViewById(R.id.quizDurationText);
        quizPasswordText = (EditText) findViewById(R.id.quizPassText);
        quizTimeText = (EditText) findViewById(R.id.quizStartTimeText);
        quizDateText = (EditText) findViewById(R.id.quizStartDateText);

        showDataTextView = (TextView) findViewById(R.id.showDataText);

    }
    public long dateToTimeStamp(String date, String time) throws Exception{
        String myDate = date+" "+time;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date myDate2 = sdf.parse(myDate);
        long millis = myDate2.getTime() - 6*3600*1000;
        return millis;
    }

    public void logOut(String header){
        Call<Void> call = retrofitInterface.executeLogout("Bearer "+header);
        Toast.makeText(HomeActivity.this,"inside this method",Toast.LENGTH_LONG).show();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    try {
                        Toast.makeText(HomeActivity.this, "logged out successfully ", Toast.LENGTH_LONG).show();
                        removeInfo();
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this,"exception "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }else if(response.code() == 400){
                    Toast.makeText(HomeActivity.this, "wrong info", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == logoutButton){
            Toast.makeText(HomeActivity.this,"logout pressed \n"+header,Toast.LENGTH_LONG).show();
            networkUtil.logOut(header, this);
        }
        else if(v == deleteAccountButton) {
            networkUtil.deleteMyAccount(header, this);
            //after deletion back to login page

        }
        else if(v == allDeviceLogoutButton){
            networkUtil.logoutAllDevice(header, this);
        }
        else if(v == editAccountButton){
            String name = userInfo.getName() + surName++;
            String password = enterPasswordText.getText().toString();
            networkUtil.editMyAccount(header, name, password, this);
        }
        else if(v == seeMyQuizButton){
            networkUtil.showMyQuiz(header, this);

        }
        else if(v == createQuizButton) {
            Quiz quiz = buildDemoQuiz();//this was made to create a demo quiz for checking; in app, it will be done by user from GUI
            networkUtil.postMyQuiz(header, quiz, this);

        }
        else if(v == deleteQuizButton){
            //in real get _id from saved instance of the user
            networkUtil.deleteQuiz(header, _id);
        }
        else if(v == showRecentQuizButton){
            //ekhane ekta bepar ase multiple data wise multiple parameter query te pathano jete pare
            //for example keu shudhu tag dile, arekta method banay dibo,shudhu title dile arekta, r kichu na dile arekta
            //otherwise showRecentQuizzes kei ektu modify kore disi jaate jekono vabei use kora jay
            //accha it's done accordingly
            //now change the following codes according to GUI's necessity
            //like tag na thakle don't put any tag..ei r ki

            /* NOTE : please handle the title in the query parameter with care and
             consider url encoding to encode whitespaces and special characters
             */

            HashMap<String, String> hashMap = new HashMap();
            hashMap.put("title", "Engineering");
            hashMap.put("tag", "General");
            int skip = 0;
            int limit = 2;
            networkUtil.showRecentQuizzes(header, hashMap, skip, limit, this);
        }
        else if(v == editQuizButton){
            Quiz quiz = buildDemoQuiz();
            networkUtil.editMyQuiz(header, quiz, this);
        }
    }

    private Quiz buildDemoQuiz(){
        Quiz quiz = new Quiz();
        try {
            String title = quizTitleText.getText().toString();
            String duration = quizDurationText.getText().toString();
            Double quizDuration = Double.parseDouble(duration);
            String quizpass = quizPasswordText.getText().toString();
            String quizStartDate = quizDateText.getText().toString();
            String quizStartTime = quizTimeText.getText().toString();
            long millis = (dateToTimeStamp(quizStartDate , quizStartTime))/1000;
            List<String> options = new ArrayList<>();
            List<String> answers = new ArrayList<>();
            answers.add("3.14159");
            options.add("3.14159");
            options.add("3.2345678");
            options.add("147895");
            Question question = new Question("What is the value of Pi?", answers, options, "SINGLE");
            question.setMarks(1);
            List<String> tags = new ArrayList<>();
            tags.add("Electric");
            tags.add("General Knowledge");
            List<Question> questions = new ArrayList<>();
            questions.add(question);
            List<com.example.quizzy.Response> responses = new ArrayList<>();
            responses.add(new com.example.quizzy.Response(20,10,"Khubi Kharap"));
            responses.add(new com.example.quizzy.Response(100,90,"Mama, kopay diso"));
            quiz.setTitle(title);quiz.setPassword(quizpass);quiz.setDuration(quizDuration);quiz.setStartTime(millis);//quiz.set_id(_id);
            quiz.setQuestions(questions);quiz.setTags(tags);quiz.setResponses(responses);
//                Toast.makeText(HomeActivity.this,quiz.toString(),Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(HomeActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        return quiz;
    }
    public void editMyQuiz(String header, Quiz quiz){
        Call<Object> call = retrofitInterface.executeEditQuiz(header, _id, quiz);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    try{
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());//the posted quiz is returned from the server
                        if(jsonObject != null) {
                            Quiz quizObject = BuildObjects.buildQuiz(jsonObject);//the posted quiz is converted into quiz object
                            showDataTextView.setText(quizObject.getResponses().get(0).getMessage());//just showing the first response
                        }
                        //show whatever you want,you have the quizObject
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this,"exception "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else if(response.code() == 400){
                    Toast.makeText(HomeActivity.this, "could not edit", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showRecentQuizzes(String header){
        Call<Object> call = retrofitInterface.executeRecentQuiz(header, "Engineering", "General", 0,2);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    try{

                        List<Object> quizzes = (ArrayList<Object>) response.body();//all the JSONObect quizzes are her
                        List<Quiz> quizList = new ArrayList<>();//will derive quizList from the quizzes
                        //building quiz list,
                        for(int i = 0 ; i < quizzes.size() ; i++){
                            JSONObject jsonObject = new JSONObject((LinkedTreeMap) quizzes.get(i));
                            Quiz quiz = BuildObjects.buildQuizExtra(jsonObject);
                            quizList.add(quiz);
                        }
                        //now you have all the quizzes of the user in the quizList arrayList
                        //only showing the first question of the first quiz of the user
                        if(quizList.size() > 0) {
                            showDataTextView.setText(quizList.get(0).getRating() +"\n"+ quizList.get(0).getDifficulty() +"\n" + quizList.get(0).getAccess());
                        }
                        else{
                            showDataTextView.setText("nothing to show");
                        }
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this,"exception "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else if(response.code() == 500){
                    Toast.makeText(HomeActivity.this, "could not get recent quizzes", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void postMyQuiz(String header, Quiz quiz){
        Call<Object> call = retrofitInterface.executeCreateQuiz(header, quiz);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 201){
                    try{
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());//the posted quiz is returned from the server
                        showDataTextView.setText("ekhaaneeeee");
                        if(jsonObject != null) {
                            Quiz quizObject = BuildObjects.buildQuiz(jsonObject);//the posted quiz is converted into quiz object
                            showDataTextView.setText(quizObject.getResponses().get(0).getMessage()+"\n"+quizObject.get_id());//just showing the first response
                        }
                        //show whatever you want,you have the quizObject
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this,"exception "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else if(response.code() == 400){
                    Toast.makeText(HomeActivity.this, "could not post", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean showMyQuiz(String header){
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
                        //now you have all the quizzes of the user in the quizList arrayList
                        //only showing the first question of the first quiz of the user
                        if(quizList.size() > 0) {
                            showDataTextView.setText(quizList.get(0).getQuestions().get(0).getDescription());
                        }
                        else{
                            showDataTextView.setText("nothing to show");
                        }
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this,"exception "+ e.toString()+" => "+call.isExecuted(), Toast.LENGTH_LONG).show();
                    }
                }
                else if(response.code() == 500){
                    Toast.makeText(HomeActivity.this, "could not post", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        return call.isExecuted();
    }


    public void deleteQuiz(String header, String id){
        Call<Void> call = retrofitInterface.executeDeleteQuiz(header, id);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 200){
                    try{
                        showDataTextView.setText("Deleted Quiz");
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this,"exception "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else if(response.code() == 500){
                    Toast.makeText(HomeActivity.this, "Server error", Toast.LENGTH_LONG).show();
                }
                else if(response.code() == 404){
                    Toast.makeText(HomeActivity.this, "Could not find the quiz", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void editMyAccount(String header, String name, String password){
        HashMap <String, String> hashMap = new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("password",password);
        Call<Object> call = retrofitInterface.executeEditMyAccount("Bearer "+header, hashMap);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    try {
                        //the server sends back the name and email of the updated user,actually it can respond with other info too,
                        // check the doc for that we can get any string in the same way as below so pera naai
                        // new email and name has been assigned to the user and then updated in the sqlite database
                        JSONObject jsonObject = new JSONObject((LinkedTreeMap) response.body());
                        if(jsonObject != null) {
                            String name = jsonObject.getString("name");
                            String email = jsonObject.getString("email");
                            userInfo.setName(name);
                            userInfo.setEmail(email);
                            //Toast.makeText(HomeActivity.this, "edited successfully ", Toast.LENGTH_LONG).show();
                            updateInfo(name, email);
                        }
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this,"exception "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }else if(response.code() == 400){
                    Toast.makeText(HomeActivity.this, "could not edit", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void deleteMyAccount(String header){
        Call<Object> call = retrofitInterface.executeDeleteMyAccount("Bearer "+header);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.code() == 200){
                    try {
                        Toast.makeText(HomeActivity.this, "deleted successfully ", Toast.LENGTH_LONG).show();
                        removeInfo();
                        //after deletion back to login page
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this,"exception "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }else if(response.code() == 500){
                    Toast.makeText(HomeActivity.this, "could not delete", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void logoutAllDevice(String header){
        //format of the header is : header-name "Authorization", header-value "Bearer <token>"
        Call<Void> call = retrofitInterface.executeLogoutAll("Bearer "+header);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //Toast.makeText(MainActivity.this,response.code()., Toast.LENGTH_LONG).show();
                if(response.code() == 200){
                    try {
                        Toast.makeText(HomeActivity.this, "logged out from all devices successfully ", Toast.LENGTH_LONG).show();
                        removeInfo();
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this,"exception "+ e.toString(), Toast.LENGTH_LONG).show();
                    }
                }else if(response.code() == 500){
                    Toast.makeText(HomeActivity.this, "could not logout", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(HomeActivity.this,"failure "+t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void removeInfo(){
        int id = databaseHelper.getIdFromEmail(userInfo.getEmail());
        if(id != -1){
            int isDeleted = databaseHelper.deleteDatabase(id);
            if(isDeleted == 0){
                Toast.makeText(HomeActivity.this,"token could not be deleted \n",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(HomeActivity.this,"token deleted \n"+header,Toast.LENGTH_LONG).show();
            }
        }

    }

    public void updateInfo(String name, String email){
        String token = userInfo.getToken();
        int id = databaseHelper.getIdFromToken(token);
        if(id != -1){
            boolean isUpdated = databaseHelper.updateDatabase(id, name, email, token);
            if(isUpdated){
                Toast.makeText(HomeActivity.this,"account updated \n",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(HomeActivity.this,"could not update \n"+header,Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void showUser(UserInfo userInfo) {
        //do whatever you want here after getting the userInfo
        showDataTextView.setText("email => "+userInfo.getEmail());
    }

    public void deleteUser(UserInfo userInfo){
        showDataTextView.setText("email => "+userInfo.getEmail());
        logOut();//removing info and getting back to login page
    }

    @Override
    public void logOut() {
        //do what you need to do after logging out
        removeInfo();
        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showMyQuizzes(List<Quiz> quizzes) {
        //put on the UI accordingly
        showDataTextView.setText(quizzes.toString());
        for(int i = 0 ; i < quizzes.size() ; i++){
            Log.d("quiz "+(i+1), quizzes.get(i).toString());
        }

    }

    @Override
    public void createQuiz(Quiz quiz) {
        //both the post and edit quiz has the same calling methods so, be alert while doing whatever
        //check docs to understand the response of this,then do whatever you want here with the quiz object
        //this quiz object is important because we need the _id of the quiz for further tasks
        showDataTextView.setText(quiz.toString());
    }

    @Override
    public void showTopFeedQuizzes(List<Quiz> quizzes) {
        //put on the UI accordingly
        //show my quizzes and top quizzes are diff
        showDataTextView.setText("size "+quizzes.size());
        Log.d("showTopFeed ", "size => \n"+quizzes.size());
        for(int i = 0 ; i < quizzes.size() ; i++){
            Log.d("quiz "+(i+1), quizzes.get(i).getStr());
        }
    }

    @Override
    public void getQuestionsFromQuiz(Quiz quiz) {

    }

    @Override
    public void getAnswerScript(AnswerScript answerScript) {
        
    }
}