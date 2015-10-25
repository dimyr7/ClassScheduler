package data.scheedule.illinoisold;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.scheedule.base.DepartmentBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;

public class Semester extends SemesterBase {
	
	//////////////////////////////////////////////////////////
	//// Members
	
	private Map<String, Department> _departments;
	
	@Override public List<DepartmentBase> getDepartments() { return ArrayUtils.castAll(_departments.values(), DepartmentBase.class); }
	
	private Config getConfig() { return (Config)getBaseConfig(); }
	
	//////////////////////////////////////////////////////////
	//// .ctor
	
	public Semester(Config config) {
		super(config);
		
		_departments = new HashMap<String, Department>();
		
		loadDepartments();
	}

	//////////////////////////////////////////////////////////
	//// Helper Functions
	
	private void loadDepartments() {
		Document document = getConfig().makeRelativeRequest("index.xml");
		
		if(document != null) {
			// Loop over all the departments and add them to our internal array
			NodeList departmentElements = document.getElementsByTagName("subject");
			for (int d = 0; d < departmentElements.getLength(); d++) {
				Element departmentElement = (Element)departmentElements.item(d);
				Department department = new Department(getConfig(), departmentElement);
				_departments.put(department.getDepartmentCode(), department);
			}
		}
	}

	//////////////////////////////////////////////////////////
	//// Public Functions

	@Override
	public DepartmentBase findDepartment(String code) {
		return _departments.get(code);
	}

}
