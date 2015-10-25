package data.scheedule.msu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

import data.scheedule.base.DepartmentBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.utils.ArrayUtils;

public class Semester extends SemesterBase {
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	private Map<String, Department> _departments;
	
	@Override public DepartmentBase findDepartment(String code) { return _departments.get(code); }
	@Override public List<DepartmentBase> getDepartments() { return ArrayUtils.castAll(_departments.values(), DepartmentBase.class); }

	public Semester(Config config) {
		super(config);
		loadDepartments();
	}

	private void loadDepartments() {
		_departments = new HashMap<String, Department>();
		
		HtmlPage page = getConfig().makeRequest(Config.MainUrl);
		HtmlSelect departments = (HtmlSelect) page.getElementById("Subject");
		
		if (departments == null) {
			System.out.println("ERROR : Unable to find deparement dropdown on search page.");
			return;
		}
		
		for (HtmlOption option : departments.getOptions()) {
			_departments.put(option.getValueAttribute(), new Department(getConfig(), option));
		}
		
		HtmlSelect semesters = (HtmlSelect) page.getElementById("Semester");
		
		if (semesters == null) {
			System.out.println("ERROR : Unable to find semesters dropdown on search page.");
			return;
		}
		
		getConfig().setSemesterValue(semesters.getOptionByText(getConfig().getSemesterString()).getValueAttribute());
	}
}
