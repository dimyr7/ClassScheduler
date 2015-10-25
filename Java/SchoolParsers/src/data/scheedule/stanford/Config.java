package data.scheedule.stanford;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.scheedule.base.ConfigBase;

public class Config extends ConfigBase {
	
	public static final String c_CourseSearchUrl = "http://explorecourses.stanford.edu/CourseSearch/";

	@Override public String getSchoolAbbreviation() { return "stanford"; }

	private WebClient _client;
	
	public Config(Term term, String year) {
		super(term, year);
		
		_client = new WebClient();
		_client.setJavaScriptEnabled(false);
		_client.setCssEnabled(false);
		_client.setThrowExceptionOnScriptError(false);
		_client.setThrowExceptionOnFailingStatusCode(false);
	}
	
	public String getTermFilter() { return String.format("&filter-term-%s=on", getTermString()); }
	public String getTermString() {
		if (getTerm() == Term.Autumn) return "Autumn";
		if (getTerm() == Term.Winter) return "Winter";
		if (getTerm() == Term.Spring) return "Spring";
		if (getTerm() == Term.Summer) return "Summer";
		return null;
	}
	
	public String getUrl(String relativePath, boolean withTermFilter) {
		return String.format("%s%s%s", c_CourseSearchUrl, relativePath, withTermFilter ? getTermFilter() : "");
	}
	
	public HtmlPage makeRequest(String url) {
		try {
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
}
