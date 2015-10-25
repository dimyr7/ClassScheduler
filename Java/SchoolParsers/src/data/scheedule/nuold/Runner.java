package data.scheedule.nuold;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.RunnerBase;
import data.scheedule.base.ConfigBase.Term;

public class Runner extends RunnerBase {

	@Override public String getCourseSearchUrl() { return "http://www.northwestern.edu/caesar/"; }
	@Override public Term[] getValidTerms() { return new Term[] { Term.Spring, Term.Summer, Term.Fall, Term.Winter }; }
	
	@Override public Config createConfig(Term term, String year) { return new Config(term, year); }
	@Override public Semester createSemester(ConfigBase config) { return new Semester((Config)config); }
	
	@Override 
	public void run(Term term, String year) {
		Config config = new Config(term, year);
		Semester semester = new Semester(config);
		
		semester.beginXml();
		
		int index = 0, size = semester.getDepartments().size();
		for (DepartmentBase dept : semester.getDepartments()) {
			System.out.printf("Loading courses for Department: %s (%s) [%d/%d]\n", dept.getDepartmentCode(), dept.getDepartmentName(), index++, size);
			dept.loadCourses();
			if (dept.getCourses().size() > 0) {
				config.writeXml(dept.toXml());
			}
		}
		
		semester.endXml();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config config = new Config(Term.Spring, "2012");
		Semester semester = new Semester(config);
		
		Department dept = (Department)semester.findDepartment("MATH");
		dept.loadCourses();
	}

}
