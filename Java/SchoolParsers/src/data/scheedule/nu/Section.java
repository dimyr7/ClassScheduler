/**
 * Section.java
 * @copyright Scheedule, Inc.
 */
package data.scheedule.nu;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import data.scheedule.base.SectionBase;
import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.common.Days.Day;
import data.scheedule.utils.StringUtils;

/**
 * This class represents a Section for Northwestern
 */
public class Section extends SectionBase {

	public static final Pattern SectionLineRegex = Pattern.compile(" (\\d+) (\\d+\\w*) (\\w+) (\\w+)?\\s*(\\w+)?\\s*(TBA|(\\d{1,2}:\\d{2}(\\w{2})?))?\\s*(TBA|(\\d{1,2}:\\d{2}(\\w{2})?))?\\s*(\\w+)?.*");
	/* Example Section Lines:
	 * ' 20240 20 LEC CLRKB01 TTh  2:00  3:20 Andersen RSTR DEPT'		-- UniqueId=20240, Name=20, Type=LEC, Building=Clark, Room#=B01, Days=Tuesday/Thursday, StartTime=1400, EndTime=1520, Instructor=Andersen, Ignore RSTR DEPT
	 * ' 26155 21 LEC TBA TBA Egerton-Warburton *I'						-- UniqueId=26155, Name=21, Type=LEC, Building=N/A, Days=TBA, Times=TBA, Instructor=Egerton-Warburton
	 */
		
	public static final int UniqueIdGroup = 1;
	private static final int SectionNameGroup = 2;
	private static final int SectionTypeGroup = 3;
	private static final int BuildingInfoGroup = 4;
	private static final int DaysGroup = 5;
	private static final int StartTimeGroup = 6;
	private static final int EndTimeGroup = 9;
	private static final int InstructorGroup = 12;
	
	private static final Pattern TimeRegex = Pattern.compile("(\\d{1,2}):(\\d{2})(\\w{2})?");
	private static final int HourGroup = 1;
	private static final int MinuteGroup = 2;
	private static final int AMPMGroup = 3;
	
	private String _sectionName;
	private String _sectionType;
	private Times _times;
	private Days _days;
	private String _startDate;
	private String _endDate;
	private String _building;
	private String _roomNumber;
	private String _instructors;
	private String _description;
	
	private boolean _isValid;
	
	/**
	 * Creates a new instance of a Section for Northwestern
	 * @param config The config that is currently running
	 * @param match Match found while parsing the PDF for the Section regex
	 */
	public Section(Config config, Matcher match) {
		super(config);
		extractDataFromMatch(match);
	}
	
	/**
	 * Appends anothter instructor to the list of instructors found for this section
	 * @param instructor New instructor found for this section
	 */
	public void appendInstructor(String instructor) {
		_instructors = (_instructors != null ? _instructors + ";" : "") + instructor;
	}
	
	/**
	 * @see SectionBase#getSectionName()
	 */
	@Override 
	public String getSectionName() { 
		return _sectionName; 
	}
	
	/**
	 * @see SectionBase#getSectionType()
	 */
	@Override 
	public String getSectionType() { 
		return _sectionType;
	}
	
	/**
	 * @see SectionBase#getTimes()
	 */
	@Override 
	public Times getTimes() { 
		return _times; 
	}

	/**
	 * @see SectionBase#getDays()
	 */
	@Override 
	public Days getDays() { 
		return _days; 
	}
	
	/**
	 * @see SectionBase#getStartDate()
	 */
	@Override 
	public String getStartDate() { 
		return _startDate; 
	}
	
	/**
	 * @see SectionBase#getEndDate()
	 */
	@Override public String getEndDate() { 
		return _endDate;
	}
	
	/**
	 * @see SectionBase#getBuilding()
	 */
	@Override public String getBuilding() { 
		return _building; 
	}
	
	/**
	 * @see SectionBase#getRoomNumber()
	 */
	@Override public String getRoomNumber() { 
		return _roomNumber; 
	}
	
	/**
	 * @see SectionBase#getInstructor()
	 */
	@Override public String getInstructor() { 
		return _instructors; 
	}
	
	/**
	 * @see SectionBase#getDescription()
	 */
	@Override public String getDescription() { 
		return _description;
	}
	
	/**
	 * @see SectionBase#isValid()
	 */
	@Override public boolean isValid() { 
		return _isValid; 
	}
	
	/**
	 * @return The current config that is running
	 */
	private Config getConfig() { 
		return (Config)getBaseConfig(); 
	}
	
	/**
	 * Extracts data from the Regex match
	 * @param match Match found while parsing the PDF for the Section regex
	 */
	private void extractDataFromMatch(Matcher match) {
		_sectionName = match.group(SectionNameGroup).trim();
		_sectionType = match.group(SectionTypeGroup).trim();
		
		getConfig().writeDebug("-- Section: %s type=%s\n", _sectionName, _sectionType);
		
		parseBuildingInfo(match.group(BuildingInfoGroup));
		parseDays(match.group(DaysGroup));
		parseTimes(match.group(StartTimeGroup), match.group(EndTimeGroup));
		_instructors = match.group(InstructorGroup);
		
		_isValid = !StringUtils.isNullOrEmpty(_sectionName) && !StringUtils.isNullOrEmpty(_sectionType);
	}
	
	/**
	 * Attempts to extract the building name and room number from the building info string
	 * @param buildingInfo String possibly containing building info
	 */
	private void parseBuildingInfo(String buildingInfo) {
		if(StringUtils.isNullOrEmpty(buildingInfo) || buildingInfo.trim().equalsIgnoreCase("TBA")) {
			return;
		}
		
		Map<String, String> buildingAbbreviations = getConfig().getBuildingAbbreviations();
		for(String buildingAbbrev : buildingAbbreviations.keySet()) {
			if(buildingInfo.contains(buildingAbbrev)) {
				_building = buildingAbbreviations.get(buildingAbbrev).trim();
				_roomNumber = buildingInfo.replace(buildingAbbrev, "").trim();
				getConfig().writeDebug("-- Parsed Building Info: '%s' => %s %s\n", buildingInfo, _building, _roomNumber);
				return;
			}
		}
		
		_building = buildingInfo;	// Default to this.
		getConfig().writeDebug("**** Unable to parse building info '%s'\n", buildingInfo);
	}
	
	/**
	 * Attempts to extract the days that the section meets on from the string version of the days
	 * @param days The days that were listed in the PDF
	 */
	private void parseDays(String days) {
		if(StringUtils.isNullOrEmpty(days) || days.trim().equalsIgnoreCase("TBA")) {
			return;
		}
		
		_days = new Days();
		for(int i=0; i<days.length(); i++) {
			char current = days.charAt(i);
			char peek = i == days.length() - 1 ? ' ' : days.charAt(i + 1);
			
			if(current == 'M') {
				_days.set(Day.Monday);
			}
			else if(current == 'T') {
				if(peek == 'h') {
					_days.set(Day.Thursday);
				}
				else {
					_days.set(Day.Tuesday);
				}
			}
			else if(current == 'W') {
				_days.set(Day.Wednesday);
			}
			else if(current == 'F') {
				_days.set(Day.Friday);
			}
			else if(current == 'S') {
				if(peek == 'u') {
					_days.set(Day.Sunday);
				}
				else {
					_days.set(Day.Saturday);
				}
			}
		}
		
		getConfig().writeDebug("-- Parsed Section Days: '%s' = %s\n", days, _days.toString());
	}
	
	/**
	 * Attempts to parse the start and end time strings 
	 * @param startTime The first string for the times when the section meets
	 * @param endTime The second string for the times when the section meets
	 */
	private void parseTimes(String startTime, String endTime) {
		if(StringUtils.isNullOrEmpty(startTime) || StringUtils.isNullOrEmpty(endTime) || startTime.equalsIgnoreCase("TBA") || endTime.equalsIgnoreCase("TBA")) {
			return;
		}
		
		Matcher startMatch = TimeRegex.matcher(startTime);
		Matcher endMatch = TimeRegex.matcher(endTime);
		if(!startMatch.matches() || !endMatch.matches()) {
			return;
		}
		
		int startHour = Integer.parseInt(startMatch.group(HourGroup)), endHour = Integer.parseInt(endMatch.group(HourGroup));
		int startMinutes = Integer.parseInt(startMatch.group(MinuteGroup)), endMinutes = Integer.parseInt(endMatch.group(MinuteGroup));
		String startAmPm = startMatch.group(AMPMGroup), endAmPm = endMatch.group(AMPMGroup);
		boolean startAm = !StringUtils.isNullOrEmpty(startAmPm) && startAmPm.equalsIgnoreCase("AM");
		boolean startPm = !StringUtils.isNullOrEmpty(startAmPm) && startAmPm.equalsIgnoreCase("PM");
		boolean endAm = !StringUtils.isNullOrEmpty(endAmPm) && endAmPm.equalsIgnoreCase("AM");
		boolean endPm = !StringUtils.isNullOrEmpty(endAmPm) && endAmPm.equalsIgnoreCase("PM");
		
		int start = parseTime(startHour, startMinutes, startAm, startPm, endAm, endPm);
		int end = parseTime(endHour, endMinutes, endAm, endPm, startAm, startPm);
		getConfig().writeDebug("-- Parsed Section Times, Start: '%s' = %d and End: '%s' = %d\n", startTime, start, endTime, end);
		_times = new Times(start, end);
	}
	
	/**
	 * Determines the given time from hours minutes, whether it is AM, whether it's pm, and information about the other time
	 * @param hour The hour part of the time
	 * @param minute The minute part of the time
	 * @param am Whether or not the time had AM
	 * @param pm Whether or not the time has PM
	 * @param otherAm Whether or not the other time had AM
	 * @param otherPm Whether or not the other time had PM
	 * @return The time we inferred
	 */
	private int parseTime(int hour, int minute, boolean am, boolean pm, boolean otherAm, boolean otherPm) {
		if(am) {
			return hour * 100 + minute;
		}
		
		if(pm) {
			return (hour + 12) * 100 + minute;
		}
		
		if(otherAm) {
			return hour * 100 + minute;
		}
		
		if(otherPm) {
			return (hour + (hour != 12 && hour >= 10 ? 0 : 12)) * 100 + minute;
		}
		
		return (hour + (hour < 8 ? 12 : 0)) * 100 + minute;
	}
}
