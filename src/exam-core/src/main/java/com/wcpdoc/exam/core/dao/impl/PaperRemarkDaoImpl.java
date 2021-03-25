package com.wcpdoc.exam.core.dao.impl;

import org.springframework.stereotype.Repository;

import com.wcpdoc.exam.core.dao.PaperRemarkDao;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PaperRemark;
import com.wcpdoc.exam.core.util.SqlUtil;
import com.wcpdoc.exam.core.util.ValidateUtil;

/**
 * 试卷评语数据访问层实现
 * 
 * v1.0 chenyun 2021-03-10 13:48:34
 */
@Repository
public class PaperRemarkDaoImpl extends RBaseDaoImpl<PaperRemark> implements PaperRemarkDao {

	@Override
	public PageOut getListpage(PageIn pageIn) {
		String sql = "SELECT * FROM EXM_PAPER_REMARK PAPER_REMARK ";
		SqlUtil sqlUtil = new SqlUtil(sql);
		sqlUtil.addWhere(ValidateUtil.isValid(pageIn.getTwo()), "PAPER_REMARK.ID = ?", pageIn.getTwo());
		PageOut pageOut = getListpage(sqlUtil, pageIn);
		return pageOut;
	}

	@Override
	public PaperRemark getPaperRemark(Integer paperId) {
		String sql = "SELECT * FROM EXM_PAPER_REMARK WHERE PAPER_ID = ? ";
		return getEntity(sql, new Object[]{ paperId });
	}
}