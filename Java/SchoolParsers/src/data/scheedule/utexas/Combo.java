package data.scheedule.utexas;

import java.util.ArrayList;
import java.util.List;

import data.scheedule.base.ComboBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;

public class Combo extends ComboBase {

	private List<SectionGroup> _sections;

	@Override public boolean isValid() { return true; }
	@Override public List<SectionGroupBase> getSections() { return ArrayUtils.castAll(_sections, SectionGroupBase.class); }
	
	public Combo(Config config, SectionGroup section) {
		super(config);
		_sections = new ArrayList<SectionGroup>();
		_sections.add(section);
	}
	
}
