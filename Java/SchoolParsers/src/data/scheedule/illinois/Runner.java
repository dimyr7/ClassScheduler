package data.scheedule.illinois;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.RunnerBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.base.ConfigBase.Term;

public class Runner extends RunnerBase {

	@Override
	protected ConfigBase createConfig(Term term, String year) {
		return new Config(term, year);
	}

	@Override
	protected SemesterBase createSemester(ConfigBase config) {
		return new Semester((Config) config);
	}

	@Override
	public String getCourseSearchUrl() {
		return "http://courses.illinois.edu/";
	}

	@Override
	public Term[] getValidTerms() {
		return new Term[] {
			Term.Spring,
			Term.Summer,
			Term.Fall
		};
	}

	@Override
	public void run(Term term, String year) {
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config config = new Config(Term.Spring, "2014");
		Semester semester = new Semester(config);
		
		//Department dept = (Department) semester.findDepartment("CS");
		//dept.loadCourses();
		
		int index = 0;
		int size = semester.getDepartments().size();
		for(DepartmentBase d : semester.getDepartments()) {
			 System.out.printf("[%d/%d] Loading courses for: %s\n", index++, size, d.getDepartmentCode());
			 d.loadCourses();
		}
	}
}
