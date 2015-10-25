package data.scheedule.uic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import data.scheedule.base.ComboBase;
import data.scheedule.base.SectionGroupBase;

public class Combo extends ComboBase {
	
	private Map<String, SectionGroup> _sections;
	
	@Override public boolean isValid() { return _sections.size() > 0; }
	@Override
	public List<SectionGroupBase> getSections() {
		List<SectionGroupBase> sections = new ArrayList<SectionGroupBase>();
		for (SectionGroup section : _sections.values()) {
			sections.add((SectionGroupBase) section);
		}
		return sections;
	}
	
	public Combo(Config config) {
		super(config);
		_sections = new HashMap<String, SectionGroup>();
	}
	
	public void add(SectionGroup section) {
		if (!_sections.containsKey(section.getUniqueId())) {
			_sections.put(section.getUniqueId(), section);
		}
	}
}
