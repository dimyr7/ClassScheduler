package data.scheedule.illinois;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.scheedule.base.ComboBase;
import data.scheedule.base.CourseBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;
import data.scheedule.utils.XmlHelper;

public class Course extends CourseBase {
	
	private boolean _isValid;
	@Override
	public boolean isValid() {
		return _isValid;
	}

	private String _courseNumber;
	@Override
	public String getCourseNumber() {
		return _courseNumber;
	}

	private String _courseName;
	@Override
	public String getCourseName() {
		return _courseName;
	}

	private String _minCreditHours;
	@Override
	public String getMinCreditHours() {
		return _minCreditHours;
	}

	private String _maxCreditHours;
	@Override
	public String getMaxCreditHours() {
		return _maxCreditHours;
	}

	private String _description;
	@Override
	public String getDescription() {
		return _description;
	}

	private List<SectionGroup> _sections;
	@Override
	public List<SectionGroupBase> getSections() {
		return ArrayUtils.castAll(_sections, SectionGroupBase.class);
	}

	private List<Combo> _combos;
	@Override
	public List<ComboBase> getCombos() {
		return ArrayUtils.castAll(_combos, ComboBase.class);
	}
	
	private Config getConfig() {
		return (Config) getBaseConfig();
	}
	
	public Course(Config config, Element courseNode) {
		super(config);
		
		_sections = new ArrayList<SectionGroup>();
		_courseName = courseNode.getTextContent();
		_courseNumber = XmlHelper.getAttributeValue(courseNode, "id");
		String href = XmlHelper.getAttributeValue(courseNode, "href");
		if(StringUtils.isNullOrEmpty(href)) {
			return;
		}
		
		
		loadCourseInfo(href);
		if(_isValid) {
			generateCombos();
		}
	}
	
	private void loadCourseInfo(String courseXmlPath) {
		String url = String.format("%s?mode=detail", courseXmlPath);
		System.out.printf("Loading course info: %s\n", url);
		Document xml = getConfig().loadXml(url);
		
		_description = XmlHelper.getTextValue(xml, "description");
		String creditHours = XmlHelper.getTextValue(xml, "creditHours");
		if(!StringUtils.isNullOrEmpty(creditHours)) {
			creditHours = creditHours.replace(" hours.", "").replace(" hours", "").replace(" hour", "");
			
			String[] parts = creditHours.split(" (TO|to|OR|or|\\-) ");

			_minCreditHours = parts[0];
			_maxCreditHours = parts.length == 2 ? parts[1] : _minCreditHours;
		}
		else {
			System.out.printf("No hours element...\n");
		}
		
		NodeList sections = xml.getElementsByTagName("detailedSection");
		for(int s = 0; s < sections.getLength(); s++) {
			Node sectionNode = sections.item(s);
			SectionGroup sectionGroup = new SectionGroup(getConfig(), sectionNode);
			
			if(sectionGroup.getSections().size() > 1) {
				System.out.printf("There are %d sections in the section group %s\n", sectionGroup.getSections().size(), sectionGroup.getUniqueId());
			}
			
			if(sectionGroup.isValid()) {
				_sections.add(sectionGroup);
			}
		}
		
		_isValid = !StringUtils.isNullOrEmpty(_courseName)
					&& !StringUtils.isNullOrEmpty(_courseNumber)
					&& _sections.size() > 0;
	}

	private void generateCombos() {
		_combos = new ArrayList<Combo>();
		// TODO : The combo generator is super complicated
	}
}
