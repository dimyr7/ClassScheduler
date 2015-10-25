package data.scheedule.illinoisold;

import java.util.ArrayList;
import java.util.List;

import data.scheedule.base.ComboBase;
import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;

public class Combo extends ComboBase {
	
	////////////////////////////////////////////////////////////////
	///// Members
	
	private List<SectionGroupBase> _sections;
	private boolean _isValid;
	
	@Override public List<SectionGroupBase> getSections() { return _sections; }
	@Override public boolean isValid() { return _isValid; }

	////////////////////////////////////////////////////////////////
	///// .ctor
	
	public Combo(Config config, List<SectionGroupBase> sections) {
		super(config);

		_sections = sections;
		_isValid = true;
	}
	
	public Combo(Config config) {
		super(config);
		
		_sections = new ArrayList<SectionGroupBase>();
	}
	
	public Combo(Combo clone) {
		super(clone.getBaseConfig());
		
		_sections = new ArrayList<SectionGroupBase>(clone._sections);
	}
	
	
	public boolean tryAdd(SectionGroup groupedSection, List<SectionGroup> allGroupedSections)
	{		
		// If this section is already in the group, return true
		for(SectionGroupBase groupedSectionToCheck : _sections) {	
			if(groupedSectionToCheck == groupedSection) {
				return true;
			}
		}
							
		if(!_sections.isEmpty()) {		
			for(SectionGroupBase existingGroupedSection : _sections) {
				// We are already added, return true without adding ourself again
				if(existingGroupedSection == groupedSection) {
					return true;
				}
				
				if(isConflict(existingGroupedSection, groupedSection)) {
					return false;
				}
			}
		}

		_sections.add(groupedSection);
		_isValid = true;

		return true;
	}
	
	private boolean isConflict(SectionGroupBase g1, SectionGroupBase g2)
	{
		for(SectionBase s1 : g1.getSections()) {
			for(SectionBase s2 : g2.getSections()) {
				if(isConflict(s1, s2)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean isConflict(SectionBase s1, SectionBase s2) {	
		// Only allow 1 of each type per combo.  This gets weird because of linked sections
		if(s1.getSectionType().equalsIgnoreCase(s2.getSectionType())) {
			return true;
		}
		
		boolean matchesDay = s1.getDays().overlapsAny(s2.getDays());
				
		if(matchesDay) {
			/*Date sd1 = s1.getStartDate();
			Date sd2 = s2.getStartDate();
			Date ed1 = s1.getEndDate();
			Date ed2 = s2.getEndDate();
			
			
			if(sd1 == null || sd2 == null || ed1 == null || ed2 == null ||
			  !(afterOrEquals(sd1, ed2) || beforeOrEquals(ed1, sd2)))
			{
				
				Date st1 = s1.getStartTime();
				Date st2 = s2.getStartTime();
				Date et1 = s1.getEndTime();
				Date et2 = s2.getEndTime();
				if((st1 != null && st2 != null && et1 != null && et2 != null) && 
					!(afterOrEquals(st1, et2) || beforeOrEquals(et1, st2)))
				{
					return true;
				}
			}*/
		}
		
		return false;
	}
	/*
	private boolean beforeOrEquals(Date d1, Date d2)
	{
		return d1.before(d2) || d1.equals(d2);
	}
	
	private boolean afterOrEquals(Date d1, Date d2)
	{
		return d1.after(d2) || d1.equals(d2);
	}*/
}