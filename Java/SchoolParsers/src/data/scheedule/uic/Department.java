package data.scheedule.uic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

public class Department extends DepartmentBase {
		
	private HtmlOption _semesterOption;
	private HtmlOption _departmentOption;
	
	private String _departmentCode;
	private String _departmentName;
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	@Override public String getDepartmentCode() { return _departmentCode; }
	@Override public String getDepartmentName() { return _departmentName; }
	
	@Override 
	public List<CourseBase> getCourses() {
		return ArrayUtils.castAll(_courses.values(), CourseBase.class);
	}
	
	private Map<String, Course> _courses;
	
	public Department(Config config, HtmlOption semesterOption, HtmlOption departmentOption) {
		super(config);
		_semesterOption = semesterOption;
		_departmentOption = departmentOption;
		
		parseDepartmentInfo();
	}
	
	private void parseDepartmentInfo() {
		_departmentCode = StringUtils.clean(_departmentOption.getValueAttribute());
		_departmentName = StringUtils.clean(_departmentOption.asText().replace(_departmentCode + " - ", ""));
		
		//System.out.printf("Found Department: %s - %s.\n", _departmentCode, _departmentName);
	}
	
	@Override
	public void loadCourses() {
		_courses = new HashMap<String, Course>();
		
		HtmlPage page = getConfig().getPage(getCourseWebRequest());
		HtmlTable table = (HtmlTable) page.getElementById("mytable");
		
		boolean first = true;
		HtmlTableRow headerRow = null;
		List<HtmlTableRow> sectionRows = new ArrayList<HtmlTableRow>();
		for (HtmlTableRow row : table.getRows()) {
			if (!first) {
				if (row.getCells().size() == 1  && row.asText().contains(Course.HeaderCourseInfo)) {
					if (headerRow != null) {
						Course newCourse = new Course(getConfig(), _departmentCode, headerRow, sectionRows);
						if (newCourse.isValid()) {
							_courses.put(newCourse.getCourseNumber(), newCourse);
						}
					}
					
					headerRow = row;
					sectionRows = new ArrayList<HtmlTableRow>();
				}
				else if (row.getCells().size() != 1) {
					sectionRows.add(row);
				}
			}
			first = false;
		}
	}

	private WebRequest getCourseWebRequest() {
		try {
			WebRequest request = new WebRequest(new URL(Config.CourseUrl), HttpMethod.POST);
			request.setRequestBody(getPostData());
			return request;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String getPostData() {
		return String.format("sec_sel=ALL&dfcoursecall=D&num=ALL&pterm=ALL&deptdisp=YES&termvalue=%s&subjvalue=%s", _semesterOption.getValueAttribute(), _departmentCode);
	}
	
	
}
