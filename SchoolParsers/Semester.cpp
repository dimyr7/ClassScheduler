#include "Semester.hpp"
Semester::Semester(string year, string season, string name){
	this->_year = year;
	this->_season = season;
	this->_name = name;
}
Semester::~Semester(){
	return;
}


void Semester::setYear(string year){
	this->_year = year;
}
void Semester::setSeason(string season){
	this->_season = season;
}
void Semester::setName(string name){
	this->_name = name;
}


bool Semester::setStartDate(int day, int month, int year){
	if(not validDay(day) or not validMonth(month) or not validYear(year)){
		return false;
	}
	this->_startDate[0] = day;
	this->_startDate[1] = month;
	this->_startDate[2] = year;
	return true;
}
bool Semester::setStartDay(int day){
	if(not validDay(day)){
		return false;
	}
	this->_startDate[0] = day;
	return true;
}
bool Semester::setStartMonth(int month){
	if(not validMonth(month)){
		return false;
	}
	this->_startDate[1] = month;
	return true;
}
bool Semester::setStartYear(int year){
	if(not validYear(year)){
		return false;
	}
	this->_startDate[2] = year;
	return true;
}


bool Semester::setEndDate(int day, int month, int year){
	if(not validDay(day) or not validMonth(month) or not validYear(year)){
		return false;
	}
	this->_endDate[0] = day;
	this->_endDate[1] = month;
	this->_endDate[2] = year;
	return true;
}
bool Semester::setEndDay(int day){
	if(not validDay(day)){
		return false;
	}
	this->_endDate[0] = day;
	return true;
}
bool Semester::setEndMonth(int month){
	if(not validMonth(month)){
		return false;
	}
	this->_endDate[1] = month;
	return true;
}
bool Semester::setEndYear(int year){
	if(not validYear(year)){
		return false;
	}
	this->_endDate[2] = year;
	return true;
}


string Semester::getYear(){
	return _year;
}
string Semester::getSeason(){
	return _season;
}
string Semester::getName(){
	return _name;
}

array<int, 3> Semester::getStartDate(){
	return this->_startDate;
}
array<int, 3> Semester::getEndDate(){
	return this->_endDate;
}


bool Semester::validDay(int day){
	return (day >= 1) and (day <= 31);
}
bool Semester::validMonth(int month){
	return (month >= 1) and (month <= 12);
}
bool Semester::validYear(int year){
	return (year >= 1990) and (year <= 2100);
}



