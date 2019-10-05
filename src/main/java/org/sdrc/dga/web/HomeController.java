package org.sdrc.dga.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.sdrc.dga.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

	
	private final Path logFileLocation = Paths.get("/opt/dgacg/logs/");
	
//	@GetMapping(value = {"/","home"})
//	public String home() {
//		return "home";
//	}
//	
//	@GetMapping("aboutUs")
//	public String aboutUs() {
//		return "aboutUs";
//	}
//	
//	@GetMapping("contactUS")
//	public String contactUS() {
//		return "contactUS";
//	}
//	
//	@GetMapping("sitemap")
//	public String sitemap() {
//		return "sitemap";
//	}
//	
//	
//	@GetMapping("termsofUse")
//	public String termsofUse() {
//		return "termsofUse";
//	}
//	
//	@GetMapping("privacyPolicy")
//	public String privacyPolicy() {
//		return "privacyPolicy";
//	}
//	
//	@GetMapping("disclaimer")
//	public String disclaimer() {
//		return "disclaimer";
//	}
//	
//	@GetMapping("exception")
//	public String exception() {
//		return "error";
//	}

	@GetMapping("getLogFile")
	public  void downloadFile(@RequestParam(value="date",required=false)String date,
			@RequestParam(value="index",required=false)Integer index,HttpServletResponse response)
	throws Exception {
		System.out.println(this.logFileLocation.toAbsolutePath().toString());
		InputStream inputStream;
		String logFileName=null;
		if(date==null)
		{
			logFileName="/"+Constants.LOG_FILE+".log";
		}
		else
		{
			logFileName="/"+Constants.LOG_FILE_ARCHIVED+"/"+Constants.LOG_FILE+"."+date+"."+index+".log";
		}
		try {
			inputStream = new FileInputStream(this.logFileLocation.toAbsolutePath().toString()+logFileName);
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					new java.io.File(logFileName).getName());
			response.setHeader(headerKey, headerValue);
			response.setContentType("application/octet-stream"); //for all file type
			ServletOutputStream outputStream = response.getOutputStream();
			FileCopyUtils.copy(inputStream, outputStream);
			outputStream.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
