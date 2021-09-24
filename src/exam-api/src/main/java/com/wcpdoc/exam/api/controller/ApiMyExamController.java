package com.wcpdoc.exam.api.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wcpdoc.exam.base.entity.Parm;
import com.wcpdoc.exam.base.entity.User;
import com.wcpdoc.exam.base.service.ParmService;
import com.wcpdoc.exam.base.service.UserService;
import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.MyExam;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.entity.QuestionAnswer;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.MyExamDetailService;
import com.wcpdoc.exam.core.service.MyExamService;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.notify.exception.NotifyException;
import com.wcpdoc.exam.notify.service.NotifyService;

/**
 * 我的考试控制层
 * 
 * v1.0 zhanghc 2017-06-11 09:13:23
 */
@Controller
@RequestMapping("/api/myExam")
public class ApiMyExamController extends BaseController{
	private static final Logger log = LoggerFactory.getLogger(ApiMyExamController.class);
	
	@Resource
	private MyExamService myExamService;
	@Resource
	private MyExamDetailService myExamDetailService;
	@Resource
	private UserService userService;
	@Resource
	private NotifyService notifyService;
	@Resource
	private ParmService parmService;
	
	/**
	 * 我的考试列表
	 * 
	 * v1.0 zhanghc 2017-05-25 16:34:59
	 * @return pageOut
	 */
	@RequestMapping("/listpage")
	@ResponseBody
	@RequiresRoles(value={"user","subAdmin"},logical = Logical.OR)
	public PageResult listpage() {
		try {
			PageIn pageIn = new PageIn(request);
			pageIn.addAttr("curUserId", getCurUser().getId());
			return PageResultEx.ok().data(myExamService.getListpage(pageIn));
		} catch (Exception e) {
			log.error("我的考试列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 考试答案列表
	 * 
	 * v1.0 zhanghc 2018年11月24日上午9:13:22
	 * @param id
	 * @return PageResult
	 */
	@RequestMapping("/answerList")
	@ResponseBody
	@RequiresRoles(value={"user","subAdmin"},logical = Logical.OR)
	public PageResult answerList(Integer id) {
		try {
			List<Map<String, Object>> list = myExamDetailService.getAnswerList(id);
			for (Map<String, Object> map : list) {
				map.put("myExamDetailId", map.remove("id"));// 前缀为myExamDetail，默认为id有歧义。
				map.put("answers", new QuestionAnswer().getAnswers((Integer)map.get("questionType"), (String)map.remove("answer")));// 如果没有值，页面也返回字段
			}
			
			return PageResultEx.ok().data(list);
		} catch (MyException e) {
			log.error("考试答案列表错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("考试答案列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 阅卷考试答案列表
	 * 
	 * v1.0 chenyun 2021年7月29日下午6:04:37
	 * @param userId
	 * @param examId
	 * @return PageResult
	 */
	@RequestMapping("/markAnswerList")
	@ResponseBody
	@RequiresRoles(value={"subAdmin","user"},logical = Logical.OR)
	public PageResult markAnswerList(Integer userId, Integer examId) {
		try {
			List<Map<String, Object>> list = myExamDetailService.getMarkAnswerList(userId, examId);
			for (Map<String, Object> map : list) {
				map.put("myExamDetailId", map.remove("id"));// 前缀为myExamDetail，默认为id有歧义。
				map.put("answers", new QuestionAnswer().getAnswers((Integer)map.get("questionType"), (String)map.remove("answer")));// 如果没有值，页面也返回字段
			}
			
			return PageResultEx.ok().data(list);
		} catch (MyException e) {
			log.error("考试答案列表错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("考试答案列表错误：", e);
			return PageResult.err();
		}
	}
	
//	/**
//	 * 到达考试页面
//	 * 
//	 * v1.0 zhanghc 2017-05-25 16:34:59
//	 * @param model
//	 * @param myExamId
//	 * @return String
//	 */
//	@RequestMapping("/toExam")
//	@RequiresRoles(value={"user"},logical = Logical.OR)
//	public String toExam(Model model, Integer myExamId) {
//		try {
//			//校验数据有效性
//			MyExam myExam = myExamService.getEntity(myExamId);
//			if(myExam == null) {
//				throw new MyException("参数错误：myExamId");
//			}
//			if (myExam.getUserId().intValue() != getCurUser().getId()) {
//				throw new MyException("未参与该考试！");
//			}
//			
//			Exam exam = examService.getEntity(myExam.getExamId());
//			if(exam.getState() == 0) {
//				throw new MyException("考试已删除！");
//			}
//			if(exam.getState() == 2) {
//				throw new MyException("考试未发布！");
//			}
//			if(exam.getStartTime().getTime() > (new Date().getTime())) {
//				throw new MyException("考试未开始！");
//			}
//			if(exam.getEndTime().getTime() < (new Date().getTime() - 30000)){//预留30秒网络延时
//				throw new MyException("考试已结束！");
//			}
//			
//			// 试卷信息
//			model.addAttribute("exam", exam);//考试信息
//			
//			Paper paper = paperService.getEntity(exam.getPaperId());//试卷信息
//			model.addAttribute("paper", paper);
//			
//			List<PaperQuestionEx> paperQuestionExList = paperService.getPaperList(exam.getPaperId());//试题信息
//			model.addAttribute("paperQuestionExList", paperQuestionExList);
//			
//			model.addAttribute("myExam", myExam);// 我的考试信息
//			
//			List<MyExamDetail> myExamDetailList = myExamDetailService.getList(myExamId);//用户已做答案信息
//			Map<Long, MyExamDetail> myExamDetailMap = new HashMap<Long, MyExamDetail>();
//			for(MyExamDetail myExamDetail : myExamDetailList) {
//				myExamDetailMap.put(myExamDetail.getQuestionId().longValue(), myExamDetail);
//			}
//			model.addAttribute("myExamDetailMap", myExamDetailMap);
//			
//			model.addAttribute("answer", true);// 控制页面展示那部分
//			
//			// 标记为考试中
//			if (myExam.getState() == 1) {
//				myExam.setState(2);
//			}
//			if (myExam.getAnswerStartTime() == null) {
//				myExam.setAnswerStartTime(new Date());
//			}
//			myExamService.update(myExam);
//			return "exam/myExam/myExamPaper";
//		} catch (Exception e) {
//			log.error("到达考试页面错误：", e);
//			model.addAttribute("message", e.getMessage());
//			return "exam/error";
//		}
//	}
	
	/**
	 * 更新答案
	 * 
	 * v1.0 zhanghc 2017年6月26日下午12:30:20
	 * @param myExamDetailId
	 * @param answers
	 * @return PageResult
	 */
	@RequestMapping("/updateAnswer")
	@ResponseBody
	@RequiresRoles(value={"user","subAdmin"},logical = Logical.OR)
	public PageResult updateAnswer(Integer myExamDetailId, String[] answers, Integer fileId) {
		try {
			myExamService.updateAnswer(myExamDetailId, answers, fileId);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("更新答案错误：", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("更新答案错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 完成交卷
	 * 
	 * v1.0 zhanghc 2017年6月26日下午12:30:20
	 * @param myExamId
	 * @return PageResult
	 */
	@RequestMapping("/doAnswer")
	@ResponseBody
	@RequiresRoles(value={"user","subAdmin"},logical = Logical.OR)
	public PageResult doAnswer(Integer myExamId) {
		try {
			myExamService.doAnswer(myExamId);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("完成交卷错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("完成交卷错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 考试排行
	 * 
	 * v1.0 chenyun 2021年3月23日下午4:07:48
	 * @param year
	 * @param month
	 * @return PageResult
	 */
	@RequestMapping("/rankingPage")
	@ResponseBody
	@RequiresRoles(value={"user","subAdmin"},logical = Logical.OR)
	public PageResult rankingPage() {
		try {
			return PageResultEx.ok().data(myExamService.getRankingPage(new PageIn(request)));
		} catch (MyException e) {
			log.error("考试时间表错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("考试时间表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 在线请求
	 * 
	 * v1.0 chenyun 2021年9月7日下午4:03:28
	 * @return PageResult
	 */
	@RequestMapping("/onLine")
	@ResponseBody
	@RequiresRoles(value={"user","subAdmin"},logical = Logical.OR)
	public PageResult onLine() {
		try {
			return PageResult.ok();
		} catch (Exception e) {
			log.error("在线请求错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 邮件通知
	 * 
	 * v1.0 zhanghc 2018年11月24日上午9:13:22
	 * @param id
	 * @return PageResult
	 */
	@RequestMapping("/email")
	@ResponseBody
	@RequiresRoles(value={"user","subAdmin"},logical = Logical.OR)
	public PageResult email(Integer examId) {
		try {
			Parm parm = parmService.getEntity(1);
			List<MyExam> list = myExamService.getList(examId);
			for(MyExam myExam : list){
				User user = userService.getEntity(myExam.getUserId());
				if (!ValidateUtil.isValid(user.getEmail())) {
					continue;
				}
				notifyService.pushEmail(parm.getEmailUserName(), user.getEmail(), "考试邮箱通知！", user.getName()+"-昵称。");
			}
			return PageResult.ok();
		} catch (NotifyException e) {
			log.error("邮件通知错误：", e);
			return PageResult.err().msg(e.getMessage());
		} catch (MyException e) {
			log.error("邮件通知错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("邮件通知错误：", e);
			return PageResult.err();
		}
	}
}