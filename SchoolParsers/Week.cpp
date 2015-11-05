#include "Week.hpp"
#include "Time.hpp"
#include <iostream>

/*
 * ======================================================
 * Object Creation
 * ======================================================
 */

const std::string Section::Week::daysStr[Section::Week::DAYSINWEEK] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

Section::Week::Week(){
	for(int i = 0; i < Section::Week::DAYSINWEEK; i++){
		for(int j = 0; j < Section::Week::TIMESINDAY; j++){
			this->_times[i][j] = NULL;
		}
	}
}
Section::Week::~Week(){
	for(int i = 0; i < Section::Week::DAYSINWEEK; i++){
		for(int j = 0; j < Section::Week::TIMESINDAY; j++){
			delete this->_times[i][j];
			this->_times[i][j] = NULL;
		}
	}
}
Section::Week::Week(const Week& copy){
	for(int i = 0; i < Section::Week::DAYSINWEEK; i++){
		for(int j = 0; j < Section::Week::TIMESINDAY; j++){
			this->_times[i][j] = new Time(*copy._times[i][j]);
		}
	}
}

Section::Week& Section::Week::operator=(const Week& copy){
	for(int i = 0; i < Section::Week::DAYSINWEEK; i++){
		for(int j = 0; j < Section::Week::TIMESINDAY; j++){
			this->_times[i][j] = new Time(*copy._times[i][j]);
		}
	}
	return *this;
}
/*
 * ======================================================
 * Getters & Setters
 * ======================================================
 */

bool Section::Week::setDay(Day day, Time* const start, Time* const end){
	if(not Time::before(start, end)){
		return false;
	}
	this->_times[day][0] = start;
	this->_times[day][1] = end;
	return true;
}

void Section::Week::unsetDay(Day day){
	delete _times[day][0];
	delete _times[day][1];
	_times[day][0] = NULL;
	_times[day][1] = NULL;
}

Section::Week::Time* Section::Week::getTimes(Day day, bool start) const {
	if(start){
		return this->_times[day][0];
	}
	return this->_times[day][1];
}

std::ostream& operator<<(std::ostream& os, const Section::Week& week){
	for(int i = 0; i < Section::Week::DAYSINWEEK; i++){
		Section::Week::Time* start = week.getTimes((Section::Week::Day)i, true);
		Section::Week::Time* end   = week.getTimes((Section::Week::Day)i, false);
		if(start == NULL){
			continue;
		}
		os << Section::Week::daysStr[i] << "\t" << *start << " - " << *end << std::endl;
	}
	
	return os;
}
