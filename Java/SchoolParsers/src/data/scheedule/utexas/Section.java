package data.scheedule.utexas;

import data.scheedule.base.SectionBase;
import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.common.Days.Day;
import data.scheedule.utils.StringUtils;

public class Section extends SectionBase {
	
	private Days _days;
	private Times _times;
	
	private String _building;
	private String _room;
	
	private String _instructor;
	private String _description;
	
	private String _type; 
	
	private boolean _isValid;
	

	@Override public boolean isValid() { return _isValid; }
	
	@Override public String getSectionName() { return ""; }
	@Override public Times getTimes() { return _times; }
	@Override public Days getDays() { return _days; }
	@Override public String getStartDate() { return ""; }
	@Override public String getEndDate() { return ""; }
	@Override public String getBuilding() { return _building; }
	@Override public String getRoomNumber() { return _room; }
	@Override public String getInstructor() { return _instructor; }
	@Override public String getDescription() { return _description; }
	@Override public String getSectionType() { return _type; }
	
	public Section(Config config, String type, String instructor, String description) {
		super(config);
		_type = type;
		_instructor = instructor;
		_description = description;
	}
	
	public Section(Config config, String type, String daysText, String timesText, String roomText, String instructor, String description) {
		super(config);
		_isValid = true;
		
		_type = type;
		_instructor = instructor;
		_description = description;
		
		parseDays(daysText);
		parseHours(timesText);
		parseBuildingInfo(roomText);
	}
	
	private void parseDays(String daysText) {
		if (StringUtils.isNullOrEmpty(daysText)) {
			_isValid = false;
			return;
		}
		
		_days = new Days();
		daysText = daysText.toUpperCase();
		
		int dayCharCount = daysText.length();
		for(int i=0; i<dayCharCount; i++) {
			char current = daysText.charAt(i);
			switch(current) {
			case 'M' : _days.set(Day.Monday); break;
			case 'T' : 
				if (i < dayCharCount-1 && daysText.charAt(i + 1) == 'H') {
					_days.set(Day.Thursday);
					i++;
				}
				else {
					_days.set(Day.Tuesday);
				}
				break;
			case 'W' : _days.set(Day.Wednesday); break;
			case 'F' : _days.set(Day.Friday); break;
			case 'S' : 
				if (i < dayCharCount-1 && daysText.charAt(i + 1) == 'U') {
					_days.set(Day.Sunday);
					i++;
				}
				else {
					_days.set(Day.Saturday);
				}
				break;
			}
		}
	}
	
	private void parseHours(String hoursText) {
		String[] parts = hoursText.toLowerCase().split(" to ");
		if (parts.length != 2) {
			_isValid = false;
			return;
		}
		
		String startStr = parts[0].trim();
		String endStr = parts[1].trim();
		
		int start = Integer.parseInt(startStr);
		int end = Integer.parseInt(endStr.substring(0, endStr.length() - 1));
		
		if (endStr.endsWith("a") || end >= 1200) {
			_times = new Times(start, end);
		}
		else {
			int modifiedEnd = 1200 + end;
			int modifiedStart = (start > end) ? start : 1200 + start;
						
			_times = new Times(modifiedStart, modifiedEnd);
		}
	}
	
	private void parseBuildingInfo(String roomText) {
		roomText = roomText.trim();
		if (!roomText.contains(" ")) {
			_isValid = false;
			return;
		}
		
		int index = roomText.indexOf(" "); 
		_building = StringUtils.clean(roomText.substring(0, index - 1));
		_room = StringUtils.clean(roomText.substring(index + 1));
	}
}
