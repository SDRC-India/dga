package org.sdrc.dga.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.dga.model.LaqshyaDatas;
import org.sdrc.dga.model.ResponseModel;
import org.sdrc.dga.service.LaqshyaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LaqshyaController {

	@Autowired
	LaqshyaService laqshyaService;
	
	@GetMapping("getLaqshyaData")
	public ResponseEntity<LaqshyaDatas> getLaqshyaData( Principal auth) {
		try {
			return ResponseEntity.status(HttpStatus.OK)
					.body(laqshyaService.getLaqshyaData(auth));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

	}
	
//	@PreAuthorize("hasAuthority('dataentry_HAVING_write')")
	@PostMapping("saveLaqshyaData")
	public ResponseEntity<ResponseModel> saveLaqshyaData(@RequestBody LaqshyaDatas laqshyaModel,
			Principal auth) {
		try {

			return ResponseEntity.status(HttpStatus.OK)
					.body(laqshyaService.saveLaqshyaData(laqshyaModel, auth));

		} catch (Exception e) {
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@GetMapping("getLaqshyaReport")
	@ResponseBody
	Map<String,String> getLaqshyaReport(Principal auth) throws IOException {
		try {
		String fileName =  laqshyaService.getLaqshyaReport(auth);
		Map<String,String> map = new HashMap<String,String>();
		map.put("statusCode", "200");
		map.put("File",fileName);
		return map;
		}catch(Exception e){
			e.printStackTrace();
			Map<String,String> map = new HashMap<String,String>();
			map.put("statusCode", "500");
			map.put("File","");
			return map;
		}
	}
	
	@PostMapping("database/downloadLaqshya")
	public void downLoad(@RequestParam("fileName") String name, HttpServletResponse response) throws IOException {

		String fileName = name.replaceAll("%3A", ":").replaceAll("%2F", "/").replaceAll("%2C", ",")
				.replaceAll("\\+", " ").replaceAll("%20", " ").replaceAll("%26", "&").replaceAll("%5C", "/");
		try(InputStream inputStream = new FileInputStream(fileName)) {
			
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", new java.io.File(fileName).getName());
			response.setHeader(headerKey, headerValue);
			response.setContentType("application/octet-stream");
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			outputStream.flush();
			Files.delete(Paths.get(name));
			
		} catch (IOException e) {
			e.printStackTrace();
//			log.error("error-while downloading raw data report with payload : {}", name, e);
		}
	}
	
	@GetMapping("database/configureLaqshyaFacility")
	private boolean configureLaqshyaFacility() {
		try {
		return laqshyaService.configureLaqshyaFacility();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
