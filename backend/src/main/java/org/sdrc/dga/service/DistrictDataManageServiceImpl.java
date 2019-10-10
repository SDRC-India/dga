package org.sdrc.dga.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.AreaLevel;
import org.sdrc.dga.domain.CollectUser;
import org.sdrc.dga.domain.DDMActionItem;
import org.sdrc.dga.domain.DDMOptionType;
import org.sdrc.dga.domain.DDMOptions;
import org.sdrc.dga.domain.DDMQuestion;
import org.sdrc.dga.domain.DDMQuestionOptionTypeMapping;
import org.sdrc.dga.domain.DDMSections;
import org.sdrc.dga.domain.DDMSubmitionData;
import org.sdrc.dga.domain.Form;
import org.sdrc.dga.model.ActionItemModel;
import org.sdrc.dga.model.DDMDataEntryQuestionModel;
import org.sdrc.dga.model.DDMDistrictReportModel;
import org.sdrc.dga.model.DDMOptionModel;
import org.sdrc.dga.model.DDMQuestionModel;
import org.sdrc.dga.model.DDMReportModel;
import org.sdrc.dga.model.ResponseModel;
import org.sdrc.dga.model.UserModel;
import org.sdrc.dga.repository.AreaLevelRepository;
import org.sdrc.dga.repository.CollectUserRepository;
import org.sdrc.dga.repository.DDMActionItemRepository;
import org.sdrc.dga.repository.DDMOptionRepository;
import org.sdrc.dga.repository.DDMOptionTypeRepository;
import org.sdrc.dga.repository.DDMQuestionOptionTypeMappingRepository;
import org.sdrc.dga.repository.DDMQuestionRepository;
import org.sdrc.dga.repository.DDMSectionRepository;
import org.sdrc.dga.repository.DDMSubmitionDataRepository;
import org.sdrc.dga.util.HeaderFooter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.itextpdf.text.pdf.draw.LineSeparator;

@Service
public class DistrictDataManageServiceImpl implements DistrictDataManageService {

	private final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY");

	private final SimpleDateFormat sdfDateType = new SimpleDateFormat("yyyy-MM-dd");

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DDMSectionRepository sectionRepository;

	@Autowired
	private DDMQuestionOptionTypeMappingRepository questionOptionTypeMappingRepository;

	@Autowired
	private DDMOptionRepository optionRepositry;

	@Autowired
	private DDMOptionTypeRepository optionTypeRepository;

	@Autowired
	private DDMQuestionRepository irfquestionRepository;

	@Autowired
	private DDMSubmitionDataRepository ddmSubmitionDataRepository;

	@Autowired
	private CollectUserRepository collectUserRepository;

	@Autowired
	private DDMActionItemRepository dDMActionItemRepository;

	@Autowired
	private AreaLevelRepository areaLevelRepository;

	@Autowired
	private ServletContext context;

	private final Path instPath = Paths.get("/dga/img");

	private final SimpleDateFormat sdfFull = new SimpleDateFormat("dd-MM-YYYY HH-mm-ss");

	@Override
	@Transactional
	public boolean configureIrfQuestionTemplate() {
		return true;
	}

	int userId;
	Area userArea = null;

	@Override
	public List<DDMDataEntryQuestionModel> getQuestion(int formId, Principal auth, Integer submissioId)
			throws Exception {

		boolean disabled = false;
		boolean submitDisabled = false;
		DDMQuestionModel questionModel = new DDMQuestionModel();
		List<DDMDataEntryQuestionModel> dataEntryQuestionModels = new ArrayList<DDMDataEntryQuestionModel>();
		questionModel.setControlType("id");
		questionModel.setColumnName("id");
		questionModel.setKey(formId);
		String rejectedReason = "";
		DDMSubmitionData submitionData = ddmSubmitionDataRepository.findByIsLiveTrueAndSubmitionId(submissioId);

		List<DDMQuestion> questions = irfquestionRepository
				.findByFormFormIdAndIsLiveTrueOrderByQuestionOrderAsc(formId);

		Map<String, DDMDataEntryQuestionModel> dataEntryQuestionModelMap = new HashMap<String, DDMDataEntryQuestionModel>();

//		Object dataFromSession = oAuth2Utility.getUserModelInfo(auth);
//		Object dataFromSession = oAuth2Utility.getUserModel();
//		UserModel userModel = oAuth2Utility.getUserModel();
		UserModel userModel = new UserModel();
		String usreName = auth.getName();
		CollectUser user = collectUserRepository.findByUsername(usreName);
		userModel.setName(user.getName());
		userModel.setEmailId(user.getEmailId());
		userModel.setUserId(user.getUserId());
		userId = user.getUserId();
		userArea = user.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getArea();
		Object dataFromSession = userModel;

		Map<String, Object> prefetchedData = new HashMap<String, Object>();

		ObjectMapper m = new ObjectMapper();
		Map<String, Object> props = m.convertValue(dataFromSession, Map.class);

		JSONObject jsonObject = new JSONObject(props);
//		JSONObject jsonObject = new JSONObject();

		Set<Integer> rejectedSectionIds = new HashSet<Integer>();

		// if their is any submitionId than that is a old submition, and all data
		// previously submitted are set on question model.
		if (submitionData != null) {
			questionModel.setValue(submitionData.getSubmitionId());
			prefetchedData = inspect(DDMSubmitionData.class, submitionData);
		}

		DDMDataEntryQuestionModel dataEntryQuestionModel = new DDMDataEntryQuestionModel();
		dataEntryQuestionModel.setId(questions.get(0).getSection().getSectionId());
		if (rejectedSectionIds.contains(dataEntryQuestionModel.getId()))
			dataEntryQuestionModel.setRejected(true);
		else
			dataEntryQuestionModel.setRejected(false);

		dataEntryQuestionModel.setRejectedRemark(rejectedReason);
		dataEntryQuestionModel.setName(questions.get(0).getSection().getSectionName());
		dataEntryQuestionModel.setSectionOrder(questions.get(0).getSection().getSectionOrder());
		dataEntryQuestionModel.setSubmitDisabled(submitDisabled);
		List<DDMQuestionModel> questionModels = new ArrayList<DDMQuestionModel>();
		questionModels.add(questionModel);
		dataEntryQuestionModel.setQuestions(questionModels);
		/**
		 * when their is any submitionId than all data pre-fetched from the database are
		 * set disabled format for which the user can't change the previous data. new
		 * services can be added to the existing data. only misc user can be able to
		 * edit the previous data
		 */
//		if (submissioId > 0 && !userModel.getUsername().equals("misc")) {
//			dataEntryQuestionModel.setDisabled(true);
//		} else
		dataEntryQuestionModel.setDisabled(disabled);
		dataEntryQuestionModelMap.put(dataEntryQuestionModel.getName(), dataEntryQuestionModel);

		for (DDMQuestion question : questions) {

			if (((byte) question.getApprovalProcess()) == 1)
				continue;

			questionModel = new DDMQuestionModel();
			questionModel.setAllChecked(false);
			questionModel.setColumnName(question.getColumnn());
			questionModel.setControlType(question.getControlType());
			questionModel.setType(question.getInputType());
			if (question.getFileExtensions() != null)
				questionModel.setFileExtension(question.getFileExtensions().split(","));
			if (question.getDefaultSetting() != null)
				questionModel.setDisabled(question.getDefaultSetting().contains("disabled"));
			else
				questionModel.setDisabled(((byte) question.getApprovalProcess()) == 1);

			if (question.getDependecy()) {
				questionModel.setDependentCondition(question.getDependentCondition().split(","));
				questionModel.setParentColumns(question.getDependentColumn().split(","));
			}

			if (question.getControlType() != "beginRepeat" && question.getGroupId() == null)
				questionModel.setValue(prefetchedData.get(question.getColumnn()) != null
						? prefetchedData.get(question.getColumnn()).toString()
						: null);

			if (question.getControlType().equals("multiSelect"))
				questionModel.setValue(prefetchedData.get(question.getColumnn()) != null
						&& !prefetchedData.get(question.getColumnn()).toString().equals("")
								? Arrays.asList(prefetchedData.get(question.getColumnn()).toString().split(","))
										.stream().mapToInt(Integer::parseInt).toArray()
								: null);

			if (question.getControlType().equals("file") && prefetchedData.get(question.getColumnn()) != null) {
			}

			questionModel.setCurrentDate(sdfDateType.format(new Date()));
			questionModel.setPlaceHolder(question.getPlaceHolder() == null ? "" : question.getPlaceHolder());
			questionModel.setFileSize((double) 102400000);
			questionModel.setKey(question.getQuestionId());
			questionModel
					.setSerialNumb(question.getSection().getSectionOrder() + "." + question.getQuestionSerial() + "  ");
			questionModel.setLabel(question.getQuestionName() != null ? question.getQuestionName() : "");
			if (question.getConstraints() != null) {
				for (String s : question.getConstraints().split(",")) {
					if (s.contains("minlength=")) {
						questionModel.setMinLength(Integer.parseInt(s.split("minlength=")[1].trim()));
					}

					if (s.contains("maxlength=")) {
						questionModel.setMaxLength(Integer.parseInt(s.split("maxlength=")[1].trim()));
					}

					if (s.contains("minDate=")) {
						if (s.split("minDate=")[1].trim() == "today")
							questionModel.setMinDate(sdf.format(new Date()));
						else
							questionModel.setMinDate(s.split("minDate=")[1].trim());
					}

					if (s.contains("maxDate=")) {
						if (s.split("maxDate=")[1].trim() == "today")
							questionModel.setMaxDate(sdf.format(new Date()));
						else
							questionModel.setMaxDate(s.split("maxDate=")[1].trim());
					}

					if (s.contains("maxSize")) {
						questionModel.setFileSize(Double.parseDouble(s.split("maxSize=")[1].trim()));
					}
				}
			}

			if (question.getInputType().contains("multiple"))
				questionModel.setMultiple(true);

			if (question.getQuestionOptionTypeMapping() != null) {
				questionModel.setOptions(formatOption(question));
			}

			if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("fetchTable")) {
				questionModel.setOptions(formatOptionPrefectTable(question));
			}

			if (question.getFeatures() != null && question.getFeatures().contains("parent")) {

				for (String feature : question.getFeatures().split(",")) {
					feature = feature.trim();
					if (feature.contains("parent:")) {
						questionModel.setOptionsParentColumn(feature.split(":")[1]);

					}
				}
			}

			if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("info")) {

				for (String feature : question.getDefaultSetting().split(",")) {
					feature = feature.trim();
					if (feature.contains("info:")) {
						questionModel.setInfoMessage(feature.split(":")[1]);
						questionModel.setInfoAvailable(true);

					}
				}
			}

			questionModel.setRequired(((byte) question.getFinalizeMandatory()) == 1);
			questionModel.setTriggable(false);

			if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("prefetchfetchDate")
					&& questionModel.getValue() == null) {
				questionModel.setValue(sdfDateType.format(new Date()));
			}

			// set values for the qustion
			if (question.getControlType() != "beginRepeat" && question.getGroupId() == null)
				questionModel.setValue(prefetchedData.get(question.getColumnn()) != null
						? prefetchedData.get(question.getColumnn()).toString()
						: null);

			if (question.getControlType().equals("multiSelect"))
				questionModel.setValue(prefetchedData.get(question.getColumnn()) != null
						&& !prefetchedData.get(question.getColumnn()).toString().equals("")
								? Arrays.asList(prefetchedData.get(question.getColumnn()).toString().split(","))
										.stream().mapToInt(Integer::parseInt).toArray()
								: null);

			// end

			if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("prefetch")
					&& questionModel.getValue() == null
					&& !question.getDefaultSetting().contains("prefetchfetchDate")) {
				questionModel.setValue(getValuePrfecthed(question, jsonObject));
			}

			if (question.getGroupId() != null) {
				questionModel.setGroupParentId(question.getGroupId());

				int minimumRepeats = 1;
				if (question.getDefaultSetting() != null && question.getDefaultSetting().contains("minimumRepeats")) {
					for (String feature : question.getDefaultSetting().split(",")) {
						feature = feature.trim();
						if (feature.contains("minimumRepeats")) {
							minimumRepeats = Integer.parseInt(feature.split(":")[1]);
						}
					}
				}

				List<?> prefetchedDataForRepeat = (List<?>) prefetchedData.get(question.getGroupId());
				if (prefetchedDataForRepeat != null && prefetchedDataForRepeat.size() > 0) {

					for (int i = 0; i < prefetchedDataForRepeat.size(); i++) {
						Map<String, Object> beginRepeatDataMap = inspect(prefetchedDataForRepeat.get(i).getClass(),
								prefetchedDataForRepeat.get(i));

						DDMQuestionModel clonedQuestionModel = (DDMQuestionModel) questionModel.clone();
						clonedQuestionModel.setValue(beginRepeatDataMap.get(clonedQuestionModel.getColumnName()) != null
								? beginRepeatDataMap.get(clonedQuestionModel.getColumnName()).toString()
								: null);

						if (clonedQuestionModel.getControlType().equals("multiSelect"))
							clonedQuestionModel
									.setValue(beginRepeatDataMap.get(clonedQuestionModel.getColumnName()) != null
											? beginRepeatDataMap.get(clonedQuestionModel.getColumnName()).toString()
													.split(",")
											: null);
						if (clonedQuestionModel.getControlType().equals("file")
								&& beginRepeatDataMap.get(clonedQuestionModel.getColumnName()) != null) {

							clonedQuestionModel
									.setFileValues(beginRepeatDataMap.get(question.getColumnn()).toString().split(","));
						}

						clonedQuestionModel.setIndexNumberTrack(
								question.getColumnn() + "_" + beginRepeatDataMap.get("indexTrackNum"));

						clonedQuestionModel.setRemovable(beginRepeatDataMap.get("removable") != null
								? Boolean.valueOf(beginRepeatDataMap.get("removable").toString())
								: true);
//						if (submissioId > 0 && !userModel.getUsername().equals("misc")
//								&& !question.getControlType().equals("beginRepeat")) {
//
//							clonedQuestionModel.setRemovable(false);
//						}

						if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
								.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
								.findFirst().get().getChildQuestionModels() != null
								&& dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
										.stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels().size() > i) {
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels().get(i).add(clonedQuestionModel);

						}

						else {

							List<List<DDMQuestionModel>> childQuestionModels = null;
							if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels() == null) {
								childQuestionModels = new ArrayList<List<DDMQuestionModel>>();
							} else {
								childQuestionModels = dataEntryQuestionModelMap
										.get(question.getSection().getSectionName()).getQuestions().stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels();
							}

							List<DDMQuestionModel> childQuestionModel = new ArrayList<DDMQuestionModel>();
							childQuestionModel.add(clonedQuestionModel);
							childQuestionModels.add(childQuestionModel);
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().setChildQuestionModels(childQuestionModels);

						}

					}

				}

				else {

					for (int i = 0; i < minimumRepeats; i++) {
						DDMQuestionModel clonedQuestionModel = (DDMQuestionModel) questionModel.clone();
						clonedQuestionModel.setIndexNumberTrack(question.getColumnn() + "_" + i);
						if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
								.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
								.findFirst().get().getChildQuestionModels() != null
								&& dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
										.stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels().size() > i) {
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels().get(i).add(clonedQuestionModel);

						}

						else {
							List<List<DDMQuestionModel>> childQuestionModels = null;
							if (dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().getChildQuestionModels() == null) {
								childQuestionModels = new ArrayList<List<DDMQuestionModel>>();
							} else {
								childQuestionModels = dataEntryQuestionModelMap
										.get(question.getSection().getSectionName()).getQuestions().stream()
										.filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
										.findFirst().get().getChildQuestionModels();
							}

							List<DDMQuestionModel> childQuestionModel = new ArrayList<DDMQuestionModel>();
							childQuestionModel.add(clonedQuestionModel);
							childQuestionModels.add(childQuestionModel);
							dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
									.stream().filter(d -> d.getColumnName().trim().equals(question.getGroupId().trim()))
									.findFirst().get().setChildQuestionModels(childQuestionModels);
						}

					}

				}

			} else {
				if (dataEntryQuestionModelMap.containsKey(question.getSection().getSectionName())) {
					dataEntryQuestionModelMap.get(question.getSection().getSectionName()).getQuestions()
							.add(questionModel);
				} else {
					dataEntryQuestionModel = new DDMDataEntryQuestionModel();
					dataEntryQuestionModel.setId(question.getSection().getSectionId());
					if (rejectedSectionIds.contains(dataEntryQuestionModel.getId()))
						dataEntryQuestionModel.setRejected(true);
					else
						dataEntryQuestionModel.setRejected(false);

					dataEntryQuestionModel.setSubmitDisabled(submitDisabled);
					dataEntryQuestionModel.setRejectedRemark(rejectedReason);
//					if (submissioId > 0 && !userModel.getUsername().equals("misc")
//							&& !question.getControlType().equals("beginRepeat"))
//						dataEntryQuestionModel.setDisabled(true);
//					else
					dataEntryQuestionModel.setDisabled(false);

					dataEntryQuestionModel.setName(question.getSection().getSectionName());
					dataEntryQuestionModel.setSectionOrder(question.getSection().getSectionOrder());
					questionModels = new ArrayList<DDMQuestionModel>();
					questionModels.add(questionModel);
					dataEntryQuestionModel.setQuestions(questionModels);
					dataEntryQuestionModelMap.put(dataEntryQuestionModel.getName(), dataEntryQuestionModel);

				}
			}

		}

		dataEntryQuestionModels = new ArrayList<DDMDataEntryQuestionModel>(dataEntryQuestionModelMap.values());

		return dataEntryQuestionModels;

	}

	/**
	 * 
	 * @param className
	 * @param institutionData
	 * @return map of key and object of questions inside begin repeat
	 */
	private Map<String, Object> inspect(Class<?> className, Object institutionData) {
		Field[] fields = className.getDeclaredFields();
		Map<String, Object> responseMap = new HashMap<String, Object>();

		for (Field field : fields) {
			String getter = "";

			if (field.getType().getSimpleName() == "boolean" && field.getName().toLowerCase().contains("removable")) {
				getter = "is" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
			} else if (field.getType().getSimpleName() != "boolean") {
				getter = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
			}

			if (getter.equals(""))
				continue;

			try {

				Method method = className.getMethod(getter);
				Object value = "";
				value = method.invoke(institutionData);

				if (value != null) {
					if (value instanceof Area) {
						responseMap.put(field.getName(), ((Area) value).getAreaId());
						responseMap.put(field.getName() + "_Obj", ((Area) value));
					} else if (value instanceof DDMOptions) {
						responseMap.put(field.getName(), ((DDMOptions) value).getOptionId());
					} else if (value instanceof List<?>) {
						responseMap.put(field.getName(), value);
					} else {
						responseMap.put(field.getName(), value);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return responseMap;
	}

	/**
	 * Getting options data pre-fetched from DB
	 * 
	 * @param question
	 * @return
	 */
	private List<DDMOptionModel> formatOptionPrefectTable(DDMQuestion question) {
		List<DDMOptionModel> optionModels = new ArrayList<DDMOptionModel>();
		for (String feature : question.getDefaultSetting().split("@")) {
			feature = feature.trim();
			if (feature.contains("fetchTable")) {
				String queryString = feature.split(":")[1];
				if (queryString.contains("AreaLevelId=4"))
					queryString = queryString + " and AreaId=" + userArea.getAreaId();
				if (queryString.contains("Area where AreaLevelId in"))
					queryString = "select * from Area where AreaLevelId=6 and ParentAreaId=" + userArea.getAreaId()
							+ " or ParentAreaId in (select AreaId from Area where ParentAreaId=" + userArea.getAreaId()
							+ ")";

				Query query = entityManager.createNativeQuery(queryString);
				List<Object[]> datas = query.getResultList();
				if (queryString.contains("AreaLevel where AreaLevelId in")) {
					for (Object[] data : datas) {
						DDMOptionModel optionModel = new DDMOptionModel();
						optionModel.setKey(Integer.parseInt(data[0].toString()));
						optionModel.setSelected(false);
						optionModel.setValue(data[2].toString());
						optionModel.setOrder(Integer.parseInt(data[0].toString()));
						optionModel.setParentKey(Integer.parseInt(data[3].toString()));
						optionModel.setLive(true);

						optionModels.add(optionModel);

					}
				} else {
					for (Object[] data : datas) {
						DDMOptionModel optionModel = new DDMOptionModel();
						optionModel.setKey(Integer.parseInt(data[0].toString()));
						optionModel.setSelected(false);
						optionModel.setValue(data[2].toString());
						optionModel.setOrder(Integer.parseInt(data[0].toString()));
						optionModel.setParentKey(Integer.parseInt(data[9].toString()));
						optionModel.setLive(Boolean.parseBoolean(data[5].toString()));

						optionModels.add(optionModel);

					}
				}

			}
		}
		return optionModels;
	}

	/**
	 * Here list of optionModel is created for that question
	 * 
	 * @param question
	 * @return List<IrfOptionMOdel>
	 */
	private List<DDMOptionModel> formatOption(DDMQuestion question) {
		List<DDMOptionModel> optionModels = new ArrayList<DDMOptionModel>();
		question.getQuestionOptionTypeMapping().getOptionType().getOptions().forEach(d -> {
			DDMOptionModel optionModel = new DDMOptionModel();
			optionModel.setKey(d.getOptionId());
			optionModel.setOrder(d.getOptionOrder());
			optionModel.setSelected(false);
			optionModel.setValue(d.getOptionName());
			if (d.getParentKey() != null)
				optionModel.setParentKey(d.getParentKey().getOptionId());
			optionModels.add(optionModel);

		});

		return optionModels;
	}

	private String getValuePrfecthed(DDMQuestion question, JSONObject dataFromSession) {
		JSONObject parsedData = new JSONObject();
		for (String feature : question.getDefaultSetting().split(",")) {
			feature = feature.trim();
			if (feature.contains("fetchsession")) {
				String jsonPath = feature.split(":")[1];

				parsedData = dataFromSession;
				for (String path : jsonPath.split("\\.")) {
					if (parsedData.get(path) instanceof Map)

						parsedData = new JSONObject((Map) parsedData.get(path));

					else if (parsedData.get(path) instanceof ArrayList<?>) {
						List<LinkedHashMap<String, Object>> mapObject = (List<LinkedHashMap<String, Object>>) parsedData
								.get(path);
						parsedData = new JSONObject(mapObject.get(0));
					}

					else
						return parsedData.get(path).toString();
				}

			}
		}
		return parsedData.toJSONString();
	}

	@Override
	@Transactional
	public ResponseModel saveSubmitData(List<DDMQuestionModel> questionModels, Principal auth) {
		ResponseModel responseModel = new ResponseModel();

		DDMSubmitionData submitionData = new DDMSubmitionData();

//		Object dataFromSession = tokenInfoExtractor.getUserModelInfo(auth);
//		Object dataFromSession = oAuth2Utility.getUserModel();
		UserModel userModel = new UserModel();
		String usreName = auth.getName();
		CollectUser user = collectUserRepository.findByUsername(usreName);
		userModel.setName(user.getName());
		userModel.setEmailId(user.getEmailId());
		userModel.setUserId(user.getUserId());
		Object dataFromSession = userModel;

		Map<String, Object> prefetchedData = new HashMap<String, Object>();

		ObjectMapper m = new ObjectMapper();
		Map<String, Object> props = m.convertValue(dataFromSession, Map.class);

		JSONObject jsonObject = new JSONObject(props);

//		JSONObject jsonObject = new JSONObject((Map) dataFromSession);
		// Account user = new Account();
		Map<String, Object> beginDataObject = new LinkedHashMap<String, Object>();
		final ObjectMapper mapper = new ObjectMapper();
//		List<Attachment> attachements = new ArrayList<Attachment>();

		Map<String, Object> datObject = new LinkedHashMap<String, Object>();
		int formId = 0;
		for (DDMQuestionModel questionModel : questionModels) {
			if (questionModel.getValue() != null || questionModel.getControlType().equals("id")
					|| questionModel.getControlType().equals("beginRepeat")
					|| questionModel.getControlType().equals("beginRepeatImageRow")) {
				switch (questionModel.getControlType()) {
				case "id":
					formId = questionModel.getKey();
					if (questionModel.getValue() == null
							|| Integer.parseInt(questionModel.getValue().toString()) == 0) {

						List<DDMQuestionModel> fileModels = questionModels.stream()
								.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
						;
						for (DDMQuestionModel fileModel : fileModels) {
							if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
									&& fileModel.getValue().toString().trim().equals(""))) {
								for (Object attachmentObject : (List<?>) fileModel.getValue()) {
//									Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
//									attachment.setAttachmentId(null);
//									attachements.add(attachment);
								}

							}

						}

						continue;
					}

					List<DDMQuestionModel> fileModels = questionModels.stream()
							.filter(d -> d.getControlType().equals("file")).collect(Collectors.toList());
					;
					for (DDMQuestionModel fileModel : fileModels) {
						if (fileModel.getValue() != null && !(fileModel.getValue() instanceof String
								&& fileModel.getValue().toString().trim().equals(""))) {
							for (Object attachmentObject : (List<?>) fileModel.getValue()) {
//									Attachment attachment = mapper.convertValue(attachmentObject, Attachment.class);
//									attachment.setAttachmentId(null);
//									attachements.add(attachment);
							}

						}

					}

					datObject.put(
							Stream.of(DDMSubmitionData.class.getDeclaredFields())
									.filter(field -> field.isAnnotationPresent(Id.class)).findFirst().get().getName(),
							Integer.parseInt(questionModel.getValue().toString()));

//					datObject.put(
//							questionModel.getColumnName(),
//							Integer.parseInt(questionModel.getValue().toString()));

					break;
				case "dropdown":
				case "radio":
					if (questionModel.getValue().toString().trim().equals("")) {
						continue;
					}

					datObject.put(questionModel.getColumnName(), Integer.parseInt(questionModel.getValue().toString()));

					break;

//				case "autocomplete":
//					String bName=getBname(cdata,questionModel.getValue().toString());
//					datObject.put(questionModel.getColumnName(), bName);
//					break;

				case "file":
					String fileData = null;
					if (questionModel.getValue() instanceof String) {
						continue;
					}
					List<Object> attachments = (List<Object>) questionModel.getValue();
					for (Object map : attachments) {
						final ObjectMapper mapper1 = new ObjectMapper();

					}
					datObject.put(questionModel.getColumnName(), fileData);
					break;
				case "multiSelect":

					if (questionModel.getValue() instanceof String) {
						continue;
					}
					String joined1 = null;
					for (Integer value : (ArrayList<Integer>) questionModel.getValue()) {
						if (joined1 == null) {
							joined1 = value.toString();
						} else {
							joined1 += "," + value;
						}
					}

					datObject.put(questionModel.getColumnName(), joined1);
					break;
				case "beginRepeat":
				case "beginRepeatImageRow":

					List<Map<String, Object>> childDataObjectMapList = new ArrayList<>();
					for (List<DDMQuestionModel> childQuestionModels : questionModel.getChildQuestionModels()) {
						Map<String, Object> childDataObject = new LinkedHashMap<String, Object>();
						for (DDMQuestionModel childQuestionModel : childQuestionModels) {

							if (!childQuestionModel.isRemovable())
								childDataObject.put("removable", childQuestionModel.isRemovable());

							if (childQuestionModel.getValue() != null) {
								switch (childQuestionModel.getControlType()) {

								case "dropdown":
								case "radio":
									if (childQuestionModel.getValue().toString().trim().equals("")) {
										continue;
									}
									childDataObject.put(childQuestionModel.getColumnName(),
											Integer.parseInt(childQuestionModel.getValue().toString()));
									break;

								case "file":
									String fileData1 = null;
									if (childQuestionModel.getValue() instanceof String) {
										continue;
									}
									List<Object> attachments1 = (List<Object>) childQuestionModel.getValue();
									for (Object map : attachments1) {
										final ObjectMapper mapper1 = new ObjectMapper();
//										Attachment d = mapper1.convertValue(map, Attachment.class);
//										if (fileData1 == null) {
//											fileData1 = d.getAttachmentId().toString();
//										} else {
//											fileData1 += "," + d.getAttachmentId();
//										}
//										Attachment attachment = mapper.convertValue(map, Attachment.class);
//										attachment.setAttachmentId(null);
//										attachements.add(attachment);
									}

									childDataObject.put(childQuestionModel.getColumnName(), fileData1);
									break;
								case "multiSelect":
									if (childQuestionModel.getValue() instanceof String) {
										continue;
									}
									String joined = String.join(",", (CharSequence[]) childQuestionModel.getValue());
									childDataObject.put(childQuestionModel.getColumnName(), joined);
									break;
								case "beginRepeat":
									break;
								case "heading":
									break;
								case "beginRepeatImageRow":
									break;

								default:
									childDataObject.put(childQuestionModel.getColumnName(),
											childQuestionModel.getValue());

									childDataObject.put("indexTrackNum", childQuestionModel.getIndexNumberTrack()
											.split(childQuestionModel.getColumnName() + "_")[1]);
								}

							}

						}
						if (!childDataObject.isEmpty()) {
							childDataObjectMapList.add(childDataObject);
						}

					}
					if (childDataObjectMapList.size() > 0)
						beginDataObject.put(questionModel.getColumnName(), childDataObjectMapList);
					break;
				default:
					datObject.put(questionModel.getColumnName(), questionModel.getValue().toString().trim());
				}

			}

		}
		submitionData = mapper.convertValue(datObject, DDMSubmitionData.class);
		submitionData.setLive(true);

		/**
		 * deleting old servies for the submition when some services are added to the
		 * existing submition then all the services are inserted into the DB so that
		 * before added to the services for the submition all services avalable for that
		 * submition is deleted from table.
		 */

		if (submitionData.getSubmitionId() > 0) {
			List<DDMActionItem> oldServices = dDMActionItemRepository.findByDdmSubmitionData(submitionData);

			for (DDMActionItem oldService : oldServices) {
				dDMActionItemRepository.deleteByActionItemId(oldService.getActionItemId());
			}

		}

		List<DDMActionItem> irfServices = new ArrayList<DDMActionItem>();

		for (String key : beginDataObject.keySet()) {
			for (Object data : (List<?>) beginDataObject.get(key)) {

				{

					switch (key.trim()) {
					case "actionItem":
						DDMActionItem servicetDetail = mapper.convertValue(data, DDMActionItem.class);
						servicetDetail.setDdmSubmitionData(submitionData);
						;
						servicetDetail.setLive(true);
						irfServices.add(servicetDetail);
						break;

					}
				}

			}

		}

		if (submitionData.getSubmitionId() == 0) {
			submitionData.setCreatedBy(user.getUserId());
		}

		else {
			submitionData.setCreatedBy(user.getUserId());
			submitionData.setUpdatedBy(user.getUserId());
		}

		submitionData = ddmSubmitionDataRepository.save(submitionData);

		if (irfServices.size() > 0) {
			dDMActionItemRepository.save(irfServices);
		}
		responseModel.setStatusCode(HttpStatus.OK.value());
		responseModel.setMessage("Submission successful");

		return responseModel;
	}

	@Override
	public int getSubmissionId(Principal principal) {
		String usreName = principal.getName();
		CollectUser user = collectUserRepository.findByUsername(usreName);
		DDMSubmitionData submissionData = ddmSubmitionDataRepository.findByIsLiveTrueAndCreatedBy(user.getUserId());
		int submissionId;
		if (submissionData != null) {
			submissionId = submissionData.getSubmitionId();
		} else {
			submissionId = 0;
		}
		return submissionId;
	}

	@Override
	public String generatePDF(List<DDMDataEntryQuestionModel> dataEntryModels, Principal auth,
			HttpServletResponse response, HttpServletRequest request) throws Exception {

		String uri = request.getRequestURI();
		String url = request.getRequestURL().toString();
		Font sectionBold = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
		Font smallBold = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
		Font smallBoldWhite = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
		Font dataFont = new Font(Font.FontFamily.HELVETICA, 10);
		BaseColor cellColor = WebColors.getRGBColor("#E8E3E2");
		BaseColor headerColor = WebColors.getRGBColor("#333a3b");

		url = url.replaceFirst(uri, "");

		Image formDataMainImage = null;
		int k = 0;

		Document document = new Document(PageSize.A4);

		// creating folder if not present
		File file = this.instPath.toFile();
		if (!file.exists()) {
			file.mkdirs();
		}
		// to be added
		String outputPath = this.instPath.resolve("Submission Data" + sdfFull.format(new java.util.Date()) + ".pdf")
				.toAbsolutePath().toString();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));

		// setting Header Footer.PLS Refer to org.sdrc.scps.util.HeaderFooter
		HeaderFooter headerFooter = new HeaderFooter(url);
		writer.setPageEvent(headerFooter);

		document.open();
		document.addAuthor(url);
		Paragraph blankSpace = new Paragraph();
		blankSpace.setAlignment(Element.ALIGN_CENTER);
		blankSpace.setSpacingAfter(10);
		Chunk blankSpaceChunk = new Chunk("          ");
		blankSpace.add(blankSpaceChunk);
		document.add(blankSpace);

		document.add(Chunk.NEWLINE);
		for (DDMDataEntryQuestionModel dataEntryQuestionModel : dataEntryModels) {
			Paragraph p = new Paragraph(
					dataEntryQuestionModel.getSectionOrder() + " : " + dataEntryQuestionModel.getName(), sectionBold);

			p.setSpacingAfter(10);
			document.add(p);

			document.add(new LineSeparator());
			for (DDMQuestionModel questionModel : dataEntryQuestionModel.getQuestions()) {

				if (questionModel.getControlType().equals("id"))
					continue;

				if (!questionModel.getControlType().equals("multiSelect") && questionModel.getOptions() != null
						&& questionModel.getOptions().size() > 0 && questionModel.getValue() != null
						&& !questionModel.getValue().toString().trim().equals("")) {
					questionModel.setValue(questionModel.getOptions().stream()
							.filter(d -> d.getKey() == Integer.parseInt(questionModel.getValue().toString()))
							.findFirst().get().getValue());
				}

				if (questionModel.getControlType().equals("multiSelect") && questionModel.getValue() != null
						&& !questionModel.getValue().toString().trim().equals("")) {
					String valuesFromOptions = "";
					for (Integer id : (ArrayList<Integer>) questionModel.getValue()) {
						String option = questionModel.getOptions().stream().filter(d -> d.getKey() == id).findFirst()
								.get().getValue();
						if (valuesFromOptions == "") {
							valuesFromOptions = option;
						} else {
							valuesFromOptions += "," + option;
						}
					}
					questionModel.setValue(valuesFromOptions);
				}

				if (questionModel.getControlType().equals("file") && questionModel.getValue() != null
						&& !questionModel.getValue().toString().trim().equals("")) {

					String files = "";
					for (Object attachmentObject : (List<?>) questionModel.getValue()) {

					}
					questionModel.setValue(files);
				}

				if (questionModel.getChildQuestionModels() != null
						&& questionModel.getControlType().equalsIgnoreCase("beginRepeat")) {

					if (questionModel.getLabel() != "") {
						Paragraph paragraph = new Paragraph(
								questionModel.getDependentCondition() != null ? "\t \t " + questionModel.getLabel()
										: questionModel.getLabel(),
								smallBold);
						paragraph.setSpacingAfter(10);
						document.add(paragraph);

					}

					PdfPTable beginRepeatTable = new PdfPTable(5);
					beginRepeatTable.setWidthPercentage(100f);
					beginRepeatTable.setHeaderRows(1);

					// Service received from column header
					PdfPCell headerCell = new PdfPCell(new Paragraph("Facility Type ", smallBoldWhite));
					headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
					headerCell.setBackgroundColor(headerColor);
					headerCell.setBorderColor(BaseColor.WHITE);
					beginRepeatTable.addCell(headerCell);

					// Service received column header
					PdfPCell headerCell1 = new PdfPCell(new Paragraph("Facility", smallBoldWhite));
					headerCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
					headerCell1.setBackgroundColor(headerColor);
					headerCell1.setBorderColor(BaseColor.WHITE);
					beginRepeatTable.addCell(headerCell1);

					// Date column header
					PdfPCell headerCell2 = new PdfPCell(new Paragraph("Action Item ", smallBoldWhite));
					headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
					headerCell2.setBackgroundColor(headerColor);
					headerCell2.setBorderColor(BaseColor.WHITE);
					beginRepeatTable.addCell(headerCell2);

					// Date column header
					PdfPCell headerCell3 = new PdfPCell(new Paragraph("Status ", smallBoldWhite));
					headerCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
					headerCell3.setBackgroundColor(headerColor);
					headerCell3.setBorderColor(BaseColor.WHITE);
					beginRepeatTable.addCell(headerCell3);

					// Date column header
					PdfPCell headerCell4 = new PdfPCell(new Paragraph("Remark", smallBoldWhite));
					headerCell4.setHorizontalAlignment(Element.ALIGN_CENTER);
					headerCell4.setBackgroundColor(headerColor);
					headerCell4.setBorderColor(BaseColor.WHITE);
					beginRepeatTable.addCell(headerCell4);

					int i = 0;
					for (List<DDMQuestionModel> questionModels : questionModel.getChildQuestionModels()) {

						String serviceReceived = null;
						String actionItem = null;
						String status = null;
						String remark = null;

						// Control is going to set the service received again and again, to avoid that
						// following variable is used
						boolean hasServiceReceived = false;

						for (DDMQuestionModel questionModel1 : questionModels) {

							String cellValue = null;

							// Service Received From
							if (questionModel1.getColumnName().equals("q3")) {
								cellValue = questionModel1.getOptions().stream().filter(
										d -> d.getKey() == Integer.parseInt(questionModel1.getValue().toString()))
										.findFirst().get().getValue();
							}

							// Service Received
							if (questionModel1.getColumnName().equals("q4")) {

								serviceReceived = questionModel1.getValue().toString().isEmpty() ? null
										: questionModel1.getOptions().stream()
												.filter(d -> d.getKey() == Integer
														.parseInt(questionModel1.getValue().toString()))
												.findFirst().get().getValue();

							}

							if (questionModel1.getColumnName().equals("q6")) {

								status = questionModel1.getValue().toString().isEmpty() ? null
										: questionModel1.getOptions().stream()
												.filter(d -> d.getKey() == Integer
														.parseInt(questionModel1.getValue().toString()))
												.findFirst().get().getValue();

							}

							if (questionModel1.getColumnName().equals("q5")) {
								actionItem = questionModel1.getValue().toString().isEmpty() ? null
										: questionModel1.getValue().toString();
							}

							if (questionModel1.getColumnName().equals("q7")) {
								remark = questionModel1.getValue().toString().isEmpty() ? null
										: questionModel1.getValue().toString();
							}

							// Checking whether any services received from the Anganwadi Centre/ AWW or not
							if (serviceReceived == null || (serviceReceived != null && serviceReceived.isEmpty())) {
								// No service received from the Anganwadi Centre/ AWW, need to check School
								if (questionModel1.getColumnName().equals("q15")) {
									serviceReceived = questionModel1.getValue().toString().isEmpty() ? null
											: questionModel1.getOptions().stream()
													.filter(d -> d.getKey() == Integer
															.parseInt(questionModel1.getValue().toString()))
													.findFirst().get().getValue();
								}
							}

							// Checking any service received from Anganwadi Centre/ AWW or School
							if (serviceReceived == null || (serviceReceived != null && serviceReceived.isEmpty())) {
								// No service received from the Anganwadi Centre/ AWW and School, need to check
								// LSC
								if (questionModel1.getColumnName().equals("q16")) {
									serviceReceived = questionModel1.getValue().toString().isEmpty() ? null
											: questionModel1.getOptions().stream()
													.filter(d -> d.getKey() == Integer
															.parseInt(questionModel1.getValue().toString()))
													.findFirst().get().getValue();
								}
							}

							// Checking any service received from Anganwadi Centre/ AWW or School or LSC
							if (serviceReceived == null || (serviceReceived != null && serviceReceived.isEmpty())) {
								// No service received from the Anganwadi Centre/ AWW, School and LSC need to
								// check Community
								if (questionModel1.getColumnName().equals("q17")) {

									// checking the id. If the id is 51 then it is the other option.
									if (!questionModel1.getValue().toString().isEmpty()) {
										int id = Integer.parseInt(questionModel1.getValue().toString());
										if (id == 51) {
											serviceReceived = "Other-" + (String) questionModels.stream()
													.filter(model -> model.getColumnName().equals("q16"))
													.collect(Collectors.toList()).get(0).getValue();
										} else {

											serviceReceived = questionModel1.getValue().toString().isEmpty() ? null
													: questionModel1.getOptions().stream().filter(d -> d.getKey() == id)
															.findFirst().get().getValue();
										}
									}

								}
							}

							// Date
							if (questionModel1.getColumnName().equals("q15")) {
								String date = (String) questionModel1.getValue();
								date = date.split("-")[1] + "-" + date.split("-")[2] + "-" + date.split("-")[0];
								cellValue = date;
							}

							if (questionModel1.getColumnName().equals("q3")
									|| questionModel1.getColumnName().equals("q17")) {
								// If "service received from" or "date", execute the following code, otherwise
								// go to else block to find out the service

								// creating one cell
								PdfPCell responseCell = new PdfPCell(new Paragraph(cellValue, dataFont));
								responseCell.setBorderColor(BaseColor.WHITE);
								responseCell.setHorizontalAlignment(Element.ALIGN_CENTER);
								if (i % 2 == 0) {
									responseCell.setBackgroundColor(cellColor);

								} else {
									responseCell.setBackgroundColor(BaseColor.LIGHT_GRAY);

								}
								responseCell.setBorderColor(BaseColor.WHITE);
								beginRepeatTable.addCell(responseCell);

							} else {
								/*
								 * Have to check, we have go the "service received" or not, if received, we will
								 * create the cell and put the value, otherwise skip the loop
								 */
								if (!(serviceReceived == null || (serviceReceived != null && serviceReceived.isEmpty()))
										|| (actionItem != null && actionItem.isEmpty())
										|| (status != null && status.isEmpty())
										|| (remark != null && remark.isEmpty()) && !hasServiceReceived) {

									// creating one cell
									PdfPCell responseCell = new PdfPCell(new Paragraph(serviceReceived, dataFont));
									PdfPCell actionCell = new PdfPCell(new Paragraph(actionItem, dataFont));
									PdfPCell statusCell = new PdfPCell(new Paragraph(status, dataFont));
									PdfPCell remarkCell = new PdfPCell(new Paragraph(remark, dataFont));
									responseCell.setBorderColor(BaseColor.WHITE);
									responseCell.setHorizontalAlignment(Element.ALIGN_CENTER);

									actionCell.setBorderColor(BaseColor.WHITE);
									actionCell.setHorizontalAlignment(Element.ALIGN_CENTER);

									statusCell.setBorderColor(BaseColor.WHITE);
									statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);

									remarkCell.setBorderColor(BaseColor.WHITE);
									remarkCell.setHorizontalAlignment(Element.ALIGN_CENTER);

									if (i % 2 == 0) {
										responseCell.setBackgroundColor(cellColor);
										actionCell.setBackgroundColor(cellColor);
										statusCell.setBackgroundColor(cellColor);
										remarkCell.setBackgroundColor(cellColor);

									} else {
										responseCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
										actionCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
										statusCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
										remarkCell.setBackgroundColor(BaseColor.LIGHT_GRAY);

									}
									responseCell.setBorderColor(BaseColor.WHITE);
									actionCell.setBorderColor(BaseColor.WHITE);
									statusCell.setBorderColor(BaseColor.WHITE);
									remarkCell.setBorderColor(BaseColor.WHITE);
									beginRepeatTable.addCell(responseCell);
									beginRepeatTable.addCell(actionCell);
									beginRepeatTable.addCell(statusCell);
									beginRepeatTable.addCell(remarkCell);
									hasServiceReceived = true;

								}

							}

						}
						i++;
					}

					document.add(beginRepeatTable);

				} else if (!questionModel.getControlType().equals("heading")
						&& questionModel.getChildQuestionModels() == null) {

					if (questionModel.getLabel() != "") {

						PdfPTable responseTable = new PdfPTable(2);
						float[] responseTableWidths;

						responseTableWidths = new float[] { 40f, 60f };
						responseTable.setWidths(responseTableWidths);

						responseTable.setWidthPercentage(100f);
						PdfPCell questionCell = new PdfPCell(new Paragraph(
								questionModel.getLabel() == null ? "" : questionModel.getLabel(), dataFont));
						questionCell.setBorderColor(BaseColor.WHITE);
						questionCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						if (questionModel.getDependentCondition() != null) {
							questionCell.setIndent(10);
						}
						questionCell.setPaddingTop(10f);
						questionCell.setPaddingBottom(2f);
						responseTable.addCell(questionCell);

						PdfPCell responseCell = new PdfPCell();
						Chunk responeChunk = new Chunk();
						responeChunk.append(
								questionModel.getValue() != null ? "\t" + questionModel.getValue().toString() : "");
						responeChunk.setFont(dataFont);
						Paragraph paragraph = new Paragraph();
						paragraph.add(responeChunk);
						paragraph.setSpacingAfter(5f);
						responseCell.setBorderColor(BaseColor.WHITE);
						responseCell.addElement(paragraph);
						responseCell.addElement(new DottedLineSeparator());

						responseCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						if (questionModel.getDependentCondition() != null) {
							responseCell.setIndent(10);
						}

						responseCell.setPaddingTop(questionModel.getValue() != null ? 5f : 15f);
						responseCell.setPaddingBottom(2f);

						responseTable.addCell(responseCell);

						if (formDataMainImage != null && k == 0) {

						}

						document.add(responseTable);

					}
				} else if (questionModel.getControlType().equals("heading")) {

					if (questionModel.getLabel() != "") {
						Paragraph paragraph = new Paragraph(
								questionModel.getDependentCondition() != null ? "\t \t " + questionModel.getLabel()
										: questionModel.getLabel(),
								smallBold);
						paragraph.setSpacingAfter(10);
						document.add(paragraph);
						document.add(new LineSeparator());

					}
				}
				k++;
			}
		}

		document.close();

		return outputPath;
	}

	@Override
	public DDMReportModel getDDMReportData(Principal principal) {
		List<ActionItemModel> item = new ArrayList<ActionItemModel>();
		List<AreaLevel> arealevels = areaLevelRepository.findAll();
//		Map<Integer,String> areaLevelMap = arealevels.stream().collect(Collectors.toMap(k->k.getAreaLevelId, k->k.getAreaLevelName()));
		Map<Integer, AreaLevel> areaLevelMap = new HashMap<>();
		for (AreaLevel level : arealevels) {
			areaLevelMap.put(level.getAreaLevel(), level);
		}

		int submitionId = getSubmissionId(principal);
		CollectUser user = collectUserRepository.findByUsername(principal.getName());
		List<DDMSubmitionData> allData = new ArrayList<>();
		if(user.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getRole().getRoleId()==6)
			allData.add(ddmSubmitionDataRepository.findByIsLiveTrueAndSubmitionId(submitionId));
		else
			allData = ddmSubmitionDataRepository.findByIsLiveTrue();
		
		DDMReportModel reportModel = null;
		
		if(allData != null && !(allData.size()==1 && allData.get(0)==null)) {
			reportModel = new DDMReportModel();
			int i = 1;
			for(DDMSubmitionData data : allData){
				 
				List<DDMActionItem> actionitems = data.getActionItem();
				for (DDMActionItem actionitem : actionitems) {
					ActionItemModel model = new ActionItemModel();
					model.setSlNo(i++);
					model.setFacilityType(areaLevelMap.get(actionitem.getQ3().getAreaId()).getAreaLevelName());
					model.setFacility(actionitem.getQ4().getAreaName());
					model.setActionItem(actionitem.getQ5());
					model.setStatus(actionitem.getQ6().getOptionName());
					model.setRemark(actionitem.getQ7());
					model.setDistrict(data.getQ1().getAreaName());
					reportModel.setDistrictName(user.getUserRoleFeaturePermissionMappings().get(0).getRoleFeaturePermissionScheme().getRole().getRoleId()==6?
							data.getQ1().getAreaName():"Chhattisgarh");
					
					item.add(model);
				}
				reportModel.setActionItems(item);
			}
		}
		

		return reportModel;
	}

	

}
