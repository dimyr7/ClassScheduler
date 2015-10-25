package data.scheedule.illinois;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;
import data.scheedule.utils.XmlHelper;

public class SectionGroup extends SectionGroupBase {

	private boolean _isValid;
	@Override
	public boolean isValid() {
		return _isValid;
	}

	private String _uniqueId;
	@Override
	public String getUniqueId() {
		return _uniqueId;
	}

	private boolean _isClosed;
	@Override
	public boolean isClosed() {
		return _isClosed;
	}

	private List<Section> _sections;
	@Override
	public List<SectionBase> getSections() {
		return ArrayUtils.castAll(_sections, SectionBase.class);
	}
	
	public String getSectionType() {
		return _sections.size() > 0 ? _sections.get(0).getSectionType() : "UNK";
	}
	
	private Config getConfig() {
		return (Config) getBaseConfig();
	}

	public SectionGroup(Config config, Node sectionNode) {
		super(config);
		
		_sections = new ArrayList<Section>();
		parseSectionInfo((Element)sectionNode);
	}
	
	private void parseSectionInfo(Element sectionNode) {
		_uniqueId = XmlHelper.getAttributeValue(sectionNode, "id");
		_isClosed = StringUtils.containsIgnoreCase(XmlHelper.getTextValue(sectionNode, "enrollmentStatus"), "open");
						
		NodeList meetings = sectionNode.getElementsByTagName("meeting");
		for(int m = 0; m < meetings.getLength(); m++) {
			Element meetingNode = (Element) meetings.item(m);
			Section section = new Section(getConfig(), sectionNode, meetingNode);
			if(section.isValid()) {
				_sections.add(section);
			}
			else {
				System.out.printf("Invalid section found: %s @ %d\n", _uniqueId, m);
			}
		}
		
		_isValid = !StringUtils.isNullOrEmpty(_uniqueId)
					&& _sections.size() > 0;
	}
}
