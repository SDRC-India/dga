package org.sdrc.dga.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.model.ChangePasswordModel;
/**
 * @author Harsh(harsh@sdrc.co.in)
 */
import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.model.FormsToDownloadMediafiles;
import org.sdrc.dga.model.MediaFilesToUpdate;
import org.sdrc.dga.model.ModelToCollectApplication;
import org.sdrc.dga.model.ProgramXFormsModel;
import org.springframework.http.ResponseEntity;

public interface UserService {
	CollectUserModel findByUserName(String userName);
	
	
	List<ProgramXFormsModel> getProgramWithXFormsList(String username, String password);	
	boolean insertUserTable();
	ModelToCollectApplication getModelToCollectApplication(List<FormsToDownloadMediafiles> list,String username,String password);
	List<MediaFilesToUpdate> getMediaFilesToUpdate(List<FormsToDownloadMediafiles> list);


	/**
	 * This service method will update the passowrd of the user
	 * @param collectUserModel
	 * @return
	 */
	boolean updatePassword(CollectUserModel collectUserModel);
	
	
	boolean configureUserDatabase();


	ResponseEntity<String> changePassoword(ChangePasswordModel changePasswordModel, Principal p);


	List<JSONObject> getAllUserRoles();


	List<JSONObject> getUsersByRoleId(int roleId);


	ResponseEntity<Boolean> resetPassword(Map<String, Object> resetPasswordMap, Principal p);


	ResponseEntity<String> enableUserName(String userId, Principal p);


	ResponseEntity<String> disableUserName(String userId, Principal p);


	Map<String, List<AreaModel>> getAllAreaList();


	ResponseEntity<String> createUser(Map<String, Object> map, Principal p);
}
