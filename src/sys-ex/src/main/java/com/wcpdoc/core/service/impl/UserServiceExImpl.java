package com.wcpdoc.core.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wcpdoc.auth.cache.TokenCache;
import com.wcpdoc.auth.realm.JWTRealm;
import com.wcpdoc.auth.util.JwtUtil;
import com.wcpdoc.base.entity.User;
import com.wcpdoc.base.service.UserExService;
import com.wcpdoc.core.util.DateUtil;

/**
 * 用户上下文服务层实现
 * 
 * v1.0 zhanghc 2021年10月15日下午1:44:08
 */
@Service
public class UserServiceExImpl implements UserExService {
	private static final Logger log = LoggerFactory.getLogger(UserServiceExImpl.class);
	@Value("${spring.profiles.active}")
	private String active;
	@Value("${token.expireMinute}")
	private Integer tokenExpireMinute;

	@Resource
	private JWTRealm jwtRealm;

	@Override
	public void roleUpdate(Integer userId) {
		jwtRealm.clearAuth(userId);// 重新授权
	}

	@Override
	public String generateToken(User user) {
		Date curTime = new Date();
		Long tokenId = curTime.getTime();
		Date expTime = DateUtil.getNextMinute(new Date(), tokenExpireMinute);
		String token = JwtUtil.getInstance()
				.createToken(tokenId.toString(), active, expTime)
				.addAttr("userId", user.getId())
				.addAttr("loginName", user.getLoginName())
				.build();
		if (log.isDebugEnabled()) {
			log.debug("shiro权限：用户【{}】登陆，旧令牌创建时间【{}】，当前令牌创建时间【{}】", 
					user.getLoginName(), null, DateUtil.formatDateTime(new Date(tokenId)));
		}
		
		return token;
	}

	@Override
	public void refreshToken(User user, String token) {
		TokenCache.put(user.getId(), token);// 缓存刷新令牌（用于续租登陆）
	}
}
