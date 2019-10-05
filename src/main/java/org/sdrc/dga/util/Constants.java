package org.sdrc.dga.util;

/**
 * This class will keep all the constants that are link to get the value from
 * properties files
 * 
 * @author harsh
 * @since version 1.0.0.0
 *
 */
public class Constants {
	public static final class Odk {
		public static final String ODK_SERVER_DIRECTORY = "odk.directory";
		public static final String ODK_MEDIAFILE_DIRECTORY = "odk.default.upload.mediafile";
		public static final String ODK_DEFAULT_UPLOAD_FOLDER_NAME = "odk.default.upload.folder.name";
		public static final String ODK_DEFAULT_FORMS_FOLDER_NAME = "odk.default.forms.folder.name";
		public static final String ODK_DEFAULT_UPLOADFAILURE_FOLDER_NAME = "odk.default.uploadfailure.folder.name";
		public static final String ODK_DEFAULT_DESTINATIONFILE_FOLDER_NAME = "odk.default.definationfile.folder.name";
		public static final String ODK_DEFAULT_SUBMISSIONFILE_FOLDER_NAME = "odk.default.submissionfile.Rfolder.name";
		public static final String DATE_FORMAT_ALL = "date.format.all";
		
		public static final String DATE_OF_VISIT_FORMAT = "date.of.visit.format";
		public static final String DATE_OF_VISIT_FORMAT_ALL = "date.format.all";

		public static final String MAX_POST_SIZE = "max.post.size";
		public static final String SUBMISSION_CREATE_FOLDER = "submission.create.folder";
		public static final String MEDIA_FILE_UPDATED_DATE = "media.file.updated.date";
	}
	public static final String USER_PRINCIPAL = "UserPrincipal";
	public static final String ERROR_LIST = "errorList";

	public static final String ACCESS_DENIED = "accessDenied";

	public static final String AUTHENTICATION_USERID = "authentication.userid";
	public static final String AUTHENTICATION_PASSWORD = "authentication.password";
	public static final String MESSAGE_SETFORM = "message.setFrom";

	// mail from and to names

	public static final String TO = "to";
	public static final String FROM = "from";

	 public static final  String REFERER ="referer";
	 
	 public static final String SMTP_HOST_KEY = "smtp.host.key";
		public static final String SOCKETFACTORY_PORT_KEY = "socketFactory.port.key";
		public static final String SOCKETFACTORY_CLASS_KEY = "socketFactory.class.key";
		public static final String SMTP_AUTH_KEY = "smtp.auth.key";
		public static final String SMTP_PORT_KEY = "smtp.port.key";

		public static final String SMTP_HOST = "smtp.host";
		public static final String SOCKETFACTORY_PORT = "socketFactory.port";
		public static final String SOCKETFACTORY_CLASS = "socketFactory.class";
		public static final String SMTP_AUTH = "smtp.auth";
		public static final String SMTP_PORT = "smtp.port";

		// template excel path
		public static final String TEMPLATE_EXCEL_PATH = "template.excel.path";
		public static final String FIP_TEMPLATE_EXCEL_PATH = "fip.template.excel.path";
		public static final String SUBMISSION_EXCEL_UPLOAD_FOLDER_NAME = "submission.upload.folder.name";

		// xform xpath
		public static final String BEGIN_GROUP_XPATH = "begin.group.xpath";
		public static final String END_GROUP_XPATH = "end.group.xpath";
		public static final String BEGIN_REPEAT_XPATH = "begin.repeat.xpath";
		public static final String END_REPEAT_XPATH = "end.repeat.xpath";
		public static final String NOTE_XPATH = "note.xpath";
		public static final String SELECT_ONE_XPATH = "select.one.xpath";
		public static final String SELECT_MULTIPLE_XPATH = "select.multiple.xpath";

		// sheet names
		public static final String SHEET_WRITE_NAME = "sheet.write.name";
		public static final String SHEET_CHOICES_NAME = "sheet.choices.name";
		
		public static final String SHEET_EXTERNAL_CHOICES_NAME = "sheet.external.choices.name";
		
		public static final int DH_ID=2;
		public static final int CHC_ID=1;
		public static final int PHC_ID=3;
		public static final int STATE_ID=2;
		
		public static final int DH_LEVEL=6;
		public static final int CHC_LEVEL=7;
		public static final int PHC_LEVEL=8;
		public static final int HSC_LEVEL=9;
		public static final int UPHC_LEVEL=10;
		public static final int UHC_LEVEL=3;
		
		
		public static final String LOG_FILE="dgacg";
		
		public static final String LOG_FILE_ARCHIVED="archived";
		
		public static final String DGA_ROOT_PATH="dga.root.path";
		public static final String DGA_DISTRICT_HEALTH_ACTION_PLAN_PATH = "dga.district.health.action.plan.path";
		public static final String DGA_LAQSHYA = "dga.laqshya.path";
		
		public static final Integer XFORM_DH_ID = 13;
		public static final Integer XFORM_PHC_ID = 14;
		public static final Integer XFORM_CHC_ID = 16;
		public static final Integer XFORM_HSC_ID = 16;

}
