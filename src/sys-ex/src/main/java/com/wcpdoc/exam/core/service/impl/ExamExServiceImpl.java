package com.wcpdoc.exam.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wcpdoc.core.dao.BaseDao;
import com.wcpdoc.core.exception.MyException;
import com.wcpdoc.core.service.impl.BaseServiceImp;
import com.wcpdoc.core.util.BigDecimalUtil;
import com.wcpdoc.core.util.StringUtil;
import com.wcpdoc.core.util.ValidateUtil;
import com.wcpdoc.exam.core.entity.Exam;
import com.wcpdoc.exam.core.entity.MyExam;
import com.wcpdoc.exam.core.entity.MyExamDetail;
import com.wcpdoc.exam.core.entity.MyMark;
import com.wcpdoc.exam.core.entity.PaperQuestion;
import com.wcpdoc.exam.core.entity.PaperQuestionAnswer;
import com.wcpdoc.exam.core.entity.PaperQuestionRule;
import com.wcpdoc.exam.core.entity.Question;
import com.wcpdoc.exam.core.entity.QuestionAnswer;
import com.wcpdoc.exam.core.entity.ex.Chapter;
import com.wcpdoc.exam.core.entity.ex.MyPaper;
import com.wcpdoc.exam.core.entity.ex.MyQuestion;
import com.wcpdoc.exam.core.service.ExamExService;
import com.wcpdoc.exam.core.service.ExamService;
import com.wcpdoc.exam.core.service.MyExamDetailService;
import com.wcpdoc.exam.core.service.MyExamService;
import com.wcpdoc.exam.core.service.MyMarkService;
import com.wcpdoc.exam.core.service.PaperQuestionAnswerService;
import com.wcpdoc.exam.core.service.PaperQuestionRuleService;
import com.wcpdoc.exam.core.service.PaperQuestionService;
import com.wcpdoc.exam.core.service.PaperService;
import com.wcpdoc.exam.core.service.QuestionAnswerService;
import com.wcpdoc.exam.core.service.QuestionService;

/**
 * 考试服务层实现
 * 
 * v1.0 zhanghc 2017-06-11 09:13:23
 */
@Service
public class ExamExServiceImpl extends BaseServiceImp<Exam> implements ExamExService {
	@Resource
	private ExamService examService;
	@Resource
	private PaperService paperService;
	@Resource
	private QuestionService questionService;
	@Resource
	private MyMarkService myMarkService;
	@Resource
	private MyExamService myExamService;
	@Resource
	private MyExamDetailService myExamDetailService;
	@Resource
	private PaperQuestionService paperQuestionService;
	@Resource
	private PaperQuestionRuleService paperQuestionRuleService;
	@Resource
	private QuestionAnswerService questionAnswerService;
	@Resource
	private PaperQuestionAnswerService paperQuestionAnswerService;
	
	@Override
	public void setDao(BaseDao<Exam> dao) {
	}

	@Override
	public void publish(Exam exam) {
		// 生成用户试卷
		List<MyMark> myMarkList = myMarkService.getList(exam.getId());
		if (!ValidateUtil.isValid(myMarkList)) {
			return;// 先发布后添加用户，是没有数据的
		}
		
		MyPaper myPaper = paperService.getMyPaper(exam.getId());
		for (MyMark myMark : myMarkList) {
			Integer[] examUserIdArr = myMark.getExamUserIdArr();
			for(Integer userId : examUserIdArr) {
				generateUserPaper(exam, userId, myPaper);
			}
		}
	}
	
	private void generateUserPaper(Exam exam, Integer userId, MyPaper myPaper) {
		MyExam myExam = new MyExam();
		myExam.setExamId(exam.getId());
		myExam.setUserId(userId);
		myExam.setState(1);// 未考试
		myExam.setMarkState(1);// 未阅卷
		myExam.setUpdateTime(new Date());
		myExam.setUpdateUserId(getCurUser().getId());
		myExamService.add(myExam);// 添加我的考试
		
		for (Chapter chapter : myPaper.getChapterList()) {
			for (MyQuestion myQuestion : chapter.getMyQuestionList()) {
				MyExamDetail myExamDetail = new MyExamDetail();
				myExamDetail.setMyExamId(myExam.getId());
				myExamDetail.setExamId(myExam.getExamId());
				myExamDetail.setUserId(myExam.getUserId());
				myExamDetail.setQuestionId(myQuestion.getQuestion().getId());
				myExamDetail.setQuestionScore(myQuestion.getAttr().getScore());// 用组卷时设置的分数
				myExamDetailService.add(myExamDetail);// 添加我的考试详细
			}
		}
	}

	@Override
	public void publishForRand(Exam exam) {
		// 校验数据有效性
		List<PaperQuestion> chapterList = paperQuestionService.getChapterList(exam.getPaperId());
		Map<Integer, List<PaperQuestionRule>> ruleCache = getRuleCache(chapterList);
		Map<Integer, List<Question>> questionListCache = getQuestionListCache(ruleCache);
		publishForRandValid(chapterList, ruleCache, questionListCache);
		
		// 获取考试用户列表
		List<MyMark> myMarkList = myMarkService.getList(exam.getId());
		for (MyMark myMark : myMarkList) {
			for (Integer userId : myMark.getExamUserIdArr()) {
				// 生成用户试卷
				MyPaper myPaper = generatePaperForRand(exam, ruleCache, questionListCache, userId);
				generateUserPaper(exam, userId, myPaper);
			}
		}
	}

	private MyPaper generatePaperForRand(Exam exam, Map<Integer, List<PaperQuestionRule>> ruleCache,
			Map<Integer, List<Question>> questionListCache, Integer userId) {
		// 查找符合规则的试题
		MyPaper myPaper = new MyPaper();
		Set<Integer> usedQuestion = new HashSet<Integer>();
		for(List<PaperQuestionRule> ruleList : ruleCache.values()) {// 循环章节
			Chapter chapter = new Chapter();
			myPaper.getChapterList().add(chapter);
			for (PaperQuestionRule rule : ruleList) {// 循环单章节下规则
				int remainQuestionNum = rule.getNum();
				Collections.shuffle(questionListCache.get(rule.getQuestionTypeId()));// 打乱顺序模拟随机
				for(Question question : questionListCache.get(rule.getQuestionTypeId())) {
					if (rule.getType().intValue() != question.getType().intValue()
							|| !rule.getDifficultys().contains(question.getDifficulty().toString()) 
							|| !rule.getAis().contains(question.getAi().toString()) ) {// 当前试题不符合规则，不添加
							continue;
					}
					
					if (usedQuestion.contains(question.getId())) {// 添加过，不添加
						continue;
					}
					
					// 添加试题
					PaperQuestion attr = new PaperQuestion();
					attr.setQuestionId(question.getId());
					attr.setType(3);
					attr.setScore(rule.getScore());
					attr.setAiOptions(rule.getAiOptions());
					attr.setParentId(rule.getPaperQuestionId());
					attr.setPaperId(rule.getPaperId());
					attr.setExamId(exam.getId());
					attr.setUserId(userId);
					attr.setNo(remainQuestionNum);
					attr.setUpdateTime(new Date());
					attr.setUpdateUserId(getCurUser().getId());
					paperQuestionService.add(attr);// 添加随机试题
					
					attr.setParentSub(String.format("_%s_%s_", attr.getParentId(), attr.getId()));
					paperQuestionService.update(attr);
					
					// 添加试题答案
					List<QuestionAnswer> questionAnswerList = questionAnswerService.getList(question.getId());//获取试题答案
					List<BigDecimal> singleScoreList = getSubScoreList(rule.getScore(), questionAnswerList.size());
					for(int j = 0; j < questionAnswerList.size(); j++){
						PaperQuestionAnswer answer = new PaperQuestionAnswer();
						answer.setAnswer(questionAnswerList.get(j).getAnswer());
						answer.setPaperId(exam.getPaperId());
						answer.setExamId(exam.getId());
						answer.setUserId(userId);
						answer.setQuestionId(question.getId());
						answer.setQuestionType(question.getType());
						answer.setQuestionAi(question.getAi());
						answer.setPaperQuestionId(attr.getId());
						answer.setNo(questionAnswerList.get(j).getNo());
						
						if (question.getType() == 1 || question.getType() == 4 ) {// 单选和判断
							answer.setScore(rule.getScore());// 子分数和当前题分数一样（就一个可以不循环）
						} else if (question.getType() == 2) {// 多选题
							answer.setScore(BigDecimalUtil.newInstance(singleScoreList.get(j)).div(2, 2).getResult());// 子分数为当前题分数一半
						} else if (question.getType() == 3 || question.getType() == 5) {// 填空和问答题
							answer.setScore(question.getAi() == 1
									? singleScoreList.get(j)// 如果是客观题，子分数为当前题分数除总关键词
									: new BigDecimal(0));// 如果是主观题，子分数无效
						}
						
						paperQuestionAnswerService.add(answer);
					}
					
					chapter.getMyQuestionList().add(new MyQuestion(question, null, null, attr));// 模拟生成一个试卷，不用所有数据都生成
					usedQuestion.add(question.getId());
					remainQuestionNum--;
					if (remainQuestionNum <= 0) {// 某章节下某规则符合，退出循环。处理下一个规则
						break;
					}
				}
			}
		}
		
		return myPaper;
	}

	/**
	 * 获取子分数列表<br/>
	 * 1分 拆分成2份，结果：0.5、0.5<br/>
	 * 1分 拆分成3份，结果：0.3、0.3、0.4<br/>
	 * 
	 * v1.0 zhanghc 2022年5月30日下午4:46:25
	 * @param score
	 * @param num
	 * @return List<BigDecimal>
	 */
	private List<BigDecimal> getSubScoreList(BigDecimal score, int num) {
		List<BigDecimal> singleScoreList = new ArrayList<>();
		BigDecimal singleScore = BigDecimalUtil.newInstance(score).div(num, 2).getResult();
		for (int j = 0; j < num - 1; j++) {
			singleScoreList.add(singleScore);
		}
		singleScoreList.add(BigDecimalUtil.newInstance(singleScore).mul(num - 1).sub(score).mul(-1).getResult());
		return singleScoreList;
	}

	private Map<Integer, List<Question>> getQuestionListCache(Map<Integer, List<PaperQuestionRule>> ruleCache) {
		Map<Integer, List<Question>> questionListCache = new HashMap<>();// 按 试题分类 分组的试题
		for (List<PaperQuestionRule> ruleList : ruleCache.values()) {
			for (PaperQuestionRule rule : ruleList) {
				if (questionListCache.get(rule.getQuestionTypeId()) != null) {
					continue;
				}
				
				List<Question> questionList = questionService.getList(rule.getQuestionTypeId());
				ListIterator<Question> questionIterator = questionList.listIterator();
				while (questionIterator.hasNext()) {
					Question question = questionIterator.next();
					if (question.getState() != 1) {// 非发布的删除
						questionIterator.remove();
					}
				}
				
				questionListCache.put(rule.getQuestionTypeId(), questionList);
			}
		}
		return questionListCache;
	}

	private Map<Integer, List<PaperQuestionRule>> getRuleCache(List<PaperQuestion> chapterList) {
		Map<Integer, List<PaperQuestionRule>> ruleCache = new HashMap<>();
		for (PaperQuestion chapter : chapterList) {
			List<PaperQuestionRule> ruleList = paperQuestionRuleService.getList(chapter.getId());
			ruleCache.put(chapter.getId(), ruleList);
		}
		return ruleCache;
	}

	private void publishForRandValid(List<PaperQuestion> chapterList, 
			Map<Integer, List<PaperQuestionRule>> ruleCache, Map<Integer, List<Question>> questionListCache) {
		Set<Integer> usedQuestion = new HashSet<Integer>();
		for (PaperQuestion chapter : chapterList) {// chapterList作用是抛异常时用
			for (PaperQuestionRule rule : ruleCache.get(chapter.getId())) {// 循环单章节下规则
				int remainQuestionNum = rule.getNum();
				for(Question question : questionListCache.get(rule.getQuestionTypeId())) {
					if (rule.getType().intValue() != question.getType().intValue()
							|| !rule.getDifficultys().contains(question.getDifficulty().toString()) 
							|| !rule.getAis().contains(question.getAi().toString()) ) {// 当前试题不符合规则，不添加
							continue;
					}
					
					if (usedQuestion.contains(question.getId())) {// 添加过，不添加
						continue;
					}
					
					remainQuestionNum--;
					if (remainQuestionNum <= 0) {// 某章节下某规则符合，退出循环
						break;
					}
				}
				
				if (remainQuestionNum > 0) {// // 某章节下某规则不符合，抛异常
					throw new MyException(String.format("章节【%s】下第【%s】个规则题数不足【%s】道", chapter.getName(), rule.getNo(), rule.getNum()));
				}
			}
		}
	}

	@Override
	public void userAdd(Exam exam, String[] examUserIds, Integer[] markUserIds) {
		// 同步考试用户
		if (ValidateUtil.isValid(markUserIds) && markUserIds.length == 1) {// 如果只有一组并且按逗号分割，会自动解析为数组
			examUserIds[0] = StringUtil.join(examUserIds);
		}
		List<MyExam> newMyExamList = userAddSyn(exam, examUserIds);
		
		// 添加用户试卷
		MyPaper myPaper = paperService.getMyPaper(exam.getId());
		for (MyExam myExam : newMyExamList) {
			generateUserPaper(exam, myExam.getUserId(), myPaper);
		}
	}

	/**
	 * 同步考试用户
	 * 
	 * v1.0 zhanghc 2022年5月31日上午11:18:42
	 * @param exam
	 * @param examUserIds
	 * @return List<MyExam> 需要添加的用户试卷
	 */
	private List<MyExam> userAddSyn(Exam exam, String[] examUserIds) {
		/**
		 * 页面：1,2,3
		 * 数据库：2,3,4
		 * 1添加；4删除；2,3不动
		 */
		List<MyExam> myExamList = myExamService.getList(exam.getId());
		ListIterator<MyExam> myExamListIterator = myExamList.listIterator();// 当前考试的人员
		Set<Integer> examUserIdSet = new HashSet<>();
		for (String userIds : examUserIds) {
			for (String userId : userIds.split(",")) {
				examUserIdSet.add(Integer.parseInt(userId));
			}
		}
		while (myExamListIterator.hasNext()) {
			MyExam myExam = myExamListIterator.next();
			if (examUserIdSet.contains(myExam.getUserId())) {
				myExamListIterator.remove();
			} else {
				List<MyExamDetail> myExamDetailList = myExamDetailService.getList(exam.getId(), myExam.getUserId());
				for (MyExamDetail myExamDetail : myExamDetailList) {
					myExamDetailService.del(myExamDetail.getId());
				}
				
				MyExam myExamDel = myExamService.getEntity(exam.getId(), myExam.getUserId());
				myExamService.del(myExamDel.getId());
			}
		}
		return myExamList;
	}

	@Override
	public void userAddForRand(Exam exam, String[] examUserIds, Integer[] markUserIds) {
		// 校验数据有效性
		List<PaperQuestion> chapterList = paperQuestionService.getChapterList(exam.getPaperId());
		Map<Integer, List<PaperQuestionRule>> ruleCache = getRuleCache(chapterList);
		Map<Integer, List<Question>> questionListCache = getQuestionListCache(ruleCache);
		publishForRandValid(chapterList, ruleCache, questionListCache);
		
		// 同步考试用户
		if (ValidateUtil.isValid(markUserIds) && markUserIds.length == 1) {// 如果只有一组并且按逗号分割，会自动解析为数组
			examUserIds[0] = StringUtil.join(examUserIds);
		}
		
		List<MyExam> newMyExamList = userAddSyn(exam, examUserIds);
		for (MyExam myExam : newMyExamList) {
			MyPaper myPaper = generatePaperForRand(exam, ruleCache, questionListCache, myExam.getUserId());
			generateUserPaper(exam, myExam.getUserId(), myPaper);
		}
	}
}