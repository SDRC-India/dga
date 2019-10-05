package org.sdrc.dga.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.model.ChangePasswordModel;
import org.sdrc.dga.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@PreAuthorize("hasAuthority('ChangePass,Edit')")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordModel changePasswordModel,Principal p) {

		return userService.changePassoword(changePasswordModel,p);

	}
	
	@RequestMapping(value = "/getAllUserRoles")
	@PreAuthorize("hasAuthority('UserMgmt,Edit')")
	public List<JSONObject> getAllUsersTypes() {

		return userService.getAllUserRoles();

	}
	
	@RequestMapping(value = "/getUsersByRoleId")
	@PreAuthorize("hasAuthority('UserMgmt,Edit')")
	public List<JSONObject> getUsersByRoleId(@RequestParam("roleId") String roleId) {

		return userService.getUsersByRoleId(Integer.parseInt(roleId));

	}
	
	@RequestMapping(value = "/resetPassword")
	@PreAuthorize("hasAuthority('UserMgmt,Edit')")
	public ResponseEntity<Boolean> resetPassword(@RequestBody Map<String,Object> resetPasswordMap,Principal p) {

		return userService.resetPassword(resetPasswordMap,p);
			
	}
	
	@RequestMapping(value = "/enableUser")
	@PreAuthorize("hasAuthority('UserMgmt,Edit')")
	public ResponseEntity<String> enableUser(@RequestParam("userId") String userId,Principal p) {

		return userService.enableUserName(userId,p);

	}
	
	@RequestMapping(value = "/disableUser")
	@PreAuthorize("hasAuthority('UserMgmt,Edit')")
	public ResponseEntity<String> disableUser(@RequestParam("userId") String userId,Principal p) {
		
		return userService.disableUserName(userId,p);
			
	}
	
	@RequestMapping(value = "/getAllArea")
	@PreAuthorize("hasAuthority('UserMgmt,Edit')")
	public Map<String, List<AreaModel>> getArea() {
		return userService.getAllAreaList();
	}
	
	@RequestMapping(value = "/createUser", method = RequestMethod.POST)
	@ResponseBody
	@PreAuthorize("hasAuthority('UserMgmt,Edit')")
	public ResponseEntity<String> createUser(@RequestBody Map<String, Object> map, Principal p) {

		return userService.createUser(map, p);

	}
	
}