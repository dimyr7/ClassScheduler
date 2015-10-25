package data.scheedule.nuold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.ComboBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;

public class Course extends CourseBase {
	
	private String _courseName;
	private String _courseNumber;
	private String _description;
	
	private String _minCreditHours;
	private String _maxCreditHours;
	
	private List<SectionGroup> _sections;
	private List<Combo> _combos;
	
	private HtmlTableRow _headerRow;
	private HtmlTableRow _sectionRow;
	
	private String _departmentCode;
	
	private boolean _isValid; 
	
	@Override public String getCourseName() { return _courseName; }
	@Override public String getCourseNumber() { return _courseNumber; }
	@Override public String getDescription() { return _description; }

	@Override public String getMinCreditHours() { return _minCreditHours; }
	@Override public String getMaxCreditHours() { return _maxCreditHours; }

	@Override public List<SectionGroupBase> getSections() { return ArrayUtils.castAll(_sections, SectionGroupBase.class); }
	@Override public List<ComboBase> getCombos() { return ArrayUtils.castAll(_combos, ComboBase.class); }

	@Override public boolean isValid() { return _isValid; }	
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	

	public Course(Config config, HtmlTableRow headerRow, HtmlTableRow sectionRow, String departmentCode) {
		super(config);
		
		_headerRow = headerRow;
		_sectionRow = sectionRow;
		
		_departmentCode = departmentCode;
		
		_sections = new ArrayList<SectionGroup>();
		_combos = new ArrayList<Combo>();
		
		parseHeader();
		parseSections();
		generateCombos();
		
		if (_isValid) {
			//System.out.printf("Found Course: %s - %s with %d combos\n", _courseNumber, _courseName, _combos.size());
		}
	}
	
	
	private void parseHeader() {
		String headerText = StringUtils.clean(_headerRow.asText()).replace(_departmentCode.toUpperCase(), "");
		
		int index = headerText.indexOf(" - ");
		if (index < 0) {
			_isValid = false;
			System.out.printf("Error parsing Course info for %s\n", headerText);
			return;
		}
		
		_courseNumber = StringUtils.clean(headerText.substring(0, index));
		_courseName = StringUtils.clean(headerText.substring(index + 3));
		
		if (HtmlUnitHelper.getElementByTagAndAttributeValue(_sectionRow, "img", "title", "Show next row (inactive button) (Alt+.)") == null) {
			System.out.printf("Course %s - %s has suspected section pagination...\n", _courseNumber, _courseName);
		}
	}
	
	private void parseSections() {
		HtmlTable tbl = (HtmlTable) HtmlUnitHelper.getElementByTagAndAttributeValue(_sectionRow, "table", "class", "PSLEVEL1SCROLLAREABODY");
		
		if (tbl == null) {
			_isValid = false;
			System.out.printf("Error finding sectiont able for course: %s\n", _courseNumber);
			return;
		}
				
		List<HtmlTableRow> sectionRows = new ArrayList<HtmlTableRow>();
		for (HtmlTableRow row : tbl.getRows()) {
			if (sectionRows.size() > 0 && HtmlUnitHelper.getElementByTagAndAttributeValue(row, "hr", "class", "PSLEVEL1SCROLLAREABODY") != null) {
				SectionGroup section = new SectionGroup(getConfig(), sectionRows);
				if (section.isValid()) {
					_sections.add(section);
				}
				sectionRows.clear();
			}
			else {
				sectionRows.add(row);
			}
		}
		if (sectionRows.size() > 0) {
			SectionGroup section = new SectionGroup(getConfig(), sectionRows);
			if (section.isValid()) {
				_sections.add(section);
			}
		}
		
		_isValid = true;
	}
	
	
	
	private void generateCombos() {
		Map<String, List<SectionGroup>> sectionsByType = new HashMap<String, List<SectionGroup>>();
		for (SectionGroup section : _sections) {
			String key = section.getSectionType();
			if (!sectionsByType.containsKey(key)) sectionsByType.put(key, new ArrayList<SectionGroup>());
			sectionsByType.get(key).add(section);
		}
		
		List<List<SectionGroup>> combinedSections = getCombinations(sectionsByType);
		
		for (List<SectionGroup> sections : combinedSections) {
			_combos.add(new Combo(getConfig(), sections));
		}
	}
	
	private List<List<SectionGroup>> getCombinations(Map<String, List<SectionGroup>> typesMap) {
		List<List<SectionGroup>> combos = new ArrayList<List<SectionGroup>>();
		if (typesMap.size() == 0) {
			combos.add(new ArrayList<SectionGroup>());
			return combos;
		}
		
		List<SectionGroup> types = typesMap.remove(typesMap.keySet().toArray()[0]);
		List<List<SectionGroup>> otherCombos = getCombinations(typesMap);
		for (SectionGroup section : types) {
			for (List<SectionGroup> otherCombo : otherCombos) {
				List<SectionGroup> newCombo = new ArrayList<SectionGroup>();
				newCombo.addAll(otherCombo);
				newCombo.add(section);
				combos.add(newCombo);
			}
		}
		return combos;
	}
	
}
