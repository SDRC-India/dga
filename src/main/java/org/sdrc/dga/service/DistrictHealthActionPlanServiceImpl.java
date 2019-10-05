/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 09-Sep-2019 4:25:37 PM
 */
package org.sdrc.dga.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.sdrc.dga.domain.Area;
import org.sdrc.dga.domain.DistrictHealthActionPlanAggregation;
import org.sdrc.dga.domain.DistrictHealthActionPlanAggregationDetails;
import org.sdrc.dga.domain.DistrictHealthActionPlanConfiguration;
import org.sdrc.dga.domain.DistrictHealthActionPlanFilePath;
import org.sdrc.dga.domain.TimePeriod;
import org.sdrc.dga.repository.AreaRepository;
import org.sdrc.dga.repository.DistrictHealthActionPlanAggregationDetailsRepository;
import org.sdrc.dga.repository.DistrictHealthActionPlanAggregationRepository;
import org.sdrc.dga.repository.DistrictHealthActionPlanConfigurationRepository;
import org.sdrc.dga.repository.DistrictHealthActionPlanFilePathRepository;
import org.sdrc.dga.repository.RawFormXapthsRepository;
import org.sdrc.dga.repository.TimePeriodRepository;
import org.sdrc.dga.repository.XFormRepository;
import org.sdrc.dga.util.Constants;
import org.sdrc.dga.util.FacilityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

@Service
public class DistrictHealthActionPlanServiceImpl implements DistrictHealthActionPlanService {
	
	@Autowired
	RawFormXapthsRepository rawFormXapthsRepository;
	
	@Autowired
	XFormRepository xFormRepository;
	
	@Autowired
	TimePeriodRepository timeperiodRepository;
	
	@Autowired
	DistrictHealthActionPlanConfigurationRepository dhapConfigurationRepository;
	
	@Autowired
	DistrictHealthActionPlanAggregationRepository dhapAggregationRepository;
	
	@Autowired
	DistrictHealthActionPlanAggregationDetailsRepository dhapAggregationDetailsRepository;
	
	@Autowired
	AreaRepository areaRepository;
	
	@Autowired
	private ConfigurableEnvironment configurableEnvironment;
	
	@Autowired
	DistrictHealthActionPlanFilePathRepository dhapFilePathRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(DistrictHealthActionPlanServiceImpl.class);

	@Override
	@Transactional
	public boolean insertIntoDHAPConfiguratiion() {
		
		XSSFWorkbook workbook = null;
		try(FileInputStream fis = new FileInputStream(ResourceUtils.getFile("classpath:DHAP-Configuration.xlsx"))){
			workbook = new XSSFWorkbook(fis);

			XSSFSheet sheet = null;
				
			for(int sheets = 0; sheets<workbook.getNumberOfSheets(); sheets++){
				sheet = workbook.getSheetAt(sheets);
				for (int row = 1; row <= sheet.getLastRowNum(); row++) {
					XSSFRow xssfRow = sheet.getRow(row);
					DistrictHealthActionPlanConfiguration dhapConfiguration = new DistrictHealthActionPlanConfiguration();
					// Starting cells
					Iterator<Cell> cellIterator = xssfRow.cellIterator();
					int cols = 0;
					Cell cell = null;
					
					while (cellIterator.hasNext()) {
						cell = cellIterator.next();
						switch (cols) {
						case 2:
							System.out.println(cell.getStringCellValue());
							String[] labels = cell.getStringCellValue().split("@AND@");
							String xPathIds = "";
							if(xssfRow.getCell(1).getStringCellValue().contains("calc") || xssfRow.getCell(8).getStringCellValue().equals("scoreHasId")){
								xPathIds = String.valueOf(Math.round(xssfRow.getCell(10).getNumericCellValue()));
							}else{
								for(String label : labels){
									xPathIds += rawFormXapthsRepository.findByLabelAndFormFormId(label, (int)xssfRow.getCell(9).getNumericCellValue()).getxPathId().toString()+",";
								}
								xPathIds = xPathIds.substring(0, xPathIds.length()-1);
							}
							dhapConfiguration.setRawFormXapths(xPathIds);
							break;
						case 4:
							dhapConfiguration.setSector(cell.getStringCellValue());
							break;
						case 5:
							dhapConfiguration.setIndicator(cell.getStringCellValue());
							break;
						case 6:
							if(cell.getCellType() == Cell.CELL_TYPE_STRING)
								dhapConfiguration.setStandard(cell.getStringCellValue());
							else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
								dhapConfiguration.setStandard(String.valueOf((int)cell.getNumericCellValue()));
							break;
						case 8:
							dhapConfiguration.setFormula(cell.getStringCellValue());
							break;
						case 9:
							dhapConfiguration.setForm(xFormRepository.findByFormId((int)cell.getNumericCellValue()));
							if((int)cell.getNumericCellValue() == 13)
								dhapConfiguration.setFaciltyType(FacilityType.DH);
							else if((int)cell.getNumericCellValue() == 14){
								dhapConfiguration.setFaciltyType(FacilityType.PHC);
							}else if((int)cell.getNumericCellValue() == 16){
								dhapConfiguration.setFaciltyType(FacilityType.CHC);
							}
							break;
						case 11:
							dhapConfiguration.setChoiceDetailsLabel(cell.getCellType() == Cell.CELL_TYPE_NUMERIC ? (int)cell.getNumericCellValue() : null);
							break;
						default:
							break;
						}
						cols++;
					}
//					set other properties here
					
					dhapConfiguration.setTimeperiod(timeperiodRepository.findByTimePeriodId(3));
					
					dhapConfigurationRepository.save(dhapConfiguration);
				}
		    }
			return true;
		} catch (Exception e) {
			logger.error("Error while inserting data in to DistrictHealthActionPlanConfiguration!", e.getMessage());
			return false;
		}
	}

	@Override
	@Transactional
	public boolean aggregateDistrictHealthActionPlan() {
		try{
			int[] formIds = {13,14,16};
			for(int formId : formIds){
				List<DistrictHealthActionPlanConfiguration> dhapcs = dhapConfigurationRepository.findByFormFormIdAndTimeperiodTimePeriodId(formId, 3);
				for(DistrictHealthActionPlanConfiguration dhapc : dhapcs){
					if(dhapc.getStandard()!=null && !dhapc.getStandard().isEmpty() && dhapc.getFormula()!=null && !dhapc.getFormula().isEmpty()){
						DistrictHealthActionPlanAggregation dhapAggregation = new DistrictHealthActionPlanAggregation();
						List<DistrictHealthActionPlanAggregationDetails> dhapAggDetails = new ArrayList<>();
						
						dhapAggregation.setDhapConfiguration(dhapc);
						dhapAggregation.setFaciltyType(dhapc.getFaciltyType());
						dhapAggregation.setIndicatorName(dhapc.getIndicator());
						dhapAggregation.setRequiredUnit(dhapc.getStandard());
						dhapAggregation.setSector(dhapc.getSector());
						dhapAggregation.setTimeperiod(dhapc.getTimeperiod());//needs to be changed from parameter.
						dhapAggregation = dhapAggregationRepository.save(dhapAggregation);
		
						System.out.println(dhapc.getStandard()+":"+ dhapc.getForm().getFormId()+":"+
								  dhapc.getTimeperiod().getTimePeriodId()+":"+ dhapc.getRawFormXapths()+":"+ dhapc.getFormula());
						List<Integer> xPathList = new ArrayList<>();
						for(String xPath : dhapc.getRawFormXapths().split(",")){
							xPathList.add(Integer.parseInt(xPath));
						}
						
						List<Object[]> allAggregatedData = new ArrayList<>();
						if(dhapc.getFormula().equals("sumOfMultiple")){
							allAggregatedData = dhapAggregationRepository.getAggregatedDataForSumOfMultiple(dhapc.getStandard(),
									dhapc.getForm().getFormId(), dhapc.getTimeperiod().getTimePeriodId(),xPathList);
						}else{
							allAggregatedData = dhapAggregationRepository.getAggregatedData(dhapc.getStandard(),
									dhapc.getForm().getFormId(), dhapc.getTimeperiod().getTimePeriodId(),
									Integer.parseInt(dhapc.getRawFormXapths()), dhapc.getFormula());
						}
						
						
						for(Object[] aggregatedData : allAggregatedData){
							DistrictHealthActionPlanAggregationDetails dhapAggDetail = new DistrictHealthActionPlanAggregationDetails();
							
							dhapAggDetail.setDistrict(formId!=13?aggregatedData[0].toString():aggregatedData[1].toString());
							dhapAggDetail.setBlock(formId!=13?aggregatedData[1].toString():"");
							dhapAggDetail.setFacility(aggregatedData[2].toString());
							if(!dhapc.getFormula().equals("scoreHasId")){
								dhapAggDetail.setUnit(Integer.parseInt(aggregatedData[4].toString()));
							}else{
								List<String> resList = Arrays.asList(aggregatedData[3].toString().split(" "));
								if(!resList.contains(dhapc.getChoiceDetailsLabel().toString())){
									dhapAggDetail.setUnit(Integer.parseInt(aggregatedData[4].toString()));
								}else{
									continue;
								}
							}
							dhapAggDetail.setDhapAggregation(dhapAggregation);
							
							dhapAggDetails.add(dhapAggDetail);
						}
						
						dhapAggregationDetailsRepository.save(dhapAggDetails);
					}
				}
			}
			
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			logger.error("Critical:: error while aggregating district health action plan!!",e.getLocalizedMessage());
			return false;
		}
	}

	@Override
	public boolean exportDistrictHealthActionPlanExcel(int timeperiodId, int districtId) {
		
		List<DistrictHealthActionPlanConfiguration> dhapConfigurations = dhapConfigurationRepository.findByTimeperiodTimePeriodId(timeperiodId);
		
		List<DistrictHealthActionPlanAggregation> dhapAggregations = dhapAggregationRepository.findByDhapConfigurationInOrderBySector(dhapConfigurations);
		Map<FacilityType, List<DistrictHealthActionPlanAggregation>> dhapAggregationsFacilityTypeAsKey = dhapAggregations.stream().collect(Collectors.groupingBy(v->v.getFaciltyType()));
		
		
		try (Workbook workbook = new XSSFWorkbook()){
			for(Map.Entry<FacilityType, List<DistrictHealthActionPlanAggregation>> facilityEntry : dhapAggregationsFacilityTypeAsKey.entrySet()){
				Sheet sheet = workbook.createSheet(facilityEntry.getKey().toString());
				Map<String, List<DistrictHealthActionPlanAggregation>> dhapAggregationsSecorAsKey = facilityEntry.getValue().stream().collect(Collectors.groupingBy(v->v.getSector()));
				//sector font
				Font sectorFont = workbook.createFont();
		        sectorFont.setBold(true);
		        sectorFont.setFontHeightInPoints((short) 11);
		        sectorFont.setColor(IndexedColors.BLACK.getIndex());
		        // Create a CellStyle with the sector font
		        CellStyle sectorCellStyle = workbook.createCellStyle();
		        sectorCellStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
		        sectorCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		        sectorCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		        sectorCellStyle.setFont(sectorFont);
		        
				//indicator font
				Font indicatorFont = workbook.createFont();
				indicatorFont.setBold(true);
				indicatorFont.setFontHeightInPoints((short) 10);
				indicatorFont.setColor(IndexedColors.BLACK.getIndex());
		        // Create a CellStyle with the indicator font
		        CellStyle indicatorCellStyle = workbook.createCellStyle();
		        indicatorCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		        indicatorCellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		        indicatorCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		        indicatorCellStyle.setFont(indicatorFont);
		        
		        int sectorRowIndex = 0;
		        int indicatorRowIndex = 0;
		        int budgetValueRowIndex=0;
				for (Map.Entry<String, List<DistrictHealthActionPlanAggregation>> entry : dhapAggregationsSecorAsKey.entrySet()) {
			        //print sector
					Row sectorRow = sheet.createRow(sectorRowIndex);
					Cell sectorCell = sectorRow.createCell(0);
					sectorCell.setCellValue("Sector: "+entry.getKey());
					sheet.addMergedRegion(new CellRangeAddress(sectorRowIndex, sectorRowIndex, 0, 3));
					sectorCell.setCellStyle(sectorCellStyle);
					indicatorRowIndex = sectorRowIndex+2;
			        for(DistrictHealthActionPlanAggregation dhapAggregation : entry.getValue()){
			        	if(dhapAggregationDetailsRepository.findByDhapAggregationIdAndDistrict(dhapAggregation.getId(), areaRepository.findByAreaId(districtId).getAreaName()).isEmpty())
			        		continue;
			        	//print indicator name
			        	Row indicatorRow = sheet.createRow(indicatorRowIndex);
			        	Cell indicatorCell = indicatorRow.createCell(0);
			        	indicatorCell.setCellValue("Indicator: "+dhapAggregation.getIndicatorName());
			        	sheet.addMergedRegion(new CellRangeAddress(indicatorRowIndex, indicatorRowIndex, 0, 3));
			        	indicatorCell.setCellStyle(indicatorCellStyle);
			        	sheet.autoSizeColumn(indicatorCell.getColumnIndex());
			        	//add table header
			        	Row tableHeaderRow = sheet.createRow(indicatorRowIndex+1);
			        	Cell slNoCell = tableHeaderRow.createCell(0);
			        	slNoCell.setCellValue("Sl No");
			        	sheet.autoSizeColumn(slNoCell.getColumnIndex());
			        	
			        	Cell unitCell = tableHeaderRow.createCell(1);
			        	unitCell.setCellValue("Unit");
			        	sheet.autoSizeColumn(unitCell.getColumnIndex());
			        	
			        	Cell facilityCell = tableHeaderRow.createCell(2);
			        	facilityCell.setCellValue("Name of Facility");
			        	sheet.autoSizeColumn(facilityCell.getColumnIndex());
			        	
			        	Cell blockCell = tableHeaderRow.createCell(3);
			        	blockCell.setCellValue("Name of Block");
			        	sheet.autoSizeColumn(blockCell.getColumnIndex());
			        	
			        	//add budget section
			        	budgetValueRowIndex = indicatorRowIndex+2;
			        	Cell budgetCell = indicatorRow.createCell(5);
			        	budgetCell.setCellValue("Budget");
			        	sheet.addMergedRegion(new CellRangeAddress(indicatorRowIndex, indicatorRowIndex, 5, 9));
			        	budgetCell.setCellStyle(indicatorCellStyle);
			        	sheet.autoSizeColumn(budgetCell.getColumnIndex());
			        	//add budget table header
			        	Cell budgetSlNoCell = tableHeaderRow.createCell(5);
			        	budgetSlNoCell.setCellValue("Sl No");
			        	sheet.autoSizeColumn(budgetSlNoCell.getColumnIndex());
			        	
			        	Cell budgetParticularCell = tableHeaderRow.createCell(6);
			        	budgetParticularCell.setCellValue("Particular");
			        	sheet.autoSizeColumn(budgetParticularCell.getColumnIndex());
			        	
			        	Cell budgetNumberRequiredCell = tableHeaderRow.createCell(7);
			        	budgetNumberRequiredCell.setCellValue("Number Required");
			        	sheet.autoSizeColumn(budgetNumberRequiredCell.getColumnIndex());
			        	
			        	Cell budgetUnitCostCell = tableHeaderRow.createCell(8);
			        	budgetUnitCostCell.setCellValue("Unit Cost");
			        	sheet.autoSizeColumn(budgetUnitCostCell.getColumnIndex());
			        	
			        	Cell budgetTotalCostCell = tableHeaderRow.createCell(9);
			        	budgetTotalCostCell.setCellValue("Total Cost");
			        	sheet.autoSizeColumn(budgetTotalCostCell.getColumnIndex());
			        			
			        	List<DistrictHealthActionPlanAggregationDetails> dhapAggregationDetails = dhapAggregationDetailsRepository.findByDhapAggregationIdAndDistrict(dhapAggregation.getId(), areaRepository.findByAreaId(districtId).getAreaName());
			        	int totalGap=0;
			        	for(int i=0; i<dhapAggregationDetails.size(); i++){
			        		//print details
			        		sectorRowIndex++;
			        		indicatorRowIndex++;
			        		
			        		Row valueRow = sheet.createRow(indicatorRowIndex+1);
				        	Cell slNoValueCell = valueRow.createCell(0);
				        	slNoValueCell.setCellValue(i+1);
				        	
				        	Cell unitValueCell = valueRow.createCell(1);
				        	unitValueCell.setCellValue(dhapAggregationDetails.get(i).getUnit());
				        	
				        	Cell facilityValueCell = valueRow.createCell(2);
				        	facilityValueCell.setCellValue(dhapAggregationDetails.get(i).getFacility());
				        	
				        	Cell blockValueCell = valueRow.createCell(3);
				        	blockValueCell.setCellValue(dhapAggregationDetails.get(i).getBlock());
				        	
				        	totalGap+=dhapAggregationDetails.get(i).getUnit();
			        	}
			        	
			        	//set budget value here
			        	Row budgetDataRow = sheet.getRow(budgetValueRowIndex);
			        	
			        	if(budgetDataRow==null){
			        		budgetDataRow = sheet.createRow(budgetValueRowIndex);
			        	}
			        	Cell budgetSlNoValueCell = budgetDataRow.createCell(5);
			        	budgetSlNoValueCell.setCellValue(1);
			        	sheet.autoSizeColumn(budgetSlNoValueCell.getColumnIndex());
			        	
			        	Cell budgetParticularValueCell = budgetDataRow.createCell(6);
			        	budgetParticularValueCell.setCellValue(dhapAggregation.getIndicatorName());
			        	sheet.autoSizeColumn(budgetParticularValueCell.getColumnIndex());
			        	
			        	Cell budgetNumberRequiredValueCell = budgetDataRow.createCell(7);
			        	budgetNumberRequiredValueCell.setCellValue(totalGap);
			        	sheet.autoSizeColumn(budgetNumberRequiredValueCell.getColumnIndex());
			        	
			        	Cell budgetUnitCostValueCell = budgetDataRow.createCell(8);
			        	budgetUnitCostValueCell.setCellValue(0);
			        	int budgetValueRowFormulaIndex = budgetValueRowIndex+1;
			        	Cell budgetTotalCostValueCell = budgetDataRow.createCell(9);
			        	String totalCostFormula= "PRODUCT(H"+budgetValueRowFormulaIndex+":I"+budgetValueRowFormulaIndex+")";
			        	budgetTotalCostValueCell.setCellFormula(totalCostFormula);
			        	
			        	sectorRowIndex+=3;
			        	indicatorRowIndex+=3;
			        }
			        sectorRowIndex++;
			    }
		       
			}
			// Write the output to a file
			String name = areaRepository.findByAreaId(districtId).getAreaName()+"_"+timeperiodRepository.findByTimePeriodId(timeperiodId).getTimeperiod() + ".xlsx";
			String filePath = Paths.get(configurableEnvironment.getProperty(Constants.DGA_DISTRICT_HEALTH_ACTION_PLAN_PATH)).toAbsolutePath()+File.separator+name;
	        FileOutputStream fileOut = new FileOutputStream(new File(filePath));
	        workbook.write(fileOut);
	        fileOut.close();
	        //insert the filePath in database
	        
	        DistrictHealthActionPlanFilePath dhapFilePath = new DistrictHealthActionPlanFilePath();
	        
	        dhapFilePath.setArea(areaRepository.findByAreaId(districtId));
	        dhapFilePath.setTimePeriod(timeperiodRepository.findByTimePeriodId(timeperiodId));
	        dhapFilePath.setFilePath(filePath);
	        dhapFilePathRepository.save(dhapFilePath);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Critical:: error while generating excel for district health action plan", e.getMessage());
			return false;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject exportDHAPExcel(int timeperiodId, int districtId) {
		TimePeriod tp = timeperiodRepository.findByTimePeriodId(timeperiodId);
		Area area = areaRepository.findByAreaId(districtId);
		DistrictHealthActionPlanFilePath dhapFilePath = dhapFilePathRepository.findByAreaAndTimePeriod(area,tp);
		
		JSONObject obj = new JSONObject();
		obj.put("statusCode", 200);
		obj.put("filePath", dhapFilePath.getFilePath());
		return obj;
	}

}
