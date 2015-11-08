#include "Week.hpp"

/*
 * ======================================================
 * Object Creation
 * ======================================================
 */

const std::string Week::daysStr[Week::DAYSINWEEK] = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

Week::Week(){
	for(int i = 0; i < Week::DAYSINWEEK; i++){
		for(int j = 0; j < Week::TIMESINDAY; j++){
			this->_times[i][j] = NULL;
		}
	}
}
Week::~Week(){
	for(int i = 0; i < Week::DAYSINWEEK; i++){
		for(int j = 0; j < Week::TIMESINDAY; j++){
			delete this->_times[i][j];
			this->_times[i][j] = NULL;
		}
	}
}
Week::Week(const Week& copy){
	for(int i = 0; i < Week::DAYSINWEEK; i++){
		for(int j = 0; j < Week::TIMESINDAY; j++){
			this->_times[i][j] = new Time(*copy._times[i][j]);
		}
	}
}

Week& Week::operator=(const Week& copy){
	for(int i = 0; i < Week::DAYSINWEEK; i++){
		for(int j = 0; j < Week::TIMESINDAY; j++){
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

bool Week::setDay(Day day, Time* const start, Time* const end){
	if(not Time::before(start, end)){
		return false;
	}
	this->_times[day][0] = start;
	this->_times[day][1] = end;
	return true;
}

void Week::unsetDay(Day day){
	delete _times[day][0];
	delete _times[day][1];
	_times[day][0] = NULL;
	_times[day][1] = NULL;
}

Time* Week::getTimes(Day day, bool start) const {
	if(start){
		return this->_times[day][0];
	}
	return this->_times[day][1];
}

std::ostream& operator<<(std::ostream& os, const Week& week){
	for(int i = 0; i < Week::DAYSINWEEK; i++){
		Time* start = week.getTimes((Week::Day)i, true);
		Time* end   = week.getTimes((Week::Day)i, false);
		if(start == NULL){
			continue;
		}
		os << Week::daysStr[i] << "\t" << *start << " - " << *end << std::endl;
	}
	
	return os;
}
