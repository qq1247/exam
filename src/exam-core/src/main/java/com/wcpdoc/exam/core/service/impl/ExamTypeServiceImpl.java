package com.wcpdoc.exam.core.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wcpdoc.exam.base.service.OrgService;
import com.wcpdoc.exam.base.service.UserService;
import com.wcpdoc.exam.core.dao.BaseDao;
import com.wcpdoc.exam.core.dao.ExamTypeDao;
import com.wcpdoc.exam.core.entity.Exam;
import com.wcpdoc.exam.core.entity.ExamType;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.ExamService;
import com.wcpdoc.exam.core.service.ExamTypeExService;
import com.wcpdoc.exam.core.service.ExamTypeService;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.file.service.FileService;
/**
 * 考试分类服务层实现
 * 
 * v1.0 zhanghc 2017-06-28 21:34:41
 */
@Service
public class ExamTypeServiceImpl extends BaseServiceImp<ExamType> implements ExamTypeService {
	@Resource
	private ExamTypeDao examTypeDao;
	@Resource
	private ExamTypeExService examTypeExService;
	@Resource
	private OrgService orgService;
	@Resource
	private UserService userService;
	@Resource
	private FileService fileService;
	@Resource
	private ExamService examService;
	
	
	@Override
	@Resource(name = "examTypeDaoImpl")
	public void setDao(BaseDao<ExamType> dao) {
		super.dao = dao;
	}

	@Override
	public void addAndUpdate(ExamType examType) {
		//校验数据有效性
		if (!ValidateUtil.isValid(examType.getName())) {
			throw new MyException("参数错误：name");
		}
		
		if (existName(examType)) {
			throw new MyException("名称已存在！");
		}
				
		// 添加试题分类
		examType.setCreateUserId(getCurUser().getId());
		examType.setCreateTime(new Date());
		examType.setUpdateUserId(getCurUser().getId());
		examType.setUpdateTime(new Date());
		examType.setState(1);
		add(examType);
		
		//保存图片
		fileService.doUpload(examType.getImgId());
	}

	@Override
	public void delAndUpdate(Integer id) {
		// 校验数据有效性
		if (id == 1) { //不包括根试题分类
			return;
		}
		List<Exam> examList = examService.getList(id);
		if (ValidateUtil.isValid(examList)) {
			throw new MyException("该考试分类下有试题，不允许删除！");
		}
		
		// 删除试题分类
		ExamType examType = getEntity(id);
		examType.setState(0);
		examType.setUpdateTime(new Date());
		examType.setUpdateUserId(getCurUser().getId());
		update(examType);
	}

	@Override
	public boolean existName(ExamType examType) {
		return examTypeDao.existName(examType.getName(), examType.getId());
	}

	@Override
	public List<ExamType> getList() {
		return examTypeDao.getList();
	}

	@Override
	public void doAuth(Integer id, String readUserIds, String writeUserIds, boolean rwState) {
		ExamType entity = getEntity(id);
		if (!ValidateUtil.isValid(readUserIds)) {
			entity.setReadUserIds(null);
		} else {
			entity.setReadUserIds(readUserIds);
		}
		if (!ValidateUtil.isValid(writeUserIds)) {
			entity.setWriteUserIds(null);
		} else {
			entity.setWriteUserIds(writeUserIds);
		}
		if(rwState){
			entity.setRwState(1);
		}else{
			entity.setRwState(0);
		}
		
		entity.setUpdateTime(new Date());
		entity.setUpdateUserId(getCurUser().getId());
		update(entity);
	}
}
