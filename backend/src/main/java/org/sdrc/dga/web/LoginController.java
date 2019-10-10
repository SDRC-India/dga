package org.sdrc.dga.web;

import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author Harsh
 * @since version 1.0.0.0
 *
 */

@Controller
public class LoginController {
	

	
	@Autowired
	UserService userService;

	
	@GetMapping(value="/updateTable")
	public boolean updateTable()
	{
		return userService.insertUserTable();
	}
	

	@GetMapping(value="/user")
	@ResponseBody
	public CollectUserModel login()
	{
		return (CollectUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}


	
	
}
