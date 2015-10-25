package data.scheedule.base;

import java.util.List;

import data.scheedule.utils.StringBuilder;
import data.scheedule.utils.XmlHelper;

/**
 * Base class for a department, it should contain a list of courses,
 * and other misc. information such as department name and code, i.e. 'Physics' 'PHY'
 * @author Mike Barker
 */
public abstract class DepartmentBase {
	
	/**********************************************************************
	 * Abstract Member Functions
	 **********************************************************************/
	
	/**
	 * @return Department code for this department, i.e. 'PHY'
	 */
	public abstract String getDepartmentCode();
	
	/**
	 * @return Full name for this department
	 */
	public abstract String getDepartmentName();
	
	
	/**
	 * @return List of Courses available in this department
	 */
	public abstract List<CourseBase> getCourses();
	
	
	/**
	 * Loads the courses, it is assumed that loading courses is a non-trivial, 
	 * i.e. requires >= 1 web request, operation, which is why it's not implied
	 * to have already happened when the department is created.
	 */
	public abstract void loadCourses();
	
	/**********************************************************************
	 * Members
	 **********************************************************************/
	
	private ConfigBase _config;
	public ConfigBase getBaseConfig() { return _config; }
	protected void setConfig(ConfigBase config) { _config = config; }

	/**********************************************************************
	 * .ctor
	 **********************************************************************/
	
	protected DepartmentBase(ConfigBase config) { _config = config; }
	
	/**********************************************************************
	 * Public Functions
	 **********************************************************************/
	
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<Department>\n");
		
		sb.appendf("\t<Code>%s</Code>\n", XmlHelper.escapeXml(getDepartmentCode().replace(" ", "")));
		sb.appendf("\t<Description>%s</Description>\n", XmlHelper.escapeXml(getDepartmentName()));
		
		for (CourseBase course : getCourses()) {
			if (course.isValid()) {
				sb.append(course.toXml());
			}
		}
						
		sb.append("</Department>\n");
		
		return sb.toString();
	}
}
