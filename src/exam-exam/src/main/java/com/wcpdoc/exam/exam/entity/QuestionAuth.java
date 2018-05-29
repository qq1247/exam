package com.wcpdoc.exam.exam.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.wcpdoc.exam.core.util.DateUtil;

/**
 * 试题授权实体
 * 
 * v1.0 zhanghc 2018年5月29日下午3:20:16
 */
@Entity
@Table(name = "ZE_QUESTION_AUTH")
public class QuestionAuth {
	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "identity")
	@Column(name = "ID")
	private Integer id;
	@Column(name = "QUESTION_TYPE_ID")
	private Integer questionTypeId;
	@Column(name = "TYPE")
	private Integer type;
	@Column(name = "USER_IDS")
	private String userIds;
	@Column(name = "ORG_IDS")
	private String orgIds;
	@Column(name = "POST_IDS")
	private String postIds;
	@Column(name = "UPDATE_USER_ID")
	private Integer updateUserId;
	@Column(name = "UPDATE_TIME")
	@DateTimeFormat(pattern = DateUtil.FORMAT_DATE_TIME)
	private Date updateTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuestionTypeId() {
		return questionTypeId;
	}
	public void setQuestionTypeId(Integer questionTypeId) {
		this.questionTypeId = questionTypeId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getOrgIds() {
		return orgIds;
	}
	public void setOrgIds(String orgIds) {
		this.orgIds = orgIds;
	}
	public String getPostIds() {
		return postIds;
	}
	public void setPostIds(String postIds) {
		this.postIds = postIds;
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
}
