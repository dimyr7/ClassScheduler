package data.scheedule.utexas;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;
import data.scheedule.utils.UriBuilder;

public class Department extends DepartmentBase {
	
	public enum CourseLevel {
		Lower("L"),
		Upper("U"),
		Graduate("G");
		
		private String _value;
		public String getValue() { return _value; }
		
		private CourseLevel(String value) { _value = value; }
	}
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	private String _departmentCode;
	private String _departmentName;
	
	private PrintWriter _writer;
	
	private Map<String, Course> _courses;
	
	@Override public String getDepartmentCode() { return _departmentCode.replace(" ", ""); }
	@Override public String getDepartmentName() { return _departmentName; }
	@Override public List<CourseBase> getCourses() {
		List<CourseBase> courses = ArrayUtils.castAll(_courses.values(), CourseBase.class); 
		for (Course course : _courses.values()) {
			if (course.hasSiblings()) {
				courses.addAll(ArrayUtils.castAll(course.getSiblings(), CourseBase.class));
			}
		}
		return courses;
	}
		
	public Department(Config config, HtmlOption option) {
		super(config);
		_departmentCode = StringUtils.clean(option.getValueAttribute());
		_departmentName = StringUtils.toProperCase(StringUtils.clean(option.asText().replace(_departmentCode + " - ", "")));
		
		_courses = new HashMap<String, Course>();
	}
	
	public void setConfig(Config config) { super.setConfig(config); }
	
	public void loadCourses() {
		loadCoursesForLevel(CourseLevel.Lower, null);
		loadCoursesForLevel(CourseLevel.Upper, null);
		loadCoursesForLevel(CourseLevel.Graduate, null);
	}
	
	private void loadCoursesForLevel(CourseLevel level, String nextUnique) {
		try {
			HtmlPage results = getConfig().makeAuthenticatedRequest(getCourseUrl(level, nextUnique));
			HtmlTable classList = (HtmlTable) results.getElementById("classList");			
			
			if (classList == null) {
				System.out.printf("ERROR: Unable to find classList table for %s - %s, retrying...\n", _departmentCode, _departmentName);
				results = getConfig().makeAuthenticatedRequest(getCourseUrl(level, nextUnique));
				classList = (HtmlTable) results.getElementById("classList");
				if (classList == null) {
					System.out.printf("ERROR: Unable to find classList table for %s - %s, giving up on %s...\n", _departmentCode, _departmentName, getCourseUrl(level, nextUnique));
					getConfig().writeDebug(String.format("\n\nDepartment Course Lookup Failed:\n\tDept: %s - %s for level %s:\n\tUrl: %s\n\n", _departmentCode, _departmentName, level, getCourseUrl(level, nextUnique)));
					getConfig().writeDebug(results.asXml());
					getConfig().writeDebug("\n\n\n");
					return;
				}
			}
			
			boolean first = true;
			HtmlTableRow headerRow = null;
			List<HtmlTableRow> sectionRows = new ArrayList<HtmlTableRow>();

			for (HtmlTableRow row : classList.getRows()) {
				if (!first) {
					if (!row.getAttribute("class").equalsIgnoreCase("tbon")) {
						sectionRows.add(row);
					}
					else {
						if (headerRow != null) {
							Course newCourse = new Course(getConfig(), _departmentCode, headerRow, sectionRows);
							if (newCourse.isValid()) {
								if (_courses.containsKey(newCourse.getCourseNumber())) _courses.get(newCourse.getCourseNumber()).merge(newCourse);
								else _courses.put(newCourse.getCourseNumber(), newCourse);
							}
						}
						
						headerRow = row;
						sectionRows = new ArrayList<HtmlTableRow>();
					}
				}
				first = false;
			}
			
			
			boolean hasNextLink = false;
			try { hasNextLink = results.getAnchorByText("next") != null; } 
			catch(ElementNotFoundException e) { hasNextLink = false; }
			
			HtmlHiddenInput nextUniqueID = null;
			try { nextUniqueID = (HtmlHiddenInput) results.getElementByName("s_next_unique"); }
			catch (ElementNotFoundException e) {}
			
			if (hasNextLink && nextUniqueID != null) {
				loadCoursesForLevel(level, nextUniqueID.getValueAttribute());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getCourseUrl(CourseLevel level, String nextUnique) {
		UriBuilder uri = new UriBuilder(getConfig().createAbsoluteUrl("registrar/nrclav/results.WBX"));
		uri.add("s_ccyys", getConfig().getSemester())
		   .add("s_search_type_main", "FIELD").add("s_substantial_writing_comp", "A")
		   .add("s_fos_fl", _departmentCode).add("s_level", level.getValue())
		   .add("s_mtg_start_time_fl", " ").add("s_mtg_days_fl", " ")
		   .add("s_keywords", "").add("s_search_type", "ALL")
		   .add("s_instr_last_name", "").add("s_instr_first_initial", "")
		   .add("s_fos_sw", " ").add("s_fos_cn", " ")
		   .add("s_course_number", "").add("s_mtg_start_time_cn", " ")
		   .add("s_mtg_days_cn", " ").add("s_unique", "").add("s_start_unique", "")
		   .add("s_end_unique", "").add("s_mtg_start_time_st", "00").add("s_mtg_days_st", "000000");
		if (nextUnique != null) uri.add("s_next_unique", nextUnique);
		return uri.toString();
	}
		
	public String debug() {
		if (_writer != null) _writer.close();
		return String.format("Department %s got %d courses.\n", _departmentCode, _courses.size());
	}
}
