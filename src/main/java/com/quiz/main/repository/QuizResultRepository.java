package com.quiz.main.repository;

import com.quiz.main.model.QuizResult;
import com.quiz.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    /**
     * Find all quiz results for a specific user ordered by most recent first
     * 
     * @param user The user to find results for
     * @return List of quiz results in descending order by ID
     */
    List<QuizResult> findByUserOrderByIdDesc(User user);
}
