package data.scheedule.utils;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HtmlUnitHelper {
	
	public static HtmlElement getElementByValue(HtmlPage page, String value) {
		for (HtmlElement element : page.getHtmlElementDescendants()) {
			if (element.getAttribute("value") != null && element.getAttribute("value").equals(value)) {
				return element;
			}
		}
		return null;
	}
	
	public static HtmlElement getElementByText(HtmlPage page, String text) {
		for (HtmlElement element : page.getHtmlElementDescendants()) {
			if (element.asText().trim().equalsIgnoreCase(text.trim())) {
				return element;
			}
		}
		return null;
	}
	public static HtmlElement getElementByText(HtmlElement root, String text) {
		for (HtmlElement element : root.getHtmlElementDescendants()) {
			if (element.asText().trim().equalsIgnoreCase(text.trim())) {
				return element;
			}
		}
		return null;
	}

	public static HtmlElement getElementByClass(HtmlElement root, String tag, String classValue) {
		for (HtmlElement element : root.getElementsByTagName(tag)) {
			if (element.hasAttribute("class") && element.getAttribute("class").toLowerCase().contains(classValue.toLowerCase())) { 
				return element;
			}
		}
		return null;
	}
	
	public static HtmlElement getElementByTagAndAttributeValue(HtmlElement root, String tag, String attribute, String value) {
		for (HtmlElement element : root.getElementsByTagName(tag)) {
			if (element.hasAttribute(attribute) && element.getAttribute(attribute).equals(value)) { 
				return element;
			}
		}
		return null;
	}
	
	public static HtmlElement getElementByTagAndAttributeValue(HtmlPage page, String tag, String attribute, String value) {
		return getElementByTagAndAttributeValue(page.getBody(), tag, attribute, value);
	}
	
	public static List<HtmlElement> getElementsByClass(HtmlElement root, String tag, String classValue) {
		List<HtmlElement> elements = new ArrayList<HtmlElement>();
		for (HtmlElement element : root.getElementsByTagName(tag)) {
			if (element.hasAttribute("class") && element.getAttribute("class").toLowerCase().contains(classValue.toLowerCase())) {
				elements.add(element);
			}
		}
		return elements;
	}
	
	public static List<HtmlElement> getElementsByTagAndAttributeValue(HtmlElement root, String tag, String attribute, String value) {
		List<HtmlElement> elements = new ArrayList<HtmlElement>();
		for (HtmlElement element : root.getElementsByTagName(tag)) {
			if (element.hasAttribute(attribute) && element.getAttribute(attribute).equals(value)) {
				elements.add(element);
			}
		}
		return elements;
	}
		
	public static List<HtmlElement> getElementsByTagAndAttributeValue(HtmlPage page, String tag, String attribute, String value) {
		return getElementsByTagAndAttributeValue(page.getBody(), tag, attribute, value);
	}
	
	public static HtmlElement getElementByTagAndAttributeValue(HtmlElement root, String tag, String attribute, String value, int index) {
		List<HtmlElement> elements = getElementsByTagAndAttributeValue(root, tag, attribute, value);
		return index >= 0 && index < elements.size() ? elements.get(index) : null;
	}
	
	public static HtmlElement getElementByTagAndAttributeValue(HtmlPage page, String tag, String attribute, String value, int index) {
		return getElementByTagAndAttributeValue(page.getBody(), tag, attribute, value, index);
	}
		
	public static List<HtmlElement> getChildrenByTag(HtmlElement element, String tag) {
		List<HtmlElement> elements = new ArrayList<HtmlElement>();
		for (HtmlElement ele : element.getChildElements()) {
			if (ele.getTagName().equalsIgnoreCase(tag)) {
				elements.add(ele);
			}
		}
		return elements; 
	}
	
	public static HtmlElement getElementByTagName(HtmlElement element, String tagName, int index) {
		if (index < 0) return null;
		DomNodeList<HtmlElement> elements = element.getElementsByTagName(tagName);
		return (index >= elements.size()) 
					? null
					: elements.get(index);
	}
	
	public static HtmlElement lookup(HtmlPage page, String xPath, int index) {
		List<HtmlElement> results = lookup(page, xPath);
		return results.size() > index ? results.get(index) : null;
	}
	
	public static HtmlElement lookup(HtmlElement root, String xPath, int index) {
		List<HtmlElement> results = lookup(root, xPath);
		return results.size() > index ? results.get(index) : null;
	}
	
	public static List<HtmlElement> lookup(HtmlPage page, String xPath) {
		List<?> r = page.getByXPath(xPath);
		List<HtmlElement> results = new ArrayList<HtmlElement>();
		for (Object o : r) {
			results.add((HtmlElement)o);
		}
		return results;
	}
	
	public static List<HtmlElement> lookup(HtmlElement root, String xPath) {
		List<?> r = root.getByXPath(xPath);
		List<HtmlElement> results = new ArrayList<HtmlElement>();
		for (Object o : r) {
			results.add((HtmlElement)o);
		}
		return results;
	}
}
