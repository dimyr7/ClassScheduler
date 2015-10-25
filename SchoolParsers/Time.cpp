#include "Time.hpp"
namespace CourseInfo{
Time::Time(){
	this->_hour = 0;
	this->_minute = 0;
}

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

Time::~Time(){
	return;
}

bool Time::validHour(int hour){
	return (hour <= 23) and (hour >= 0);
}

bool Time::validMinute(int minute){
	return (minute <= 59) and (minute >= 0);
}

int Time::getHour(){
	return _hour;
}

int Time::getMinute(){
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

bool Time::before(Time* before, Time* after){
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
}
