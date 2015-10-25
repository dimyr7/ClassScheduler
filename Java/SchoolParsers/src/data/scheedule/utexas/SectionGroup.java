package data.scheedule.utexas;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.HtmlUnorderedList;

import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

public class SectionGroup extends SectionGroupBase {
	
	private String _uniqueId;
	
	private List<Section> _sections;
	
	private String _instructor;
	private String _status;
	private String _description;
	
	private List<String> _flags;
	
	private boolean _isValid;
	
	private Config getConfig() { return (Config) getBaseConfig(); }
	
	@Override public boolean isValid() { return _isValid; }
	
	@Override public String getUniqueId() { return _uniqueId; } 
	@Override public boolean isClosed() { return _status != null && _status.contains("closed"); }
	
	@Override public List<SectionBase> getSections() { return ArrayUtils.castAll(_sections, SectionBase.class); }
	
	
	private String _sectionLink;
	public String getSectionLink() { return _sectionLink; }
	
	public SectionGroup(Config config, HtmlTableRow sectionRow) {
		super(config);
		_sections = new ArrayList<Section>();
		parseSectionInfo(sectionRow);
	}
	
	
	private void parseSectionInfo(HtmlTableRow sectionRow) {
		int cellCount = sectionRow.getCells().size();
		if (cellCount >= 6)  {
			HtmlAnchor sectionLink = (HtmlAnchor) sectionRow.getCell(0).getElementsByTagName("a").get(0);
			_sectionLink = sectionLink.getHrefAttribute();
			_uniqueId = StringUtils.clean(sectionLink.asText());
			
			String[] days = sectionRow.getCell(1).asText().split("\n");
			String[] hours = sectionRow.getCell(2).asText().split("\n");
			String[] rooms = sectionRow.getCell(3).asText().split("\n");
			
			if (days.length == hours.length && hours.length == rooms.length && days.length > 0) {
				for(int i=0; i<days.length; i++) {
					// TODO : Get the section type.
					_sections.add(new Section(getConfig(), "", StringUtils.clean(days[i]), StringUtils.clean(hours[i]), StringUtils.clean(rooms[i]), _instructor, _description));
				}
			}
						
			_instructor = StringUtils.clean(sectionRow.getCell(4).asText());
			_status = StringUtils.clean(sectionRow.getCell(5).asText());
			
			if (cellCount >= 7) {
				DomNodeList<HtmlElement> results = sectionRow.getCell(6).getElementsByTagName("ul");
				if (results.size() == 1) {
					_flags = new ArrayList<String>();
					HtmlUnorderedList flagsUl = (HtmlUnorderedList) results.get(0);
					for (HtmlElement li : flagsUl.getElementsByTagName("li")) {
						_flags.add(StringUtils.clean(li.asText().replace("tooltip", "")));
					}
				}
			}
			
			// Cancelled classes are invalid.
			if (_status != null && !_status.contains("cancelled")) {
				_isValid = true;				
			}
		}
		else {
			_isValid = false;
			String sectionId = (cellCount > 0) ? sectionRow.getCell(0).asText().trim() : "UNKNOWN";
			System.out.printf("Expecting >= 6 cells for %s got %d\n", sectionId, cellCount);
		}
	}	
}
