package data.scheedule.base;

import java.util.List;

import data.scheedule.utils.StringBuilder;

/**
 * Base class for Combinations of Sections that are required to be taken simultaneously.
 * @author Mike Barker
 *
 */
public abstract class ComboBase {
	
	/**********************************************************************
	 * Abstract Member Functions
	 **********************************************************************/ 

	/**
	 * @return Whether or not this Combo is valid
	 */
	public abstract boolean isValid();
	
	/**
	 * @return List of SectionGroups required for this Combo
	 */
	public abstract List<SectionGroupBase> getSections();

	
	/**********************************************************************
	 * Member Functions
	 **********************************************************************/ 
	
	private ConfigBase _config;
	/**
	 * @return Config which is shared between all Base classes for one instance
	 */
	public ConfigBase getBaseConfig() { return _config; }

	/**********************************************************************
	 * .ctor
	 **********************************************************************/ 
	
	protected ComboBase(ConfigBase config) { _config = config; }

	
	/**********************************************************************
	 * Public Functions
	 **********************************************************************/ 
	
	/**
	 * Converts this Combo into XML which is parsable by Scheedule's XmlToDatabase script.
	 * @return XML Representation of this Combo
	 */
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\t\t<Combo>\n");
		
		for (SectionGroupBase section : getSections()) {
			sb.appendf("\t\t\t<SectionGroup>%d</SectionGroup>\n", section.getSectionId());
		}
		
		sb.append("\t\t</Combo>\n");
		
		return sb.toString();
	}
}
