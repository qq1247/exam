package com.wcpdoc.exam.api.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.util.DateUtil;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.quartz.entity.Cron;
import com.wcpdoc.exam.quartz.service.CronService;
import com.wcpdoc.exam.quartz.util.QuartzUtil;

/**
 * 定时任务控制层
 * 
 * v1.0 zhanghc 2019-07-29 10:38:17
 */
@Controller
@RequestMapping("/api/cron")
public class ApiCronController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ApiCronController.class);

	@Resource
	private CronService cronService;

	/**
	 * 定时任务列表
	 * 
	 * v1.0 zhanghc 2019-07-29 10:38:17
	 * 
	 * @param pageIn
	 * @return PageOut
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(PageIn pageIn, String name) {
		try {
			if (ValidateUtil.isValid(name)) {
				pageIn.setTwo(name);
			}
			
			PageOut pageOut = cronService.getListpage(pageIn);
			List<Map<String, Object>> list = pageOut.getRows();
			for (Map<String, Object> map : list) {
				if ("1".equals(map.get("STATE").toString())) {
					map.put("STATE_NAME", "启动");
				} else if ("2".equals(map.get("STATE").toString())) {
					map.put("STATE_NAME", "停止");
				}
				
				String cron = map.get("CRON").toString();
				List<Date> timeList = QuartzUtil.getRecentTriggerTime(cron, 3);
				StringBuilder timeStr = new StringBuilder();
				for (Date date : timeList) {
					timeStr.append(DateUtil.formatDateCustom(date, DateUtil.FORMAT_DATE_TIME)).append("；");
				}
				map.put("RECENT_TRIGGER_TIME", timeStr.toString());
			}
			return PageResultEx.ok().data(pageOut);
		} catch (Exception e) {
			log.error("定时任务列表错误：", e);
			return PageResult.err();
		}
	}

	/**
	 * 完成添加定时任务
	 * 
	 * v1.0 zhanghc 2019-07-29 10:38:17
	 * 
	 * @param cron
	 * @return PageResult
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/add")
	@ResponseBody
	public PageResult add(Cron cron) {
		try {
			cron.setUpdateUserId(getCurUser().getId());
			cron.setUpdateTime(new Date());
			// 校验数据有效性
			if (!ValidateUtil.isValid(cron.getJobClass())) {
				throw new MyException("参数错误：jobClass");
			}
			if (!ValidateUtil.isValid(cron.getCron())) {
				throw new MyException("参数错误：cron");
			}

			Class<Job> jobClass = null;
			try {
				jobClass = (Class<Job>) Class.forName(cron.getJobClass());
			} catch (ClassNotFoundException e) {
				throw new MyException("无法实例化：" + cron.getJobClass());
			}
			if (!Job.class.isAssignableFrom(jobClass)) {
				throw new MyException("未实现Job接口：" + cron.getJobClass());
			}
			if (!QuartzUtil.validExpression(cron.getCron())) {
				throw new MyException("cron表达式错误：" + cron.getCron());
			}

			// 添加定时任务，默认不启动
			cron.setState(2);
			cronService.add(cron);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成添加定时任务错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成添加定时任务错误：", e);
			return PageResult.err();
		}
	}

	/**
	 * 完成修改定时任务
	 * 
	 * v1.0 zhanghc 2019-07-29 10:38:17
	 * 
	 * @param cron
	 * @return PageResult
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public PageResult edit(Cron cron) {
		try {
			cronService.updateAndUpdate(cron);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成修改定时任务错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成修改定时任务错误：", e);
			return PageResult.err();
		}
	}

	/**
	 * 完成删除定时任务
	 * 
	 * v1.0 zhanghc 2019-07-29 10:38:17
	 * 
	 * @param ids
	 * @return PageResult
	 */
	@RequestMapping("/del")
	@ResponseBody
	public PageResult del(Integer id) {
		try {
			cronService.delAndUpdate(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成删除定时任务错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成删除定时任务错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 启动任务
	 * 
	 * v1.0 zhanghc 2019年9月12日下午5:30:34
	 * @param ids
	 * @return PageResult
	 */
	@RequestMapping("/startTask")
	@ResponseBody
	public PageResult startTask(Integer id) {
		try {
			cronService.startTask(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("启动任务错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("启动任务错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 停止任务
	 * 
	 * v1.0 zhanghc 2019年9月12日下午5:30:34
	 * @param ids
	 * @return PageResult
	 */
	@RequestMapping("/stopTask")
	@ResponseBody
	public PageResult stopTask(Integer id) {
		try {
			cronService.stopTask(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("停止任务错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("停止任务错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 执行一次
	 * 
	 * v1.0 zhanghc 2019年9月12日下午5:30:34
	 * @param ids
	 * @return PageResult
	 */
	@RequestMapping("/runOnceTask")
	@ResponseBody
	public PageResult runOnceTask(Integer id) {
		try {
			cronService.runOnceTask(id);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("执行一次错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("执行一次错误：", e);
			return PageResult.err();
		}
	}
}
