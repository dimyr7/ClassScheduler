#include "Time.hpp"
namespace CourseInfo{
	/*
	 * ======================================================
	 * Object Creation
	 * ======================================================
	 */
	Time::Time(int hour, int minute){
		if(not Time::validHour(hour) or not Time::validMinute(minute)){
			this->_hour = 0;
			this->_minute = 0;
		}
		else{
			this->_hour = hour;
			this->_minute = minute;
		}

	}
	Time::Time(const Time& copy){
		this->_hour = copy._hour;
		this->_minute = copy._minute;
	}
	Time::~Time(){
		return;
	}
	Time& Time::operator=(const Time& copy){
		this->_hour = copy._hour;
		this->_minute = copy._minute;
		return *this;
	}

	
	/*
	 * ======================================================
	 * Getters and Setters
	 * ======================================================
	 */
	int Time::getHour() const {
		return _hour;
	}
	int Time::getMinute() const {
		return _minute;
	}
	bool Time::setHour(int hour){
		if(not Time::validHour(hour)){
			return false;
		}
		this->_hour = hour;
		return true;
	}
	bool Time::setMinute(int minute){
		if(not Time::validMinute(minute)){
			return false;
		}
		this->_minute = minute;
		return true;
	}

	/*
	 * ======================================================
	 * Static functions
	 * ======================================================
	 */
	bool Time::before(const Time* before, const Time* after){
		if(before->getHour() > after->getHour()){
			return false;
		}
		else if(before->getHour() < after->getHour()){
			return true;
		}
		else if(before->getMinute() >= after->getMinute()){
			return false;
		}
		return true;
	}
bool Time::validHour(int hour){
		return (hour <= 23) and (hour >= 0);
	}
	bool Time::validMinute(int minute){
		return (minute <= 59) and (minute >= 0);
	}
	/*
	 * ======================================================
	 * Additional Functionality
	 * ======================================================
	 */
	Time Time::operator+(const Time& second) const{
		Time t;
		bool minOverflow = false;
		if(this->_minute + second._minute> 59){
			minOverflow = true;	
			t._minute = (this->_minute + second._minute)%60;
		}
		t._hour = (this->_hour + second._hour)%24;
		if(minOverflow){
			t._hour++;
		}
		return t;
	}
	ostream& operator<<(ostream& os, const Time& time){
		string min;
		string hour;

		if(time._hour <10){
			hour = "0"+to_string(time._hour);
		}
		else{
			hour = to_string(time._hour);
		}

		if(time._minute < 10){
			min = "0"+to_string(time._minute);
		}
		else{
			min = to_string(time._minute);
		}

		os<<hour<<":"<<min;
		return os;
	}

}
