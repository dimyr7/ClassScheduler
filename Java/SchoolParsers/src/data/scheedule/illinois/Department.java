package data.scheedule.illinois;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.utils.XmlHelper;

public class Department extends DepartmentBase {

	private String _departmentCode;
	private String _departmentName;
	private List<Course> _courses;
	
	private String _subjectXmlPath;
		
	@Override
	public String getDepartmentCode() {
		return _departmentCode;
	}

	@Override
	public String getDepartmentName() {
		return _departmentName;
	}

	@Override
	public List<CourseBase> getCourses() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Config getConfig() {
		return (Config) getBaseConfig();
	}
	
	public Department(Config config, Element subjectNode) {
		super(config);
		_courses = new ArrayList<Course>();
		
		_departmentCode = XmlHelper.getAttributeValue(subjectNode, "id");
		_departmentName = subjectNode.getTextContent();
		_subjectXmlPath = XmlHelper.getAttributeValue(subjectNode, "href");
	}

	@Override
	public void loadCourses() {
		System.out.printf("Loading courses for department: %s @ %s\n", _departmentCode, _subjectXmlPath);
		Document xml = getConfig().loadXml(_subjectXmlPath);
		
		if(xml == null) {
			System.out.printf("No course xml page not returned, skipping....\n");
			return;
		}
		
		NodeList courses = xml.getElementsByTagName("course");
		for(int s = 0; s < courses.getLength(); s++) {
			Element courseNode = (Element)courses.item(s);
			Course course = new Course(getConfig(), courseNode);
			if(course.isValid()) {
				_courses.add(course);
			}
		}
	}

}
