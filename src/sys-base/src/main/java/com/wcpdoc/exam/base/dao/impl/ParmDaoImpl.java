package com.wcpdoc.exam.base.dao.impl;

import org.springframework.stereotype.Repository;

import com.wcpdoc.exam.base.dao.ParmDao;
import com.wcpdoc.exam.base.entity.Parm;
import com.wcpdoc.exam.core.dao.impl.RBaseDaoImpl;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.util.DateUtil;
import com.wcpdoc.exam.core.util.HibernateUtil;
import com.wcpdoc.exam.core.util.SqlUtil;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.core.util.SqlUtil.Order;

/**
 * 参数数据访问层实现
 * 
 * v1.0 chenyun 2021-03-04 15:02:18
 */
@Repository
public class ParmDaoImpl extends RBaseDaoImpl<Parm> implements ParmDao {

	@Override
	public PageOut getListpage(PageIn pageIn) {
		String sql = "SELECT * "
				+ "FROM SYS_PARM PARM";
		SqlUtil sqlUtil = new SqlUtil(sql);
		sqlUtil.addWhere(ValidateUtil.isValid(pageIn.getTwo()), "PARM.ORG_NAME LIKE ?", "%" + pageIn.getTwo() + "%")
			   .addOrder("PARM.UPDATE_TIME", Order.DESC);
		PageOut pageOut = getListpage(sqlUtil, pageIn);
		HibernateUtil.formatDate(pageOut.getRows(), "UPDATE_TIME", DateUtil.FORMAT_DATE_TIME);
		return pageOut;
	}

}