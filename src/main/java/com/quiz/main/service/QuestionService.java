package com.quiz.main.service;

import com.quiz.main.model.Question;
import com.quiz.main.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public Question updateQuestion(Long id, Question questionDetails) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + id));

        question.setText(questionDetails.getText());
        question.setOptionA(questionDetails.getOptionA());
        question.setOptionB(questionDetails.getOptionB());
        question.setOptionC(questionDetails.getOptionC());
        question.setCorrectAnswer(questionDetails.getCorrectAnswer());

        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
