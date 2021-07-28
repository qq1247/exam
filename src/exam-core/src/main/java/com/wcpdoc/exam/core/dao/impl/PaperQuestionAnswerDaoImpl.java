package com.wcpdoc.exam.core.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.wcpdoc.exam.core.dao.PaperQuestionAnswerDao;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PaperQuestionAnswer;

/**
 * 试题答案数据访问层实现
 * 
 * v1.0 chenyun 2021-07-20 18:14:32
 */
@Repository
public class PaperQuestionAnswerDaoImpl extends RBaseDaoImpl<PaperQuestionAnswer> implements PaperQuestionAnswerDao {

	@Override
	public PageOut getListpage(PageIn pageIn) {
		return null;
	}

	@Override
	public List<PaperQuestionAnswer> getPaperQuestionAnswerList(Integer paperQuestionId, Integer questionId) {
		String sql = "SELECT * FROM EXM_PAPER_QUESTION_ANSWER WHERE PAPER_QUESTION_ID = ? AND QUESTION_ID = ?";
		return getList(sql, new Object[] { paperQuestionId, questionId }, PaperQuestionAnswer.class);
	}
}