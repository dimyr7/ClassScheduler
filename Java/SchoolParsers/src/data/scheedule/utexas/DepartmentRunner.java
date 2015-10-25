package data.scheedule.utexas;

public class DepartmentRunner extends Thread
{
	private Department _department;
	public DepartmentRunner(Department department, Config config)
	{
		_department = department;
		_department.setConfig(config);
	}
		
	public Department getDepartment()
	{
		return _department;
	}
	
	public void run()
	{
		_department.loadCourses();
	}
}
