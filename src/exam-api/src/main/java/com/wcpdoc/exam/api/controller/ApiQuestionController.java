package com.wcpdoc.exam.api.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wcpdoc.exam.base.cache.DictCache;
import com.wcpdoc.exam.core.constant.ConstantManager;
import com.wcpdoc.exam.core.controller.BaseController;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.entity.PageResult;
import com.wcpdoc.exam.core.entity.PageResultEx;
import com.wcpdoc.exam.core.entity.Question;
import com.wcpdoc.exam.core.entity.QuestionOption;
import com.wcpdoc.exam.core.exception.MyException;
import com.wcpdoc.exam.core.service.QuestionOptionService;
import com.wcpdoc.exam.core.service.QuestionService;
import com.wcpdoc.exam.core.service.QuestionTypeService;
import com.wcpdoc.exam.core.util.ValidateUtil;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * 试题控制层
 * 
 * v1.0 zhanghc 2017-05-07 14:56:29
 */
@Controller
@RequestMapping("/api/question")
public class ApiQuestionController extends BaseController {
	private static final Logger log = LoggerFactory.getLogger(ApiQuestionController.class);

	@Resource
	private QuestionService questionService;
	@Resource
	private QuestionTypeService questionTypeService;
	@Resource
	private QuestionOptionService questionOptionService;
	
	/**
	 * 试题列表 
	 * 
	 * v1.0 zhanghc 2017-05-07 14:56:29
	 * @return pageOut
	 */
	@RequestMapping("/list")
	@ResponseBody
	public PageResult list(PageIn pageIn, Integer questionTypeId, Integer id, String title, Integer type, Integer difficulty, Double score, Double scoreStart, Double scoreEnd, String name) {
		try {
			if(questionTypeId != null){
				pageIn.setOne(questionTypeId.toString());
			}
			if(id != null){
				pageIn.setTwo(id.toString());
			}
			if(ValidateUtil.isValid(title)){
				pageIn.setThree(title);
			}
			if(type != null){
				pageIn.setFive(type.toString());
			}
			if(difficulty != null){
				pageIn.setSix(difficulty.toString());
			}
			if(ValidateUtil.isValid(name)){
				pageIn.setSeven(name);
			}
			if(score != null){
				pageIn.setEight(String.valueOf(score));
			}
			if(scoreStart != null){
				pageIn.setSortOne(String.valueOf(scoreStart));
			}
			if(scoreEnd != null){
				pageIn.setSortTwo(String.valueOf(scoreEnd));
			}
			
			if(!ConstantManager.ADMIN_LOGIN_NAME.equals(getCurUser().getLoginName())) {
				pageIn.setTen(getCurUser().getId().toString());
			}
			
			PageOut listpage = questionService.getListpage(pageIn);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for(Map<String, Object> map : listpage.getRows()){
				map.put("typeName", DictCache.getDictValue("questionType", map.get("type").toString()));
				map.put("difficultyName", DictCache.getDictValue("questionDifficulty", map.get("difficulty").toString()));
				
				//选择
				if (map.get("type").toString().equals("1") || map.get("type").toString().equals("2")) {
					List<QuestionOption> optionList = questionOptionService.getQuestionOptionList(Integer.valueOf(map.get("id").toString()));
					map.put("option", optionList);
				}
				list.add(map);
			}
			listpage.setRows(list);
			return PageResultEx.ok().data(listpage);
		} catch (Exception e) {
			log.error("试题列表错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 添加试题
	 * 
	 * v1.0 zhanghc 2017-05-07 14:56:29
	 * @return pageOut
	 */
	@RequestMapping("/add")
	@ResponseBody
	public PageResult add(Question question, String[] options) {
		try {
			questionService.addAndUpdate(question, options);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("添加试题错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		}  catch (Exception e) {
			log.error("添加试题错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 修改试题
	 * 
	 * v1.0 zhanghc 2017-05-07 14:56:29
	 * @param question 试题
	 * @param newVer 新版本
	 * @return PageResult
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public PageResult edit(Question question, boolean newVer, String[] options) {
		try {
			questionService.updateAndUpdate(question, newVer, options);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("修改试题错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		}  catch (Exception e) {
			log.error("修改试题错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 删除试题
	 * 
	 * v1.0 zhanghc 2017-05-07 14:56:29
	 * @param id
	 * @return pageOut
	 */
	@RequestMapping("/del")
	@ResponseBody
	public PageResult del(Integer id) {
		try {
			Question question = questionService.getEntity(id);
			question.setState(0);
			question.setUpdateTime(new Date());
			question.setUpdateUserId(getCurUser().getId());
			questionService.update(question);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("删除试题错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		}  catch (Exception e) {
			log.error("删除试题错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 拷贝试题
	 * 
	 * v1.0 zhanghc 2017-05-07 14:56:29
	 * @param id
	 * @return pageOut
	 */
	@RequestMapping("/copy")
	@ResponseBody
	public PageResult copy(Integer id) {
		try {
			Question question = questionService.getEntity(id);
			Question entity = new Question();
			BeanUtils.copyProperties(entity, question);
			entity.setUpdateTime(new Date());
			entity.setUpdateUserId(getCurUser().getId());
			questionService.add(entity);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("拷贝试题错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		}  catch (Exception e) {
			log.error("拷贝试题错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 预览试题
	 * 
	 * v1.0 zhanghc 2017-05-07 14:56:29
	 * @param id
	 * @return pageOut
	 */
	@RequestMapping("/get")
	@ResponseBody
	public PageResult get(Integer id) {
		try {
			Question question = questionService.getEntity(id);
			List<QuestionOption> questionOptionList = null;
			if (question.getType() == 1 || question.getType() == 2) {
				questionOptionList = questionOptionService.getQuestionOptionList(question.getId());
			}
			return PageResultEx.ok()
					.addAttr("id", question.getId())
					.addAttr("type", question.getType())
					.addAttr("difficulty", question.getDifficulty())
					.addAttr("title", question.getTitle())
					.addAttr("answer", question.getAnswer())
					.addAttr("analysis", question.getAnalysis())
					.addAttr("state", question.getState())
					.addAttr("updateUserId", question.getUpdateUserId())
					.addAttr("updateTime", question.getUpdateTime())
					.addAttr("questionTypeId", question.getQuestionTypeId())
					.addAttr("score", question.getScore())
					.addAttr("scoreOptions", question.getScoreOptions())
					.addAttr("ver", question.getVer())
					.addAttr("srcId", question.getSrcId())
					.addAttr("no", question.getNo())
					.addAttr("options", questionOptionList == null ? null : questionOptionList);
		} catch (MyException e) {
			log.error("预览试题错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		}  catch (Exception e) {
			log.error("预览试题错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * word试题导入
	 * 
	 * v1.0 zhanghc 2019年8月10日下午5:12:53
	 * @param file
	 * @param questionTypeId
	 * @return PageResult
	 */
	@RequestMapping("/wordImp")
	@ResponseBody
	public PageResult wordImp(@RequestParam("file") MultipartFile file, Integer questionTypeId) {
		try {
			questionService.wordImp(file, questionTypeId);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("导入试题错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("导入试题错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * word模板导出
	 * 
	 * v1.0 zhanghc 2019年8月14日下午5:33:20 
	 * void
	 */
	@RequestMapping(value = "/wordTemplateExport")
	public void wordTemplateExport() {
		OutputStream output = null;
		try {
			java.io.File file = new java.io.File(this.getClass().getResource("/").getPath() + "res/试题模板.doc");
			
			String fileName = new String((file.getName()).getBytes("UTF-8"), "ISO-8859-1");
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.setContentType("application/force-download");

			output = response.getOutputStream();
			FileUtils.copyFile(file, output);
		} catch (MyException e) {
			log.error("下载模板失败：{}", e.getMessage());
		} catch (Exception e) {
			log.error("下载模板失败：", e);
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
	/**
	 * 发布
	 * 
	 * v1.0 zhanghc 2018年11月24日上午9:13:22
	 * @param id
	 * @return PageResult
	 */
	@RequestMapping("/publish")
	@ResponseBody
	public PageResult publish(Integer id) {
		try {
			Question question = questionService.getEntity(id);
			if (question.getState() == 0) {
				throw new MyException("试题已删除！");
			}
			if (question.getState() == 1) {
				question.setState(2);
			} else if (question.getState() == 2) {
				question.setState(1);
			}

			question.setUpdateTime(new Date());
			question.setUpdateUserId(getCurUser().getId());
			questionService.update(question);
			return PageResult.ok();
		} catch (MyException e) {
			log.error("发布错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		} catch (Exception e) {
			log.error("发布错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * word试题导出
	 * 
	 * v1.0 zhanghc 2019年8月14日下午5:33:20 
	 * void
	 */
	@RequestMapping(value = "/wordQuestionExport")
	public Map<String, String> wordQuestionExport(String ids) {
		try {
		/*if (ids != null) {
		Map<String, String> map = new HashMap<>();
			throw new RuntimeException("参数无效：dbAddr");
		}*/
	
		Question entity = questionService.getEntity(26);

		Map<String, Object> mapList = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("typeName", "单选");
		map.put("difficultyName", "简单");
		map.put("title", entity.getTitle());
		
		map.put("optionA", "AAAAAAAAA");
		map.put("optionB", "BBBBBBBBB");
		map.put("optionC", "CCCCCCCCC");
		map.put("optionD", "DDDDDDDDD");
		map.put("optionE", "EEEEEEEEEE");
		map.put("optionF", "FFFFFFFFFF");
		map.put("optionG", "GGGGGGGGGG");
		
		map.put("answer", entity.getAnswer());
		map.put("score", "3.0");
		map.put("scoreOptions", "半对半错");
		map.put("analysis", entity.getAnalysis());
		list.add(map);
		mapList.put("list", list);
		
		Template template = null;
		try {
			//设置模板相对路径
			File file = new File(this.getClass().getResource("/").toURI().getPath() +"/res/");
			
			Configuration configuration =  new Configuration(Configuration.VERSION_2_3_22);
			configuration.setTemplateLoader(new FileTemplateLoader(file));
			configuration.setDefaultEncoding("UTF-8");
			configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
			configuration.setLogTemplateExceptions(false);
			configuration.setWrapUncheckedExceptions(true);
			
			template = configuration.getTemplate("template.doc.ftl");

		} catch (IOException e) {
			e.printStackTrace();
		}
		File outFile = new File("D:/outFile/outFile" + Math.random() * 10000 + ".docx"); // 导出文件
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			template.process(mapList, out); // 将填充数据填入模板文件并输出到目标文件
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
			return null;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
			return null;
	}
	
	/**
	 * 试题统计
	 * 
	 * v1.0 zhanghc 2017-05-07 14:56:29
	 * @param id
	 * @return pageOut
	 */
	@RequestMapping("/count")
	@ResponseBody
	@RequiresRoles("OP")
	public PageResult count(Integer questionTypeId) {
		try {
			return PageResultEx.ok().data(questionService.count(questionTypeId));
		} catch (MyException e) {
			log.error("试题统计错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		}  catch (Exception e) {
			log.error("试题统计错误：", e);
			return PageResult.err();
		}
	}
	
	/**
	 * 试题准确率
	 * 
	 * v1.0 zhanghc 2017-05-07 14:56:29
	 * @param id
	 * @return pageOut
	 */
	@RequestMapping("/accuracy")
	@ResponseBody
	@RequiresRoles("OP")
	public PageResult accuracy(Integer examId) {
		try {
			return PageResultEx.ok().data(questionService.accuracy(examId));
		} catch (MyException e) {
			log.error("试题统计错误：{}", e.getMessage());
			return PageResult.err().msg(e.getMessage());
		}  catch (Exception e) {
			log.error("试题统计错误：", e);
			return PageResult.err();
		}
	}
}