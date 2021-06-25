package com.wcpdoc.exam.core.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;

import com.wcpdoc.exam.core.dao.BaseDao;
import com.wcpdoc.exam.core.dao.PaperDao;
import com.wcpdoc.exam.core.entity.Paper;
import com.wcpdoc.exam.core.entity.PaperQuestion;
import com.wcpdoc.exam.core.entity.PaperQuestionEx;
import com.wcpdoc.exam.core.entity.Question;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.PaperQuestionService;
import com.wcpdoc.exam.core.service.PaperService;
import com.wcpdoc.exam.core.service.QuestionService;
import com.wcpdoc.exam.core.util.BigDecimalUtil;
import com.wcpdoc.exam.core.util.StringUtil;
import com.wcpdoc.exam.core.util.ValidateUtil;

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

	@Override
	@Resource(name = "paperDaoImpl")
	public void setDao(BaseDao<Paper> dao) {
		super.dao = dao;
	}

	@Override
	public void chapterAdd(PaperQuestion chapter) {
		//校验数据有效性
		if(chapter.getPaperId() == null) {
			throw new MyException("参数错误：paperId");
		}
		Paper paper = getEntity(chapter.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
				
		//添加章节
		chapter.setUpdateTime(new Date());
		chapter.setUpdateUserId(getCurUser().getId());
		chapter.setType(1);
		chapter.setParentId(0);
		
		List<PaperQuestion> paperQuestionList = paperQuestionService.getQuestionList(chapter.getParentId());
		chapter.setNo(paperQuestionList.size() + 1);
		
		paperQuestionService.add(chapter);
		
		//更新父子关系
		chapter.setParentSub("_" + chapter.getId() + "_");
		paperQuestionService.update(chapter);
	}

	@Override
	public void chapterEdit(PaperQuestion chapter) {
		//校验数据有效性
		PaperQuestion entity = paperQuestionService.getEntity(chapter.getId());
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		
		// 修改章节
		entity.setName(chapter.getName());
		entity.setDescription(chapter.getDescription());
		entity.setUpdateTime(new Date());
		entity.setUpdateUserId(getCurUser().getId());
		paperQuestionService.update(entity);
	}
	
	@Override
	public void chapterDel(Integer id) {
		//校验数据有效性
		PaperQuestion entity = paperQuestionService.getEntity(id);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
				
		//删除章节
		List<PaperQuestion> questionList = paperQuestionService.getQuestionList(id);
		for(PaperQuestion pq : questionList) {
			paperQuestionService.del(pq.getId());
		}
		PaperQuestion chapter = paperQuestionService.getEntity(id);//不要放到下一行，因为第二行执行删除了。
		paperQuestionService.del(id);
		
		//更新总分数
		updateTotalScore(chapter.getPaperId());
	}
	
	@Override
	public void chapterUp(Integer chapterId) {
		//校验数据有效性
		PaperQuestion entity = paperQuestionService.getEntity(chapterId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		
		//当前章节上移
		PaperQuestion chapter = paperQuestionService.getEntity(chapterId);
		List<PaperQuestion> chapterList = paperQuestionService.getChapterList(chapter.getPaperId());
		Collections.sort(chapterList, new Comparator<PaperQuestion>() {
			@Override
			public int compare(PaperQuestion o1, PaperQuestion o2) {
				return o2.getNo() - o1.getNo();
			}
		});
		
		for(PaperQuestion cur : chapterList) {
			if(chapter.getNo() > cur.getNo()) {
				Integer no = cur.getNo();
				cur.setNo(chapter.getNo());
				chapter.setNo(no);
				paperQuestionService.update(cur);
				paperQuestionService.update(chapter);
				break;
			}
		}
	}

	@Override
	public void chapterDown(Integer chapterId) {
		//校验数据有效性
		PaperQuestion entity = paperQuestionService.getEntity(chapterId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		
		//当前章节下移
		PaperQuestion chapter = paperQuestionService.getEntity(chapterId);
		List<PaperQuestion> chapterList = paperQuestionService.getChapterList(chapter.getPaperId());
		Collections.sort(chapterList, new Comparator<PaperQuestion>() {
			@Override
			public int compare(PaperQuestion o1, PaperQuestion o2) {
				return o1.getNo() - o2.getNo();
			}
		});
		
		for(PaperQuestion cur : chapterList) {
			if(chapter.getNo() < cur.getNo()) {
				Integer no = cur.getNo();
				cur.setNo(chapter.getNo());
				chapter.setNo(no);
				paperQuestionService.update(cur);
				paperQuestionService.update(chapter);
				break;
			}
		}
	}
	
	private void updateTotalScore(Integer paperId) {
		BigDecimalUtil bigDecimalUtil = BigDecimalUtil.newInstance(0);
		List<PaperQuestion> paperQuestionList = paperQuestionService.getList(paperId);
		for (PaperQuestion paperQuestion : paperQuestionList) {
			if (paperQuestion.getType() != 2) {
				continue;
			}

			bigDecimalUtil.add(paperQuestion.getScore());
		}

		Paper paper = getEntity(paperId);
		paper.setTotalScore(bigDecimalUtil.getResult());
		update(paper);
	}
	
	@Override
	public List<PaperQuestionEx> getPaperList(Integer id) {
		List<PaperQuestion> paperQuestionList = paperDao.getPaperQuestionList(id);
		List<PaperQuestionEx> paperQuestionExList = new ArrayList<PaperQuestionEx>();
		List<Question> questionList = paperDao.getQuestionList(id);
		
		Map<Integer, PaperQuestionEx> paperQuestionExMap = new HashMap<Integer, PaperQuestionEx>();
		Map<Integer, Question> questionMap = new HashMap<Integer, Question>();
		for(Question question : questionList) {
			questionMap.put(question.getId(), question);
		}
		
		for(PaperQuestion paperQuestion : paperQuestionList) {
			PaperQuestionEx paperQuestionEx = new PaperQuestionEx();
			try {
				BeanUtils.copyProperties(paperQuestionEx, paperQuestion);
			} catch (Exception e) {
				new MyException(e);
			}
			paperQuestionEx.setQuestion(questionMap.get(paperQuestionEx.getQuestionId()));
			paperQuestionExList.add(paperQuestionEx);
			paperQuestionExMap.put(paperQuestionEx.getId(), paperQuestionEx);
		}
		
		List<PaperQuestionEx> treeList = new ArrayList<PaperQuestionEx>();
		for(PaperQuestionEx paperQuestionEx : paperQuestionExList) {
			if(paperQuestionEx.getParentId() == 0) {
				treeList.add(paperQuestionEx);
			}else{
				PaperQuestionEx parentPaperQuestionEx = paperQuestionExMap.get(paperQuestionEx.getParentId());
				parentPaperQuestionEx.getSubList().add(paperQuestionEx);
			}
		}
		
		return treeList;
	}

	@Override
	public void questionAdd(Integer chapterId, Integer[] questionIds) {
		// 校验数据有效性
		if (chapterId == null) {
			throw new MyException("无法获取参数：chapterId");
		}
		if (!ValidateUtil.isValid(questionIds)) {
			throw new MyException("无法获取参数：questionIds");
		}
		PaperQuestion entity = paperQuestionService.getEntity(chapterId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}

		// 添加试题
		PaperQuestion chapter = paperQuestionService.getEntity(chapterId);
		List<PaperQuestion> questionList = paperQuestionService.getQuestionList(chapterId);
		int maxNo = questionList.size();
		for (Integer questionId : questionIds) {
			Question question = questionService.getEntity(questionId);

			PaperQuestion pq = new PaperQuestion();
			pq.setUpdateTime(new Date());
			pq.setUpdateUserId(getCurUser().getId());
			pq.setPaperId(chapter.getPaperId());
			pq.setParentId(chapterId);
			pq.setQuestionId(questionId);
			pq.setType(2);
			pq.setNo(++maxNo);
			pq.setScore(question.getScore());
			pq.setScoreOptions(question.getScoreOptions());
			paperQuestionService.add(pq);
		}
		
		//更新总分数
		updateTotalScore(chapter.getPaperId());
	}
	
	@Override
	public void scoreUpdate(Integer paperQuestionId, BigDecimal score) {
		//校验数据有效性
		if(paperQuestionId == null) {
			throw new MyException("无法获取参数：paperQuestionId");
		}
		if(score == null) {
			throw new MyException("无法获取参数：score");
		}
		PaperQuestion entity = paperQuestionService.getEntity(paperQuestionId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		
		// 设置分数
		PaperQuestion pq = paperQuestionService.getEntity(paperQuestionId);
		pq.setScore(score);
		paperQuestionService.update(pq);
		
		//更新试卷总分
		updateTotalScore(pq.getPaperId());
	}
	
	@Override
	public void optionsUpdate(Integer paperQuestionId, Integer[] scoreOptions) {
		// 校验数据有效性
		if (paperQuestionId == null) {
			throw new MyException("无法获取参数：paperQuestionId");
		}
		PaperQuestion entity = paperQuestionService.getEntity(paperQuestionId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}

		// 更新试卷选项
		PaperQuestion pq = paperQuestionService.getEntity(paperQuestionId);
		Question question = questionService.getEntity(pq.getQuestionId());
		if (question.getType() == 2) {
			if (ValidateUtil.isValid(scoreOptions) && StringUtil.join(scoreOptions).contains("1")) {
				pq.setScoreOptions("1");
			} else {
				pq.setScoreOptions(null);
			}
		} else if (question.getType() == 3) {
			pq.setScoreOptions(StringUtil.join(scoreOptions));
		} else {
			pq.setScoreOptions(null);
		}

		paperQuestionService.update(pq);
	}
	
	@Override
	public void questionUp(Integer paperQuestionId) {
		//校验数据有效性
		if(paperQuestionId == null) {
			throw new MyException("无法获取参数：paperQuestionId");
		}
		PaperQuestion entity = paperQuestionService.getEntity(paperQuestionId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		
		//当前试题上移
		PaperQuestion pq = paperQuestionService.getEntity(paperQuestionId);
		List<PaperQuestion> pqList = paperQuestionService.getQuestionList(pq.getParentId());
		
		Collections.sort(pqList, new Comparator<PaperQuestion>() {
			@Override
			public int compare(PaperQuestion o1, PaperQuestion o2) {
				return o2.getNo() - o1.getNo();
			}
		});
		
		for(PaperQuestion cur : pqList) {
			if(pq.getNo() > cur.getNo()) {
				Integer no = cur.getNo();
				cur.setNo(pq.getNo());
				pq.setNo(no);
				paperQuestionService.update(cur);
				paperQuestionService.update(pq);
				break;
			}
		}
	}

	@Override
	public void questionDown(Integer paperQuestionId) {
		//校验数据有效性
		if(paperQuestionId == null) {
			throw new MyException("无法获取参数：paperQuestionId");
		}
		PaperQuestion entity = paperQuestionService.getEntity(paperQuestionId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		
		//当前试题下移
		PaperQuestion pq = paperQuestionService.getEntity(paperQuestionId);
		List<PaperQuestion> pqList = paperQuestionService.getQuestionList(pq.getParentId());
		
		Collections.sort(pqList, new Comparator<PaperQuestion>() {
			@Override
			public int compare(PaperQuestion o1, PaperQuestion o2) {
				return o1.getNo() - o2.getNo();
			}
		});
		
		for(PaperQuestion cur : pqList) {
			if(pq.getNo() < cur.getNo()) {
				Integer no = cur.getNo();
				cur.setNo(pq.getNo());
				pq.setNo(no);
				paperQuestionService.update(cur);
				paperQuestionService.update(pq);
				break;
			}
		}
	}

	@Override
	public void questionDel(Integer paperQuestionId) {
		// 校验数据有效性
		PaperQuestion entity = paperQuestionService.getEntity(paperQuestionId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}
		
		// 删除试题
		PaperQuestion pq = paperQuestionService.getEntity(paperQuestionId);
		paperQuestionService.del(paperQuestionId);
		List<PaperQuestion> pqList = paperQuestionService.getQuestionList(pq.getParentId());

		Collections.sort(pqList, new Comparator<PaperQuestion>() {
			@Override
			public int compare(PaperQuestion o1, PaperQuestion o2) {
				return o1.getNo() - o2.getNo();
			}
		});

		// 更新排序
		int maxNo = 1;
		for (PaperQuestion cur : pqList) {
			cur.setNo(maxNo++);
			paperQuestionService.update(cur);
		}
		
		// 更新总分数
		updateTotalScore(pq.getPaperId());
	}

	@Override
	public void questionClear(Integer chapterId) {
		// 校验数据有效性
		if (chapterId == null) {
			throw new MyException("无法获取参数：chapterId");
		}
		PaperQuestion entity = paperQuestionService.getEntity(chapterId);
		Paper paper = getEntity(entity.getPaperId());
		if (paper.getState() == 0) {
			throw new MyException("试卷已删除");
		}
		if (paper.getState() == 1) {
			throw new MyException("试卷已发布");
		}

		// 清空试题
		PaperQuestion chapter = paperQuestionService.getEntity(chapterId);
		List<PaperQuestion> pqList = paperQuestionService.getQuestionList(chapterId);
		for (PaperQuestion pq : pqList) {
			paperQuestionService.del(pq.getId());
		}

		// 更新总分数
		updateTotalScore(chapter.getPaperId());
	}

	@Override
	public List<Question> getQuestionList(Integer paperId) {
		return paperDao.getQuestionList(paperId);
	}

	@Override
	public List<Paper> getList(Integer paperTypeId) {
		return paperDao.getList(paperTypeId);
	}

	@Override
	public void batchScoreUpdate(Integer chapterId, BigDecimal score, String options) {
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
		List<PaperQuestion> pqList = paperQuestionService.getQuestionList(chapterId);
		for (PaperQuestion pq : pqList) {
			pq.setScoreOptions(null);
			Question question = questionService.getEntity(pq.getQuestionId());
			if (question.getType() == 2) {
				if (ValidateUtil.isValid(options) && options.contains("1")) {
					pq.setScoreOptions("1");
				}
			} else if (question.getType() == 3) {
				pq.setScoreOptions(options);
			}

			pq.setScore(score);
			paperQuestionService.update(pq);
		}

		// 更新试卷总分
		if (pqList.size() > 0) {
			updateTotalScore(pqList.get(0).getPaperId());
		}
	}
}
