package com.wcpdoc.exam.core.service;

import java.util.List;
import java.util.Map;

import com.wcpdoc.exam.core.entity.MyExam;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
/**
 * 我的考试服务层接口
 * 
 * v1.0 zhanghc 2017-06-19 16:28:29
 */
public interface MyExamService extends BaseService<MyExam>{

	/**
	 * 删除实体
	 * 
	 * v1.0 zhanghc 2017年6月26日下午2:09:55
	 * @param examId
	 * @param id
	 * void
	 */
	void del(Integer examId, Integer id);

	/**
	 * 获取我的考试列表
	 * 
	 * v1.0 zhanghc 2020年9月29日下午5:09:50
	 * @param examId
	 * @return List<MyExam>
	 */
	List<MyExam> getList(Integer examId);
	
	/**
	 * 获取我的考试列表
	 * 
	 * v1.0 chenyun 2021年7月30日下午3:49:53
	 * @param examId
	 * @param userId
	 * @return MyExam
	 */
	MyExam getEntity(Integer examId, Integer userId);

	/**
	 * 考试时间表
	 * 
	 * v1.0 chenyun 2021年3月23日上午11:05:51
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> kalendar(Integer year, Integer month);
	
	/**
	 * 成绩排名
	 * 
	 * v1.0 chenyun 2021年3月23日下午3:14:01
	 * @param pageIn
	 * @return PageOut
	 */
	PageOut getRankingPage(PageIn pageIn);
	
	/**
	 * 分数统计
	 * 
	 * v1.0 chenyun 2021年3月24日上午10:04:05
	 * @param examId
	 * @return Map<String,Object>
	 */
	Map<String, Object> count(Integer examId);
	
	/**
	 * 更新答案
	 * 
	 * v1.0 chenyun 2021年8月24日上午9:55:53
	 * @param myExamDetailId
	 * @param answers void
	 */
	void updateAnswer(Integer myExamDetailId, String[] answers);
	
	/**
	 * 完成交卷
	 * 
	 * v1.0 chenyun 2021年8月24日上午10:05:25
	 * @param myExamId void
	 */
	void doAnswer(Integer myExamId);
}
