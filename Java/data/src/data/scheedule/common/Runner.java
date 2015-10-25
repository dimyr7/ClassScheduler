package data.scheedule.common;

import data.scheedule.base.RunnerBase;
import data.scheedule.base.ConfigBase.Term;

public class Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String school = findArgValue(args, "school"),
			   term = findArgValue(args, "term"),
			   year = findArgValue(args, "year"),
			   dept = findArgValue(args, "dept");
		
		if (findArgValue(args, "info") != null && school != null) {
			printSchoolInfo(school);
			return;
		}
			   
		if (school == null || term == null || year == null || findArgValue(args, "help") != null) {
			printHelp();
			return;
		}
		
		Term selectedTerm = getTermFrom(term);
		if (selectedTerm == null) {
			System.out.printf("Error finding Term '%s'\n", term);
			return;
		}
		
		RunnerBase runner = getRunnerFrom(school);
		if (runner == null) {
			System.out.printf("Error finding Runner for '%s'\n", school);
			return;
		}
		
		if (dept == null) {
			runner.run(selectedTerm, year);
		}
		else {
			runner.run(selectedTerm, year, dept);
		}
	}
	
	private static String findArgValue(String[] args, String argName) {
		for(String arg : args) {
			if (arg.startsWith(String.format("--%s=", argName))) {
				return arg.substring(arg.indexOf('=') + 1);
			}
			else if (arg.equalsIgnoreCase(String.format("--%s", argName))) {
				return "";
			}
		}
		return null;
	}
	
	private static void printHelp() {
		System.out.println("DataParser --school=<school> --term=<term> --year=<year> [--dept=<department code>] | --help | --school=<school> --info");
		System.out.println("	<school> = School to run the parser for.");
		System.out.println("	<term> = Term to run the parser for, valid terms: Spring, Summer, Fall, Autumn, or Winter.");
		System.out.println("	<year> = Year to run the parser for.");
		System.out.println("	<department code> =  Optional department code");
		System.out.println("	--info = Prints out info for the specified school, does not run parser.");
		System.out.println("	--help = Prints this output");
	}
	
	private static void printSchoolInfo(String school) {
		RunnerBase runner = getRunnerFrom(school);
		if (runner == null) {
			System.out.printf("Error finding Runner for '%s'\n", school);
			return;
		}
		
		System.out.printf("Course Lookup Url: %s\n", runner.getCourseSearchUrl());
		System.out.printf("Valid Terms for %s: ", school);
		
		for (Term validTerm : runner.getValidTerms()) {
			System.out.printf(" '%s'", validTerm.toString());
		}
		System.out.printf("\n");
	}

	private static Term getTermFrom(String str) {
		try {
			return Term.valueOf(Term.class, str);
		} catch(Exception e) {}
		return null;
	}

	private static RunnerBase getRunnerFrom(String school) {
		String runnerClass = String.format("data.scheedule.%s.Runner", school.toLowerCase());
		try {
			return (RunnerBase) Class.forName(runnerClass).getConstructors()[0].newInstance();
		} catch(Exception e) {}
		return null;
	}
}
