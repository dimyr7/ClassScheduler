package data.scheedule.uic;

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
	
	private Map<String, Department> _departments;
	
	private Config getConfig() { return (Config)getBaseConfig(); }

	@Override
	public List<DepartmentBase> getDepartments() {
		return ArrayUtils.castAll(_departments.values(), DepartmentBase.class);
	}
		
	public Semester(Config config) {
		super(config);
		_departments = new HashMap<String, Department>();
		loadDepartments();
	}
	
	private String getSemester() { return String.format("%s %s", getConfig().getTerm(), getConfig().getYear()); }
	
	private void loadDepartments() {
		
		HtmlPage page = getConfig().getPage(Config.SearchUrl);
		HtmlSelect select = page.getElementByName("termvalue");			
		HtmlOption option = select.getOptionByText(getSemester());
		
		HtmlPage deptPage = getConfig().getPage(Config.getDepartmentUrl(option.getValueAttribute()));
		HtmlSelect departments = deptPage.getElementByName("subjvalue");
		
		for (HtmlOption deptOption : departments.getOptions()) {
			_departments.put(deptOption.getValueAttribute(), new Department(getConfig(), option, deptOption));
		}
	}
	
	
	@Override
	public Department findDepartment(String code) {
		return _departments.get(code);
	}
}
