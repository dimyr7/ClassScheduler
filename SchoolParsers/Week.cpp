#include "Week.hpp"

namespace CourseInfo{
	/*
	 * ======================================================
	 * Object Creation
	 * ======================================================
	 */

	Week::Week(){
		for(auto i : this->_times){
			for(auto j: i){
				j = NULL;
			}
		}
	}
	Week::~Week(){
		for(auto i :this->_times){
			for(auto j : i){
				delete j;
				j = NULL;
			}
		}
	}
	Week::Week(const Week& copy){
		std::copy(copy._times.begin(), copy._times.end(), this->_times.begin());
	}

	Week& Week::operator=(const Week& copy){
		std::copy(copy._times.begin(), copy._times.end(), this->_times.begin());
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

	const Time* Week::getTimes(Day day, bool start) const {
		if(start){
			return this->_times[day][0];
		}
		return this->_times[day][1];
	}
}
