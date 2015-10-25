package data.scheedule.base;

import data.scheedule.base.ConfigBase.Term;

public abstract class RunnerBase {

	protected abstract ConfigBase createConfig(Term term, String year);
	protected abstract SemesterBase createSemester(ConfigBase config);
	
	public abstract String getCourseSearchUrl();
	public abstract Term[] getValidTerms();
	
	public abstract void run(Term term, String year);	
	
	public void run(Term term, String year, String departmentCode) {
		ConfigBase config = createConfig(term, year);
		SemesterBase semester = createSemester(config);
		
		semester.beginXml();
		DepartmentBase dept = semester.findDepartment(departmentCode);
		if (dept != null) {
			System.out.printf("Found department %s\n", departmentCode);
			dept.loadCourses();
			config.writeXml(dept.toXml());
		}
		else {
			System.out.printf("Unable to find department %s\n", departmentCode);
		}
		semester.endXml();
	}
}
