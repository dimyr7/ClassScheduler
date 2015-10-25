package data.scheedule.stanford;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

import data.scheedule.base.ComboBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;

public class Course extends CourseBase {
	
	//////////////////////////////////////////////////////////
	//// Members
	
	private String _courseName;
	private String _courseNumber;
	private String _courseDescription;
	private String _minCredits;
	private String _maxCredits;
	
	// Non-used
	private String _instructors;	// semi-colon seperated list of instructors
	private String _grading;		// special instructions on grading
	private String _ugReqs;			// undergraduate requirements
	private String _terms;			// comma seperated list of terms
		
	private boolean _isValid;
	
	
	private List<Combo> _combos;
	private List<SectionGroup> _sections;

	@Override public List<ComboBase> getCombos() { return ArrayUtils.castAll(_combos, ComboBase.class); }
	@Override public List<SectionGroupBase> getSections() { return ArrayUtils.castAll(_sections, SectionGroupBase.class); }

	@Override public String getCourseName() { return _courseName; }
	@Override public String getCourseNumber() { return _courseNumber; }
	@Override public String getDescription() { return _courseDescription; }
	@Override public String getMinCreditHours() { return _minCredits; }
	@Override public String getMaxCreditHours() {  return _maxCredits; }
	
	@Override public boolean isValid() { return _isValid; }
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	private String _department;
	
	//////////////////////////////////////////////////////////////
	//// .ctor
	
	public Course(Config config, String department, HtmlElement searchResult) {
		super(config);
		_combos = new ArrayList<Combo>();
		_sections = new ArrayList<SectionGroup>();
		
		_department = department;
		_isValid = false;
		parseSearchResultDiv(searchResult);
	}
	
	
	private void parseSearchResultDiv(HtmlElement searchResult) {
		_isValid = parseHeader(HtmlUnitHelper.getElementByTagName(searchResult, "h2", 0))
					&& parseDescription(HtmlUnitHelper.getElementsByTagAndAttributeValue(searchResult, "div", "class", "courseDescription"))
					&& parseCourseAttributes(HtmlUnitHelper.getElementsByTagAndAttributeValue(searchResult, "div", "class", "courseAttributes"))
					&& parseSectionInfo(HtmlUnitHelper.getElementByTagAndAttributeValue(searchResult, "div", "class", "sectionInfo"))
					&& generateCombos();
	}
	
	private boolean parseHeader(HtmlElement header) {
		if (header == null) return false;
		
		List<HtmlElement> spans = header.getElementsByTagName("span");
		if (spans.size() != 2) return false;
		
		_courseNumber = StringUtils.clean(spans.get(0).asText().replace(_department, "").replace(":", ""));
		_courseName = StringUtils.clean(spans.get(1).asText());
		
		return true;
	}

	
	private boolean parseDescription(List<HtmlElement> descriptions) {
		if (descriptions.size() == 0) return false;
		
		// The last one should be the full description, if there are multiple.
		_courseDescription = StringUtils.clean(descriptions.get(descriptions.size() - 1).asText());
		
		return true;
	}
	
	private boolean parseCourseAttributes(List<HtmlElement> attributes) {
		if (attributes.size() == 0) return false;
		
		Map<String, String> attrs = new HashMap<String, String>();
		for (HtmlElement attribute : attributes) {
			String[] parts = attribute.asText().split("\\|");
			for (String part : parts) {
				String[] kvParts = part.split("\\: ");
				if (kvParts.length == 2) {
					attrs.put(kvParts[0].trim().toLowerCase(), kvParts[1].trim());
				}
			}
		}
		
		_instructors = attrs.get("instructors");
		_terms = attrs.get("terms");
		_ugReqs = attrs.get("ug reqs");
		_grading = attrs.get("grading");
		parseCredits(attrs.get("units"));
		
		return true;
	}
	
	private void parseCredits(String credits) {
		if (credits == null) return;
		
		String[] parts = credits.split("\\-");
		if (parts.length == 2) {
			_minCredits = StringUtils.clean(parts[0]);
			_maxCredits = StringUtils.clean(parts[1]);
		}
		else {
			_minCredits = StringUtils.clean(credits);
			_maxCredits = StringUtils.clean(credits);
		}		
	}
	
	private boolean parseSectionInfo(HtmlElement sectionInfo) {
		if (sectionInfo == null) return false;
				
		List<HtmlElement> sections = HtmlUnitHelper.getElementsByTagAndAttributeValue(sectionInfo, "li", "class", "sectionDetails");
		for (HtmlElement sectionDetails : sections) {
			SectionGroup group = new SectionGroup(getConfig(), sectionDetails);
			if (group.isValid()) {
				_sections.add(group);
			}
			else {
				getConfig().writeDebug("Failed to parse section group for course: %s - %s\n", _department, _courseNumber);
			}
		}
		
		return true;
	}
	
	private boolean generateCombos() {
		List<List<SectionGroup>> combos = getCombinations(getSectionsByType());
		
		for (List<SectionGroup> combo : combos) {
			_combos.add(new Combo(getConfig(), combo));
		}
				
		return true;
	}
	
	private Map<String, List<SectionGroup>> getSectionsByType() {
		Map<String, List<SectionGroup>> sectionsByType = new HashMap<String, List<SectionGroup>>();
		for (SectionGroup section : _sections) {
			if (!sectionsByType.containsKey(section.getType())) {
				sectionsByType.put(section.getType(), new ArrayList<SectionGroup>());
			}
			sectionsByType.get(section.getType()).add(section);
		}
		return sectionsByType;
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
