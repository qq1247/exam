package com.wcpdoc.api.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.base.entity.Parm;
import com.wcpdoc.base.service.ParmService;
import com.wcpdoc.core.controller.BaseController;
import com.wcpdoc.core.entity.PageResult;
import com.wcpdoc.core.entity.PageResultEx;
import com.wcpdoc.core.exception.MyException;
import com.wcpdoc.core.util.ValidateUtil;
/**
 * 参数控制层
 * 
 * v1.0 chenyun 2021-03-04 15:02:18
 */
@Controller
@RequestMapping("/api/parm")
public class ApiParmController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ApiParmController.class);
	
	@Resource
	private ParmService parmService;
	
	/**
	 * 自定义logo、单位名称
	 * 
	 * v1.0 chenyun 2021-03-04 15:02:18
	 * @return pageOut
	 */
	@RequestMapping("/logo")
	@ResponseBody
	public PageResult logo(Parm parm) {
		try {
			parmService.editLogo(parm);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("修改参数错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("修改参数错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 系统参数邮件
	 * 
	 * v1.0 wjj 2021年11月8日下午1:25:33
	 * 
	 * @param parm
	 * @return PageResult
	 */
	@RequestMapping("/email")
	@ResponseBody
	public PageResult email(String host, String userName, String pwd, String protocol, String encode) {
		try {
			if (!ValidateUtil.isValid(host)) {
				return PageResult.err().msg("参数错误：host");
			}
			if (!ValidateUtil.isValid(userName)) {
				return PageResult.err().msg("参数错误：userName");
			}
			if (!ValidateUtil.isValid(pwd)) {
				return PageResult.err().msg("参数错误：pwd");
			}
			if (!ValidateUtil.isValid(protocol)) {
				return PageResult.err().msg("参数错误：protocol");
			}
			if (!ValidateUtil.isValid(encode)) {
				return PageResult.err().msg("参数错误：encode");
			}
			
			Parm entity = parmService.get();
			entity.setEmailHost(host);
			entity.setEmailUserName(userName);
			entity.setEmailPwd(pwd);
			entity.setEmailProtocol(protocol);
			entity.setEmailEncode(encode);
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(getCurUser().getId());
			parmService.update(entity);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("添加参数错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("添加参数错误：", e);
			return PageResult.err();
		}
	}

	/**
	 * 系统参数上传附件目录
	 * 
	 * v1.0 wjj 2021年11月8日下午1:25:33
	 * 
	 * @param uploadDir
	 * @return PageResult
	 */
	@RequestMapping("/file")
	@ResponseBody
	public PageResult file(String uploadDir) {
		try {
			Parm parm = parmService.get();
			parm.setFileUploadDir(uploadDir);
			parmService.updateAndUpdate(parm);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("上传附件目录错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("上传附件目录错误：", e);
			return PageResult.err();
		}
	}

	/**
	 * 系统参数数据库备份目录
	 * “剪切之前的文件到新位置 对象要克隆” 暂时未实现
	 * 
	 * v1.0 wjj 2021年11月8日下午1:35:33
	 * 
	 * @param dbBakDir
	 * @return PageResult
	 */
	@RequestMapping("/db")
	@ResponseBody
	public PageResult db(String dbBakDir) {
		try {
			Parm parm = parmService.get();
			parm.setDbBakDir(dbBakDir);
			parmService.updateAndUpdate(parm);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("数据库备份目录错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("数据库备份目录错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 系统参数数据库备份目录
	 * “剪切之前的文件到新位置 对象要克隆” 暂时未实现
	 * 
	 * v1.0 wjj 2021年11月8日下午1:35:33
	 * 
	 * @param oldDir
	 * @param newDir
	 * @return PageResult
	 */
	@RequestMapping("/get")
	@ResponseBody
	public PageResult get() {
		try {
			Parm parm = parmService.get();
			return PageResultEx.ok()
					.addAttr("emailHost", parm.getEmailHost())
					.addAttr("emailUserName", parm.getEmailUserName())
					.addAttr("emailPwd", parm.getEmailPwd())
					.addAttr("emailProtocol", parm.getEmailProtocol())
					.addAttr("emailEncode", parm.getEmailEncode())
					.addAttr("orgLogo", parm.getOrgLogo())
					.addAttr("orgName", parm.getOrgName())
					.addAttr("fileUploadDir", parm.getFileUploadDir())
					.addAttr("dbBakDir", parm.getDbBakDir())
					;
		} catch (MyException e) {
			log.error("数据库备份目录错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("数据库备份目录错误：", e);
			return PageResult.err();
		}
	}
}