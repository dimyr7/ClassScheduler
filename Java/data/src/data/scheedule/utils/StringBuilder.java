package data.scheedule.utils;

public class StringBuilder {
	private java.lang.StringBuilder _sb;
	
	public StringBuilder() {
		_sb = new java.lang.StringBuilder();
	}
	public StringBuilder(String str) {
		_sb = new java.lang.StringBuilder(str);
	}
	
	public StringBuilder appendf(String format, Object... args) { _sb.append(String.format(format, args)); return this; }
	
	public StringBuilder append(char c) { _sb.append(c); return this; }
	public StringBuilder append(char[] s) { _sb.append(s); return this; }
	public StringBuilder append(CharSequence s) { _sb.append(s); return this; }
	public StringBuilder append(String s) { _sb.append(s); return this; }
	public StringBuilder append(boolean b) { _sb.append(b); return this; }
	public StringBuilder append(double d) { _sb.append(d); return this; }
	public StringBuilder append(float f) { _sb.append(f); return this; }
	public StringBuilder append(int i) { _sb.append(i); return this; }
	public StringBuilder append(long l) { _sb.append(l); return this; }
	public StringBuilder append(Object o) { _sb.append(o); return this; }

	@Override
	public String toString() { return _sb.toString(); }
	
	public int length() { return _sb.length(); }
}
