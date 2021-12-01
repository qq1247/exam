package com.wcpdoc.exam.core.dao;

import java.util.List;
import java.util.Map;

import com.wcpdoc.core.dao.BaseDao;
import com.wcpdoc.exam.core.entity.MyExamDetail;

/**
 * 我的考试详细数据访问层接口
 * 
 * v1.0 zhanghc 2017-06-19 16:28:29
 */
public interface MyExamDetailDao extends BaseDao<MyExamDetail>{
	
	/**
	 * 获取我的考试详细列表
	 * 
	 * v1.0 zhanghc 2017年7月3日上午9:44:45
	 * @param myExamId
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
	List<Map<String, Object>> getAnswerList(Integer myExamId, Integer curUserId);

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
	 * v1.0 zhanghc 2021年10月19日上午9:55:31
	 * @param examId
	 * @param userId
	 * @param questionId
	 * @return MyExamDetail
	 */
	MyExamDetail getEntity(Integer examId, Integer userId, Integer questionId);

	/**
	 * 删除我的考试详细
	 * 
	 * v1.0 zhanghc 2021年10月27日下午2:20:11
	 * @param examId
	 * @param userId void
	 */
	void del(Integer examId, Integer userId);

	/**
	 * 获取未完成阅卷详情
	 * 
	 * v1.0 chenyun 2021年11月30日下午5:44:58
	 * @param examId
	 * @param questionId
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> getOutMarkList(Integer examId, Integer questionId);
}
