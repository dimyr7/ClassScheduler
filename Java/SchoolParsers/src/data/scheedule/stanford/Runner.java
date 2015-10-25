package data.scheedule.stanford;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.RunnerBase;
import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.base.ConfigBase.Term;

public class Runner extends RunnerBase {

	@Override protected ConfigBase createConfig(Term term, String year) { return new Config(term, year); }
	@Override protected SemesterBase createSemester(ConfigBase config) { return new Semester((Config)config); }

	@Override public String getCourseSearchUrl() { return Config.c_CourseSearchUrl; }
	@Override public Term[] getValidTerms() { return new Term[] { Term.Spring, Term.Summer, Term.Autumn, Term.Winter }; }
	
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
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			Config config = new Config(Term.Autumn, "2011");
			
			Semester semester = new Semester(config);
			Department dept = (Department)semester.findDepartment("MS&E");
			dept.loadCourses();	
			
			for (CourseBase c : dept.getCourses()) {
				System.out.printf("Course %s (%s-%s hours) has %d section groups.\n", c.getCourseNumber(), c.getMinCreditHours(), c.getMaxCreditHours(), c.getSections().size());
				if (c.getSections().size() < 10) {
					System.out.printf("[");
					for (SectionGroupBase sg : c.getSections()) {
						//System.out.printf("SectionGroup: %s (%s) => %d sections.\n", sg.getSectionId(), sg.getUniqueId(), sg.getSections().size());
						//System.out.printf("%s, ", sg.getUniqueId());
						
						for (SectionBase s : sg.getSections()) {
							System.out.printf("%s, ", s.getSectionType());
							/*System.out.printf("Section %s => Start: %s  End: %s  Days: %s  Time: %d-%d\n", 
									s.getSectionName(),
									s.getStartDate(),
									s.getEndDate(),
									s.getDays().toString(),
									s.getTimes() != null ? s.getTimes().getStartTime() : -1,
									s.getTimes() != null ? s.getTimes().getEndTime() : -1);*/
						}
					}
					System.out.printf("]\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
