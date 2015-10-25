/**
 * Department.java
 * @copyright Scheedule, Inc.
 */

package data.scheedule.nu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.utils.ArrayUtils;

/**
 * This class represents a department at Northwestern
 */
public class Department extends DepartmentBase {

	private String _departmentCode;
	private String _departmentName;
	private Map<String, Course> _courses;
	
	/**
	 * Creates a new instance of a Department
	 * @param config The config that is currently running
	 * @param departmentCode The department code that was found
	 * @param departmentName The department name that was found
	 */
	public Department(Config config, String departmentCode, String departmentName) {
		super(config);
		
		_courses = new HashMap<String, Course>();
		_departmentCode = departmentCode.replace(">>", "").trim();
		_departmentName = departmentName.replace(">>", "").trim();
	}
	
	/**
	 * @see DepartmentBase#getDepartmentCode()
	 */
	@Override 
	public String getDepartmentCode() { 
		return _departmentCode; 
	}
	
	/**
	 * @see DepartmentBase#getDepartmentName()
	 */
	@Override 
	public String getDepartmentName() { 
		return _departmentName; 
	}
	
	/**
	 * @see DepartmentBase#getCourses()
	 */
	@Override 
	public List<CourseBase> getCourses() { 
		return ArrayUtils.castAll(_courses.values(), CourseBase.class); 
	}
	
	/**
	 * @see DepartmentBase#loadCourses()
	 */
	@Override 
	public void loadCourses() { 
		/* no-op */ 
	}
	
	/**
	 * Adds a course to the department
	 * @param course The course that is to be added to the department
	 * @return The course that is being referenced by the course number in the department
	 */
	public Course addCourse(Course course) {
		if(_courses.containsKey(course.getCourseNumber())) {
			getConfig().writeDebug("*** Course already found for department: %s\n", course.getCourseNumber());
		}
		else {
			_courses.put(course.getCourseNumber(), course);
		}
		
		return _courses.get(course.getCourseNumber());
	}
	
	/**
	 * @return The config that is currently running
	 */
	private Config getConfig() { 
		return (Config)getBaseConfig(); 
	}
}
