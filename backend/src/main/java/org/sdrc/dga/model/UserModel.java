
package org.sdrc.dga.model;

import org.sdrc.dga.domain.Area;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Azhar Created Date:21-07-2018 11:56:52 AM
 */
@Data
@NoArgsConstructor
public class UserModel {

	private Integer userId;
	private String username;
	private String emailId;
	private String name;
	private Integer[] roleIds;// value Object
	private Area[] areas;
}
