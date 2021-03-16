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
 * 试卷实体
 * 
 * zhanghc 2018年10月21日上午8:16:06
 */
@Entity
@Table(name = "EXM_PAPER")
public class Paper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "NAME")
	private String name;
	@Column(name = "PREVIEW_TYPE")
	private Integer previewType;
	@Column(name = "PASS_SCORE")
	private BigDecimal passScore;
	@Column(name = "TOTAL_SCORE")
	private BigDecimal totalScore;
	@Column(name = "SCORE_E_REMARK")
	private String scoreERemark;
	@Column(name = "DESCRIPTION")
	private String description;
	@Column(name = "PAPER_TYPE_ID")
	private Integer paperTypeId;
	@Column(name = "STATE")
	private Integer state;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(BigDecimal totalScore) {
		this.totalScore = totalScore;
	}

	public BigDecimal getPassScore() {
		return passScore;
	}

	public void setPassScore(BigDecimal passScore) {
		this.passScore = passScore;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getPaperTypeId() {
		return paperTypeId;
	}

	public void setPaperTypeId(Integer paperTypeId) {
		this.paperTypeId = paperTypeId;
	}

	public Integer getPreviewType() {
		return previewType;
	}

	public void setPreviewType(Integer previewType) {
		this.previewType = previewType;
	}

	/** 0：删除；1：启用；2：禁用 */
	public Integer getState() {
		return state;
	}

	/** 0：删除；1：启用；2：禁用 */
	public void setState(Integer state) {
		this.state = state;
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

	public String getScoreERemark() {
		return scoreERemark;
	}

	public void setScoreERemark(String scoreERemark) {
		this.scoreERemark = scoreERemark;
	}
}