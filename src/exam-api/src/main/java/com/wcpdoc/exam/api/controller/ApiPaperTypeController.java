package com.wcpdoc.exam.api.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.base.entity.User;
import com.wcpdoc.exam.base.service.OrgService;
import com.wcpdoc.exam.base.service.UserService;
import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.entity.PaperType;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.PaperTypeService;
import com.wcpdoc.exam.core.util.DateUtil;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.file.service.FileService;

/**
 * 试卷分类控制层
 * 
 * v1.0 zhanghc 2017-05-25 16:34:59
 */
@Controller
@RequestMapping("/api/paperType")
public class ApiPaperTypeController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ApiPaperTypeController.class);
	
	@Resource
	private PaperTypeService paperTypeService;
	@Resource
	private OrgService orgService;
	@Resource
	private UserService userService;
	@Resource
	private FileService fileService;
	
	/**
	 * 试卷分类列表 
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/listpage")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult listpage(PageIn pageIn, String name, String userName) {
		try {
			if (ValidateUtil.isValid(name)) {
				pageIn.setTwo(name);
			}
			if (ValidateUtil.isValid(userName)) {
				pageIn.setThree(userName);
			}
			
			PageOut listpage = paperTypeService.getListpage(pageIn);
			for(Map<String, Object> mapList : listpage.getRows()){
				if(mapList.get("readUserIds")!= null){
					String[] readUserSplit = mapList.get("readUserIds").toString().subSequence(1, mapList.get("readUserIds").toString().length()).toString().split(",");
					for(String id : readUserSplit){
						User user = userService.getEntity(Integer.parseInt(id));
						if(mapList.get("readUserNames") == null){
							mapList.put("readUserNames", user.getName());
						}else{
							mapList.put("readUserNames", mapList.get("readUserNames")+","+user.getName());
						}
					}
				}
				
				if (mapList.get("writeUserIds")!= null) {
					String[] writeUserSplit = mapList.get("writeUserIds").toString().subSequence(1, mapList.get("writeUserIds").toString().length()).toString().split(",");
					for(String id :writeUserSplit){
						User user = userService.getEntity(Integer.parseInt(id));
						if(mapList.get("writeUserNames") == null){
							mapList.put("writeUserNames", user.getName());
						}else{
							mapList.put("writeUserNames", mapList.get("readUserNames")+","+user.getName());
						}
					}
				}
				
				if ("0".equals(mapList.get("createUserId").toString())) {
					continue;
				}
				
				User user = userService.getEntity(Integer.parseInt(mapList.get("createUserId").toString()));
				mapList.put("createUserName", user.getName());
			}
			return PageResultEx.ok().data(listpage);
		} catch (Exception e) {
			log.error("试卷分类列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成添加试卷分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/add")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult add(PaperType paperType) {
		try {
			paperTypeService.addAndUpdate(paperType);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成添加试卷分类错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成添加试卷分类错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成修改试卷分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/edit")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult edit(PaperType paperType) {
		try {
			paperTypeService.editAndUpdate(paperType);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成修改试卷分类错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成修改试卷分类错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成删除试卷分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/del")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult del(Integer id) {
		try {
			paperTypeService.delAndUpdate(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成删除试卷分类错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成删除试卷分类错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 获取试卷分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/get")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult get(Integer id) {
		try {
			PaperType entity = paperTypeService.getEntity(id);
			return PageResultEx.ok()
					.addAttr("id", entity.getId())
					.addAttr("name", entity.getName())
					.addAttr("imgId", entity.getImgId())
					.addAttr("createUserId", entity.getCreateUserId())
					.addAttr("createTime", DateUtil.formatDateTime(entity.getCreateTime()))
					.addAttr("readUserIds", entity.getReadUserIds())
					.addAttr("writeUserIds", entity.getWriteUserIds());
		} catch (MyException e) {
			log.error("获取试卷分类错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("获取试卷分类错误：", e);
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
	@RequestMapping("/authUserListpage")
	@ResponseBody
	@RequiresRoles("subAdmin")
	public PageResult authUserListpage(PageIn pageIn, String rw, Integer id) {
		try {
			if(id != null && ValidateUtil.isValid(rw) && "w".equals(rw)){
				pageIn.setOne(id.toString());
			}
			if(id != null && ValidateUtil.isValid(rw) && "r".equals(rw)){
				pageIn.setTwo(id.toString());
			}
			return PageResultEx.ok().data(paperTypeService.authUserListpage(pageIn));
		} catch (MyException e) {
			log.error("权限用户列表错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("权限用户列表错误：", e);
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
	public PageResult auth(Integer id, String readUserIds, String writeUserIds) {
		try {
			paperTypeService.doAuth(id, readUserIds, writeUserIds);
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
