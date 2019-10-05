package org.sdrc.dga.security;

import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	// private Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private UserService userService;
	
	@Autowired
	org.sdrc.dga.util.DomainToModelConverter domainToModelConverter;

	/*
	 * 
	 * 
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 * 
	 * Here,we are overriding spring security loadbyUsername method. Note: We cannot apply @Transactional directly because,
	 * Spring uses JDK-Proxy by default.So AspectJ required for class Injection directly from container.
	 * 
	 * Authorities are ADDED IN format of : ROLE_NAME:Feature,Permission. So @PreAuthorize annotation will be like
	 * 
	 * @PreAuthorize("hasRole('CCI:data_entry,edit')")
	 * 
	 * Note : If you want to remove Role specifics, you can remove Role Name from GrantedAuthority
	 * 
	 * @PreAuthorize("hasRole('data_entry,edit')")
	 */

	@Override
	@Transactional
	public CollectUserModel loadUserByUsername(String userName) throws UsernameNotFoundException {
		CollectUserModel user = userService.findByUserName(userName);

		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password !");
		}
		

		
		return user;
	}

}