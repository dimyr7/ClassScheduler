package data.scheedule.common;

import java.util.ArrayList;
import java.util.List;

import data.scheedule.utils.StringBuilder;

public class Days {

	private List<Day> _days;
	
	public enum Day {
		Monday("M", 1),
		Tuesday("T", 2),
		Wednesday("W", 4),
		Thursday("R", 8),
		Friday("F", 16),
		Saturday("S", 32),
		Sunday("U", 64);
		
		private int _bitVal;
		public int getBitValue() { return _bitVal; }
		
		private String _val;
		public String getValue() { return _val; }
		
		private Day(String val, int bitVal) {
			_val = val;
			_bitVal = bitVal;
		}
	};
		
	public Days() {
		_days = new ArrayList<Day>();
	}
	
	public void set(Day day) {
		_days.add(day);
	}
	
	public boolean has(Day day) {
		return _days.contains(day);
	}
	
	public int getBitMask() {
		int mask = 0;
		for (Day day : _days) {
			mask |= day.getBitValue();
		}
		return mask;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Day day : _days) {
			if (!first) sb.append(" ");
			sb.append(day.getValue());
			first = false;
		}
		return sb.toString();
	}
	
	public boolean overlapsAny(Days days) {
		return (getBitMask() & days.getBitMask()) != 0;
	}
	
	@Override
	public boolean equals(Object other) {
		Days otherDays = (Days) other;
		if (otherDays == null) {
			return false;
		}
		
		return getBitMask() == otherDays.getBitMask();
	}
}
