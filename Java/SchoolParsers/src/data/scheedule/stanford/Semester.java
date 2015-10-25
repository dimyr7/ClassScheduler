package data.scheedule.stanford;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.scheedule.base.DepartmentBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.utils.ArrayUtils;

public class Semester extends SemesterBase {
	
	//////////////////////////////////////////////////////////////////
	/// Members
	
	private Map<String, Department> _departments;
	
	@Override public List<DepartmentBase> getDepartments() { return ArrayUtils.castAll(_departments.values(), DepartmentBase.class); }
	@Override public DepartmentBase findDepartment(String code) { return _departments.get(code); }
	
	public Config getConfig() { return (Config)getBaseConfig(); }

	//////////////////////////////////////////////////////////////////
	//// .ctor
	
	public Semester(Config config) {
		super(config);
		_departments = new HashMap<String, Department>();
		loadDepartments();
	}

	private void loadDepartments() {
		HtmlPage page = getConfig().makeRequest(Config.c_CourseSearchUrl);
		
		HtmlElement mainContent = page.getElementById("mainContent");
		for (HtmlElement link : mainContent.getElementsByTagName("a"))
		{
			HtmlAnchor a = (HtmlAnchor) link;
			Department dept = new Department(getConfig(), a);
			if (dept.isValid()) {
				_departments.put(dept.getDepartmentCode(), dept);
			}
		}
	}
}
