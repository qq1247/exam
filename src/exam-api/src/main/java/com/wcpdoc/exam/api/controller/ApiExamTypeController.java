package com.wcpdoc.exam.api.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.base.service.OrgService;
import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.ExamType;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.ExamTypeService;
import com.wcpdoc.exam.core.util.DateUtil;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.file.service.FileService;

/**
 * 考试分类控制层
 * 
 * v1.0 zhanghc 2017-06-28 21:34:41
 */
@Controller
@RequestMapping("/api/examType")
public class ApiExamTypeController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ApiExamTypeController.class);
	
	@Resource
	private ExamTypeService examTypeService;
	@Resource
	private OrgService orgService;
	@Resource
	private FileService fileService;
	
	/**
	 * 试题分类列表 
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/listpage")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult listpage(PageIn pageIn) {
		try {
			return PageResultEx.ok().data(examTypeService.getListpage(pageIn));
		} catch (Exception e) {
			log.error("试题分类列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成添加试题分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/add")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult add(ExamType examType) {
		try {
			examTypeService.addAndUpdate(examType);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成添加试题分类错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成添加试题分类错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 获取试题分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/get")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult get(Integer id) {
		try {
			ExamType entity = examTypeService.getEntity(id);
			return PageResultEx.ok()
					.addAttr("id", entity.getId())
					.addAttr("name", entity.getName())
					.addAttr("imgId", entity.getImgId())
					.addAttr("createUserId", entity.getCreateUserId())
					.addAttr("createTime", DateUtil.formatDateTime(entity.getCreateTime()))
					.addAttr("rwState", entity.getRwState())
					.addAttr("readUserIds", entity.getReadUserIds())
					.addAttr("writeUserIds", entity.getWriteUserIds());
		} catch (MyException e) {
			log.error("获取试题分类错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("获取试题分类错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成修改试题分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/edit")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult edit(ExamType examType) {
		try {
			//校验数据有效性
			if(!ValidateUtil.isValid(examType.getName())) {
				throw new MyException("参数错误：name");
			}
			if(examTypeService.existName(examType)) {
				throw new MyException("名称已存在！");
			}
			
			//修改试题分类
			ExamType entity = examTypeService.getEntity(examType.getId());
			entity.setName(examType.getName());
			entity.setImgId(examType.getImgId());
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(getCurUser().getId());
			examTypeService.update(entity);
			
			//保存图片
			fileService.doUpload(examType.getImgId());
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成修改试题分类错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成修改试题分类错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成删除试题分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/del")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult del(Integer id) {
		try {
			examTypeService.delAndUpdate(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成删除试题分类错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成删除试题分类错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成添加权限
	 * 
	 * v1.0 zhanghc 2017年6月16日下午5:02:45
	 * @param id
	 * @param userIds
	 * @param postIds
	 * @param orgIds
	 * @param syn2Sub true ： 同步授权到子分类
	 * @return PageResult
	 */
	@RequestMapping("/auth")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult auth(Integer id, String readUserIds, String writeUserIds, boolean rwState) {
		try {
			examTypeService.doAuth(id, readUserIds, writeUserIds, rwState);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成添加权限用户错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成添加权限用户错误：", e);
			return PageResult.err();
		}
	}
}
