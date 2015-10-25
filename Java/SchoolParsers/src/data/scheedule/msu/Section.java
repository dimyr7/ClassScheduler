package data.scheedule.msu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.SectionBase;
import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.utils.StringUtils;

public class Section extends SectionBase {
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	private Days _days;
	private Times _times;
	private String _building;
	private String _roomNumber;
	private String _instructor;
	
	private int _enrolled;
	private int _limit;
	private int _roomSize;
	
	private String _startDate;
	private String _endDate;
	
	private String _sectionId;
	
	private String _type;
	
	private boolean _isValid;

	@Override public Days getDays() { return _days; }
	@Override public Times getTimes() { return _times; }
	@Override public String getBuilding() { return _building; }
	@Override public String getRoomNumber() { return _roomNumber; }
	@Override public String getInstructor() { return _instructor; }

	@Override public String getDescription() { return null; }
	@Override public String getStartDate() { return _startDate; }
	@Override public String getEndDate() { return _endDate; }
	@Override public String getSectionName() { return _sectionId; }
	
	@Override public String getSectionType() { return _type; }

	@Override public boolean isValid() { return _isValid; }
	
	public int getEnrolled() { return _enrolled; }
	public int getLimit() { return _limit; }
	
	public void setStartDate(String startDate) { _startDate = startDate; }
	public void setEndDate(String endDate) { _endDate = endDate; }
	
	private static Pattern _dateRegex = Pattern.compile("(\\d\\d?/\\d\\d?/\\d\\d\\d\\d) - (\\d\\d?/\\d\\d?/\\d\\d\\d\\d)");
	
	public Section(Config config, String type, HtmlTableRow sectionRow, String sectionId) {
		super(config);
		_type = type;
		_sectionId = sectionId;
		
		parseSectionInfo(sectionRow);
	}
	
	private void parseSectionInfo(HtmlTableRow sectionRow) {		
		_days = parseDays(getCellText(sectionRow, "Days"));
		_times = parseTimes(getCellText(sectionRow, "Times"));
		parseBuilding(getCellText(sectionRow, "Building"));
		_instructor = getCellText(sectionRow, "Instructor");
		_enrolled = !StringUtils.isNullOrEmpty(getCellText(sectionRow, "Enrolled")) ? Integer.parseInt(getCellText(sectionRow, "Enrolled")) : 0;
		_limit = !StringUtils.isNullOrEmpty(getCellText(sectionRow, "Limit")) ? Integer.parseInt(getCellText(sectionRow, "Limit")) : 0;
		_roomSize = !StringUtils.isNullOrEmpty(getCellText(sectionRow, "Room Size")) ? Integer.parseInt(getCellText(sectionRow, "Room Size")) : 0;
		
		HtmlTableRow siblingRow = (HtmlTableRow) sectionRow.getNextSibling();
		if(siblingRow != null) {
			Matcher dateMatcher = _dateRegex.matcher(StringUtils.clean(siblingRow.asText()));
			if(dateMatcher.matches()) {
				_startDate = dateMatcher.group(1);
				_endDate = dateMatcher.group(2);
			}
		}
					
		_isValid = true;
	}
	
	private String getCellText(HtmlTableRow sectionRow, String header) {
		for (HtmlTableCell cell : sectionRow.getCells()) {
			if (cell.hasAttribute("headers") && cell.getAttribute("headers").equalsIgnoreCase(header)) {
				return StringUtils.clean(cell.asText());
			}
		}
		return null;
	}
	
	private Days parseDays(String info) {
		Days d = new Days();
		if(info.contains(" ")) {
			String[] parts = info.split(" ");
			for(String part : parts) {
				if(part.equalsIgnoreCase("M")) d.set(Days.Day.Monday);
				else if(part.equalsIgnoreCase("Tu")) d.set(Days.Day.Tuesday);
				else if(part.equalsIgnoreCase("W")) d.set(Days.Day.Wednesday);
				else if(part.equalsIgnoreCase("Th")) d.set(Days.Day.Thursday);
				else if(part.equalsIgnoreCase("F")) d.set(Days.Day.Friday);
				else if(part.equalsIgnoreCase("Sa")) d.set(Days.Day.Saturday);
				else if(part.equalsIgnoreCase("Su")) d.set(Days.Day.Sunday);
			}
		}
		else {
			for (int i=0; i<info.length(); i++) {
				char peek = (i + 1) < info.length() ? info.charAt(i+1) : '-';
				switch(info.charAt(i)) {
				case 'M' : d.set(Days.Day.Monday); break;
				case 'W' : d.set(Days.Day.Wednesday); break;
				case 'F' : d.set(Days.Day.Friday); break;
				case 'T' : 
					if (peek == 'u') d.set(Days.Day.Tuesday);
					if (peek == 'h') d.set(Days.Day.Thursday);
					break;
				case 'S' :
					if (peek == 'u') d.set(Days.Day.Sunday);
					if (peek == 'a') d.set(Days.Day.Saturday);
					break;
				}
			}
		}
		
		return d;
	}
	
	private Times parseTimes(String info) {
		if (StringUtils.isNullOrEmpty(info)) return null;
		
		String[] parts = info.split(" - ");
		if (parts.length != 2) return null;
		
		return new Times(parseTime(parts[0]), parseTime(parts[1]));
	}
	
	private int parseTime(String str) {
		int strVal = Integer.parseInt(str.replace(":", "").replace(" AM", "").replace(" PM", ""));
		return strVal + ((strVal < 1200 && str.endsWith("PM")) ? 1200 : 0);
	}

	private void parseBuilding(String info) {
		int spaceIndex = info.indexOf(' ');
		if (spaceIndex >= 0) {
			_building = info.substring(0, spaceIndex);
			_roomNumber = info.substring(spaceIndex + 1);
		}
	}
}
