#include "Section.hpp"

Section::Section(){
	this->_sectionName = "";
	this->_sectionType = "";
	this->_description = "";
	
	this->_instructor = NULL;
	this->_daysOfWeek = NULL;
	this->_dates = NULL;
	this->_bulding = NULL;
}

void Section::setSectionName(string sectionName){
	this->_sectionName = sectionName;
}
void Section::setSectionType(string sectionType){
	this->_sectionType = sectionType;
}
void Section::setDescription(string description){
	this->_description = description;
}

void Section::setInstructor(Instructor* instructor){
	this->_instructor = instructor;
}
void Section::setDaysOfWeek(Week* daysOfWeek){
	this->_daysOfWeek = daysOfWeek;
}
void Section::setSemester(Semester* semester){
	this->_dates = semester;
}
void Section::setLocation(Location* building){
	this->_bulding = building;
}

string Section::getSectionName(){
	return this->_sectionName;
}
string Section::getSectionType(){
	return this->_sectionType;
}
string Section::getDescription(){
	return this->_description;
}

Instructor* Section::getInstructor(){
	return this->_instructor;
}
Week* Section::getWeek(){
	return this->_daysOfWeek;
}
Semester* Section::getSemester(){
	return this->_dates;
}
Location* Section::getBuilding(){
	return this->_bulding;
}
