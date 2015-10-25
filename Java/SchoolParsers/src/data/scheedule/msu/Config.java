package data.scheedule.msu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.scheedule.base.ConfigBase;

public class Config extends ConfigBase {

	public static final String MainUrl = "http://schedule.msu.edu/";
	public static final String SearchUrl = "http://schedule.msu.edu/searchResults.asp";
	public static final String SearchResults = "http://schedule.msu.edu/searchResults_Print.asp?SM=%s&Ins=&STm=0600&SJ=%s&SCd=%s&pagenum=1&AR=";
	
	private WebClient _client;
	
	@Override public String getSchoolAbbreviation() { return "msu"; }
	
	
	public String getSemesterString() { return String.format("%s %s", getTerm().toString().toLowerCase(), getYear()); }
	
	private String _semesterValue;
	public String getSemesterValue() { return _semesterValue; }
	public void setSemesterValue(String value) { _semesterValue = value; }
	
	public String getDepartmentSearchUrl(String departmentCode, String departmentName) {
		return String.format(SearchResults, encode(_semesterValue), encode(departmentName), encode(departmentCode));
	}
	
	private String encode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		return str.replace(" ", "+");
	}
	
	public Config(Term term, String year) {
		super(term, year);
		
		_client = new WebClient();
		_client.setJavaScriptEnabled(false);
		_client.setCssEnabled(false);
	}
	
	public Config(Config config) {
		this(config.getTerm(), config.getYear());
		_semesterValue = config._semesterValue;
	}
	
	public HtmlPage makeRequest(String url) {
		try {
			writeDebug("Making request to %s\n", url);
			return _client.getPage(url);
		} 
		catch (FailingHttpStatusCodeException e) { e.printStackTrace(); } 
		catch (MalformedURLException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
		
		return null;
	}
	
	
	public HtmlPage makeRequest(WebRequest request) {
		try {
			writeDebug("Making %s request to %s\n", request.getHttpMethod().toString(), request.getUrl().toString());
			return _client.getPage(request);
		}
		catch (FailingHttpStatusCodeException e) { e.printStackTrace(); } 
		catch (MalformedURLException e) { e.printStackTrace(); } 
		catch (IOException e) { e.printStackTrace(); }
		
		return null;
	}	
}
