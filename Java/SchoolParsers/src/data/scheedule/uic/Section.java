package data.scheedule.uic;

import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.SectionBase;
import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.utils.StringUtils;

public class Section extends SectionBase {
	
	private HtmlTableRow _sectionRow;
	
	private String _crn;
	private String _sectionType;
	private String _sectionID;
	private Days _days;
	private Times _times;
	
	private String _building;
	private String _roomNumber;
	
	private String _seatsAvailable;
	private String _instructor;
	
	private String _books;
	private String _sectionRestrictions;
	
	private String _maxEnrollment;
	private String _currentEnrollment;
	private String _seatsRemaining;
	
	private boolean _isValid;
		
	@Override public boolean isValid() 			{ return _isValid; }
	
	@Override public String getSectionName() 	{ return _sectionID; }
	@Override public Times getTimes() 			{ return _times; }
	@Override public Days getDays() 			{ return _days; }
	@Override public String getStartDate() 		{ return ""; }
	@Override public String getEndDate() 		{ return ""; }
	@Override public String getBuilding() 		{ return _building; }
	@Override public String getRoomNumber() 	{ return _roomNumber; }
	@Override public String getInstructor() 	{ return _instructor; }
	@Override public String getDescription() 	{ return ""; }
	@Override public String getSectionType() 	{ return _sectionType; }
	
	public void setSectionName(String name) { _sectionID = name; }
	
	public int getMaxEnrollment() 		{ return StringUtils.tryParse(_maxEnrollment);     }
	public int getCurrentlyEnrolled() 	{ return StringUtils.tryParse(_currentEnrollment); }
	
	public Section(Config config, HtmlTableRow sectionRow) {
		super(config);
		_sectionRow = sectionRow;
		
		parseSectionInfo();
	}
	
	private void parseSectionInfo() {
		int cellCount = _sectionRow.getCells().size(); 
		if (cellCount == 11) {
			
			_crn = getCellText(1);
			parseSectionInfo(getCellText(2));
			_days = parseDays(getCellText(3));
			_times = parseTimes(getCellText(4));
			parseBuildingInfo(getCellText(5));
			_seatsAvailable = getCellText(6);
			_instructor = getCellText(7);
			_books = getCellText(8);
			_sectionRestrictions = getCellText(9);
			parseEnrollmentInfo(getCellText(10));
			
			_isValid = true;
		}
		else {
			_isValid = false;
			String sectionName = (cellCount >= 2) ? _sectionRow.getCell(1).asText() : "UNKNOWN";
			System.out.printf("Section row expected 11 cells, found %d, for section %s\n", cellCount, sectionName);
		}
	}
	
	private String getCellText(int i) {
		return StringUtils.clean(_sectionRow.getCell(i).asText());
	}

	private void parseSectionInfo(String info) {
		String[] parts = info.split("   ");
		_sectionType = parts[0];
		if (parts.length == 2) _sectionID = parts[1];
	}
	
	private Days parseDays(String days) {
		Days d = new Days();
		for (char c : days.toCharArray()) {
			switch (c) {
			case 'M' : d.set(Days.Day.Monday); break;
			case 'T' : d.set(Days.Day.Tuesday); break;
			case 'W' : d.set(Days.Day.Wednesday); break;
			case 'R' : d.set(Days.Day.Thursday); break;
			case 'F' : d.set(Days.Day.Friday); break;
			case 'S' : d.set(Days.Day.Saturday); break;
			case 'U' : d.set(Days.Day.Sunday); break;
			}
		}
		return d;
	}
		
	private Times parseTimes(String times) {
		String[] parts = times.split(" - ");
		if (parts.length == 2) {
			return new Times(parseTime(parts[0]), parseTime(parts[1]));
		}
		return null;
	}
	
	private int parseTime(String str) {
		int strVal = Integer.parseInt(str.replace(":", "").replace(" AM", "").replace(" PM", ""));
		return strVal + ((strVal < 1200 && str.endsWith("PM")) ? 1200 : 0);
	}

	private void parseBuildingInfo(String info) {
		String[] parts = info.split(" ");
		if (parts.length == 2) {
			_building = parts[0];
			_roomNumber = parts[1];
		}
	}

	private void parseEnrollmentInfo(String info) {
		String[] parts = info.split("|");
		if (parts.length == 3) {
			_maxEnrollment = parts[0];
			_currentEnrollment = parts[1];
			_seatsRemaining = parts[2];
		}
	}
	
	
	@Override
	public boolean equals(Object other) {
		Section otherSection = (Section) other;
		if (otherSection == null) {
			return false;
		}
		
		return sectionCrnsEqual(otherSection)
				&& sectionNamesEqual(otherSection)
				&& sectionDaysEqual(otherSection)
				&& sectionTimesEqual(otherSection);
	}

	private boolean sectionCrnsEqual(Section other) {
		return (_crn == null && other._crn == null)
				|| (_crn != null && _crn.equalsIgnoreCase(other._crn));
	}
	
	private boolean sectionNamesEqual(Section other) {
		return (_sectionID == null && other._sectionID == null)
				|| (_sectionID != null && _sectionID.equalsIgnoreCase(other._sectionID));
	}
	
	private boolean sectionDaysEqual(Section other) {
		return (_days == null && other._days == null)
				|| (_days != null && _days.equals(other._days));
	}
	
	private boolean sectionTimesEqual(Section other) {
		return (_times == null && other._times == null)
				|| (_times != null && _times.equals(other._times));
	}
}
