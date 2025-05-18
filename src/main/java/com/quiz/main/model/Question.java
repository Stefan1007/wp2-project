package com.quiz.main.model;

import javax.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String text;
	private String optionA;
	private String optionB;
	private String optionC;
	private char correctAnswer; // Assuming 'A', 'B', or 'C'

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "quiz_id")
	private Quiz quiz;

	// Standard getters and setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getOptionA() {
		return optionA;
	}

	public void setOptionA(String optionA) {
		this.optionA = optionA;
	}

	public String getOptionB() {
		return optionB;
	}

	public void setOptionB(String optionB) {
		this.optionB = optionB;
	}

	public String getOptionC() {
		return optionC;
	}

	public void setOptionC(String optionC) {
		this.optionC = optionC;
	}

	public char getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(char correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
}
