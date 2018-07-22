package com.pns.sgdg.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.pns.sgdg.annotation.CheckPermission;
import com.pns.sgdg.common.constant.Constant;
import com.pns.sgdg.dao.CustomerDAO;
import com.pns.sgdg.dao.UserDAO;
import com.pns.sgdg.exception.ForbiddenException;
import com.pns.sgdg.model.Me;
import com.pns.sgdg.security.UserSession;

@Order(100)
@Aspect
public class PermissionAspect {

	@Autowired
	UserSession userSession;
	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	UserDAO userDAO;

	@Before(value = "@annotation(checkPermission)", argNames = "checkPermission")
	public void checkPermission(final CheckPermission checkPermission) throws Throwable {
		Me me = new Me();
		if (userSession.getRoleId() == Constant.Role.CUSTOMER) {
			me = customerDAO.getMe(userSession.getId());
			if (me == null || me.getId() == 0) {
				throw new ForbiddenException("Access Denied: No permission required.");
			}
		} else if (userSession.getRoleId() != Constant.Role.CUSTOMER) {
			me = userDAO.getMe(userSession.getId());
			if (me == null || me.getId() == 0) {
				throw new ForbiddenException("Access Denied: No permission required.");
			}
		}

		boolean isCorrectRole = false;
		if (userSession.getRoleId() != 0) {

			if (checkPermission.role() != Constant.Role.GUEST && userSession.getRoleId() != checkPermission.role()) {
				throw new ForbiddenException("Access Denied: No permission required.");
			}
			if (userSession.getRoleId() == checkPermission.role()) {
				isCorrectRole = true;
			}

			if (checkPermission.permission().length > 0 && !isCorrectRole) {
				boolean isValid = false;
				for (long p : checkPermission.permission()) {
					if (me.getPermissions().contains("," + p + ",")) {
						isValid = true;
					}
				}
				if (!isValid) {
					throw new ForbiddenException("Access Denied: No permission required.");
				}
			}
		}
	}
}
