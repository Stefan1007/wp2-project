package com.quiz.main.controller;

import com.quiz.main.model.QuizResult;
import com.quiz.main.model.User;
import com.quiz.main.model.UserDto;
import com.quiz.main.service.QuizResultService;
import com.quiz.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

/