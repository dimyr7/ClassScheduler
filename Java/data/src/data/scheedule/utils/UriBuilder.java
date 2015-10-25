package data.scheedule.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class UriBuilder {

	private String _baseUri;
	private ArrayList<String> _paths;
	private ArrayList<NameValuePair> _params;
	
	
	public UriBuilder(String baseUri) {
		_baseUri = baseUri;
		_paths = new ArrayList<String>();
		_params = new ArrayList<NameValuePair>();
	}
	
	public UriBuilder add(String path) {
		_paths.add(path);
		return this;
	}
	
	public UriBuilder add(String key, String value) {
		return add(new BasicNameValuePair(key, value));
	}
	
	public UriBuilder add(NameValuePair param) {
		_params.add(param);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder uri = new StringBuilder(_baseUri);
		
		boolean first = true;
		for (String path : _paths) {
			uri.append(first  ? "" : "/").append(urlEncode(path));
			first = false;
		}
		
		first = true;
		for (NameValuePair param : _params) {
			uri.append(first ? "?" : "&").append(urlEncode(param.getName())).append("=").append(urlEncode(param.getValue()));
			first = false;
		}
		
		return uri.toString();
	}
	
	protected String getUrlEncoding() { return "UTF-8"; }
	protected String urlEncode(String value) {
		try {
			return URLEncoder.encode(value, getUrlEncoding());
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}
	
}
