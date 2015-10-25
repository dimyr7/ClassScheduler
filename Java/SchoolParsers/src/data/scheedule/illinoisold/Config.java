package data.scheedule.illinoisold;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import data.scheedule.base.ConfigBase;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;
import data.scheedule.utils.XmlHelper;

public class Config extends ConfigBase {

	//////////////////////////////////////////////////////////////////////////////
	//// Members
	
	public static final String c_CoursesBaseUrl = "http://courses.illinois.edu/cis";
	public static final String c_PreviewPath = "preview/";
	public static final String c_SchedulePath = "schedule";
	public static final String c_CatalogPath = "catalog";
	public static final String c_AllPath = "/all";
	public static final String c_DepartmentListPath = "index.xml";
	
	@Override public String getSchoolAbbreviation() { return "illinois"; } 

	private WebClient _client;
	private boolean _preview;
	
	private String getTermString() {
		if (getTerm() == Term.Fall) return "Fall";
		if (getTerm() == Term.Spring) return "Spring";
		if (getTerm() == Term.Summer) return "Summer";
		return null;
	}

	public String getScheduleBaseUrl() {
		return String.format("%s/%s%s/%s/%s%s",
								c_CoursesBaseUrl, 
								_preview ? c_PreviewPath : "",
								getYear(), 
								getTermString().toLowerCase(),
								c_SchedulePath,
								getTerm() == Term.Summer ? c_AllPath : "");
	}
	
	public String getRelativeUrl(String relative) {
		return String.format("%s/%s", getScheduleBaseUrl(), relative);
	}
	
	//////////////////////////////////////////////////////////////////////////////
	//// .ctor
	public Config(Term term, String year) {
		super(term, year);
		
		_client = new WebClient();
		_client.setJavaScriptEnabled(false);
		_client.setCssEnabled(false);
		_client.setActiveXNative(false);
		_client.setThrowExceptionOnScriptError(false);
		_client.setThrowExceptionOnFailingStatusCode(false);
				
		initialize();
	}

	////////////////////////////////////////////////////////////////////////////////
	//// Helper Functions
	
	private void initialize() {
		HtmlPage page = makeRequest(c_CoursesBaseUrl);
		
		HtmlElement element = page.getElementById("navlist");
		if (element != null) {
			String lookupSemester = String.format("%s %s", getTermString(), getYear());
			
			HtmlElement currentSemesterAnchor = HtmlUnitHelper.getElementByTagName(element, "a", 0);
			if (currentSemesterAnchor != null) {
				if (lookupSemester.equalsIgnoreCase(StringUtils.clean(currentSemesterAnchor.asText()))) {
					_preview = false;
					return;
				}
			}
			
			// The others are Term YY
			List<String> nonPreviewSemesters = new ArrayList<String>();
			lookupSemester = String.format("%s %s", getTermString(), getYear().substring(2));
			HtmlSelect archivedSemesters = (HtmlSelect) element.getElementById("selectClassSchedule");
			if (archivedSemesters != null) {
				for (HtmlElement option : archivedSemesters.getChildElements()) {
					nonPreviewSemesters.add(StringUtils.clean(option.asText()));
				}
			}
						
			boolean contains = false;
			for (String nonPreviewSemester : nonPreviewSemesters) {
				if (nonPreviewSemester.equalsIgnoreCase(lookupSemester)) {
					contains = true;
					break;
				}
			}
			
			_preview = !contains;
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	//// Public Functions
	
	public Document makeRelativeRequest(String relative) {
		return XmlHelper.loadXmlDocument(getRelativeUrl(relative));
	}
	
	public HtmlPage makeRequest(String url) {
		_client.closeAllWindows();
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
