package data.scheedule.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringEscapeUtils;


public class StringUtils {

	public static boolean isNullOrEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static boolean isNullOrEmptyTrimmed(String str) {
		return str == null || isNullOrEmpty(str.trim());
	}
	
	public static boolean startsWithDigit(String str) {
		if (isNullOrEmpty(str)) return false;
		return Character.isDigit(str.charAt(0));
	}
	
	public static String clean(String str) {
		if (str == null) return str;
		
		return StringEscapeUtils.unescapeHtml(str).replace("&apos;", "'").trim();
	}
	
	public static String trim(String str, String trimValues) {
		if (isNullOrEmpty(str)) return str;
		
		while (str.length() > 0 && trimValues.indexOf(str.charAt(0)) >= 0) {
			str = str.substring(1);
		}
		
		while (str.length() > 0 && trimValues.indexOf(str.charAt(str.length() - 1)) >= 0) {
			str = str.substring(0, str.length() - 2);
		}
		
		return str;
	}
	
	public static String toProperCase(String str) {
		if (str == null || str.length() == 0) return str;

		StringBuilder resultBuilder = new StringBuilder();
		char[] letters = str.toLowerCase().toCharArray();

		boolean previousCharWasWordBreak = true;
		for(int i = 0; i < letters.length; i++)
		{
			char ch = letters[i];
			resultBuilder.append(previousCharWasWordBreak ? Character.toUpperCase(ch) : ch);
			
			previousCharWasWordBreak = ch == ' ';
		}

		String resultString = resultBuilder.toString();
		resultString = resultString.replaceAll(" Ii", " II");
		resultString = resultString.replaceAll(" Iii", " III");
		resultString = resultString.replaceAll(" Iv", " IV");
		resultString = resultString.replaceAll("And ", "and ");
		resultString = resultString.replaceAll("Of ", "of ");
		resultString = resultString.replaceAll("On ", "on ");
		resultString = resultString.replaceAll("The ", "the ");
		
		return resultString;
	}
	
	public static int tryParse(String str) {
		return tryParse(str, 0);
	}
	
	public static int tryParse(String str, int fallback) {
		int value = fallback;
		try {
			value = Integer.parseInt(str.trim());
		}
		catch(Exception e) {}
		return value;
	}
	
	public static String padLeft(String str, int count, String value) {
		StringBuilder sb = new StringBuilder();
		while(count-- > 0) {
			sb.append(value);
		}
		sb.append(str);
		return sb.toString();
	}
	
	public static String substring(String baseString, int fromIndex) {
		if (isNullOrEmpty(baseString) || fromIndex >= baseString.length()) return "";
		return baseString.substring(fromIndex);
	}
	
	public static String substring(String baseString, int fromIndex, int toIndex) {
		if (isNullOrEmpty(baseString) || fromIndex >= baseString.length() || toIndex >= baseString.length()) return "";
		return baseString.substring(fromIndex, toIndex);
	}
	
	public static String urlEncode(String str) {
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return str;
		}
	}
	
	public static boolean containsIgnoreCase(String string, String contains) {
		if(string == null || contains == null) {
			return false;
		}
		
		return string.toLowerCase().contains(contains.toLowerCase());
	}
	
	public static boolean equalsAny(String string, String[] canEqual) {
		for(String equal : canEqual) {
			if(string.equals(equal)) {
				return true;
			}
		}
		
		return false;
	}
}
