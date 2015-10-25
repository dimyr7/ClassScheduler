package data.scheedule.base;

import java.util.List;

import data.scheedule.utils.StringBuilder;
import data.scheedule.utils.XmlHelper;

/**
 * Base class that represents a Section Group. A section group is a group
 * of section time/date/building info that are all contained under the 
 * same Section Identifier (Crn, UniqueId, etc)
 * @author Mike Barker
 */
public abstract class SectionGroupBase {
	
	/**********************************************************************
	 * Abstract Member Functions
	 **********************************************************************/
	
	/**
	 * @return Whether or not this Section Group is valid
	 */
	public abstract boolean isValid();
	
	/**
	 * @return Unique identifier of this section group, i.e. Crn, UniqueId, etc..
	 */
	public abstract String getUniqueId();
	
	/**
	 * @return Whether or not this section group is marked as closed.
	 */
	public abstract boolean isClosed();
		
	/**
	 * @return List of Sections contained in this Section Group (may only be one).
	 */
	public abstract List<SectionBase> getSections();
	
	/**********************************************************************
	 * Members
	 **********************************************************************/
	
	private static int SectionGroupId = 0;
	private int _sectionId;
	/**
	 * @return Unique identifier for this Section for this instance, counter
	 * used to identify in combos.
	 */
	public int getSectionId() { return _sectionId; }
	
	private ConfigBase _config;
	public ConfigBase getBaseConfig() { return _config; }
	
	public String getType() {
		String type = "", lastType = null;
		if (getSections() == null) return type;
		
		for (SectionBase section : getSections()) {
			String sectionType = section.getSectionType();
			
			if (lastType == null || !lastType.equalsIgnoreCase(sectionType)) {
				lastType = sectionType;
				type += (type.length() > 0 ? "/" : "") + lastType;
			}
		}
		return type;
		
	}
	
	/**********************************************************************
	 * .ctor
	 **********************************************************************/
	
	protected SectionGroupBase(ConfigBase config) { 
		_sectionId = SectionGroupId++;
		_config = config;
	}
	
	/**********************************************************************
	 * Public Functions
	 **********************************************************************/
	
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\t\t<SectionGroup>\n");
		sb.appendf("\t\t\t<Id>%d</Id>\n", _sectionId);
		sb.appendf("\t\t\t<Crn>%s</Crn>\n", XmlHelper.escapeXml(getUniqueId()));
		sb.appendf("\t\t\t<SectionClosed>%s</SectionClosed>\n", isClosed() ? "1" : "0");
				
		boolean hasSection = false;
		for (SectionBase section : getSections()) {
			if (section.isValid()) {
				hasSection = true;
				sb.append(section.toXml());
			}
		}
		
		if (!hasSection) {
			sb.append(new SectionBase.BlankSection().toXml());
		}
		
		sb.append("\t\t</SectionGroup>\n");
				
		return sb.toString();
	}
}
