package data.scheedule.uic;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.RunnerBase;
import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.base.ConfigBase.Term;

public class Runner extends RunnerBase {
	
	@Override protected ConfigBase createConfig(Term term, String year) { return new Config(term, year); }
	@Override protected SemesterBase createSemester(ConfigBase config) { return new Semester((Config)config); }
	
	@Override public String getCourseSearchUrl() { return Config.SearchUrl; }
	@Override public Term[] getValidTerms() { return new Term[] { Term.Spring, Term.Summer, Term.Fall }; }

	@Override
	public void run(Term term, String year) {

		Semester semester = new Semester(new Config(term, year));
		
		semester.beginXml();
		
		int i = 1;
		int count = semester.getDepartments().size();
		for (DepartmentBase dept : semester.getDepartments()) {
			System.out.printf("Loading courses for Department: %s - %s [%d/%d]\n", dept.getDepartmentCode(), dept.getDepartmentName(), i++, count);
			dept.loadCourses();
			semester.getBaseConfig().writeXml(dept.toXml());
		}
		
		semester.endXml();
	}

	public static void main(String[] args) {
		Semester semester = new Semester(new Config(Term.Spring, "2012"));
		
		Department dept = (Department)semester.findDepartment("PHYS");
		dept.loadCourses();
		
		for (CourseBase course : dept.getCourses()) {
			if (course.getCourseNumber().equalsIgnoreCase("141")) {
				System.out.printf("Found Course: %s\n", course.getCourseNumber());
				
				for (SectionGroupBase sectionGroup : course.getSections()) {
					System.out.printf("\tSection Group: %s (%s)\n", sectionGroup.getUniqueId(), sectionGroup.getType());
					
					for (SectionBase section : sectionGroup.getSections()) {
						System.out.printf("\t\tSection: %s %s %d-%d %s\n", section.getSectionName(), section.getDays().toString(), section.getTimes().getStartTime(), section.getTimes().getEndTime(), section.isValid() ? "VALID" : "INVALID");
					}
				}
			}
			
		}
	}
}
