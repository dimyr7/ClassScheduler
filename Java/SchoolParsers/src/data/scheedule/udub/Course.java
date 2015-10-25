package data.scheedule.udub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import data.scheedule.base.ComboBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

public class Course extends CourseBase {
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	private SectionKey _sectionKey;
	
	private HtmlTable _headerTable;
	private List<HtmlTable> _sectionTables;
	
	private HtmlAnchor _courseInfoLink;
	
	private String _departmentCode;
	
	private List<Combo> _combos;
	private List<SectionGroup> _sections;
	
	private String _courseName;
	private String _courseNumber;
	private String _description;
	
	private String _minCreditHours;
	private String _maxCreditHours;
	
	private boolean _isValid;
	
	@Override public String getCourseName() { return _courseName; }
	@Override public String getCourseNumber() { return _courseNumber; }
	@Override public String getDescription() { return _description; }
	
	@Override public String getMinCreditHours() { return _minCreditHours; }
	@Override public String getMaxCreditHours() { return _maxCreditHours; }

	@Override public List<SectionGroupBase> getSections() { return ArrayUtils.castAll(_sections, SectionGroupBase.class); }
	@Override public List<ComboBase> getCombos() { return ArrayUtils.castAll(_combos, ComboBase.class); }
	
	@Override public boolean isValid() { return _isValid; }
	
	public HtmlAnchor getCourseInfoLink()   { return _courseInfoLink; }
	public void setName(String name) 		{ _courseName = name;  }
	public void setDescription(String desc) { _description = desc; } 

	public Course(Config config, SectionKey key, HtmlTable header, List<HtmlTable> sections, String departmentCode) {
		super(config);
	
		_sectionKey = key;
		_headerTable = header;
		_sectionTables = sections;
		_departmentCode = departmentCode;
	
		parseHeader();
		parseSections();
		generateCombos();
	}
	
	private void parseHeader() {
		if (_headerTable.getRowCount() == 0 || _headerTable.getRow(0).getCells().size() == 0) {
			System.out.printf("Error finding info cell for course, header HTML: %s.\n", _headerTable.asXml());
			_isValid = false;
			return;
		}
		
		HtmlAnchor courseNumberInfo = (HtmlAnchor)_headerTable.getElementsByTagName("a").get(0);
		HtmlAnchor courseNameInfo = (HtmlAnchor) _headerTable.getElementsByTagName("a").get(1);
	
		_courseNumber = StringUtils.clean(courseNumberInfo.asText().replace(_departmentCode, ""));
		_courseName = StringUtils.toProperCase(StringUtils.clean(courseNameInfo.asText()));

		_courseInfoLink = courseNameInfo;
		_isValid = true;
		loadExtra();
	}
	
	private void loadExtra() {}
	
	private void parseSections() {
		_sections = new ArrayList<SectionGroup>();
		for (HtmlTable sectionTable : _sectionTables) {
			_sections.add(new SectionGroup(getConfig(), _sectionKey, sectionTable));
		}
	}
	
	private void generateCombos() {
		_combos = new ArrayList<Combo>();
		List<List<SectionGroup>> combinedSections = new ArrayList<List<SectionGroup>>();
		List<SectionGroup> lectures = new ArrayList<SectionGroup>();
		for (SectionGroup section : _sections) {
			if (section.getSectionType().equalsIgnoreCase("LEC")) {
				lectures.add(section);
			}
		}
		
		for (SectionGroup lecture : lectures) {
			Map<String, List<SectionGroup>> typesMap = new HashMap<String, List<SectionGroup>>();
			typesMap.put("LEC", new ArrayList<SectionGroup>());
			typesMap.get("LEC").add(lecture);
			String lectureSection = lecture.getSection();
			for (SectionGroup section : _sections) {
				String key = section.getSectionType();
				if (!key.equals("LEC") && section.getSection().equalsIgnoreCase(lectureSection)) {
					if (!typesMap.containsKey(key)) typesMap.put(key, new ArrayList<SectionGroup>());
					typesMap.get(key).add(section);
				}
			}
			
			combinedSections.addAll(getCombinations(typesMap));
		}
		
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
