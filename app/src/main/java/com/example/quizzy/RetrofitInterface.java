package com.example.quizzy;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @POST("/users")
    Call<Object> executeInfo (@Body HashMap<String,String> hashMap);

    @POST("/users/login")
    Call<Object> executeLogin (@Body HashMap<String,String> hashMap);

    @POST("/users/logout")
    Call<Void> executeLogout (@Header("Authorization") String header);

    @POST("/users/logoutAll")
    Call<Void> executeLogoutAll (@Header("Authorization") String header);

    @GET("/users/me")
    Call<Object> executeHomeNavigation (@Header("Authorization") String header);

    @DELETE("/users/me")
    Call<Object> executeDeleteMyAccount (@Header("Authorization") String header);

    @POST("/quizzes")
    Call<Object> executeCreateQuiz (@Header("Authorization") String header, @Body Quiz quiz);

    @GET("/quizzes/me")
    Call<Object> executeGetMyQuiz(@Header("Authorization") String header);

    @GET("/quizzes")
    Call<Object> executeRecentQuiz(@Header("Authorization") String header,@Query("title") String title,@Query("tag") String tag,
                                  @Query("skip") int skip,@Query("limit") int limit);

    @GET("/quizzes")
    Call<Object> executeRecentQuizByTag(@Header("Authorization") String header, @Query("tag") String tag,
                                   @Query("skip") int skip,@Query("limit") int limit);

    @GET("/quizzes")
    Call<Object> executeRecentQuizByTitle(@Header("Authorization") String header, @Query("title") String title,
                                   @Query("skip") int skip,@Query("limit") int limit);

    @GET("/quizzes")
    Call<Object> executeRecentQuizSimple(@Header("Authorization") String header,
                                   @Query("skip") int skip,@Query("limit") int limit);

    @PATCH("/quizzes/{id}")
    Call<Object> executeEditQuiz(@Header("Authorization") String header, @Path("id") String id, @Body Quiz quiz);

    @DELETE("/quizzes/{id}")
    Call<Void> executeDeleteQuiz(@Header("Authorization") String header, @Path("id") String id);

    @PATCH("/users/me")
    Call<Object> executeEditMyAccount (@Header("Authorization") String header, @Body HashMap<String,String> hashMap);

    @GET("/quizzes/{id}")
    Call<Object> executeQuizQuestionPrivate(@Header("Authorization") String header, @Path("id") String id,
                                            @Query("pwd") String password);

    @GET("/quizzes/{id}")
    Call<Object> executeQuizQuestionPublic(@Header("Authorization") String header, @Path("id") String id);

    @POST("/quizzes/{id}")
    Call<Object> executeSubmitAnswer(@Header("Authorization") String header, @Path("id") String id,
                                     @Body AnswerScript answerScript);

}
