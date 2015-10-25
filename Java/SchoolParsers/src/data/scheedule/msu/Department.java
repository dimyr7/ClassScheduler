package data.scheedule.msu;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringBuilder;
import data.scheedule.utils.StringUtils;

public class Department extends DepartmentBase {
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	private Map<String, Course> _courses;
	
	private String _departmentCode;
	private String _departmentName;

	@Override public List<CourseBase> getCourses() { return ArrayUtils.castAll(_courses.values(), CourseBase.class); }
	@Override public String getDepartmentCode() { return _departmentCode; }
	@Override public String getDepartmentName() { return _departmentName; }
	
	public Department(Config config, HtmlOption option) {
		super(config);
		_courses = new HashMap<String, Course>();
		
		parseDepartmentInfo(option);
	}
	
	public void setConfig(Config config) { super.setConfig(config); }

	@Override
	public void loadCourses() {
		HtmlPage page = getConfig().makeRequest(getSearchRequest());
		
		if (page == null) {
			System.out.printf("Failed to load courses for Department %s\n", _departmentCode);
			return;
		}
		
		HtmlElement content = page.getElementById("content");
		List<HtmlElement> childTables = HtmlUnitHelper.getElementsByTagAndAttributeValue(content, "table", "summary", "subject");
		// skip the first 2 since they are the just info tables
		for (int i=2; i<childTables.size(); i++) { 
			parseCourseTable((HtmlTable) childTables.get(i));
		}
	}
	
	public void clearCourses() {
		_courses.clear();
	}
	
	private void parseCourseTable(HtmlTable courseTable) {
		HtmlTableRow headerRow = courseTable.getRow(0);
		List<HtmlTableRow> sectionRows = new ArrayList<HtmlTableRow>();
		for (int i=1; i<courseTable.getRowCount(); i++) {
			HtmlTableRow row = courseTable.getRow(i);
			if (row.hasAttribute("class") && row.getAttribute("class").equalsIgnoreCase("row_Section")) {
				sectionRows.add(row);
			}
		}
				
		if (headerRow != null && sectionRows.size() > 0) {
			Course course = new Course(getConfig(), headerRow, sectionRows, _departmentCode);
			_courses.put(course.getCourseNumber(), course);
		}
	}
	
	private WebRequest getSearchRequest() {
		try {
			WebRequest req = new WebRequest(new URL(Config.SearchUrl), HttpMethod.POST);
			req.setRequestBody(getPostData());
			return req;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getPostData() {
		StringBuilder sb = new StringBuilder();
				
		sb.appendf("Semester=%s&", getConfig().getSemesterValue().replace(" ", "+"))
		  .append("POST=Y&Button=&Online=&")
		  .appendf("Subject=%s&", _departmentCode)
		  .append("CourseNumber=*&Instructor=ANY&")
		  .append("StartTime=0600&EndTime=2350&")
		  .append("OnBeforeDate=&OnAfterDate=&")
		  .append("Sunday=Su&Monday=M&Tuesday=Tu&Wednesday=W&Thursday=Th&Friday=F&Saturday=Sa&")
		  .append("OnCampus=Y&OffCampus=Y&OnlineCourses=Y&StudyAbroad=Y&MSUDubai=Y&")
		  .append("OpenCourses=A&AllOnePage=Y&Submit=Search+for+Courses");
		
		return sb.toString();
	}
	
	private void parseDepartmentInfo(HtmlOption option) {
		_departmentCode = StringUtils.clean(option.getValueAttribute());
		_departmentName = StringUtils.clean(option.asText().replace(_departmentCode + ": ", ""));
		
		getConfig().writeDebug("Found Department: %s - %s\n", _departmentCode, _departmentName);
	}
}
