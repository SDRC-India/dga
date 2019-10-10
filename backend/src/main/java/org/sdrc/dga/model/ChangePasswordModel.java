package org.sdrc.dga.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Pratyush
 *
 */
@Getter
@Setter
public class ChangePasswordModel {
	
	private String userName;

	private String oldPassword;

	private String newPassword;

	private String confirmPassword;
}
