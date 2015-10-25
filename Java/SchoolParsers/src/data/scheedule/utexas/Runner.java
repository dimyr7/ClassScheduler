package data.scheedule.utexas;

import java.util.List;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.RunnerBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.base.ConfigBase.Term;

public class Runner extends RunnerBase {
	
	@Override protected ConfigBase createConfig(Term term, String year) { return new Config(term, year); }
	@Override protected SemesterBase createSemester(ConfigBase config) { return new Semester((Config)config); }
	
	@Override public String getCourseSearchUrl() { return String.format("%s/%s with credentials: %s/%s", Config.BaseUrl, Config.IndexPath, Config.LoginName, Config.LoginPassword); }
	@Override public Term[] getValidTerms() { return new Term[] { Term.Spring, Term.Summer, Term.Fall }; }

	@Override
	public void run(Term term, String year) {
		Semester semester = new Semester(new Config(term, year));
		semester.loadDepartments();
		
		semester.beginXml();
		
		int i = 1;
		int count = semester.getDepartments().size();
		
		List<DepartmentBase> departments = semester.getDepartments(); 
		
		while (departments.size() > 0) {
			DepartmentBase dept = departments.remove(0);
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
		Semester semester = new Semester(new Config(Term.Spring, "2012"));
		semester.loadDepartments();
			
		semester.beginXml();
		
		int i = 1;
		int count = semester.getDepartments().size();
		
		List<DepartmentBase> departments = semester.getDepartments(); 
		
		while (departments.size() > 0) {
			DepartmentBase dept = departments.remove(0);
			System.out.printf("Loading courses for Department: %s - %s [%d/%d]\n", dept.getDepartmentCode(), dept.getDepartmentName(), i++, count);
			dept.loadCourses();
			semester.getBaseConfig().writeXml(dept.toXml());
		}
		
		semester.endXml();
	}
}