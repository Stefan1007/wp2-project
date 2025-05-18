package com.quiz.main.model;

import java.util.Map;

public class QuizSubmission {
    private Long quizId;
    private Map<Long, Character> answers; // Question ID -> Answer ('A', 'B', 'C')

    public Long getQuizId() {
        return quizId;
    }

    public void setQuizId(Long quizId) {
        this.quizId = quizId;
    }

    public Map<Long, Character> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, Character> answers) {
        this.answers = answers;
    }
// Getters and setters
}
