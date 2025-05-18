package com.quiz.main.controller;

import com.quiz.main.model.Question;
import com.quiz.main.model.Quiz;
import com.quiz.main.model.QuizResult;
import com.quiz.main.model.QuizSubmission;
import com.quiz.main.model.User;
import com.quiz.main.service.QuizResultService;
import com.quiz.main.service.QuizService;
import com.quiz.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/quizzes")
public class UserQuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private QuizResultService quizResultService;

    @GetMapping
    public String listQuizzes(Model model) {
        model.addAttribute("quizzes", quizService.getAllQuizzes());
        return "userQuizzesList";
    }

    @GetMapping("/{id}")
    public String takeQuiz(@PathVariable Long id, Model model, HttpSession session) {
        Quiz quiz = quizService.getQuizById(id);
        if (quiz == null) {
            return "redirect:/dashboard";
        }

        // Ensure the user is logged in
        User currentUser = userService.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        // Store the user in the session for later use
        session.setAttribute("currentUser", currentUser);
        
        // Store the start time in the session to calculate the duration on submission
        session.setAttribute("startTime", System.currentTimeMillis());
        session.setAttribute("quizId", id);

        // Pass the quiz and its questions to the view
        model.addAttribute("quiz", quiz);
        model.addAttribute("questions", quiz.getQuestions());
        model.addAttribute("duration", quiz.getDuration());
        model.addAttribute("quizSubmission", new QuizSubmission());

        return "takeQuiz";
    }

    @PostMapping("/{quizId}/submit")
    public String submitQuizAnswers(
            @PathVariable Long quizId, 
            @ModelAttribute QuizSubmission submission, 
            Model model, 
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        // Get current user from session
        User currentUser = (User) session.getAttribute("currentUser");
        
        // If no user in session, try to get from security context
        if (currentUser == null) {
            currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return "redirect:/login";
            }
        }
        
        // Get the quiz
        Quiz quiz = quizService.getQuizById(quizId);
        if (quiz == null) {
            redirectAttributes.addFlashAttribute("error", "Quiz not found");
            return "redirect:/dashboard";
        }
        
        // Set the quiz ID for the submission (if not already set)
        submission.setQuizId(quizId);
        
        // Validate time limit
        Long startTime = (Long) session.getAttribute("startTime");
        Long storedQuizId = (Long) session.getAttribute("quizId");
        
        // Validate quiz ID matches the one in session
        if (storedQuizId == null || !storedQuizId.equals(quizId)) {
            redirectAttributes.addFlashAttribute("error", "Invalid quiz submission");
            return "redirect:/dashboard";
        }
        
        if (startTime != null) {
            long currentTime = System.currentTimeMillis();
            long elapsedTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - startTime);
            
            // Add a small buffer (2 minutes) to account for network delays and processing time
            if (elapsedTimeMinutes > quiz.getDuration() + 2) {
                redirectAttributes.addFlashAttribute("error", "Time limit exceeded. Your submission was not accepted.");
                return "redirect:/quizzes";
            }
        }
        
        // Validate that all questions are answered
        boolean allQuestionsAnswered = true;
        Map<Long, String> unansweredQuestions = new HashMap<>();
        
        if (submission.getAnswers() == null) {
            submission.setAnswers(new HashMap<>());
            allQuestionsAnswered = false;
        } else {
            for (Question question : quiz.getQuestions()) {
                if (!submission.getAnswers().containsKey(question.getId())) {
                    allQuestionsAnswered = false;
                    unansweredQuestions.put(question.getId(), question.getText());
                }
            }
        }
        
        // If not all questions were answered, return to the quiz with warnings
        if (!allQuestionsAnswered) {
            model.addAttribute("quiz", quiz);
            model.addAttribute("questions", quiz.getQuestions());
            model.addAttribute("duration", quiz.getDuration());
            model.addAttribute("quizSubmission", submission);
            model.addAttribute("unansweredQuestions", unansweredQuestions);
            model.addAttribute("warning", "Please answer all questions before submitting.");
            
            // Reset start time to give the user more time
            session.setAttribute("startTime", System.currentTimeMillis());
            
            return "takeQuiz";
        }
        
        // Evaluate the submission to calculate the score
        int score = evaluateSubmission(submission);
        
        // Save the quiz result
        QuizResult quizResult = new QuizResult();
        quizResult.setUser(currentUser);
        quizResult.setQuiz(quiz);
        quizResult.setScore(score);
        quizResultService.saveResult(quizResult);
        
        // Clean up session
        session.removeAttribute("startTime");
        session.removeAttribute("quizId");
        
        // Convert Character answers to String answers for template compatibility
        Map<Long, String> stringAnswers = new HashMap<>();
        if (submission.getAnswers() != null) {
            for (Map.Entry<Long, Character> entry : submission.getAnswers().entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    stringAnswers.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
        }
        
        // Prepare model for results page
        model.addAttribute("quiz", quiz);
        model.addAttribute("score", score);
        model.addAttribute("totalQuestions", quiz.getQuestions().size());
        model.addAttribute("submittedAnswers", stringAnswers);
        
        return "quizResults";
    }

    private int evaluateSubmission(QuizSubmission submission) {
        int correctAnswers = 0;
        Quiz quiz = quizService.getQuizById(submission.getQuizId());
        
        if (quiz == null || submission.getAnswers() == null) {
            return 0;
        }
        
        for (Question question : quiz.getQuestions()) {
            Character submittedAnswer = submission.getAnswers().get(question.getId());
            if (submittedAnswer != null && 
                Character.toUpperCase(submittedAnswer) == Character.toUpperCase(question.getCorrectAnswer())) {
                correctAnswers++;
            }
        }
        
        return correctAnswers;
    }
}
