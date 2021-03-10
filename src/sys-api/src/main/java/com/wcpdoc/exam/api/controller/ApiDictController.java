package com.wcpdoc.exam.api.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.base.entity.Dict;
import com.wcpdoc.exam.base.service.DictService;
import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.util.ValidateUtil;

/**
 * 数据字典控制层
 * 
 * v1.0 zhanghc 2016-11-3下午9:03:40
 */
@Controller
@RequestMapping("/api/dict")
public class ApiDictController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ApiDictController.class);

	@Resource
	private DictService dictService;

	/**
	 * 数据字典列表
	 * 
	 * v1.0 zhanghc 2016-11-3下午9:03:40
	 * 
	 * @param pageIn
	 * @return PageOut
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(PageIn pageIn, String dictIndex, String dictKey, String dictValue) {
		try {
			if (!ValidateUtil.isValid(dictIndex)) {
				pageIn.setTwo(dictIndex);
			}
			if (!ValidateUtil.isValid(dictKey)) {
				pageIn.setThree(dictKey);
			}
			if (!ValidateUtil.isValid(dictValue)) {
				pageIn.setFour(dictValue);
			}
			return PageResultEx.ok().data(dictService.getListpage(pageIn));
		} catch (Exception e) {
			log.error("数据字典列表错误：", e);
			return PageResult.err();
		}
	}

	/**
	 * 添加数据字典
	 * 
	 * v1.0 zhanghc 2016-11-3下午9:03:40
	 * 
	 * @param dict
	 * @return PageResult
	 */
	@RequestMapping("/add")
	@ResponseBody
	public PageResult add(Dict dict) {
		try {
			dictService.addAndUpdate(dict);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("添加数据字典错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("添加数据字典错误：", e);
			return PageResult.err();
		}
	}

	/**
	 * 修改数据字典
	 * 
	 * v1.0 zhanghc 2016-11-3下午9:03:40
	 * 
	 * @param dict
	 * @return PageResult
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public PageResult edit(Dict dict) {
		try {
			dictService.updateAndUpdate(dict);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("修改数据字典错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("修改数据字典错误：", e);
			return PageResult.err();
		}
	}

	/**
	 * 删除数据字典
	 * 
	 * v1.0 zhanghc 2016-11-3下午9:03:40
	 * 
	 * @param id
	 * @return PageResult
	 */
	@RequestMapping("/del")
	@ResponseBody
	public PageResult del(Integer id) {
		try {
			dictService.delAndUpdate(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("删除数据字典错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("删除数据字典错误：", e);
			return PageResult.err();
		}
	}
}
