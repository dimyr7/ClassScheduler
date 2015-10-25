/**
 * Course.java
 * @copyright Scheedule, Inc.
 */

package data.scheedule.nu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.scheedule.base.ComboBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

/**
 * Represents a course found in the NW PDF
 */
public class Course extends CourseBase {
	
	public static final Pattern CourseLineRegex = Pattern.compile(" (\\d{3}-\\d{0,2}\\w{0,2}) (.+) (\\d\\.\\d)?\\s*(\\d\\.\\d)?");
	/* Example Course Lines:
	 * ' 316-0 Topics in Financial Economics RSTR DEPT  1.0'				--  #=316-0, Title=Topics in Financial Economics, Hours=1.0, RSTR DEPT should be ignored
	 * ' 385-2 Practicum: Building Pedagogical Theory & Content  1.0'		--  #=358-2, Title=Practicum: Building Pedagogical Theory & Content, Hours=1.0
	 * ' 499-0 Independent Study  1.0  3.0'									--  #=499-0, Title=Independent Study, MinHours=1.0, MaxHours=3.0
	 */
	
	private static final int CourseNumberGroup = 1;
	private static final int CourseTitleGroup = 2;
	private static final int MinCreditHoursGroup = 3;
	private static final int MaxCreditHoursGroup = 4;
	
	private String _courseNumber;
	private String _courseName;
	private String _minCreditHours;
	private String _maxCreditHours;
	private String _description;
		
	private boolean _isValid;
	
	private List<SectionGroup> _sectionGroups;
	private List<Combo> _combos;

	/**
	 * Creates a new instance of the Course for Northwestern
	 * @param config Config that is curring running
	 * @param match Match found while parsing the PDF for the Course regex
	 */
	public Course(Config config, Matcher match) {
		super(config);
		
		_sectionGroups = new ArrayList<SectionGroup>();
		_combos = new ArrayList<Combo>();
		
		extractDataFromMatcher(match);
	}
	
	/**
	 * Adds a section group to the course
	 * @param sectionGroup New section group to add to the course
	 */
	public void addSectionGroup(SectionGroup sectionGroup) {
		_sectionGroups.add(sectionGroup);
	}
	
	/**
	 * Appends the given string to the name of the course
	 * @param name Part of the course name to append to the current course name
	 */
	public void appendName(String name) {
		_courseName = (StringUtils.isNullOrEmpty(_courseName) ? "" : (_courseName + " ")) + getConfig().clean(name.trim());
	}
	
	/**
	 * Generates the combos for this course once all of the sections are done being added
	 */
	public void generateCombos() {
		Map<String, List<SectionGroup>> sectionsByType = new HashMap<String, List<SectionGroup>>();
		for (SectionGroup section : _sectionGroups) {
			if(section.isValid()) {
				String key = section.getSectionType();
				if (!sectionsByType.containsKey(key)) {
					sectionsByType.put(key, new ArrayList<SectionGroup>());
				}
				
				sectionsByType.get(key).add(section);
			}
		}
		
		List<List<SectionGroup>> combinedSections = getCombinations(sectionsByType);
		
		for (List<SectionGroup> sections : combinedSections) {
			_combos.add(new Combo(getConfig(), sections));
		}
	}
	
	/**
	 * @see CourseBase#getCourseNumber()
	 */
	@Override 
	public String getCourseNumber() { 
		return _courseNumber; 
	}
	
	/**
	 * @see CourseBase#getCourseName()
	 */
	@Override 
	public String getCourseName() { 
		return _courseName; 
	}
	
	/**
	 * @see CourseBase#getMinCreditHours()
	 */
	@Override 
	public String getMinCreditHours() { 
		return _minCreditHours; 
	}
	
	/**
	 * @see CourseBase#getMaxCreditHours()
	 */
	@Override 
	public String getMaxCreditHours() { 
		return _maxCreditHours; 
	}
	
	/**
	 * @see CourseBase#getDescription()
	 */
	@Override 
	public String getDescription() { 
		return _description; 
	}

	/**
	 * @see CourseBase#isValid()
	 */
	@Override 
	public boolean isValid() { 
		return _isValid; 
	}

	/**
	 * @see CourseBase#getSections()
	 */
	@Override 
	public List<SectionGroupBase> getSections() { 
		return ArrayUtils.castAll(_sectionGroups, SectionGroupBase.class); 
	}
	
	/**
	 * @see CourseBase#getCombos()
	 */
	@Override 
	public List<ComboBase> getCombos() { 
		return ArrayUtils.castAll(_combos, ComboBase.class); 
	}
	
	/**
	 * @return The config that is currently running
	 */
	private Config getConfig() { 
		return (Config)getBaseConfig(); 
	}
	
	/**
	 * Gets the combination of section groups, one of each type, from the mapping of type to list of sections with that type
	 * @param typesMap Mapping of type to list of section groups with the given type
	 * @return List of combination of section groups with one of each type section group 
	 */
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
	
	/**
	 * Extracts the data found in the regex matcher
	 * @param match The match found
	 */
	private void extractDataFromMatcher(Matcher match) {
		_courseNumber = match.group(CourseNumberGroup).trim();
		_courseName = getConfig().clean(match.group(CourseTitleGroup)).trim();
		String minCreditHours = match.group(MinCreditHoursGroup);
		String maxCreditHours = match.group(MaxCreditHoursGroup);
		_minCreditHours = minCreditHours != null ? minCreditHours.trim() : null;
		_maxCreditHours = maxCreditHours != null ? maxCreditHours.trim() : _minCreditHours;
				
		_isValid = true;

		getConfig().writeDebug("-- Course: %s (%s)\n", _courseNumber, _courseName);
	}
}
