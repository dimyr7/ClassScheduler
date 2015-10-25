package data.scheedule.utexas;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.ComboBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringBuilder;
import data.scheedule.utils.StringUtils;

public class Course extends CourseBase {
	
	private Config getConfig() { return (Config) getBaseConfig(); }
			
	private String _courseCode;
	private String _courseName;
	
	private String _creditHours;
	private String _description;
	private String _departmentName;
	
	private boolean _isValid;
	
	private List<SectionGroup> _sections;
	
	private List<Course> _siblingCourses;
	private boolean _hasSiblings;
	
	public boolean hasSiblings() { return _hasSiblings; }
	public List<Course> getSiblings() { return _siblingCourses; }
	
	@Override public boolean isValid() { return _isValid; }
	
	@Override public String getCourseNumber() { return _courseCode; }
	@Override public String getCourseName() { return _courseName; }
	@Override public String getMinCreditHours() { return _creditHours; }
	@Override public String getMaxCreditHours() { return _creditHours; }
	@Override public String getDescription() { return _description; }
	
	@Override public List<SectionGroupBase> getSections() { return ArrayUtils.castAll(_sections, SectionGroupBase.class); } 
	@Override public List<ComboBase> getCombos() { 
		List<ComboBase> combos = new ArrayList<ComboBase>();
		for (SectionGroup section : _sections) {
			if (section.isValid()) {
				combos.add(new Combo(getConfig(), section));
			}
		}
		return combos;
	}
	
	
	
	public Course(Config config, String departmentName, HtmlTableRow headerRow, List<HtmlTableRow> sectionRows) {
		super(config);
		
		_departmentName = departmentName;
		
		parseHeader(departmentName, headerRow);
		parseSections(sectionRows);
	}
	
	
	private void parseHeader(String departmentName, HtmlTableRow headerRow) {
		List<HtmlElement> results = headerRow.getElementsByAttribute("span", "class", "title");
		
		if (results.size() == 0) {
			_isValid = false;
			return;
		}
		
		HtmlSpan span = (HtmlSpan) results.get(0);
		
		if (span == null) {
			_isValid = false;
			return;
		}
		
		String courseName = StringUtils.clean(span.asText());
		_courseName = StringUtils.toProperCase(courseName);
		_courseCode = StringUtils.clean(headerRow.asText().replace(courseName, "").replace(departmentName, ""));
		_creditHours = _courseCode.substring(0, 1);
		
		_isValid = true;
	}
	
	private void parseSections(List<HtmlTableRow> sectionRows) {
		_sections = new ArrayList<SectionGroup>();
		for (HtmlTableRow row : sectionRows) {
			_sections.add(new SectionGroup(getConfig(), row));
		}
		
		loadDescription();
	}
	
	public void merge(Course other) {
		getConfig().writeDebug("Merging: %s %s : '%s' with '%s'\n", _departmentName, _courseCode, _courseName, other.getCourseName());
		//if (_courseName.toLowerCase().equals(other.getCourseNumber().toLowerCase())) {
			for (SectionGroup section : other._sections) {
				_sections.add(section);
			}
			
			if (_description == null) {
				loadDescription();
			}
		/*}
		else {
			_hasSiblings = true;
			other._hasSiblings = true;
			if (_siblingCourses == null) _siblingCourses = new ArrayList<Course>();
			_siblingCourses.add(other);
			
			other.loadDescription();
		}*/
	}
	
	private void loadDescription() {
		SectionGroup firstSection = null;
		for (SectionGroup section : _sections) {
			if (section.isValid()) {
				firstSection = section;
				break;
			}
		}
		
		if (firstSection == null) return;
		
		// registrar/nrclav/ + link href
		try {
			HtmlPage page = getConfig().makeAuthenticatedRequest(getConfig().createAbsoluteUrl("/registrar/nrclav/" + firstSection.getSectionLink()));
			
			List<HtmlElement> results = page.getElementsByTagName("p");
			StringBuilder sb = new StringBuilder();
			
			for (HtmlElement ele : results) {
				HtmlParagraph p = (HtmlParagraph) ele;
				if (p != null && p.hasAttribute("class") && p.getAttribute("class").equalsIgnoreCase("space")) {
					sb.append(StringUtils.clean(p.asText()));
				}
			}
			
			_description = sb.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
