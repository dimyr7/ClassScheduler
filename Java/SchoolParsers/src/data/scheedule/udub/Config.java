package data.scheedule.udub;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.scheedule.base.ConfigBase;

public class Config extends ConfigBase {

	@Override public String getSchoolAbbreviation() { return "udub"; }

	private WebClient _client;
	
	public static final String BaseUrl = "http://www.washington.edu/students/";
	public static final String TimeSchedule = "timeschd/";
	public static final String CourseCatalog = "crscat/";
	
	public Config(Term term, String year) {
		super(term, year);
		
		_client = new WebClient();
		_client.setJavaScriptEnabled(false);
		_client.setCssEnabled(false);
	}
	
	private String getTermPath() {
		if (getTerm() == Term.Spring)	return "SPR";
		if (getTerm() == Term.Summer)	return "SUM";
		if (getTerm() == Term.Autumn)	return "AUT";
		if (getTerm() == Term.Winter)	return "WIN";
		return "";
	}
	
	public String getBaseCatalogUrl() {
		return String.format("%s%s", BaseUrl, CourseCatalog);
	}
	
	public String getBaseScheduleUrl() {
		return String.format("%s%s%s%s", BaseUrl, TimeSchedule, getTermPath(), getYear()); 
	}
	
	public String getRelativeScheduleUrl(String relativePath) {
		return String.format("%s/%s", getBaseScheduleUrl(), relativePath);
	}
	
	public String getRelativeCatalogUrl(String relativePath) {
		return String.format("%s/%s", getBaseCatalogUrl(), relativePath);
	}
	
	public HtmlPage makeRequest(String url) {
		try {
			//System.out.printf("Attempting request to %s\n", url);
			return _client.getPage(url);
		} 
		catch (FailingHttpStatusCodeException e) { } 
		catch (MalformedURLException e) { } 
		catch (IOException e) { }
		
		return null;
	}
}
