package data.scheedule.illinoisold;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.RunnerBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.base.ConfigBase.Term;

public class Runner extends RunnerBase {

	@Override protected ConfigBase createConfig(Term term, String year) { return new Config(term, year); }
	@Override protected SemesterBase createSemester(ConfigBase config) { return new Semester((Config) config); }
	
	@Override public String getCourseSearchUrl() { return Config.c_CoursesBaseUrl; }
	@Override public Term[] getValidTerms() { return new Term[] { Term.Spring, Term.Summer, Term.Fall }; }

	@Override
	public void run(Term term, String year) {
		Semester semester = new Semester(new Config(term, year));
		
		semester.beginXml();
		int i = 0, count = semester.getDepartments().size();
		for (DepartmentBase dept : semester.getDepartments()) {
			System.out.printf("Loading courses for Department: %s - %s [%d/%d]\n", dept.getDepartmentCode(), dept.getDepartmentName(), i++, count);
			dept.loadCourses();
			semester.getBaseConfig().writeXml(dept.toXml());
		}
		
		semester.endXml();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config config = new Config(Term.Spring, "2012");
		
		Semester semester = new Semester(config);
		//Department dept = (Department) semester.findDepartment("CS");
		//dept.loadCourses();
		
		int index = 0, size = semester.getDepartments().size();
		for (DepartmentBase dept : semester.getDepartments()) {
			Department department = (Department) dept;
			System.out.printf("Loading courses for %s [%d/%d]=%f%%\n", department.getDepartmentCode(), ++index, size, (100.0 * index) / size);
			config.writeDebug("Loading Courses for %s\n", department.getDepartmentCode());
			department.loadCourses();
		}
	}

}
