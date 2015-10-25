package data.scheedule.nuold;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.utils.ArrayUtils;

public class Department extends DepartmentBase {
	
	private Map<String, Course> _courses;
	
	private String _departmentCode;
	private String _departmentName;
	
	@Override public String getDepartmentCode() { return _departmentCode; }
	@Override public String getDepartmentName() { return _departmentName; }

	@Override public List<CourseBase> getCourses() { return ArrayUtils.castAll(_courses.values(), CourseBase.class); }

	private Config getConfig() { return (Config) getBaseConfig(); }
	
	public Department(Config config, String departmentCode, String departmentName) {
		super(config);
		
		_departmentCode = departmentCode;
		_departmentName = departmentName;
		_courses = new HashMap<String, Course>();
	}

	@Override
	public void loadCourses() {
		HtmlPage page = getConfig().getSearchPage();
		HtmlElement input = page.getElementById("CLASS_SRCH_WRK2_SUBJECT$69$");
		input.setAttribute("value", _departmentCode);
	
		getConfig().setSearchAction("CLASS_SRCH_WRK2_SSR_PB_CLASS_SRCH");
		page = getConfig().submitSearchForm();
		
		HtmlTable tbl = (HtmlTable) page.getElementById("ACE_$ICField100$0");
		
		if (tbl == null) {
			System.out.printf("Unable to find course table for %s\n", _departmentCode);
			return;
		}
		
		HtmlTableRow headerRow = null;
		
		for (HtmlTableRow row : tbl.getRows()) {
			if (row.asText().trim().length() > 0) {
				if (headerRow != null) {
					Course c = new Course(getConfig(), headerRow, row, _departmentCode);
					if (!_courses.containsKey(c.getCourseNumber())) {
						_courses.put(c.getCourseNumber(), c);
					}
					headerRow = null;
				}
				else {
					headerRow = row;
				}
			}
		}
	}
}
