package data.scheedule.udub;

import java.util.List;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.RunnerBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.base.ConfigBase.Term;

public class Runner extends RunnerBase {
	
	@Override protected ConfigBase createConfig(Term term, String year) { return new Config(term, year); }
	@Override protected SemesterBase createSemester(ConfigBase config) { return new Semester((Config)config); }
	
	@Override public String getCourseSearchUrl() { return Config.BaseUrl + Config.TimeSchedule; }
	@Override public Term[] getValidTerms() { return new Term[] { Term.Spring, Term.Summer, Term.Autumn, Term.Winter }; }
	
	@Override
	public void run(Term term, String year) {

		ConfigBase config = createConfig(term, year);
		SemesterBase semester = createSemester(config);
		
		semester.beginXml();
		List<DepartmentBase> departments = semester.getDepartments();
		int i = 1;
		int size = departments.size();
		while(departments.size() > 0) {
			DepartmentBase dept = departments.remove(0);
			System.out.printf("Loading courses for %s - %s [%d/%d]\n", dept.getDepartmentName(), dept.getDepartmentCode(), i++, size);
			
			dept.loadCourses();
			config.writeXml(dept.toXml());
		}
		
		semester.endXml();
		
		/*Department physics = (Department)semester.findDepartment("PHYS");
		
		semester.beginXml();
		
		if (physics != null) {
			System.out.println("Found physics!!");
			physics.loadCourses();
			
			config.writeXml(physics.toXml());
		}
		else {
			System.out.println("Didnt find physics");
		}
		
		semester.endXml();*/
	}
}
