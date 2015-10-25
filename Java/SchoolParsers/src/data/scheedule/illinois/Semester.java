package data.scheedule.illinois;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import data.scheedule.base.DepartmentBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.utils.ArrayUtils;

public class Semester extends SemesterBase {

	private Map<String, Department> _departments;
	
	@Override
	public List<DepartmentBase> getDepartments() {
		return ArrayUtils.castAll(_departments.values(), DepartmentBase.class);
	}

	private Config getConfig() {
		return (Config)getBaseConfig();
	}
	
	public Semester(Config config) {
		super(config);
		_departments = new HashMap<String, Department>();
		
		loadDepartments();
	}
	
	private void loadDepartments() {
		Document xml = getConfig().loadXml(String.format("%s.xml", getConfig().getBaseSemesterUrl()));
		
		NodeList subjects = xml.getElementsByTagName("subject");
		for(int s = 0; s < subjects.getLength(); s++) {
			Element subject = (Element)subjects.item(s);
			Department dept = new Department(getConfig(), subject);
			_departments.put(dept.getDepartmentCode(), dept);
		}
	}

	@Override
	public DepartmentBase findDepartment(String code) {
		return _departments.get(code);
	}
	
}
