package com.wcpdoc.exam.home.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wcpdoc.exam.core.dao.BaseDao;
import com.wcpdoc.exam.core.entity.LoginUser;
import com.wcpdoc.exam.core.service.impl.BaseServiceImp;
import com.wcpdoc.exam.exam.service.QuestionTypeService;
import com.wcpdoc.exam.file.entity.FileEx;
import com.wcpdoc.exam.file.service.FileService;
import com.wcpdoc.exam.home.service.HomeQuestionService;
import com.wcpdoc.exam.sys.service.UserService;

/**
 * 试题服务层实现
 * 
 * v1.0 zhanghc 2018年6月3日上午10:34:21
 */
@Service
public class HomeQuestionServiceImpl extends BaseServiceImp<Object> implements HomeQuestionService {
	@Resource
	private QuestionTypeService questionTypeService;
	@Resource
	private UserService userService;
	@Resource
	private FileService fileService;

	@Override
	public void setDao(BaseDao<Object> dao) {
		
	}

	@Override
	public List<Map<String, Object>> getQuestionTypeTreeList(Integer userId) {
		return null;
//		//获取授权相关数据
//		Org org = userService.getOrg(userId);
//		List<Post> postList = userService.getPostList(userId);
//		List<QuestionType> questionTypeList = questionTypeService.getList();
//		List<QuestionTypeAuth> questionTypeAuthList = questionTypeService.getQuestionTypeAuthList();
//		
//		Map<Integer, QuestionTypeAuth> questionTypeAuthCache = new HashMap<Integer, QuestionTypeAuth>();
//		for(QuestionTypeAuth questionTypeAuth : questionTypeAuthList){
//			questionTypeAuthCache.put(questionTypeAuth.getId(), questionTypeAuth);
//		}
//		
//		//封装成树形数据
//		List<Map<String, Object>> questionTypeTreeList = new ArrayList<Map<String,Object>>();
//		for(QuestionType questionType : questionTypeList){
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("ID", questionType.getId());
//			map.put("NAME", questionType.getName());
//			map.put("PARENT_ID", questionType.getParentId());
//			//map.put("DISABLED", true);
//			//map.put("EXPANDED", true);
//			
//			if(userId == 1){//如果是系统管理员
//				questionTypeTreeList.add(map);
//				continue;
//			}
//			
//			QuestionTypeAuth questionTypeAuth = questionTypeAuthCache.get(questionType.getId());
//			if(questionTypeAuth == null){//如果没有设置权限
//				continue;
//			}
//			
//			if(questionTypeAuth.getUserIds() != null 
//					&& questionTypeAuth.getUserIds().contains(userId.toString())){//有用户权限
//				questionTypeTreeList.add(map);
//				continue;
//			}
//			if(questionTypeAuth.getOrgIds() != null 
//					&& questionTypeAuth.getOrgIds().contains(org.getId().toString())){//有机构权限
//				questionTypeTreeList.add(map);
//				continue;
//			}
//			
//			for(Post post : postList){
//				if(questionTypeAuth.getPostIds() != null 
//						&& questionTypeAuth.getPostIds().contains(post.getId().toString())){//有岗位权限
//					questionTypeTreeList.add(map);
//					break;
//				}
//			}
//		}
//		
//		return questionTypeTreeList;
	}

	@Override
	public String doTempUpload(MultipartFile[] files, String[] allowTypes, LoginUser user, String ip) {
		return fileService.doTempUpload(files, allowTypes, user, ip);
	}

	@Override
	public void doUpload(Integer id, LoginUser user, String ip) throws Exception {
		fileService.doUpload(id, user, ip);
	}

	@Override
	public FileEx getFileEx(Integer fileId) {
		return fileService.getEntityEx(fileId);
	}
}
