package data.scheedule.udub;

import data.scheedule.base.SectionBase;
import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.utils.StringUtils;

public class Section extends SectionBase {
		
	private String _sectionInfo;
	private SectionKey _sectionKey;
	
	private String _sectionName;
	
	private Days _days;
	private Times _times;
	
	private String _building;
	private String _roomNumber;
	private String _instructor;
	
	private String _description;
	
	private boolean _isValid;
	
	private String _type;


	@Override public String getSectionName() { return _sectionName; }

	@Override public Days getDays() { return _days; }
	@Override public Times getTimes() { return _times; }
	
	@Override public String getBuilding() { return _building; }
	@Override public String getRoomNumber() { return _roomNumber; }
	@Override public String getInstructor() { return _instructor; }
	
	@Override public String getDescription() { return _description; }

	@Override public String getEndDate() { return null; }
	@Override public String getStartDate() { return null; }
	
	@Override public String getSectionType() { return _type; }
	
	@Override public boolean isValid() { return _isValid; }
	
	
	public Section(Config config, String type, SectionKey sectionKey, String sectionInfo) {
		super(config);
		
		_type = type;
		_sectionKey = sectionKey;
		_sectionInfo = sectionInfo;
		
		parseSectionInfo();
	}
	
	private void parseSectionInfo() {
		String meetingTimes = _sectionKey.getMeetingTimes(_sectionInfo);
		String[] parts = meetingTimes.split(" ");
		String days = null, times = null;
		for (String part : parts) {
			if (part.length() > 0) { 
				if (days == null) 		days = part;
				else if (times == null) times = part;
			}
		}
		
		if(StringUtils.isNullOrEmptyTrimmed(days) || StringUtils.isNullOrEmptyTrimmed(times)) {
			System.out.printf("Invalid days/times for section: %s...\n", meetingTimes);
			return;
		}
		
		_times = parseTimes(times);
		_days = parseDays(days);
		parseBuilding(_sectionKey.getBuildingRoom(_sectionInfo));
		_instructor = _sectionKey.getInstructor(_sectionInfo);
				
		_isValid = true;
	}
	
	private Days parseDays(String days) {
		Days d = new Days();
		for (int i=0; i<days.length(); i++) {
			char ch = days.charAt(i);
			char peek = (i + 1) < days.length() ? days.charAt(i + 1) : '-';
			
			switch (ch) {
			case 'M' : d.set(Days.Day.Monday); break;
			case 'W' : d.set(Days.Day.Wednesday); break;
			case 'F' : d.set(Days.Day.Friday); break;
			case 'T' : 
				if(peek == 'h') d.set(Days.Day.Thursday);
				else d.set(Days.Day.Tuesday);
				break;
			case 'S':
				if (peek == 'u') d.set(Days.Day.Sunday);
				else d.set(Days.Day.Saturday);
				break;
			}
		}
		return d;
	}
	
	private Times parseTimes(String time) {
		String[] parts = time.split("-");
		
		if (parts.length != 2) return null;
		int startVal = Integer.parseInt(parts[0].replace("P", "").replace("A", ""));
		int endVal = Integer.parseInt(parts[1].replace("P", "").replace("A", ""));
		
		if (time.endsWith("A")) return new Times(startVal, endVal);
		if (time.endsWith("P")) return new Times(startVal >= 1200 ? startVal : (1200 + startVal),
												 endVal >= 1200 ? endVal : (1200 + endVal));
		return new Times(startVal < 600 ? 1200 + startVal : startVal,
						 endVal < 800 ? 1200 + endVal : endVal);
	}
	
	private void parseBuilding(String buildingRoom) {
		String[] parts = buildingRoom.split(" ");
		
		for (String part : parts) {
			if (part.length() > 0) {
				if (_building == null) 			_building = part;
				else if (_roomNumber == null) 	_roomNumber = part;
			}
		}
	}
}
