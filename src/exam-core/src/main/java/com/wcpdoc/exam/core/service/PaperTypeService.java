package com.wcpdoc.exam.core.service;

import java.util.List;

import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PaperType;
/**
 * 试卷分类服务层接口
 * 
 * v1.0 zhanghc 2017-05-25 16:34:59
 */
public interface PaperTypeService extends BaseService<PaperType> {
	
	/**
	 * 添加试卷分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * @param paperType
	 * void
	 */
	void addAndUpdate(PaperType paperType);

	/**
	 * 修改试卷分类
	 * 
	 * v1.0 cY 2021年6月11日下午2:43:48
	 * @param paperType void
	 */
	void editAndUpdate(PaperType paperType);
	
	/**
	 * 删除试卷分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * @param id
	 * void
	 */
	void delAndUpdate(Integer id);
	
	/**
	 * 名称是否重复
	 * 
	 * v1.0 zhanghc 2020-09-05 10:12:16
	 * @param paperType
	 * @return boolean
	 */
	boolean existName(PaperType paperType);

	/**
	 * 获取试卷分类列表
	 * 
	 * v1.0 zhanghc 2018年6月3日上午11:19:02
	 * @return List<PaperType>
	 */
	List<PaperType> getList();

	/**
	 * 完成授权
	 * 
	 * v1.0 zhanghc 2020年9月8日上午10:06:53
	 * @param id
	 * @param readUserIds
	 * @param writeUserIds
	 * void
	 */
	void doAuth(Integer id, String readUserIds, String writeUserIds);

	/**
	 * 获取人员列表
	 * 
	 * v1.0 zhanghc 2018年5月30日下午6:28:19
	 * @param pageIn
	 * @return PageOut
	 */
	PageOut authUserListpage(PageIn pageIn);
}
