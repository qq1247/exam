package com.wcpdoc.exam.api.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.core.constant.ConstantManager;
import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.Exam;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.ExamService;
import com.wcpdoc.exam.core.service.ExamTypeService;
import com.wcpdoc.exam.core.service.PaperService;
import com.wcpdoc.exam.core.service.PaperTypeService;

/**
 * 考试控制层
 * 
 * v1.0 zhanghc 2017-06-11 09:13:23
 */
@Controller
@RequestMapping("/api/exam")
public class ApiExamController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(ApiExamController.class);
	
	@Resource
	private ExamService examService;
	@Resource
	private PaperService paperService;
	@Resource
	private PaperTypeService paperTypeService;
	@Resource
	private ExamTypeService examTypeService;
	
	/**
	 * 用户列表 
	 * 
	 * v1.0 zhanghc 2018年10月31日上午10:27:22
	 * @param pageIn
	 * @return PageOut
	 */
	@RequestMapping("/userList")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult userListpage() {
		try {
			return PageResultEx.ok().data(examService.getUserListpage(new PageIn(request)));
		} catch (Exception e) {
			log.error("用户列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 考试列表 
	 * 
	 * v1.0 zhanghc 2018年10月25日下午9:23:06
	 * @return pageOut
	 */
	@RequestMapping("/listpage")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult listpage() {
		try {
			PageIn pageIn = new PageIn(request);
			if(!ConstantManager.ADMIN_LOGIN_NAME.equals(getCurUser().getLoginName())) {
				pageIn.addAttr("CurUserId", getCurUser().getId());
			}
			return PageResultEx.ok().data(examService.getListpage(pageIn));
		} catch (Exception e) {
			log.error("考试列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成添加考试
	 * 
	 * v1.0 zhanghc 2018年10月25日下午9:23:06
	 * @return pageOut
	 */
	@RequestMapping("/add")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult add(Exam exam) {
		try {
			//校验数据有效性
			if(exam.getStartTime().getTime() <= new Date().getTime()) {
				throw new MyException("考试开始时间必须大于当前时间！");
			}
			if(exam.getStartTime().getTime() >= exam.getEndTime().getTime()) {
				throw new MyException("考试结束时间必须大于考试开始时间！");
			}
			if(exam.getMarkStartTime().getTime() <= exam.getEndTime().getTime()) {
				throw new MyException("阅卷开始时间必须大于考试结束时间！");
			}
			if(exam.getMarkStartTime().getTime() >= exam.getMarkEndTime().getTime()) {
				throw new MyException("阅卷结束时间必须大于阅卷开始时间！");
			}
			
			//添加考试
			exam.setUpdateTime(new Date());
			exam.setUpdateUserId(getCurUser().getId());
			exam.setState(2);
			examService.add(exam);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成添加考试错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成添加考试错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成修改考试
	 * 
	 * v1.0 zhanghc 2018年10月25日下午9:23:06
	 * @return pageOut
	 */
	@RequestMapping("/edit")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult edit(Exam exam) {
		try {
			//校验数据有效性
			Exam entity = examService.getEntity(exam.getId());
			if(entity.getState() == 1) {
				throw new MyException("考试已发布！");
			}
			if(exam.getStartTime().getTime() <= new Date().getTime()) {
				throw new MyException("考试开始时间必须大于当前时间！");
			}
			if(exam.getStartTime().getTime() >= exam.getEndTime().getTime()) {
				throw new MyException("考试结束时间必须大于考试开始时间！");
			}
			if(exam.getMarkStartTime().getTime() <= exam.getEndTime().getTime()) {
				throw new MyException("阅卷开始时间必须大于考试结束时间！");
			}
			if(exam.getMarkStartTime().getTime() >= exam.getMarkEndTime().getTime()) {
				throw new MyException("阅卷结束时间必须大于阅卷开始时间！");
			}
			
			//添加考试
			entity.setName(exam.getName());
			entity.setPaperId(exam.getPaperId());
			entity.setStartTime(exam.getStartTime());
			entity.setEndTime(exam.getEndTime());
			entity.setMarkStartTime(exam.getMarkStartTime());
			entity.setMarkEndTime(exam.getMarkEndTime());
			entity.setScoreState(exam.getScoreState());
			entity.setRankState(exam.getRankState());
			entity.setLoginType(exam.getLoginType());
			entity.setDescription(exam.getDescription());
			entity.setScoreERemark(exam.getScoreERemark());
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(getCurUser().getId());
			examService.update(entity);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成修改考试错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成修改考试错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成删除考试
	 * 
	 * v1.0 zhanghc 2017-06-11 09:13:23
	 * @return pageOut
	 */
	@RequestMapping("/del")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult del(Integer id) {
		try {
			Date curTime = new Date();
			Exam exam = examService.getEntity(id);
			if(exam.getStartTime().getTime() >= curTime.getTime()
					&& exam.getEndTime().getTime() <= curTime.getTime()) {
				throw new MyException("【"+exam.getName()+"】考试未结束");
			}
			
			exam.setState(0);
			examService.update(exam);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成删除考试错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成删除考试错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成考试配置
	 * 
	 * v1.0 zhanghc 2017年6月16日下午5:02:45
	 * @param id
	 * @param userIds
	 * @param markIds
	 * @return PageResult
	 */
	@RequestMapping("/cfg")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult cfg(Integer id, Integer[] userIds, Integer[] myMarkIds) {
		try {
			examService.cfg(id, userIds, myMarkIds);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成考试配置错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成考试配置错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成发布
	 * 
	 * v1.0 zhanghc 2018年11月24日上午9:13:22
	 * @param id
	 * @return PageResult
	 */
	@RequestMapping("/publish")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult publish(Integer id) {
		try {
			Exam exam = examService.getEntity(id);
			if(exam.getState() == 0) {
				throw new MyException("考试【"+exam.getName()+"】已删除！");
			}
			if(exam.getState() == 1) {
				throw new MyException("考试【"+exam.getName()+"】已发布！");
			}
			
			exam.setState(1);
			examService.update(exam);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成发布错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成发布错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 归档
	 * 
	 * v1.0 zhanghc 2018年11月24日上午9:13:22
	 * @param id
	 * @return PageResult
	 */
	@RequestMapping("/archive")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult archive(Integer id) {
		try {
			Exam exam = examService.getEntity(id);
			exam.setState(3);
			examService.update(exam);
			return PageResult.ok();
		}catch (Exception e) {
			log.error("归档错误：", e);
			return PageResult.err();
		}
	}

}