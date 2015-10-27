#include "Week.hpp"
#include "Time.hpp"
/*
 * ======================================================
 * Object Creation
 * ======================================================
 */

Section::Week::Week(){
	for(auto i : this->_times){
		for(auto j: i){
			j = NULL;
		}
	}
}
Section::Week::~Week(){
	for(auto i :this->_times){
		for(auto j : i){
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
