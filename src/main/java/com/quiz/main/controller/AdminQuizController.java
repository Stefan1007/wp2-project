package com.quiz.main.controller;

import com.quiz.main.model.Question;
import com.quiz.main.model.Quiz;
import com.quiz.main.service.QuestionService;
import com.quiz.main.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminQuizController {
