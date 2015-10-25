package data.scheedule.nu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.base.SemesterBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

public class Semester extends SemesterBase {

	private Map<String, Department> _departments;
	private String[] _linesToIgnore = new String[] {
		"CAT # TITLE CRS ATTR CREDIT",
		"CLS # SEC TYPE ROOM DAYS START END INSTRUCTOR CLS ATTR PERM",
		"TOPIC/NOTES",
		"PLEASE SEE DEPT FOR CLASS # AND PERMISSION"
	};
	
	private String[] _instruments = new String[] {
		"Conducting",
		"Piano",
		"Organ",
		"Violin",
		"Viola",
		"Cello",
		"Guitar",
		"Trumpet",
		"Horn",
		"Euphonium",
		"Trombone",
		"Tuba",
		"Flute",
		"Oboe",
		"Clarinet",
		"Saxophone",
		"Bassoon",
		"Percussion",
		"Saxophone",
		"Trumpet",
		"Trombone",
		"Piano",
		"Percussion",
		"Guitar",
		"Saxophone",
		"Trumpet",
		"Trombone",
		"Piano",
		"Percussion",
		"Guitar",
		"Saxophone",
		"Trumpet",
		"Trombone",
		"Piano",
		"Percussion",
		"Guitar",
		"Saxophone",
		"Trumpet",
		"Trombone",
		"Piano",
		"Percussion",
		"Guitar",
		"Saxophone",
		"Trumpet",
		"Trombone",
		"Piano",
		"Percussion",
		"Guitar",
		"Content",
		"Content",
		"Piano",
		"Strings",
		"Operetta",
		"Harpsichord",
		"Organ",
		"Harpsichord",
		"Organ",
		"Violin",
		"Viola",
		"Cello",
		"Harp",
		"Guitar",
		"Violin",
		"Viola",
		"Cello",
		"Harp",
		"Guitar",
		"Ornamentation",
		"Saxophone",
		"Bassoon",
		"Oboe",
		"Tuba",
		"Trombone",
		"Percussion",
		"Saxophone",
		"Bassoon",
		"Clarinet",
		"Oboe",
		"Tuba",
		"Trombone",
		"Percussion",
		"Euphonium",
		"E-Commerce"	
	};
	
	private String[] _notTitles = new String[] {
		"RSTR ISP",
		"CASD INTERDISCP"
	};
	
	private String[] _invalidProfessorChars = new String[] {
		" ",
		"/",
		"&",
		","
	};
	
	private String _lastLineDepartmentName;
	private Department _lastLineDepartment;
	private Course _lastLineCourse;
	private Section _lastLineSection;

	/**
	 * Creates a new instance of the Semester that we are parsing
	 * @param config The config that is currently running
	 */
	public Semester(Config config) {
		super(config);
		
		_departments = new HashMap<String, Department>();
		parsePdf();
		generateCombos();
	}
	
	/**
	 * @see SemesterBase#getDepartments()
	 */
	@Override 
	public List<DepartmentBase> getDepartments() { 
		return ArrayUtils.castAll(_departments.values(), DepartmentBase.class); 
	}
	
	/**
	 * @see SemesterBase#findDepartment(String)
	 */
	@Override 
	public DepartmentBase findDepartment(String code) { 
		return _departments.get(code); 
	}
	
	/**
	 * @return The currently running config
	 */
	private Config getConfig() { return (Config)getBaseConfig(); }

	/**
	 * Parses the PDF
	 */
	private void parsePdf() {
		String[] pdfLines = getConfig().getCourseDataLines();
		
		System.out.printf("There are %d lines to parse through in the PDF\n", pdfLines.length);
		for(String line : pdfLines) {
			parseLine(line);
		}
	}
	
	/**
	 * Clears the last line parts we are storing
	 */
	private void clearLastLines() {
		_lastLineCourse = null;
		_lastLineDepartment = null;
		_lastLineDepartmentName = null;
		_lastLineSection = null;
	}
	
	/**
	 * Parses a single line from the PDF
	 * @param line The line we are parsing
	 */
	private void parseLine(String line) {
		getConfig().writeDebug("! Parsing Line: '%s'\n", line);
		
		if(isLineToIgnore(line)) {
			return;
		}
		
		if(isLineSchool(line)) {
			clearLastLines();
			return;
		}
		
		if(isDepartmentNameOrCode(line)) {
			if(_lastLineDepartmentName != null) {
				Department dept = new Department(getConfig(), line, _lastLineDepartmentName);
				if(_departments.containsKey(dept.getDepartmentCode())) {
					System.out.printf("*** The department %s (%s) was already in the department list for a semester\n", dept.getDepartmentCode(), dept.getDepartmentName());
					dept = _departments.get(dept.getDepartmentCode());
				}
				
				_departments.put(dept.getDepartmentCode(), dept);
				clearLastLines();
				getConfig().writeDebug("-- Found Department: %s (%s)\n", dept.getDepartmentCode(), dept.getDepartmentName());
				_lastLineDepartment = dept;
				return;
			}
			
			clearLastLines();
			_lastLineDepartmentName = line;
			return;
		}
		
		if(_lastLineDepartment != null) {
			parseLineForDepartment(line);
		}
		else {
			getConfig().writeDebug("*** Unknown Line without a current department!!! '%s'\n", line);
		}
	}
	
	/**
	 * Parses a line from the PDF when we have a department ready to go, meaning this line should be related to a course/section
	 * @param line The pdf line we are parsing
	 */
	private void parseLineForDepartment(String line) {
		Matcher m = Course.CourseLineRegex.matcher(line);
		if(m.matches()) {
			_lastLineCourse = _lastLineDepartment.addCourse(new Course(getConfig(), m));
			_lastLineSection = null;	// Make sure this is cleared
			return;
		}
		
		m = Section.SectionLineRegex.matcher(line);
		if(m.matches()) {
			SectionGroup sectionGroup = new SectionGroup(getConfig(), m);
			_lastLineCourse.addSectionGroup(sectionGroup);
			_lastLineSection = sectionGroup.getFirstSection();
			return;
		}
		
		// Ignore
		if(isLineCourseAttribute(line)) {
			return;
		}
		
		// Clean the line
		String cleanedLine = getConfig().clean(line);
		if(StringUtils.isNullOrEmptyTrimmed(cleanedLine)) {
			return;
		}
		
		if(isLineCourseAttribute(cleanedLine.trim())) {
			return;
		}
				
		// If it's just one word assume it's another instructor listed
		if(_lastLineSection != null && isPotentialInstructor(cleanedLine.trim())) {
			getConfig().writeDebug("+=+ Assuming this is a instructor: '%s'\n", cleanedLine);
			_lastLineSection.appendInstructor(line);
			return;
		}
		
		if(_lastLineSection == null && _lastLineCourse != null && isPotentialTitle(cleanedLine.trim())) {
			getConfig().writeDebug("+=+ Assuming this is an extension of a title '%s'\n", cleanedLine);
			_lastLineCourse.appendName(cleanedLine.trim());
			return;
		}
		
		getConfig().writeDebug("***!! Not sure what this line is '%s'\n", line);
	}
	
	/**
	 * There are some lines in the PDF that are all over the place in the middle of the text which aren't section/course data, they are just extra info that we should ignore
	 * @param line The pdf line we are checking if it's an ignored line
	 * @return Whether or not the line is one we should ignore
	 */
	private boolean isLineToIgnore(String line) {
		for(String lineToIgnore : _linesToIgnore) {
			if(line.equalsIgnoreCase(lineToIgnore) || line.startsWith(lineToIgnore)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * School lines should start with '>>>' since that was prefixed because of their font size, it means we are done with the previous school/department/courses/sections
	 * @param line The pdf line we are looking at
	 * @return Whether or not the line is a School name 
	 */
	private boolean isLineSchool(String line) {
		return line.startsWith(">>>");
	}
	
	/**
	 * Department names and department codes should be prefixed with '>>' because of their font size
	 * @param line The pdf line we are looking at
	 * @return Whether or not the line is a Department Name/Code
	 */
	private boolean isDepartmentNameOrCode(String line) {
		return line.startsWith(">>") && !line.startsWith(">>>"); 
	}
	
	/**
	 * Determines if the line is ONLY a course attribute abbreviation, meaning we can ignore it
	 * @param line The pdf line we are looking at
	 * @return Whether or not the line is a Course Attribute Abbreviation
	 */
	private boolean isLineCourseAttribute(String line) {
		return getConfig().getCourseAttributes().containsKey(line);
	}
	
	/**
	 * Determines if the line is potentially a course title, since some overflow onto multiple lines
	 * @param line The pdf line we are looking at
	 * @return Whether or not it's probably part of a course title
	 */
	private boolean isPotentialTitle(String line) {
		for(String invalidTitle : _notTitles) {
			if(line.contains(invalidTitle)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Determines if the line is potentially a section instructor name, if there are multiple instructors they are sepearted by multiple lines, 
	 * however there's also often useless info below lines that are things we have to ignore, i.e. in the Music department there are instruments under lots of the sections
	 * @param line The pdf line we are looking at
	 * @return Whether or not it's probably an instructors name
	 */
	private boolean isPotentialInstructor(String line) {
		for(String invalidChar : _invalidProfessorChars) {
			if(line.contains(invalidChar)) {
				return false;
			}
		}
		
		// hardcoded to handle the extra lines in the music department
		for(String instrument : _instruments) {
			if(line.equalsIgnoreCase(instrument) || line.startsWith(instrument)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * After the PDF parsing is done, we should generate combos for all classes
	 */
	private void generateCombos() {
		for(String departmentCode : _departments.keySet()) {
			Department department = _departments.get(departmentCode);
			for(CourseBase c : department.getCourses()) {
				Course course = (Course)c;
				course.generateCombos();
			}
		}
	}
}
