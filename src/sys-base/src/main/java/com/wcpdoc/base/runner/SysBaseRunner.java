package com.wcpdoc.base.runner;

import java.io.File;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.wcpdoc.base.cache.ParmCache;
import com.wcpdoc.base.entity.Parm;
import com.wcpdoc.base.service.ParmService;
import com.wcpdoc.core.util.SpringUtil;

/**
 * 系统基础初始化
 * 
 * v1.0 zhanghc 2019年9月29日下午2:32:16
 */
@Component
public class SysBaseRunner implements ApplicationRunner {
	private static final Logger log = LoggerFactory.getLogger(SysBaseRunner.class);
	@Resource
	private ParmService parmService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Parm parm = parmService.getEntity(1);
		if (parm == null) {
			parm = new Parm();
			parm.setId(1);
			parm.setUpdateUserId(1);
			parm.setUpdateTime(new Date());
			parm.setFileUploadDir(String.format("bak%sfile", File.separator));
			parm.setDbBakDir(String.format("bak%sdb", File.separator));
			parm.setPwdType(2);
			parm.setPwdValue("111111");
			SpringUtil.getBean(ParmService.class).add(parm);
			log.info("系统基础初始化：初始化系统参数表");
		}
		
		log.info("系统基础初始化：缓存系统参数");
		ParmCache.flushCache(parm);
	}
}
