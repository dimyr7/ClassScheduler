package data.scheedule.nuold;

import java.io.IOException;
import java.net.MalformedURLException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import data.scheedule.base.ConfigBase;
import data.scheedule.utils.HtmlUnitHelper;

public class Config extends ConfigBase {
	
	public static final String c_LoginUrl = "http://www.northwestern.edu/caesar/in/guest.html";
	
	@Override public String getSchoolAbbreviation() { return "nu"; }

	private WebClient _client;	
	private HtmlPage _searchPage;
	private HtmlForm _searchForm;
	private HtmlElement _searchAction;
	private HtmlButton _searchFormSubmit;
	
	public Config(Term term, String year) {
		super(term, year);
		
		_client = new WebClient();
		_client.setActiveXNative(false);
		_client.setCssEnabled(false);
		_client.setJavaScriptEnabled(false);
		_client.setThrowExceptionOnScriptError(false);
		_client.setThrowExceptionOnFailingStatusCode(false);
		
		initialize();
	}
	
	private void initialize() {
		try {
			HtmlPage page = makeRequest(c_LoginUrl);
			
			HtmlForm form = (HtmlForm) page.getElementById("login");
			HtmlButton submitButton = (HtmlButton)page.createElement("button");
			submitButton.setAttribute("type", "submit");
			form.appendChild(submitButton);
			
			page = submitButton.click();
			
			HtmlElement container = page.getElementById("crefli_NW_SS_SA_CLASS_SEARCH");
			HtmlElement link = HtmlUnitHelper.getElementByTagName(container, "a", 0);
			
			page = link.click();
			
			HtmlElement iframe = (HtmlElement) page.getElementById("ptifrmtgtframe");
			_searchPage = makeRequest(iframe.getAttribute("src"));
			
			HtmlSelect select = (HtmlSelect)_searchPage.getElementById("CLASS_SRCH_WRK2_STRM$52$");
			for (HtmlOption op : select.getOptions()) {
				if (op.asText().equalsIgnoreCase(String.format("%s %s", getYear(), getTermValue()))) {
					op.setSelected(true);
					break;
				}
			}
			
			_searchForm = _searchPage.getFormByName("win0");
			
			_searchAction = HtmlUnitHelper.getElementByTagAndAttributeValue(_searchForm, "input", "name", "ICAction");
			_searchFormSubmit = (HtmlButton)_searchPage.createElement("button");
			_searchFormSubmit.setAttribute("type", "submit");
			_searchForm.appendChild(_searchFormSubmit);
			
			//_searchPage.getElementByName("CLASS_SRCH_WRK2_SSR_OPEN_ONLY$chk").setAttribute("value", "N");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void setSearchAction(String action) {
		_searchAction.setAttribute("value", action);
	}
	
	public HtmlPage submitSearchForm() {
		try {
			return _searchFormSubmit.click();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	private String getTermValue() { 
		if (getTerm() == Term.Spring) return "Spring";
		if (getTerm() == Term.Summer) return "Summer";
		if (getTerm() == Term.Fall)   return "Fall";
		if (getTerm() == Term.Winter) return "Winter";
		return "";
	}
	
	public HtmlPage getSearchPage() {
		return _searchPage;
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
