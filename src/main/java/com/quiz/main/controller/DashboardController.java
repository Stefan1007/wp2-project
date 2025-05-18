package com.quiz.main.controller;

import com.quiz.main.model.Quiz;
import com.quiz.main.model.QuizResult;
import com.quiz.main.model.User;
import com.quiz.main.service.QuizResultService;
import com.quiz.main.service.QuizService;
import com.quiz.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.OptionalDouble;

@Controller
public class DashboardController {

    @Autowired
    private QuizService quizService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private QuizResultService quizResultService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        // Get current user from session
        User currentUser = userService.getCurrentUser();
        
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        // Get latest quizzes
        List<Quiz> latestQuizzes = quizService.getAllQuizzes();
        model.addAttribute("latestQuizzes", latestQuizzes);
        
        // Get user's quiz results
        List<QuizResult> userResults = quizResultService.getResultsByUser(currentUser);
        model.addAttribute("recentResults", userResults);
        
        // Calculate statistics
        int quizzesTaken = userResults.size();
        model.addAttribute("quizzesTaken", quizzesTaken);
        
        if (quizzesTaken > 0) {
            // Calculate average score
            OptionalDouble avgScore = userResults.stream()
                    .mapToDouble(result -> {
                        Quiz quiz = result.getQuiz();
                        int totalQuestions = quiz.getQuestions().size();
                        return totalQuestions > 0 ? (double) result.getScore() / totalQuestions * 100 : 0;
                    })
                    .average();
            
            model.addAttribute("averageScore", String.format("%.1f%%", avgScore.orElse(0)));
            
            // Calculate highest score
            double highestScore = userResults.stream()
                    .mapToDouble(result -> {
                        Quiz quiz = result.getQuiz();
                        int totalQuestions = quiz.getQuestions().size();
                        return totalQuestions > 0 ? (double) result.getScore() / totalQuestions * 100 : 0;
                    })
                    .max()
                    .orElse(0);
            
            model.addAttribute("highestScore", String.format("%.1f%%", highestScore));
        }
        
        return "dashboard";
    }
} 