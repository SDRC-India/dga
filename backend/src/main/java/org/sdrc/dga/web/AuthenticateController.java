package org.sdrc.dga.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.model.FormsToUpdate;
import org.sdrc.dga.model.ModelToCollectApplication;
import org.sdrc.dga.model.ProgramXFormsModel;
import org.sdrc.dga.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.pdf.codec.Base64;

/**
 * This controller class will handle all the request that are coming for login and XForm assignment and all.
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 * @since version 1.0.0.0
 *
 */

@Controller
public class AuthenticateController implements AuthenticationProvider{
	
	private static final Logger logger = LoggerFactory.getLogger(AuthenticateController.class);
	

	
	@Autowired
	private UserService userService;
	
	
	@PostMapping(value = "authenticate")
	public @ResponseBody List<ProgramXFormsModel> authenticate(@RequestParam("userString") String userString,HttpServletResponse response) throws IOException{
		
		byte[] decodedUserString = Base64.decode(userString);		
		String decodedUserStringString = new String (decodedUserString);		
		String splitArray[] = decodedUserStringString.split(":");
		
		if(splitArray.length == 2){
			String username = splitArray[0];
			String password = splitArray[1];
			try{
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			    this.authenticate(token);
				return userService.getProgramWithXFormsList(username, (String)token.getCredentials());				
			}catch(Exception e){
				logger.error("unauthorized server hit for login User String : " + userString);
				response.sendError(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
			}			
			
		}else{
			logger.error("invalid string length : " + userString);
			response.sendError(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
		}			
		return null;
	}

	
		@Override
		public Authentication authenticate(Authentication authentication)
				throws AuthenticationException {
			CollectUserModel collectUserModel = userService.findByUserName(authentication.getName());
			if (collectUserModel == null ||!collectUserModel.getPassword().equals((String)authentication.getCredentials())){
				throw new BadCredentialsException("Invalid User!");			
			}
			return new UsernamePasswordAuthenticationToken(authentication.getName(), (String)authentication.getCredentials(), null);
		}

		@Override
		public boolean supports(Class<?> authentication) {
			return false;
		}
		/**
		 * This controller class will update all the form and all media files.
		 * @author Subhadarshani Patra (subhadarshani@sdrc.co.in)
		 * @since version 1.0.0.0
		 *
		 */
		
		@PostMapping(value = "update")
		@ResponseBody
		public ModelToCollectApplication update(@RequestBody FormsToUpdate formsToUpdate,HttpServletResponse response) throws IOException{
			
			byte[] decodedUserString = Base64.decode(formsToUpdate.getUserString());		
			String decodedUserStringString = new String (decodedUserString);		
			String splitArray[] = decodedUserStringString.split(":");
			
			if(splitArray.length == 2){
				String username = splitArray[0];
				String password = splitArray[1];
				try{
					UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
					//String encodedPassword = passwordEncoder.encodePassword(token.getName(),(String)token.getCredentials());
				    this.authenticate(token);
				    return userService.getModelToCollectApplication(formsToUpdate.getFormsToDownloadMediafiles(),username, password);
				   
				}catch(Exception e){
					logger.error("unauthorized server hit for login User String : " + "");
					response.sendError(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
				}			
				
			}else{
				logger.error("invalid string length : " + "");
				response.sendError(org.apache.http.HttpStatus.SC_UNAUTHORIZED);
			}		
			
			
			return null;
		}
}



