package com.wcpdoc.exam.core.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 我的考试实体
 * 
 * v1.0 zhanghc 2017-06-19 16:28:29
 */
@Entity
@Table(name = "EXM_MY_EXAM")
public class MyExam {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "EXAM_ID")
	private Integer examId;
	@Column(name = "USER_ID")
	private Integer userId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "ANSWER_TIME")
	private Date answerTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "ANSWER_FINISH_TIME")
	private Date answerFinishTime;
	@Column(name = "MARK_USER_ID")
	private Integer myMarkId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "MARK_TIME")
	private Date markTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "MARK_FINISH_TIME")
	private Date markFinishTime;
	@Column(name = "TOTAL_SCORE")
	private BigDecimal totalScore;
	@Column(name = "STATE")
	private Integer state;
	@Column(name = "MARK_STATE")
	private Integer markState;
	@Column(name = "ANSWER_STATE")
	private Integer answerState;
	@Column(name = "UPDATE_USER_ID")
	private Integer updateUserId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExamId() {
		return examId;
	}

	public void setExamId(Integer examId) {
		this.examId = examId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	public Integer getMyMarkId() {
		return myMarkId;
	}

	public void setMyMarkId(Integer myMarkId) {
		this.myMarkId = myMarkId;
	}

	public BigDecimal getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(BigDecimal totalScore) {
		this.totalScore = totalScore;
	}

	/** 1：未考试；2：考试中；3：已交卷；4：强制交卷； */
	public Integer getState() {
		return state;
	}

	/** 1：未考试；2：考试中；3：已交卷；4：强制交卷； */
	public void setState(Integer state) {
		this.state = state;
	}

	/** 1：未阅卷；2：阅卷中；3：已阅卷； */
	public Integer getMarkState() {
		return markState;
	}

	/** 1：未阅卷；2：阅卷中；3：已阅卷； */
	public void setMarkState(Integer markState) {
		this.markState = markState;
	}

	/** 1：及格；2：不及格 */
	public Integer getAnswerState() {
		return answerState;
	}

	/** 1：及格；2：不及格 */
	public void setAnswerState(Integer answerState) {
		this.answerState = answerState;
	}

	public Integer getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getAnswerFinishTime() {
		return answerFinishTime;
	}

	public void setAnswerFinishTime(Date answerFinishTime) {
		this.answerFinishTime = answerFinishTime;
	}

	public Date getMarkTime() {
		return markTime;
	}

	public void setMarkTime(Date markTime) {
		this.markTime = markTime;
	}

	public Date getMarkFinishTime() {
		return markFinishTime;
	}

	public void setMarkFinishTime(Date markFinishTime) {
		this.markFinishTime = markFinishTime;
	}
}
