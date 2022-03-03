package com.wcpdoc.exam.core.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wcpdoc.base.service.OrgService;
import com.wcpdoc.base.service.UserService;
import com.wcpdoc.core.dao.BaseDao;
import com.wcpdoc.core.entity.PageIn;
import com.wcpdoc.core.entity.PageOut;
import com.wcpdoc.core.exception.MyException;
import com.wcpdoc.core.service.impl.BaseServiceImp;
import com.wcpdoc.core.util.StringUtil;
import com.wcpdoc.core.util.ValidateUtil;
import com.wcpdoc.exam.core.cache.AutoMarkCache;
import com.wcpdoc.exam.core.dao.ExamDao;
import com.wcpdoc.exam.core.entity.Exam;
import com.wcpdoc.exam.core.entity.ExamType;
import com.wcpdoc.exam.core.entity.MyExam;
import com.wcpdoc.exam.core.entity.MyExamDetail;
import com.wcpdoc.exam.core.entity.MyMark;
import com.wcpdoc.exam.core.entity.Paper;
import com.wcpdoc.exam.core.entity.PaperQuestion;
import com.wcpdoc.exam.core.entity.Question;
import com.wcpdoc.exam.core.entity.RandChapterRules;
import com.wcpdoc.exam.core.service.ExamService;
import com.wcpdoc.exam.core.service.ExamTypeService;
import com.wcpdoc.exam.core.service.MyExamDetailService;
import com.wcpdoc.exam.core.service.MyExamService;
import com.wcpdoc.exam.core.service.MyMarkService;
import com.wcpdoc.exam.core.service.PaperQuestionService;
import com.wcpdoc.exam.core.service.PaperService;
import com.wcpdoc.exam.core.service.PaperTypeService;
import com.wcpdoc.exam.core.service.QuestionService;
import com.wcpdoc.exam.core.service.RandChapterRulesService;

/**
 * 考试服务层实现
 * 
 * v1.0 zhanghc 2017-06-11 09:13:23
 */
@Service
public class ExamServiceImpl extends BaseServiceImp<Exam> implements ExamService {
	@Resource
	private ExamDao examDao;
	@Resource
	private PaperService paperService;
	@Resource
	private QuestionService questionService;
	@Resource
	private PaperTypeService paperTypeService;
	@Resource
	private OrgService orgService;
	@Resource
	private ExamTypeService examTypeService;
	@Resource
	private MyExamService myExamService;
	@Resource
	private MyExamDetailService myExamDetailService;
	@Resource
	private MyMarkService myMarkService;
	@Resource
	private UserService userService;
	@Resource
	private PaperQuestionService paperQuestionService;
	@Resource
	private RandChapterRulesService randChapterRulesService;
	
	@Override
	@Resource(name = "examDaoImpl")
	public void setDao(BaseDao<Exam> dao) {
		super.dao = dao;
	}

	@Override
	public Integer addAndUpdate(Exam exam) {
		// 校验数据有效性
		if (!ValidateUtil.isValid(exam.getName())) {
			throw new MyException("参数错误：name");
		}
		if (!ValidateUtil.isValid(exam.getPaperId())) {
			throw new MyException("参数错误：paperId");
		}
		if (!ValidateUtil.isValid(exam.getStartTime())) {
			throw new MyException("参数错误：startTime");
		}
		if (!ValidateUtil.isValid(exam.getEndTime())) {
			throw new MyException("参数错误：endTime");
		}
//		if (exam.getStartTime().getTime() <= new Date().getTime()) {
//			throw new MyException("考试开始时间必须大于当前时间");
//		}
		if (exam.getStartTime().getTime() >= exam.getEndTime().getTime()) {
			throw new MyException("考试结束时间必须大于考试开始时间");
		}

		Paper paper = paperService.getEntity(exam.getPaperId());
		if (paper.getState() != 1) {
			throw new MyException("试卷未发布");
		}
		if (paper.getMarkType() == 2) {// 如果是自动阅卷类型，没有阅卷开始时间和阅卷结束时间
			if (!ValidateUtil.isValid(exam.getMarkStartTime())) {
				throw new MyException("参数错误：markStartTime");
			}
			if (!ValidateUtil.isValid(exam.getMarkEndTime())) {
				throw new MyException("参数错误：markEndTime");
			}
			if (exam.getMarkStartTime().getTime() <= exam.getEndTime().getTime()) {
				throw new MyException("阅卷开始时间必须大于考试结束时间");
			}
			if (exam.getMarkStartTime().getTime() >= exam.getMarkEndTime().getTime()) {
				throw new MyException("阅卷结束时间必须大于阅卷开始时间");
			}
		}

		// 添加考试
		exam.setCreateUserId(getCurUser().getId());
		exam.setCreateTime(new Date());
		exam.setUpdateUserId(getCurUser().getId());
		exam.setUpdateTime(new Date());
		exam.setState(2);// 草稿
		exam.setMarkState(1);// 标记为未阅卷（考试时间结束，定时任务自动阅卷，标记为已阅）
		exam.setMarkStartTime(paper.getMarkType() == 1 ? null : exam.getMarkStartTime());
		exam.setMarkEndTime(paper.getMarkType() == 1 ? null : exam.getMarkEndTime());
		add(exam);
		return exam.getId(); //快速创建考试需要用id查找信息
	}
	
	@Override
	public void updateAndUpdate(Exam exam) {
		//校验数据有效性
		if (!ValidateUtil.isValid(exam.getName())) {
			throw new MyException("参数错误：name");
		}
		if (!ValidateUtil.isValid(exam.getPaperId())) {
			throw new MyException("参数错误：paperId");
		}
		if (!ValidateUtil.isValid(exam.getStartTime())) {
			throw new MyException("参数错误：startTime");
		}
		if (!ValidateUtil.isValid(exam.getEndTime())) {
			throw new MyException("参数错误：endTime");
		}
//		if (exam.getStartTime().getTime() <= new Date().getTime()) {
//			throw new MyException("考试开始时间必须大于当前时间");
//		}
		if (exam.getStartTime().getTime() >= exam.getEndTime().getTime()) {
			throw new MyException("考试结束时间必须大于考试开始时间");
		}

		Paper paper = paperService.getEntity(exam.getPaperId());
		if (paper.getMarkType() == 2) {// 如果是自动阅卷类型，没有阅卷开始时间和阅卷结束时间
			if (!ValidateUtil.isValid(exam.getMarkStartTime())) {
				throw new MyException("参数错误：markStartTime");
			}
			if (!ValidateUtil.isValid(exam.getMarkEndTime())) {
				throw new MyException("参数错误：markEndTime");
			}
			if (exam.getMarkStartTime().getTime() <= exam.getEndTime().getTime()) {
				throw new MyException("阅卷开始时间必须大于考试结束时间");
			}
			if (exam.getMarkStartTime().getTime() >= exam.getMarkEndTime().getTime()) {
				throw new MyException("阅卷结束时间必须大于阅卷开始时间");
			}
		}
		
		Exam entity = getEntity(exam.getId());
		if(entity.getState() == 0) {
			throw new MyException("已删除");
		}
		if(entity.getState() == 1) {
			throw new MyException("已发布");
		}
		if(entity.getState() == 3) {
			throw new MyException("已归档");
		}
		
		//添加考试
		entity.setName(exam.getName());
		entity.setStartTime(exam.getStartTime());
		entity.setEndTime(exam.getEndTime());
		entity.setMarkStartTime(paper.getMarkType() == 1 ? null : exam.getMarkStartTime());
		entity.setMarkEndTime(paper.getMarkType() == 1 ? null : exam.getMarkEndTime());
		//exam.setMarkState(1);// 不处理
		//entity.setScoreState(exam.getScoreState());
		//entity.setRankState(exam.getRankState());
		//entity.setLoginType(exam.getLoginType());
		//entity.setDescription(exam.getDescription());
		entity.setUpdateTime(new Date());
		entity.setUpdateUserId(getCurUser().getId());
		//entity.setState(null);
		entity.setPaperId(exam.getPaperId());
		//exam.setExamTypeId(null);// 分类不变
		update(entity);
	}
	
	@Override
	public void delAndUpdate(Integer id) {
		Date curTime = new Date();
		Exam exam = examDao.getEntity(id);
		ExamType examType = examTypeService.getEntity(exam.getExamTypeId());
		if(examType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		if(exam.getState() == 1 && exam.getStartTime().getTime() >= curTime.getTime() && exam.getEndTime().getTime() <= curTime.getTime()) {
			throw new MyException("考试未结束");
		}
		
		exam.setState(0);
		exam.setUpdateTime(new Date());
		exam.setUpdateUserId(getCurUser().getId());
		update(exam);
	}
	
	@Override
	public void publish(Integer id) {
		// 校验数据有效性
		Exam exam = examDao.getEntity(id);
		if(exam.getState() == 0) {
			throw new MyException("考试已删除");
		}
		if(exam.getState() == 1) {
			throw new MyException("考试已发布");
		}
		if(exam.getState() == 3) {
			throw new MyException("考试已归档");
		}
//		if (exam.getStartTime().getTime() <= new Date().getTime()) {
//			throw new MyException("考试开始时间必须大于当前时间");
//		}
		if (exam.getStartTime().getTime() >= exam.getEndTime().getTime()) {
			throw new MyException("考试结束时间必须大于考试开始时间");
		}
		Paper paper = paperService.getEntity(exam.getPaperId());
		if (paper.getMarkType() == 2) {// 如果是自动阅卷类型，没有阅卷开始时间和阅卷结束时间
			if (exam.getMarkStartTime().getTime() <= exam.getEndTime().getTime()) {
				throw new MyException("阅卷开始时间必须大于考试结束时间");
			}
			if (exam.getMarkStartTime().getTime() >= exam.getMarkEndTime().getTime()) {
				throw new MyException("阅卷结束时间必须大于阅卷开始时间");
			}
		}
		if (paper.getGenType() == 2) {
			randChapterRulesService.checkRandChapterRules(id);//校验章节随机规则
		}
		
		String[] examUserIds = new String[0];
		Integer[] markUserIds = new Integer[0];
		if (paper.getMarkType() == 2) {//人工
			List<MyMark> myMarkList = myMarkService.getList(exam.getId());
			if (myMarkList != null && myMarkList.size() > 0 ) {
				examUserIds = new String[myMarkList.size()];
				markUserIds = new Integer[myMarkList.size()];
				for (int i = 0; i < myMarkList.size(); i++) {
					markUserIds[i] = myMarkList.get(i).getMarkUserId();
					examUserIds[i] = myMarkList.get(i).getExamUserIds().substring(1, myMarkList.get(i).getExamUserIds().length()-1);
				}
			}
		}

		List<MyExam> myExamList = myExamService.getList(exam.getId()) ;//看看有没有考试人
		if (myExamList != null && myExamList.size() > 0 ) {//如果有删除重新添加试题
			if (paper.getMarkType() == 1) {
				examUserIds = new String[myExamList.size()];
			}

			for(int i = 0; i < myExamList.size(); i++){
				if (paper.getMarkType() == 1) {
					examUserIds[i] = myExamList.get(i).getUserId().toString();
				}
				
				myExamService.del(myExamService.getEntity(myExamList.get(i).getExamId(), myExamList.get(i).getUserId()).getId()); //不修改状态，直接删除 无用的数据
				myExamDetailService.del(myExamList.get(i).getId(), myExamList.get(i).getUserId());
			}
		}
		
		// 发布考试
		exam.setState(1);
		exam.setUpdateUserId(getCurUser().getId());
		exam.setUpdateTime(new Date());
		examDao.update(exam);
		if (exam.getId() != null  && examUserIds.length > 0) {
			updateMarkSet(id, examUserIds, markUserIds); //重新设置考试人 阅卷人
		}
		
		// 标记为需要监听的考试（考试结束自动阅卷）
		AutoMarkCache.put(exam.getId(), exam);
		//if (paper.getMarkType() == 2) {// 智能试卷，考试结束，定时任务就处理完成了
		//	AutoMarkCache.put(exam.getId(), exam.getMarkEndTime());// 为保证同步执行，考试定时任务执行完时，在进行添加
		//}
	}
	
	@Override
	public List<Map<String, Object>> getExamUserList(Integer id) {
		return examDao.getExamUserList(id);
	}

	@Override
	public void updateMarkSet(Integer id, String[] examUserIds, Integer[] markUserIds) {
		// 校验数据有效性
		if (id == null) {
			throw new MyException("参数错误：id");
		}
		
		if (examUserIds == null) {
			throw new MyException("参数错误：examUserIds");
		}
		
		Exam exam = getEntity(id);
		if (exam.getState() == 0) {
			throw new MyException("考试已删除");
		}
//		if (exam.getState() == 2) {
//			throw new MyException("考试未发布");
//		}
		if (exam.getState() == 3) {
			throw new MyException("考试已归档");
		}
		if (exam.getEndTime().getTime() < System.currentTimeMillis()) {
			throw new MyException("考试已结束");
		}
		
		Paper paper = paperService.getEntity(exam.getPaperId());
		if (paper.getMarkType() == 2) {// 如果试卷是智能阅卷类型，没有阅卷用户
			if (markUserIds == null) {
				throw new MyException("参数错误：markUserIds");
			}
			
			if (markUserIds.length != 1 && examUserIds.length != markUserIds.length) {
				throw new MyException("参数错误：markUserIds和examUserIds数量不等");
			} 
		}
		Set<Integer> examUserIdSet = new HashSet<>();
		for (String userIds : examUserIds) {
			for (String userId : userIds.split(",")) {
				int _userId = Integer.parseInt(userId);
				if (examUserIdSet.contains(_userId)) {
					throw new MyException("考试用户重复");
				}
				examUserIdSet.add(_userId);
			}
		}
		Set<Integer> markUserIdSet = new HashSet<>();
		for (Integer userId : markUserIdSet) {
			if (markUserIdSet.contains(userId)) {
				throw new MyException("阅卷用户重复");
			}
			markUserIdSet.add(userId);
		}
		
		// 同步考试人员和阅卷人员到数据库
		if (ValidateUtil.isValid(markUserIds) && markUserIds.length == 1) {
			examUserIds[0] = StringUtil.join(examUserIds);// 按逗号分隔的参数，服务器接收会变成数组
		}
		
		List<MyExam> myExamList = myExamService.getList(id);// 当前考试的人员
		ListIterator<MyExam> myExamListIterator = myExamList.listIterator();
		Date curTime = new Date();
		while (myExamListIterator.hasNext()) {// 同步考试人员信息
			/**
			 * 页面：1,2,3
			 * 数据库：2,3,4
			 * 1添加；4删除；2,3不动
			 */
			MyExam myExam = myExamListIterator.next();
			if (examUserIdSet.contains(myExam.getUserId())) {// 页面有数据库有，不处理
				myExamListIterator.remove();
				examUserIdSet.remove(myExam.getUserId());
			} else {// 页面没有数据库有，删除数据库数据
				myExamDetailService.del(myExam.getExamId(), myExam.getUserId());
				myExamService.del(myExam.getId());
				paperQuestionService.del(myExam.getExamId(), myExam.getUserId());//如果试卷时随机组卷 删除随机试题
			}
		}
		
		List<PaperQuestion> paperQuestionList = null;
		Map<Integer, List<Question>> checkRandChapterRules = null;
		if (paper.getGenType() == 1) {//人工组卷
			paperQuestionList = paperService.getPaperQuestionList(exam.getPaperId());// 试题列表（不能用试题中的试题 分值修改有问题 只能用试卷试题中的试题）
		}
		if (paper.getGenType() == 2) {//随机组卷
			paperQuestionList = paperQuestionService.getChapterList(paper.getId());//章节
			checkRandChapterRules = randChapterRulesService.checkRandChapterRules(paper.getId());// Map<试题分类id, 试题分类下所有的试题>  分页查询出来的
		}
		for(Integer userId : examUserIdSet){
			MyExam myExam = new MyExam();
			myExam.setExamId(id);
			myExam.setUserId(userId);
			myExam.setState(1);// 未考试
			myExam.setMarkState(1);// 未阅卷
			myExam.setUpdateTime(curTime);
			myExam.setUpdateUserId(getCurUser().getId());
			myExamService.add(myExam);// 添加我的考试
			
			if (exam.getState() == 1 && paper.getGenType() == 1) {
				for (PaperQuestion paperQuestion : paperQuestionList) {
					MyExamDetail myExamDetail = new MyExamDetail();
					myExamDetail.setMyExamId(myExam.getId());
					myExamDetail.setExamId(myExam.getExamId());
					myExamDetail.setUserId(myExam.getUserId());
					myExamDetail.setQuestionId(paperQuestion.getQuestionId());
					myExamDetail.setQuestionScore(paperQuestion.getScore());
					myExamDetailService.add(myExamDetail);// 添加我的考试详细
				}
			}
			if (exam.getState() == 1 && paper.getGenType() == 2) {
				for (PaperQuestion chapter : paperQuestionList) {
					for(RandChapterRules randChapterRules : randChapterRulesService.getChapterList(chapter.getPaperId(), chapter.getId())){//章节规则
						// 试题类型、试题难易程度、分值、自能阅卷、个数
						for (int i = 1; i <= randChapterRules.getTotalNumber(); i++) {//随机试题
							List<Question> questionList = checkRandChapterRules.get(randChapterRules.getQuestionTypeId());
							for(int j = 0; j < questionList.size(); j++){
								if ( !(!ValidateUtil.isValid(randChapterRules.getType()) || randChapterRules.getType().equals(questionList.get(j).getType())) 
										&& !(!ValidateUtil.isValid(randChapterRules.getDifficulty()) || randChapterRules.getDifficulty().contains(questionList.get(j).getDifficulty().toString())) 
										&& !(!ValidateUtil.isValid(randChapterRules.getAi()) || randChapterRules.getAi().contains(questionList.get(j).getAi().toString())) ) {
										continue;
									}
								
									PaperQuestion paperQuestion = new PaperQuestion();
									paperQuestion.setUpdateTime(new Date());
									paperQuestion.setUpdateUserId(getCurUser().getId());
									paperQuestion.setQuestionId(questionList.get(j).getId());
									paperQuestion.setType(3);
									paperQuestion.setNo(i);
									paperQuestion.setScore(randChapterRules.getScore());
									paperQuestion.setScoreOptions(randChapterRules.getScoreOptions());
									paperQuestion.setParentId(randChapterRules.getPaperQuestionId());
									paperQuestion.setPaperId(randChapterRules.getPaperId());
									paperQuestion.setExamId(myExam.getExamId());
									paperQuestion.setUserId(userId);
									paperQuestion.setRandChapterRulesId(randChapterRules.getId());
									paperQuestionService.add(paperQuestion);
									
									paperQuestion.setParentSub(String.format("%s%s_", chapter.getParentSub(), paperQuestion.getId()));
									paperQuestionService.update(paperQuestion);
									
									MyExamDetail myExamDetail = new MyExamDetail();
									myExamDetail.setMyExamId(myExam.getId());
									myExamDetail.setExamId(myExam.getExamId());
									myExamDetail.setUserId(myExam.getUserId());
									myExamDetail.setQuestionId(questionList.get(j).getId());
									myExamDetail.setQuestionScore(randChapterRules.getScore());
									myExamDetailService.add(myExamDetail);// 添加我的考试详细
									
									questionList.remove(j);//删除列表中现在
									break;
							}
							Collections.shuffle(questionList);//乱序
							checkRandChapterRules.put(randChapterRules.getQuestionTypeId(), questionList);
						}
					}
				}
			}
		}
		
		if (paper.getMarkType() == 2) {// 如果试卷是人工阅卷，添加阅卷用户
			List<MyMark> myMarkList = myMarkService.getList(id);// 先清理阅卷用户
			for (MyMark myMark : myMarkList) {
				myMarkService.del(myMark.getId());
			}
			
			for (int i = 0; i < markUserIds.length; i++) {
				MyMark myMark = new MyMark();
				myMark.setMarkUserId(markUserIds[i]);
				myMark.setExamUserIds(String.format(",%s,", examUserIds[i]));
				//myMark.setQuestionIds(null);这一版不实现按题阅卷
				myMark.setUpdateUserId(getCurUser().getId());
				myMark.setUpdateTime(curTime);
				myMark.setExamId(id);
				myMarkService.add(myMark);
			}
		}
	}

	public List<Map<String, Object>> getMapList(Integer examTypeId) {
		
		return null;
	}
	
	@Override
	public List<Exam> getList(Integer examTypeId) {
		return examDao.getList(examTypeId);
	}

	@Override
	public PageOut getGradeListpage(PageIn pageIn) {   
		return examDao.getGradeListpage(pageIn);
	}

	@Override
	public List<Exam> getExamList(Integer paperId) {
		return examDao.getExamList(paperId);
	}

	@Override
	public List<Map<String, Object>> getMarkExamUserList(Integer id, Integer markUserId) {
		return examDao.getMarkExamUserList(id, markUserId);
	}

	@Override
	public List<Map<String, Object>> getMarkQuestionList(Integer id, Integer markUserId) {
		return examDao.getMarkQuestionList(id, markUserId);
	}

	@Override
	public List<Exam> getUnMarkList() {
		return examDao.getUnMarkList();
	}

}
