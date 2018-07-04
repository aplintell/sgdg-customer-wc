package com.pns.sgdg.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.pns.sgdg.annotation.CheckPermission;
import com.pns.sgdg.exception.ForbiddenException;
import com.pns.sgdg.security.UserSession;

@Order(100)
@Aspect
public class PermissionAspect {

	@Autowired
	UserSession userSession;

	@Before(value = "@annotation(checkPermission)", argNames = "checkPermission")
	public void checkPermission(final CheckPermission checkPermission) throws Throwable {
		if (userSession.getId() == 0) {
			throw new ForbiddenException("Access Denied: No permission required.");
		}
		System.out.println("usession :" + userSession);
		System.out.println("usession :" + userSession.getLoginId());
		System.out.println("checkPermission.permission().length :" + checkPermission.permission().length);
		System.out.println("checkPermission.role()" + checkPermission.role());
	}
}
