package com.wcpdoc.exam.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
	@RequiresRoles("OP")
	public PageResult listpage(PageIn pageIn, String name, String userName) {
		try {
			if (ValidateUtil.isValid(name)) {
				pageIn.setTwo(name);
			}
			if (ValidateUtil.isValid(userName)) {
				pageIn.setThree(userName);
			}
			
			PageOut listpage = paperTypeService.getListpage(pageIn);
			List<Map<String, Object>> rows = listpage.getRows();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for(Map<String, Object> mapList : rows){
				if ("0".equals(mapList.get("createUserId").toString())) {
					continue;
				}
				
				User user = userService.getEntity(Integer.parseInt(mapList.get("createUserId").toString()));
				mapList.put("createUserName", user.getName());
				list.add(mapList);
			}
			listpage.setRows(list);
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
	@RequiresRoles("OP")
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
	 * 获取试卷分类
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * 
	 * @return pageOut
	 */
	@RequestMapping("/get")
	@ResponseBody
	@RequiresRoles("OP")
	public PageResult get(Integer id) {
		try {
			PaperType entity = paperTypeService.getEntity(id);
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
			log.error("获取试卷分类错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("获取试卷分类错误：", e);
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
	@RequiresRoles("OP")
	public PageResult edit(PaperType paperType) {
		try {
			//校验数据有效性
			if(!ValidateUtil.isValid(paperType.getName())) {
				throw new MyException("参数错误：name");
			}
			if(paperTypeService.existName(paperType)) {
				throw new MyException("名称已存在！");
			}
			
			//修改试卷分类
			PaperType entity = paperTypeService.getEntity(paperType.getId());
			entity.setName(paperType.getName());
			entity.setImgId(paperType.getImgId());
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(getCurUser().getId());
			paperTypeService.update(entity);
			
			//保存图片
			//fileService.doUpload(paperType.getImgId());
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
	@RequiresRoles("OP")
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
	@RequiresRoles("OP")
	public PageResult auth(Integer id, String readUserIds, String writeUserIds, boolean rwState) {
		try {
			paperTypeService.doAuth(id, readUserIds, writeUserIds, rwState);
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
