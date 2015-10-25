package data.scheedule.illinoisold;

import java.util.List;

import org.w3c.dom.Element;

import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.SectionBase;
import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.common.Days.Day;
import data.scheedule.utils.StringUtils;
import data.scheedule.utils.XmlHelper;

public class Section extends SectionBase {
	
	///////////////////////////////////////////////////////////
	//// Members
	
	private String _sectionName;
	private String _sectionType;
	private Days _days;
	private Times _times;
	private String _startDate;
	private String _endDate;
	private String _description;
	private String _instructor;
	private String _building;
	private String _roomNumber;
	private boolean _isValid;
	
	@Override public String getSectionName() { return _sectionName; }
	@Override public String getSectionType() { return _sectionType; }
	@Override public String getStartDate() { return _startDate; }
	@Override public String getEndDate() { return _endDate; }
	@Override public Days getDays() { return _days; }
	@Override public Times getTimes() { return _times; }
	@Override public String getDescription() { return _description; }
	@Override public String getInstructor() { return _instructor; }
	@Override public String getBuilding() { return _building; }
	@Override public String getRoomNumber() { return _roomNumber; }
	@Override public boolean isValid() { return _isValid; }
	
	private Config getConfig() { return (Config)getBaseConfig(); }
	
	private String _groupByName = null;
	public String getGroupByName() { return _groupByName; }
	public void setGroupByName(String value) { _groupByName = value; }
	
	private String _crn;
	public String getCrn() { return _crn; }
	public void setCrn(String value) { _crn = value; }
	
	public void setSectionName(String value) { _sectionName = value; }
	public void setSectionType(String value) { _sectionType = value; }
			
	///////////////////////////////////////////////////////////
	//// .ctor
	
	public Section(Config config, Element sectionRow) {
		super(config);
		
		_isValid = false;
		
		parseSectionRow(sectionRow);
	}
	
	
	//////////////////////////////////////////////////////////
	//// Helper Functions
	
	private void parseSectionRow(Element sectionRow) {
		
		_crn = XmlHelper.getTextValue(sectionRow, "referenceNumber");
		
		_sectionType = XmlHelper.getTextValue(sectionRow, "sectionType");
		// S# are basically lecture-discussions
		if (_sectionType.startsWith("S"))
		{
			_sectionType = "LCD";
		}
		if (_sectionType.startsWith("C"))
		{
			_sectionType = "CNF";
		}
		_sectionName = XmlHelper.getTextValue(sectionRow, "sectionId");
		_building = XmlHelper.getTextValue(sectionRow, "building");
		_roomNumber = XmlHelper.getTextValue(sectionRow, "roomNumber");
		_instructor = XmlHelper.getTextValue(sectionRow, "instructor");
		_description = XmlHelper.getTextValue(sectionRow, "sectionText");
		_days = parseDays(XmlHelper.getTextValue(sectionRow, "days"));
		_times = parseTimes(XmlHelper.getTextValue(sectionRow, "startTime"), XmlHelper.getTextValue(sectionRow, "endTime"));

		//this._dateRange = new UiucDateRange(XmlHelper.getTextValue(sectionElement, "sectionDateRange"));
		
		_isValid = (_sectionType != null) && (_sectionName != null) && (_days != null)  
						&& (_building != null) && (_roomNumber != null) 
						&& (_instructor != null) && (_description != null);
		
		if (_isValid) {
			System.out.printf("Found Section: %s [%s] from %d-%d on %s in %s room %s taught by %s\n", 
									_sectionName, _sectionType, 
									_times.getStartTime(), _times.getEndTime(),
									_days.toString(),
									_building, _roomNumber, _instructor);
		}
	}

	
	private Times parseTimes(String startTime, String endTime) {
		return new Times(parseTime(startTime), parseTime(endTime));
	}
	
	private int parseTime(String str) {
		int strVal = Integer.parseInt(str.replace(":", "").replace(" AM", "").replace(" PM", ""));
		return strVal + ((strVal < 1200 && str.endsWith("PM")) ? 1200 : 0);
	}
	
	private Days parseDays(String days) {
		Days d = new Days();
		for (int i = 0; i < days.length(); i++) {
			switch (days.charAt(i)) {
			case 'M': d.set(Day.Monday); break;
			case 'T': d.set(Day.Tuesday); break;
			case 'W': d.set(Day.Wednesday); break;
			case 'R': d.set(Day.Thursday); break;
			case 'F': d.set(Day.Friday); break;
			case 'S': d.set(Day.Saturday); break;
			case 'U': d.set(Day.Sunday); break;
			}
		}
		return d;
	}
}
