package data.scheedule.stanford;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import data.scheedule.base.SectionBase;
import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.utils.StringUtils;

public class Section extends SectionBase {
	
	/////////////////////////////////////////////////////////////
	/// Members
	
	private String _sectionName;
	private String _description;
	private String _instructors;
	
	private String _building;
	private String _roomNumber;
	
	private String _startDate;
	private String _endDate;
	private Days _days;
	private Times _times;
	
	private boolean _isValid;
	
	private String _type;

	@Override public String getSectionName() { return _sectionName; }
	@Override public String getDescription() { return _description; }
	@Override public String getInstructor() { return _instructors; }
	
	@Override public String getBuilding() { return _building; }
	@Override public String getRoomNumber() { return _roomNumber; }
		
	@Override public String getStartDate() { return _startDate; }
	@Override public String getEndDate() { return _endDate; }
	@Override public Days getDays() { return _days; }
	@Override public Times getTimes() { return _times; }
	
	@Override public boolean isValid() { return _isValid; }
	
	@Override public String getSectionType() { return _type; }
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	private String _details;
	private String _timeInfo;
	
	///////////////////////////////////////////////////////////////////////////////
	//// .ctor
	
	public Section(Config config, String type, String details, String timeInfo) {
		super(config);
		_type = type;
		_details = details;
		_timeInfo = timeInfo;
		parseSectionInfo();		
	}
	
	
	private void parseSectionInfo() {
		_isValid = parseDetails()
					&& parseTimeInfo();
	}
	
	private boolean parseDetails() {
		String[] detailsParts = _details.split("\\|");
		for (String part : detailsParts) {
			part = part.trim();
			
			if (part.startsWith("Section ")) {
				_sectionName = StringUtils.clean(part.replace("Section ", ""));
				break;
			}
		}
		
		return _sectionName != null;
	}
	
	private boolean parseTimeInfo() {
		String info = _timeInfo.trim();
		
		SimpleDateFormat inFormat = new SimpleDateFormat("mm/DD/yyyy");
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyymmDD");
		
		// Extract start date
		int index = info.indexOf(" - ");
		if (index >= 0 && index < info.length()) {
			try {
				Date dt = inFormat.parse(StringUtils.clean(info.substring(0, index)));
				_startDate = outFormat.format(dt);
			} catch (ParseException e) {}
			info = StringUtils.substring(info, index + 3).trim();
		}
		
		// Extract end date
		index = info.indexOf(" ");
		if (index >= 0 && index < info.length()) {
			try {
				Date dt = inFormat.parse(StringUtils.clean(info.substring(0, index)));
				_endDate = outFormat.format(dt);
			} catch (ParseException e) {}
			info = StringUtils.substring(info, index + 1).trim();
		}
		
		// Extract days
		List<String> days = new ArrayList<String>();
		while (info.length() >= 3 && isDay(info.substring(0, 3))) {
			days.add(info.substring(0, 3));
			info = StringUtils.substring(info, 4).trim();
		}
		parseDays(days);
		
		// Extract the Times
		index = info.indexOf(" - ");
		if (index >= 0 && index < info.length()) {
			String startTime = info.substring(0, index).trim();
			info = StringUtils.substring(info, index + 3).trim();
			
			index = info.indexOf(" AM");
			if (index < 0 || index >= info.length()) index = info.indexOf(" PM");
			if (index >= 0 && index < info.length()) {
				String endTime = info.substring(0, index + 3).trim();
				info = StringUtils.substring(info, index + 4).trim();
				parseTimes(startTime, endTime);
			}
		}
			
		// Attempt to extract building info
		if (info.startsWith("at ")) {
			index = info.indexOf(" with ");
			if (index < 0 || index >= info.length()) index = info.length() - 1;
			parseBuilding(info.substring(3, index));
			info = StringUtils.substring(info, index + 1);
		}
		
		// Extract instructors
		if (info.startsWith("with ")) {
			_instructors = info.substring(5).trim();
		}
		
		return true;
	}
	
	private boolean isDay(String str) {
		return str.equalsIgnoreCase("Mon") 
				|| str.equalsIgnoreCase("Tue")
				|| str.equalsIgnoreCase("Wed")
				|| str.equalsIgnoreCase("Thu")
				|| str.equalsIgnoreCase("Fri")
				|| str.equalsIgnoreCase("Sat")
				|| str.equalsIgnoreCase("Sun");
	}
	
	private void parseDays(List<String> days) {
		_days = new Days();
		
		for (String day : days) {
			if (day.equalsIgnoreCase("Mon")) 	  _days.set(Days.Day.Monday);
			else if (day.equalsIgnoreCase("Tue")) _days.set(Days.Day.Tuesday);
			else if (day.equalsIgnoreCase("Wed")) _days.set(Days.Day.Wednesday);
			else if (day.equalsIgnoreCase("Thu")) _days.set(Days.Day.Thursday);
			else if (day.equalsIgnoreCase("Fri")) _days.set(Days.Day.Friday);
			else if (day.equalsIgnoreCase("Sat")) _days.set(Days.Day.Saturday);
			else if (day.equalsIgnoreCase("Sun")) _days.set(Days.Day.Sunday);
		}
	}

	private void parseTimes(String start, String end) {
		_times = new Times(parseTime(start), parseTime(end));
	}
	
	private int parseTime(String str) {
		int strVal = Integer.parseInt(str.replace(":", "").replace(" AM", "").replace(" PM", ""));
		return strVal + ((strVal < 1200 && str.endsWith("PM")) ? 1200 : 0);
	}
	
	private void parseBuilding(String building) {
		if (StringUtils.isNullOrEmpty(building)) return;
		
		if (building.contains("-")) {	// e.g.: 09/26/2011 - 12/09/2011 Wed 2:15 PM - 3:05 PM at 420-371
			String[] parts = building.split("-");
			if (parts.length == 2) {
				_building = StringUtils.clean(parts[0]);
				_roomNumber = StringUtils.clean(parts[1]);
				return;
			}
		}
		
		// e.g.: 
		//			09/26/2011 - 12/09/2011 Tue 4:15 PM - 5:05 PM at Hewlett Teaching Center 200
		// 			09/26/2011 - 12/09/2011 Wed 7:00 PM - 7:50 PM at Gates B12
		//			09/26/2011 - 12/09/2011 Wed 5:15 PM - 6:05 PM at Hewlett Teaching Center Rm 101
		if (building.contains(" ") && Character.isDigit(building.charAt(building.length() - 1))) { 
			int index = building.lastIndexOf(" ");
			_building = StringUtils.clean(building.substring(0, index).replace("Rm", "").replace("Room", "").replace("rm", "").replace("room", ""));
			_roomNumber = StringUtils.clean(building.substring(index));
			return;
		}
		
		// e.g.:
		//			09/26/2011 - 12/09/2011 Mon, Wed 3:15 PM - 4:30 PM at Gates100 with Winograd, T. (PI)
		if (Character.isDigit(building.charAt(building.length() - 1))) {
			int index = building.length() - 1;
			while (Character.isDigit(building.charAt(index--)) && index >= 0);
			
			_building = StringUtils.clean(building.substring(0, index));
			_roomNumber = StringUtils.clean(building.substring(index + 1));
			return;
		}
		
		// e.g.: 		
		//			09/26/2011 - 12/09/2011 Mon, Wed, Fri 9:00 AM - 9:50 AM at Skillaud with Cain, G. (PI) 
		//			09/26/2011 - 12/09/2011 Mon, Wed, Fri 11:00 AM - 11:50 AM at Cubberley Auditorium with Rosenblum, M. (PI) 
		// 			09/26/2011 - 12/09/2011 Mon, Wed 9:30 AM - 10:45 AM at NVIDIA Auditorium with Ng, A. (PI) 
		_roomNumber = building;
		_building = "";
	}
}
