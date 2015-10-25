package data.scheedule.stanford;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;

public class Department extends DepartmentBase {
	
	///////////////////////////////////////////////////////////////
	//// Members
	
	private String _departmentName;
	private String _departmentCode;
	private List<Course> _courses = null;
	private boolean _isValid;
	
	@Override public List<CourseBase> getCourses() { return ArrayUtils.castAll(_courses, CourseBase.class); }
	@Override public String getDepartmentCode() { return _departmentCode; }
	@Override public String getDepartmentName() { return _departmentName; }
	
	public Config getConfig() { return (Config)getBaseConfig(); }
	
	public boolean isValid() { return _isValid; }
	
	////////////////////////////////////////////////////////////////
	//// .ctor
	
	public Department(Config config, String departmentCode, String departmentName) {
		super(config);
		_departmentCode = departmentCode;
		_departmentName = departmentName;
		_isValid = true;
		_courses = new ArrayList<Course>();
	}
	public Department(Config config, HtmlAnchor link) {
		super(config);
		
		_courses = new ArrayList<Course>();
		parseLink(link);
	}
	
	/////////////////////////////////////////////////////////////////
	//// Functions
	
	private void parseLink(HtmlAnchor link) {
		_isValid = false;
		String linkText = link.asText();
		
		int start = linkText.indexOf("(");
		int end = linkText.indexOf(")");
		
		if (start < 0 || end < 0 || start >= linkText.length() || end >= linkText.length()) return;
		
		_departmentCode = StringUtils.clean(linkText.substring(start + 1, end));
		_departmentName = StringUtils.clean(linkText.substring(0, start));
		
		// Invalid departments, any links with () in them that aren't for departments in the main department listing.
		// e.g.: Service Learning Courses (certified by Haas Center)
		if (_departmentCode.equalsIgnoreCase("certified by Haas Center")) return;
		
		_isValid = true;
	}
	
	@Override
	public void loadCourses() {
		loadCoursesFor(0);		
	}
	
	private void loadCoursesFor(int pageNumber) {
		String url = getCourseUrl(pageNumber);
		getConfig().writeDebug("Attempting to load page %d for department %s : %s\n", pageNumber, _departmentCode, url);
		HtmlPage page = getConfig().makeRequest(url);
		
		HtmlElement searchResults = page.getElementById("searchResults");
		for (HtmlElement searchResult : searchResults.getChildElements()) {
			Course course = new Course(getConfig(), _departmentCode, searchResult);
			if (course.isValid()) {
				_courses.add(course);
			}
			else {
				getConfig().writeDebug("Unable to parse course in department %s.", _departmentCode);
			}
		}
		
		// Recurse on the next page.
		HtmlElement pagination = page.getElementById("pagination");
		if (pagination == null || HtmlUnitHelper.getElementByText(pagination, "next »") != null) {
			loadCoursesFor(pageNumber + 1);
		}
	}
	
	
	
	private String getCourseUrl(int pageNumber) {
		//http://explorecourses.stanford.edu/CourseSearch/search?q=CS&view=catalog&page=0&catalog=&filter-term-Autumn=on&filter-term-Summer=on&collapse=&filter-coursestatus-Active=on&filter-catalognumber-CS=on&filter-catalognumber-CS=on
		return String.format("%ssearch?q=%s&filter-coursestatus-Active=on&view=catalog&page=%d&filter-term-%s=on&filter-catalognumber-%s=on", 
								getConfig().c_CourseSearchUrl, 
								StringUtils.urlEncode(_departmentCode), 
								pageNumber,
								StringUtils.urlEncode(getConfig().getTermString()),
								StringUtils.urlEncode(_departmentCode));
		
	}
	
	

}
