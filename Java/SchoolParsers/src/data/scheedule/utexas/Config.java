package data.scheedule.utexas;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.scheedule.base.ConfigBase;

public class Config extends ConfigBase {
	
	public static final String BaseUrl = "https://utdirect.utexas.edu";
	public static final String IndexPath = "registrar/nrclav/index.WBX?s_ccyys=";
	
	public static final String LoginName = "mb43765";
	public static final String LoginPassword = "ButtMudd33";
	
	@Override public String getSchoolAbbreviation() { return "utexas"; }
	
	private WebClient _client;
		
	private String getTermString() {
		if (getTerm() == Term.Spring) return "2";
		if (getTerm() == Term.Summer) return "6";
		if (getTerm() == Term.Fall) return "9"; 
		return "";
	}
	public String getSemester() { return getYear() + getTermString(); }
	public String getIndexUrl() { return createAbsoluteUrl(IndexPath) + getSemester(); }
	public String createAbsoluteUrl(String path) { return BaseUrl + (!path.startsWith("/") ? "/" + path : path); }
	
	public Config(Term term, String year) {
		super(term, year);
				
		_client = new WebClient();
		_client.setJavaScriptEnabled(false);
		_client.setCssEnabled(false);
		
		attemptLogin();
	}
		
	public HtmlPage makeAuthenticatedRequest(String uri) throws Exception {
		//System.out.println("Making request: " + uri);
		return _client.getPage(uri);
	}
	
	public HtmlPage getIndexPage() throws Exception {
		return makeAuthenticatedRequest(getIndexUrl());
	}
		
	private void attemptLogin() {
		
		try {
			_client.setAlertHandler(new CollectingAlertHandler());
			
			HtmlPage loginPage = _client.getPage(getIndexUrl());
			HtmlForm loginForm = loginPage.getFormByName("logonform");
			loginForm.getInputByName("LOGON").setValueAttribute(LoginName);
			loginForm.getInputsByName("PASSWORDS").get(1).setValueAttribute(LoginPassword);
			loginForm.getInputByValue("Log In").click();
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
