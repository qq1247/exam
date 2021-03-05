package com.wcpdoc.exam.notify.service;

import com.wcpdoc.exam.notify.entity.Email;
import com.wcpdoc.exam.notify.exception.EmailException;

/**
 * 邮件服务接口
 * 
 * v1.0 zhanghc 2019年10月15日下午15:51:27
 */
public interface EmailExService {
	
	/**
	 * 获取邮件信息
	 * 
	 * v1.0 zhanghc 2019年10月15日下午15:51:27
	 * void
	 */
	public Email getEmail() throws EmailException;
	
}
