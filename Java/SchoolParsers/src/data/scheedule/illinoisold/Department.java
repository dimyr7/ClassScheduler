package data.scheedule.illinoisold;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;
import data.scheedule.utils.XmlHelper;

public class Department extends DepartmentBase {

	///////////////////////////////////////////////////////////////////////
	//// Members
	
	private List<Course> _courses;
	private String _departmentCode;
	private String _departmentName;
	
	@Override public List<CourseBase> getCourses() { return ArrayUtils.castAll(_courses, CourseBase.class); }
	@Override public String getDepartmentCode() { return _departmentCode; }
	@Override public String getDepartmentName() { return _departmentName; }

	protected Config getConfig() { return (Config)getBaseConfig(); }
	
	///////////////////////////////////////////////////////////////////////
	//// .ctor
	
	public Department(Config config, Element departmentRow) {
		super(config);
		
		_courses = new ArrayList<Course>();
		
		parseDepartmentRow(departmentRow);
	}

	////////////////////////////////////////////////////////////////////////
	//// Helper Functions
	
	private void parseDepartmentRow(Element departmentRow) {
		_departmentCode = XmlHelper.getTextValue(departmentRow, "subjectCode");
		_departmentName = XmlHelper.getTextValue(departmentRow, "subjectDescription"); 
	}

	////////////////////////////////////////////////////////////////////////
	//// Public Functions
	
	@Override
	public void loadCourses() {
		Document document = getConfig().makeRelativeRequest(String.format("%s/index.xml", _departmentCode));
		// Get the course data out of the xml		
		if(document != null) {
			NodeList courseElements = document.getElementsByTagName("course");
            for(int c = 0; c < courseElements.getLength(); c++) {
            	Course course = new Course(getConfig(), _departmentCode, (Element)courseElements.item(c));
            	if (course.isValid()) {
            		_courses.add(course);
            	}
			}
		}
	}

}
