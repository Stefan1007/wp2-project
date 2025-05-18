package com.quiz.main.service;

import com.quiz.main.model.Question;
import com.quiz.main.model.Quiz;
import com.quiz.main.model.QuizResult;
import com.quiz.main.model.User;
import com.quiz.main.repository.QuestionRepository;
import com.quiz.main.repository.QuizRepository;
import com.quiz.main.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuizService {

	@Autowired
	private QuizRepository quizRepository;
	@Autowired
	private QuizResultRepository quizResultRepository;

	@Autowired
	private QuestionRepository questionRepository; // Add this

	public Quiz saveQuiz(Quiz quiz) {
		return quizRepository.save(quiz);
	}

	public List<Quiz> getAllQuizzes() {
		return quizRepository.findAll();
	}

	public Quiz getQuizById(Long id) {
		return quizRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Quiz not found"));
	}

	public void deleteQuiz(Long id) {
		quizRepository.deleteById(id);
	}

	// Add this method
	public Map<Long, Character> getCorrectAnswers(Long quizId) {
		List<Question> questions = questionRepository.findByQuizId(quizId);
		Map<Long, Character> correctAnswers = new HashMap<>();
		for (Question question : questions) {
			correctAnswers.put(question.getId(), question.getCorrectAnswer());
			// Debugging: Print each correct answer
			System.out.println("Question ID: " + question.getId() + ", Correct Answer: " + question.getCorrectAnswer());
		}
		return correctAnswers;
	}
	public void saveQuizResult(User user, Quiz quiz, int score) {
		QuizResult quizResult = new QuizResult();
		quizResult.setUser(user);
		quizResult.setQuiz(quiz);
		quizResult.setScore(score);
		quizResultRepository.save(quizResult);
	}
	// Additional methods as needed
}
