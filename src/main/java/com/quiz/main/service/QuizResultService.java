package com.quiz.main.service;

import com.quiz.main.model.QuizResult;
import com.quiz.main.model.User;
import com.quiz.main.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizResultService {

    @Autowired
    private QuizResultRepository quizResultRepository;

    /**
     * Get all quiz results for a specific user
     * 
     * @param user The user to find results for
     * @return List of quiz results
     */
    public List<QuizResult> getResultsByUser(User user) {
        return quizResultRepository.findByUserOrderByIdDesc(user);
    }
    
    /**
     * Save a quiz result
     * 
     * @param quizResult The quiz result to save
     * @return The saved quiz result
     */
    public QuizResult saveResult(QuizResult quizResult) {
        return quizResultRepository.save(quizResult);
    }
} 