package data.scheedule.illinois;

import java.util.List;

import data.scheedule.base.ComboBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;

public class Combo extends ComboBase {

	private boolean _isValid;
	@Override
	public boolean isValid() {
		return _isValid;
	}

	private List<SectionGroup> _sections;
	@Override
	public List<SectionGroupBase> getSections() {
		return ArrayUtils.castAll(_sections, SectionGroupBase.class);
	}

	public Combo(Config config, List<SectionGroup> sections) {
		super(config);
		_sections = sections;
	}
}
