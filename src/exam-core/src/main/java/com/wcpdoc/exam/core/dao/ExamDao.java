package com.wcpdoc.exam.core.dao;

import java.util.List;
import java.util.Map;

import com.wcpdoc.core.dao.BaseDao;
import com.wcpdoc.core.entity.PageIn;
import com.wcpdoc.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.Exam;

/**
 * 考试数据访问层接口
 * 
 * v1.0 zhanghc 2017-06-11 09:13:23
 */
public interface ExamDao extends BaseDao<Exam>{
	
	/**
	 * 获取考试用户列表 
	 * 
	 * v1.0 zhanghc 2017年6月16日下午5:02:45
	 * @param pageIn
	 * @return PageOut
	 */
	List<Map<String, Object>> getExamUserList(Integer id);

	/**
	 * 获取考试分类列表
	 * 
	 * v1.0 zhanghc 2017年8月6日下午10:03:54
	 * @param examTypeId
	 * @return List<Exam>
	 */
	List<Exam> getList(Integer examTypeId);

	/**
	 * 获取成绩列表
	 * 
	 * v1.0 zhanghc 2017年8月29日下午3:18:37
	 * @param pageIn
	 * @return PageOut
	 */
	PageOut getGradeListpage(PageIn pageIn);
	
	/**
	 * 获取考试列表
	 * 
	 * v1.0 zhanghc 2017年8月6日下午10:03:09
	 * @param paperId
	 * @return List<Exam>
	 */
	List<Exam> getExamList(Integer paperId);
	
	/**
	 * 阅卷考试用户列表
	 * 
	 * v1.0 zhanghc 2021年6月25日下午2:51:40
	 * @param id
	 * @param markUserId
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> getMarkExamUserList(Integer id, Integer markUserId);

	/**
	 * 阅卷试题列表
	 * 
	 * v1.0 zhanghc 2021年6月25日下午2:51:50
	 * @param id
	 * @param markUserId
	 * @return List<Map<String,Object>>
	 */
	List<Map<String, Object>> getMarkQuestionList(Integer id, Integer markUserId); 
}
