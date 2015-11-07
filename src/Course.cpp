#include "Course.hpp"
#include "Section.hpp"
#include "SectionCombo.hpp"
#include <vector>
Course::Course(std::string department, std::string courseNumber){
	this->_department = department;
	this->_courseNumber = courseNumber;

	this->_syncd = false;
}

Course::~Course(){
	for(std::vector<Section*>::const_iterator it = this->_sections.begin(); it != this->_sections.end(); it++){
		delete *it;
	}

	for(std::vector<SectionCombo*>::const_iterator it = this->_combos.begin(); it != this->_combos.end(); it++){
		delete *it;
	}
}

std::string Course::getDepartment() const{
	return this->_department;
}

std::string Course::getCourseNumber() const{
	return this->_courseNumber;
}

std::vector<Section*> Course::getSections() const{
	return this->_sections;
}

std::vector<SectionCombo*> Course::getCombos() const{
	return this->_combos;
}

bool Course::isSyncd() const{
	return this->_syncd;
}

bool Course::generateCombo(){
	return false;
}

void Course::setDepartment(std::string department){
	this->_department = department;
}

void Course::setCourseNumber(std::string number){
	this->_courseNumber = number;
}

void Course::addSection(Section* section){
	this->_sections.push_back(section);
}

