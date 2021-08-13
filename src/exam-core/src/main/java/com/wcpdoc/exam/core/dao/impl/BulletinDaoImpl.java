package com.wcpdoc.exam.core.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.wcpdoc.exam.base.cache.DictCache;
import com.wcpdoc.exam.base.dao.UserDao;
import com.wcpdoc.exam.base.entity.User;
import com.wcpdoc.exam.core.dao.BulletinDao;
import com.wcpdoc.exam.core.entity.Bulletin;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.util.DateUtil;
import com.wcpdoc.exam.core.util.HibernateUtil;
import com.wcpdoc.exam.core.util.SqlUtil;
import com.wcpdoc.exam.core.util.SqlUtil.Order;
import com.wcpdoc.exam.core.util.ValidateUtil;

/**
 * 公告数据访问层实现
 * 
 * v1.0 chenyun 2021-03-24 13:39:37
 */
@Repository
public class BulletinDaoImpl extends RBaseDaoImpl<Bulletin> implements BulletinDao {
	@Resource
	private UserDao userDao;
	
	@Override
	public PageOut getListpage(PageIn pageIn) {
		String sql = "SELECT BULLETIN.ID, BULLETIN.TITLE, BULLETIN.TOP_STATE, BULLETIN.UPDATE_TIME, "
				+ "BULLETIN.IMG_FILE_ID, BULLETIN.CONTENT, BULLETIN.STATE, USER.NAME AS UPDATE_USER_NAME, "
				+ "( SELECT GROUP_CONCAT( Z.NAME ) FROM SYS_USER Z WHERE BULLETIN.READ_USER_IDS LIKE CONCAT( '%', Z.ID, '%' ) ) AS READ_USER_NAMES "
				+ "FROM EXM_BULLETIN BULLETIN "
				+ "LEFT JOIN SYS_USER USER ON BULLETIN.UPDATE_USER_ID = USER.ID ";
		SqlUtil sqlUtil = new SqlUtil(sql);
		sqlUtil.addWhere(ValidateUtil.isValid(pageIn.get("id")), "BULLETIN.ID = ?", pageIn.get("id"))
			   .addWhere(ValidateUtil.isValid(pageIn.get("title")), "BULLETIN.TITLE LIKE ?", "%" + pageIn.get("title") + "%")
			   .addWhere(ValidateUtil.isValid(pageIn.get("topState")), "BULLETIN.TOP_STATE = ?", pageIn.get("topState", Integer.class))
			   .addWhere(pageIn.get("curUserId1", Integer.class)!= null, "BULLETIN.UPDATE_USER_ID = ?", pageIn.get("curUserId1", Integer.class))
			   .addWhere(pageIn.get("state", Integer.class)!= null, "BULLETIN.STATE = ?", pageIn.get("state", Integer.class))
			   .addWhere("BULLETIN.STATE != ?", 0)
			   .addOrder("BULLETIN.TOP_STATE", Order.ASC)
			   .addOrder("BULLETIN.UPDATE_TIME", Order.DESC);
		
		  if (pageIn.get("readUserIds", Integer.class) != null) {
			   User user = userDao.getEntity(pageIn.get("readUserIds", Integer.class));
			   StringBuilder partSql = new StringBuilder();
			   List<Object> params = new ArrayList<>();
			   partSql.append("(");
			   partSql.append("BULLETIN.READ_USER_IDS IS NULL OR BULLETIN.READ_USER_IDS LIKE ? ");
			   params.add("%," + user.getId() + ",%");
			   /*partSql.append("OR BULLETIN.READ_ORG_IDS LIKE ? ");
			   params.add("%," + user.getOrgId() + ",%");*/
			   partSql.append(")");
			   sqlUtil.addWhere(partSql.toString(), params.toArray(new Object[params.size()]));
			  }
		
		PageOut pageOut = getListpage(sqlUtil, pageIn);
		HibernateUtil.formatDate(pageOut.getList(), "updateTime", DateUtil.FORMAT_DATE_TIME);
		HibernateUtil.formatDict(pageOut.getList(), DictCache.getIndexkeyValueMap(), "STATE_YN", "topState");
		return pageOut;
	}

	@Override
	public PageOut getUserListpage(PageIn pageIn) {
		String sql = "SELECT USER.ID, USER.NAME AS NAME "
				+ "FROM SYS_USER USER ";
		SqlUtil sqlUtil = new SqlUtil(sql);
		sqlUtil.addWhere(ValidateUtil.isValid(pageIn.get("name")), "(USER.NAME LIKE ? )", "%" + pageIn.get("name") + "%")
				.addWhere(ValidateUtil.isValid(pageIn.get("id")), "EXISTS (SELECT 1 FROM EXM_BULLETIN Z WHERE Z.ID = ? AND Z.READ_USER_IDS LIKE CONCAT('%,', USER.ID, ',%'))", pageIn.get("id"))
				.addWhere("USER.STATE = 1")
				.addOrder("USER.UPDATE_TIME", Order.DESC);
		PageOut pageOut = getListpage(sqlUtil, pageIn);
		return pageOut;
	}

	@Override
	public PageOut getOrgListpage(PageIn pageIn) {
		String sql = "SELECT ORG.ID, ORG.NAME AS NAME "
				+ "FROM SYS_ORG ORG ";
		SqlUtil sqlUtil = new SqlUtil(sql);
		sqlUtil.addWhere(ValidateUtil.isValid(pageIn.get("name")), "(ORG.NAME LIKE ? )", "%" + pageIn.get("name") + "%")
				.addWhere(ValidateUtil.isValid(pageIn.get("id")), "EXISTS (SELECT 1 FROM EXM_BULLETIN Z WHERE Z.ID = ? AND Z.READ_ORG_IDS LIKE CONCAT('%,', ORG.ID, ',%'))", pageIn.get("id"))
				.addWhere("ORG.STATE = 1")
				.addOrder("ORG.UPDATE_TIME", Order.DESC);
		PageOut pageOut = getListpage(sqlUtil, pageIn);
		return pageOut;
	}
}