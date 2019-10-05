package org.sdrc.dga.util;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Scope(value = "singleton")
public class SessionStateManager implements StateManager {

	public SessionStateManager() {
	}

	@Override
	public Object getValue(String key) {
		// return session().getAttribute(key);
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Override
	public void setValue(String key, Object value) {
		session().setAttribute(key, value);
	}

	

	private HttpSession session() {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		return attr.getRequest().getSession(true);
	}

}
