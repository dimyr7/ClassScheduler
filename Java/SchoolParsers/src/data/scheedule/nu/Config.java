/**
 * Config.java
 * @copyright Scheedule, Inc.
 */

package data.scheedule.nu;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.util.PDFTextStripperByArea;

import java.awt.Rectangle;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import data.scheedule.base.ConfigBase;

/**
 * This class represents a config for getting course data for Northwestern
 */
public class Config extends ConfigBase {

	public static final String c_PdfUrl = "http://www.registrar.northwestern.edu/registration/class_schedule";
	//	/4450/4450_class_schedule_full.pdf
	
	private PDDocument _pdf;
	private List<String> _schools;
	
	private Map<String, String> _buildingAbbreviations;
	private Map<String, String> _courseAttributes;
	
	private String _rawCourseData;
	
	/**
	 * Creates a new instance of the Config for Northwestern that parses the PDF
	 * @param term The Term to get course data for
	 * @param year The year to get course data for
	 */
	@SuppressWarnings("unchecked")
	public Config(Term term, String year) {
		super(term, year);
		
		_schools = new ArrayList<String>();
		_schools.add("J L Kellogg School of Management");
		_schools.add("Medill School of Journalism");
		_schools.add("Weinberg College of Arts & Sciences"); 
		_schools.add("Bienen School of Music");
		_schools.add("School of Communication");
		_schools.add("McCormick School of Engineering & Applied Sciences"); 
		_schools.add("The Graduate School");
		_schools.add("Non-Degree Courses");
		
		System.out.printf("Loading Url: %s\n", getPdfUrl());
		try {
			_pdf = PDDocument.load(getPdfUrl());			
			PDOutlineItem start = null, end = null;
			PDDocumentOutline root = _pdf.getDocumentCatalog().getDocumentOutline();
			PDOutlineItem item = root.getFirstChild();
			PDOutlineItem buildingAbbreviations = null;
			while(item != null) {
				int index = _schools.indexOf(item.getTitle());
				if(index == 0) {
					start = item.getFirstChild();
				}
				else if(index == _schools.size() - 1) {
					end = item.getLastChild();
				}
				else if(item.getTitle().equals("Building Abbreviations")) {
					buildingAbbreviations = item;
				}
				
				item = item.getNextSibling();
			}
			
			parseAbbreviations(buildingAbbreviations);
			
			List<PDPage> allPages = _pdf.getDocumentCatalog().getAllPages();
			int startPage = allPages.indexOf(start.findDestinationPage(_pdf));
			int endPage = allPages.indexOf(end.findDestinationPage(_pdf));
			
			System.out.printf("Found pages: %d - %d\n", startPage, endPage);
			for(int i=startPage; i<=endPage; i++) {
				PDPage page = allPages.get(i);
				NWPDFTextStripper s = new NWPDFTextStripper();
				s.extractColumns(page);
				_rawCourseData += s.getLeftColumnText() + s.getRightColumnText();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return The building abbreviation to full building name found in the pdf
	 */
	public Map<String, String> getBuildingAbbreviations() {
		return _buildingAbbreviations;
	}
	
	/**
	 * @return The course attributes found in the pdf, mapping the abbreviation => full attribute name 
	 */
	public Map<String, String> getCourseAttributes() {
		return _courseAttributes;
	}
	
	/**
	 * @return The raw course data lines found in the pdf
	 */
	public String[] getCourseDataLines() {
		return _rawCourseData.split("\r?\n");
	}
	
	/**
	 * @return The list of school header names that we should look under for 
	 */
	public List<String> getSchoolNames() { 
		return _schools; 
	}
	
	/**
	 * @see ConfigBase#getSchoolAbbreviation()
	 */
	@Override 
	public String getSchoolAbbreviation() { 
		return "nu"; 
	}
	
	/**
	 * Cleans the given text, removing any course attributes that might be in the string
	 * @param text Text to clean
	 * @return Cleaned text with course abbreviations removed if any found
	 */
	public String clean(String text) {
		for(String attribute : _courseAttributes.keySet()) {
			// Special case: RESTR MMSS I assume means replace MMSS with the major abbreviation....so it could be anything, make the regex  \w+ for those, similarly for MMM
			Pattern p = Pattern.compile(attribute.replace(" MMM", " \\w+").replace(" MMSS", " \\w+"));
			Matcher m = p.matcher(text);
			if(m.find()) {
				return m.replaceAll("");
			}
		}
		
		return text;
	}
	
	/**
	 * Parse the Building abbreviations and Course Attribute abbreviations from the PDF
	 * @param item The outline item which points to the page containing the building list and course attribute list
	 * @throws IOException
	 */
	private void parseAbbreviations(PDOutlineItem item) throws IOException {
		if(item == null) {
			System.out.println("What the heck item is null!");
			return;
		}
		
		// Pull out the text from the different regions, assuming the page is split between 3 equal width columns
		// The first column being a list of building abbreviations and their corresponding nice names
		// The second column being a guide to help students go to CAESAR 
		// The third column being the list of course attribute abbreviations and their corresponding nice names
		PDPage page = item.findDestinationPage(_pdf);
		PDRectangle r = page.findCropBox();
		PDFTextStripperByArea s = new PDFTextStripperByArea();
		s.setSortByPosition(true);
		s.addRegion("building", new Rectangle(0, 0, (int)(r.getWidth() / 3), (int)(r.getHeight() * 0.60)));
		s.addRegion("course", new Rectangle((int)(r.getWidth() * 0.66), 0, (int)(r.getWidth() / 3), (int)(r.getHeight())));
		s.extractRegions(page);
		
		//System.out.println("Extracted Regions!!!");
		String[] buildingLines = s.getTextForRegion("building").split("\r\n");
		_buildingAbbreviations = new HashMap<String, String>();
		for(String buildingLine : buildingLines) {
			if(buildingLine.startsWith("Building Abbreviations") || buildingLine.trim().isEmpty()) continue;
			
			String[] lineParts = buildingLine.split(" ", 2);
			if(lineParts.length != 2) continue;
			_buildingAbbreviations.put(lineParts[0], lineParts[1]);
		}
		
		_courseAttributes = new HashMap<String, String>();
		String[] courseAttributeLines = s.getTextForRegion("course").split("\r?\n");
		String lastCourseAttributePrefix = null;
		for(String courseAttributeLine : courseAttributeLines) {
			// Skip the intro text
			if(courseAttributeLine.startsWith("Course Attributes")
				|| courseAttributeLine.startsWith("These appear in the class ")
				|| courseAttributeLine.startsWith("more information about courses")
				|| courseAttributeLine.trim().isEmpty()) continue;
			
			String[] parts = courseAttributeLine.split(" ");
			if(parts.length < 2) {
				continue;
			}
			
			if(!courseAttributeLine.startsWith(" ")) {
				lastCourseAttributePrefix = parts[0];
			}
			
			// Ignore P/N prefix since it is prefixed again in the course attributes, i.e. P/N P/N REQ
			String courseAttribute = (lastCourseAttributePrefix.equals("P/N") ? "" : (lastCourseAttributePrefix + " ")) + parts[1];
			String courseAttributeString = "";
			boolean appendingAttribute = true;
			for(int i=2; i<parts.length; i++) {
				String part = parts[i];
				boolean isAllCaps = true;
				if(appendingAttribute) {
					for(int c=0; c<part.length(); c++) {
						if(Character.isLowerCase(part.charAt(c))) {
							isAllCaps = false;
							break;
						}
					}
				}
				
				if(isAllCaps && appendingAttribute) {
					courseAttribute += " " + part;
					// special case: RESTR MMM or RESTR MMSS, after the first MMM or MMSS we know we are done building the abbreviation
					if(part.equals("MMM") || part.equals("MMSS")) {
						appendingAttribute = false;
					}
				}
				else {
					appendingAttribute = false;
					courseAttributeString += (courseAttributeString.isEmpty() ? "" : " ") + part;
				}
			}
			
			_courseAttributes.put(courseAttribute, courseAttributeString);
		}
	}

	/**
	 * Gets the semester value, the 4 digit number that Northwestern uses to identify a semester
	 * @return The semester value that Northwestern uses to ID a semester
	 */
	private String getSemesterValue() {
		int yearsDiff = Integer.parseInt(getYear()) - 2011;
		int semestersDiff = 0;
		if (getTerm() == Term.Spring) {
			semestersDiff = 1;
		}
		else if (getTerm() == Term.Summer) {
			semestersDiff = 2;
		}
		else if (getTerm() == Term.Fall) {
			semestersDiff = 3;
		}
		else if (getTerm() == Term.Winter) {
			semestersDiff = 0;
		}
		
		// Semester values are probably 10 * the semester number 
		// 4410 is Winter 2011, so just find the number of semesters away from Winter 2011 the current one is, and add 10 * that number to 4410
		int semesterValue = 4410 + 10 * (yearsDiff * 4 + semestersDiff);
		return Integer.toString(semesterValue);
	}
	
	/**
	 * @return The url to the PDF for the current semester
	 */
	private URL getPdfUrl() {
		String semesterValue = getSemesterValue();
		try {
			return new URL(String.format("%s/%s/%s_%s_%s_class_sched.pdf", c_PdfUrl, semesterValue, semesterValue, getTerm().getValue(), getYear()));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
