package data.scheedule.illinois;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.scheedule.base.SectionBase;
import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.common.Days.Day;
import data.scheedule.utils.StringUtils;
import data.scheedule.utils.XmlHelper;

public class Section extends SectionBase {

	private boolean _isValid;
	@Override
	public boolean isValid() {
		return _isValid;
	}

	private String _sectionName;
	@Override
	public String getSectionName() {
		return _sectionName;
	}

	private Times _times;
	@Override
	public Times getTimes() {
		return _times;
	}

	private Days _days;
	@Override
	public Days getDays() {
		return _days;
	}

	private String _startDate;
	@Override
	public String getStartDate() {
		return _startDate;
	}

	private String _endDate;
	@Override
	public String getEndDate() {
		return _endDate;
	}

	private String _building;
	@Override
	public String getBuilding() {
		return _building;
	}

	private String _roomNumber;
	@Override
	public String getRoomNumber() {
		return _roomNumber;
	}

	private String _instructor;
	@Override
	public String getInstructor() {
		return _instructor;
	}

	private String _description;
	@Override
	public String getDescription() {
		return _description;
	}

	private String _sectionType;
	@Override
	public String getSectionType() {
		return _sectionType;
	}

	public Section(Config config, Element sectionNode, Element meetingNode) {
		super(config);
		
		parseSection(sectionNode, meetingNode);
	}
	
	private void parseSection(Element sectionNode, Element meetingNode) {
		_sectionName = XmlHelper.getTextValue(sectionNode, "sectionNumber");
		_startDate = XmlHelper.getTextValue(sectionNode, "startDate");
		_endDate = XmlHelper.getTextValue(sectionNode, "endDate");
		_sectionType = XmlHelper.getAttributeValue(meetingNode, "type", "code");
		_building = XmlHelper.getAttributeValue(meetingNode, "buildingName");
		_roomNumber = XmlHelper.getAttributeValue(meetingNode, "roomNumber");
		_times = parseTimes(XmlHelper.getTextValue(meetingNode, "start"), XmlHelper.getTextValue(meetingNode, "end"));
		_days = parseDays(XmlHelper.getTextValue(meetingNode, "daysOfTheWeek"));
		
		NodeList instructors = meetingNode.getElementsByTagName("instructor");
		for(int i = 0; i < instructors.getLength(); i++) {
			Node instructorNode = instructors.item(i);
			if(_instructor == null) {
				_instructor = "";
			}
			else {
				_instructor += ";";
			}
			
			_instructor += instructorNode.getTextContent();
		}
		
		_isValid = !StringUtils.isNullOrEmpty(_sectionName)
					&& !StringUtils.isNullOrEmpty(_startDate)
					&& !StringUtils.isNullOrEmpty(_endDate)
					&& !StringUtils.isNullOrEmpty(_sectionType);
	}
	
	private Times parseTimes(String start, String end) {
		if(StringUtils.isNullOrEmpty(start) || StringUtils.isNullOrEmpty(end) || start.equalsIgnoreCase("arranged") || end.equalsIgnoreCase("arranged") || start.equalsIgnoreCase("arr") || end.equalsIgnoreCase("arr")) {
			return null;
		}
		
		return new Times(parseTime(start), parseTime(end));
	}
	
	private int parseTime(String time) {
		int strVal = Integer.parseInt(time.replace(":", "").replace(" AM", "").replace(" PM", ""));
		return strVal + ((strVal < 1200 && time.endsWith("PM")) ? 1200 : 0);
	}
	
	private Days parseDays(String days) {
		if(StringUtils.isNullOrEmpty(days)) {
			return null;
		}
		
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
