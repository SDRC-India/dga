/**
 * @author Pratyush(pratyush@sdrc.co.in)
 * Created on: 09-Sep-2019 4:24:10 PM
 */
package org.sdrc.dga.service;

import org.json.simple.JSONObject;

public interface DistrictHealthActionPlanService {

	boolean insertIntoDHAPConfiguratiion();

	boolean aggregateDistrictHealthActionPlan();

	boolean exportDistrictHealthActionPlanExcel(int timeperiodId, int districtId);

	JSONObject exportDHAPExcel(int timeperiodId, int districtId);

}
