#include "Semester.hpp"
#include <iostream>
/*
 * ======================================================
 * Object Creation
 * ======================================================
 */
Section::Semester::Semester(std::string year, std::string season, std::string name){
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

Section::Semester::~Semester(){
	return;
}

const std::string Section::Semester::monthsStr[13] = {"", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov",  "Dec" };



Section::Semester::Semester(const Semester& copy){
	this->_year = copy._year;
	this->_season = copy._season;
	this->_name = copy._name;

	for(int i = 0; i < Section::Semester::NUMOFDATESPECIFIER; i++){
		this->_startDate[i] = copy._startDate[i];
		this->_endDate[i]   = copy._endDate[i];

	}
}

Section::Semester& Section::Semester::operator=(const Semester& copy){
	this->_year = copy._year;
	this->_season = copy._season;
	this->_name = copy._name;

	for(int i = 0; i < Section::Semester::NUMOFDATESPECIFIER; i++){
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
void Section::Semester::setYear(std::string year){
	this->_year = year;
}
void Section::Semester::setSeason(std::string season){
	this->_season = season;
}
void Section::Semester::setName(std::string name){
	this->_name = name;
}

bool Section::Semester::setDates(const int (&start)[3], const int (&end)[3]){
	if(not Section::Semester::before(start, end)){ 
		return false;
	}
	for(int i = 0; i < Section::Semester::NUMOFDATESPECIFIER; i++){
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

std::string Section::Semester::getYear() const{
	return _year;
}
std::string Section::Semester::getSeason() const{
	return _season;
}
std::string Section::Semester::getName() const{
	return _name;
}

int Section::Semester::getDay(bool start) const{
	if(start){
		return this->_startDate[0];
	}
	return this->_endDate[0];
}
int Section::Semester::getMonth(bool start) const{
	if(start){
		return this->_startDate[1];
	}
	return this->_endDate[1];
}
int Section::Semester::getYear(bool start) const{
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

bool Section::Semester::validDay(int day){
	return (day >= 1) and (day <= 31);
}
bool Section::Semester::validMonth(int month){
	return (month >= 1) and (month <= 12);
}
bool Section::Semester::validYear(int year){
	return (year >= 1990) and (year <= 2100);
}

bool Section::Semester::before(const int (&first)[3], const int (&second)[3]){
	if(not Section::Semester::validDay(first[0])){
		return false;
	}
	else if(not Section::Semester::validDay(second[0])){
		return false;
	}
	else if(not Section::Semester::validMonth(first[1])){
		return false;
	}
	else if(not Section::Semester::validMonth(second[1])){
		return false;
	}
	else if(not Section::Semester::validYear(first[2])){
		return false;
	}
	else if(not Section::Semester::validYear(second[2])){
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

std::ostream& operator<<(std::ostream& os, const Section::Semester& semester){
	os << semester.getYear() << " - " << semester.getSeason() << " - " << semester.getName() <<std::endl;
	os << Section::Semester::monthsStr[semester.getMonth(true)] << " " << semester.getDay(true) << ", " << semester.getYear(true) << std::endl;
	os << Section::Semester::monthsStr[semester.getMonth(false)] << " " << semester.getDay(false) << ", " << semester.getYear(false) << std::endl;
	return os;
}
