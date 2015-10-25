package data.scheedule.udub;

import com.gargoylesoftware.htmlunit.html.HtmlTable;

import data.scheedule.utils.StringUtils;

public class SectionKey {

	private Config _config;
	
	private HtmlTable _sectionKeyTable;
	
	private int _enrlRestr;
	private int _sln;
	private int _sectID;
	private int _cred;
	private int _meetingTimes;
	private int _bldgRm;
	private int _instructor;
	private int _status;
	private int _enrlLim;
	private int _grades;
	private int _crsFees;
	private int _other;

	private int _length;
	
	public SectionKey(Config config, HtmlTable sectionKeyTable) {
		_config = config;
		_sectionKeyTable = sectionKeyTable;
		
		parseSectionKeyTable();
	}
	
	private void parseSectionKeyTable() {
		
		String secondLine = _sectionKeyTable.getTextContent().split("\n")[1];
		
		_enrlRestr = secondLine.indexOf("Restr");
		_sln = secondLine.indexOf("SLN", _enrlRestr) - 2;
		_sectID = secondLine.indexOf("ID", _sln);
		_cred = secondLine.indexOf("Cred", _sectID);
		_meetingTimes = secondLine.indexOf("Meeting Times", _cred);
		_bldgRm = secondLine.indexOf("Bldg/Rm", _meetingTimes);
		_instructor = secondLine.indexOf("Instructor", _bldgRm);
		_status = secondLine.indexOf("Status", _instructor);
		_enrlLim = secondLine.indexOf("Enrl/Lim", _status);
		_grades = secondLine.indexOf("Grades", _enrlLim);
		_crsFees = secondLine.indexOf("Fee", _grades);
		_other = secondLine.indexOf("Other", _crsFees);
		
		_length = secondLine.length() + 3;
	}
	
	public String adjust(String str) { return StringUtils.padLeft(str, (_length - str.length()) < 10 ? (_length - str.length()) : 0, " "); }
	
	public String getEnrollmentRestriction(String str) 	{ return get(str, _enrlRestr, _sln); }
	public String getSLN(String str) 					{ return get(str, _sln, _sectID); }
	public String getSectionID(String str) 				{ return get(str, _sectID, _cred); }
	public String getCredits(String str) 				{ return get(str, _cred, _meetingTimes); }
	public String getMeetingTimes(String str) 			{ return get(str, _meetingTimes, _bldgRm); }
	public String getBuildingRoom(String str) 			{ return get(str, _bldgRm, _instructor); }
	public String getInstructor(String str) 			{ return get(str, _instructor, _status); }
	public String getStatus(String str) 				{ return get(str, _status, _enrlLim); }
	public String getEnrollmentLimit(String str) 		{ return get(str, _enrlLim, _grades); }
	public String getGrades(String str) 				{ return get(str, _grades, _crsFees); }
	public String getCourseFees(String str) 			{ return get(str, _crsFees, _other); }
	public String getOther(String str) 					{ return get(str, _other); }
	
	private String get(String str, int start) { return get(str, start, str.length() - 1); } 
	private String get(String str, int start, int end) {
		if (end >= str.length() || start >= str.length()) {
			return "";
		}
		return StringUtils.clean(str.substring(start, end));
	}
	
	public boolean isSubSection(String str) {
		return isMeetingTimesValid(getMeetingTimes(str));
	}
	
	private boolean isMeetingTimesValid(String meetingTime) {
		meetingTime = meetingTime.trim();
		if (meetingTime.length() == 0) return false;
		if (meetingTime.equalsIgnoreCase("to be arranged")) return true;
		if (meetingTime.equals("*")) return true;
		
		return meetingTime.matches("^\\w+\\s+\\d{3,4}\\-\\d{3,4}\\w{0,2}$");
	}
}
