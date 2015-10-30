#include "Week.hpp"
#include "Time.hpp"
#include <iostream>
#define DAYSINWEEK 7
/*
 * ======================================================
 * Object Creation
 * ======================================================
 */
const std::array<std::string, 7> Section::Week::daysStr = {{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"}};	

Section::Week::Week(){
	for(auto& i : this->_times){
		for(auto& j: i){
			j = NULL;
		}
	}
}
Section::Week::~Week(){
	for(auto& i :this->_times){
		for(auto& j : i){
			delete j;
			j = NULL;
		}
	}
}
Section::Week::Week(const Week& copy){
	std::copy(copy._times.begin(), copy._times.end(), this->_times.begin());
}

Section::Week& Section::Week::operator=(const Week& copy){
	std::copy(copy._times.begin(), copy._times.end(), this->_times.begin());
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
	for(int i = 0; i < DAYSINWEEK; i++){
		Section::Week::Time* start = week.getTimes((Section::Week::Day)i, true);
		Section::Week::Time* end   = week.getTimes((Section::Week::Day)i, false);
		if(start == NULL){
			continue;
		}
		os << Section::Week::daysStr[i] << "\t" << *start << " - " << *end << std::endl;
	}
	
	return os;
}
