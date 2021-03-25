package com.wcpdoc.exam.core.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.core.entity.BulletinBoard;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.BulletinBoardService;
/**
 * 公告栏控制层
 * 
 * v1.0 chenyun 2021-03-24 13:39:37
 */
@Controller
@RequestMapping("/bulletinBoard")
public class BulletinBoardController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(BulletinBoardController.class);
	
	@Resource
	private BulletinBoardService bulletinBoardService;
	
	/**
	 * 公告栏列表
	 * 
	 * v1.0 chenyun 2021-03-24 13:39:37
	 * @return pageOut
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(PageIn pageIn) {
		try {
			return PageResultEx.ok().data(bulletinBoardService.getListpage(pageIn));
		} catch (Exception e) {
			log.error("公告栏列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 添加公告栏
	 * 
	 * v1.0 chenyun 2021-03-24 13:39:37
	 * @return pageOut
	 */
	@RequestMapping("/add")
	@ResponseBody
	public PageResult add(BulletinBoard bulletinBoard) {
		try {
			bulletinBoard.setUpdateTime(new Date());
			bulletinBoard.setUpdateUserId(getCurUser().getId());
			bulletinBoardService.add(bulletinBoard);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("添加公告栏错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("添加公告栏错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 修改公告栏
	 * 
	 * v1.0 chenyun 2021-03-24 13:39:37
	 * @return pageOut
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public PageResult edit(BulletinBoard bulletinBoard) {
		try {
			BulletinBoard entity = bulletinBoardService.getEntity(bulletinBoard.getId());
			entity.setTitle(bulletinBoard.getTitle());
			entity.setImgs(bulletinBoard.getImgs());
			entity.setVideo(bulletinBoard.getVideo());
			entity.setContent(bulletinBoard.getContent());
			entity.setImgsHeight(bulletinBoard.getImgsHeight());
			entity.setImgsWidth(bulletinBoard.getImgsWidth());
			entity.setUrl(bulletinBoard.getUrl());
			entity.setNo(bulletinBoard.getNo());
			entity.setState(bulletinBoard.getState());
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(getCurUser().getId());
			entity.setReadUserIds(bulletinBoard.getReadUserIds());
			entity.setReadOrgIds(bulletinBoard.getReadOrgIds());
			bulletinBoardService.update(entity);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("修改公告栏错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("修改公告栏错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 删除公告栏
	 * 
	 * v1.0 chenyun 2021-03-24 13:39:37
	 * @return pageOut
	 */
	@RequestMapping("/del")
	@ResponseBody
	public PageResult del(Integer id) {
		try {
			bulletinBoardService.delAndUpdate(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("删除公告栏错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("删除公告栏错误：", e);
			return PageResult.err();
		}
	}

}
