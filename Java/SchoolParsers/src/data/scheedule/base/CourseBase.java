package data.scheedule.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.scheedule.utils.StringBuilder;
import data.scheedule.utils.XmlHelper;

/**
 * Base class for a Course. A Course should contain multiple Section Groups, and also contain some 
 * general information about the course, name, number, description, etc...
 * @author Mike Barker
 */
public abstract class CourseBase {

	/**********************************************************************
	 * Abstract Member Functions
	 **********************************************************************/
	
	/**
	 * @return Whether or not this course is valid
	 */
	public abstract boolean isValid();
	
	/**
	 * @return The number for the course, i.e. for Math 200, the '200' portion
	 */
	public abstract String getCourseNumber();
	
	/**
	 * @return The full, readable, name of the course, i.e. 'Calculus I'
	 */
	public abstract String getCourseName();
	
	/**
	 * @return The minimum number of credit hours of the course 
	 */
	public abstract String getMinCreditHours();
	
	/**
	 * @return The maximum number of credit hours of the course
	 */
	public abstract String getMaxCreditHours();
	
	/**
	 * @return A detailed description of what the course does (may contain HTML).
	 */
	public abstract String getDescription();
	
	
	/**
	 * @return List of Section Groups that the Course Contains
	 */
	public abstract List<SectionGroupBase> getSections();
	
	/**
	 * @return List of Combos for this Course
	 */
	public abstract List<ComboBase> getCombos();
	
	/**********************************************************************
	 * Members
	 **********************************************************************/
	
	private ConfigBase _config;
	public ConfigBase getBaseConfig() { return _config; }

	/**********************************************************************
	 * .ctor
	 **********************************************************************/
	
	protected CourseBase(ConfigBase config) { _config = config; }

	/**********************************************************************
	 * Public Functions
	 **********************************************************************/
	
	/**
	 * Converts this Course into it's XML Representation for the Scheedule
	 * XmlToDatabase parser.
	 * @return Xml representation of this course
	 */
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\t<Course>\n");

		sb.appendf("\t\t<CourseNumber>%s</CourseNumber>\n", XmlHelper.escapeXml(getCourseNumber()));
		sb.appendf("\t\t<Title>%s</Title>\n", XmlHelper.escapeXml(getCourseName()));
		
		sb.appendf("\t\t<MinHours>%s</MinHours>\n", XmlHelper.escapeXml(getMinCreditHours()));
		sb.appendf("\t\t<MaxHours>%s</MaxHours>\n", XmlHelper.escapeXml(getMaxCreditHours()));
		sb.appendf("\t\t<Description>%s</Description>\n", XmlHelper.escapeXml(getDescription()));
		
		for (SectionGroupBase section : getSections()) {
			if (section.isValid()) {
				sb.append(section.toXml());
			}
		}
		
		for (ComboBase combo : getCombos()) {
			if(combo.isValid()) {
				sb.append(combo.toXml());
			}
		}
		
		sb.append("\t</Course>\n");
		
		return sb.toString();
	}
	
	/***********************************************************************
	 * Helper Functions
	 ***********************************************************************/

	/**
	 * Generates a map of section groups by their accumlated types
	 * @param sectionGroups List of SectionGroups to generate a map section groups by type from
	 * @return Map by Type of Section Groups
	 */
	protected <T extends SectionGroupBase> Map<String, List<SectionGroupBase>> getSectionsByType(List<T> sectionGroups) {
		Map<String, List<SectionGroupBase>> sectionsByType = new HashMap<String, List<SectionGroupBase>>();
		for (SectionGroupBase sectionGroup : sectionGroups) {
			String type = sectionGroup.getType();
			if (!sectionsByType.containsKey(type)) {
				sectionsByType.put(type, new ArrayList<SectionGroupBase>());
			}
			sectionsByType.get(type).add(sectionGroup);
		}
		return sectionsByType;
	}
	
	/**
	 * Generates combinations of section groups based on the rule: 1 per type.
	 * This is the most common method
	 * @param sectionGroups List of Section Groups to generate combinations of section groups by
	 * @return List of combinations of section groups that are required for the course.
	 */
	protected <T extends SectionGroupBase> List<List<SectionGroupBase>> generateCombinations(List<T> sectionGroups) {
		return generateCombinations(getSectionsByType(sectionGroups));
	}
	
	/**
	 * Generates combinations of section groups based on the rule: 1 per type
	 * @param typesMap Map of Section Groups by Type
	 * @return List of combinations of section groups that are required for the course.
	 */
	protected List<List<SectionGroupBase>> generateCombinations(Map<String, List<SectionGroupBase>> typesMap) {
		List<List<SectionGroupBase>> combos = new ArrayList<List<SectionGroupBase>>();
		if (typesMap.size() == 0) {
			combos.add(new ArrayList<SectionGroupBase>());
			return combos;
		}
		
		List<SectionGroupBase> types = typesMap.remove(typesMap.keySet().toArray()[0]);
		List<List<SectionGroupBase>> otherCombos = generateCombinations(typesMap);
		for (SectionGroupBase section : types) {
			for (List<SectionGroupBase> otherCombo : otherCombos) {
				List<SectionGroupBase> newCombo = new ArrayList<SectionGroupBase>();
				newCombo.addAll(otherCombo);
				newCombo.add(section);
				combos.add(newCombo);
			}
		}
		return combos;
	}
}
