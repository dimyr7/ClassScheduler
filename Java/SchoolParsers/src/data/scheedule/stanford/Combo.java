package data.scheedule.stanford;

import java.util.List;

import data.scheedule.base.ComboBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;

public class Combo extends ComboBase {

	private List<SectionGroup> _sectionGroups;
	private boolean _isValid;
	
	@Override public List<SectionGroupBase> getSections() { return ArrayUtils.castAll(_sectionGroups, SectionGroupBase.class); }
	@Override public boolean isValid() { return _isValid; }

	public Combo(Config config, List<SectionGroup> sectionGroups) {
		super(config);
		
		_sectionGroups = sectionGroups;
		_isValid = true;
	}
}
