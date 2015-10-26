#include "Semester.hpp"
namespace CourseInfo{
	/*
	 * ======================================================
	 * Object Creation
	 * ======================================================
	 */
	Semester::Semester(string year, string season, string name){
		this->_year = year;
		this->_season = season;
		this->_name = name;
		_startDate = {{1, 1, 2000}};
		_endDate = {{31, 12, 2000}};
	}
	Semester::~Semester(){
		return;
	}
	Semester::Semester(const Semester& copy){
		this->_year = copy._year;
		this->_season = copy._season;
		this->_name = copy._name;

		std::copy(copy._startDate.begin(), copy._startDate.end(), this->_startDate.begin());
		std::copy(copy._endDate.begin(), copy._endDate.end(), this->_endDate.begin());
	}
	Semester& Semester::operator=(const Semester& copy){
		this->_year = copy._year;
		this->_season = copy._season;
		this->_name = copy._name;

		std::copy(copy._startDate.begin(), copy._startDate.end(), this->_startDate.begin());
		std::copy(copy._endDate.begin(), copy._endDate.end(), this->_endDate.begin());
		return *this;
	}

	/*
	 * ======================================================
	 * Setters
	 * ======================================================
	 */
	void Semester::setYear(string year){
		this->_year = year;
	}
	void Semester::setSeason(string season){
		this->_season = season;
	}
	void Semester::setName(string name){
		this->_name = name;
	}


	bool Semester::setDate(int day, int month, int year, bool start){
		if(not validDay(day) or not validMonth(month) or not validYear(year)){
			return false;
		}
		if(start){
			this->_startDate[0] = day;
			this->_startDate[1] = month;
			this->_startDate[2] = year;
		}
		else{
			this->_endDate[0] = day;
			this->_endDate[1] = month;
			this->_endDate[2] = year;
		}
		return true;
	}
	bool Semester::setDay(int day, bool start){
		if(not validDay(day)){
			return false;
		}
		if(start){
			this->_startDate[0] = day;
		}
		else{
			this->_endDate[0] = day;
		}
		return true;
	}
	bool Semester::setMonth(int month, bool start){
		if(not validMonth(month)){
			return false;
		}
		if(start){
			this->_startDate[1] = month;
		}
		else{
			this->_endDate[1] = month;
		}
		return true;
	}
	bool Semester::setYear(int year, bool start){
		if(not validYear(year)){
			return false;
		}
		if(start){
			this->_startDate[2] = year;
		}
		else{
			this->_endDate[2] = year;
		}
		return true;
	}

	/*
	 * ======================================================
	 * Getters
	 * ======================================================
	 */

	string Semester::getYear() const{
		return _year;
	}
	string Semester::getSeason() const{
		return _season;
	}
	string Semester::getName() const{
		return _name;
	}

	int Semester::getDay(bool start) const{
		if(start){
			return this->_startDate[0];
		}
		return this->_endDate[0];
	}
	int Semester::getMonth(bool start) const{
		if(start){
			return this->_startDate[1];
		}
		return this->_endDate[1];
	}
	int Semester::getYear(bool start) const{
		if(start){
			return this->_startDate[2];
		}
		return this->_endDate[2];
	}
	/*
	 * ======================================================
	 * Validators
	 * ======================================================
	 */

	bool Semester::validDay(int day){
		return (day >= 1) and (day <= 31);
	}
	bool Semester::validMonth(int month){
		return (month >= 1) and (month <= 12);
	}
	bool Semester::validYear(int year){
		return (year >= 1990) and (year <= 2100);
	}
}

