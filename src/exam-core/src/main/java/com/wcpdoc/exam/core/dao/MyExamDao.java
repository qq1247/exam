package com.wcpdoc.exam.core.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.wcpdoc.exam.core.dao.BaseDao;
import com.wcpdoc.exam.core.entity.MyExam;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;

/**
 * 我的考试数据访问层接口
 * 
 * v1.0 zhanghc 2017-06-19 16:28:29
 */
public interface MyExamDao extends BaseDao<MyExam>{

	/**
	 * 删除实体
	 * 
	 * v1.0 zhanghc 2017年6月26日下午2:11:44
	 * @param examId
	 * @param userId
	 * void
	 */
	void del(Integer examId, Integer userId);

	/**
	 * 获取我的考试
	 * 
	 * v1.0 zhanghc 2020年9月30日上午11:00:50
	 * @param examId
	 * @return List<MyExam>
	 */
	List<MyExam> getList(Integer examId);
	
	/**
	 * 考试时间表
	 * 
	 * v1.0 chenyun 2021年3月23日上午11:14:46
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @return List<MyExam>
	 */
	List<MyExam> kalendar(Integer userId, Date startTime, Date endTime);
	
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
	 * @return List<Map<String, Object>>
	 */
	List<Map<String, Object>> count(Integer examId);
}
