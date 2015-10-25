package data.scheedule.illinois;

import org.w3c.dom.Document;

import data.scheedule.base.ConfigBase;
import data.scheedule.utils.XmlHelper;

public class Config extends ConfigBase {
	
	public static final String ExplorerBaseUrl = "http://courses.illinois.edu/cisapp/explorer/schedule";

	@Override
	public String getSchoolAbbreviation() {
		return "illinois";
	}

	
	public Config(Term term, String year) {
		super(term, year);
		// TODO Auto-generated constructor stub
	}
	
	private String getTermString() {
		return getTerm().toString().toLowerCase();
	}

	public String getBaseSemesterUrl() {
		return String.format("%s/%s/%s", ExplorerBaseUrl, getYear(), getTermString());
	}
	
	public String getRelativeSemesterUrl(String relativePath) {
		return String.format("%s/%s", getBaseSemesterUrl(), relativePath);
	}
	
	public Document loadXml(String url) {
		try {
			Document xml = XmlHelper.loadXmlDocument(url);
			return xml;
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
}
