#include "Section.hpp"
#include "Instructor.hpp"
#include "Week.hpp"
#include "Semester.hpp"
#include "Location.hpp"
#include <string>

/*
 * ======================================================
 * Object Creation
 * ======================================================
 */

Section::Section(){
	this->_sectionName = "";
	this->_sectionType = "";
	this->_description = "";
	this->_crn = "";

	this->_instructor 	= NULL;
	this->_daysOfWeek 	= NULL;
	this->_dates 		= NULL;
	this->_bulding 		= NULL;
}

Section::~Section(){
	delete this->_instructor;
	delete this->_daysOfWeek;
	delete this->_dates;
	delete this->_bulding;
}

Section::Section(const Section& copy){
	this->_sectionName = copy._sectionName;
	this->_sectionType = copy._sectionType;
	this->_description = copy._description;
	this->_crn 		   = copy._crn;

	*(this->_instructor) = *(copy._instructor);
	*(this->_daysOfWeek) = *(copy._daysOfWeek);
	*(this->_dates) 	 = *(copy._dates);
	*(this->_bulding)	 = *(copy._bulding);
}

Section& Section::operator=(const Section& copy){
	this->_sectionName = copy._sectionName;
	this->_sectionType = copy._sectionType;
	this->_description = copy._description;
	this->_crn         = copy._crn;

	*(this->_instructor) = *(copy._instructor);
	*(this->_daysOfWeek) = *(copy._daysOfWeek);
	*(this->_dates) 	 = *(copy._dates);
	*(this->_bulding)	 = *(copy._bulding);

	return *this;

}

/*
 * ======================================================
 * Setters
 * ======================================================
 */


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

void Section::setCRN(std::string crn){
	this->_crn = crn;
}

/*
 * ======================================================
 * Getters
 * ======================================================
 */


string Section::getSectionName() const{
	return this->_sectionName;
}

string Section::getSectionType() const{
	return this->_sectionType;
}

string Section::getDescription() const{
	return this->_description;
}

Section::Instructor* Section::getInstructor() const{
	return this->_instructor;
}

Section::Week* Section::getWeek() const{
	return this->_daysOfWeek;
}

Section::Semester* Section::getSemester() const{
	return this->_dates;
}

Section::Location* Section::getBuilding() const{
	return this->_bulding;
}

std::string Section::getCRN() const {
	return this->_crn;
}


std::ostream& operator<<(std::ostream& os, const Section& section){
	os << section.getSectionName() << std::endl;
	os << section.getSectionType() << std::endl;
	os << section.getDescription() << std::endl;
	os << section.getCRN() << std::endl;
	
	os << "=== Instructor ==="<< std::endl;
	os << *section.getInstructor() << std::endl;
	
	os << "=== Week ==="<< std::endl;
	os << *section.getWeek() << std::endl;

	os << "=== Semester ==="<< std::endl;
	os << *section.getSemester() << std::endl;

	os << "=== Location ==="<< std::endl;
	os << *section.getBuilding() << std::endl;

	return os;
}
