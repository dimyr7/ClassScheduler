package data.scheedule.msu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.ComboBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;

public class Course extends CourseBase {
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	private String _departmentCode;
	
	private HtmlAnchor _courseLink;
	
	private Map<String, SectionGroup> _sections;
	private List<Combo> _combos;
	
	private String _courseName;
	private String _courseNumber;
	private String _courseDescription;
	
	private String _minCredits;
	private String _maxCredits;
	
	private boolean _isValid;

	@Override public List<ComboBase> getCombos() { return ArrayUtils.castAll(_combos, ComboBase.class); }
	@Override public List<SectionGroupBase> getSections() { return ArrayUtils.castAll(_sections.values(), SectionGroupBase.class); }
	
	@Override public String getCourseName() { return _courseName; }
	@Override public String getCourseNumber() { return _courseNumber; }
	@Override public String getDescription() { return _courseDescription; }
	
	@Override public String getMinCreditHours() { return _minCredits; }
	@Override public String getMaxCreditHours() { return _maxCredits; }

	@Override public boolean isValid() { return _isValid; }
	
	public Course(Config config, HtmlTableRow headerRow, List<HtmlTableRow> sectionRows, String departmentCode) {
		super(config);
		
		_departmentCode = departmentCode;
		
		parseHeader(headerRow);
		parseSections(sectionRows);
		generateCombos();
		
		getConfig().writeDebug("Found Course: %s %s - %d sections.  Had %d section rows\n", _courseNumber, _courseName, _sections.size(), sectionRows.size());
		//System.out.printf("Found Course: %s %s - %d sections.  Had %d section rows\n", _courseNumber, _courseName, _sections.size(), _sectionRows.size());
	}
	
	private void parseHeader(HtmlTableRow headerRow) {
		if (headerRow.getCells().size() == 2) {
			_courseNumber = StringUtils.clean(headerRow.getCell(0).asText().replace(_departmentCode + " ", ""));
			_courseName = StringUtils.clean(headerRow.getCell(1).asText());
			
			_courseLink = (HtmlAnchor) headerRow.getCell(0).getElementsByTagName("a").get(0);
			parseCourseExtras();
			_isValid = true;
		}
		else {
			System.out.println("ERROR: Failed to parse course header.");
			_isValid = false;
		}
	}
	
	private void parseCourseExtras() {
		try {
			HtmlPage page = _courseLink.click();
			_courseDescription = getCellTextFor(page , "Description:");
			// TODO : Parse the Credits: section?
		} catch (Exception e) {}
	}
	
	private String getCellTextFor(HtmlPage page, String id) {
		HtmlTableCell cell = (HtmlTableCell) HtmlUnitHelper.getElementByText(page, id);
		if (cell != null) {
			return StringUtils.clean(((HtmlTableRow)cell.getParentNode()).getCell(1).asText());
		}
		return "";
	}
	
	private void parseSections(List<HtmlTableRow> allSectionRows) {
		_sections = new HashMap<String, SectionGroup>();

		double minCredits = 100.0;
		double maxCredits = 0.0;
		List<HtmlTableRow> sectionRows = new ArrayList<HtmlTableRow>();
		for (HtmlTableRow sectionRow : allSectionRows) {
			if (sectionRow.getCells().size() >= 11) {
				if (sectionRows.size() > 0) {
					SectionGroup section = new SectionGroup(getConfig(), sectionRows);
					_sections.put(section.getUniqueId(), section);
					
					if (section.getMinCredits() < minCredits) {
						minCredits = section.getMinCredits();
					}
					if (section.getMaxCredits() > maxCredits) {
						maxCredits = section.getMaxCredits();
					}
				}
				sectionRows = new ArrayList<HtmlTableRow>();
			}
			sectionRows.add(sectionRow);
		}
		
		if (sectionRows.size() > 0) {
			SectionGroup section = new SectionGroup(getConfig(), sectionRows);
			_sections.put(section.getUniqueId(), section);
			
			if (section.getMinCredits() < minCredits) {
				minCredits = section.getMinCredits();
			}
			if (section.getMaxCredits() > maxCredits) {
				maxCredits = section.getMaxCredits();
			}
		}
		
		_minCredits = Double.toString(minCredits);
		_maxCredits = Double.toString(maxCredits);
	}
	
	private void generateCombos() {
		_combos = new ArrayList<Combo>();
		
		for (SectionGroup section : _sections.values()) {
			_combos.add(new Combo(getConfig(), section));
		}
	}
	
}
