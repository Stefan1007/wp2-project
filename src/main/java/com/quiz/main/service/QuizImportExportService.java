package com.quiz.main.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.main.model.Question;
import com.quiz.main.model.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Service for handling import and export of quizzes in JSON format
 */
@Service
public class QuizImportExportService {

    @Autowired
    private QuizService quizService;
    
    @Autowired
    private QuestionService questionService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Import quizzes from a JSON file
     * @param file The JSON file containing quizzes to import
     * @return Number of quizzes imported
     * @throws IOException If there's an issue reading the file
     */
    public int importQuizzes(MultipartFile file) throws IOException {
        // Parse the JSON file
        List<Map<String, Object>> quizDataList = objectMapper.readValue(
                file.getInputStream(), 
                new TypeReference<List<Map<String, Object>>>() {});
        
        int importedCount = 0;
        
        for (Map<String, Object> quizData : quizDataList) {
            try {
                // Create a new quiz from the data
                Quiz quiz = new Quiz();
                quiz.setTitle((String) quizData.get("title"));
                quiz.setDuration(((Number) quizData.get("duration")).intValue());
                
                // Save the quiz to get an ID
                Quiz savedQuiz = quizService.saveQuiz(quiz);
                
                // Process questions
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> questionsData = (List<Map<String, Object>>) quizData.get("questions");
                
                for (Map<String, Object> questionData : questionsData) {
                    Question question = new Question();
                    question.setText((String) questionData.get("text"));
                    question.setOptionA((String) questionData.get("optionA"));
                    question.setOptionB((String) questionData.get("optionB"));
                    question.setOptionC((String) questionData.get("optionC"));
                    question.setCorrectAnswer(((String) questionData.get("correctAnswer")).charAt(0));
                    question.setQuiz(savedQuiz);
                    
                    questionService.saveQuestion(question);
                }
                
                importedCount++;
            } catch (Exception e) {
                // Log error and continue with next quiz
                System.err.println("Error importing quiz: " + e.getMessage());
            }
        }
        
        return importedCount;
    }
    
    /**
     * Export all quizzes as JSON
     * @return A JSON string containing all quizzes and their questions
     * @throws IOException If there's an issue creating the JSON
     */
    public String exportAllQuizzes() throws IOException {
        List<Quiz> quizzes = quizService.getAllQuizzes();
        List<Map<String, Object>> exportData = new ArrayList<>();
        
        for (Quiz quiz : quizzes) {
            Map<String, Object> quizData = new HashMap<>();
            quizData.put("title", quiz.getTitle());
            quizData.put("duration", quiz.getDuration());
            
            List<Map<String, Object>> questionsData = new ArrayList<>();
            for (Question question : quiz.getQuestions()) {
                Map<String, Object> questionData = new HashMap<>();
                questionData.put("text", question.getText());
                questionData.put("optionA", question.getOptionA());
                questionData.put("optionB", question.getOptionB());
                questionData.put("optionC", question.getOptionC());
                questionData.put("correctAnswer", String.valueOf(question.getCorrectAnswer()));
                
                questionsData.add(questionData);
            }
            
            quizData.put("questions", questionsData);
            exportData.add(quizData);
        }
        
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(exportData);
    }
    
    /**
     * Export a specific quiz as JSON
     * @param quizId The ID of the quiz to export
     * @return A JSON string containing the quiz and its questions
     * @throws IOException If there's an issue creating the JSON
     */
    public String exportQuiz(Long quizId) throws IOException {
        Quiz quiz = quizService.getQuizById(quizId);
        
        if (quiz == null) {
            throw new IllegalArgumentException("Quiz not found");
        }
        
        Map<String, Object> quizData = new HashMap<>();
        quizData.put("title", quiz.getTitle());
        quizData.put("duration", quiz.getDuration());
        
        List<Map<String, Object>> questionsData = new ArrayList<>();
        for (Question question : quiz.getQuestions()) {
            Map<String, Object> questionData = new HashMap<>();
            questionData.put("text", question.getText());
            questionData.put("optionA", question.getOptionA());
            questionData.put("optionB", question.getOptionB());
            questionData.put("optionC", question.getOptionC());
            questionData.put("correctAnswer", String.valueOf(question.getCorrectAnswer()));
            
            questionsData.add(questionData);
        }
        
        quizData.put("questions", questionsData);
        
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Collections.singletonList(quizData));
    }
} 