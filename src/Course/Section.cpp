#include "Course/Section.hpp"

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

	this->_daysOfWeek 	= NULL;
	this->_dates 		= NULL;
	this->_bulding 		= NULL;
}

Section::~Section(){
	for (auto instructor : this->_instructor) {
        delete instructor;
    }
	delete this->_daysOfWeek;
	delete this->_dates;
	delete this->_bulding;
}

Section::Section(const Section& copy){
	this->_sectionName = copy._sectionName;
	this->_sectionType = copy._sectionType;
	this->_description = copy._description;
	this->_crn 		   = copy._crn;

    this->_instructor = copy._instructor;
    
    *(this->_daysOfWeek) = *(copy._daysOfWeek);
	*(this->_dates) 	 = *(copy._dates);
	*(this->_bulding)	 = *(copy._bulding);
}

Section& Section::operator=(const Section& copy){
	this->_sectionName = copy._sectionName;
	this->_sectionType = copy._sectionType;
	this->_description = copy._description;
	this->_crn         = copy._crn;

    this->_instructor = copy._instructor;

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


void Section::setSectionName(std::string sectionName){
	this->_sectionName = sectionName;
}

void Section::setSectionType(std::string sectionType){
	this->_sectionType = sectionType;
}

void Section::setDescription(std::string description){
	this->_description = description;
}

void Section::setInstructor(Instructor* instructor){
	this->_instructor.push_back(instructor);
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


std::string Section::getSectionName() const{
	return this->_sectionName;
}

std::string Section::getSectionType() const{
	return this->_sectionType;
}

std::string Section::getDescription() const{
	return this->_description;
}

std::vector<Instructor*>   Section::getInstructor() const{
	return this->_instructor;
}

Week* Section::getWeek() const{
	return this->_daysOfWeek;
}

Semester* Section::getSemester() const{
	return this->_dates;
}

Location* Section::getBuilding() const{
	return this->_bulding;
}

std::string Section::getCRN() const {
	return this->_crn;
}


std::ostream& operator<<(std::ostream& os, const Section& section){
	os << "====== Section =====" << std::endl;
	os << section.getSectionName() << std::endl;
	os << section.getSectionType() << std::endl;
	os << section.getDescription() << std::endl;
	os << section.getCRN() << std::endl;

	os << "=== Instructor(s) ==="<< std::endl;
    for (auto instructor : section.getInstructor()) {
        os << *instructor;
    }

	os << "=== Week ==="<< std::endl;
	os << *section.getWeek();

	os << "=== Semester ==="<< std::endl;
	os << *section.getSemester();

	os << "=== Location ==="<< std::endl;
	os << *section.getBuilding();

	return os;
}

/*
 * ======================================================
 * helper functions
 * ======================================================
 */
bool Section::overlap(Section* a, Section* b){
	Week* weekA = a->getWeek();
	Week* weekB = b->getWeek();
	for(int d = 0; d != Week::TIMESINDAY; d++){
		Time* timeAStart = weekA->getTimes((Week::Day)d, true);
		Time* timeAEnd	 = weekA->getTimes((Week::Day)d, false);
		Time* timeBStart = weekB->getTimes((Week::Day)d, true);
		Time* timeBEnd	 = weekB->getTimes((Week::Day)d, false);
		
		if(timeAStart == NULL or timeBStart == NULL){
			continue;
		}

		if(not Time::before(timeAEnd, timeBStart) and not Time::before(timeBEnd, timeAStart)){
			return true;
		}
	}
	return false;
}
