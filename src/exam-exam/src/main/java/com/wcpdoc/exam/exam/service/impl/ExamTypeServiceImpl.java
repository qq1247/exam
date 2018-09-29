package com.wcpdoc.exam.exam.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.wcpdoc.exam.core.dao.BaseDao;
import com.wcpdoc.exam.core.entity.LoginUser;
import com.wcpdoc.exam.core.entity.PageIn;
import com.wcpdoc.exam.core.entity.PageOut;
import com.wcpdoc.exam.core.service.impl.BaseServiceImp;
import com.wcpdoc.exam.core.util.ValidateUtil;
import com.wcpdoc.exam.exam.dao.ExamTypeAuthDao;
import com.wcpdoc.exam.exam.dao.ExamTypeDao;
import com.wcpdoc.exam.exam.entity.ExamType;
import com.wcpdoc.exam.exam.entity.ExamTypeAuth;
import com.wcpdoc.exam.exam.service.ExamTypeExService;
import com.wcpdoc.exam.exam.service.ExamTypeService;
import com.wcpdoc.exam.sys.service.OrgService;
import com.wcpdoc.exam.sys.service.PostService;
/**
 * 考试分类服务层实现
 * 
 * v1.0 zhanghc 2017-06-28 21:34:41
 */
@Service
public class ExamTypeServiceImpl extends BaseServiceImp<ExamType> implements ExamTypeService{
	@Resource
	private ExamTypeDao examTypeDao;
	@Resource
	private ExamTypeAuthDao examTypeAuthDao;
	@Resource
	private ExamTypeExService examTypeExService;
	@Resource
	private OrgService orgService;
	@Resource
	private PostService postService;

	@Override
	@Resource(name = "examTypeDaoImpl")
	public void setDao(BaseDao<ExamType> dao) {
		super.dao = dao;
	}

	@Override
	public void saveAndUpdate(ExamType examType) {
		//校验数据有效性
		if(!ValidateUtil.isValid(examType.getName())){
			throw new RuntimeException("无法获取参数：examType.name");
		}
		if(existName(examType)){
			throw new RuntimeException("名称已存在！");
		}
				
		//保存考试分类
		if(examType.getParentId() == null){
			examType.setParentId(0);
		}
		examTypeDao.save(examType);
		
		//更新父子关系
		ExamType parentExamType = examTypeDao.getEntity(examType.getParentId());
		if(parentExamType == null){
			examType.setParentSub("_" + examType.getId() + "_");
		}else {
			examType.setParentSub(parentExamType.getParentSub() + examType.getId() + "_");
		}
		examTypeDao.update(examType);
	}
	
	@Override
	public void editAndUpdate(ExamType examType) {
		//校验数据有效性
		if(!ValidateUtil.isValid(examType.getName())){
			throw new RuntimeException("无法获取参数：examType.name");
		}
		if(existName(examType)){
			throw new RuntimeException("名称已存在！");
		}
		
		//修改考试分类
		examTypeDao.update(examType);
	}

	@Override
	public void delAndUpdate(Integer[] ids) {
		//校验数据有效性
		if(!ValidateUtil.isValid(ids)){
			throw new RuntimeException("无法获取参数：ids");
		}
		
		//删除考试分类，不包括根考试分类
		for(Integer id : ids){
			if(id == 1){
				continue;
			}
			
			List<ExamType> examTypeList = examTypeDao.getAllSubExamTypeList(id);
			for(ExamType examType : examTypeList){
				if(examType.getState().equals("0")){
					continue;
				}
				
				examType.setState(0);
				examTypeDao.update(examType);
				
				examTypeExService.delAndUpdate(examType);
			}
		}
	}

	@Override
	public List<Map<String, Object>> getTreeList() {
		return examTypeDao.getTreeList();
	}

	@Override
	public void doMove(Integer sourceId, Integer targetId) {
		//校验数据有效性
		if(sourceId == null){
			throw new RuntimeException("无法获取参数：sourceId");
		}
		if(targetId == null){
			throw new RuntimeException("无法获取参数：targetId");
		}
		
		//移动考试分类
		examTypeDao.doMove(sourceId, targetId);
	}

	/**
	 * 名称是否已存在
	 * v1.0 zhanghc 2016-5-24下午14:54:09
	 * @param examType
	 * @return boolean
	 */
	private boolean existName(ExamType examType){
		//校验数据有效性
		if(examType == null){
			throw new RuntimeException("无法获取参数：examType");
		}
		if(!ValidateUtil.isValid(examType.getName())){
			throw new RuntimeException("无法获取参数：examType.name");
		}
		
		//如果是添加
		ExamType examType2 = examTypeDao.getExamTypeByName(examType.getName());
		if(examType2 != null){
			examTypeDao.evict(examType2);
		}
		
		if(examType.getId() == null){
			if(examType2 != null){
				return true;
			}
			return false;
		}
		
		//如果是修改
		if(examType2 != null && !examType.getId().equals(examType2.getId())){
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> getOrgTreeList() {
		return orgService.getTreeList();
	}

	@Override
	public PageOut getAuthUserListpage(PageIn pageIn) {
		return examTypeDao.getAuthUserListpage(pageIn);
	}

	@Override
	public PageOut getAuthUserAddList(PageIn pageIn) {
		return examTypeDao.getAuthUserAddList(pageIn);
	}

	@Override
	public void doAuthUserAdd(Integer id, Integer[] userIds, boolean syn2Sub, LoginUser user) {
		//校验数据有效性
		if(id == null){
			throw new RuntimeException("无法获取参数：id");
		}
		if(!ValidateUtil.isValid(userIds)){
			throw new RuntimeException("无法获取参数：userIds");
		}
		
		//添加权限用户
		if(syn2Sub){
			List<ExamType> examTypeList = getList(id);
			for(ExamType examType : examTypeList){
				doAuthUserAdd(examType.getId(), userIds, syn2Sub, user);
			}
		}
		
		ExamTypeAuth auth = examTypeAuthDao.getEntity(id);
		if(auth != null){
			StringBuilder _userIds = new StringBuilder();
			if(ValidateUtil.isValid(auth.getUserIds())){
				_userIds.append(auth.getUserIds());
			}else{
				_userIds.append(",");
			}
			for(Integer userId : userIds){
				if(_userIds.toString().contains("," + userId + ",")){
					continue;
				}
				_userIds.append(userId).append(",");
			}
			auth.setUserIds(_userIds.toString());
			auth.setUpdateTime(new Date());
			auth.setUpdateUserId(user.getId());
			examTypeAuthDao.update(auth);
			return;
		}
		
		auth = new ExamTypeAuth();
		StringBuilder _userIds = new StringBuilder(",");
		for(Integer userId : userIds){
			_userIds.append(userId).append(",");
		}
		auth.setId(id);
		auth.setUserIds(_userIds.toString());
		auth.setUpdateTime(new Date());
		auth.setUpdateUserId(user.getId());
		examTypeAuthDao.save(auth);
	}

	private List<ExamType> getList(Integer id) {
		return examTypeDao.getList(id);
	}

	@Override
	public void doAuthUserDel(Integer id, Integer[] userIds, boolean syn2Sub, LoginUser user) {
		//校验数据有效性
		if(id == null){
			throw new RuntimeException("无法获取参数：id");
		}
		if(!ValidateUtil.isValid(userIds)){
			throw new RuntimeException("无法获取参数：userIds");
		}
		
		if(syn2Sub){
			List<ExamType> examTypeList = getList(id);
			for(ExamType examType : examTypeList){
				doAuthUserDel(examType.getId(), userIds, syn2Sub, user);
			}
		}
		
		ExamTypeAuth examTypeAuth = examTypeAuthDao.getEntity(id);
		if(examTypeAuth == null){
			return;
		}
		
		String _userIds = examTypeAuth.getUserIds();
		if(!ValidateUtil.isValid(_userIds)){
			return;
		}
		for(Integer userId : userIds){
			_userIds = _userIds.replaceAll("," + userId + ",", ",");//,2,4,5,55,32,32,
		}
		if(_userIds.equals(",")){
			_userIds = null;
		}
		
		examTypeAuth.setUserIds(_userIds);
		examTypeAuthDao.update(examTypeAuth);
	}

	@Override
	public void doAuthOrgUpdate(Integer id, Integer[] orgIds, boolean syn2Sub, LoginUser user) {
		//校验数据有效性
		if(id == null){
			throw new RuntimeException("无法获取参数：id");
		}
//		if(!ValidateUtil.isValid(orgIds)){
//			throw new RuntimeException("无法获取参数：orgIds");//全不选就是空。
//		}
		
		//添加权限机构
		if(syn2Sub){
			List<ExamType> examTypeList = getList(id);
			for(ExamType examType : examTypeList){
				doAuthOrgUpdate(examType.getId(), orgIds, syn2Sub, user);
			}
		}
		
		ExamTypeAuth auth = examTypeAuthDao.getEntity(id);
		if(auth != null){
			StringBuilder _orgIds = new StringBuilder(",");
			for(Integer orgId : orgIds){
				_orgIds.append(orgId).append(",");
			}
			if(_orgIds.toString().equals(",")){
				auth.setOrgIds(null);
			}else{
				auth.setOrgIds(_orgIds.toString());
			}
			auth.setUpdateTime(new Date());
			auth.setUpdateUserId(user.getId());
			examTypeAuthDao.update(auth);
			return;
		}
		
		auth = new ExamTypeAuth();
		StringBuilder _orgIds = new StringBuilder(",");
		for(Integer orgId : orgIds){
			_orgIds.append(orgId).append(",");
		}
		if(_orgIds.toString().equals(",")){
			auth.setOrgIds(null);
		}else{
			auth.setOrgIds(_orgIds.toString());
		}
		auth.setId(id);
		auth.setUpdateTime(new Date());
		auth.setUpdateUserId(user.getId());
		examTypeAuthDao.save(auth);
	}

	@Override
	public ExamTypeAuth getExamTypeAuth(Integer examTypeAuthId) {
		return examTypeAuthDao.getEntity(examTypeAuthId);
	}

	@Override
	public List<Map<String, Object>> getOrgPostTreeList() {
		return postService.getOrgPostTreeList();
	}

	@Override
	public void doAuthPostUpdate(Integer id, Integer[] postIds, boolean syn2Sub, LoginUser user) {
		//校验数据有效性
		if(id == null){
			throw new RuntimeException("无法获取参数：id");
		}
//		if(!ValidateUtil.isValid(postIds)){
//			throw new RuntimeException("无法获取参数：postIds");//全不选就是空。
//		}
		
		//添加权限机构
		if(syn2Sub){
			List<ExamType> examTypeList = getList(id);
			for(ExamType examType : examTypeList){
				doAuthPostUpdate(examType.getId(), postIds, syn2Sub, user);
			}
		}
		
		ExamTypeAuth auth = examTypeAuthDao.getEntity(id);
		if(auth != null){
			StringBuilder _postIds = new StringBuilder(",");
			for(Integer postId : postIds){
				_postIds.append(postId).append(",");
			}
			if(_postIds.toString().equals(",")){
				auth.setPostIds(null);
			}else{
				auth.setPostIds(_postIds.toString());
			}
			auth.setUpdateTime(new Date());
			auth.setUpdateUserId(user.getId());
			examTypeAuthDao.update(auth);
			return;
		}
		
		auth = new ExamTypeAuth();
		StringBuilder _postIds = new StringBuilder(",");
		for(Integer postId : postIds){
			_postIds.append(postId).append(",");
		}
		if(_postIds.toString().equals(",")){
			auth.setPostIds(null);
		}else{
			auth.setPostIds(_postIds.toString());
		}
		auth.setId(id);
		auth.setUpdateTime(new Date());
		auth.setUpdateUserId(user.getId());
		examTypeAuthDao.save(auth);
	}

	@Override
	public List<ExamType> getList() {
		return examTypeDao.getList();
	}

	@Override
	public List<ExamTypeAuth> getExamTypeAuthList() {
		return examTypeAuthDao.getList();
	}
}
