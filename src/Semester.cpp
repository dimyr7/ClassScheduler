#include <Semester.hpp>
#include <iostream>
/*
 * ======================================================
 * Object Creation
 * ======================================================
 */
Semester::Semester(std::string year, std::string season, std::string name){
	this->_year = year;
	this->_season = season;
	this->_name = name;

	this->_startDate[0] = 1;
	this->_startDate[1] = 1;
	this->_startDate[2] = 2000;

	this->_endDate[0] = 31;
	this->_endDate[1] = 12;
	this->_endDate[2] = 2000;
	
	this->_isSet = false;
}

Semester::~Semester(){
	return;
}

const std::string Semester::monthsStr[12] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",  "Dec" };



Semester::Semester(const Semester& copy){
	this->_year = copy._year;
	this->_season = copy._season;
	this->_name = copy._name;

	for(int i = 0; i < 3; i++){
		this->_startDate[i] = copy._startDate[i];
		this->_endDate[i]   = copy._endDate[i];

	}
	this->_isSet = false;
}

Semester& Semester::operator=(const Semester& copy){
	this->_year = copy._year;
	this->_season = copy._season;
	this->_name = copy._name;

	for(int i = 0; i < 3; i++){
		this->_startDate[i] = copy._startDate[i];
		this->_endDate[i]   = copy._endDate[i];
	}

	this->_isSet = false;
	return *this;
}

bool Semester::setDates(const int (&start)[3], const int (&end)[3]){
	// Setting dates multiple times is not allowed
	if(this->_isSet){
		return false;
	}

	// Start date should actually be before the end date
	if(not Semester::before(start, end)){ 
		return false;
	}
	// TODO validate the dates are actual dates
	for(int i = 0; i < 3 ; i++){
		this->_startDate[i] = start[i];
		this->_endDate[i]   = end[i];
	}
	this->_isSet = true;
	return true;

}

/*
 * ======================================================
 * Getters
 * ======================================================
 */

std::string Semester::getYear() const{
	return _year;
}
std::string Semester::getSeason() const{
	return _season;
}
std::string Semester::getName() const{
	return _name;
}

int Semester::getStartDay() const{
	return this->_startDate[0];
}
int Semester::getStartMonth() const{
	return this->_startDate[1];
}
int Semester::getStartYear() const{
	return this->_startDate[2];
}

int Semester::getEndDay() const{
	return this->_endDate[0];
}

int Semester::getEndMonth() const{
	return this->_endDate[1];
}

int Semester::getEndYear() const{
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

bool Semester::before(const int (&first)[3], const int (&second)[3]){
	// Is the start day valid
	if(not Semester::validDay(first[0])){
		return false;
	}
	// Is the end day valid
	else if(not Semester::validDay(second[0])){
		return false;
	}
	// Is the start month valid
	else if(not Semester::validMonth(first[1])){
		return false;
	}
	// Is the end month valid
	else if(not Semester::validMonth(second[1])){
		return false;
	}
	// Is the stary year valid
	else if(not Semester::validYear(first[2])){
		return false;
	}
	// Is the end year valid
	else if(not Semester::validYear(second[2])){
		return false;
	}
	// Check the years of the dates
	else if(first[2] > second[2]){
		return false;
	}
	else if(first[2] < second[2]){
		return true;
	}
	// Check the months of the dates
	else if(first[1] > second[1]){
		return false;
	}
	else if(first[1] < second[1]){
		return true;
	}
	// Check the days of the dates
	else if(first[0] >= second[0]){
		return false;
	}
	else{
		return true;
	}
}

std::ostream& operator<<(std::ostream& os, const Semester& semester){
	os << semester.getYear() << " - " << semester.getSeason() << " - " << semester.getName() <<std::endl;
	os << "Starting Date: " << Semester::monthsStr[semester.getStartMonth()] << " " << semester.getStartDay() << ", " << semester.getStartYear() << std::endl;
	os << "Ending Date:: " << Semester::monthsStr[semester.getEndMonth()] << " " << semester.getEndDay() << ", " << semester.getEndYear() << std::endl;
	return os;
}
