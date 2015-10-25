package data.scheedule.nuold;

import java.util.List;

import data.scheedule.base.ComboBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;

public class Combo extends ComboBase {
	
	private List<SectionGroup> _sections;
	private boolean _isValid;

	@Override public List<SectionGroupBase> getSections() { return ArrayUtils.castAll(_sections, SectionGroupBase.class); }
	@Override public boolean isValid() { return _isValid; }
	
	public Combo(Config config, List<SectionGroup> sections) {
		super(config);
		_sections = sections;
		_isValid = true;
	}
}
