package data.scheedule.base;

import java.util.List;

/**
 * Base class which represents a Semester, should contain a list of Departments
 * @author Mike Barker
 */
public abstract class SemesterBase {
	
	/**********************************************************************
	 * Abstract Member Functions
	 **********************************************************************/
	
	/**
	 * @return List of Departments listed for this Semester
	 */
	public abstract List<DepartmentBase> getDepartments();
	
	/**
	 * Finds a specific Department by Department Code
	 * @param code Code of the Department, case sensitive
	 * @return Department if found or null
	 */
	public abstract DepartmentBase findDepartment(String code);
	
	/**********************************************************************
	 * Members
	 **********************************************************************/
	
	private ConfigBase _config;
	public ConfigBase getBaseConfig() { return _config; }
	
	/**********************************************************************
	 * .ctor
	 **********************************************************************/
	
	protected SemesterBase(ConfigBase config) { _config = config; }
	
	/**********************************************************************
	 * Public Function
	 **********************************************************************/
	
	/**
	 * Begins writing the Xml
	 */
	public void beginXml() {
		_config.writeXml("<ScheeduleData>\n");
	}
	
	/**
	 * Finishes writing the Xml
	 */
	public void endXml() {
		_config.writeXml("</ScheeduleData>\n");
		
		_config.close();
	}
}
