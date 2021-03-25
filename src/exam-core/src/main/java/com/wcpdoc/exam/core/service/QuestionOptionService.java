package com.wcpdoc.exam.core.service;

import com.wcpdoc.exam.core.service.BaseService;
import com.wcpdoc.exam.core.entity.QuestionOption;

/**
 * 试题选项服务层接口
 * 
 * v1.0 chenyun 2021-03-10 16:11:06
 */
public interface QuestionOptionService extends BaseService<QuestionOption> {

	/**
	 * 删除试题选项
	 * 
	 * v1.0 chenyun 2021-03-10 16:11:06
	 * 
	 * @param id
	 * void
	 */
	void delAndUpdate(Integer id);
}