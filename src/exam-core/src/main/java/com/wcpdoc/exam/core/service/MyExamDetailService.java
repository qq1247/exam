package com.wcpdoc.exam.core.service;

import java.util.List;
import java.util.Map;

import com.wcpdoc.core.service.BaseService;
import com.wcpdoc.exam.core.entity.MyExamDetail;
/**
 * 我的考试详细服务层接口
 * 
 * v1.0 zhanghc 2017-06-19 16:28:29
 */
public interface MyExamDetailService extends BaseService<MyExamDetail>{

	/**
	 * 获取我的考试详细列表
	 * 
	 * v1.0 zhanghc 2017年7月3日上午9:44:45
	 * @param examId
	 * @param userId
	 * @return List<MyExamDetail>
	 */
	List<MyExamDetail> getList(Integer examId, Integer userId);

	/**
	 * 获取我的考试详细列表
	 * 
	 * v1.0 zhanghc 2017年7月3日上午9:44:45
	 * @param myExamId
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> getAnswerList(Integer myExamId);

	/**
	 * 阅卷获取我的考试详细列表
	 * 
	 * v1.0 chenyun 2021年7月29日下午6:01:59
	 * @param userId
	 * @param examId
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> getMarkAnswerList(Integer userId, Integer examId);

	/**
	 * 获取我的考试详细
	 * 
	 * v1.0 zhanghc 2021年10月19日上午9:54:36
	 * @param examId
	 * @param userId
	 * @param questionId
	 * @return MyExamDetail
	 */
	MyExamDetail getEntity(Integer examId, Integer userId, Integer questionId);
	

	/**
	 * 自动阅卷
	 * 
	 * v1.0 zhanghc 2020年10月13日下午1:13:52
	 * @param examId
	 * void
	 */
	void autoMark(Integer examId);

	/**
	 * 删除我的考试详细
	 * 
	 * v1.0 zhanghc 2021年10月27日下午2:19:13
	 * @param examId
	 * @param userId void
	 */
	void del(Integer examId, Integer userId);
}
