package com.wcpdoc.exam.core.entity;

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
 * 考试实体
 * 
 * v1.0 zhanghc 2017-06-11 09:13:23
 */
@Entity
@Table(name = "EXM_EXAM")
public class Exam {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "NAME")
	private String name;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "START_TIME")
	private Date startTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "END_TIME")
	private Date endTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "MARK_START_TIME")
	private Date markStartTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "MARK_END_TIME")
	private Date markEndTime;
	@Column(name = "SCORE_STATE")
	private Integer scoreState;
	@Column(name = "RANK_STATE")
	private Integer rankState;
	@Column(name = "LOGIN_TYPE")
	private Integer loginType;
	private String scoreERemark;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "UPDATE_USER_ID")
	private Integer updateUserId;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@Column(name = "UPDATE_TIME")
	private Date updateTime;
	@Column(name = "STATE")
	private Integer state;
	@Column(name = "PAPER_ID")
	private Integer paperId;
	@Column(name = "EXAM_TYPE_ID")
	private Integer examTypeId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getMarkStartTime() {
		return markStartTime;
	}
	public void setMarkStartTime(Date markStartTime) {
		this.markStartTime = markStartTime;
	}
	public Date getMarkEndTime() {
		return markEndTime;
	}
	public void setMarkEndTime(Date markEndTime) {
		this.markEndTime = markEndTime;
	}
	public Integer getScoreState() {
		return scoreState;
	}
	public void setScoreState(Integer scoreState) {
		this.scoreState = scoreState;
	}
	public Integer getRankState() {
		return rankState;
	}
	public void setRankState(Integer rankState) {
		this.rankState = rankState;
	}
	public Integer getLoginType() {
		return loginType;
	}
	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}
	public String getScoreERemark() {
		return scoreERemark;
	}
	public void setScoreERemark(String scoreERemark) {
		this.scoreERemark = scoreERemark;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getPaperId() {
		return paperId;
	}
	public void setPaperId(Integer paperId) {
		this.paperId = paperId;
	}
	public Integer getExamTypeId() {
		return examTypeId;
	}
	public void setExamTypeId(Integer examTypeId) {
		this.examTypeId = examTypeId;
	}
}
