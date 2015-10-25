package data.scheedule.common;

public class Times {

	private int _startTime;
	private int _endTime;
	
	public int getStartTime() { return _startTime; }
	public int getEndTime() { return _endTime; }
	
	public Times(int startTime, int endTime) {
		_startTime = startTime;
		_endTime = endTime;
		
		// TODO : Error check here? assert endTime > startTime?
	}
	
	@Override
	public boolean equals(Object other) {
		Times otherTimes = (Times) other;
		if (otherTimes == null) {
			return false;
		}
		
		return _startTime == otherTimes._startTime && _endTime == otherTimes._endTime;
	}
}
