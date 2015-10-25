/**
 * Runner.java
 * @copyright Scheedule, Inc.
 */

package data.scheedule.nu;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.RunnerBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.base.ConfigBase.Term;

/**
 * This class is the runner class for Northwestern using the PDFs
 */
public class Runner extends RunnerBase {

	/**
	 * @see RunnerBase#createConfig(Term, String)
	 */
	@Override
	protected ConfigBase createConfig(Term term, String year) {
		return new Config(term, year);
	}

	/**
	 * @see RunnerBase#createSemester(ConfigBase)
	 */
	@Override
	protected SemesterBase createSemester(ConfigBase config) {
		return null;
	}

	/**
	 * @see RunnerBase#getCourseSearchUrl()
	 */
	@Override
	public String getCourseSearchUrl() {
		//return Config.c_CourseUrl;
		return null;
	}

	/**
	 * @see RunnerBase#getValidTerms()
	 */
	@Override
	public Term[] getValidTerms() {
		return new Term[] {
			Term.Spring,
			Term.Summer,
			Term.Fall,
			Term.Winter
		};
	}

	/**
	 * @see RunnerBase#run(Term, String)
	 */
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
	 * Main for running only this package
	 * @param args
	 */
	public static void main(String[] args) {		
		Config config = new Config(Term.Winter, "2014");
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
}
