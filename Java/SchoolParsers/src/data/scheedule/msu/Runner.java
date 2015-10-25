package data.scheedule.msu;

import java.util.List;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.RunnerBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.base.ConfigBase.Term;

public class Runner extends RunnerBase {
	
	@Override protected ConfigBase createConfig(Term term, String year) { return new Config(term, year); }
	@Override protected SemesterBase createSemester(ConfigBase config) { return new Semester((Config)config); }
	
	@Override public String getCourseSearchUrl() { return Config.MainUrl; }
	@Override public Term[] getValidTerms() { return new Term[] { Term.Spring, Term.Summer, Term.Fall }; }

	
	@Override
	public void run(Term term, String year) {

		Config config = new Config(term, year);
		Semester semester = new Semester(config);
		
		semester.beginXml();
		int index = 0, count = semester.getDepartments().size();
		for(DepartmentBase d : semester.getDepartments()) {
			Department dept = (Department)d;
			System.out.printf("[%d/%d] Loading departments for %s\n", ++index, count, dept.getDepartmentCode());
			dept.loadCourses();
			semester.getBaseConfig().writeXml(dept.toXml());
			dept.clearCourses();
		}
		
		semester.endXml();
		
		/*
		List<DepartmentBase> departments = semester.getDepartments();
		
		semester.beginXml();
		
		DepartmentBase dept = semester.findDepartment("PHY");
		dept.loadCourses();
		semester.getBaseConfig().writeXml(dept.toXml());
		
		semester.endXml();
		
		
		List<DepartmentBase> departments = semester.getDepartments();
		int numThreads = 5;
		int num = 1;
		int count = departments.size();
		
		DepartmentRunner[] runners = new DepartmentRunner[numThreads];
		Config[] configs = new Config[numThreads];
		for (int i=0; i<numThreads; i++) {
			configs[i] = new Config(config);
		}
		
		semester.beginXml();
		while (departments.size() > 0) {
			for (int i=0; i<numThreads; i++) {
				if (departments.size() > 0) {
					Department dept = (Department) departments.remove(0);
					System.out.printf("Loading courses for Department: %s [%d/%d]\n", dept.getDepartmentCode(), num++, count);
					runners[i] = new DepartmentRunner(dept, configs[i]);
					runners[i].start();
				}
			}
			
			for (int i=0; i<numThreads; i++) {
				if (runners[i] != null) {
					try {
						runners[i].join();
					} catch (InterruptedException e) {e.printStackTrace(); } 
				}
			}
			
			for (int i=0; i<numThreads; i++) {
				if (runners[i] != null) {
					semester.getBaseConfig().writeXml(runners[i].getDepartment().toXml());
					runners[i] = null;
				}
			}
		}
		
		semester.endXml();  */
	}

}
