package data.scheedule.nuold;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.SectionBase;
import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;

public class Section extends SectionBase {
	
	private List<HtmlTableRow> _sectionRows;
	private HtmlTableRow _sectionInfoRow;
	
	private Days _days;
	private Times _times;
		
	private String _sectionName;
	
	private int _availableSeats;
	private int _waitListTotal;
	
	private String _description;
	private String _instructor;
	
	private String _building;
	private String _roomNumber;
	
	private String _startDate;
	private String _endDate;
	
	private boolean _isValid;
	
	private String _type;
	
	@Override public String getBuilding() { return _building; }
	@Override public String getRoomNumber() { return _roomNumber; }

	@Override public Times getTimes() { return _times; }	
	@Override public Days getDays() { return _days; }
	
	@Override public String getSectionName() { return _sectionName; }
	@Override public String getDescription() { return _description; }
	@Override public String getInstructor() { return _instructor; }

	@Override public String getStartDate() { return _startDate; }
	@Override public String getEndDate() { return _endDate; }
	
	@Override public String getSectionType() { return _type; }
	
	@Override public boolean isValid() { return _isValid; }

	public Section(Config config, String type, List<HtmlTableRow> sectionRows, HtmlTableRow sectionInfoRow) {
		super(config);
		
		_type = type;
		_sectionRows = sectionRows;
		_sectionInfoRow = sectionInfoRow;
		
		parseSectionInfo();
	}
	
	private void parseSectionInfo() {		
		if (_sectionInfoRow.getCells().size() != 4) {
			System.out.printf("Found %d (expected 4) cells in Section Info Row: %s\n", _sectionInfoRow.getCells().size(), _sectionInfoRow.asXml());
			_isValid = false;
			return;
		}
		
		for (HtmlSpan span : getEditBoxSpans()) {
			if (span.hasAttribute("id")) {
				if (span.getAttribute("id").startsWith("NW_DERIVED_SS3_WAIT_TOT")) {
					_waitListTotal = StringUtils.tryParse(StringUtils.clean(span.asText()));
				}
				else if(span.getAttribute("id").startsWith("NW_DERIVED_SS3_AVAILABLE_SEATS")) {
					_availableSeats = StringUtils.tryParse(StringUtils.clean(span.asText()));
				}
			}
		}
				
		try {			
			parseDaysAndTimes(getCell(_sectionInfoRow, 0));
			parseRoomInfo(getCell(_sectionInfoRow, 1));
			_instructor = getCell(_sectionInfoRow, 2).replace("\n", "").replace("\r", "");
			parseMeetingDates(getCell(_sectionInfoRow, 3));
						
			_isValid = true;
			
			/*System.out.printf("Found Section: %s type %s (%s) :: %d %d-%d in %s - %s taught by %s from %s to %s\n", 
								_sectionName, _sectionType, _uniqueId,
								_days != null ? _days.getBitMask() : -1, 
								_times != null ? _times.getStartTime() : -1, _times != null ? _times.getEndTime() : -1, 
								_roomNumber != null ? _roomNumber : "TBA", _building != null ? _building : "TBA", _instructor, _startDate, _endDate);*/
			
		} catch (Exception e) {
			System.out.printf("***ERROR: %s", e.getMessage());
			_isValid = false;
		}
	}
	
	private List<HtmlSpan> getEditBoxSpans() {
		List<HtmlSpan> spans = new ArrayList<HtmlSpan>();
		for (HtmlTableRow row : _sectionRows) {
			for (HtmlElement ele : HtmlUnitHelper.getElementsByTagAndAttributeValue(row, "span", "class", "PSEDITBOX_DISPONLY")) {
				spans.add((HtmlSpan) ele);
			}
		}
		return spans;
	}
		
	private String getCell(HtmlTableRow row, int cell) {
		try {
			return StringUtils.clean(row.getCell(cell).asText());
		} catch(Exception e) {}
		return "";
	}
		
	private void parseDaysAndTimes(String daysAndTimes) throws Exception {
		if (daysAndTimes.equalsIgnoreCase("TBA")) return;
		
		String[] parts = daysAndTimes.split(" ");
		
		if (parts.length != 4) {
			throw new Exception(String.format("Unable to parse days/times '%s'\n", daysAndTimes));
		}
		
		_days = parseDays(parts[0]);
		_times = parseTimes(parts[1], parts[3]);
	}
	
	private Days parseDays(String days) throws Exception {
		Days d = new Days();
		for (int i=0; i<days.length(); i+=2) {
			String curr = days.substring(i, i+2);
			if (curr.equals("Mo")) d.set(Days.Day.Monday); 
			else if (curr.equals("Tu")) d.set(Days.Day.Tuesday);
			else if (curr.equals("We")) d.set(Days.Day.Wednesday); 
			else if (curr.equals("Th")) d.set(Days.Day.Thursday); 
			else if (curr.equals("Fr")) d.set(Days.Day.Friday); 
			else if (curr.equals("Sa")) d.set(Days.Day.Saturday); 
			else if (curr.equals("Su")) d.set(Days.Day.Sunday); 
			else throw new Exception(String.format("Unknown Day: '%s'\n", curr));
		}
		return d;
	}
	
	private Times parseTimes(String startTime, String endTime) {
		return new Times(getTime(startTime), getTime(endTime));
	}
	
	private int getTime(String time) {
		int intTime = StringUtils.tryParse(time.replace(":", "").replace("AM", "").replace("PM", ""));
		return intTime + ((time.endsWith("PM") && intTime < 1200) ? 1200 : 0);
	}
	
	private void parseRoomInfo(String roomInfo) throws Exception {
		if (roomInfo.equals("TBA")) return;
		
		_building = StringUtils.clean(roomInfo.substring(0, roomInfo.lastIndexOf(' ')));
		_roomNumber = StringUtils.clean(roomInfo.substring(roomInfo.lastIndexOf(' ')));	
	}
	
	private void parseMeetingDates(String meetingDates) {
		String[] parts = meetingDates.split(" - ");
		if (parts.length == 2) {
			_startDate = StringUtils.clean(parts[0]);
			_endDate = StringUtils.clean(parts[1]);
		}
	}
}
