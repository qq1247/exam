package com.wcpdoc.exam.api.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.Bulletin;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.BulletinService;
import com.wcpdoc.exam.core.util.StringUtil;
/**
 * 公告控制层
 * 
 * v1.0 chenyun 2021-03-24 13:39:37
 */
@Controller
@RequestMapping("/api/bulletin")
public class ApiBulletinController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ApiBulletinController.class);
	
	@Resource
	private BulletinService bulletinService;
	
	/**
	 * 公告列表
	 * 
	 * v1.0 chenyun 2021-03-24 13:39:37
	 * @return pageOut
	 */
	@RequestMapping("/listpage")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult listpage() {
		try {
			return PageResultEx.ok().data(bulletinService.getListpage(new PageIn(request)));
		} catch (Exception e) {
			log.error("公告列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 添加公告
	 * 
	 * v1.0 chenyun 2021-03-24 13:39:37
	 * @return pageOut
	 */
	@RequestMapping("/add")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult add(Bulletin Bulletin) {
		try {
			Bulletin.setState(1);
			Bulletin.setUpdateTime(new Date());
			Bulletin.setUpdateUserId(getCurUser().getId());
			bulletinService.add(Bulletin);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("添加公告错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("添加公告错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 修改公告
	 * 
	 * v1.0 chenyun 2021-03-24 13:39:37
	 * @return pageOut
	 */
	@RequestMapping("/edit")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult edit(Bulletin Bulletin) {
		try {
			Bulletin entity = bulletinService.getEntity(Bulletin.getId());
			entity.setTitle(Bulletin.getTitle());
			entity.setImgFileId(Bulletin.getImgFileId());
			entity.setContent(Bulletin.getContent());
			entity.setReadUserIds(Bulletin.getReadUserIds());
			entity.setTopState(Bulletin.getTopState());
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(getCurUser().getId());
			bulletinService.update(entity);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("修改公告错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("修改公告错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 删除公告
	 * 
	 * v1.0 chenyun 2021-03-24 13:39:37
	 * @return pageOut
	 */
	@RequestMapping("/del")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult del(Integer id) {
		try {
			bulletinService.delAndUpdate(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("删除公告错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("删除公告错误：", e);
			return PageResult.err();
		}
	}

	/**
	 * 获取公告
	 * 
	 * v1.0 chenyun 2021-03-04 15:02:18
	 * @return pageOut
	 */
	@RequestMapping("/get")
	@ResponseBody
	@RequiresRoles(value={"admin"},logical = Logical.OR)
	public PageResult get(Integer id) {		try {
			Bulletin entity = bulletinService.getEntity(id);
			List<Integer> readUserIds = StringUtil.toInt(entity.getReadUserIds());
			return PageResultEx.ok()
					.addAttr("id", entity.getId())
					.addAttr("title", entity.getTitle())
					.addAttr("imgFileId", entity.getImgFileId())
					.addAttr("content", entity.getContent())
					.addAttr("topState", entity.getTopState())
					.addAttr("readUserIds", readUserIds);
		} catch (MyException e) {
			log.error("获取参数错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("获取参数错误：", e);
			return PageResult.err();
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
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult auth(Integer id, String readUserIds, String readOrgIds) {
		try {
			bulletinService.auth(id, readUserIds, readOrgIds);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("添加权限用户错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("添加权限用户错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 获取人员列表 
	 * 
	 * v1.0 zhanghc 2017年6月16日下午5:02:45
	 * @param pageIn
	 * @return PageOut
	 */
	@RequestMapping("/authUserList")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult userList() {
		try {
			return PageResultEx.ok().data(bulletinService.getUserListpage(new PageIn(request)));
		} catch (Exception e) {
			log.error("权限用户列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 获取组织机构列表 
	 * 
	 * v1.0 zhanghc 2017年6月16日下午5:02:45
	 * @param pageIn
	 * @return PageOut
	 */
	@RequestMapping("/authOrgList")
	@ResponseBody
	@RequiresRoles(value={"subAdmin"},logical = Logical.OR)
	public PageResult authOrgList(PageIn pageIn, String name, Integer id) {
		try {
			return PageResultEx.ok().data(bulletinService.getOrgListpage(new PageIn(request)));
		} catch (Exception e) {
			log.error("权限用户列表错误：", e);
			return PageResult.err();
		}
	}
}