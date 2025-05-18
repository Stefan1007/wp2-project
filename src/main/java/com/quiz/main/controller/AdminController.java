package com.quiz.main.controller;

import com.quiz.main.model.Quiz;
import com.quiz.main.repository.QuestionRepository;
import com.quiz.main.repository.QuizRepository;
import com.quiz.main.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private QuizRepository quizRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping({"", "/"})
    public String adminHome(Model model) {
      
    }
} 