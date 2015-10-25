package data.scheedule.utexas;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.scheedule.base.DepartmentBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;

public class Semester extends SemesterBase {

	private Config getConfig() { return (Config) getBaseConfig(); }
	private Map<String, Department> _departments;
	
	@Override
	public List<DepartmentBase> getDepartments() { return ArrayUtils.castAll(_departments.values(), DepartmentBase.class); }
	
	public Collection<Department> getAllDepartments() { return _departments.values(); }
	
	public Semester(Config config) {
		super(config);
		_departments = new HashMap<String, Department>();
	}
	
	
	public void loadDepartments() {
		try {
			HtmlPage index = getConfig().makeAuthenticatedRequest(getConfig().getIndexUrl());
			HtmlSelect combo = (HtmlSelect) index.getElementById("s_fos_fl");
			for(HtmlOption option : combo.getOptions()) {
				if (!StringUtils.isNullOrEmptyTrimmed(option.getValueAttribute())) {
					_departments.put(option.getValueAttribute(), new Department(getConfig(), option));
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Department findDepartment(String code) {
		return _departments.get(code);
	}
}
