package com.example.quizzy;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BuildObjects {
    public static Quiz buildQuiz(JSONObject jsonObject){
        Quiz quiz =  new Quiz();
        try {
            String title = jsonObject.getString("title");
            JSONArray jsonTags = jsonObject.getJSONArray("tags");
            double duration = jsonObject.getDouble("duration");
            String _id = jsonObject.getString("_id");
            String password = jsonObject.getString("password");
            String owner = jsonObject.getString("owner");
            String startTime = jsonObject.getString("startTime");
            Date startAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(startTime);
            String created = jsonObject.getString("createdAt");
            Date createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(created);
            String updated = jsonObject.getString("updatedAt");
            Date updatedAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(updated);
            JSONArray jsonQuestions = jsonObject.getJSONArray("questions");
            JSONArray jsonResponses = jsonObject.getJSONArray("responses");
            List<String> tags = new ArrayList<>();
            for(int i = 0 ; i < jsonTags.length() ; i++){
                String tag = jsonTags.getString(i);
                tags.add(tag);
            }
            List<Question> questions = buildQuestions(jsonQuestions);
            List<com.example.quizzy.Response> responses = buildResponses(jsonResponses);
            quiz.setStartAt(startAt);quiz.setCreatedAt(createdAt);quiz.setUpdatedAt(updatedAt);
            quiz.set_id(_id);quiz.setOwner(owner);quiz.setTitle(title);quiz.setPassword(password);quiz.setDuration(duration);
            quiz.setQuestions(questions);quiz.setTags(tags);quiz.setResponses(responses);
        }catch (JSONException e){
//            Toast.makeText(HomeActivity.this,"Json Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }catch (ParseException e){
//            Toast.makeText(HomeActivity.this,"Parse Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return quiz;
    }


    public static UserInfo buildUserInfo(JSONObject jsonObject){
        UserInfo userInfo = new UserInfo();
        try {
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            String _id = jsonObject.getString("_id");
            String created = jsonObject.getString("createdAt");
            Date createdAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(created);
            String updated = jsonObject.getString("updatedAt");
            Date updatedAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(updated);
            userInfo.setName(name);userInfo.setEmail(email);userInfo.set_id(_id);
            userInfo.setCreatedAt(createdAt);userInfo.setUpdatedAt(updatedAt);
        }catch (JSONException e){
//            Toast.makeText(HomeActivity.this,"Json Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }catch (ParseException e){
//            Toast.makeText(HomeActivity.this,"Parse Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return userInfo;
    }

    public static Quiz buildQuizExtra(JSONObject jsonObject){
        Quiz quiz =  new Quiz();
        try {
            String title = jsonObject.getString("title");
            JSONArray jsonTags = jsonObject.getJSONArray("tags");
            double duration = jsonObject.getDouble("duration");
            String _id = jsonObject.getString("_id");
            String owner = jsonObject.getString("owner");
            double rating = jsonObject.getDouble("rating");
            double difficulty = jsonObject.getDouble("difficulty");
            String access = jsonObject.getString("access");
            String startTime = jsonObject.getString("startTime");
            Date startAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(startTime);
            List<String> tags = new ArrayList<>();
            for(int i = 0 ; i < jsonTags.length() ; i++){
                String tag = jsonTags.getString(i);
                tags.add(tag);
            }
            quiz.setStartAt(startAt);
            quiz.setRating(rating);quiz.setDifficulty(difficulty);quiz.setAccess(access);
            quiz.set_id(_id);quiz.setOwner(owner);quiz.setTitle(title);quiz.setDuration(duration);
            quiz.setTags(tags);
        }catch (JSONException e){
//            Toast.makeText(HomeActivity.this,"Json Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }catch (ParseException e){
//            Toast.makeText(HomeActivity.this,"Parse Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return quiz;
    }


    public static Quiz buildQuizForQuestions(JSONObject jsonObject){
        Quiz quiz =  new Quiz();
        try {
            String title = jsonObject.getString("title");
            double duration = jsonObject.getDouble("duration");
            String _id = jsonObject.getString("_id");
            String startTime = jsonObject.getString("startTime");
            Date startAt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(startTime);
            JSONArray jsonQuestions = jsonObject.getJSONArray("questions");
            List<Question> questions = buildQuestions(jsonQuestions);
            quiz.setStartAt(startAt);quiz.set_id(_id);quiz.setTitle(title);quiz.setDuration(duration);
            quiz.setQuestions(questions);
        }catch (JSONException e){
//            Toast.makeText(HomeActivity.this,"Json Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }catch (ParseException e){
//            Toast.makeText(HomeActivity.this,"Parse Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return quiz;
    }

    public static AnswerScript buildAnswerScript(JSONObject jsonObject){
        AnswerScript answerScript = new AnswerScript();
        try {
            JSONArray jsonSubmissions = jsonObject.getJSONArray("submissions");
            List<Submission> submissionList = buildSubmissions(jsonSubmissions);
            String _id = jsonObject.getString("_id");
            String quizId = jsonObject.getString("quizId");
            String userId = jsonObject.getString("userId");
            double marks = jsonObject.getDouble("marks");
            List<String> corrects = (List<String>)jsonObject.get("correct");
            List<String> incorrects = (List<String>)jsonObject.get("incorrect");
            answerScript.setUserSubmissions(submissionList);
            answerScript.set_id(_id);answerScript.setQuizId(quizId);answerScript.setUserId(userId);
            answerScript.setMarks(marks);answerScript.setCorrect(corrects);answerScript.setIncorrect(incorrects);

        }catch (JSONException e) {
//            Toast.makeText(HomeActivity.this,"Json Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return answerScript;
    }


    public static List<Submission> buildSubmissions(JSONArray jsonArray){
        List<Submission> submissions = new ArrayList<>();
        try {
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject submissionObject = (JSONObject)jsonArray.get(i);
                String qId = submissionObject.getString("questionId");
                JSONArray answers = submissionObject.getJSONArray("answers");
                List<String> answerList = new ArrayList<>();
                for(int j = 0 ; j < answers.length() ; j++){
                    String answer = answers.getString(i);
                    answerList.add(answer);
                }
                Submission submission = new Submission(qId, answerList);
                submissions.add(submission);
            }
        }catch (JSONException e){
//            Toast.makeText(HomeActivity.this,"Json Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return submissions;
    }

    public static List<com.example.quizzy.Response> buildResponses(JSONArray jsonArray){
        List<com.example.quizzy.Response> responses = new ArrayList<>();
        try {
            for(int i = 0 ; i < jsonArray.length() ; i++) {
                JSONObject responseObject = (JSONObject) jsonArray.get(i);
                String _id = responseObject.getString("_id");
                String message = responseObject.getString("message");
                double high = responseObject.getDouble("high");
                double low = responseObject.getDouble("low");
                com.example.quizzy.Response response = new com.example.quizzy.Response(high, low, message);
                response.set_id(_id);
                responses.add(response);
            }

        }catch (JSONException e){
//            Toast.makeText(HomeActivity.this,"Json Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return responses;
    }

    public static List<Question> buildQuestions(JSONArray jsonArray){
        List<Question> questions = new ArrayList<>();
        try {
            for(int i = 0 ; i < jsonArray.length() ; i++){
                JSONObject questionObject = (JSONObject)jsonArray.get(i);
                String _id = questionObject.getString("_id");
                String type = questionObject.getString("type");
                String description = questionObject.getString("description");
                double marks = questionObject.getDouble("marks");
                JSONArray options = questionObject.getJSONArray("options");
                List<String> optionList = new ArrayList<>();
                for(int j = 0 ; j < options.length() ; j++){
                    String option = options.getString(i);
                    optionList.add(option);
                }
                JSONArray answers = questionObject.getJSONArray("answers");
                List<String> answerList = new ArrayList<>();
                for(int j = 0 ; j < answers.length() ; j++){
                    String answer = answers.getString(i);
                    answerList.add(answer);
                }
                Question question = new Question(description,answerList,optionList,type);
                question.set_id(_id);question.setMarks(marks);
                questions.add(question);
            }
        }catch (JSONException e){
//            Toast.makeText(HomeActivity.this,"Json Exception =>\n"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return questions;
    }


}
