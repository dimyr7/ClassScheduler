// =========================================
// XmlHelper.java
// 
// Copyright (C) Scheedule Development Team
// =========================================
package data.scheedule.utils;
import java.io.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class XmlHelper
{
	private final static String c_Encoding = "ISO-8859-1";

	public static String escapeXml(int i)
	{
		return Integer.toString(i);
	}
	public static String escapeXml(double d)
	{
		return Double.toString(d);
	}
    public static String escapeXml(String str)
    {
		String escapedStr = "";
		if(str != null)
		{
			escapedStr = str.replaceAll("&", "&amp;");
			escapedStr = escapedStr.replaceAll("<", "&lt;");
			escapedStr = escapedStr.replaceAll(">", "&gt;");
			escapedStr = escapedStr.replaceAll("\"", "&quot;");
			escapedStr = escapedStr.replaceAll("'", "&apos;");
		}
		
		return escapedStr;
   }
   
   public static String getTextValue(Element parentElement, String tagName) {
		NodeList childNodes = parentElement.getElementsByTagName(tagName);
		if(childNodes.getLength() > 0) {
			return ((Element)childNodes.item(0)).getTextContent().trim();
		}
		
		return null;
   }
   
   // thanks java, don't be OO or anything any make Document and Element implement an interface or anything like that..
   public static String getTextValue(Document parentElement, String tagName) {	
	   NodeList childNodes = parentElement.getElementsByTagName(tagName);
		if(childNodes.getLength() > 0) {
			return ((Element)childNodes.item(0)).getTextContent().trim();
		}
		
		return null;
   }
   
   public static String getAttributeValue(Element element, String attributeName) {
	   return element.hasAttribute(attributeName) ? element.getAttributeNode(attributeName).getTextContent() : null;
   }
   
   public static String getAttributeValue(Element parentElement, String tagName, String attributeName) {
	   NodeList childNodes = parentElement.getElementsByTagName(tagName);
	   if(childNodes.getLength() > 0) {
		   Element childNode = (Element)childNodes.item(0);
		   return getAttributeValue(childNode, attributeName);
	   }
	   
	   return null;
   }
   
   public static String getAttributeValue(Document parentElement, String tagName, String attributeName) {
	   NodeList childNodes = parentElement.getElementsByTagName(tagName);
	   if(childNodes.getLength() > 0) {
		   Element childNode = (Element)childNodes.item(0);
		   return getAttributeValue(childNode, attributeName);
	   }
	   
	   return null;
   }
  
   public static Document loadXmlDocument(String path, int numRetries)
   {
		Document xmlDocument = null;
		int retries = 0;
		while(retries < numRetries && (xmlDocument = XmlHelper.loadXmlDocument(path)) == null)
		{
			retries++;
			System.out.println ("Xml read failed... ["+retries+"]..");
		}
		
		return xmlDocument;
   }
   
   public static Document loadXmlDocument(String path)
   {
		try
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			
			return docBuilder.parse(path);
		}
		catch (SAXParseException err) 
		{
			System.out.println ("** Parsing error" + ", line " + err.getLineNumber () + ", uri " + err.getSystemId ());
			System.out.println(" " + err.getMessage ());

        }
		catch (SAXException e) 
		{
			Exception x = e.getException();
			((x == null) ? e : x).printStackTrace ();
        }
		catch (ParserConfigurationException e)
		{
		}
		catch (IOException e)
		{
		}
		return null;
   }
   
   public static PrintWriter createForWriting(String directoryPath, String fileName)
   {
		try
		{
			File directory = new File(directoryPath);
			directory.mkdirs();
		
			FileWriter fileWriter = new FileWriter(directoryPath + "/" + fileName);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
			PrintWriter writer = new PrintWriter(bufferedWriter);
			writer.println("<?xml version=\"1.0\" encoding=\""+c_Encoding+"\"?>");
			return writer;
		}
		catch(FileNotFoundException e)
		{
			return null;
		}
		catch(IOException e)
		{
			return null;
		}
   }
}
