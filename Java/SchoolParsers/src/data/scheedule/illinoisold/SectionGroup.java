package data.scheedule.illinoisold;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;

public class SectionGroup extends SectionGroupBase {
	
	////////////////////////////////////////////////////////////////////
	//// Members
	
	private List<Section> _sections;
	private String _crn;
	private boolean _isClosed;
	private boolean _isValid;
	
	@Override public List<SectionBase> getSections() { return ArrayUtils.castAll(_sections, SectionBase.class); }
	@Override public String getUniqueId() { return _crn; }
	@Override public boolean isClosed() { return _isClosed; }
	@Override public boolean isValid() { return _isValid; }
	
	private Config getConfig() { return (Config)getBaseConfig(); }
			
	///////////////////////////////////////////////////////////////////
	//// .ctor
	
	public SectionGroup(Config config) {
		super(config);
		
		_isValid = false;
		_sections = new ArrayList<Section>();
	}
	
	
	///////////////////////////////////////////////////////////////////
	//// Public Functions
	
	public void populateGroupByNames(String departmentCode, String courseNumber) {
		int fakeGroupById = 0;
		boolean onlyOneType = true;
		boolean allOneCharacter = true;
		boolean firstCharacterIsType = true;
		boolean atLeastOneMultipleType = false;
		boolean isSpecialTopics = (courseNumber.endsWith("98") || courseNumber.endsWith("99") 
									|| (departmentCode.equalsIgnoreCase("CS") && courseNumber.equals("591"))
									|| (departmentCode.equalsIgnoreCase("ECE") && courseNumber.equals("590"))
									|| (departmentCode.equalsIgnoreCase("DANC") && courseNumber.equals("223"))
									|| (departmentCode.equalsIgnoreCase("EDPR") && courseNumber.equals("420"))
									|| (departmentCode.equalsIgnoreCase("EDPR") && courseNumber.equals("438"))
									|| (departmentCode.equalsIgnoreCase("EDPR") && courseNumber.equals("442"))
									|| (departmentCode.equalsIgnoreCase("DANC") && courseNumber.equals("423"))
									|| (departmentCode.equalsIgnoreCase("THEA") && courseNumber.equals("550"))
									|| (departmentCode.equalsIgnoreCase("THEA") && courseNumber.equals("591")))
									&& !((departmentCode.equalsIgnoreCase("VCM") && courseNumber.equals("698")));
		
		Map<String, String> typeToStartingChar = new HashMap<String, String>();
		String firstType = null;
		for (Section section : _sections) {
			if (!section.getSectionName().isEmpty()) {
				String sectionNameFirstChar = section.getSectionName().substring(0,1);
				if(!typeToStartingChar.containsKey(section.getSectionType())) {
					// Also make sure no types start with the same letter
					firstCharacterIsType = firstCharacterIsType && !typeToStartingChar.containsValue(sectionNameFirstChar);
					
					typeToStartingChar.put(section.getSectionType(), sectionNameFirstChar);
				}
				else {
					atLeastOneMultipleType = true;
					firstCharacterIsType = firstCharacterIsType && typeToStartingChar.get(section.getSectionType()).equalsIgnoreCase(sectionNameFirstChar);
				}
			}
			
			if(firstType == null) {
				firstType = section.getSectionType();
			}
			else {
				onlyOneType = onlyOneType && (firstType.equalsIgnoreCase(section.getSectionType()));
			}
			allOneCharacter = allOneCharacter && section.getSectionName().length() == 1;
		}
		
		firstCharacterIsType = firstCharacterIsType && atLeastOneMultipleType && !isSpecialTopics;
		
		for(Section section : _sections)
		{
			String sectionName = section.getSectionName().toUpperCase();
			String groupByName = sectionName.length() > 0 ? sectionName.substring(0, 1) : "";
			groupByName = isSpecialTopics ? sectionName : groupByName;
			if(departmentCode.equalsIgnoreCase("MATH") && (sectionName.endsWith("8") || sectionName.endsWith("H")))
			{
				groupByName = sectionName;
			}
					
			if(groupByName.equals(""))
			{
				groupByName = "F" + fakeGroupById;
				fakeGroupById++;
			}
			// For physics, the section names are weird but they all can go together
			if(firstCharacterIsType)
			{
				groupByName = "ALL";
			}
			// Make sure online sections don't get included in normal combos
			if(section.getSectionType().equalsIgnoreCase("ONL") && !departmentCode.equalsIgnoreCase("CHEM"))
			{
				groupByName = "I-" + groupByName;
			}
			// Allow grad and undergrad to combine
			if(sectionName.startsWith("GR") || sectionName.matches("^G\\d") || sectionName.equalsIgnoreCase("G"))
			{
				groupByName = "U";
			}
			// If the group name starts with a #, just ignore it.
			if(sectionName.matches("$\\d.*^"))
			{
				groupByName = "";
			}
			// If there is only 1 section type and all sections are 1 letter, don't group
			if(!isSpecialTopics && onlyOneType && (allOneCharacter || departmentCode.equalsIgnoreCase("CHEM") && !departmentCode.equalsIgnoreCase("RST")))
			{
				groupByName = "";
			}

			section.setGroupByName(groupByName);
		}
	}
	
	public void addSection(Section section) {
		if (_crn != null) {
			_crn = section.getCrn();
			_isValid = true;
		}
		_sections.add(section);
	}
}
