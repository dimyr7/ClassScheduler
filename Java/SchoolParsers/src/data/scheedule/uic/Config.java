package data.scheedule.uic;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.scheedule.base.ConfigBase;

public class Config extends ConfigBase {
		
	public static final String BaseUrl = "http://ossswebcs.admin.uillinois.edu";
	public static final String ClassUrl = BaseUrl + "/class_schedule/";
	public static final String SearchUrl = ClassUrl + "searchdeptview.asp";
	public static final String DepartmentUrl = ClassUrl + "getsubject.asp?q1=";	
	public static final String CourseUrl = ClassUrl + "classscheduledisplaynew.asp";
	
	public static final String getDepartmentUrl(String semesterValue) { return DepartmentUrl + semesterValue; }
	
	private WebClient _client;
	
	@Override public String getSchoolAbbreviation() { return "uic"; }
	
	public Config(Term term, String year) {
		super(term, year);
		_client = new WebClient();
		_client.setJavaScriptEnabled(false);
	}
	
	public HtmlPage getPage(String url) { 
		try {
			//System.out.printf("Making request to: %s\n", url);
			return _client.getPage(url);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public HtmlPage getPage(WebRequest request) { 
		try {
			//System.out.printf("Making %s request to: %s\n", request.getHttpMethod(), request.getUrl());
			return _client.getPage(request);
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
