package com.wcpdoc.exam.core.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcpdoc.core.exception.MyException;
import com.wcpdoc.core.util.SpringUtil;
import com.wcpdoc.core.util.ValidateUtil;
import com.wcpdoc.exam.core.cache.AutoMarkCache;
import com.wcpdoc.exam.core.service.MyExamDetailService;

/**
 * 自动阅卷任务 （阅卷结束时间到，自动完成阅卷）
 * 该任务每间隔1秒执行一次
 * 
 * v1.0 chenyun 2021-11-30 14:31:15
 */
public class AutoMarkJob implements Job {
	private static final Logger log = LoggerFactory.getLogger(AutoMarkJob.class);
	private static final MyExamDetailService myExamDetailService = SpringUtil.getBean(MyExamDetailService.class);
	private static boolean TASK_RUN = false;// 上一次任务是否正在运行中
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// 校验数据有效性
		try {
			if (TASK_RUN) {
				log.info("自动阅卷：上一次任务正在运行中，本次不做任何操作");
				return;
			}
			TASK_RUN = true;
			
			List<Integer> examList = AutoMarkCache.getList();
			if (!ValidateUtil.isValid(examList)) {
				return;
			}
			
			for (Integer examId : examList) {
				try {
					if (AutoMarkCache.get(examId).getTime() > System.currentTimeMillis()) {// 如果阅卷未结束，不处理
						continue;
					}
					
					// 完成阅卷
					AutoMarkCache.del(examId);// 先清理掉缓存，定时任务不在监控（如果完成阅卷报错，在执行一遍也报错，通过页面添加按钮，人工修复后手动执行）
					myExamDetailService.doMark(examId);// 完成阅卷
				} catch (MyException e) {// 一个有问题，不要影响其他任务执行。
					log.error("自动阅卷错误：{}", e.getMessage());
					continue;
				} catch (Exception e) {
					log.error("自动阅卷错误：", e);
					continue;
				}
			}
		} catch (Exception e) {
			log.error("自动阅卷错误：", e);
		} finally {
			TASK_RUN = false;
		}
	}
}
