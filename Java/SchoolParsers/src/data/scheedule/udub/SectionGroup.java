package data.scheedule.udub;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPreformattedText;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;

public class SectionGroup extends SectionGroupBase {
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	private SectionKey _sectionKey;
	private HtmlTable _sectionTable;
	
	private List<Section> _sections;
	
	private String _sln;
	private String _restricted;
	private HtmlAnchor _slnLink;
	private String _sectionId;
	private String _credits;
	private String _minHours;
	private String _maxHours;
	private String _sectionType;
	
	private String _status;
	private int _currentlyEnrolled;
	private int _enrollmentLimit;
	
	private String _grades;
	private String _courseFees;
	private String _other;
	
	private String _metaInfo;
	
	private boolean _isValid; 
	
	
	@Override public List<SectionBase> getSections() { return ArrayUtils.castAll(_sections, SectionBase.class); }

	@Override public String getUniqueId() { return _sln; }
	@Override public boolean isClosed() { return _enrollmentLimit > 0 && _enrollmentLimit == _currentlyEnrolled; }
	@Override public boolean isValid() { return _isValid; }
	
	public String getSectionType() { return _sectionType; }
	public String getSection() { return _sectionId; }

	public SectionGroup(Config config, SectionKey sectionKey, HtmlTable sectionTable) {
		super(config);

		_sectionKey = sectionKey;
		_sectionTable = sectionTable;
		
		parseSectionInfo();
	}

	private void parseSectionInfo() {
		HtmlPreformattedText pre = (HtmlPreformattedText) HtmlUnitHelper.getElementByTagName(_sectionTable, "pre", 0);
		
		if (pre == null) {
			System.out.printf("Unable to find section group info in : %s\n", _sectionTable.asXml());
			_isValid = false;
			return;
		}
		
		String[] subSections = pre.getTextContent().split("\\n"); 
		
		if (subSections.length == 0) {
			System.out.printf("Unable to find section info in : %s\n", pre.asText());
			_isValid = false;
			return;
		}
		
		String mainLine = null;
		for (String line : subSections) {
			if (!StringUtils.isNullOrEmpty(line)) {
				mainLine = _sectionKey.adjust(line);
				break;
			}
		}
		
		_restricted = _sectionKey.getEnrollmentRestriction(mainLine); 
		_sln = StringUtils.trim(_sectionKey.getSLN(mainLine), ">");
		
		for (HtmlElement aEle : pre.getElementsByTagName("a")) {
			HtmlAnchor a = (HtmlAnchor) aEle;
			if (a.asText().contains(_sln)) { 
				_slnLink = a;
				break;
			}
		}
		
		_sectionId = _sectionKey.getSectionID(mainLine);
		String credits = _sectionKey.getCredits(mainLine);
		_status = _sectionKey.getStatus(mainLine);
		
		if (credits.matches("^\\d+\\.?\\-?\\d*$") || credits.equalsIgnoreCase("VAR")) {
			_sectionType = "LEC";
			_credits = credits;
			
			String[] creditParts = credits.split("-");
			_minHours = creditParts[0];
			_maxHours = (creditParts.length > 1) ? creditParts[1] : _minHours;
		}
		else {
			_sectionType = credits;
		}
		
		String enrollment = _sectionKey.getEnrollmentLimit(mainLine);
		String[] enrollmentParts = enrollment.split("/");
		if (enrollmentParts.length >= 2) {
			_currentlyEnrolled = StringUtils.tryParse(enrollmentParts[0]);
			_enrollmentLimit = StringUtils.tryParse(enrollmentParts[1]);
		}
		
		_grades = _sectionKey.getGrades(mainLine);
		_courseFees = _sectionKey.getCourseFees(mainLine);
		_other = _sectionKey.getOther(mainLine);
		_metaInfo = "";
		
		getConfig().writeDebug("Enroll: %d/%d from '%s'\n", _currentlyEnrolled, _enrollmentLimit, enrollment);
		
		_sections = new ArrayList<Section>();
		for (String subSection : subSections) {
			if (StringUtils.isNullOrEmpty(subSection)) continue;
			
			subSection = _sectionKey.adjust(subSection);
			
			if (_sectionKey.isSubSection(subSection)) {
				_sections.add(new Section(getConfig(), _sectionType, _sectionKey, subSection));
			}
			else {
				_metaInfo += StringUtils.clean(subSection);
			}
		}
				
		_isValid = true;
	}
}
