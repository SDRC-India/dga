package org.sdrc.dga.service;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.CollectUser;
import org.sdrc.dga.domain.FeaturePermissionMapping;
import org.sdrc.dga.domain.Program;
import org.sdrc.dga.domain.Program_XForm_Mapping;
import org.sdrc.dga.domain.Role;
import org.sdrc.dga.domain.RoleFeaturePermissionScheme;
import org.sdrc.dga.domain.UserRoleFeaturePermissionMapping;
import org.sdrc.dga.domain.User_Program_XForm_Mapping;
import org.sdrc.dga.domain.XForm;
import org.sdrc.dga.model.AreaModel;
import org.sdrc.dga.model.ChangePasswordModel;
import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.model.FeatureModel;
import org.sdrc.dga.model.FeaturePermissionMappingModel;
import org.sdrc.dga.model.FormsToDownloadMediafiles;
import org.sdrc.dga.model.MediaFilesToUpdate;
import org.sdrc.dga.model.ModelToCollectApplication;
import org.sdrc.dga.model.PermissionModel;
import org.sdrc.dga.model.ProgramModel;
import org.sdrc.dga.model.ProgramXFormsModel;
import org.sdrc.dga.model.RoleFeaturePermissionSchemeModel;
import org.sdrc.dga.model.RoleModel;
import org.sdrc.dga.model.UserRoleFeaturePermissionMappingModel;
import org.sdrc.dga.model.XFormModel;
import org.sdrc.dga.repository.AreaRepository;
import org.sdrc.dga.repository.CollectUserRepository;
import org.sdrc.dga.repository.FeaturePermissionMappingRepository;
import org.sdrc.dga.repository.ProgrammRepository;
import org.sdrc.dga.repository.RoleFeaturePermissionSchemeRepository;
import org.sdrc.dga.repository.RoleRepository;
import org.sdrc.dga.repository.UserRoleFeaturePermissionMappingRepository;
import org.sdrc.dga.repository.User_Program_XForm_MappingRepository;
import org.sdrc.dga.repository.XFormRepository;
import org.sdrc.dga.security.PasswordMismatchException;
import org.sdrc.dga.security.UserAlreadyExistException;
import org.sdrc.dga.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.itextpdf.text.pdf.codec.Base64;

/**
 * 
 * @author Harsh Pratyush (harsh@sdrc.co.in), Pratyush(pratyush@sdrc.co.in)
 *
 */
@Service
//@PropertySource("ugmtmessages.properties")
public class UserServiceImpl implements UserService {

	@Autowired
	CollectUserRepository collectUserRepository;

	@Autowired
	private User_Program_XForm_MappingRepository user_Program_XForm_MappingRepository;

	@Autowired
	private ProgrammRepository programmRepository;

	@Autowired
	private AreaRepository areaRepositroy;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private FeaturePermissionMappingRepository featurePermissionMappingRepository;

	@Autowired
	private UserRoleFeaturePermissionMappingRepository userRoleFeaturePermissionMappingRepository;

	@Autowired
	private RoleFeaturePermissionSchemeRepository roleFeaturePermissionSchemeRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	
	private XFormRepository xFormRepository;
	@Autowired
	private MessageSource messages;

	@Override
	public CollectUserModel findByUserName(String userName) {
		CollectUser collectUser = collectUserRepository.findByUsernameAndIsLiveTrue(userName);

		CollectUserModel collectUserModel = null;
		if (collectUser != null) {


			Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
			Set<String> roles = new HashSet<>();
			Set<Integer> roleIds = new HashSet<>();
			for (UserRoleFeaturePermissionMapping userRoleMapping : collectUser.getUserRoleFeaturePermissionMappings()) {
				
					RoleFeaturePermissionScheme roleFeaturePermissionScheme = userRoleMapping.getRoleFeaturePermissionScheme();
					
					
					roles.add(roleFeaturePermissionScheme.getRole().getRoleName().trim());
					roleIds.add(roleFeaturePermissionScheme.getRole().getRoleId());
					

					grantedAuthorities.add(new SimpleGrantedAuthority(
							roleFeaturePermissionScheme.getFeaturePermissionMapping().getFeature().getFeatureName()));
					//adding authorities using syntax : (Feature,Permission)
					grantedAuthorities.add(new SimpleGrantedAuthority(
							roleFeaturePermissionScheme.getFeaturePermissionMapping().getFeature().getFeatureName()
							.concat(",")
							.concat
							(roleFeaturePermissionScheme.getFeaturePermissionMapping().getPermission().getPermissionName())));
				
				
			
			}
			
			List<UserRoleFeaturePermissionMappingModel> userRoleFeaturePermissionMappingModels = new ArrayList<UserRoleFeaturePermissionMappingModel>();

			for (UserRoleFeaturePermissionMapping featurePermissionMapping : collectUser
					.getUserRoleFeaturePermissionMappings()) {
				UserRoleFeaturePermissionMappingModel userRoleFeaturePermissionMappingModel = new UserRoleFeaturePermissionMappingModel();

				userRoleFeaturePermissionMappingModel
						.setUserRoleFeaturePermissionId(featurePermissionMapping.getUserRoleFeaturePermissionId());

				RoleFeaturePermissionSchemeModel roleSchemeModel = new RoleFeaturePermissionSchemeModel();
				AreaModel areaModel = new AreaModel();
				RoleModel roleModel = new RoleModel();
				FeaturePermissionMappingModel featurePermissionMappingModel = new FeaturePermissionMappingModel();
				FeatureModel featureModel = new FeatureModel();
				PermissionModel permissionModel = new PermissionModel();

				roleSchemeModel.setAreaCode(featurePermissionMapping.getRoleFeaturePermissionScheme().getAreaCode());
				areaModel.setAreaId(featurePermissionMapping.getRoleFeaturePermissionScheme().getArea().getAreaId());
				areaModel.setAreaLevelId(featurePermissionMapping.getRoleFeaturePermissionScheme().getArea()
						.getAreaLevel().getAreaLevelId());
				areaModel
						.setAreaName(featurePermissionMapping.getRoleFeaturePermissionScheme().getArea().getAreaName());
				areaModel.setParentAreaId(
						featurePermissionMapping.getRoleFeaturePermissionScheme().getArea().getParentAreaId());
				areaModel
						.setAreaCode(featurePermissionMapping.getRoleFeaturePermissionScheme().getArea().getAreaCode());
				roleModel.setRoleId(featurePermissionMapping.getRoleFeaturePermissionScheme().getRole().getRoleId());
				roleModel
						.setRoleName(featurePermissionMapping.getRoleFeaturePermissionScheme().getRole().getRoleName());

				roleSchemeModel.setRole(roleModel);
				roleSchemeModel.setAreaModel(areaModel);
				roleSchemeModel
						.setSchemeName(featurePermissionMapping.getRoleFeaturePermissionScheme().getSchemeName());
				featureModel.setFeatureName(featurePermissionMapping.getRoleFeaturePermissionScheme()
						.getFeaturePermissionMapping().getFeature().getFeatureName());
				featureModel.setDescription(featurePermissionMapping.getRoleFeaturePermissionScheme()
						.getFeaturePermissionMapping().getFeature().getDescription());
				permissionModel.setPermissionName(featurePermissionMapping.getRoleFeaturePermissionScheme()
						.getFeaturePermissionMapping().getPermission().getPermissionName());
				featurePermissionMappingModel.setFeature(featureModel);
				featurePermissionMappingModel.setPermission(permissionModel);
				roleSchemeModel.setFeaturePermissionMapping(featurePermissionMappingModel);
				userRoleFeaturePermissionMappingModel.setRoleFeaturePermissionSchemeModel(roleSchemeModel);
				userRoleFeaturePermissionMappingModels.add(userRoleFeaturePermissionMappingModel);
			}
			
			collectUserModel = new CollectUserModel(collectUser.getUsername(),collectUser.getPassword(),true,true,
					true, true, grantedAuthorities,
			collectUser.getUserId(), roles, userRoleFeaturePermissionMappingModels.get(0).getRoleFeaturePermissionSchemeModel().getAreaModel());
			collectUserModel.setUserId(collectUser.getUserId());
			collectUserModel.setUsername(collectUser.getUsername());
			collectUserModel.setName(collectUser.getName());
			collectUserModel.setPassword(collectUser.getPassword());
			collectUserModel.setLive(collectUser.isLive());
			collectUserModel.setEmailId(collectUser.getEmailId());
			collectUserModel.setRole(roles);
			collectUserModel.setAreaModel(userRoleFeaturePermissionMappingModels.get(0).getRoleFeaturePermissionSchemeModel().getAreaModel());
			collectUserModel.setUserRoleFeaturePermissionMappings(userRoleFeaturePermissionMappingModels);
		}

		return collectUserModel;
	}

	@Override
	public List<ProgramXFormsModel> getProgramWithXFormsList(String username, String password) {

		// the user is present in the database or not. if present, live or not
		CollectUser user = collectUserRepository.findByUsernameAndPasswordAndIsLiveTrue(username, password);
		if (user != null) {

			// this variable will return
			List<ProgramXFormsModel> programWithXFormsList = new ArrayList<ProgramXFormsModel>();

			// fetching user-program-xForm mappings
			for (User_Program_XForm_Mapping user_Program_XForm_Mapping : user_Program_XForm_MappingRepository
					.findByUser(username)) {

				List<XFormModel> xFormModels = new ArrayList<XFormModel>();

				/*
				 * Checking whether the program and it's xForms are present or not in the
				 * programWithXFormsList. If present, get the XForm list, add one, remove the
				 * list and restore the latest list If not present add new program and xForm
				 * list
				 */
				boolean programPresent = false;
				for (ProgramXFormsModel programXFormsModel : programWithXFormsList) {
					if (programXFormsModel.getProgramModel().getProgramId() == user_Program_XForm_Mapping
							.getProgram_XForm_Mapping().getProgram().getProgramId()) {
						programPresent = true;
						// get the XForm list
						xFormModels = programXFormsModel.getxFormsModel();
					}
				}

				if (programPresent) {
					// program and it's xForms are present in the
					// programWithXFormsList
					XForm xForm = user_Program_XForm_Mapping.getProgram_XForm_Mapping().getxForm();

					XFormModel xFormModel = new XFormModel();

					xFormModel.setxFormId(xForm.getxFormId().trim());
					xFormModel.setOdkServerURL(xForm.getOdkServerURL().trim());
					xFormModel.setUsername(xForm.getUsername().trim());
					xFormModel.setPassword(Base64.encodeBytes(xForm.getPassword().trim().getBytes()));
					xFormModel.setTimeperiodId(xForm.getTimePeriod().getTimePeriodId());
					// add one
					xFormModels.add(xFormModel);

					// remove the list
					ProgramModel programModel = null;
					for (int i = 0; i < programWithXFormsList.size(); i++) {
						if (programWithXFormsList.get(i).getProgramModel().getProgramId() == user_Program_XForm_Mapping
								.getProgram_XForm_Mapping().getProgram().getProgramId()) {
							programModel = programWithXFormsList.get(i).getProgramModel();
							programWithXFormsList.remove(i);
						}
					}

					// restore the latest list
					ProgramXFormsModel programWithXFormsModelChild = new ProgramXFormsModel();

					programWithXFormsModelChild.setProgramModel(programModel);
					programWithXFormsModelChild.setxFormsModel(xFormModels);

					programWithXFormsList.add(programWithXFormsModelChild);

				} else {
					ProgramXFormsModel programWithXFormsModelChild = new ProgramXFormsModel();

					Program program = user_Program_XForm_Mapping.getProgram_XForm_Mapping().getProgram();

					ProgramModel programModel = new ProgramModel();
					programModel.setProgramId(program.getProgramId());
					programModel.setProgramName(program.getProgramName());
					programWithXFormsModelChild.setProgramModel(programModel);

					XForm xForm = user_Program_XForm_Mapping.getProgram_XForm_Mapping().getxForm();

					XFormModel xFormModel = new XFormModel();

					xFormModel.setxFormId(xForm.getxFormId().trim());
					xFormModel.setOdkServerURL(xForm.getOdkServerURL().trim());
					xFormModel.setUsername(xForm.getUsername().trim());
					xFormModel.setPassword(Base64.encodeBytes(xForm.getPassword().trim().getBytes()));
					xFormModel.setTimeperiodId(xForm.getTimePeriod().getTimePeriodId());
					xFormModels.add(xFormModel);

					programWithXFormsModelChild.setxFormsModel(xFormModels);

					programWithXFormsList.add(programWithXFormsModelChild);
				}
			}
			logger.info("Data sent for username : " + username);
			return programWithXFormsList;
		} else {
			logger.warn("Username : " + username + " authentication failed!");
			return null;
		}
	}

	@Override
	@Transactional
	public boolean insertUserTable() {

		Program program = programmRepository.findByProgramId(1);
		List<Program_XForm_Mapping> program_XForm_Mappings = program.getProgram_XForm_Mappings().stream().filter(d->d.getxForm().isLive()==true).collect(Collectors.toList());
		System.out.println("Xforms"+program_XForm_Mappings);
		List<CollectUser> collectUsers = collectUserRepository.findByIsLiveTrue();
		Map<String, CollectUser> collectUserMap = new HashMap<String, CollectUser>();
		for (CollectUser collectUser : collectUsers) {
			collectUserMap.put(collectUser.getUsername(), collectUser);
		}
		try {
			FileInputStream file = new FileInputStream("C:\\Workspaces\\DGA-2018\\Excel Sheet\\DGA V3\\Excel Template\\user-list.xlsx");
			XSSFWorkbook wb = new XSSFWorkbook(file);
			XSSFSheet sheet = wb.getSheetAt(0);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				if (i > 161) {
					wb.close();
					return true;
				}
				Row row = sheet.getRow(i);
				System.out.println(i);

				Cell nameCell = row.getCell(0);
				Cell emailCell = row.getCell(1);
//			Cell passWord=row.getCell(3);
				System.out.println(nameCell.getStringCellValue());
				String username = nameCell.getStringCellValue().replaceAll(" ", "").replace(".", "").toLowerCase()
						.trim().substring(0, 5);

				CollectUser collectUser = new CollectUser();
				collectUser.setCreatedDate(new Timestamp(new java.util.Date().getTime()));
				collectUser.setCreatedBy("Harsh");
				collectUser.setUsername(username + RandomStringUtils.randomNumeric(3));
				collectUser.setName(nameCell.getStringCellValue());
				collectUser.setEmailId(emailCell.getStringCellValue());
				collectUser.setPassword(collectUser.getUsername() + "#!");
				collectUser.setLive(true);
				if (collectUserMap.containsKey(collectUser.getUsername())) {
					CollectUser collectUserOld = collectUserMap.get(collectUser.getUsername());
					collectUserOld.setUpdatedDate(new Timestamp(new java.util.Date().getTime()));
					collectUserOld.setUpdatedBy("Harsh");
					collectUserOld.setLive(false);
					collectUserRepository.save(collectUserOld);
				}
				CollectUser collectUsernew = null;
				try {
					collectUsernew = collectUserRepository.save(collectUser);
				} catch (DataIntegrityViolationException data) {
					try {
						collectUser.setUsername(nameCell.getStringCellValue().split(" ")[0].toLowerCase().trim()
								+ nameCell.getStringCellValue().split(" ")[1].toLowerCase().trim());
						collectUser.setPassword(
								collectUser.getUsername() + "@" + RandomStringUtils.randomNumeric(3) + "#!");
						collectUsernew = collectUserRepository.save(collectUser);
					} catch (DataIntegrityViolationException error) {
						System.out.println(nameCell.getStringCellValue());
						error.printStackTrace();
						wb.close();
						throw new RuntimeException(error);
					}
				}
				for (Program_XForm_Mapping program_XForm_Mapping : program_XForm_Mappings) {
					User_Program_XForm_Mapping user_Program_XForm_Mapping = new User_Program_XForm_Mapping();

					user_Program_XForm_Mapping.setCollectUser(collectUsernew);
					user_Program_XForm_Mapping.setIsLive(true);
					user_Program_XForm_Mapping.setProgram_XForm_Mapping(program_XForm_Mapping);
					user_Program_XForm_Mapping.setCreatedDate(new Timestamp(new java.util.Date().getTime()));
					user_Program_XForm_Mapping.setCreatedBy("Harsh");

					user_Program_XForm_MappingRepository.save(user_Program_XForm_Mapping);
				}
				System.out.println("Done");
			}
			wb.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public ModelToCollectApplication getModelToCollectApplication(List<FormsToDownloadMediafiles> list, String username,
			String password) {

		ModelToCollectApplication modelToCollectApplication = new ModelToCollectApplication();
		List<ProgramXFormsModel> programWithXFormsList = getProgramWithXFormsList(username, password);
		if (programWithXFormsList != null) {
			modelToCollectApplication.setProgramXFormModelList(programWithXFormsList);
		} else {
			return null;
		}
		// modelToCollectApplication.setListOfMediaFilesToUpdate(null);
		if (getMediaFilesToUpdate(list) != null) {
			modelToCollectApplication.setListOfMediaFilesToUpdate(getMediaFilesToUpdate(list));
		} else {
			modelToCollectApplication.setListOfMediaFilesToUpdate(null);
		}

		return modelToCollectApplication;
	}

	@Override
	public List<MediaFilesToUpdate> getMediaFilesToUpdate(List<FormsToDownloadMediafiles> formToDownloadMediaList) {
		List<MediaFilesToUpdate> mediaFilesToUpdatesList = new ArrayList<MediaFilesToUpdate>();
		try {
			List<XForm> xForms = xFormRepository.findAllByIsLiveTrue();
			formToDownloadMediaList.forEach(formFromMobile -> {
				MediaFilesToUpdate mediaFilesToUpdate = new MediaFilesToUpdate();
				XForm xForm = getValidatedXForm(xForms, formFromMobile);
				if (xForm != null) {
					mediaFilesToUpdate.setxFormId(xForm.getxFormId());
					Path path = Paths.get(xForm.getMediaPath());
					byte[] data;
					try {
						data = Files.readAllBytes(path);
						String encodedString = org.apache.commons.codec.binary.Base64.encodeBase64String(data);
						mediaFilesToUpdate.setMediaFile(encodedString);
					} catch (Exception e) {
						logger.error("Media file not present in the specified path");
					}

				} else {
					mediaFilesToUpdate.setxFormId(formFromMobile.getFormId());
					mediaFilesToUpdate.setMediaFile(null);
				}
				mediaFilesToUpdatesList.add(mediaFilesToUpdate);
			});
		} catch (Exception e) {
			logger.error("" + e);
		}

		return mediaFilesToUpdatesList;
	}

	private XForm getValidatedXForm(List<XForm> xForms, FormsToDownloadMediafiles formFromMobile) {

		SimpleDateFormat sdf = new SimpleDateFormat(
				messages.getMessage(Constants.Odk.MEDIA_FILE_UPDATED_DATE, null, null));

		String xFormUpdatedDate = null;

		for (XForm form : xForms) {
			Date date = new Date();
			if (form.getUpdatedDate() != null) {
				date.setTime(form.getUpdatedDate().getTime());
				xFormUpdatedDate = new SimpleDateFormat(
						messages.getMessage(Constants.Odk.MEDIA_FILE_UPDATED_DATE, null, null)).format(date);
				try {
					if (formFromMobile.getFormId().equals(form.getxFormId())
							&& sdf.parse(formFromMobile.getDownloadOrUpdateDate()).before(sdf.parse(xFormUpdatedDate))
							&& form.getMediaPath() != null) {
						return form;

					}
				} catch (ParseException e) {
					return null;
				}
			}

		}

		return null;
	}

	@Override
	@Transactional
	public boolean updatePassword(CollectUserModel collectUserModel) {

		CollectUser collectUser = collectUserRepository.findByUsernameAndIsLiveTrue(collectUserModel.getUsername());
		if (collectUser == null)
			return false;
		collectUser.setPassword(passwordEncoder.encode(collectUserModel.getPassword()));
		return true;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean configureUserDatabase() {

		try {
			List<Integer> areaLevelIds = new ArrayList<>();
			areaLevelIds.add(1);
			areaLevelIds.add(2);
			areaLevelIds.add(4);
			
			Map<Integer,Area> areaMap = new HashMap<>();

			List<Area> areas = areaRepositroy.findByAreaLevelAreaLevelIdIn(areaLevelIds);
			areas.forEach(d->{
				areaMap.put(d.getAreaId(),d);
			});

			List<Role> roles = roleRepository.findAll();
			roles.get(0).getRoleCode();

			List<FeaturePermissionMapping> featurePermissionMappings = featurePermissionMappingRepository.findAll();

			List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemes = new ArrayList<>();

			for (Role role : roles) {
				List<Area> areaForRole = new ArrayList<>();
				List<FeaturePermissionMapping> featurePermissionForRole = new ArrayList<>();

				switch (role.getRoleId()) {
				// state level
				case 1:
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() == 2)
							.collect(Collectors.toList());
					featurePermissionForRole = featurePermissionMappings.stream()
							.filter(fpm -> (fpm.getFeature().getFeatureId() != 9 || fpm.getPermission().getPermissionId() != 2)
									&& (fpm.getFeature().getFeatureId() != 12 || fpm.getPermission().getPermissionId() != 2))
							.collect(Collectors.toList());
					
					break;
				
				case 2:// national level
				case 4:// admin level
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() == 1)
							.collect(Collectors.toList());
					featurePermissionForRole = featurePermissionMappings.stream()
							.filter(fpm -> (fpm.getFeature().getFeatureId() != 9 || fpm.getPermission().getPermissionId() != 2)
									&& (fpm.getFeature().getFeatureId() != 12 || fpm.getPermission().getPermissionId() != 2))
							.collect(Collectors.toList());
					break;

				// district level
				case 3:
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() == 4)
							.collect(Collectors.toList());
					featurePermissionForRole = featurePermissionMappings.stream()
							.filter(fpm -> (fpm.getFeature().getFeatureId() != 9 || fpm.getPermission().getPermissionId() != 2)
									&& (fpm.getFeature().getFeatureId() != 12 || fpm.getPermission().getPermissionId() != 2))
							.collect(Collectors.toList());
					break;

				// guest level
				case 5:
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() <= 4)
							.collect(Collectors.toList());
					featurePermissionForRole = featurePermissionMappings.stream()
							.filter(fpm -> (fpm.getFeature().getFeatureId() != 9 || fpm.getPermission().getPermissionId() != 2)
									&& (fpm.getFeature().getFeatureId() != 12 || fpm.getPermission().getPermissionId() != 2))
							.collect(Collectors.toList());
					break;
				//DDM
				case 6:
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() == 4)
							.collect(Collectors.toList());
					featurePermissionForRole = featurePermissionMappings;
					break;
				default:
					break;
				}

				for (Area area : areaForRole) {

					for (FeaturePermissionMapping featurePermissionMapping : featurePermissionForRole) {
						RoleFeaturePermissionScheme roleFeaturePermissionScheme = new RoleFeaturePermissionScheme();

						roleFeaturePermissionScheme.setArea(area);
						roleFeaturePermissionScheme.setAreaCode(area.getAreaCode());
						roleFeaturePermissionScheme.setFeaturePermissionMapping(featurePermissionMapping);
						roleFeaturePermissionScheme.setRole(role);
						String areaNameForRole = area.getAreaName().replaceAll(" ", "_");
						roleFeaturePermissionScheme
								.setSchemeName(area.getAreaLevel().getAreaLevelId() == 4 || role.getRoleId() == 5
										? role.getRoleName() + "_" + areaNameForRole
										: role.getRoleName());
						roleFeaturePermissionScheme.setUpdatedBy("System");
						roleFeaturePermissionSchemes.add(roleFeaturePermissionScheme);
					}

				}

			}

			roleFeaturePermissionSchemeRepository.save(roleFeaturePermissionSchemes);

			List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemesSaved = roleFeaturePermissionSchemeRepository
					.findAll();

			for (Role role : roles) {
				List<Area> areaForRole = new ArrayList<>();

				switch (role.getRoleId()) {
				// state level
				case 1:
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() == 2).collect(Collectors.toList());
					break;

				// national level
				case 2:
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() == 1).collect(Collectors.toList());
					break;

				// district level
//				case 6:
				case 3:
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() == 4).collect(Collectors.toList());
					break;

				// admin level
				case 4:
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() == 1).collect(Collectors.toList());
					break;

				// guest level
				case 5:
					areaForRole = areas.stream().filter(d -> d.getAreaLevel().getAreaLevelId() <= 4).collect(Collectors.toList());
					break;
					
				default:
					break;
				}

				for (Area area : areaForRole) {

					List<RoleFeaturePermissionScheme> roleFeaturePermissionSchemesForArea = roleFeaturePermissionSchemesSaved
							.stream().filter(d -> d.getAreaCode() == area.getAreaCode() && d.getRole().getRoleId()==role.getRoleId()).collect(Collectors.toList());

				
					String userName = role.getRoleId() == 4
							|| role.getRoleId() == 5
									? role.getRoleId() == 4 ? "admin"
											: area.getAreaLevel()
													.getAreaLevelId() > 1
															? "guest_"
																	+ areaMap.get(area.getParentAreaId())
																			.getAreaName().trim().replaceAll(" ", "")
																			.toLowerCase().substring(0, 2)
																	+ "_"
																	+ area.getAreaName().trim()
																			.replaceAll(" ", "").toLowerCase()
															: "guest"
									: area.getAreaLevel().getAreaLevelId() == 4
											? areaMap.get(area.getParentAreaId()).getAreaName().trim()
													.replaceAll(" ", "").toLowerCase().substring(0, 2)
													+ "_"
													+ area.getAreaName().trim().trim().replaceAll(" ", "").toLowerCase()
															.replaceAll(" ", "").toLowerCase()
											: area.getAreaName().trim().replaceAll(" ", "").toLowerCase();
															
					System.out.println(userName);										

					CollectUser collectUser = new CollectUser();
					collectUser.setCreatedBy("Harsh");
					collectUser.setName(userName);
					collectUser.setUsername(collectUser.getName());
					collectUser.setPassword(passwordEncoder.encode(collectUser.getUsername() + "001"));
					collectUser.setLive(true);

					CollectUser collectUserSaved = collectUserRepository.findByUsernameAndIsLiveTrue(userName)==null?
							collectUserRepository.save(collectUser):collectUserRepository.findByUsernameAndIsLiveTrue(userName);

					List<UserRoleFeaturePermissionMapping> userRoleFeaturePermissionMappings = new ArrayList<UserRoleFeaturePermissionMapping>();

					for (RoleFeaturePermissionScheme roleFeaturePermissionScheme : roleFeaturePermissionSchemesForArea) {

						UserRoleFeaturePermissionMapping userRoleFeaturePermissionMapping = new UserRoleFeaturePermissionMapping();
						userRoleFeaturePermissionMapping.setRoleFeaturePermissionScheme(roleFeaturePermissionScheme);
						userRoleFeaturePermissionMapping.setUserDetail(collectUserSaved);
						userRoleFeaturePermissionMappings.add(userRoleFeaturePermissionMapping);
					}

					userRoleFeaturePermissionMappingRepository.save(userRoleFeaturePermissionMappings);

				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return true;
	}

	@Override
	public ResponseEntity<String> changePassoword(ChangePasswordModel changePasswordModel, Principal p) {
		/**
		 * using it to parse string as json
		 */
		Gson gson = new Gson();
		CollectUser user = collectUserRepository.findByUsername(changePasswordModel.getUserName());
		try {
			/**
			 * check newpassword and confirm password is same or nots
			 */
			checkIfNewPasswordAndDbPasswordAreSame(changePasswordModel.getNewPassword(),
					changePasswordModel.getConfirmPassword(), user);

			/**
			 * check user has entered correct old password or not
			 */
			checkCorrectOldPassword(changePasswordModel.getOldPassword(), user.getPassword(), user);
			/**
			 * check new password is same as db password or not if same
			 */
			checkIfNewPasswordAndDbPasswordAreSame(changePasswordModel.getNewPassword(), user.getPassword(), user);
			/**
			 * encoding password
			 */
			user.setPassword(passwordEncoder.encode(changePasswordModel.getNewPassword()));
			/**
			 * updating db
			 */
			collectUserRepository.save(user);

			logger.info("Password has been updated Successfully. Please login again." + " for user : "
					+ user.getUsername());
			return new ResponseEntity<>(gson.toJson("Password has been updated Successfully. Please login again."),
					HttpStatus.OK);

		}  catch (PasswordMismatchException e) {
			logger.error("Action : change-password By {}: error while updating password! ", user.getUsername(), e);
			throw new PasswordMismatchException(e.getMessage());
		} catch (Exception e) {
			logger.error("Action : change-password By {}: error while updating password! ", user.getUsername(), e);
			return new ResponseEntity<>(gson.toJson("Password has not been updated."),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	private void checkIfNewPasswordAndDbPasswordAreSame(String newPassword, String password, CollectUser user) {
		if (passwordEncoder.matches(newPassword, password)) {
			logger.error(
					"Action : change-password By {}: error while updating password! : message : "
							+ "New password is same as Current password",
					user.getUsername());
			throw new PasswordMismatchException("New password is same as Current password");

		}
	}
	
	private void checkCorrectOldPassword(String oldPassword, String password, CollectUser user) {
		if (!passwordEncoder.matches(oldPassword, password)) {
			logger.error("Action : change-password By {} : error while updating password! : message : ",
					user.getUsername(), "Please provide the correct current password.");
			throw new PasswordMismatchException("Please provide the correct current password.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> getAllUserRoles() {
		List<Integer> adminrole = new ArrayList<>();
		adminrole.add(4);
		return roleRepository.findAllByRoleIdNotIn(adminrole).stream().map(ud->{
			JSONObject obj = new JSONObject();
			obj.put("id", ud.getRoleId());
			obj.put("roleName", ud.getDescription());
			return obj;
		}).collect(Collectors.toList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JSONObject> getUsersByRoleId(int roleId) {

		List<CollectUser> users = collectUserRepository.findByRoleId(roleId);
		return users.stream().map(ud->{
			JSONObject obj = new JSONObject();
			obj.put("userId", ud.getUserId());
			obj.put("name", ud.getName());
			obj.put("userName", ud.getUsername());
			obj.put("live", ud.isLive());
			return obj;
		}).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public ResponseEntity<Boolean> resetPassword(Map<String, Object> resetPasswordMap, Principal p) {

		if (resetPasswordMap.get("userId") == null || resetPasswordMap.get("userId").toString().isEmpty())
			throw new RuntimeException("key : userId not found in map");

		if (resetPasswordMap.get("newPassword") == null || resetPasswordMap.get("newPassword").toString().isEmpty())
			throw new RuntimeException("key : newPassword not found in map");

		try {
			CollectUser user = collectUserRepository.findByUserId((int) resetPasswordMap.get("userId"));

			user.setPassword(passwordEncoder.encode(resetPasswordMap.get("newPassword").toString()));

			collectUserRepository.save(user);

			return new ResponseEntity<Boolean>(true, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Action : while resetting password with payload {}", resetPasswordMap, e);
			throw new RuntimeException();
		}

	}

	@Override
	public ResponseEntity<String> enableUserName(String userId, Principal p) {
		Gson gson = new Gson();
		CollectUser user = collectUserRepository.findByUserId(Integer.valueOf(userId));
		try {
			user.setLive(true);
			collectUserRepository.save(user);
			logger.info("Action : enable-user ::{} for user name : {}",
					"User enabled successfully", user.getUsername());
			return new ResponseEntity<String>(gson.toJson("User enabled successfully"),
					HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Action : enable-user By{}: error while creating new user with userId {}", user.getUsername(),
					user.getUserId(), e);
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseEntity<String> disableUserName(String userId, Principal p) {
		Gson gson = new Gson();
		CollectUser user = collectUserRepository.findByUserId(Integer.valueOf(userId));
		try {
			user.setLive(false);
			collectUserRepository.save(user);
			return new ResponseEntity<String>(gson.toJson("User disabled successfully"),
					HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Action : enable-user By{}: error while creating new user with userId {}", user.getUsername(),
					user.getUserId(), e);
			throw new RuntimeException();
		}
	}

	@Override
	public Map<String, List<AreaModel>> getAllAreaList() {
		List<Area> areas = areaRepositroy.findAllByIsLiveTrue();

		List<AreaModel> areaModelList = new ArrayList<>();
		List<AreaModel> areaModelListForGuestRole = new ArrayList<>();
		Map<String, List<AreaModel>> areaMap = new LinkedHashMap<>();

		// setting areas is area-model list
		for (Area area : areas) {

			AreaModel areaModel = new AreaModel();

			areaModel.setAreaCode(area.getAreaCode());
			areaModel.setAreaId(area.getAreaId());
			areaModel.setAreaLevel(area.getAreaLevel().getAreaLevelName());
			areaModel.setAreaLevelId(area.getAreaLevel().getAreaLevelId());;
			areaModel.setAreaName(area.getAreaName());
			areaModel.setParentAreaId(area.getParentAreaId());
			areaModelList.add(areaModel);
			
			if(areaModel.getAreaLevelId()==1 || areaModel.getAreaLevelId()==2 || areaModel.getAreaLevelId()==4)
				areaModelListForGuestRole.add(areaModel);

		}
		areaMap.put("allArea", areaModelListForGuestRole);
		// making levelName as a key
		for (AreaModel areaModel : areaModelList) {

			if (areaMap.containsKey(areaModel.getAreaLevel())) {
				areaMap.get(areaModel.getAreaLevel()).add(areaModel);
			} else {
				areaModelList = new ArrayList<>();
				areaModelList.add(areaModel);
				areaMap.put(areaModel.getAreaLevel(), areaModelList);
			}
		}
		
		return areaMap;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseEntity<String> createUser(Map<String, Object> map, Principal p) {
		Gson gson = new Gson();
		if (map.get("userName") == null || map.get("userName").toString().isEmpty())
			throw new RuntimeException("key : userName not found in map");

		if (map.get("designationIds") == null || map.get("designationIds").toString().isEmpty())
			throw new RuntimeException("key : designationIds not found in map");

		if (map.get("password") == null || map.get("password").toString().isEmpty())
			throw new RuntimeException("key : password not found in map");
		
		try{
			CollectUser user = collectUserRepository.findByUsername(map.get("userName").toString());
			if (user != null) {
	
				throw new UserAlreadyExistException("Username already exist. Try another.");
	
			}
			List<Integer> degs = (List<Integer>) map.get("designationIds");
			List<RoleFeaturePermissionScheme> rfps = new ArrayList<>();
			switch (degs.get(0)) {
			case 2://national level
				rfps = roleFeaturePermissionSchemeRepository.findByAreaAreaIdAndRoleRoleId(1,degs.get(0));
				break;
			case 1://state level(only state)
			case 3://district(only districts)
			case 5://guest(national, state, district)
			case 6://district data manager(only district)
				rfps = roleFeaturePermissionSchemeRepository.findByAreaAreaIdAndRoleRoleId(Integer.parseInt(map.get("areaId").toString()),degs.get(0));
				break;
			default:
				break;
			}
			
			
			CollectUser newUser = new CollectUser();
			
			newUser.setCreatedBy(p.getName());
			newUser.setCreatedDate(new Timestamp(new java.util.Date().getTime()));
			newUser.setEmailId(null);
			newUser.setLive(true);
			newUser.setName(map.get("name").toString());
			newUser.setPassword(passwordEncoder.encode(map.get("password").toString()));
			newUser.setUpdatedBy(null);
			newUser.setUsername(map.get("userName").toString());
			
			newUser = collectUserRepository.save(newUser);
			
			List<UserRoleFeaturePermissionMapping> urfpms = new ArrayList<>();
			for(RoleFeaturePermissionScheme rfp : rfps){
				UserRoleFeaturePermissionMapping urfpm = new UserRoleFeaturePermissionMapping();
				urfpm.setUserDetail(newUser);
				urfpm.setRoleFeaturePermissionScheme(rfp);
				urfpm.setUpdatedDate(new Timestamp(new java.util.Date().getTime()));
				
				urfpms.add(urfpm);
			}
			urfpms = userRoleFeaturePermissionMappingRepository.save(urfpms);
			
			newUser.setUserRoleFeaturePermissionMappings(urfpms);
			collectUserRepository.save(newUser);
			return new ResponseEntity<String>(gson.toJson("User created successfully"), HttpStatus.OK);
		}catch(Exception e){
			logger.error(e.getLocalizedMessage());
			return new ResponseEntity<String>(gson.toJson(e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
		}
	}

}
