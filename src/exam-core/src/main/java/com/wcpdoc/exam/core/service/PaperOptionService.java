package com.wcpdoc.exam.core.service;

import com.wcpdoc.exam.core.service.BaseService;
import com.wcpdoc.exam.core.entity.PaperOption;

/**
 * 试卷服务层接口
 * 
 * v1.0 chenyun 2021-03-10 13:47:35
 */
public interface PaperOptionService extends BaseService<PaperOption> {

	/**
	 * 删除试卷
	 * 
	 * v1.0 chenyun 2021-03-10 13:47:35
	 * 
	 * @param id
	 * void
	 */
	void delAndUpdate(Integer id);
	
	/**
	 * 获取实体
	 * 
	 * v1.0 chenyun 2021年3月10日下午3:28:44
	 * @param paperId
	 * @return PaperOption
	 */
	PaperOption getPaperOption(Integer paperId);
}