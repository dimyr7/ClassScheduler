package data.scheedule.msu;

public class DepartmentRunner extends Thread {
	
	private Department _department;
	public Department getDepartment() { return _department; }
	
	public DepartmentRunner(Department department, Config config) {
		_department = department;
		_department.setConfig(config);
	}

	@Override
	public void run() {
		_department.loadCourses();
	}
}
