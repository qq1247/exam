package com.wcpdoc.exam.api.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wcpdoc.exam.base.entity.User;
import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.entity.QuestionType;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.QuestionTypeService;
import com.wcpdoc.exam.core.util.ValidateUtil;

/**
 * 试题分类控制层
 * 
 * v1.0 zhanghc 2016-5-24下午14:54:09
 */
@Controller
@RequestMapping("/api/questionType")
public class ApiQuestionTypeController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ApiQuestionTypeController.class);
	
	@Resource
	private QuestionTypeService questionTypeService;
	
	/**
	 * 试题分类列表 
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(PageIn pageIn) {
		try {
			return new PageResultEx(true, "查询成功", questionTypeService.getListpage(pageIn));
		} catch (Exception e) {
			log.error("试题分类列表错误：", e);
			return new PageResult(false, "查询失败");
		}
	}

	/**
	 * 添加试题分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/add")
	@ResponseBody
	public PageResult add(String name, @RequestParam("file") MultipartFile file) {
		try {
			questionTypeService.addAndUpdate(name, file);
			return new PageResult(true, "添加成功");
		} catch (MyException e) {
			log.error("添加试题分类错误：{}", e.getMessage());
			return new PageResult(false, e.getMessage());
		} catch (Exception e) {
			log.error("添加试题分类错误：", e);
			return new PageResult(false, "未知异常");
		}
	}

	/**
	 * 修改试题分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public PageResult edit(Integer id, String name, @RequestParam("file") MultipartFile file) {
		try {
			//校验数据有效性
			if(id != null) {
				throw new MyException("参数错误：id");
			}
			if(!ValidateUtil.isValid(name)) {
				throw new MyException("参数错误：name");
			}
			QuestionType entity = questionTypeService.getEntity(id);
			entity.setName(name);
			if(questionTypeService.existName(entity)) {
				throw new MyException("名称已存在！");
			}
			
			//修改试题分类
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(((User)getCurUser()).getId());
			questionTypeService.update(entity);
			return new PageResult(true, "修改成功");
		} catch (MyException e) {
			log.error("修改试题分类错误：{}", e.getMessage());
			return new PageResult(false, e.getMessage());
		} catch (Exception e) {
			log.error("修改试题分类错误：", e);
			return new PageResult(false, "未知异常");
		}
	}
	
	/**
	 * 删除试题分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/del")
	@ResponseBody
	public PageResult del(Integer id) {
		try {
			questionTypeService.delAndUpdate(id);
			return new PageResult(true, "删除成功");
		} catch (MyException e) {
			log.error("删除试题分类错误：{}", e.getMessage());
			return new PageResult(false, e.getMessage());
		} catch (Exception e) {
			log.error("删除试题分类错误：", e);
			return new PageResult(false, "未知异常");
		}
	}
	
	/**
	 * 获取人员列表 
	 * 
	 * v1.0 zhanghc 2017年6月16日下午5:02:45
	 * @param pageIn
	 * @return PageOut
	 */
	@RequestMapping("/userList")
	@ResponseBody
	public PageResult userList(PageIn pageIn) {  //Two - name (userName || orgName)  Ten - id
		try {
			return new PageResultEx(true, "查询成功", questionTypeService.getUserListpage(pageIn));
		} catch (Exception e) {
			log.error("权限用户列表错误：", e);
			return new PageResult(false, "查询失败");
		}
	}
	
	/**
	 * 添加权限
	 * 
	 * v1.0 chenyun 2021年3月2日上午10:18:26
	 * @param id	
	 * @param readUserIds
	 * @param writeUserIds
	 * @param rwState
	 * @return PageResult
	 */
	@RequestMapping("/auth")
	@ResponseBody
	public PageResult auth(Integer id, Integer[] readUserIds, Integer[] writeUserIds, boolean rwState) {
		try {
			questionTypeService.doAuth(id, readUserIds, writeUserIds, rwState);
			return new PageResult(true, "添加成功");
		} catch (MyException e) {
			log.error("添加权限用户错误：{}", e.getMessage());
			return new PageResult(false, e.getMessage());
		} catch (Exception e) {
			log.error("添加权限用户错误：", e);
			return new PageResult(false, "未知异常！");
		}
	}
}
