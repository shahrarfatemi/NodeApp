package com.example.quizzy;

import java.util.List;

public interface UiInterface {

    void showUser(UserInfo userInfo);
    void deleteUser(UserInfo userInfo);
    void logOut();
    void showMyQuizzes(List<Quiz> quizzes);
    void createQuiz(Quiz quiz);
    void showTopFeedQuizzes(List<Quiz> quizzes);
    void getQuestionsFromQuiz(Quiz quiz);
    void getAnswerScript(AnswerScript answerScript);

}

