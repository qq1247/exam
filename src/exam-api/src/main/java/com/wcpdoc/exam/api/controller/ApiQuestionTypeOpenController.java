package com.wcpdoc.exam.api.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.entity.QuestionTypeOpen;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.QuestionTypeOpenService;
import com.wcpdoc.exam.core.util.ValidateUtil;

/**
 * 试题分类开放控制层
 * 
 * v1.0 chenyun 2021-03-02 13:43:21
 */
@Controller
@RequestMapping("/api/questionTypeOpen")
public class ApiQuestionTypeOpenController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ApiQuestionTypeOpenController.class);
	
	@Resource
	private QuestionTypeOpenService questionTypeOpenService;
	
	/**
	 * 试题分类开放列表
	 * 
	 * v1.0 chenyun 2021-03-02 13:43:21
	 * @return pageOut
	 */
	@RequestMapping("/listpage")
	@ResponseBody
	public PageResult listpage() {
		try {
			PageIn pageIn = new PageIn(request);
			pageIn.addAttr("curUserId", getCurUser().getId());
			return PageResultEx.ok().data(questionTypeOpenService.getListpage(pageIn));
		} catch (Exception e) {
			log.error("试题分类开放列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 试题开放列表
	 * 
	 * v1.0 chenyun 2021-03-02 13:43:21
	 * @return pageOut
	 */
	@RequestMapping("/questionListpage")
	@ResponseBody
	public PageResult questionListpage() {
		try {
			PageIn pageIn = new PageIn(request);
			pageIn.addAttr("curUserId", getCurUser().getId())
			.addAttr("open", "open");
			return PageResultEx.ok().data(questionTypeOpenService.questionListpage(pageIn));
		} catch (Exception e) {
			log.error("试题分类开放列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成添加试题分类开放
	 * 
	 * v1.0 chenyun 2021-03-02 13:43:21
	 * @return pageOut
	 */
	@RequestMapping("/add")
	@ResponseBody
	public PageResult add(QuestionTypeOpen questionTypeOpen) {
		try {
			questionTypeOpenService.addAndUpdate(questionTypeOpen);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成添加试题分类开放错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成添加试题分类开放错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成修改试题分类开放
	 * 
	 * v1.0 chenyun 2021-03-02 13:43:21
	 * @return pageOut
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public PageResult edit(QuestionTypeOpen questionTypeOpen) {
		try {
			QuestionTypeOpen entity = questionTypeOpenService.getEntity(questionTypeOpen.getId());
			entity.setStartTime(questionTypeOpen.getStartTime());
			entity.setEndTime(questionTypeOpen.getEndTime());
			if (ValidateUtil.isValid(questionTypeOpen.getUserIds())) {
				entity.setUserIds(","+questionTypeOpen.getUserIds()+",");
			}
			if (ValidateUtil.isValid(questionTypeOpen.getOrgIds())) {
				entity.setOrgIds(","+questionTypeOpen.getOrgIds()+",");
			}
			entity.setUpdateUserId(getCurUser().getId());
			entity.setUpdateTime(new Date());
			entity.setState(questionTypeOpen.getState());
			entity.setQuestionTypeId(questionTypeOpen.getQuestionTypeId());
			questionTypeOpenService.update(entity);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成修改试题分类开放错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成修改试题分类开放错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成删除试题分类开放
	 * 
	 * v1.0 chenyun 2021-03-02 13:43:21
	 * @return pageOut
	 */
	@RequestMapping("/del")
	@ResponseBody
	public PageResult del(Integer id) {
		try {
			questionTypeOpenService.delAndUpdate(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成删除试题分类开放错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成删除试题分类开放错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 获取试题
	 * 拥有读写权限才显示答案字段
	 * 
	 * v1.0 zhanghc 2021年7月1日下午2:18:05
	 * @param id
	 * @return PageResult
	 */
	@RequestMapping("/questionGet")
	@ResponseBody
	public PageResult questionGet(Integer questionId) {
		try {
			return questionTypeOpenService.get(questionId);
		} catch (MyException e) {
			log.error("获取试题错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		}  catch (Exception e) {
			log.error("获取试题错误：", e);
			return PageResult.err();
		}
	}
}
