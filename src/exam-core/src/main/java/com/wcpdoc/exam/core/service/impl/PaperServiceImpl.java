package com.wcpdoc.exam.core.service.impl;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.wcpdoc.base.service.UserService;
import com.wcpdoc.core.dao.BaseDao;
import com.wcpdoc.core.exception.MyException;
import com.wcpdoc.core.service.impl.BaseServiceImp;
import com.wcpdoc.core.util.BigDecimalUtil;
import com.wcpdoc.core.util.StringUtil;
import com.wcpdoc.core.util.ValidateUtil;
import com.wcpdoc.exam.core.dao.PaperDao;
import com.wcpdoc.exam.core.entity.Exam;
import com.wcpdoc.exam.core.entity.Paper;
import com.wcpdoc.exam.core.entity.PaperQuestion;
import com.wcpdoc.exam.core.entity.PaperQuestionAnswer;
import com.wcpdoc.exam.core.entity.PaperQuestionRule;
import com.wcpdoc.exam.core.entity.PaperRemark;
import com.wcpdoc.exam.core.entity.PaperType;
import com.wcpdoc.exam.core.entity.Question;
import com.wcpdoc.exam.core.entity.QuestionAnswer;
import com.wcpdoc.exam.core.entity.QuestionOption;
import com.wcpdoc.exam.core.service.ExamService;
import com.wcpdoc.exam.core.service.PaperQuestionAnswerService;
import com.wcpdoc.exam.core.service.PaperQuestionRuleService;
import com.wcpdoc.exam.core.service.PaperQuestionService;
import com.wcpdoc.exam.core.service.PaperRemarkService;
import com.wcpdoc.exam.core.service.PaperService;
import com.wcpdoc.exam.core.service.PaperTypeService;
import com.wcpdoc.exam.core.service.QuestionAnswerService;
import com.wcpdoc.exam.core.service.QuestionOptionService;
import com.wcpdoc.exam.core.service.QuestionService;
import com.wcpdoc.exam.core.service.QuestionTypeService;

/**
 * 试卷服务层实现
 * 
 * zhanghc 2018年10月21日上午8:16:06
 */
@Service
public class PaperServiceImpl extends BaseServiceImp<Paper> implements PaperService {
	@Resource
	private PaperDao paperDao;
	@Resource
	private PaperQuestionService paperQuestionService;
	@Resource
	private QuestionService questionService;
	@Resource
	private QuestionAnswerService questionAnswerService;
	@Resource
	private PaperQuestionAnswerService paperQuestionAnswerService;
	@Resource
	private PaperTypeService paperTypeService;
	@Resource
	private UserService userService;
	@Resource
	private QuestionTypeService questionTypeService;
	@Resource
	private PaperRemarkService paperRemarkService;
	@Resource
	private QuestionOptionService questionOptionService;
	@Resource
	private PaperQuestionRuleService paperQuestionRuleService;
	@Resource
	private ExamService examService;
	
	@Override
	@Resource(name = "paperDaoImpl")
	public void setDao(BaseDao<Paper> dao) {
		super.dao = dao;
	}
	
	@Override
	public Integer addAndUpdate(Paper paper, PaperRemark paperRemark) {// paperRemark暂时不用保留
		// 校验数据有效性
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		// 添加试卷
		Date curTime = new Date();
		paper.setCreateUserId(getCurUser().getId());
		paper.setCreateTime(curTime);
		paper.setUpdateUserId(getCurUser().getId());
		paper.setUpdateTime(curTime);
		paper.setTotalScore(BigDecimal.ZERO);// 默认总分数为0
		paper.setState(2);// 默认为草稿
		add(paper);
		
		return paper.getId();//快速创建考试需要用id查找信息
	}
	
	@Override
	public void updateAndUpdate(Paper paper) {
		// 校验数据有效性
		Paper entity = getEntity(paper.getId());
		if (entity.getState() == 0) {
			throw new MyException("已删除");
		}
		if (entity.getState() == 1) {// 已发布时不能修改组卷方式和阅卷方式
			if (entity.getGenType().intValue() != paper.getGenType().intValue()) {
				throw new MyException("已发布");
			}
			if (entity.getMarkType().intValue() != paper.getMarkType().intValue()) {
				throw new MyException("已发布");
			}
		}
		if (entity.getState() == 3) {
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(entity.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		List<Exam> examList = examService.getList(); //是否被考试引用
		if (ValidateUtil.isValid(examList)) {
			for(Exam exam : examList){
				if (exam.getPaperId().intValue() ==  paper.getId().intValue()) {
					throw new MyException(String.format("该试卷已被【%s】考试引用", exam.getName()));
				}
			}
		}
		
		// 更新试卷
		entity.setGenType(paper.getGenType());
		entity.setShowType(paper.getShowType());
		entity.setName(paper.getName());
		entity.setMarkType(paper.getMarkType());
		entity.setPassScore(paper.getPassScore());
		entity.setUpdateUserId(getCurUser().getId());
		entity.setUpdateTime(new Date());
		update(entity);
	}
	
	@Override
	public void delAndUpdate(Integer id) {
		// 校验数据有效性
		Paper paper = getEntity(id);
		if(paper.getState() == 0) {
			throw new MyException("已删除");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		// 删除试卷
		paper.setState(0);
		paper.setUpdateTime(new Date());
		paper.setUpdateUserId(getCurUser().getId());
		update(paper);
	}
	
	@Override
	public void copy(Integer id) {
		// 校验数据有效性
		Paper paper = paperDao.getEntity(id);
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		// 复制试卷
		Paper copyPaper = new Paper();// 复制试卷
		try {
			BeanUtils.copyProperties(copyPaper, paper);
		} catch (Exception e) {
			throw new MyException(e.getMessage());
		}
		copyPaper.setName(String.format("%s%s", "【复件】", paper.getName()));
		copyPaper.setState(2);
		copyPaper.setCreateTime(new Date());
		copyPaper.setCreateUserId(getCurUser().getId());
		copyPaper.setUpdateTime(new Date());
		copyPaper.setUpdateUserId(getCurUser().getId());
		add(copyPaper);

		List<PaperQuestion> chapterList = paperQuestionService.getChapterList(paper.getId());// 复制章节
		
			for (PaperQuestion chapter : chapterList) {
				PaperQuestion copyChapter = new PaperQuestion();
				try {
					BeanUtils.copyProperties(copyChapter, chapter);
				} catch (Exception e) {
					throw new MyException(e.getMessage());
				}
				copyChapter.setPaperId(copyPaper.getId());
				paperQuestionService.add(copyChapter);
				copyChapter.setParentSub(String.format("_%s_", copyChapter.getId()));
				paperQuestionService.update(copyChapter);
				
				if (copyPaper.getGenType() == 1) {// 人工组卷
					List<PaperQuestion> questionList = paperQuestionService.getQuestionList(chapter.getId(), null, null);//复制试题
					for (PaperQuestion question : questionList) {
						PaperQuestion copyQuestion = new PaperQuestion();
						try {
							BeanUtils.copyProperties(copyQuestion, question);
						} catch (Exception e) {
							throw new MyException(e.getMessage());
						}
						copyQuestion.setParentId(copyChapter.getId());
						copyQuestion.setPaperId(copyPaper.getId());
						paperQuestionService.add(copyQuestion);
						copyQuestion.setParentSub(String.format("%s%s_", copyChapter.getParentSub(), copyQuestion.getId()));
						paperQuestionService.update(copyQuestion);

						List<PaperQuestionAnswer> answerList = paperQuestionAnswerService.getList(id, question.getQuestionId());// 复制答案
						for (PaperQuestionAnswer answer : answerList) {
							PaperQuestionAnswer copyAnswer = new PaperQuestionAnswer();
							try {
								BeanUtils.copyProperties(copyAnswer, answer);
							} catch (Exception e) {
								throw new MyException(e.getMessage());
							}
							copyAnswer.setPaperQuestionId(copyChapter.getId());
							copyAnswer.setPaperId(copyPaper.getId());
							paperQuestionAnswerService.add(copyAnswer);
						}
					}
				} else {// 随机组卷
					List<PaperQuestionRule> randChapterRuleList = paperQuestionRuleService.getChapterList(chapter.getPaperId(), chapter.getId());
					for(PaperQuestionRule paperQuestionRule : randChapterRuleList){
						PaperQuestionRule copyPaperQuestionRule = new PaperQuestionRule();
						try {
							BeanUtils.copyProperties(copyPaperQuestionRule, paperQuestionRule);
						} catch (Exception e) {
							throw new MyException(e.getMessage());
						}
						
						copyPaperQuestionRule.setPaperId(copyPaper.getId());
						copyPaperQuestionRule.setPaperQuestionId(copyChapter.getId());
						copyPaperQuestionRule.setUpdateTime(new Date());
						copyPaperQuestionRule.setUpdateUserId(getCurUser().getId());
						paperQuestionRuleService.add(copyPaperQuestionRule);
					}
				}
		}
	}
	
	@Override
	public void archive(Integer id) {
		// 校验数据有效性
		Paper paper = getEntity(id);
		if(paper.getState() == 0) {
			throw new MyException("已删除");
		}
		if(paper.getState() == 2){
			throw new MyException("未发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		// 归档
		paper.setState(3);
		paper.setUpdateTime(new Date());
		paper.setUpdateUserId(getCurUser().getId());
		update(paper);
	}

	@Override
	public void chapterAdd(PaperQuestion chapter) {
		//校验数据有效性
		if(chapter.getPaperId() == null) {
			throw new MyException("参数错误：paperId");
		}
		if(!ValidateUtil.isValid(chapter.getName())) {
			throw new MyException("参数错误：name");
		}
		Paper paper = getEntity(chapter.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		//添加章节
		chapter.setUpdateTime(new Date());
		chapter.setUpdateUserId(getCurUser().getId());
		chapter.setType(1);
		chapter.setParentId(0);
		
		List<PaperQuestion> paperQuestionList = paperQuestionService.getChapterList(chapter.getPaperId());
		chapter.setNo(paperQuestionList.size() + 1);
		paperQuestionService.add(chapter);
		
		//更新父子关系
		chapter.setParentSub("_" + chapter.getId() + "_");
		paperQuestionService.update(chapter);
	}

	@Override
	public void chapterEdit(PaperQuestion chapter) {
		//校验数据有效性
		PaperQuestion paperQuestion = paperQuestionService.getEntity(chapter.getId());
		if(paperQuestion == null){
			throw new MyException("参数错误：id");
		}
		if (paperQuestion.getType() != 1) {
			throw new MyException("参数错误：id");
		}
		Paper paper = getEntity(paperQuestion.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		// 修改章节
		paperQuestion.setName(chapter.getName());
		paperQuestion.setDescription(chapter.getDescription());
		paperQuestion.setUpdateTime(new Date());
		paperQuestion.setUpdateUserId(getCurUser().getId());
		paperQuestionService.update(paperQuestion);
	}
	
	@Override
	public void chapterDel(Integer chapterId) {
		//校验数据有效性
		PaperQuestion chapter = paperQuestionService.getEntity(chapterId);
		if(chapter == null){
			throw new MyException("参数错误：chapterId");
		}
		if (chapter.getType() != 1) {
			throw new MyException("参数错误：chapterId");
		}
		Paper paper = getEntity(chapter.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		//删除章节
		List<PaperQuestion> questionList = paperQuestionService.getQuestionList(chapterId, null, null);// 删除章节下试题
		for(PaperQuestion pq : questionList) {
			paperQuestionService.del(pq.getId());
		}
		
		if (paper.getGenType() == 2) {//删除随机章节规则
			List<PaperQuestionRule> chapterList = paperQuestionRuleService.getChapterList(paper.getId(), chapterId);
			for(PaperQuestionRule rcr : chapterList) {
				paperQuestionRuleService.del(rcr.getId());
			}
		}
		paperQuestionService.del(chapterId);// 删除章节
		
		
	}
	
	@Override
	public void chapterMove(Integer sourceId, Integer targetId) {
		//校验数据有效性
		if (!ValidateUtil.isValid(sourceId)) {
			throw new MyException("参数错误：sourceId");
		}
		if (!ValidateUtil.isValid(targetId)) {
			throw new MyException("参数错误：targetId");
		}
		PaperQuestion source = paperQuestionService.getEntity(sourceId);
		PaperQuestion target = paperQuestionService.getEntity(targetId);
		if (source.getType() != 1) {
			throw new MyException("参数错误：sourceId");
		}
		if (target.getType() != 1) {
			throw new MyException("参数错误：targetId");
		}
		if (source.getPaperId().intValue() != target.getPaperId().intValue()) {// 如果修改的不是同一张试卷
			throw new MyException("参数错误：targetId");
		}
		Paper paper = getEntity(source.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		// 移动
		Integer sourceNo = source.getNo();
		source.setUpdateUserId(getCurUser().getId());
		source.setUpdateTime(new Date());
		source.setNo(target.getNo());
		target.setUpdateUserId(getCurUser().getId());
		target.setUpdateTime(new Date());
		target.setNo(sourceNo);
		paperQuestionService.update(source);
		paperQuestionService.update(target);
	}
	
	@Override
	public void questionMove(Integer id, Integer sourceId, Integer targetId) {
		//校验数据有效性
		if (!ValidateUtil.isValid(id)) {
			throw new MyException("参数错误：id");
		}
		if (!ValidateUtil.isValid(sourceId)) {
			throw new MyException("参数错误：sourceId");
		}
		if (!ValidateUtil.isValid(targetId)) {
			throw new MyException("参数错误：targetId");
		}
		PaperQuestion source = paperQuestionService.getEntity(id, sourceId);
		PaperQuestion target = paperQuestionService.getEntity(id, targetId);
		if (source.getType() == 1) {
			throw new MyException("参数错误：sourceId");
		}
		if (target.getType() == 1) {
			throw new MyException("参数错误：targetId");
		}
		//if (source.getPaperId().intValue() != target.getPaperId().intValue()) {// 如果修改的不是同一张试卷
		//	throw new MyException("参数错误：targetId");// 肯定是一张，不用在校验
		//}
		Paper paper = getEntity(source.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		// 移动
		Integer sourceNo = source.getNo();
		source.setUpdateUserId(getCurUser().getId());
		source.setUpdateTime(new Date());
		source.setNo(target.getNo());
		target.setUpdateUserId(getCurUser().getId());
		target.setUpdateTime(new Date());
		target.setNo(sourceNo);
		
		if (source.getParentId().intValue() != target.getParentId().intValue()) {// 如果不是同一个章节，更新父子关系
			Integer parentId = source.getParentId();
			String parentSub = source.getParentSub();
			source.setParentId(target.getParentId());
			source.setParentSub(target.getParentSub());
			
			target.setParentId(parentId);
			target.setParentSub(parentSub);
		}
		
		paperQuestionService.update(source);
		paperQuestionService.update(target);
	}
	
	@Override
	public void questionAdd(Integer chapterId, Integer[] questionIds) {
		// 校验数据有效性
		if (!ValidateUtil.isValid(chapterId)) {
			throw new MyException("参数错误：chapterId");
		}
		if (!ValidateUtil.isValid(questionIds)) {
			throw new MyException("参数错误：questionIds");
		}
		PaperQuestion chapter = paperQuestionService.getEntity(chapterId);
		if (chapter.getType() != 1) {
			throw new MyException("参数错误：chapterId");
		}
		Paper paper = getEntity(chapter.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		List<PaperQuestion> paperQuestionList = paperQuestionService.getList(paper.getId());
		for (PaperQuestion pq : paperQuestionList) {
			for (Integer questionId : questionIds) {
				if (pq.getType() != 1 && pq.getQuestionId().intValue() == questionId.intValue()) {
					throw new MyException("添加试题重复");
				}
			}
		}
		
		List<Question> questionList = new ArrayList<>();
		for (Integer questionId : questionIds) {
			Question question = questionService.getEntity(questionId);
			if (question.getState() != 1) {
				throw new MyException("当前试题未发布");
			}
			if (paper.getMarkType() == 1 && question.getAi() != 1) {// 如果试卷是智能阅卷，添加试题为人工阅卷类型
				throw new MyException("不支持人工阅卷类型的试题");
			}
			questionList.add(question);
		}
		
		// 添加试题
		int maxNo = 1;// 试题最大序号
		for (PaperQuestion pq : paperQuestionList) {
			if (pq.getParentId().intValue() == chapterId.intValue()) {
				++maxNo;
			}
		}
		
		for (Question question : questionList) {
			PaperQuestion pq = new PaperQuestion();
			pq.setUpdateTime(new Date());
			pq.setUpdateUserId(getCurUser().getId());
			pq.setPaperId(chapter.getPaperId());
			pq.setParentId(chapterId);
			pq.setQuestionId(question.getId());
			pq.setType(2);
			pq.setNo(++maxNo);
			pq.setScore(question.getScore());
			pq.setAiOptions(question.getAiOptions());
			paperQuestionService.add(pq);
			
			pq.setParentSub(String.format("%s%s_", chapter.getParentSub(), pq.getId()));
			paperQuestionService.update(pq);
			
			//添加试题答案
			List<QuestionAnswer> questionAnswerList = questionAnswerService.getList(question.getId());
			for(QuestionAnswer questionAnswer : questionAnswerList){
				PaperQuestionAnswer paperQuestionAnswer = new PaperQuestionAnswer();
				try {
					BeanUtils.copyProperties(paperQuestionAnswer, questionAnswer);
				} catch (Exception e) {
					throw new MyException(e.getMessage());
				}
				paperQuestionAnswer.setPaperQuestionId(chapterId);
				paperQuestionAnswer.setPaperId(chapter.getPaperId());
				paperQuestionAnswerService.add(paperQuestionAnswer);
			}
		}
	}
	
	@Override
	public void scoreUpdate(Integer id, Integer questionId, BigDecimal score, BigDecimal[] subScores, Integer[] aiOptions) {
		//校验数据有效性
		if(!ValidateUtil.isValid(id)) {
			throw new MyException("参数错误：id");
		}
		if(!ValidateUtil.isValid(questionId)) {
			throw new MyException("参数错误：questionId");
		}
		if(!ValidateUtil.isValid(score)) {
			throw new MyException("参数错误：score");
		}
		
		Question question = questionService.getEntity(questionId);
		List<PaperQuestionAnswer> answerList = paperQuestionAnswerService.getList(id, questionId);
		if (question.getAi() == 1 && (question.getType() == 3 || question.getType() == 5)) {// 试题为智能阅卷，并且是填空或问答时有效
			if(!ValidateUtil.isValid(subScores)) {
				throw new MyException("参数错误：subScores");
			}
			
			BigDecimalUtil bigDecimalUtil = BigDecimalUtil.newInstance(0);
			for (BigDecimal subScore : subScores) {
				bigDecimalUtil.add(subScore);
			}
			if (score.doubleValue() != bigDecimalUtil.getResult().doubleValue()) {
				throw new MyException("参数错误：subScores总分和score不匹配");
			}
			
			if (subScores.length != answerList.size()) {
				throw new MyException("参数错误：subScores个数不匹配");
			}
		}
		
		PaperQuestion paperQuestion = paperQuestionService.getEntity(id, questionId);
		if (paperQuestion == null) {
			throw new MyException("参数错误：questionId");
		}
		
		Paper paper = getEntity(id);
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		// 设置分数
		PaperQuestion pq = paperQuestionService.getEntity(id, questionId);
		
		// 设置分数选项
		String aiOptionStr = aiOptions == null ? "" : StringUtil.join(aiOptions);
		if (question.getType() == 2) {// 分数选项：1：漏选得分；2：答案无顺序；3：大小写不敏感；
			if (aiOptionStr.contains("1")) {
				pq.setAiOptions("1");
			}
		} else if (question.getType() == 3) {
			if (aiOptionStr.contains("2") && aiOptionStr.contains("3")) {
				pq.setAiOptions("2,3");
			} else if (aiOptionStr.contains("2")) {
				pq.setAiOptions("2");
			} else if (aiOptionStr.contains("3")) {
				pq.setAiOptions("3");
			}  
		} else if (question.getType() == 5 && question.getAi() == 1) {
			if (aiOptionStr.contains("3")) {
				pq.setAiOptions("3");
			}
		}
		
		pq.setScore(score);
		paperQuestionService.update(pq);
		
//		if (question.getAi() == 1 && (question.getType() == 3 || question.getType() == 5)) {// 试题为智能阅卷，并且是填空或问答时更新子分数
//			for (int i = 0; i < subScores.length; i++) {
//				PaperQuestionAnswer answer = answerList.get(i);
//				answer.setScore(subScores[i]);
//				paperQuestionAnswerService.update(answer);
//			}
//		}
		if (question.getAi() == 1 && (question.getType() == 2 || question.getType() == 3 || question.getType() == 5 )) {
			for (int i = 0; i < subScores.length; i++) {
				PaperQuestionAnswer answer = answerList.get(i);
				answer.setScore(subScores[i]);
				paperQuestionAnswerService.update(answer);
			}
		}
		if (question.getAi() == 1 && (question.getType() == 1 || question.getType() == 4)) {
			for (int i = 0; i < subScores.length; i++) {
				PaperQuestionAnswer answer = answerList.get(i);
				answer.setScore(score);
				paperQuestionAnswerService.update(answer);
			}
		}
	}

	@Override
	public void questionDel(Integer id, Integer questionId) {
		// 校验数据有效性
		if (!ValidateUtil.isValid(id)) {
			throw new MyException("参数错误：id");
		}
		if (!ValidateUtil.isValid(questionId)) {
			throw new MyException("参数错误：questionId");
		}
		
		Paper paper = getEntity(id);
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		PaperQuestion pq = paperQuestionService.getEntity(id, questionId);
		if (pq == null) {
			throw new MyException("参数错误：questionId");
		}
		
		// 删除试题
		List<PaperQuestionAnswer> answerList = paperQuestionAnswerService.getList(id, pq.getQuestionId());// 删除答案
		for(PaperQuestionAnswer answer : answerList){
			paperQuestionAnswerService.del(answer.getId());
		}
		paperQuestionService.del(pq.getId());// 删除试题
		
		// 同级试题重新排序
		List<PaperQuestion> pqList = paperQuestionService.getQuestionList(pq.getParentId(), null, null);
		int maxNo = 1;
		for (PaperQuestion cur : pqList) {
			cur.setNo(maxNo++);
			paperQuestionService.update(cur);
		}
	}

	@Override
	public void questionClear(Integer chapterId) {
		// 校验数据有效性
		PaperQuestion chapter = paperQuestionService.getEntity(chapterId);
		if(chapter == null){
			throw new MyException("参数错误：chapterId");
		}
		if (chapter.getType() != 1) {
			throw new MyException("参数错误：chapterId");
		}
		Paper paper = getEntity(chapter.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}

		// 清空试题
		List<PaperQuestion> pqList = paperQuestionService.getQuestionList(chapterId, null, null);
		for (PaperQuestion pq : pqList) {
			List<PaperQuestionAnswer> answerList = paperQuestionAnswerService.getList(pq.getPaperId(), pq.getQuestionId());// 删除答案
			for(PaperQuestionAnswer answer : answerList){
				paperQuestionAnswerService.del(answer.getId());
			}
			paperQuestionService.del(pq.getId());// 删除试题
		}
	}

	@Override
	public List<Question> getQuestionList(Integer id) {
		return paperDao.getQuestionList(id);
	}
	
	@Override
	public List<PaperQuestion> getPaperQuestionList(Integer id) {
		return paperDao.getPaperQuestionList(id);
	}
	
	@Override
	public List<Paper> getList(Integer paperTypeId) {
		return paperDao.getList(paperTypeId);
	}

	@Override
	public void batchScoreUpdate(Integer chapterId, BigDecimal score, BigDecimal subScores, Integer[] aiOptions) {
		// 校验数据有效性
		if (chapterId == null) {
			throw new MyException("参数错误：chapterId");
		}
		if (score == null) {
			throw new MyException("参数错误：score");
		}
		PaperQuestion entity = paperQuestionService.getEntity(chapterId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}

		// 更新试卷分数
		String aiOptionsString = "";
		if (aiOptions != null && aiOptions.length > 0) {			
			aiOptionsString = StringUtil.join(aiOptions);
		}
		
		List<PaperQuestion> pqList = paperQuestionService.getQuestionList(chapterId, null, null);
		for(int i = 0; i < pqList.size(); i ++ ){
			PaperQuestion pq = pqList.get(i);
			Question question = questionService.getEntity(pq.getQuestionId());
			if (question.getType() == 2) {
				if (ValidateUtil.isValid(aiOptionsString) && aiOptionsString.contains("1")) {
					pq.setAiOptions("1");
				}
			} else if (question.getType() == 3) {
				if (ValidateUtil.isValid(aiOptionsString) && (aiOptionsString.contains("2") || aiOptionsString.contains("3")) ) {
					pq.setAiOptions(aiOptionsString);
				}
			} else if (question.getType() == 5) {
				if (ValidateUtil.isValid(aiOptionsString) && aiOptionsString.contains("3")) {
					pq.setAiOptions("3");
				}
			}
			pq.setScore(score);
			paperQuestionService.update(pq);
			//更新答案分数
			List<PaperQuestionAnswer> paperQuestionAnswerList = paperQuestionAnswerService.getList(pq.getPaperId(), pq.getQuestionId());
	        NumberFormat nformat = NumberFormat.getNumberInstance();
	        nformat.setMaximumFractionDigits(2);
			if (question.getType() == 1 || question.getType() == 4) { //单选 判断
				for(PaperQuestionAnswer paperQuestionAnswer : paperQuestionAnswerList){
					paperQuestionAnswer.setScore(score);
					paperQuestionAnswerService.update(paperQuestionAnswer);
				}
			}
			if (question.getType() == 2 && ValidateUtil.isValid(aiOptionsString) && aiOptionsString.contains("1")) {// 多选
				for(PaperQuestionAnswer paperQuestionAnswer : paperQuestionAnswerList){
					paperQuestionAnswer.setScore(subScores);
					paperQuestionAnswerService.update(paperQuestionAnswer);
				}
			}
			
			if (question.getType() == 3 || question.getType() == 5 ) { // 填空  问答
				BigDecimal bitScore = new BigDecimal(0); //保留两位数的平均值
				BigDecimal lastOneScore = new BigDecimal(0); // 不能被平分的最后一位分值
				if (paperQuestionAnswerList != null && paperQuestionAnswerList.size() > 0) {
			        BigDecimal listSize = new BigDecimal(paperQuestionAnswerList.size());
					BigDecimal averageScore = score.divide(listSize, 2, BigDecimal.ROUND_HALF_UP); // 平均分
					bitScore = new BigDecimal(nformat.format(averageScore));
					BigDecimal multiply = bitScore.multiply(listSize);// 平均分*size
					
					if (score.compareTo(multiply) != 0) {// 不能平分平均分
						lastOneScore = score.subtract(bitScore.multiply(new BigDecimal(paperQuestionAnswerList.size() - 1)));
					}
				}
				
				if (lastOneScore.compareTo(new BigDecimal(0)) == 0) {//可以平分
					for(PaperQuestionAnswer paperQuestionAnswer : paperQuestionAnswerList){
						paperQuestionAnswer.setScore(bitScore);
						paperQuestionAnswerService.update(paperQuestionAnswer);
					}
					continue;
				}
				
				for(int j = 1; j <= paperQuestionAnswerList.size(); j++){ //不能平分
					PaperQuestionAnswer paperQuestionAnswer = paperQuestionAnswerList.get(j-1);
					paperQuestionAnswer.setScore(bitScore);
					if (j == paperQuestionAnswerList.size() ) {
						paperQuestionAnswer.setScore(lastOneScore);
					}
					paperQuestionAnswerService.update(paperQuestionAnswer);
				}
			}
		}	
//		for (PaperQuestion pq : pqList) {
//			pq.setAiOptions(null);
//			Question question = questionService.getEntity(pq.getQuestionId());
//			if (question.getType() == 2) {
//				if (ValidateUtil.isValid(options) && options.contains("1")) {
//					pq.setAiOptions("1");
//				}
//			} else if (question.getType() == 3) {
//				pq.setAiOptions(options);
//			}
//
//			pq.setScore(score);
//			paperQuestionService.update(pq);
//			//更新答案分数
//			if (question.getType() == 1 || question.getType() == 4) { //单选 判断
//				
//			}
//			if (question.getType() == 2 || question.getType() == 3 || question.getType() == 5 ) { //多选  填空 问答  分值平分
//				
//			}
//		}
	}

	@Override
	public void move(Integer sourceId, Integer targetId) {
		// 校验数据有效性
		if (sourceId == null) {
			throw new MyException("参数错误：sourceId");
		}
		if(targetId == null){
			throw new MyException("参数错误：targetId");
		}
		PaperType source = paperTypeService.getEntity(sourceId);
		if (source.getState() == 0 ){
			throw new MyException("该分类已删除");
		}
		PaperType target = paperTypeService.getEntity(targetId);
		if (target.getState() == 0) {
			throw new MyException("该分类已删除");
		}
		
		if (source.getCreateUserId().intValue() != getCurUser().getId().intValue()) {// 只能移动自己的分类
			throw new MyException("无操作权限");
		}
		//if (!target.getWriteUserIds().contains(String.format(",%s,", getCurUser().getId()))) {// 只能移动到自己的分类或有组权限的分类
		//	throw new MyException("无操作权限");
		//}
		
		// 移动
		List<Paper> list = paperDao.getList(sourceId);
		for (Paper paper : list) {
			paper.setPaperTypeId(targetId);
			update(paper);
		}
	}

	@Override
	public List<Map<String, Object>> paperQuestionList(Integer id, Integer examId, Integer userId) {
		Paper paper = paperDao.getEntity(id);
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		List<PaperQuestion> chapterList = paperQuestionService.getChapterList(paper.getId());
		Map<Integer, Question> idQuestionMap = new HashMap<>();
		if (paper.getGenType() == 1) {
			List<Question> questionList = getQuestionList(id);
			for (Question question : questionList) {
				idQuestionMap.put(question.getId(), question);
			}
			
			for (PaperQuestion chapter : chapterList) {
				Map<String, Object> singleResult = new HashMap<String, Object>();// 章节
				Map<String, Object> chapterMap = new HashMap<String, Object>();
				chapterMap.put("id", chapter.getId());
				chapterMap.put("name", chapter.getName());
				chapterMap.put("description", chapter.getDescription());
				singleResult.put("chapter", chapterMap);

				List<PaperQuestion> paperQuestionList = paperQuestionService.getQuestionList(chapter.getId(), null, null);// 试题
				List<Map<String, Object>> questionsListMap = new ArrayList<>();
				for (PaperQuestion paperQuestion : paperQuestionList) {
					Map<String, Object> questionMap = new HashMap<>();
					Question question = idQuestionMap.get(paperQuestion.getQuestionId());
					questionMap.put("id", question.getId());
					questionMap.put("type", question.getType());
					questionMap.put("difficulty", question.getDifficulty());
					questionMap.put("title", question.getTitle());
					questionMap.put("ai", question.getAi());
					questionMap.put("analysis", question.getAnalysis());
					questionMap.put("score", paperQuestion.getScore());// 分数从试卷中取
					
					Integer[] aiOptions = null;//new Integer[split.length];
					if (ValidateUtil.isValid(paperQuestion.getAiOptions())) {
						String[] split = paperQuestion.getAiOptions().split(",");
						aiOptions = new Integer[split.length];
						for(int i = 0; i < split.length; i++ ){
							aiOptions[i] = Integer.parseInt(split[i]);
						}
					} else {
						aiOptions = new Integer[0];
					}
					
					questionMap.put("aiOptions", aiOptions);// 分数选项从试卷中取
					questionMap.put("options", new String[0]);// 默认为长度为0的数组
					if (question.getType() == 1 || question.getType() == 2) {// 如果是单选或多选，添加选项
						List<QuestionOption> questionOptionList = questionOptionService
								.getList(paperQuestion.getQuestionId());
						String[] options = new String[questionOptionList.size()];
						for (int i = 0; i < questionOptionList.size(); i++) {
							options[i] = questionOptionList.get(i).getOptions();// 按选项顺序添加试题
						}
						questionMap.put("options", options);
					}

					List<PaperQuestionAnswer> answerList = paperQuestionAnswerService.getList(id, question.getId());// 答案
					List<Map<String, Object>> answerMapList = new ArrayList<Map<String, Object>>();
					for (PaperQuestionAnswer answer : answerList) {
						Map<String, Object> map2 = new HashMap<String, Object>();
						map2.put("score", answer.getScore());

						if (question.getType() == 3 || (question.getType() == 5 && question.getAi() == 1)) {
							map2.put("answer", answer.getAnswer().split("\n"));
						} else if (question.getType() == 2) {
							map2.put("answer", answer.getAnswer().split(","));
						} else {
							map2.put("answer", new String[] { answer.getAnswer() });
						}

						answerMapList.add(map2);
					}

					questionMap.put("answers", answerMapList);
					questionsListMap.add(questionMap);
				}
				singleResult.put("questionList", questionsListMap);
				resultList.add(singleResult);
			}
		} else {
			List<Question> questionList = paperQuestionService.getQuestionRandList(examId, paper.getId());
			for (Question question : questionList) {
				idQuestionMap.put(question.getId(), question);
			}
			
			for (PaperQuestion chapter : chapterList) {
				Map<String, Object> singleResult = new HashMap<String, Object>();// 章节
				Map<String, Object> chapterMap = new HashMap<String, Object>();
				chapterMap.put("id", chapter.getId());
				chapterMap.put("name", chapter.getName());
				chapterMap.put("description", chapter.getDescription());
				singleResult.put("chapter", chapterMap);
				// 章节规则
				List<Map<String, Object>> randChapterRulesListMap = new ArrayList<>();
				if (!ValidateUtil.isValid(userId)) {
					userId = getCurUser().getId();
				}
				List<PaperQuestion> questionRandList = paperQuestionService.getQuestionList(chapter.getId(), examId, userId);//随机试题
				for (PaperQuestion paperQuestion : questionRandList) {
					Map<String, Object> randChapterRulesMap = new HashMap<>();
					Question question = idQuestionMap.get(paperQuestion.getQuestionId());
					randChapterRulesMap.put("id", question.getId());
					randChapterRulesMap.put("type", question.getType());
					randChapterRulesMap.put("difficulty", question.getDifficulty());
					randChapterRulesMap.put("title", question.getTitle());
					randChapterRulesMap.put("ai", question.getAi());
					randChapterRulesMap.put("analysis", question.getAnalysis());
					randChapterRulesMap.put("score", paperQuestion.getScore());
					
					Integer[] aiOptions = null;
					if (ValidateUtil.isValid(paperQuestion.getAiOptions())) {
						String[] split = paperQuestion.getAiOptions().split(",");
						aiOptions = new Integer[split.length];
						for(int i = 0; i < split.length; i++ ){
							aiOptions[i] = Integer.parseInt(split[i]);
						}
					} else {
						aiOptions = new Integer[0];
					}
					
					randChapterRulesMap.put("aiOptions", aiOptions);
					randChapterRulesMap.put("options", new String[0]);// 默认为长度为0的数组
					if (question.getType() == 1 || question.getType() == 2) {// 如果是单选或多选，添加选项
						List<QuestionOption> questionOptionList = questionOptionService.getList(paperQuestion.getQuestionId());
						String[] options = new String[questionOptionList.size()];
						for (int i = 0; i < questionOptionList.size(); i++) {
							options[i] = questionOptionList.get(i).getOptions();// 按选项顺序添加试题
						}
						randChapterRulesMap.put("options", options);
					}

					List<QuestionAnswer> questionAnswerList = questionAnswerService.getList(question.getId());// 答案
					List<Map<String, Object>> answerMapList = new ArrayList<Map<String, Object>>();
					for (QuestionAnswer answer : questionAnswerList) {
						Map<String, Object> map2 = new HashMap<String, Object>();
						map2.put("score", answer.getScore());

						if (question.getType() == 3 || (question.getType() == 5 && question.getAi() == 1)) {
							map2.put("answer", answer.getAnswer().split("\n"));
						} else if (question.getType() == 2) {
							map2.put("answer", answer.getAnswer().split(","));
						} else {
							map2.put("answer", new String[] { answer.getAnswer() });
						}

						answerMapList.add(map2);
					}

					randChapterRulesMap.put("answers", answerMapList);
					randChapterRulesListMap.add(randChapterRulesMap);
				}
				singleResult.put("questionList", randChapterRulesListMap);
				resultList.add(singleResult);
			}
		}
		return resultList;
	}

	@Override
	public void publish(Integer id) {
		// 校验数据有效性
		Paper paper = getEntity(id);
		if (paper.getState() == 0) {
			throw new MyException("已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("已发布");
		}
		if (paper.getState() == 3) {
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		List<PaperQuestion> chapterList = paperQuestionService.getChapterList(id);
		if (chapterList == null || chapterList.size() <= 0) {
			throw new MyException("至少需要添加一个章节");
		}
		if (paper.getGenType() == 1 ) {
			List<PaperQuestion> QuestionList = paperQuestionService.getQuestionList(chapterList.get(0).getId(), null, null);
			if (QuestionList == null || QuestionList.size() <= 0) {				
				throw new MyException("至少需要添加一道试题");
			}
		}
		if (paper.getGenType() == 2 ) {//章节随机规则
			List<PaperQuestionRule> PaperQuestionRuleList = paperQuestionRuleService.getChapterList(id, chapterList.get(0).getId());
			if (PaperQuestionRuleList == null || PaperQuestionRuleList.size() <= 0) {
				throw new MyException("至少需要添加一个章节规则");
			}
		}
		
		boolean ai = true;
		BigDecimalUtil totalScore = BigDecimalUtil.newInstance(0); //试卷总分
		if (paper.getGenType() == 1) {
			List<Question> questionList = paperDao.getQuestionList(id);
			for (Question question : questionList) {
				if (question.getAi() != 1) {
					ai = false;
					break;
				}
			}
			
			List<PaperQuestion> paperQuestionList = paperQuestionService.getList(id);
			for (PaperQuestion paperQuestion : paperQuestionList) {
				if (paperQuestion.getType() == 1) {
					continue;
				}
				totalScore.add(paperQuestion.getScore());
			}
		}
		
		if (paper.getGenType() == 2) {
			paperQuestionRuleService.publishCheck(paper);//校验章节随机规则
			
			for (PaperQuestion paperQuestion : chapterList) {
				List<PaperQuestionRule> paperQuestionRuleList = paperQuestionRuleService.getChapterList(paperQuestion.getPaperId(), paperQuestion.getId());
				for (PaperQuestionRule paperQuestionRule : paperQuestionRuleList) {
					totalScore.add(new BigDecimal(paperQuestionRule.getNum()).multiply(paperQuestionRule.getScore()));
					if (paperQuestionRule.getAis().contains("2")) {
						ai = false;
					}
				}
			}
		}
		
		if (paper.getMarkType() == 1 && !ai) {
			throw new MyException("包含人工阅卷试题，发布失败");
		}
		if (paper.getMarkType() == 2 && ai) {// 如果试题不包含人工阅卷试题，修改试卷类型为智能阅卷
			paper.setMarkType(1);
		}
		
		// 发布试卷
		paper.setTotalScore(totalScore.getResult());
		paper.setUpdateTime(new Date());
		paper.setUpdateUserId(getCurUser().getId());
		paper.setState(1);
		update(paper);
	}
	
	@Override
	public void totalScoreUpdate(Integer id) {
		// 校验数据有效性
		Paper paper = getEntity(id);
		if(paper.getState() == 0) {
			throw new MyException("已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("已发布");
		}
		if(paper.getState() == 3){
			throw new MyException("已归档");
		}
		PaperType paperType = paperTypeService.getEntity(paper.getPaperTypeId());
		if(paperType.getCreateUserId().intValue() != getCurUser().getId().intValue()) {
			throw new MyException("无操作权限");
		}
		
		// 计算总分数
		BigDecimalUtil bigDecimalUtil = BigDecimalUtil.newInstance(0);
		List<PaperQuestion> paperQuestionList = paperQuestionService.getList(id);
		for (PaperQuestion paperQuestion : paperQuestionList) {
			if (paperQuestion.getType() != 2) {
				continue;
			}

			bigDecimalUtil.add(paperQuestion.getScore());
		}

		// 更新总分数
		paper.setTotalScore(bigDecimalUtil.getResult());
		update(paper);
	}
}
