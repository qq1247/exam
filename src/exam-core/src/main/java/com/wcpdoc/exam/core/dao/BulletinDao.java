package com.wcpdoc.exam.core.dao;

import com.wcpdoc.core.dao.RBaseDao;
import com.wcpdoc.core.entity.PageIn;
import com.wcpdoc.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.Bulletin;

/**
 * 公告数据访问层接口
 * 
 * v1.0 chenyun 2021-03-24 13:39:37
 */
public interface BulletinDao extends RBaseDao<Bulletin> {
	/**
	 * 获取人员列表
	 * 
	 * v1.0 zhanghc 2018年5月30日下午6:28:19
	 * @param pageIn
	 * @return PageOut
	 */
	PageOut getUserListpage(PageIn pageIn);
	
	/**
	 * 获取人员列表
	 * 
	 * v1.0 zhanghc 2018年5月30日下午6:28:19
	 * @param pageIn
	 * @return PageOut
	 */
	PageOut getOrgListpage(PageIn pageIn);
}
