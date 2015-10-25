package data.scheedule.nuold;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.HtmlUnitHelper;
import data.scheedule.utils.StringUtils;

public class SectionGroup extends SectionGroupBase {
	
	private List<HtmlTableRow> _sectionRows;
	
	private List<Section> _sections;
	
	private String _uniqueId;
	
	private String _sectionName;
	private String _sectionType;
	
	private boolean _isClosed;
	private boolean _isValid;

	@Override public List<SectionBase> getSections() { return ArrayUtils.castAll(_sections, SectionBase.class); }

	@Override public String getUniqueId() { return _uniqueId; }
	@Override public boolean isClosed() { return _isClosed; }
	@Override public boolean isValid() { return _isValid; }
	
	public String getSectionType() { return _sectionType; }
	
	private Config getConfig() { return (Config) getBaseConfig(); }

	public SectionGroup(Config config, List<HtmlTableRow> sectionRows) {
		super(config);
		_sectionRows = sectionRows;
		_sections = new ArrayList<Section>();
		
		parseSectionRows();
	}
	
	
	private void parseSectionRows() {		
		if (_sectionRows.size() < 4) {
			System.out.printf("Not enough rows..invalid section group..\n");
			_isValid = false;
			return; 
		}
		
		HtmlTable sectionInfoTable = tryFindSectionInfoTable();
		
		if (sectionInfoTable == null || sectionInfoTable.getRowCount() <= 1) {
			System.out.printf("Unable to find section info table for %s [%s].\n", _sectionRows.get(1).asText(), _sectionRows.size());
			_isValid = false;
			return;
		}
		
		HtmlAnchor sectionInfoAnchor = tryFindSectionInfoLink();
		
		if (sectionInfoAnchor == null) {
			System.out.printf("Unable to find section info anchor...\n");
			_isValid = false;
			return;
		}
		
		String sectionInfo = StringUtils.clean(sectionInfoAnchor.asText());
		if (!parseSectionInfo(sectionInfo)) {
			System.out.printf("Unable to parse Section Info String: '%s'\n", sectionInfo);
			_isValid = false;
			return;
		}
		parseIsClosed();
		
		for (int i=1; i<sectionInfoTable.getRowCount(); i++) {
			Section section = new Section(getConfig(), _sectionType, _sectionRows, sectionInfoTable.getRow(i));
			if (section.isValid()) {
				_sections.add(section);
			}
		}
		_isValid = _sections.size() > 0;
	}
	
	private void parseIsClosed() {
		HtmlImage statusImage = (HtmlImage) HtmlUnitHelper.getElementByTagAndAttributeValue(_sectionRows.get(0), "img", "class", "SSSIMAGECENTER");
		if (statusImage != null) {
			_isClosed = !statusImage.getSrcAttribute().equalsIgnoreCase("/cs/s9prod/cache_PT85012/PS_CS_STATUS_OPEN_ICN_1.gif");		
		}
		else {
			HtmlTableRow seating = _sectionRows.get(3);
		
			try {
				_isClosed = (Integer.parseInt(StringUtils.clean(seating.getCell(2).asText()))) == 0;
			} catch(Exception e) {
				_isClosed = false;
			}
		}
	}
	
	private boolean parseSectionInfo(String sectionInfo) {
		if (!sectionInfo.matches("^.+\\-.+\\(\\d+\\)$")) {
			return false;
		}
		String[] parts = sectionInfo.split("\\(");
		
		_sectionName = StringUtils.clean(parts[0]);
		_uniqueId = StringUtils.clean(parts[1].replace(")", ""));
		_sectionType = _sectionName.substring(_sectionName.indexOf('-') + 1);
		return true;
	}	
	
	
	private HtmlAnchor tryFindSectionInfoLink() {
		for (HtmlTableRow row : _sectionRows) {
			HtmlAnchor anchor = (HtmlAnchor) HtmlUnitHelper.getElementByTagAndAttributeValue(row, "a", "class", "PSHYPERLINK");
			if (anchor != null) return anchor;
		}
		return null;
	}

	
	private HtmlTable tryFindSectionInfoTable() {
		for (HtmlTableRow row : _sectionRows) {
			HtmlTable tbl = (HtmlTable)HtmlUnitHelper.getElementByTagAndAttributeValue(row, "table", "class", "PSLEVEL1GRIDWBO");
			if (tbl != null) return tbl;
		}
		return null;	
	}
}
