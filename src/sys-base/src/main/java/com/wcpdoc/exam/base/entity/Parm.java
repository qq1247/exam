package com.wcpdoc.exam.base.entity;

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
 * 参数实体
 * 
 * v1.0 chenyun 2021-03-04 15:02:18
 */
@Entity
@Table(name = "SYS_PARM")
public class Parm {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Integer id;
	@Column(name = "EMAIL_HOST")
	private String emailHost;
	@Column(name = "EMAIL_ENCODE")
	private String emailEncode;
	@Column(name = "EMAIL_USER_NAME")
	private String emailUserName;
	@Column(name = "EMAIL_PWD")
	private String emailPwd;
	@Column(name = "EMAIL_PROTOCOL")
	private String emailProtocol;
	@Column(name = "UPDATE_USER_ID")
	private Integer updateUserId;
	@Column(name = "UPDATE_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmailHost() {
		return emailHost;
	}

	public void setEmailHost(String emailHost) {
		this.emailHost = emailHost;
	}

	public String getEmailEncode() {
		return emailEncode;
	}

	public void setEmailEncode(String emailEncode) {
		this.emailEncode = emailEncode;
	}

	public String getEmailUserName() {
		return emailUserName;
	}

	public void setEmailUserName(String emailUserName) {
		this.emailUserName = emailUserName;
	}

	public String getEmailPwd() {
		return emailPwd;
	}

	public void setEmailPwd(String emailPwd) {
		this.emailPwd = emailPwd;
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

	public String getEmailProtocol() {
		return emailProtocol;
	}

	public void setEmailProtocol(String emailProtocol) {
		this.emailProtocol = emailProtocol;
	}
}