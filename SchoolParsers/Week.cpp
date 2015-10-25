#include "Week.hpp"
Week::Week(){
	for(auto it = this->_times.begin(); it != this->_times.end(); it++){
		for(auto is = it[0].begin(); is != it[0].end(); is++){
			is = NULL;
		}
	}
}
Week::~Week(){
	for(auto it = this->_times.begin(); it != this->_times.end(); it++){
		for(auto is = it[0].begin(); is != it[0].end(); is++){
			delete is;
			is = NULL
		}
	}
}

bool Week::setDay(Day day, Time* start, Time* end){
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

array<Time*, 2> Week::getTimes(Day day){
	return this->_times[day];
}
