package com.wcpdoc.exam.api.controller;
 
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.core.controller.BaseController;
import com.wcpdoc.core.entity.PageIn;
import com.wcpdoc.core.entity.PageResult;
import com.wcpdoc.core.entity.PageResultEx;
import com.wcpdoc.core.exception.MyException;
import com.wcpdoc.exam.report.service.ReportService;
import com.wcpdoc.exam.report.service.ServerPramService;
 
/**
 * 成绩报表控制层
 * 
 * v1.0 zhanghc 2017年8月29日下午2:03:36
 */
@Controller
@RequestMapping("/api/report")
public class ApiReportController extends BaseController{
    private static final Logger log = LoggerFactory.getLogger(ApiReportController.class);
    
    @Resource
    private ReportService reportService;
    @Resource
    private ServerPramService serverParmService;
    
    /**
     * 首页用户
     * 
     * v1.0 chenyun 2021年12月10日上午10:14:34
     * @return PageResult
     */
    @RequestMapping("/home/user")
    @ResponseBody
    public PageResult homeUser() {
        try {
            return PageResultEx.ok().data(reportService.homeUser());
        } catch (MyException e) {
            log.error("首页用户统计错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("首页用户统计错误：", e);
            return PageResult.err();
        }
    }
    
    /**
     * 首页子管理
     * 
     * v1.0 chenyun 2021年12月10日上午10:14:34
     * @return PageResult
     */
    @RequestMapping("/home/subAdmin")
    @ResponseBody
    public PageResult homeSubAdmin() {
        try {
            return PageResultEx.ok().data(reportService.homeSubAdmin());
        } catch (MyException e) {
            log.error("首页子管理统计错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("首页子管理统计错误：", e);
            return PageResult.err();
        }
    }
    
    /**
     * 首页管理
     * 
     * v1.0 chenyun 2021年12月10日上午10:14:34
     * @return PageResult
     */
    @RequestMapping("/home/admin")
    @ResponseBody
    public PageResult homeAdmin() {
        try {
            return PageResultEx.ok().data(reportService.homeAdmin());
        } catch (MyException e) {
            log.error("首页管理员统计错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("首页管理员统计错误：", e);
            return PageResult.err();
        }
    }
    
    /**
     * 服务器参数
     * 
     * v1.0 chenyun 2021年12月10日上午10:14:34
     * @return PageResult
     */
    @RequestMapping("/server/parm")
    @ResponseBody
    public PageResult serverParm() {
        try {
            return PageResultEx.ok().data(serverParmService.getList());
        } catch (MyException e) {
            log.error("首页服务器参数统计错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("首页服务器参数统计错误：", e);
            return PageResult.err();
        }
    }
    
    /**
     * 慢接口日志
     * 
     * v1.0 chenyun 2021-12-15 13:49:29
     * @return PageResult
     */
    @RequestMapping("/server/log")
    @ResponseBody
    public PageResult serverLog() {
        try {
           return PageResultEx.ok().data(reportService.serverLog());
        } catch (MyException e) {
            log.error("首页慢接口日志统计错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("首页慢接口日志统计错误：", e);
            return PageResult.err();
        }
    }
    
    /**
     * 试题统计
     * 
     * v1.0 chenyun 2021-12-15 13:44:47
     * @param id
     * @return PageResult
     */
    @RequestMapping("/question/statis")
    @ResponseBody
    public PageResult questionStatis(Integer questionTypeId) {
        try {
            return PageResultEx.ok().data(reportService.questionStatis(questionTypeId));
        } catch (MyException e) {
            log.error("试题统计错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("试题统计错误：", e);
            return PageResult.err();
        }
    }
    
    /**
     * 考试统计
     * 
     * v1.0 chenyun 2021-12-15 13:44:47
     * @param id
     * @return PageResult
     */
    @RequestMapping("/exam/statis")
    @ResponseBody
    public PageResult examStatis(Integer examId) {
        try {
            return PageResultEx.ok().data(reportService.examStatis(examId));
        } catch (MyException e) {
            log.error("考试统计错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("考试统计错误：", e);
            return PageResult.err();
        }
    }
    
    /**
     * 考试排名
     * 
     * v1.0 chenyun 2021-12-15 13:44:47
     * @return PageResult
     */
    @RequestMapping("/myExam/listpage")
    @ResponseBody
    public PageResult myExamListpage() {
        try {
            return PageResultEx.ok().data(reportService.myExamListpage(new PageIn(request)));
        } catch (MyException e) {
            log.error("考试排名错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("考试排名错误：", e);
            return PageResult.err();
        }
    }
    
    /**
     * 错题分析
     * 
     * v1.0 chenyun 2021-12-15 13:44:47
     * @return PageResult
     */
    @RequestMapping("/question/listpage")
    @ResponseBody
    public PageResult questionListpage() {
        try {
            return PageResultEx.ok().data(reportService.questionListpage(new PageIn(request)));
        } catch (MyException e) {
            log.error("错题分析统计错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("错题分析统计错误：", e);
            return PageResult.err();
        }
    }
    
    /**
     * 分数统计
     * 
     * v1.0 zhanghc 2018年11月24日上午9:13:22
     * @param id
     * @return PageResult
     */
/*    @RequestMapping("/count")
    @ResponseBody
    public PageResult count(Integer examId) {
        try {
            return PageResultEx.ok().data(reportService.count(examId));
        } catch (MyException e) {
            log.error("分数统计错误：{}", e.getMessage());
            return PageResult.err().msg(e.getMessage());
        } catch (Exception e) {
            log.error("分数统计错误：", e);
            return PageResult.err();
        }
    }*/
}