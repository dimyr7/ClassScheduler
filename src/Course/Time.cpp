#include "Course/Time.hpp"
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


/*
 * ======================================================
 * Static functions
 * ======================================================
 */
bool Time::before(const Time* first, const Time* second){
	if(first->getHour() > second->getHour()){
		return false;
	}
	else if(first->getHour() < second->getHour()){
		return true;
	}
	else if(first->getMinute() >= second->getMinute()){
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

std::ostream& operator<<(std::ostream& os, const Time& time){
	
	std::string min = std::to_string(time._minute);
	std::string hour = std::to_string(time._hour);

	if(time._hour <10){
		hour = "0"+std::to_string(time._hour);
	}

	if(time._minute < 10){
		min = "0"+std::to_string(time._minute);
	}

	os<<hour<<":"<<min;
	return os;
}
