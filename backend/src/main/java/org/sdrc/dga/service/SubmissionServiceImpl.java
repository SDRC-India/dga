/**
 * 
 */
package org.sdrc.dga.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bushe.swing.event.EventBus;
import org.opendatakit.briefcase.model.BriefcaseFormDefinition;
import org.opendatakit.briefcase.model.DocumentDescription;
import org.opendatakit.briefcase.model.FormStatus;
import org.opendatakit.briefcase.model.FormStatusEvent;
import org.opendatakit.briefcase.model.ServerConnectionInfo;
import org.opendatakit.briefcase.model.TerminationFuture;
import org.opendatakit.briefcase.util.AggregateUtils;
import org.opendatakit.briefcase.util.ServerUploader.SubmissionResponseAction;
import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.AreaLevel;
import org.sdrc.dga.domain.ChoicesDetails;
import org.sdrc.dga.domain.CollectUser;
import org.sdrc.dga.domain.LastVisitData;
import org.sdrc.dga.domain.Program_XForm_Mapping;
import org.sdrc.dga.domain.TimePeriod;
import org.sdrc.dga.domain.User_Program_XForm_Mapping;
import org.sdrc.dga.domain.XForm;
import org.sdrc.dga.model.CollectUserModel;
import org.sdrc.dga.model.Mail;
import org.sdrc.dga.model.PostSubmissionModel;
import org.sdrc.dga.model.XFormModel;
import org.sdrc.dga.repository.AreaRepository;
import org.sdrc.dga.repository.ChoiceDetailsRepository;
import org.sdrc.dga.repository.CollectUserRepository;
import org.sdrc.dga.repository.LastVisitDataRepository;
import org.sdrc.dga.repository.User_Program_XForm_MappingRepository;
import org.sdrc.dga.repository.XFormRepository;
import org.sdrc.dga.thread.PostSubmissionThread;
import org.sdrc.dga.util.Constants;
import org.sdrc.dga.util.DomainToModelConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Harsh Pratyush (harsh@sdrc.co.in)
 *
 */
@Service
public class SubmissionServiceImpl implements SubmissionService {

	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	DomainToModelConverter  domainToModelConverter;
	
	@Autowired
	private XFormRepository xFormRepository;

	@Autowired
	private CollectUserRepository collectUserRepository;

	@Autowired
	private User_Program_XForm_MappingRepository user_Program_XForm_MappingRepository;
	
	@Autowired
	private AreaRepository areaRepository;
	
	@Autowired
	private LastVisitDataRepository lastVisitDataRepository;
	
	@Autowired
	private MailService mailService;
	
	private static final Logger logger = LoggerFactory.getLogger(SubmissionServiceImpl.class);
	
	@Autowired
	private ApplicationContext appContext;
	
	
	@Autowired
	private ChoiceDetailsRepository choiceDetailsRepository;
	
	@Autowired
	private MasterRawDataService masterRawDataService;
	
	/* (non-Javadoc)
	 * @see org.sdrc.dga.service.SubmissionService#uploadForm(javax.servlet.http.HttpServletRequest, java.lang.String, org.sdrc.dga.model.CollectUserModel)
	 */
	@Override
	public int uploadForm(HttpServletRequest request, String deviceID,
			CollectUserModel collectUserModel) {
		try {

			SimpleDateFormat sdf = new SimpleDateFormat(
					messages.getMessage(Constants.Odk.SUBMISSION_CREATE_FOLDER, null, null));
			String folderName = collectUserModel.getUserId().toString() + "_" + sdf.format(new Date());
			
			if(!new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null)).exists())
			{
				new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null)).mkdir();
			}
			
			if(!new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "/"
					+ messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null)).exists())
			{
				new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "/"
						+ messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null)).mkdir();
				
			}

			if ((new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "/"
					+ messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "/"
					+ folderName)).mkdir()) {
				logger.info("Folder created");
				// Keeping all files from mobile device
				// cast request
			    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			    // You can get your file from request
			    MultipartFile multipartFile =  null; // multipart file class depends on which class you use assuming you are using org.springframework.web.multipart.commons.CommonsMultipartFile

			    Iterator<String> iterator = multipartRequest.getFileNames();

			    while (iterator.hasNext()) {
			        String key = (String) iterator.next();
			        // create multipartFile array if you upload multiple files
			        multipartFile =  multipartRequest.getFile(key);			        
			        String path = (messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "/"
							+ messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "/"
							+ folderName).toString();			       
			        File convFile = new File(path, multipartFile.getOriginalFilename());
			        multipartFile.transferTo(convFile);
			        
			    }
			    
			    
				// Set respective files in respective places
				List<String> xmlFileNames = new ArrayList<>();
				List<String> mediaFileNames = new ArrayList<>();

				for (String fileName : allFilesFromMobileApp(folderName)) {
					if (fileName.substring(fileName.length() - 4, fileName.length()).equals(".xml"))
						xmlFileNames.add(fileName);
					else
						mediaFileNames.add(fileName);
				}

				// Do the upload work
				for (String fileName : xmlFileNames) {
					Map<String, String> map = getFormIdFromXML(folderName, fileName);

					if (map != null) {

						String formId = null;
						String xFormIdFromODK = null;

						for (Map.Entry<String, String> entry : map.entrySet()) {
							formId = entry.getKey();
							xFormIdFromODK = entry.getValue();
							break;
						}

						if (formId != null || xFormIdFromODK != null) {

							XForm xForm = xFormRepository.findByXFormIdAndIsLiveTrue(formId);
							XFormModel xFormModel = domainToModelConverter.toXFormModel(xForm);
							if (xForm != null) {
								
								String decodedPasswordString = xForm.getPassword();//xForm.getPassword() can not be null
								//because the password field in xForm table will not allow null value entry
								TerminationFuture terminationFuture = new TerminationFuture();
								DocumentDescription description = new DocumentDescription(
										"Submission upload failed.  Detailed error: ", "Submission upload failed.",
										"submission (" + 0 + " of " + 1 + ")", terminationFuture);
								ServerConnectionInfo serverConnectionInfo = new ServerConnectionInfo(
										xForm.getOdkServerURL() + "submission?deviceID=" + deviceID,
										xForm.getUsername(), decodedPasswordString.toCharArray());

								Map<String, List<File>> formFilesMap = extractMediaFiles(folderName, fileName);
								Integer upxm_id = validateUserFormMapping(xForm, collectUserModel);
								if (upxm_id != null) {
									if (upload(folderName, fileName, formId, formFilesMap, serverConnectionInfo,
											description, terminationFuture, xFormModel, collectUserModel, upxm_id,
											xFormIdFromODK)) {
										logger.info(
												"Submission success for filename : " + folderName + "\\" + fileName);
										return 0;
									} else {
										logger.error(
												"Form upload failure for filename : " + folderName + "\\" + fileName);
										return 500;
									}
								} else {
									logger.warn(
											"Submission failure, unassigned form : " + folderName + "\\" + fileName);
									return 406;
								}
							} else {
								logger.warn("No xForm found in the database in this file name : " + folderName + "\\"
										+ fileName);
								return 500;
							}
						} else {
							logger.warn("Could not extract form id from this file : " + folderName + "\\" + fileName);
							return 500;
						}

					} else {
						logger.warn("Could not extract form id from this file : " + folderName + "\\" + fileName);
						return 500;
					}
				}

				return 0;

			} else {
				
				logger.error("Could not create folder name " + folderName);
				return 500;
			}

		} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
			e.printStackTrace();
			logger.error("Exception while creating folder : ",e);
			return 500;
		}

	}
	
	private List<String> allFilesFromMobileApp(String folderName) {

		List<String> files = new ArrayList<String>();
		File folder = new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
				+ messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\" + folderName);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				files.add(listOfFiles[i].getName());
			}
		}
		return files;
	}
	
	private Integer validateUserFormMapping(XForm xForm, CollectUserModel collectUserModel) {
		// see whether the user has this particular form assigned or not
		List<User_Program_XForm_Mapping> User_Program_XForm_Mappings = user_Program_XForm_MappingRepository
				.findByUser(collectUserModel.getUsername());

		for (User_Program_XForm_Mapping user_Program_XForm_Mapping : User_Program_XForm_Mappings) {
			// get program with xForm mapping
			Program_XForm_Mapping program_XForm_Mapping = user_Program_XForm_Mapping.getProgram_XForm_Mapping();
			if (program_XForm_Mapping != null) {
				if (program_XForm_Mapping.getxForm().getFormId() == xForm.getFormId()) {

					return user_Program_XForm_Mapping.getUserProgramXFomrMappingId();
				}
			}
		}
		return null;

	}

	/**
	 * This method is responsible for extracting form id from the xml file.
	 * 
	 * @return formId
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @since version 1.0.0.0
	 */
	private Map<String, String> getFormIdFromXML(String folderName, String fileName) {
		Map<String, String> map = new HashMap<>(1);
		try {
			String formId = null;
			File inputFile = new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\" + folderName
					+ "\\" + fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			NodeList nList2 = doc.getChildNodes();
			String xFormId = nList2.item(0).getNodeName();
			formId = nList2.item(0).getAttributes().item(0).getTextContent();
			map.put(formId, xFormId);
			return map;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			logger.error("Exception in getting form id from XML, Exception message : " , e );
			return null;
		}
	}
	
	private Map<String, List<File>> extractMediaFiles(String folderName, String xmlFileName) {
		List<File> files = new ArrayList<File>();
		Map<String, List<File>> formFilesMap = new HashMap<String, List<File>>();

		try {

			File inputFile = new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\" + folderName
					+ "\\" + xmlFileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			String xmlString = "";

			FileInputStream fis = null;
			try {
				fis = new FileInputStream(inputFile);
				int content;
				while ((content = fis.read()) != -1) {
					// convert to char and display it
					xmlString += (char) content;
				}

			} catch (IOException e) {
//				e.printStackTrace();
				logger.error("Exception while creating file : ",e);
			} finally {
				try {
					if (fis != null)
						fis.close();
				} catch (IOException ex) {
//					ex.printStackTrace();
					logger.error("Exception while creating file : ",ex);
				}
			}

			List<String> imgList = getImageList(xmlString);

			for (String imageFileName : imgList) {
				try {
					File imageFile = new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
							+ messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\"
							+ folderName + "\\" + imageFileName);
					if (imageFile.exists())
						files.add(imageFile);
				} catch (NullPointerException e) {
//					e.printStackTrace();
					logger.error("Exception while creating imageFile file : ",e);
				}
			}
			formFilesMap.put(xmlFileName, files);
			return formFilesMap;
		} catch (ParserConfigurationException | SAXException | IOException e) {
//			e.printStackTrace();
			logger.error("Exception while creating imageFile file : ",e);
			return formFilesMap;
		}
	}
	
	private List<String> getImageList(String xmlString) {
		List<String> imageFileNames = new ArrayList<String>();

		String array[] = xmlString.split(".jpg<");

		for (int i = 0; i < array.length - 1; i++) {
			String imgFile = null;
			for (int j = array[i].length() - 1; j >= 0; j--) {
				if (array[i].charAt(j) == '>') {
					imgFile = array[i].substring(j + 1) + ".jpg";
					break;
				}
			}
			imageFileNames.add(imgFile);
		}
		return imageFileNames;
	}

	
	/**
	 * This method is responsible for extracting form id from the xml file.
	 * 
	 * @return formId
	 * @author Harsh Pratyush(harsh@sdrc.co.in)
	 * @since version 1.0.0.0
	 */

	private boolean upload(String folderName, String fileName, String formId, Map<String, List<File>> formFilesMap,
			ServerConnectionInfo serverConnectionInfo, DocumentDescription documentDescription,
			TerminationFuture terminationFuture, XFormModel xFormModel, CollectUserModel collectUserModel,
			int userProgramXFormMappingId, String xFormIdFromOdk) {
		try {

			URI uri = null;

			// validate the URI from ui otherwise exception will occur
			uri = AggregateUtils.testServerConnectionWithHeadRequest(serverConnectionInfo, "formUpload");

			File file = new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null) + "\\" + folderName
					+ "\\" + fileName);
			File dFile = new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messages.getMessage(Constants.Odk.ODK_DEFAULT_FORMS_FOLDER_NAME, null, null) + "\\" + formId
					+ "\\" + messages.getMessage(Constants.Odk.ODK_DEFAULT_DESTINATIONFILE_FOLDER_NAME, null, null)
					+ "\\" + formId + ".xml");

			BriefcaseFormDefinition lfd = null;
			File f = new File(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
					+ messages.getMessage(Constants.Odk.ODK_DEFAULT_FORMS_FOLDER_NAME, null, null) + "\\" + formId
					+ "\\" + messages.getMessage(Constants.Odk.ODK_DEFAULT_DESTINATIONFILE_FOLDER_NAME, null, null));
			lfd = new BriefcaseFormDefinition(f, dFile);

			FormStatus formToTransfer = new FormStatus(FormStatus.TransferType.UPLOAD, lfd);
			SubmissionResponseAction action = null;

			if (!file.exists()) {
				String msg = "Submission file not found: " + file.getAbsolutePath();
				formToTransfer.setStatusString(msg, false);
				EventBus.publish(new FormStatusEvent(formToTransfer));
			}

			boolean uploadSuccess = AggregateUtils.uploadFilesToServer(serverConnectionInfo, uri, "xml_submission_file",
					file, formFilesMap.get(fileName), documentDescription, action, terminationFuture, formToTransfer);
				if(uploadSuccess)
				{
					PostSubmissionModel postSubmissionModel = new PostSubmissionModel();
					xFormModel.setxFormIdByODK(xFormIdFromOdk);
					postSubmissionModel.setxFormModel(xFormModel);
					postSubmissionModel.setFormFilesMap(formFilesMap);
					postSubmissionModel.setCollectUserModel(collectUserModel);

					// need the following for persisting data in the
					// PlanningSchedule table
					postSubmissionModel.setUserProgramXFormMappingId(userProgramXFormMappingId);
					postSubmissionModel
							.setSubmissionFileString(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null)
									+ "\\" + messages.getMessage(Constants.Odk.ODK_DEFAULT_UPLOAD_FOLDER_NAME, null, null)
									+ "\\" + folderName + "\\" + fileName);

					// Starting the thread
					PostSubmissionThread postSubmissionThread = (PostSubmissionThread) appContext
							.getBean("postSubmissionThread");
					postSubmissionThread.setPostSubmissionModel(postSubmissionModel);
					postSubmissionThread.start();
				
				}

			return uploadSuccess;
		} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
			e.printStackTrace();
			logger.error("Exception in uploading process, Exception message : " , e);
			return false;
		}
	}

	@Override
	@Transactional
	public void postSubmissionWork(PostSubmissionModel postSubmissionModel) {

		try {
			
			logger.info("postSubmissionWork started for : "
					+ postSubmissionModel.getxFormModel().getxFormTitle() + ", for user "
					+ postSubmissionModel.getCollectUserModel().getName());
			
			File submissionFile = new File(postSubmissionModel.getSubmissionFileString());

			PostSubmissionModel postSubmissionModelReturned = saveLastVisitRecord(submissionFile, postSubmissionModel);
			
			if (postSubmissionModelReturned != null) {
				masterRawDataService.persistData(postSubmissionModelReturned);
				logger.info("Last visit data(submission data) saved for xForm "
						+ postSubmissionModel.getxFormModel().getxFormTitle() + ", for user "
						+ postSubmissionModel.getCollectUserModel().getName());
				postSubmissionNotificationWork(postSubmissionModelReturned);
				
			} else {
				logger.error("Could not save last visit data(submission data) for xForm "
						+ postSubmissionModel.getxFormModel().getxFormTitle() + ", for user "
						+ postSubmissionModel.getCollectUserModel().getName());
			}

		} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
			logger.error("Exception in getting submission file for xForm "
					+ postSubmissionModel.getxFormModel().getxFormTitle() , e);
		}

	}


	@Transactional
	private PostSubmissionModel saveLastVisitRecord(File inputFile,
			PostSubmissionModel postSubmissionModel) {
		try {
			String areaCode = null;
			String secondaryAreaName = null;
			String dateOfVisit = null;
			String locationXPath = null;
			String latitude = null;
			String longitude = null;
			String toEmailIdsString = null;
			List<String> toEmailIds = new ArrayList<String>();

			// extracting from submission file
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			XPath xPath = XPathFactory.newInstance().newXPath();

			String instanceId = null;
			instanceId = doc.getElementsByTagName("instanceID").item(0).getTextContent();
//			instanceId = instanceId.replace(":", ""); //sarita commented. confirmed from ratikanta

			postSubmissionModel.setInstanceId(instanceId); // set instance id
															// here instead of
															// calling method,
															// by sarita

			// Extraction area code(AreaId in devinfo term)
			if (postSubmissionModel.getxFormModel() != null) {

				// Keeping the splitted string array
				String areaXPathArray[] = postSubmissionModel.getxFormModel().getAreaXPath() != null
						? postSubmissionModel.getxFormModel().getAreaXPath().split(",") : null;
				if (areaXPathArray != null) {
					for (String areaXPath : areaXPathArray) {

						// There might be spaces in the splitted area XPath
						areaXPath = areaXPath.trim();

						// Only space may come, we need to check
						if (!areaXPath.equals("")) {
							try {
								if (postSubmissionModel.getxFormModel().getAreaXPath() != null) {
									areaCode = xPath
											.compile("/" + postSubmissionModel.getxFormModel().getRootElement()
													+ areaXPath) 
											.evaluate(doc);

									// Checking whether we got the area code or
									// not
									if (areaCode != null && !areaCode.equals("")) {

										// We got the area code(AreaId in
										// devinfo term), so no need to continue
										// the loop
										break;
									}
								}
							} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
								logger.warn("Exception in extacting Area Code Exception message : " + e.getMessage());
								logger.error("Exception in extacting Area Code Exception message : " , e);
							}
						}
					}
				}
			}

		/*	try {
				if (false) {
					secondaryAreaName = xPath.compile("/" + postSubmissionModel.getxFormModel().getxFormIdByODK()
							+ postSubmissionModel.getxFormModel().getSecondaryAreaXPath()).evaluate(doc);
				}
			} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
				logger.warn("Exception in extracting seconday name , Exception message : " + e.getMessage());
				logger.error("Exception in extracting seconday name , Exception message : " , e);
			}*/

			try {
				if (postSubmissionModel.getxFormModel().getDateOfVisitXPath() != null) {
					dateOfVisit = xPath.compile("/" + postSubmissionModel.getxFormModel().getxFormIdByODK()
							+ postSubmissionModel.getxFormModel().getDateOfVisitXPath()).evaluate(doc);
				}
			} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
				logger.error("Exception in extacting date of visit, Exception message : ", e);
			}

			// We can not persist the record if date of visit is not given.
			if (dateOfVisit == null) {
				logger.error("Date of visit for this form is null, please check date of visit xpath. Form id "
						+ postSubmissionModel.getxFormModel().getxFormId() + ", user id "
						+ postSubmissionModel.getCollectUserModel().getUserId());
				return null;
			}

			try {
				if (postSubmissionModel.getxFormModel().getLocationXPath() != null) {
					locationXPath = xPath.compile("/" + postSubmissionModel.getxFormModel().getxFormIdByODK()
							+ postSubmissionModel.getxFormModel().getLocationXPath()).evaluate(doc);
				}
			} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
				logger.warn("Exception in extacting location xpath , Exception message : " + e.getMessage());
				logger.error("Exception in extacting location xpath , Exception message : " , e);
			}

			// toEmailIds
			try {
				if (locationXPath != null && !locationXPath.equals("")) {
					latitude = locationXPath.split(" ")[0];
					longitude = locationXPath.split(" ")[1];
				}
			} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
				logger.warn("Exception in extacting latitude and longitude from location xpath , Exception message : "
						+ e.getMessage());
				logger.error("Exception in extacting latitude and longitude from location xpath , Exception message : " , e);
			}

			if (postSubmissionModel.getxFormModel().getToEmailIdsXPath() != null) {
				toEmailIdsString = xPath.compile("/" + postSubmissionModel.getxFormModel().getxFormIdByODK()
						+ postSubmissionModel.getxFormModel().getToEmailIdsXPath()).evaluate(doc);
			}
			if (toEmailIdsString != null) {
				for (String toEmailId : toEmailIdsString.split(",")) {
					toEmailIds.add(toEmailId.trim());
				}
			}

			postSubmissionModel.setToEmailIds(toEmailIds);
			Area area = null;

			// Getting area id from area code
			if (areaCode != null) {
				area = areaRepository.findByAreaCode(areaCode);
			}
			
			if(area==null)
			{
				area=new Area();
				String blockName = xPath.compile("/" + postSubmissionModel.getxFormModel().getxFormIdByODK()
						+ postSubmissionModel.getxFormModel().getSecondaryAreaXPath()).evaluate(doc);
				Area block=areaRepository.findByAreaCode(blockName);
				List<ChoicesDetails> choiceDetails=choiceDetailsRepository.findByFormFormIdAndLabel(postSubmissionModel.getxFormModel().getFormId(),blockName);
				area.setAreaCode(areaCode);
				area.setParentAreaId(block.getAreaId());
				area.setIsLive(true);
				area.setCreatedDate(new Timestamp(new java.util.Date().getTime()));
				area.setAreaName(choiceDetails.get(0).getChoiceValue());
				area.setAreaCode(choiceDetails.get(0).getLabel());
				if(choiceDetails.get(0).getLabel().contains("DH"))
					area.setAreaLevel(new AreaLevel(6));
				else if(choiceDetails.get(0).getLabel().contains("CH"))
					area.setAreaLevel(new AreaLevel(7));
				else if(choiceDetails.get(0).getLabel().contains("PH"))
					area.setAreaLevel(new AreaLevel(8));
				
				area = areaRepository.save(area);
			}

//			String xFormId = postSubmissionModel.getxFormModel().getxFormId();

//			String rootElement = postSubmissionModel.getxFormModel().getxFormIdByODK();


			if (area != null) {
				LastVisitData lastVisitData = insertRecordInLastVisitDataTable(inputFile.getName(), area,
						secondaryAreaName, dateOfVisit, latitude, longitude, postSubmissionModel);
				postSubmissionModel.setLastVisitDataModel(domainToModelConverter.toLastVisitDataModel(lastVisitData));

				if (lastVisitData != null) {

					

//					List<FacilityScore> facilities = new ArrayList<FacilityScore>();
//					Map<String, String> imgMap = null;

					
			
					return postSubmissionModel;
				} else {
					return null;
				}
			} else {
				logger.warn("Last vist data could not save because no area found for xForm "
						+ postSubmissionModel.getxFormModel().getxFormTitle() + " of user "
						+ postSubmissionModel.getCollectUserModel().getName());
				return null;
			}

		} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
//			e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.error("Instance Id :" + postSubmissionModel.getInstanceId()+ ", Exception in saving last visit record , Exception message : ", e);
			logger.warn("Exception in saving last visit record , Exception message : " + e.getMessage());
			return null;
		}
	}
	
	
	private LastVisitData insertRecordInLastVisitDataTable(String fileName, Area area, String secondaryAreaName,
			String dateOfVist, String latitude, String longitude, PostSubmissionModel postSubmissionModel) {

		try {

			// Extracting image file names if any
			String imageFileNames = "";
			Iterator<Entry<String, List<File>>> it = postSubmissionModel.getFormFilesMap().entrySet().iterator();

			while (it.hasNext()) {
				Map.Entry<String, List<File>> pair = (Map.Entry<String, List<File>>) it.next();

				for (File mediaFile : (List<File>) pair.getValue()) {
					imageFileNames += mediaFile.getName() + ",";
				}
				if (imageFileNames.length() > 0) {
					imageFileNames = imageFileNames.substring(0, imageFileNames.length() - 1);
				}

			}

			CollectUser collectUser = collectUserRepository
					.findByUserId(postSubmissionModel.getCollectUserModel().getUserId());
			XForm xForm = xFormRepository.findByXFormIdAndIsLiveTrue(postSubmissionModel.getxFormModel().getxFormId());

			SimpleDateFormat sdf = new SimpleDateFormat(
					messages.getMessage(Constants.Odk.DATE_OF_VISIT_FORMAT, null, null));

			sdf.setLenient(false);
			SimpleDateFormat sdfAll = new SimpleDateFormat(
					messages.getMessage(Constants.Odk.DATE_OF_VISIT_FORMAT_ALL, null, null));
			sdfAll.setLenient(false);
			LastVisitData lastVisitDatas = null;

			lastVisitDatas = lastVisitDataRepository.findByxFormFormIdAndInstanceId(xForm.getFormId(),postSubmissionModel.getInstanceId());
			if (lastVisitDatas ==null) {

				// Code to check whether submitted record's instance id is same
				// with current submission file

			
				 

				// Create new record
				LastVisitData lastVisitDataLocal = new LastVisitData();
				lastVisitDataLocal.setRawDataExcelPath(	postSubmissionModel.getSubmissionFileString());
				lastVisitDataLocal.setMarkedAsCompleteDate(new Timestamp(new Date().getTime()));
				lastVisitDataLocal
						.setSubmissionFileURL(messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "\\"
								+ messages.getMessage(Constants.Odk.ODK_DEFAULT_FORMS_FOLDER_NAME, null, null) + "\\"
								+ postSubmissionModel.getxFormModel().getxFormId() + "\\"
								+ messages.getMessage(Constants.Odk.ODK_DEFAULT_SUBMISSIONFILE_FOLDER_NAME, null, null)
								+ "\\" + postSubmissionModel.getInstanceId() + "\\");
				lastVisitDataLocal.setSubmissionFileName(fileName);
				try {
					if (dateOfVist != null) {
						lastVisitDataLocal.setDateOfVisit(new java.sql.Date(sdf.parse(dateOfVist).getTime()));
					}
				} catch (ParseException e) {
					try {
						lastVisitDataLocal.setDateOfVisit(new java.sql.Date(sdfAll.parse(dateOfVist).getTime()));
					} catch (ParseException e1) {
						logger.error("Instance Id : " + postSubmissionModel.getInstanceId()+
								"fileName : "+fileName + ", Exception while setting dat of visit in lastVisitData : ",e);
					}
				}
				if (latitude != null) {
					lastVisitDataLocal.setLatitude(latitude);
				}
				if (longitude != null) {
					lastVisitDataLocal.setLongitude(longitude);
				}
				if (postSubmissionModel.getInstanceId() != null) {
					lastVisitDataLocal.setInstanceId(postSubmissionModel.getInstanceId());
				}
				if (secondaryAreaName != null) {
					if (!secondaryAreaName.equals("")) {
						lastVisitDataLocal.setSecondaryAreaName(secondaryAreaName);
					}
				}

				lastVisitDataLocal.setLive(true);
				if (collectUser != null) {
					lastVisitDataLocal.setCreatedBy(collectUser.getName());
				}

				lastVisitDataLocal.setCreatedDate(new Timestamp(new Date().getTime()));
				if (area != null) {
					lastVisitDataLocal.setArea(area);
				}
				if (xForm != null) {
					lastVisitDataLocal.setxForm(xForm);
				}
				if (collectUser != null) {
					lastVisitDataLocal.setUser(collectUser);
				}

				if (!imageFileNames.equals("")) {
					lastVisitDataLocal.setImageFileNames(imageFileNames);
				}
				TimePeriod timPeriod=new TimePeriod();
				
				timPeriod.setTimePeriodId(postSubmissionModel.getxFormModel().getTimeperiodId());
				lastVisitDataLocal.setTimPeriod(timPeriod);

				// Date code

//				SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-M-d");
				Calendar c = Calendar.getInstance();

				// setting first date of the month
				c.set(Calendar.DAY_OF_MONTH, 1);

				// Check whether this visit is a planned visit FOR THIS MONTH or
				// not, if planned then the isPlanned column of
				// LastVistData table will be true otherwise false

				// We are expecting list of planning record

			
					return lastVisitDataRepository.save(lastVisitDataLocal);
				

				
				
			}
			else
			{
					logger.warn("Instance id already uploaded : " + postSubmissionModel.getInstanceId());
					return null;
			}
		} catch (Exception e) { e.printStackTrace(); e.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			logger.warn("Exception in  inserting record in LastVisitData Table, Exception message : " + e.getMessage());
			logger.error("Instance Id : " + postSubmissionModel.getInstanceId()+ 
					"fileName : "+fileName +", Exception in inserting record in LastVisitData Table, Exception message : " , e);
			return null;
		}
	}
	@Override
	public void postSubmissionNotificationWork(
			PostSubmissionModel postSubmissionModel) {

		 {

			String fileName = "";
			try {
				fileName = getSubmittedFileInExcel(postSubmissionModel);

				if (postSubmissionModel != null) {
					if (postSubmissionModel.getLastVisitDataModel() != null) {
						LastVisitData lastVisitData = lastVisitDataRepository.findByLastVisitDataIdAndIsLiveTrue(
								postSubmissionModel.getLastVisitDataModel().getLastVisitDataId());
						if (lastVisitData != null) {
							lastVisitData.setRawDataExcelPath(fileName);
							lastVisitDataRepository.save(lastVisitData);
						} else {
							logger.warn(
									"Could not save raw data excel path in LastVisitData table because record not found to update filename for form"
											+ postSubmissionModel.getxFormModel().getxFormTitle());
						}
					} else {
						logger.warn(
								"Could not save raw data excel path in LastVisitData table because LastVisitDataModel is null for form"
										+ postSubmissionModel.getxFormModel().getxFormTitle());
					}
				}

				Mail mail = new Mail();
				// logger.info(postSubmissionModel.getxFormModel().getxFormId());

				// set to and cc email ids

				mail.setToEmailIds(postSubmissionModel.getToEmailIds());
				String ccEmailIdsString = postSubmissionModel.getxFormModel().getCcEmailIds() != null
						? postSubmissionModel.getxFormModel().getCcEmailIds() : "";

				List<String> ccEmailIds = new ArrayList<String>();

				ccEmailIds.add(postSubmissionModel.getCollectUserModel().getEmailId());
				for (String ccEmailId : ccEmailIdsString.split(",")) {
					ccEmailIds.add(ccEmailId.trim());
				}

				mail.setCcEmailIds(ccEmailIds);

				// set attachments if there is any
				Map<String, String> attachments = new HashMap<String, String>();
				String file[] = fileName.split("//");
				
				// key===name; value==path
				attachments.put(file[file.length - 1].split("#")[0], fileName
						.split(postSubmissionModel.getxFormModel().getxFormId()
								+ "_"
								+ (fileName.split("#").length > 1 ? fileName
										.split("#")[1] : ""))[0]);
				// key===name; value==path

				mail.setAttachments(attachments);

				mail.setFromUserName(messages.getMessage(Constants.FROM, null, null));
				mail.setMessage("Please find the attached submission excel file for the form "
						+ postSubmissionModel.getxFormModel().getxFormTitle() + " of "
						+ postSubmissionModel.getLastVisitDataModel().getAreaModel().getAreaName() );
				mail.setSubject("Submission Instance");
				mail.setToUserName(messages.getMessage(Constants.TO, null, null));

				// send the mail

				mailService.sendMail(mail);
				logger.info("Mail sent for instance id " + postSubmissionModel.getInstanceId());
			} catch (Exception e) {
			e.printStackTrace();
				logger.error("Could not send mail, Exception message : ",e);
				logger.warn("Could not send mail, Exception message : " + e.getMessage());
			}
		} 
	}

	private String getSubmittedFileInExcel(PostSubmissionModel postSubmissionModel) throws Exception {
		
		String formId = postSubmissionModel.getxFormModel().getxFormId();
		File xmlFile = new File(postSubmissionModel.getSubmissionFileString());
		
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder = builderFactory.newDocumentBuilder();
		org.w3c.dom.Document document = builder.parse(xmlFile);

		XPath xPath = XPathFactory.newInstance().newXPath();
		document.getDocumentElement().normalize();

		//THE XLSX FILE TO WRITE 
		//-------------------------KEEP TEMPLATE NAME SAME AS FORMID + .XLSX -----------------ALWAYS................
		
//		FileInputStream fileInputStream = new FileInputStream(context.getRealPath((messages.getMessage(
//				Constants.TEMPLATE_EXCEL_PATH, null, null)))+"\\"+formId+".xlsx");
		
		FileInputStream fileInputStream = new FileInputStream(
				ResourceUtils.getFile("classpath:"+(messages.getMessage(Constants.TEMPLATE_EXCEL_PATH, null, null))
						+ ""+formId+".xlsx"));
		
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
		
		
		XSSFSheet sheet = xssfWorkbook.getSheet(messages.getMessage(
				Constants.SHEET_WRITE_NAME, null, null));
		
		StringBuilder queryString =new StringBuilder(postSubmissionModel.getxFormModel().getxFormIdByODK());
		String splittingStr ="";
		
		XSSFCellStyle cellStyle =xssfWorkbook.createCellStyle();
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(CellStyle.ALIGN_CENTER);
		
		/////create map of choices list name---name and label-----------
		
		XSSFSheet choicesSheet = xssfWorkbook.getSheet(messages.getMessage(
				Constants.SHEET_CHOICES_NAME, null, null));
		
		
		
		Map<String, String> nameLabelMap = new HashMap<String, String>();
		Map<String, Map<String, String>> listNameLabelMap = new HashMap<String, Map<String,String>>();
		
		for (int i = 1; i <= choicesSheet.getLastRowNum(); i++) {
			XSSFRow row = choicesSheet.getRow(i);
			
			if(null!=row){
				XSSFCell listNameCell = row.getCell(0);
				XSSFCell nameCell = row.getCell(1);
				XSSFCell labelCell = row.getCell(2);
				
				if(null!=listNameCell && null!=labelCell && null!=nameCell){
					String nameVal =nameCell.getCellType() == Cell.CELL_TYPE_STRING ? nameCell.getStringCellValue() :
						Integer.toString(((Double)nameCell.getNumericCellValue()).intValue()) ;
					
					String labelVal =labelCell.getCellType() == Cell.CELL_TYPE_STRING ? labelCell.getStringCellValue() :
						Integer.toString(((Double)labelCell.getNumericCellValue()).intValue()) ;
				
				if(listNameLabelMap.containsKey(listNameCell.getStringCellValue() )){
					
					listNameLabelMap.get(listNameCell.getStringCellValue() )
						.put(nameVal.trim(), labelVal);
				}else{
					nameLabelMap = new HashMap<String, String>();
					
					nameLabelMap.put(nameVal.trim(), labelVal);
					
					listNameLabelMap.put(listNameCell.getStringCellValue().trim(), nameLabelMap);
				}
				}
			}
		}
		
		
		if(xssfWorkbook.getSheet(messages.getMessage(
				Constants.SHEET_EXTERNAL_CHOICES_NAME, null, null))!=null)
		{
		XSSFSheet externalChoicesSheet = xssfWorkbook.getSheet(messages.getMessage(
				Constants.SHEET_EXTERNAL_CHOICES_NAME, null, null));
		for (int i = 1; i <= externalChoicesSheet.getLastRowNum(); i++) {
			XSSFRow row = externalChoicesSheet.getRow(i);
			
			if(null!=row){
				XSSFCell listNameCell = row.getCell(0);
				XSSFCell nameCell = row.getCell(1);
				XSSFCell labelCell = row.getCell(2);
				
				if(null!=listNameCell && null!=labelCell && null!=nameCell){
					String nameVal =nameCell.getCellType() == Cell.CELL_TYPE_STRING ? nameCell.getStringCellValue() :
						Integer.toString(((Double)nameCell.getNumericCellValue()).intValue()) ;
					
					String labelVal =labelCell.getCellType() == Cell.CELL_TYPE_STRING ? labelCell.getStringCellValue() :
						Integer.toString(((Double)labelCell.getNumericCellValue()).intValue()) ;
				
				if(listNameLabelMap.containsKey(listNameCell.getStringCellValue() )){
					
					listNameLabelMap.get(listNameCell.getStringCellValue() )
						.put(nameVal.trim(), labelVal);
				}else{
					nameLabelMap = new HashMap<String, String>();
					
					nameLabelMap.put(nameVal.trim(), labelVal);
					
					listNameLabelMap.put(listNameCell.getStringCellValue().trim(), nameLabelMap);
				}
				}
			}
		}
		}
		///end
		
		//iterate through all the sheet rows
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			
			XSSFRow row = sheet.getRow(i);
			if(null!=row ){
				
				
				XSSFCell typeCell = row.getCell(0);
				XSSFCell nameCell = row.getCell(1);
				if( null!=typeCell && !typeCell.getStringCellValue().isEmpty()){
					
//					if(typeCell.getStringCellValue().equalsIgnoreCase(messages.getMessage(
//							Constants.NOTE_XPATH, null, null)))
//						continue;
					
					if(typeCell.getStringCellValue().equalsIgnoreCase(messages.getMessage(
							Constants.BEGIN_GROUP_XPATH, null, null)) || 
							typeCell.getStringCellValue().equalsIgnoreCase(messages.getMessage(
									Constants.BEGIN_REPEAT_XPATH, null, null))||typeCell
							.getStringCellValue().equalsIgnoreCase(
									"begin_group")){
						queryString = queryString.append("/"+nameCell.getStringCellValue());
					}
					if(typeCell.getStringCellValue().equalsIgnoreCase(messages.getMessage(
							Constants.END_GROUP_XPATH, null, null)) || 
							typeCell.getStringCellValue().equalsIgnoreCase(messages.getMessage(
									Constants.END_REPEAT_XPATH, null, null))||typeCell
							.getStringCellValue().equalsIgnoreCase(
									"end_group")){
						splittingStr = queryString.toString().split("/")[queryString.toString().split("/").length-1];
						queryString = new StringBuilder( queryString.substring(0, queryString.lastIndexOf("/"+splittingStr)));
					}
					
					if(!(typeCell.getStringCellValue().equalsIgnoreCase(messages.getMessage(
							Constants.BEGIN_GROUP_XPATH, null, null))
							|| typeCell.getStringCellValue().equalsIgnoreCase(messages.getMessage(
									Constants.END_GROUP_XPATH, null, null))||typeCell
							.getStringCellValue().equalsIgnoreCase(
									"begin_group")||typeCell
							.getStringCellValue().equalsIgnoreCase(
									"end_group"))){
						
						//create response cell for that corresponding xpath
						XSSFCell valueCell = row.createCell(3);
						if(!(nameCell.getStringCellValue().equals("") || nameCell.getStringCellValue() == null)){
							
							queryString = queryString.append("/"+nameCell.getStringCellValue());
							
//							System.out.println("queryString==>>"+queryString);
							
							String response = xPath.compile(queryString.toString()).evaluate(document);
							String type[] = typeCell.getStringCellValue().split("\\s+");
							
							if(!response.equals("") && typeCell.getStringCellValue().contains(messages.getMessage(
									Constants.SELECT_ONE_XPATH, null, null))){
								
								valueCell.setCellValue(listNameLabelMap.get(typeCell.getStringCellValue().split("\\s+")[type.length-1].trim()).get(response));
								
							}else if(!response.equals("") && typeCell.getStringCellValue().contains(messages.getMessage(
									Constants.SELECT_MULTIPLE_XPATH, null, null))){
								StringBuilder multipleVal = new StringBuilder();
								
								for(int m=0; m < response.split("\\s+").length; m++){
									multipleVal.append(m==0 ? listNameLabelMap.get(type[type.length-1])
											.get(response.split("\\s+")[m]) : ", "+
											listNameLabelMap.get(type[type.length-1]).get(response.split("\\s+")[m]));
								}
								
								valueCell.setCellValue(multipleVal.toString());
							}else
								valueCell.setCellValue(xPath.compile(queryString.toString()).evaluate(document));
							
							valueCell.setCellStyle(valueCell.getCellStyle());
							valueCell.setCellStyle(cellStyle);
							
							
							splittingStr = queryString.toString().split("/")[queryString.toString().split("/").length-1];
							queryString = new StringBuilder( queryString.substring(0, queryString.lastIndexOf("/"+splittingStr)));
						}
						
					}
					
				}
			}
	
		}
		
		try {
			// submission excel folder
			if (!new File(messages.getMessage(
					Constants.Odk.ODK_SERVER_DIRECTORY, null, null)
					+ "\\"
					+ messages.getMessage(
							Constants.Odk.ODK_DEFAULT_FORMS_FOLDER_NAME, null,
							null)
					+ "\\"
					+ formId
					+ "\\"
					+ messages.getMessage(
							Constants.SUBMISSION_EXCEL_UPLOAD_FOLDER_NAME,
							null, null)).exists()) {

				// create the directories of submission excel folder
				new File(messages.getMessage(
						Constants.Odk.ODK_SERVER_DIRECTORY, null, null)
						+ "\\"
						+ messages.getMessage(
								Constants.Odk.ODK_DEFAULT_FORMS_FOLDER_NAME,
								null, null)
						+ "\\"
						+ formId
						+ "\\"
						+ messages.getMessage(
								Constants.SUBMISSION_EXCEL_UPLOAD_FOLDER_NAME,
								null, null)).mkdir();
			}
		} catch (Exception e) {
			logger.error("Error in creating default folders, Exception message : "
					+ e.getMessage());
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss-SSS");
		
		String areaName=null;
		for(String areaCode:postSubmissionModel.getxFormModel().getAreaXPath().split(","))
		{
		areaName= xPath.compile(queryString.append("/"+areaCode).toString()).evaluate(document);
		}
		String filepath = 
				messages.getMessage(Constants.Odk.ODK_SERVER_DIRECTORY, null, null) + "//"
						+ messages.getMessage(Constants.Odk.ODK_DEFAULT_FORMS_FOLDER_NAME, null, null) + "//" + formId
						+ "//" + messages.getMessage(Constants.Odk.ODK_DEFAULT_DESTINATIONFILE_FOLDER_NAME, null, null)
						+ "//"+
						formId+"_"+areaName+"_"+sdf.format(new Date())+".xlsx";
		
		FileOutputStream fileOutputStream = new FileOutputStream(filepath);
		xssfWorkbook.write(fileOutputStream);
		xssfWorkbook.close();
		
		return filepath+"#"+areaName;
	}
}
