package data.scheedule.stanford;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.HtmlElement;

import data.scheedule.base.SectionBase;
import data.scheedule.base.SectionGroupBase;
import data.scheedule.utils.ArrayUtils;
import data.scheedule.utils.StringUtils;

public class SectionGroup extends SectionGroupBase {

	////////////////////////////////////////////////////////////
	//// Members
	
	private String _uniqueId;
	private boolean _isClosed;
	
	private boolean _isValid;
	
	private String _instructors;
	private String _notes;
	
	private List<Section> _sections;
	
	private String _type;
	
		
	@Override public List<SectionBase> getSections() { return ArrayUtils.castAll(_sections, SectionBase.class); }
	@Override public String getUniqueId() { return _uniqueId; }
	@Override public boolean isClosed() { return _isClosed; }
	@Override public boolean isValid() { return _isValid; }
	
	public String getType() { return _type; }
	public String getInstructors() { return _instructors; }
	public String getNotes() { return _notes; }
	
	private Config getConfig() { return (Config) getBaseConfig(); }
		
	///////////////////////////////////////////////////////////
	//// .ctor
	
	public SectionGroup(Config config, HtmlElement sectionDetails) {
		super(config);
		_sections = new ArrayList<Section>();
		parseSections(sectionDetails);
	}
	
	
	private void parseSections(HtmlElement sectionDetails) {
		_isValid = false;
				
		String[] detailParts = StringUtils.clean(sectionDetails.asText()).split("\n");
		
		String details = detailParts[0].trim();
		List<String> sectionParts = new ArrayList<String>();
		String instructorPart = "";
		String notesPart = "";
		
		for (int i=1; i<detailParts.length; i++) {
			String part = detailParts[i].trim();
			
			if (part.startsWith("Instructors: ")) {
				instructorPart = part.replace("Instructors: ", "").trim();
			}
			else if(part.startsWith("Notes: "))	  {
				notesPart = part.replace("Notes: ", "").trim();
			}
			else {
				sectionParts.add(part);
			}
		}
		_instructors = instructorPart;
		_notes = notesPart;
		
		// Parse the details
		if (!parseDetails(details)) {
			return;
		}
		
		// Parse the sections
		for (String sectionPart : sectionParts) {
			Section section = new Section(getConfig(), _type, details, sectionPart);
			if (section.isValid()) {
				_sections.add(section);
			}
			else {
				getConfig().writeDebug("Unable to properly parse section info: '%s'  and '%s'\n", details, sectionPart);
			}
		}
		
		_isValid = true;
	}	
	
	private boolean parseDetails(String details) {
		String[] detailsParts = details.split("\\|");
		
		String classPart = null;
		String typePart = detailsParts[detailsParts.length - 1]; 
		for (String part : detailsParts) {
			part = part.trim();
			if (part.startsWith("Class # ")) { 
				classPart = part;
				break;
			}
		}
		
		if (classPart == null) return false;
		
		_uniqueId = classPart.replace("Class # ", "").trim();
		_type = typePart.trim();
		_isClosed = false;
		
		return true;
	}
}
