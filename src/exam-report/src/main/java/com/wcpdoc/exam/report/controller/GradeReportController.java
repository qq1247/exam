package com.wcpdoc.exam.report.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.base.cache.DictCache;
import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.report.service.GradeService;

/**
 * 成绩报表控制层
 * 
 * v1.0 zhanghc 2017年8月29日下午2:03:36
 */
@Controller
@RequestMapping("/gradeReport")
public class GradeReportController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(GradeReportController.class);
	
	@Resource
	private GradeService gradeService;
	
	/**
	 * 到达成绩列表页面 
	 * 
	 * v1.0 zhanghc 2017年8月29日下午2:03:36
	 * @return String
	 */
	@RequestMapping("/toList")
	public String toList(Model model) {
		try {
			model.addAttribute("MY_EXAM_STATE", DictCache.getIndexDictlistMap().get("MY_EXAM_STATE"));
			return "report/gradeReport/gradeReportList";
		} catch (Exception e) {
			log.error("到达成绩列表页面错误：", e);
			return "report/gradeReport/gradeReportList";
		}
	}
	
	/**
	 * 成绩列表 
	 * 
	 * v1.0 zhanghc 2017年8月29日下午2:03:36
	 * @return pageOut
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageOut list(PageIn pageIn) {
		try {
			return gradeService.getListpage(pageIn);
		} catch (Exception e) {
			log.error("成绩列表错误：", e);
			return new PageOut();
		}
	}
}
