package com.quiz.main.model;

import com.quiz.main.model.Quiz;
import com.quiz.main.model.User;

import javax.persistence.*;

@Entity
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Quiz quiz;

    private int score;

    public QuizResult() {

    }

    // Constructors, Getters, and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public QuizResult(Long id, User user, Quiz quiz, int score) {
        this.id = id;
        this.user = user;
        this.quiz = quiz;
        this.score = score;
    }
}
