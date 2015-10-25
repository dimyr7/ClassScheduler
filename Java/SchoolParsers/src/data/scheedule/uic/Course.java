package data.scheedule.uic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.ComboBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

public class Course extends CourseBase {

	private String _departmentCode;
	
	private HtmlTableRow _headerRow;
	private List<HtmlTableRow> _sectionRows;
	
	private Map<String, SectionGroup> _sections;
	
	private List<Combo> _combos;
	
	private String _courseNumber;
	private String _courseName;
	
	private String _courseDescription;
	private String _courseInformation;
	
	private String _minHours;
	private String _maxHours;
	
	private boolean _isValid;
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	@Override public boolean isValid() { return _isValid; }
	@Override public String getCourseNumber() { return _courseNumber; }
	@Override public String getCourseName() { return _courseName; }
	@Override public String getMinCreditHours() { return _minHours; }
	@Override public String getMaxCreditHours() { return _maxHours; }
	@Override public String getDescription() { return _courseDescription; }
	
	@Override
	public List<SectionGroupBase> getSections() {
		return ArrayUtils.castAll(_sections.values(), SectionGroupBase.class);
	}
	
	@Override
	public List<ComboBase> getCombos() { 
		return ArrayUtils.castAll(_combos, ComboBase.class); 
	}
	
	
	
	public Course(Config config, String departmentCode, HtmlTableRow headerRow, List<HtmlTableRow> sectionRows) {
		super(config);
		_departmentCode = departmentCode;
		_headerRow = headerRow;
		_sectionRows = sectionRows;
		
		parseHeader();
		parseSections();
		generateCombos();
	}
	
	public static final String HeaderCourseInfo = "Course Information: ";
	public static final String HeaderCatalogDescription = "Catalog Description: ";
	private void parseHeader() {
		String headerText = StringUtils.clean(_headerRow.asText());
		
		int infoIndex = headerText.indexOf(HeaderCourseInfo);
		if (infoIndex < 0) {
			System.out.printf("Error parsing header for course. Unable to find '%s' in '%s'.\n", HeaderCourseInfo, headerText);
			_isValid = false;
			return;
		}
		
		int descriptionIndex = headerText.indexOf(HeaderCatalogDescription);
		if (descriptionIndex < 0) {
			_courseDescription = "";
			descriptionIndex = headerText.length() - 1;
		}
		else {
			_courseDescription = StringUtils.clean(headerText.substring(descriptionIndex + HeaderCatalogDescription.length()));
		}
		
		String courseName = headerText.substring(0, infoIndex).replace(_departmentCode, "").trim();
		_courseInformation = headerText.substring(infoIndex + HeaderCourseInfo.length(), descriptionIndex);
		// Course information contains info about combos?  Possibly.
		
		if (!courseName.contains(" - ")) {
			System.out.printf("Error parsing header for course. Unable to find ' - ' in %s of coursename.\n", courseName);
			_isValid = false;
			return;
		}
		_courseNumber = courseName.substring(0, courseName.indexOf(" - ")).trim();
		_courseName = courseName.replace(_courseNumber + " - ", "").trim(); 
		
		String hoursInfo = StringUtils.clean(_courseInformation.substring(0, _courseInformation.indexOf("hours")));
		
		if (hoursInfo.contains("OR") || hoursInfo.contains("TO")) {
			hoursInfo = hoursInfo.replace(" OR ", " ").replace(" TO ", " ");
			String[] parts = hoursInfo.split(" ");
			if (parts.length == 2) {
				_minHours = parts[0].trim();
				_maxHours = parts[1].trim();
			}
		}
		else {
			_minHours = hoursInfo;
			_maxHours = hoursInfo;
		}
		
		_isValid = true;
	}
	
	private void parseSections() {
		_sections = new HashMap<String, SectionGroup>();
		
		for (HtmlTableRow sectionRow : _sectionRows) {
			SectionGroup section = new SectionGroup(getConfig(), sectionRow);
			if (section.isValid()) {
				if (_sections.containsKey(section.getUniqueId())) 
					_sections.get(section.getUniqueId()).merge(section);
				else 
					_sections.put(section.getUniqueId(), section);
			}
		}
		
		// Make sure all the sections have names, even if they are just Type P#
		for (SectionGroup sectionGroup : _sections.values()) {
			int i = 0;
			String firstSectionName = null;
			for (Section section : sectionGroup.getSubSections()) {
				// If there is no section name, set them to Unique ID P# (UniqueID for the first one)
				if (section.getSectionName() == null) {
					if (i > 0) {
						section.setSectionName(String.format("%s P%d", sectionGroup.getUniqueId(), i));
					}
					else {
						section.setSectionName(sectionGroup.getUniqueId());
					}
				}
				else if (firstSectionName == null) {
					firstSectionName = section.getSectionName();
				}
				// If they have the same section names, set them to Name P#
				else if (section.getSectionName().equalsIgnoreCase(firstSectionName)) {
					section.setSectionName(String.format("%s P%d", section.getSectionName(), i));
				}
				
				i++;
			}
		}
	}
	
	private void generateCombos() {
		_combos = new ArrayList<Combo>();
		Combo current = null;
		for (HtmlTableRow sectionRow : _sectionRows) {
			String crn = SectionGroup.getSectionRowCrn(sectionRow);
			if ((crn != null) && _sections.containsKey(crn)) {
				if (sectionRow.getCell(0).asText().trim().length() > 0) {
					current = new Combo(getConfig());
					_combos.add(current);
				}
				current.add(_sections.get(crn));
			}
		}
	}
	
	
}
