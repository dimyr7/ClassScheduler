package data.scheedule.udub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlParagraph;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import data.scheedule.base.ConfigBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.DepartmentBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;

public class Department extends DepartmentBase {
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	private HtmlAnchor _departmentLink;
	
	private Map<String, Course> _courses;
	
	private String _departmentName;
	private String _departmentCode;

	@Override public List<CourseBase> getCourses() { return ArrayUtils.castAll(_courses.values(), CourseBase.class); }
	@Override public String getDepartmentCode() { return _departmentCode; }
	@Override public String getDepartmentName() { return _departmentName; }


	public Department(ConfigBase config, HtmlAnchor departmentLink) {
		super(config);
		_departmentLink = departmentLink;
		parseDepartmentInfo();
	}

	private void parseDepartmentInfo() {
		String departmentLink = StringUtils.clean(_departmentLink.asText()); 
		int codeStart = departmentLink.lastIndexOf('(') + 1;
		_departmentName = departmentLink.substring(0, codeStart - 2);
		_departmentCode = departmentLink.substring(codeStart).replace(")", "");
	}

	@Override
	public void loadCourses() {
		_courses = new HashMap<String, Course>();
		
		HtmlPage page = getConfig().makeRequest(getConfig().getRelativeScheduleUrl(_departmentLink.getHrefAttribute()));
		
		if (page == null) {
			System.out.printf("No courses found for %s\n", _departmentCode);
			return;
		}
		
		HtmlTable sectionKeyTable = (HtmlTable)HtmlUnitHelper.getElementByTagAndAttributeValue(page, "table", "bgcolor", "#d3d3d3", 0);
		if (sectionKeyTable == null) {
			System.out.printf("Failed to find the Section Key Table for Department %s\n", _departmentCode);
			return;
		}
		
		SectionKey sectionKey = new SectionKey(getConfig(), sectionKeyTable);
		HtmlTable header = null;
		List<HtmlTable> sections = new ArrayList<HtmlTable>();
		for (HtmlElement tbl : page.getElementsByTagName("table")) {
			HtmlTable table = (HtmlTable) tbl;
			if(table.hasAttribute("bgcolor") && table.getAttribute("bgcolor").equals("#d3d3d3")) {
				continue;	// Skip the key table
			}
			else if(table.hasAttribute("bgcolor")) {
				String[] headerColors = new String[]{
						"#ccffcc", // Green for Spring
						"#99ccff", // blue for winter
						"#ffffcc", // yellow for summer
						"#ffcccc"  // red for autumn
				};
				
				String bgColor = table.getAttribute("bgcolor");
				if(StringUtils.equalsAny(bgColor, headerColors)) {
					if (header != null) {
						Course course = new Course(getConfig(), sectionKey, header, sections, _departmentCode);
						_courses.put(course.getCourseNumber(), course);
					}
					
					header = table;
					sections = new ArrayList<HtmlTable>();
				}
				else {
					System.out.printf("Unknown table color: %s for department: %s\n", bgColor, _departmentCode);
				}
			}
			else if(header != null) {
				sections.add(table);
			}
		}
		
		if (header != null && sections.size() > 0) {
			Course course = new Course(getConfig(), sectionKey, header, sections, _departmentCode);
			_courses.put(course.getCourseNumber(), course);
		}
		
		loadCourseDescriptions();
	}
	
	private void loadCourseDescriptions() {
		HtmlAnchor anchor = null;
		for (Course c : _courses.values()) {
			if (c.getCourseInfoLink() != null) {
				anchor = c.getCourseInfoLink();
				break;
			}
		}
		
		if (anchor == null) return;
		try {
			HtmlPage page = anchor.click();
			List<HtmlAnchor> anchors = page.getAnchors();
			String dept = _departmentCode.toLowerCase();
			
			for (HtmlAnchor a : anchors) {
				String name = a.hasAttribute("name") ? a.getAttribute("name") : "";
				String number = name.replace(dept, "");
				if (name.startsWith(dept) && _courses.containsKey(number)) {
					HtmlParagraph p = (HtmlParagraph) a.getParentNode().getParentNode();
					if (p == null) continue;
					if (p.getChildNodes().size() >= 1) {
						String title = p.getChildNodes().get(0).asText();
						_courses.get(number).setName(StringUtils.clean(title.substring(0, title.indexOf('(') - 1).replace(a.asText(), "")));
						
						StringBuilder description = new StringBuilder();
						for (int i=2; i<p.getChildNodes().size(); i++) {
							description.append(p.getChildNodes().get(i).asText());
						}
						_courses.get(number).setDescription(StringUtils.clean(description.toString()));
					}
				}
			}
		} catch(Exception e) {}
	}
}
