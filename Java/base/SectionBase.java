package data.scheedule.base;

import data.scheedule.common.Days;
import data.scheedule.common.Times;
import data.scheedule.utils.StringBuilder;
import data.scheedule.utils.XmlHelper;

/**
 * Base class for a specific Section. Should contain the specific information
 * such as Days, Times, etc.
 * @author Mike Barker
 */
public abstract class SectionBase {
	
	/**********************************************************************
	 * Abstract Member Functions
	 **********************************************************************/
	
	/**
	 * @return Whether or not this Section is valid
	 */
	public abstract boolean isValid();
	
	/**
	 * @return The name of the section, i.e. 'A1'
	 */
	public abstract String getSectionName();
	
	/**
	 * @return The times that this Section meets
	 */
	public abstract Times getTimes();
	
	/**
	 * @return The days that this Section meets
	 */
	public abstract Days getDays();
	
	/**
	 * @return Gets the Start Date of the Section
	 */
	public abstract String getStartDate();
	
	/**
	 * @return Gets the End Date of this Section
	 */
	public abstract String getEndDate();
	
	/**
	 * @return Gets the building that this Section meets in
	 */
	public abstract String getBuilding();
	
	/**
	 * @return Gets the room number that this Section meets in
	 */
	public abstract String getRoomNumber();
	
	/**
	 * @return Gets the instructor(s) that teach this section
	 */
	public abstract String getInstructor();
	
	/**
	 * @return Gets a description of the seciton if necessary
	 */
	public abstract String getDescription();
	
	/**
	 * @return Gets the type of section this is
	 */
	public abstract String getSectionType();

	
	/**********************************************************************
	 * Members
	 **********************************************************************/
	
	/**
	 * @return Whether or not this section has a Times variable
	 */
	private boolean hasTimes() { return getTimes() != null; }
	
	/**
	 * @return Whether or not this section has a Days variable
	 */
	private boolean hasDays()  { return getDays() != null; }
	
	private ConfigBase _config;
	public ConfigBase getBaseConfig() { return _config; }

	/**********************************************************************
	 * .ctor
	 **********************************************************************/
	
	protected SectionBase(ConfigBase config) { _config = config; }

	/**********************************************************************
	 * Public Functions
	 **********************************************************************/
	
	/**
	 * Converts this Section into an Xml Represenation to be parsed by
	 * the Scheedule XmlToDatabase script
	 * @return Xml representation of this section
	 */
	public String toXml() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\t\t\t<Section>\n");
		
		sb.appendf("\t\t\t\t<Name>%s</Name>\n", XmlHelper.escapeXml(getSectionName()));
		sb.appendf("\t\t\t\t<StartTime>%s</StartTime>\n", hasTimes() ? XmlHelper.escapeXml(getTimes().getStartTime()) : "");
		sb.appendf("\t\t\t\t<EndTime>%s</EndTime>\n", hasTimes() ? XmlHelper.escapeXml(getTimes().getEndTime()) : "");
		sb.appendf("\t\t\t\t<Days>%s</Days>\n", hasDays() ? XmlHelper.escapeXml(getDays().getBitMask()) : "");
		sb.appendf("\t\t\t\t<StartDate>%s</StartDate>\n", XmlHelper.escapeXml(getStartDate()));
		sb.appendf("\t\t\t\t<EndDate>%s</EndDate>\n", XmlHelper.escapeXml(getEndDate()));
		sb.appendf("\t\t\t\t<Building>%s</Building>\n", XmlHelper.escapeXml(getBuilding()));
		sb.appendf("\t\t\t\t<RoomNumber>%s</RoomNumber>\n", XmlHelper.escapeXml(getRoomNumber()));
		sb.appendf("\t\t\t\t<Instructor>%s</Instructor>\n", XmlHelper.escapeXml(getInstructor()));
		sb.appendf("\t\t\t\t<Description>%s</Description>\n", XmlHelper.escapeXml(getDescription()));
		sb.appendf("\t\t\t\t<Type>%s</Type>\n", XmlHelper.escapeXml(getSectionType()));
		
		sb.append("\t\t\t</Section>\n");
		
		return sb.toString();
	}
	
	public static class BlankSection extends SectionBase {
		@Override public String getBuilding() { return ""; }
		@Override public Days getDays() { return null; }
		@Override public String getDescription() { return ""; }
		@Override public String getEndDate() { return ""; }
		@Override public String getInstructor() { return ""; }
		@Override public String getRoomNumber() { return ""; }
		@Override public String getSectionName() { return ""; }
		@Override public String getStartDate() { return ""; }
		@Override public String getSectionType() { return ""; }
		@Override public Times getTimes() { return null; }
		@Override public boolean isValid() { return true; }
		
		public BlankSection() { super(null); }
	}
	
}
