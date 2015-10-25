package data.scheedule.udub;

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
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	private Map<String, Department> _departments;
	
	@Override public List<DepartmentBase> getDepartments() { return ArrayUtils.castAll(_departments.values(), DepartmentBase.class); }
	
	public Semester(Config config) {
		super(config);
		
		loadDepartments();
	}

	private void loadDepartments() {
		HtmlPage page = getConfig().makeRequest(getConfig().getBaseScheduleUrl());
		
		_departments = new HashMap<String, Department>();
		for (HtmlElement ele : page.getElementsByTagName("a")) {
			HtmlAnchor anchor = (HtmlAnchor) ele;
			
			if (!anchor.hasAttribute("href")) continue;
			if (anchor.getHrefAttribute().startsWith("#")) continue;
			if (anchor.getHrefAttribute().startsWith("/")) continue;
			if (anchor.getHrefAttribute().startsWith("http")) continue;
			if (!anchor.asText().matches(".+\\(.+\\)")) continue;
			if (anchor.asText().contains("see ")) continue;
			
			Department dept = new Department(getConfig(), anchor);
			_departments.put(dept.getDepartmentCode(), dept);
		}
	}

	@Override
	public DepartmentBase findDepartment(String code) {
		return _departments.get(code);
	}

	

}
