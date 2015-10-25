package data.scheedule.base;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import data.scheedule.utils.XmlHelper;

/**
 * Configuration Base class which contains basic configuration functionality such as 
 * writing to XML output file, tracking a debugging file, etc... 
 * @author Mike Barker
 */
public abstract class ConfigBase {
	
	/**********************************************************************
	 * Helper Classes/Enums
	 **********************************************************************/ 
	
	/**
	 * Generalized enumeration for the Term, i.e. Spring, Fall, Summer, etc...
	 */
	public enum Term {
		Spring("spring"),
		Summer("summer"),
		Fall("fall"),
		Autumn("autumn"),
		Winter("winter");	// TODO : Any more...
		
		private String _value;
		public String getValue() { return _value; }
		private Term(String value) { _value = value; } 
	}
	
	/**********************************************************************
	 * Abstract Member Functions
	 **********************************************************************/ 
	
	/**
	 * @return The school abbreviation, e.g. uillinois, udub, uic, etc...
	 */
	public abstract String getSchoolAbbreviation();
	
	/**********************************************************************
	 * Members
	 **********************************************************************/ 

	private Term _term;
	private String _year;
	
	private PrintWriter _output;
	private PrintWriter _debug;

	/**
	 * @return The specified Term this is config is for
	 */
	public Term getTerm()   { return _term; }
	
	/**
	 * @return The specified Year this config is for
	 */
	public String getYear() { return _year; }

	/**********************************************************************
	 * .ctor
	 **********************************************************************/ 
	
	protected ConfigBase(Term term, String year) {
		_term = term;
		_year = year;
		
		initializeXml();
		initializeDeubg();
	}
	
	/**********************************************************************
	 * Public Functions
	 **********************************************************************/ 
	
	/**
	 * Prints the given string to the xml file
	 * @param str String to print
	 */
	public void writeXml(String str) { _output.print(str); }
	
	/**
	 * Prints the given format to the xml file
	 * @param str Format string to print
	 * @param args Format string arguments
	 */
	public void writeXml(String str, Object...args) { _output.printf(str, args); }
	
	/**
	 * Prints the given string to the debug file 
	 * @param str String to print
	 */
	public void writeDebug(String str) { _debug.print(str); }
	
	/**
	 * Prints the given format to the debug file
	 * @param str Format string to print
	 * @param args Format string arguments
	 */
	public void writeDebug(String str, Object... args) { _debug.printf(str, args); }
	
	/**
	 * Closes up the files
	 */
	public void close() {
		_output.close();
		_debug.close();
	}
	
	/**********************************************************************
	 * Private (Helper) Functions
	 **********************************************************************/ 
	
	/**
	 * Initializes the XML file
	 */
	private void initializeXml() {
		_output = XmlHelper.createForWriting("./output/", String.format("%s_%s%s_CourseData.xml", getSchoolAbbreviation(), _term.getValue(), _year));
	}
	
	/**
	 * Initializes the Debug file
	 */
	private void initializeDeubg() {
		try {
			_debug = new PrintWriter(String.format("./output/%s_%s%s_Debug.xml", getSchoolAbbreviation(), _term.getValue(), _year));
		} catch (FileNotFoundException e) { e.printStackTrace(); }	
	}
}
