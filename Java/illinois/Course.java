package data.scheedule.illinoisold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.ComboBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;
import data.scheedule.utils.XmlHelper;

public class Course extends CourseBase {

	/////////////////////////////////////////////////////////////////////
	//// Members

	private String _courseName;
	private String _courseNumber;
	private String _description;
	private String _minCreditHours;
	private String _maxCreditHours;
	private boolean _isValid;

	private List<SectionGroup> _sectionGroups;
	private List<Combo> _combos;
	private List<Section> _sections;
	
	@Override public String getCourseName() { return _courseName; }
	@Override public String getCourseNumber() { return _courseNumber; }
	@Override public String getDescription() { return _description; }
	@Override public String getMinCreditHours() { return _minCreditHours; }
	@Override public String getMaxCreditHours() { return _maxCreditHours; }
	@Override public List<SectionGroupBase> getSections() { return ArrayUtils.castAll(_sectionGroups, SectionGroupBase.class); }
	@Override public List<ComboBase> getCombos() { return ArrayUtils.castAll(_combos, ComboBase.class); }
	@Override public boolean isValid() { return _isValid; }

	private Config getConfig() { return (Config)getBaseConfig(); }
	
	private String _departmentCode;
	
	private static Pattern _hoursRegex = Pattern.compile("(\\d+(\\.\\d+)?)");
	
	//////////////////////////////////////////////////////////////////////////////////
	//// .ctor
	
	public Course(Config config, String departmentCode, Element courseRow) {
		super(config);
		
		_sections = new ArrayList<Section>();
		_sectionGroups = new ArrayList<SectionGroup>();
		_combos = new ArrayList<Combo>();
		_isValid = false;
		
		_departmentCode = departmentCode;
		
		parseCourseRow(courseRow);
				
		if (_isValid) {
			generateCombos();
			
			if (_combos.size() == 0) {
				getConfig().writeDebug("********** WARNING: Course %s %s generated 0 combos.\n", _departmentCode, _courseNumber);
			}
		}
	}
	
	
	private void parseCourseRow(Element courseRow) {	
		_courseNumber = XmlHelper.getTextValue(courseRow, "courseNumber");
		_courseName = XmlHelper.getTextValue(courseRow, "title");
		
		_description = parseDescription(courseRow);
		parseCreditHours(courseRow);
		parseSections(courseRow);
		
		_isValid = _courseNumber != null && _courseName != null 
						&& _description != null 
						&& _minCreditHours != null && _maxCreditHours != null 
						&& _sections.size() > 0 && _sectionGroups.size() > 0;
	}
	
	private String parseDescription(Element courseRow) {
		// Build the description from a few more specific fields
		String description = XmlHelper.getTextValue(courseRow, "description");  // A lot of times, this already contains the information in the more specific fields.  But not always...
		
		String genEdCategory = XmlHelper.getTextValue(courseRow, "generalEducationCategory");
		String sectionDegreeAttributes = XmlHelper.getTextValue(courseRow, "sectionDegreeAttributes"); // E.g. "Discover course."; Might also == GenEd category
		String courseSectionInformation = XmlHelper.getTextValue(courseRow, "courseSectionInformation"); // E.g. "Prerequisite: ..."; "Same as..."
		String sectionCappArea = XmlHelper.getTextValue(courseRow, "sectionCappArea");  // E.g. "Restricted to first time freshman." 
		String sectionRegistrationNotes = XmlHelper.getTextValue(courseRow, "sectionRegistrationNotes"); // E.g. "Restricted to graduate students.  Urbana-champaign."
		String sectionApprovalCode = XmlHelper.getTextValue(courseRow, "sectionApprovalCode"); // E.g. "Department approval required."
		if(sectionDegreeAttributes != null && sectionDegreeAttributes != "" && !description.contains(sectionDegreeAttributes) && !genEdCategory.equals(sectionDegreeAttributes)) {
			description += " " + sectionDegreeAttributes;
		}
		if(courseSectionInformation != null && courseSectionInformation != "" && !description.contains(courseSectionInformation)) {
			description += " " + courseSectionInformation;
		}
		if(sectionCappArea != null && sectionCappArea != "" && !description.contains(sectionCappArea)) {
			description += " " + sectionCappArea;
		}
		if(sectionRegistrationNotes != null && sectionRegistrationNotes != "" && !description.contains(sectionRegistrationNotes)) {
			description += " " + sectionRegistrationNotes;
		}
		if(sectionApprovalCode != null && sectionApprovalCode != "" && !description.contains(sectionApprovalCode)) {
			description += " " + sectionApprovalCode;
		}
		
		return description;
	}
	
	private void parseCreditHours(Element courseRow) {
		// Parse the Hours data out of the string
		Matcher hoursMatcher = _hoursRegex.matcher(XmlHelper.getTextValue(courseRow, "hours"));
		if(hoursMatcher.find())
		{
			_minCreditHours = hoursMatcher.group();
			_maxCreditHours = _minCreditHours;
		}
		if(hoursMatcher.find())
		{
			_maxCreditHours = hoursMatcher.group();
		}
	}
	
	private void parseSections(Element courseRow) {
		boolean mergeLcdLabLbd = (_departmentCode.equalsIgnoreCase("MCB") && _courseNumber.equals("334"))
									|| (_departmentCode.equalsIgnoreCase("ABE") && _courseNumber.equals("440")) 
									|| (_departmentCode.equalsIgnoreCase("ANSC") && _courseNumber.equals("440"))
									|| (_departmentCode.equalsIgnoreCase("CPSC") && _courseNumber.equals("440"));

		// Get the section data out of the xml
		NodeList sectionElements = courseRow.getElementsByTagName("section");
		Section lastSection = null;
		for(int s =  0; s < sectionElements.getLength(); s++) {
			Element sectionElement = (Element)sectionElements.item(s);
			Section section = new Section(getConfig(), sectionElement);
			
			if (StringUtils.isNullOrEmpty(section.getCrn()) && lastSection != null) {
				section.setCrn(lastSection.getCrn());
			}
			if (StringUtils.isNullOrEmpty(section.getSectionName()) && lastSection != null) {
				section.setSectionName(lastSection.getSectionName());
			}
			
			boolean isLcdLabLbc = section.getSectionType().equalsIgnoreCase("LAB")
									|| section.getSectionType().equalsIgnoreCase("LBD")
									|| section.getSectionType().equalsIgnoreCase("LCD");
			if (mergeLcdLabLbd && isLcdLabLbc) {
				section.setSectionType("LCD");
			}
			
			if (section.isValid() && !section.getCrn().equalsIgnoreCase("PEND")) {
				_sections.add(section);
			}
		}
		
		// Merge sections into groups by CRN
		for (Section section : _sections) {
			SectionGroup containerGroup = null;
			for (SectionGroup sectionGroup : _sectionGroups) {
				if (sectionGroup.getUniqueId().equalsIgnoreCase(section.getCrn()) && !section.getCrn().equalsIgnoreCase("PEND")) {
					containerGroup = sectionGroup;
					break;
				}
			}
			
			if (containerGroup == null) {
				containerGroup = new SectionGroup(getConfig());
				_sectionGroups.add(containerGroup);
			}
			containerGroup.addSection(section);
		}
	}
	
	
	private void generateCombos() {
		// Populate the group names
		for (SectionGroup sectionGroup : _sectionGroups) {
			sectionGroup.populateGroupByNames(_departmentCode, _courseNumber);
		}
		
		Map<String, List<SectionGroup>> groupedSectionsByName = new HashMap<String, List<SectionGroup>>();
		
		for(SectionGroup groupedSection : _sectionGroups) {
			String groupByName = ((Section)groupedSection.getSections().get(0)).getGroupByName();		
			if(!groupedSectionsByName.containsKey(groupByName)) {
				groupedSectionsByName.put(groupByName, new ArrayList<SectionGroup>());
			}
		
			groupedSectionsByName.get(groupByName).add(groupedSection);
		}
		
		List<Combo> combos = new ArrayList<Combo>();
		for(Map.Entry<String, List<SectionGroup>> pair : groupedSectionsByName.entrySet()) {
			combos.addAll(addCombosForGroups(combos, pair.getValue()));
		}
		
		List<String> existingIds = new ArrayList<String>();
		// De-dupe.  Dupes can happen because of linked sections :(
		for(Combo combo : combos) {
			List<Integer> orderedSectionGroupIds = new ArrayList<Integer>();
			for(SectionGroupBase groupedSection : combo.getSections()) {
				int insertIndex = 0;
				for(Integer orderedSectionGroupId : orderedSectionGroupIds) {
					if(orderedSectionGroupId > groupedSection.getSectionId()) {
						break;
					}
					insertIndex++;
				}
				orderedSectionGroupIds.add(insertIndex, groupedSection.getSectionId());
			}
			
			String comboId = "";
			for(Integer i : orderedSectionGroupIds) {
				comboId += i.toString() + "|";
			}

			if(!existingIds.contains(comboId)) {
				existingIds.add(comboId);
				_combos.add(combo);
			}	
		}	
	}
	
	

	private List<Combo> addCombosForGroups(List<Combo> combos, List<SectionGroup> groupedSections) {
		Map<String, List<SectionGroup>> sectionsByType= new HashMap<String, List<SectionGroup>>();
		
		for(SectionGroup groupedSection : groupedSections) {			
			String groupType = "group";
			if(groupedSection.getSections().size() == 1) {
				groupType = groupedSection.getSections().get(0).getSectionType();
			}
			
			if(!sectionsByType.containsKey(groupType)) {
				sectionsByType.put(groupType, new ArrayList<SectionGroup>());
			}
			sectionsByType.get(groupType).add(groupedSection);
		}
		
		List<Combo> localCombos = new ArrayList<Combo>();
		for(Map.Entry<String, List<SectionGroup>> pair : sectionsByType.entrySet()) {
			localCombos = crossJoinSections(localCombos, pair.getValue(), groupedSections);
		}
		
		return localCombos;
	}
	
	private List<Combo> crossJoinSections(List<Combo> existingCombos, List<SectionGroup> groupedSections, List<SectionGroup> allGroupedSections)
	{
		List<Combo> joinedCombos = new ArrayList<Combo>();
		
		if(existingCombos.isEmpty()) {
			for(SectionGroup groupedSection : groupedSections) {
				Combo newCombo = new Combo(getConfig());
				newCombo.tryAdd(groupedSection, allGroupedSections);
				joinedCombos.add(newCombo);
			}
		}
		else {
			
			for(Combo combo : existingCombos) {
				for(SectionGroup groupedSection : groupedSections) {
					Combo comboClone = new Combo(combo);
					if(comboClone.tryAdd(groupedSection, allGroupedSections)) {
						joinedCombos.add(comboClone);
					}
				}
			}
		}
		
		return joinedCombos;
	}
}
