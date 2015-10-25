package data.scheedule.msu;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

public class SectionGroup extends SectionGroupBase {

	private Config getConfig() { return (Config) getBaseConfig(); }

	private List<Section> _sections;
	
	private String _sectionId;
	private String _uniqueId;
	private boolean _isClosed;
	
	private double _minCredits;
	private double _maxCredits;

	private boolean _isValid;
	
	@Override public List<SectionBase> getSections() { return ArrayUtils.castAll(_sections, SectionBase.class); }
	
	@Override public String getUniqueId() { return _uniqueId; }
	@Override public boolean isClosed() { return _isClosed; }
	@Override public boolean isValid() { return _isValid; }
	
	public double getMinCredits() { return _minCredits; }
	public double getMaxCredits() { return _maxCredits; }

	public SectionGroup(Config config, List<HtmlTableRow> sectionRows) {
		super(config);
		
		parseSectionInfo(sectionRows);
	}
	
	private void parseSectionInfo(List<HtmlTableRow> sectionRows) {
		_sections = new ArrayList<Section>();
		
		if (sectionRows.size() == 0) {
			_isValid = false;
			return;
		}
		
		HtmlAnchor sectionLink = null;
		for (HtmlTableRow sectionRow : sectionRows) {
			if (sectionRow.getCells().size() >= 11) {
				sectionLink = (HtmlAnchor) sectionRow.getCell(1).getElementsByTagName("a").get(0);
				_sectionId = StringUtils.clean(sectionLink.asText());
				
				_uniqueId = sectionLink.getNameAttribute().substring(1);
				String[] parts = StringUtils.clean(sectionRow.getCell(2).asText()).split("-");
				String minCredits = parts[0];
				String maxCredits = (parts.length > 1) ? parts[1] : minCredits;
				
				if (!StringUtils.isNullOrEmpty(minCredits)) _minCredits = Double.parseDouble(minCredits);
				if (!StringUtils.isNullOrEmpty(maxCredits)) _maxCredits = Double.parseDouble(maxCredits);
			}
			Section section = new Section(getConfig(), "", sectionRow, _sectionId);	// TODO : get the type 
			_sections.add(section);
		}
		
		if (sectionLink == null) {
			_isValid = false;
			return;
		}
		
		_isClosed = _sections.get(0).getLimit() > 0 && _sections.get(0).getEnrolled() == _sections.get(0).getLimit();
		_isValid = true;
	}
}
