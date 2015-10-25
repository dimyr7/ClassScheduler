/**
 * Combo.java
 * @copyright Scheedule, Inc.
 */

package data.scheedule.nu;

import java.util.List;

import data.scheedule.base.ComboBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;

/**
 * This class represents a combinatino of section groups for northwestern
 */
public class Combo extends ComboBase {
	
	private List<SectionGroup> _sections;

	/**
	 * Creates a new instance of the Combo class
	 * @param config The config being used
	 * @param sections The list of sections for this combo
	 */
	public Combo(Config config, List<SectionGroup> sections) {
		super(config);
		_sections = sections;
	}
	
	/**
	 * @see ComboBase#isValid()
	 */
	@Override 
	public boolean isValid() { 
		return true; 
	}
	
	/**
	 * @see ComboBase#getSections()
	 */
	@Override 
	public List<SectionGroupBase> getSections() { 
		return ArrayUtils.castAll(_sections, SectionGroupBase.class); 
	}
}
