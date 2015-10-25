package data.scheedule.uic;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlTableRow;

import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

public class SectionGroup extends SectionGroupBase {
	
	private HtmlTableRow _sectionRow;
	
	private String _crn;
	private List<Section> _subSections;
	
	private boolean _isValid;
	
	private Config getConfig() { return (Config) getBaseConfig(); }

	@Override public boolean isValid() { return _isValid; }
	@Override public String getUniqueId() { return _crn; }
	@Override public boolean isClosed() { 
		return _subSections.size() > 0 
					? _subSections.get(0).getCurrentlyEnrolled() == _subSections.get(0).getMaxEnrollment() 
					: false; 
	}
	
	@Override public List<SectionBase> getSections() { return ArrayUtils.castAll(_subSections, SectionBase.class); }
	
	public List<Section> getSubSections() { return _subSections; }
		
	public SectionGroup(Config config, HtmlTableRow sectionRow) {
		super(config);
		_sectionRow = sectionRow;
		_subSections = new ArrayList<Section>();
		
		parseSectionInfo();
	}
	
	private void parseSectionInfo() {
		if (_sectionRow.getCells().size() >= 2) {
			_crn = getSectionRowCrn(_sectionRow);
			_subSections.add(new Section(getConfig(), _sectionRow));
			_isValid = true;
		}
		else {
			_isValid = false;
		}
	}
	
	public void merge(SectionGroup other) {
		for (Section newSection : other._subSections) {
			if (!alreadyHasSection(newSection)) {
				_subSections.add(newSection);
			}
		}
	}
	
	private boolean alreadyHasSection(Section newSection) {
		for (Section section : _subSections) {
			if (section.equals(newSection)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static String getSectionRowCrn(HtmlTableRow sectionRow) {
		if (sectionRow.getCells().size() >= 2) {
			return StringUtils.clean(sectionRow.getCell(1).asText());
		}
		return null;
	}
}
