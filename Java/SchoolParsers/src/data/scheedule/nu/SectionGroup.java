/**
 * SectionGroup.java
 * @copyright Scheedule, Inc.
 */
package data.scheedule.nu;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;

/**
 * This class represents a Section Group for Northwestern, each one should only have one Section
 */
public class SectionGroup extends SectionGroupBase {

	private String _uniqueId;
	private boolean _isClosed;	
	private boolean _isValid;
	
	private List<Section> _sections;

	/**
	 * Creates a new instance of a Section Group for Northwestern 
	 * @param config The config that is currently running
	 * @param match The match found from parsing the pdf
	 */
	public SectionGroup(Config config, Matcher match) {
		super(config);
		
		_sections = new ArrayList<Section>();
		extractDataFromMatcher(match);
		Section section = new Section(config, match);
		_sections.add(section);
		_isValid = section.isValid();
	}
	
	/**
	 * @return The first section in the section group, each section group should only have one section
	 */
	public Section getFirstSection() {
		return _sections.get(0);
	}
	
	/**
	 * @return The section type of the first section in the section group
	 */
	public String getSectionType() { 
		return getFirstSection().getSectionType();
	}
	
	/**
	 * @see SectionGroupBase#isValid()
	 */
	@Override 
	public boolean isValid() { 
		return _isValid; 
	}
	
	/**
	 * @see SectionGroupBase#getUniqueId()
	 */
	@Override 
	public String getUniqueId() { 
		return _uniqueId; 
	}
	
	/**
	 * @see SectionGroupBase#isClosed()
	 */
	@Override 
	public boolean isClosed() { 
		return _isClosed; 
	}

	/**
	 * @see SectionGroupBase#getSections()
	 */
	@Override 
	public List<SectionBase> getSections() { 
		return ArrayUtils.castAll(_sections, SectionBase.class); 
	} 
	
	/**
	 * Extracts the data for the Section group from the match 
	 * @param match Match found while parsing the PDF for the Section regex
	 */
	private void extractDataFromMatcher(Matcher match) {
		_uniqueId = match.group(Section.UniqueIdGroup).trim();
	}
}
