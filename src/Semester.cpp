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

}

Semester::~Semester(){
	return;
}

const std::string Semester::monthsStr[13] = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",  "Dec" };



Semester::Semester(const Semester& copy){
	this->_year = copy._year;
	this->_season = copy._season;
	this->_name = copy._name;

	for(int i = 0; i < Semester::NUMOFDATESPECIFIER; i++){
		this->_startDate[i] = copy._startDate[i];
		this->_endDate[i]   = copy._endDate[i];

	}
}

Semester& Semester::operator=(const Semester& copy){
	this->_year = copy._year;
	this->_season = copy._season;
	this->_name = copy._name;

	for(int i = 0; i < Semester::NUMOFDATESPECIFIER; i++){
		this->_startDate[i] = copy._startDate[i];
		this->_endDate[i]   = copy._endDate[i];
	}
	return *this;
}

/*
 * ======================================================
 * Setters
 * ======================================================
 */
void Semester::setYear(std::string year){
	this->_year = year;
}
void Semester::setSeason(std::string season){
	this->_season = season;
}
void Semester::setName(std::string name){
	this->_name = name;
}

bool Semester::setDates(const int (&start)[3], const int (&end)[3]){
	if(not Semester::before(start, end)){ 
		return false;
	}
	for(int i = 0; i < Semester::NUMOFDATESPECIFIER; i++){
		this->_startDate[i] = start[i];
		this->_endDate[i]   = end[i];
	}
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

bool Semester::before(const int (&first)[3], const int (&second)[3]){
	if(not Semester::validDay(first[0])){
		return false;
	}
	else if(not Semester::validDay(second[0])){
		return false;
	}
	else if(not Semester::validMonth(first[1])){
		return false;
	}
	else if(not Semester::validMonth(second[1])){
		return false;
	}
	else if(not Semester::validYear(first[2])){
		return false;
	}
	else if(not Semester::validYear(second[2])){
		return false;
	}
	else if(first[2] > second[2]){
		return false;
	}
	else if(first[2] < second[2]){
		return true;
	}
	else if(first[1] > second[1]){
		return false;
	}
	else if(first[1] < second[1]){
		return true;
	}
	else if(first[0] >= second[0]){
		return false;
	}
	else{
		return true;
	}
}

std::ostream& operator<<(std::ostream& os, const Semester& semester){
	os << semester.getYear() << " - " << semester.getSeason() << " - " << semester.getName() <<std::endl;
	os << Semester::monthsStr[semester.getMonth(true)] << " " << semester.getDay(true) << ", " << semester.getYear(true) << std::endl;
	os << Semester::monthsStr[semester.getMonth(false)] << " " << semester.getDay(false) << ", " << semester.getYear(false) << std::endl;
	return os;
}
