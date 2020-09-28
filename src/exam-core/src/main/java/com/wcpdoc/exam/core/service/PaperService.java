package com.wcpdoc.exam.core.service;

import java.math.BigDecimal;
import java.util.List;

import com.wcpdoc.exam.core.entity.Paper;
import com.wcpdoc.exam.core.entity.PaperQuestion;
import com.wcpdoc.exam.core.entity.PaperQuestionEx;

/**
 * 试卷服务层接口
 * 
 * zhanghc 2018年10月21日上午8:16:06
 */
public interface PaperService extends BaseService<Paper> {
	/**
	 * 完成添加章节
	 * 
	 * v1.0 zhanghc 2017年5月27日上午9:42:57
	 * @param chapter
	 * @param user
	 * void
	 */
	void doChapterAdd(PaperQuestion chapter);
	
	/**
	 * 完成修改章节
	 * 
	 * v1.0 zhanghc 2017年5月27日上午11:05:42
	 * @param chapter
	 * @param user
	 * void
	 */
	void doChapterEdit(PaperQuestion chapter);
	
	/**
	 * 完成删除章节
	 * 
	 * v1.0 zhanghc 2017年5月27日下午5:58:58
	 * @param chapterId
	 * void
	 */
	void doChapterDel(Integer chapterId);
	
	/**
	 * 获取试卷列表
	 * 
	 * v1.0 zhanghc 2017年6月6日上午8:52:39
	 * @param id
	 * @return List<PaperQuestionEx>
	 */
	List<PaperQuestionEx> getPaperList(Integer id);

	/**
	 * 完成添加试题
	 * 
	 * v1.0 zhanghc 2017年5月27日下午3:32:43
	 * @param chapterId
	 * @param questionIds
	 * void
	 */
	void doQuestionAdd(Integer chapterId, Integer[] questionIds);
	
	/**
	 * 完成设置分数
	 * 
	 * v1.0 zhanghc 2018年10月21日下午3:10:37
	 * @param paperQuestionId 
	 * @param score 
	 * void
	 */
	void doScoreUpdate(Integer paperQuestionId, BigDecimal score);
	
	/**
	 * 完成设置选项
	 * 
	 * v1.0 zhanghc 2018年10月21日下午3:10:37
	 * @param paperQuestionId 
	 * @param score 
	 * void
	 */
	void doOptionsUpdate(Integer paperQuestionId, Integer[] options);

	/**
	 * 完成试题上移
	 * 
	 * v1.0 zhanghc 2018年10月21日下午4:15:35
	 * @param paperQuestionId void
	 */
	void doQuestionUp(Integer paperQuestionId);

	/**
	 * 完成试题下移
	 * 
	 * v1.0 zhanghc 2018年10月21日下午4:15:43
	 * @param paperQuestionId void
	 */
	void doQuestionDown(Integer paperQuestionId);

	/**
	 * 完成试题删除
	 * 
	 * v1.0 zhanghc 2018年10月21日下午10:43:15
	 * @param paperQuestionId void
	 */
	void doQuestionDel(Integer paperQuestionId);
	
	/**
	 * 完成清空试题
	 * 
	 * v1.0 zhanghc 2018年10月21日上午8:18:04
	 * @param chapterId
	 * void
	 */
	void doQuestionClear(Integer chapterId);

	/**
	 * 完成章节上移
	 * 
	 * v1.0 zhanghc 2018年10月21日下午2:51:35
	 * @param chapterId void
	 */
	void doChapterUp(Integer chapterId);
	
	/**
	 * 完成章节下移
	 * 
	 * v1.0 zhanghc 2018年10月21日下午2:51:35
	 * @param chapterId void
	 */
	void doChapterDown(Integer chapterId);
	
}
